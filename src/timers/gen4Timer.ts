import { getMinutesBeforeTarget } from '../utils/constants';
import {
  CalibratorSettings,
  createCalibration,
  toDelays,
  toMilliseconds,
} from './calibrator';
import { createDelayPhases, calibrateDelay } from './delayTimer';

export interface Gen4Model {
  targetDelay: number;
  targetSecond: number;
  calibratedDelay: number;
  calibratedSecond: number;
}

function getCalibration(settings: CalibratorSettings, model: Gen4Model): number {
  return createCalibration(settings, model.calibratedDelay, model.calibratedSecond);
}

export function createGen4Phases(settings: CalibratorSettings, model: Gen4Model): number[] {
  return createDelayPhases(settings, model.targetDelay, model.targetSecond, getCalibration(settings, model));
}

export function getGen4MinutesBeforeTarget(settings: CalibratorSettings, model: Gen4Model): number {
  return getMinutesBeforeTarget(createDelayPhases(settings, model.targetDelay, model.targetSecond, 0));
}

export function calibrateGen4(
  settings: CalibratorSettings,
  model: Gen4Model,
  delayHit: number,
): number {
  if (delayHit > 0) {
    return toDelays(
      settings,
      calibrateDelay(settings, model.targetDelay, delayHit),
    );
  }
  return 0;
}
