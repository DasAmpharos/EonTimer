import { useState, useCallback, useMemo, forwardRef, useImperativeHandle } from 'react';
import { useSettingsStore, DEFAULT_GEN4 } from '../store';
import { INT_MAX, INT_MIN } from '../utils/constants';
import { FormField } from './common/FormField';
import { IntInput } from './common/IntInput';
import { CalibratorSettings } from '../timers/calibrator';
import { createGen4Phases, calibrateGen4 } from '../timers/gen4Timer';
import type { TimerPanelHandle } from './Gen5Panel';

interface Gen4PanelProps {
  onPhasesChange: () => void;
  disabled?: boolean;
}

export const Gen4Panel = forwardRef<TimerPanelHandle, Gen4PanelProps>(
  function Gen4Panel({ onPhasesChange, disabled }, ref) {
    const gen4 = useSettingsStore((s) => s.gen4);
    const timer = useSettingsStore((s) => s.timer);
    const updateGen4 = useSettingsStore((s) => s.updateGen4);

    const [delayHit, setDelayHit] = useState<number | null>(null);

    const calSettings: CalibratorSettings = useMemo(() => ({
      console: timer.console,
      customFramerate: timer.customFramerate,
      precisionCalibration: timer.precisionCalibration,
      minimumLength: timer.minimumLength * 1000,
    }), [timer.console, timer.customFramerate, timer.precisionCalibration, timer.minimumLength]);

    const createPhases = useCallback(() => {
      return createGen4Phases(calSettings, gen4);
    }, [calSettings, gen4]);

    const canCalibrate = useCallback(() => delayHit !== null, [delayHit]);

    const calibrate = useCallback(() => {
      if (delayHit === null) return;
      const delta = calibrateGen4(calSettings, gen4, delayHit);
      updateGen4({ calibratedDelay: gen4.calibratedDelay + delta });
      setDelayHit(null);
    }, [calSettings, gen4, delayHit, updateGen4]);

    const reset = useCallback(() => {
      updateGen4({ ...DEFAULT_GEN4 });
      setDelayHit(null);
    }, [updateGen4]);

    useImperativeHandle(ref, () => ({ createPhases, calibrate, canCalibrate, reset }), [createPhases, calibrate, canCalibrate, reset]);

    const update = useCallback(
      (patch: Partial<typeof gen4>) => {
        updateGen4(patch);
        setTimeout(onPhasesChange, 0);
      },
      [updateGen4, onPhasesChange],
    );

    return (
      <div className="timer-panel gen4-panel">
        <div className="panel-scroll-area">
          <div className="panel-form-group">
            <FormField label="Calibrated Delay">
              <IntInput value={gen4.calibratedDelay} onChange={(v) => update({ calibratedDelay: v ?? 0 })} min={INT_MIN} max={INT_MAX} disabled={disabled} />
            </FormField>
            <FormField label="Calibrated Second">
              <IntInput value={gen4.calibratedSecond} onChange={(v) => update({ calibratedSecond: v ?? 0 })} min={0} max={INT_MAX} disabled={disabled} />
            </FormField>
            <FormField label="Target Delay">
              <IntInput value={gen4.targetDelay} onChange={(v) => update({ targetDelay: v ?? 0 })} min={0} max={INT_MAX} disabled={disabled} />
            </FormField>
            <FormField label="Target Second">
              <IntInput value={gen4.targetSecond} onChange={(v) => update({ targetSecond: v ?? 0 })} min={0} max={INT_MAX} disabled={disabled} />
            </FormField>
          </div>
        </div>
        <div className="panel-hit-fields">
          <FormField label="Delay Hit">
            <IntInput value={delayHit} onChange={setDelayHit} min={0} max={INT_MAX} allowBlank placeholder="Enter delay hit" disabled={disabled} />
          </FormField>
        </div>
      </div>
    );
  },
);
