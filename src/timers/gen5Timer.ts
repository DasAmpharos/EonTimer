import { Gen5Mode } from '../utils/types';
import {
  CalibratorSettings,
  calibrateToDelays,
  calibrateToMilliseconds,
  toDelays,
} from './calibrator';
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
      // Standard mode is second-based; calibration is stored in ms, no frame conversion needed
      return createSecondPhases(model.targetSecond, model.calibration, settings.minimumLength);
    case Gen5Mode.C_GEAR:
      return createDelayPhases(settings, model.targetDelay, model.targetSecond, calibration);
    case Gen5Mode.ENTRALINK:
      return createEntralinkPhases(
        settings, model.targetDelay, model.targetSecond, calibration, entralinkCalibration,
      );
    case Gen5Mode.ENTRALINK_PLUS:
      return createEnhancedEntralinkPhases(
        settings, model.targetDelay, model.targetSecond, model.targetAdvances,
        calibration, entralinkCalibration, model.frameCalibration,
      );
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
        // Standard mode calibration is in ms; return directly without converting to delays
        calibrationDelta = calibrateSecond(model.targetSecond, input.secondHit);
      }
      break;
    case Gen5Mode.C_GEAR:
      if (input.delayHit !== null) {
        calibrationDelta = calibrateToDelays(
          settings, calibrateDelay(settings, model.targetDelay, input.delayHit),
        );
      }
      break;
    case Gen5Mode.ENTRALINK:
    case Gen5Mode.ENTRALINK_PLUS:
      if (input.secondHit !== null && input.secondHit !== model.targetSecond) {
        calibrationDelta = calibrateToDelays(
          settings, calibrateSecond(model.targetSecond, input.secondHit),
        );
      }
      if (input.delayHit !== null && input.delayHit !== model.targetDelay) {
        entralinkCalibrationDelta = calibrateToDelays(
          settings, calibrateEntralinkDelay(settings, model.targetDelay, input.delayHit),
        );
      }
      if (model.mode === Gen5Mode.ENTRALINK_PLUS) {
        if (input.advancesHit !== null && input.advancesHit !== model.targetAdvances) {
          frameCalibrationDelta = calibrateEntralinkAdvances(model.targetAdvances, input.advancesHit);
        }
      }
      break;
  }

  return { calibrationDelta, entralinkCalibrationDelta, frameCalibrationDelta };
}
