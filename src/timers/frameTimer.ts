import { INFINITY } from '../utils/constants';
import { CalibratorSettings, toMilliseconds } from './calibrator';

export function createFramePhases(
  settings: CalibratorSettings,
  preTimer: number,
  targetFrame: number,
  calibration: number,
): number[] {
  return [preTimer, createFramePhase(settings, targetFrame, calibration)];
}

export function createFramePhase(
  settings: CalibratorSettings,
  targetFrame: number,
  calibration: number,
): number {
  return toMilliseconds(settings, targetFrame) + calibration;
}

export function calibrateFrame(
  settings: CalibratorSettings,
  targetFrame: number,
  frameHit: number,
): number {
  return toMilliseconds(settings, targetFrame - frameHit);
}

export function createVariableFramePhases(preTimer: number): number[] {
  return [preTimer, INFINITY];
}
