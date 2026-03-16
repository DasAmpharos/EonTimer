import { useState, useCallback, useMemo, forwardRef, useImperativeHandle } from 'react';
import { useSettingsStore, DEFAULT_GEN5 } from '../store';
import { Gen5Mode } from '../utils/types';
import { INT_MAX, INT_MIN } from '../utils/constants';
import { FormField } from './common/FormField';
import { IntInput } from './common/IntInput';
import { EnumSelect } from './common/EnumSelect';
import { CalibratorSettings } from '../timers/calibrator';
import { createGen5Phases, calibrateGen5, getGen5MinutesBeforeTarget } from '../timers/gen5Timer';
import type { TimerPanelHandle } from './timerPanel';
export type { TimerPanelData, TimerPanelHandle } from './timerPanel';

const GEN5_MODES = Object.values(Gen5Mode) as Gen5Mode[];

interface Gen5PanelProps {
  onPhasesChange: () => void;
  disabled?: boolean;
}

export const Gen5Panel = forwardRef<TimerPanelHandle, Gen5PanelProps>(function Gen5Panel(
  { onPhasesChange, disabled },
  ref,
) {
  const gen5 = useSettingsStore((s) => s.gen5);
  const timer = useSettingsStore((s) => s.timer);
  const updateGen5 = useSettingsStore((s) => s.updateGen5);

  const [delayHit, setDelayHit] = useState<number | null>(null);
  const [secondHit, setSecondHit] = useState<number | null>(null);
  const [advancesHit, setAdvancesHit] = useState<number | null>(null);

  const calSettings: CalibratorSettings = useMemo(
    () => ({
      console: timer.console,
      customFramerate: timer.customFramerate,
      precisionCalibration: timer.precisionCalibration,
      minimumLength: timer.minimumLength * 1000,
    }),
    [timer.console, timer.customFramerate, timer.precisionCalibration, timer.minimumLength],
  );

  const createDisplayData = useCallback(() => {
    const phases = createGen5Phases(calSettings, gen5);
    return {
      phases,
      minutesBeforeTarget: getGen5MinutesBeforeTarget(calSettings, gen5),
    };
  }, [calSettings, gen5]);

  const canCalibrate = useCallback(() => {
    switch (gen5.mode) {
      case Gen5Mode.STANDARD:
        return secondHit !== null;
      case Gen5Mode.C_GEAR:
        return delayHit !== null;
      case Gen5Mode.ENTRALINK:
        return delayHit !== null && secondHit !== null;
      case Gen5Mode.ENTRALINK_PLUS:
        return delayHit !== null && secondHit !== null && advancesHit !== null;
    }
  }, [gen5.mode, delayHit, secondHit, advancesHit]);

  const calibrate = useCallback(() => {
    if (!canCalibrate()) return;
    const result = calibrateGen5(calSettings, gen5, { delayHit, secondHit, advancesHit });
    updateGen5({
      calibration: gen5.calibration + result.calibrationDelta,
      entralinkCalibration: gen5.entralinkCalibration + result.entralinkCalibrationDelta,
      frameCalibration: gen5.frameCalibration + result.frameCalibrationDelta,
    });
    setDelayHit(null);
    setSecondHit(null);
    setAdvancesHit(null);
  }, [calSettings, gen5, delayHit, secondHit, advancesHit, updateGen5, canCalibrate]);

  const reset = useCallback(() => {
    updateGen5({ ...DEFAULT_GEN5 });
    setDelayHit(null);
    setSecondHit(null);
    setAdvancesHit(null);
  }, [updateGen5]);

  useImperativeHandle(ref, () => ({ createDisplayData, calibrate, canCalibrate, reset }), [
    createDisplayData,
    calibrate,
    canCalibrate,
    reset,
  ]);

  const update = useCallback(
    (patch: Partial<typeof gen5>) => {
      updateGen5(patch);
      setTimeout(onPhasesChange, 0);
    },
    [updateGen5, onPhasesChange],
  );

  const mode = gen5.mode;
  const showTargetDelay = mode !== Gen5Mode.STANDARD;
  const showTargetAdvances = mode === Gen5Mode.ENTRALINK_PLUS;
  const showEntralinkCalibration = mode === Gen5Mode.ENTRALINK || mode === Gen5Mode.ENTRALINK_PLUS;
  const showFrameCalibration = mode === Gen5Mode.ENTRALINK_PLUS;
  const showDelayHit = mode !== Gen5Mode.STANDARD;
  const showSecondHit = mode !== Gen5Mode.C_GEAR;
  const showAdvancesHit = mode === Gen5Mode.ENTRALINK_PLUS;

  return (
    <div className="timer-panel gen5-panel">
      <FormField label="Mode">
        <EnumSelect
          values={GEN5_MODES}
          value={mode}
          onChange={(v) => update({ mode: v })}
          disabled={disabled}
        />
      </FormField>
      <div className="panel-scroll-area">
        <div className="panel-form-group">
          <FormField label="Target Delay" visible={showTargetDelay}>
            <IntInput
              value={gen5.targetDelay}
              onChange={(v) => update({ targetDelay: v ?? 0 })}
              min={0}
              max={INT_MAX}
              disabled={disabled}
            />
          </FormField>
          <FormField label="Target Second">
            <IntInput
              value={gen5.targetSecond}
              onChange={(v) => update({ targetSecond: v ?? 0 })}
              min={0}
              max={INT_MAX}
              disabled={disabled}
            />
          </FormField>
          <FormField label="Target Advances" visible={showTargetAdvances}>
            <IntInput
              value={gen5.targetAdvances}
              onChange={(v) => update({ targetAdvances: v ?? 0 })}
              min={0}
              max={INT_MAX}
              disabled={disabled}
            />
          </FormField>
          <FormField label="Calibration">
            <IntInput
              value={gen5.calibration}
              onChange={(v) => update({ calibration: v ?? 0 })}
              min={INT_MIN}
              max={INT_MAX}
              disabled={disabled}
            />
          </FormField>
          <FormField label="Entralink Calibration" visible={showEntralinkCalibration}>
            <IntInput
              value={gen5.entralinkCalibration}
              onChange={(v) => update({ entralinkCalibration: v ?? 0 })}
              min={INT_MIN}
              max={INT_MAX}
              disabled={disabled}
            />
          </FormField>
          <FormField label="Frame Calibration" visible={showFrameCalibration}>
            <IntInput
              value={gen5.frameCalibration}
              onChange={(v) => update({ frameCalibration: v ?? 0 })}
              min={INT_MIN}
              max={INT_MAX}
              disabled={disabled}
            />
          </FormField>
        </div>
      </div>
      <div className="panel-hit-fields">
        <FormField label="Delay Hit" visible={showDelayHit}>
          <IntInput
            value={delayHit}
            onChange={setDelayHit}
            min={0}
            max={INT_MAX}
            allowBlank
            placeholder="Enter delay hit"
            disabled={disabled}
          />
        </FormField>
        <FormField label="Second Hit" visible={showSecondHit}>
          <IntInput
            value={secondHit}
            onChange={setSecondHit}
            min={0}
            max={INT_MAX}
            allowBlank
            placeholder="Enter second hit"
            disabled={disabled}
          />
        </FormField>
        <FormField label="Advances Hit" visible={showAdvancesHit}>
          <IntInput
            value={advancesHit}
            onChange={setAdvancesHit}
            min={0}
            max={INT_MAX}
            allowBlank
            placeholder="Enter advances hit"
            disabled={disabled}
          />
        </FormField>
      </div>
    </div>
  );
});
