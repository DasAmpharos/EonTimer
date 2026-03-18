import { toMinimumLength, MINIMUM_LENGTH } from '../utils/constants';

export function createSecondPhases(
  targetSecond: number,
  calibration: number,
  minimumLength: number = MINIMUM_LENGTH,
): number[] {
  return [toMinimumLength(targetSecond * 1000 + calibration + 200, minimumLength)];
}

export function calibrateSecond(targetSecond: number, secondHit: number): number {
  if (secondHit < targetSecond) {
    return (targetSecond - secondHit) * 1000 - 500;
  } else if (secondHit > targetSecond) {
    return (targetSecond - secondHit) * 1000 + 500;
  }
  return 0;
}
