import { CustomUnit } from '../utils/types';
import { CalibratorSettings, toMilliseconds } from './calibrator';

export interface CustomPhase {
  unit: CustomUnit;
  target: number;
  calibration: number;
}

export function createCustomPhases(settings: CalibratorSettings, phases: CustomPhase[]): number[] {
  return phases.map((phase) => {
    let value = phase.target;
    if (phase.unit === CustomUnit.ADVANCES || phase.unit === CustomUnit.HEX) {
      value = toMilliseconds(settings, value);
    }
    return value + phase.calibration;
  });
}

export function calibrateCustomPhase(
  settings: CalibratorSettings,
  phase: CustomPhase,
  hit: number,
): number {
  if (phase.unit !== CustomUnit.MILLISECONDS) {
    return toMilliseconds(settings, phase.target - hit);
  }
  return phase.target - hit;
}
