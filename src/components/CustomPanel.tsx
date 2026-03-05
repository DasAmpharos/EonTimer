import { useState, useCallback, useMemo, forwardRef, useImperativeHandle } from 'react';
import { useSettingsStore } from '../store';
import { CustomUnit } from '../utils/types';
import { INT_MAX, INT_MIN } from '../utils/constants';
import { FormField } from './common/FormField';
import { IntInput } from './common/IntInput';
import { FloatInput } from './common/FloatInput';
import { EnumSelect } from './common/EnumSelect';
import { CalibratorSettings } from '../timers/calibrator';
import { createCustomPhases, calibrateCustomPhase, type CustomPhase } from '../timers/customTimer';
import type { TimerPanelHandle } from './Gen5Panel';

const CUSTOM_UNITS = Object.values(CustomUnit) as CustomUnit[];

interface PhaseState {
  unit: CustomUnit;
  target: number;
  calibration: number;
  hit: number | null;
}

interface CustomPanelProps {
  onPhasesChange: () => void;
  disabled?: boolean;
}

export const CustomPanel = forwardRef<TimerPanelHandle, CustomPanelProps>(
  function CustomPanel({ onPhasesChange, disabled }, ref) {
    const custom = useSettingsStore((s) => s.custom);
    const timer = useSettingsStore((s) => s.timer);
    const setCustomPhases = useSettingsStore((s) => s.setCustomPhases);

    const [phases, setPhases] = useState<PhaseState[]>(() =>
      custom.phases.length > 0
        ? custom.phases.map((p) => ({ ...p, hit: null }))
        : [{ unit: CustomUnit.MILLISECONDS, target: 0, calibration: 0, hit: null }],
    );

    const calSettings: CalibratorSettings = useMemo(() => ({
      console: timer.console,
      customFramerate: timer.customFramerate,
      precisionCalibration: timer.precisionCalibration,
      minimumLength: timer.minimumLength * 1000,
    }), [timer.console, timer.customFramerate, timer.precisionCalibration, timer.minimumLength]);

    const persist = useCallback(
      (updated: PhaseState[]) => {
        setCustomPhases(updated.map(({ unit, target, calibration }) => ({ unit, target, calibration })));
      },
      [setCustomPhases],
    );

    const createPhasesCalc = useCallback(() => {
      const models: CustomPhase[] = phases.map(({ unit, target, calibration }) => ({ unit, target, calibration }));
      return createCustomPhases(calSettings, models);
    }, [calSettings, phases]);

    const canCalibrate = useCallback(() => {
      return phases.some((p) => p.hit !== null);
    }, [phases]);

    const calibrate = useCallback(() => {
      const updated = phases.map((p) => {
        if (p.hit === null) return p;
        const delta = calibrateCustomPhase(calSettings, p, p.hit);
        return { ...p, calibration: p.calibration + delta, hit: null };
      });
      setPhases(updated);
      persist(updated);
      setTimeout(onPhasesChange, 0);
    }, [phases, calSettings, persist, onPhasesChange]);

    const reset = useCallback(() => {
      const initial: PhaseState[] = [{ unit: CustomUnit.MILLISECONDS, target: 0, calibration: 0, hit: null }];
      setPhases(initial);
      persist(initial);
    }, [persist]);

    useImperativeHandle(ref, () => ({ createPhases: createPhasesCalc, calibrate, canCalibrate, reset }), [createPhasesCalc, calibrate, canCalibrate, reset]);

    const updatePhase = useCallback(
      (index: number, patch: Partial<PhaseState>) => {
        setPhases((prev) => {
          const updated = [...prev];
          updated[index] = { ...updated[index], ...patch };
          persist(updated);
          return updated;
        });
        setTimeout(onPhasesChange, 0);
      },
      [persist, onPhasesChange],
    );

    const addPhase = useCallback(() => {
      setPhases((prev) => {
        const updated = [...prev, { unit: CustomUnit.MILLISECONDS, target: 0, calibration: 0, hit: null }];
        persist(updated);
        return updated;
      });
      setTimeout(onPhasesChange, 0);
    }, [persist, onPhasesChange]);

    const removePhase = useCallback(
      (index: number) => {
        setPhases((prev) => {
          if (prev.length <= 1) return prev;
          const updated = prev.filter((_, i) => i !== index);
          persist(updated);
          return updated;
        });
        setTimeout(onPhasesChange, 0);
      },
      [persist, onPhasesChange],
    );

    return (
      <div className="timer-panel custom-panel">
        <div className="panel-scroll-area custom-scroll">
          {phases.map((phase, index) => (
            <div key={index} className="custom-phase-row">
              <span className="custom-phase-index">{index + 1}.</span>
              <div className="custom-phase-group">
                <div className="custom-phase-fields">
                  <FormField label="Unit">
                    <div className="custom-unit-row">
                      <EnumSelect
                        values={CUSTOM_UNITS}
                        value={phase.unit}
                        onChange={(v) => updatePhase(index, { unit: v })}
                        disabled={disabled}
                      />
                      <button
                        className="btn btn-danger btn-icon"
                        onClick={() => removePhase(index)}
                        disabled={disabled || phases.length <= 1}
                        title="Remove"
                      >
                        ✕
                      </button>
                    </div>
                  </FormField>
                  <FormField label="Target">
                    <IntInput
                      value={phase.target}
                      onChange={(v) => updatePhase(index, { target: v ?? 0 })}
                      min={0}
                      max={INT_MAX}
                      disabled={disabled}
                      radix={phase.unit === CustomUnit.HEX ? 16 : 10}
                    />
                  </FormField>
                  <FormField label="Calibration">
                    <FloatInput
                      value={phase.calibration}
                      onChange={(v) => updatePhase(index, { calibration: v })}
                      min={INT_MIN}
                      max={INT_MAX}
                      disabled={disabled}
                    />
                  </FormField>
                  <FormField label="Hit">
                    <IntInput
                      value={phase.hit}
                      onChange={(v) => updatePhase(index, { hit: v })}
                      min={0}
                      max={INT_MAX}
                      allowBlank
                      disabled={disabled}
                      radix={phase.unit === CustomUnit.HEX ? 16 : 10}
                    />
                  </FormField>
                </div>
              </div>
            </div>
          ))}
        </div>
        <button className="btn btn-success btn-add" onClick={addPhase} disabled={disabled}>
          ＋
        </button>
      </div>
    );
  },
);
