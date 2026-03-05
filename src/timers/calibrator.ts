import {
  GBA_FRAMERATE,
  NDS_SLOT1_FRAMERATE,
  NDS_SLOT2_FRAMERATE,
} from '../utils/constants';
import { Console } from '../utils/types';

export interface CalibratorSettings {
  console: Console;
  customFramerate: number;
  precisionCalibration: boolean;
  minimumLength: number; // in milliseconds
}

function getFramerate(settings: CalibratorSettings): number {
  switch (settings.console) {
    case Console.GBA:
      return GBA_FRAMERATE;
    case Console.NDS_SLOT2:
      return NDS_SLOT2_FRAMERATE;
    case Console.NDS_SLOT1:
    case Console.DSI:
    case Console.THREE_DS:
      return NDS_SLOT1_FRAMERATE;
    case Console.CUSTOM: {
      if (settings.customFramerate === 0) {
        throw new Error('Custom framerate must be greater than 0');
      }
      return 1000 / settings.customFramerate;
    }
    default:
      return NDS_SLOT1_FRAMERATE;
  }
}

export function toDelays(settings: CalibratorSettings, milliseconds: number): number {
  return Math.floor(milliseconds / getFramerate(settings));
}

export function toMilliseconds(settings: CalibratorSettings, delays: number): number {
  return getFramerate(settings) * delays;
}

export function calibrateToDelays(settings: CalibratorSettings, milliseconds: number): number {
  return settings.precisionCalibration ? Math.round(milliseconds) : toDelays(settings, milliseconds);
}

export function calibrateToMilliseconds(settings: CalibratorSettings, delays: number): number {
  return settings.precisionCalibration ? delays : toMilliseconds(settings, delays);
}

export function createCalibration(settings: CalibratorSettings, delays: number, seconds: number): number {
  return toMilliseconds(settings, delays - toDelays(settings, seconds * 1000));
}
