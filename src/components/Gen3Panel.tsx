import { useState, useCallback, useMemo, useEffect, useRef, forwardRef, useImperativeHandle } from 'react';
import { useSettingsStore, useAppStore, DEFAULT_GEN3 } from '../store';
import { Gen3Mode } from '../utils/types';
import { INT_MAX, INT_MIN } from '../utils/constants';
import { FormField } from './common/FormField';
import { IntInput } from './common/IntInput';
import { FloatInput } from './common/FloatInput';
import { EnumSelect } from './common/EnumSelect';
import { CalibratorSettings } from '../timers/calibrator';
import { createGen3Phases, calibrateGen3, createFramePhase } from '../timers/gen3Timer';
import type { TimerPanelHandle } from './timerPanel';

const GEN3_MODES = Object.values(Gen3Mode) as Gen3Mode[];

interface Gen3PanelProps {
  onPhasesChange: () => void;
  disabled?: boolean;
}

export const Gen3Panel = forwardRef<TimerPanelHandle, Gen3PanelProps>(
  function Gen3Panel({ onPhasesChange, disabled }, ref) {
    const gen3 = useSettingsStore((s) => s.gen3);
    const timer = useSettingsStore((s) => s.timer);
    const updateGen3 = useSettingsStore((s) => s.updateGen3);
    const running = useAppStore((s) => s.running);
    const setPhase = useAppStore((s) => s.setPhase);

    const [frameHit, setFrameHit] = useState<number | null>(null);
    const [targetFrameLocked, setTargetFrameLocked] = useState(false);

    const prevRunning = useRef(running);
    useEffect(() => {
      if (prevRunning.current && !running) {
        setTargetFrameLocked(false);
      }
      prevRunning.current = running;
    }, [running]);

    const calSettings: CalibratorSettings = useMemo(() => ({
      console: timer.console,
      customFramerate: timer.customFramerate,
      precisionCalibration: timer.precisionCalibration,
      minimumLength: timer.minimumLength * 1000,
    }), [timer.console, timer.customFramerate, timer.precisionCalibration, timer.minimumLength]);

    const createDisplayData = useCallback(() => {
      return {
        phases: createGen3Phases(calSettings, gen3),
        minutesBeforeTarget: null,
      };
    }, [calSettings, gen3]);

    const canCalibrate = useCallback(() => frameHit !== null, [frameHit]);

    const calibrate = useCallback(() => {
      if (frameHit === null) return;
      const offset = calibrateGen3(calSettings, gen3, frameHit);
      updateGen3({ calibration: gen3.calibration + offset });
      setFrameHit(null);
    }, [calSettings, gen3, frameHit, updateGen3]);

    const reset = useCallback(() => {
      updateGen3({ ...DEFAULT_GEN3 });
      setFrameHit(null);
      setTargetFrameLocked(false);
    }, [updateGen3]);

    useImperativeHandle(ref, () => ({ createDisplayData, calibrate, canCalibrate, reset }), [createDisplayData, calibrate, canCalibrate, reset]);

    const update = useCallback(
      (patch: Partial<typeof gen3>) => {
        updateGen3(patch);
        setTimeout(onPhasesChange, 0);
      },
      [updateGen3, onPhasesChange],
    );

    const handleSetTargetFrame = useCallback(() => {
      const phase = createFramePhase(calSettings, gen3.targetFrame, gen3.calibration);
      setPhase(1, phase);
      setTargetFrameLocked(true);
    }, [calSettings, gen3.targetFrame, gen3.calibration, setPhase]);

    const mode = gen3.mode;
    const isVariableTarget = mode === Gen3Mode.VARIABLE_TARGET;

    return (
      <div className="timer-panel gen3-panel">
        <FormField label="Mode">
          <EnumSelect values={GEN3_MODES} value={mode} onChange={(v) => { update({ mode: v }); setTargetFrameLocked(false); }} disabled={disabled} />
        </FormField>
        <div className="panel-scroll-area">
          <div className="panel-form-group">
            <FormField label="Pre-Timer">
              <IntInput value={gen3.preTimer} onChange={(v) => update({ preTimer: v ?? 0 })} min={0} max={INT_MAX} disabled={disabled} />
            </FormField>
            <FormField label="Target Frame">
              <IntInput
                value={gen3.targetFrame}
                onChange={(v) => update({ targetFrame: v ?? 0 })}
                min={0}
                max={INT_MAX}
                disabled={isVariableTarget ? targetFrameLocked : disabled}
              />
            </FormField>
            <FormField label="Calibration">
              <FloatInput value={gen3.calibration} onChange={(v) => update({ calibration: v })} min={INT_MIN} max={INT_MAX} disabled={disabled} />
            </FormField>
            {isVariableTarget && (
              <button
                className="btn"
                style={{ width: '100%' }}
                onClick={handleSetTargetFrame}
                disabled={!running || targetFrameLocked}
              >
                Set Target Frame
              </button>
            )}
          </div>
        </div>
        <div className="panel-hit-fields">
          <FormField label="Frame Hit">
            <IntInput value={frameHit} onChange={setFrameHit} min={0} max={INT_MAX} allowBlank placeholder="Enter frame hit" disabled={disabled} />
          </FormField>
        </div>
      </div>
    );
  },
);
