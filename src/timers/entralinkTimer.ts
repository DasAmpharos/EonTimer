import { CalibratorSettings } from './calibrator';
import { createDelayPhases, calibrateDelay } from './delayTimer';

const ENTRALINK_FRAME_RATE = 0.837148929;

export function createEntralinkPhases(
  settings: CalibratorSettings,
  targetDelay: number,
  targetSecond: number,
  calibration: number,
  entralinkCalibration: number,
): number[] {
  const durations = createDelayPhases(settings, targetDelay, targetSecond, calibration);
  durations[0] += 250;
  durations[1] -= entralinkCalibration;
  return durations;
}

export function calibrateEntralinkDelay(
  settings: CalibratorSettings,
  targetDelay: number,
  delayHit: number,
): number {
  return calibrateDelay(settings, targetDelay, delayHit);
}

export function createEnhancedEntralinkPhases(
  settings: CalibratorSettings,
  targetDelay: number,
  targetSecond: number,
  targetAdvances: number,
  calibration: number,
  entralinkCalibration: number,
  frameCalibration: number,
): number[] {
  const phases = createEntralinkPhases(settings, targetDelay, targetSecond, calibration, entralinkCalibration);
  phases.push((targetAdvances / ENTRALINK_FRAME_RATE) * 1000 + frameCalibration);
  return phases;
}

export function calibrateEntralinkAdvances(targetAdvances: number, advancesHit: number): number {
  return ((targetAdvances - advancesHit) / ENTRALINK_FRAME_RATE) * 1000;
}
