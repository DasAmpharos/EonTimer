export const INFINITY = Infinity;
export const INT_MAX = 2 ** 31 - 1;
export const INT_MIN = -(2 ** 31 - 1);
export const MINIMUM_LENGTH = 14000;

export function toMinimumLength(value: number, minimumLength: number = MINIMUM_LENGTH): number {
  while (value < minimumLength) {
    value += 60000;
  }
  return value;
}

export function getMinutesBeforeTarget(phases: number[]): number {
  let total = 0;
  for (const phase of phases) {
    if (phase === INFINITY) continue;
    total += phase;
  }
  return Math.floor(total / 60000);
}

// Console framerates
export const GBA_FPS = 59.7275;
export const NDS_SLOT1_FPS = 59.8261;
export const NDS_SLOT2_FPS = 59.6555;

export const GBA_FRAMERATE = 1000 / GBA_FPS;
export const NDS_SLOT1_FRAMERATE = 1000 / NDS_SLOT1_FPS;
export const NDS_SLOT2_FRAMERATE = 1000 / NDS_SLOT2_FPS;
