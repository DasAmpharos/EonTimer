import { toMinimumLength } from '../utils/constants';
import { CalibratorSettings, toMilliseconds } from './calibrator';
import { createSecondPhases } from './secondTimer';

const CLOSE_THRESHOLD = 167;
const UPDATE_FACTOR = 1.0;
const CLOSE_UPDATE_FACTOR = 0.75;

export function createDelayPhases(
  settings: CalibratorSettings,
  targetDelay: number,
  targetSecond: number,
  calibration: number,
): number[] {
  const phase1 = toMinimumLength(
    createSecondPhases(targetSecond, calibration)[0] - toMilliseconds(settings, targetDelay),
  );
  const phase2 = toMilliseconds(settings, targetDelay) - calibration;
  return [phase1, phase2];
}

export function calibrateDelay(
  settings: CalibratorSettings,
  targetDelay: number,
  delayHit: number,
): number {
  const delta = toMilliseconds(settings, delayHit) - toMilliseconds(settings, targetDelay);
  if (Math.abs(delta) <= CLOSE_THRESHOLD) {
    return CLOSE_UPDATE_FACTOR * delta;
  }
  return UPDATE_FACTOR * delta;
}
