import {
  GBA_MS_PER_FRAME,
  NDS_SLOT1_MS_PER_FRAME,
  NDS_SLOT2_MS_PER_FRAME,
} from '../utils/constants';
import { Console } from '../utils/types';

export interface CalibratorSettings {
  console: Console;
  customFramerate: number;
  precisionCalibration: boolean;
  minimumLength: number; // in milliseconds
}

// Match the desktop timer's C# Math.Round(decimal) midpoint-to-even behavior.
function roundHalfToEven(value: number): number {
  if (!Number.isFinite(value)) {
    return Math.round(value);
  }

  const lower = Math.floor(value);
  const upper = Math.ceil(value);
  if (lower === upper) {
    return lower;
  }

  const lowerDistance = value - lower;
  const upperDistance = upper - value;
  const epsilon = Number.EPSILON * Math.max(1, Math.abs(value));

  if (Math.abs(lowerDistance - upperDistance) <= epsilon) {
    return Math.abs(lower) % 2 === 0 ? lower : upper;
  }

  return lowerDistance < upperDistance ? lower : upper;
}

function getMsPerFrame(settings: CalibratorSettings): number {
  switch (settings.console) {
    case Console.GBA:
      return GBA_MS_PER_FRAME;
    case Console.NDS_SLOT2:
      return NDS_SLOT2_MS_PER_FRAME;
    case Console.NDS_SLOT1:
    case Console.DSI:
    case Console.THREE_DS:
      return NDS_SLOT1_MS_PER_FRAME;
    case Console.CUSTOM: {
      if (settings.customFramerate === 0) {
        throw new Error('Custom framerate must be greater than 0');
      }
      return 1000 / settings.customFramerate;
    }
    default:
      return NDS_SLOT1_MS_PER_FRAME;
  }
}

export function toDelays(settings: CalibratorSettings, milliseconds: number): number {
  return roundHalfToEven(milliseconds / getMsPerFrame(settings));
}

export function toMilliseconds(settings: CalibratorSettings, delays: number): number {
  return roundHalfToEven(getMsPerFrame(settings) * delays);
}

export function calibrateToDelays(settings: CalibratorSettings, milliseconds: number): number {
  return settings.precisionCalibration
    ? roundHalfToEven(milliseconds)
    : toDelays(settings, milliseconds);
}

export function calibrateToMilliseconds(settings: CalibratorSettings, delays: number): number {
  return settings.precisionCalibration ? delays : toMilliseconds(settings, delays);
}

export function createCalibration(
  settings: CalibratorSettings,
  delays: number,
  seconds: number,
): number {
  return toMilliseconds(settings, delays - toDelays(settings, seconds * 1000));
}
