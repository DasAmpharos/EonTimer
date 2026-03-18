import { Gen5Mode } from '../utils/types';
import { getMinutesBeforeTarget } from '../utils/constants';
import { CalibratorSettings, calibrateToDelays, calibrateToMilliseconds } from './calibrator';
import { createSecondPhases, calibrateSecond } from './secondTimer';
import { createDelayPhases, calibrateDelay } from './delayTimer';
import {
  createEntralinkPhases,
  createEnhancedEntralinkPhases,
  calibrateEntralinkDelay,
  calibrateEntralinkAdvances,
} from './entralinkTimer';

export interface Gen5Model {
  mode: Gen5Mode;
  calibration: number;
  frameCalibration: number;
  entralinkCalibration: number;
  targetDelay: number;
  targetSecond: number;
  targetAdvances: number;
}

export function createGen5Phases(settings: CalibratorSettings, model: Gen5Model): number[] {
  const calibration = calibrateToMilliseconds(settings, model.calibration);
  const entralinkCalibration = calibrateToMilliseconds(settings, model.entralinkCalibration);

  switch (model.mode) {
    case Gen5Mode.STANDARD:
      return createSecondPhases(model.targetSecond, calibration, settings.minimumLength);
    case Gen5Mode.C_GEAR:
      return createDelayPhases(settings, model.targetDelay, model.targetSecond, calibration);
    case Gen5Mode.ENTRALINK:
      return createEntralinkPhases(
        settings,
        model.targetDelay,
        model.targetSecond,
        calibration,
        entralinkCalibration,
      );
    case Gen5Mode.ENTRALINK_PLUS:
      return createEnhancedEntralinkPhases(
        settings,
        model.targetDelay,
        model.targetSecond,
        model.targetAdvances,
        calibration,
        entralinkCalibration,
        model.frameCalibration,
      );
  }
}

export function getGen5MinutesBeforeTarget(settings: CalibratorSettings, model: Gen5Model): number {
  switch (model.mode) {
    case Gen5Mode.STANDARD:
      return getMinutesBeforeTarget(
        createSecondPhases(model.targetSecond, 0, settings.minimumLength),
      );
    case Gen5Mode.C_GEAR:
      return getMinutesBeforeTarget(
        createDelayPhases(settings, model.targetDelay, model.targetSecond, 0),
      );
    case Gen5Mode.ENTRALINK:
    case Gen5Mode.ENTRALINK_PLUS:
      return getMinutesBeforeTarget(
        createEntralinkPhases(settings, model.targetDelay, model.targetSecond, 0, 0),
      );
    default:
      model.mode satisfies never;
      return 0;
  }
}

export interface Gen5CalibrationInput {
  delayHit: number | null;
  secondHit: number | null;
  advancesHit: number | null;
}

export interface Gen5CalibrationResult {
  calibrationDelta: number;
  entralinkCalibrationDelta: number;
  frameCalibrationDelta: number;
}

export function calibrateGen5(
  settings: CalibratorSettings,
  model: Gen5Model,
  input: Gen5CalibrationInput,
): Gen5CalibrationResult {
  let calibrationDelta = 0;
  let entralinkCalibrationDelta = 0;
  let frameCalibrationDelta = 0;

  switch (model.mode) {
    case Gen5Mode.STANDARD:
      if (input.secondHit !== null) {
        calibrationDelta = calibrateToDelays(
          settings,
          calibrateSecond(model.targetSecond, input.secondHit),
        );
      }
      break;
    case Gen5Mode.C_GEAR:
      if (input.delayHit !== null) {
        calibrationDelta = calibrateToDelays(
          settings,
          calibrateDelay(settings, model.targetDelay, input.delayHit),
        );
      }
      break;
    case Gen5Mode.ENTRALINK:
    case Gen5Mode.ENTRALINK_PLUS:
      if (input.secondHit !== null && input.secondHit !== model.targetSecond) {
        calibrationDelta = calibrateToDelays(
          settings,
          calibrateSecond(model.targetSecond, input.secondHit),
        );
      }
      if (input.delayHit !== null && input.delayHit !== model.targetDelay) {
        entralinkCalibrationDelta = calibrateToDelays(
          settings,
          calibrateEntralinkDelay(settings, model.targetDelay, input.delayHit),
        );
      }
      if (model.mode === Gen5Mode.ENTRALINK_PLUS) {
        if (input.advancesHit !== null && input.advancesHit !== model.targetAdvances) {
          frameCalibrationDelta = calibrateEntralinkAdvances(
            model.targetAdvances,
            input.advancesHit,
          );
        }
      }
      break;
  }

  return { calibrationDelta, entralinkCalibrationDelta, frameCalibrationDelta };
}
