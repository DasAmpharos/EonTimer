import { Gen3Mode } from '../utils/types';
import { CalibratorSettings } from './calibrator';
import {
  createFramePhases,
  createFramePhase,
  calibrateFrame,
  createVariableFramePhases,
} from './frameTimer';

export interface Gen3Model {
  mode: Gen3Mode;
  preTimer: number;
  targetFrame: number;
  calibration: number;
}

export function createGen3Phases(settings: CalibratorSettings, model: Gen3Model): number[] {
  switch (model.mode) {
    case Gen3Mode.STANDARD:
      return createFramePhases(settings, model.preTimer, model.targetFrame, model.calibration);
    case Gen3Mode.VARIABLE_TARGET:
      return createVariableFramePhases(model.preTimer);
  }
}

export function calibrateGen3(
  settings: CalibratorSettings,
  model: Gen3Model,
  frameHit: number,
): number {
  return calibrateFrame(settings, model.targetFrame, frameHit);
}

export { createFramePhase };
