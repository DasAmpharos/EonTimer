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
export const GBA_FRAMERATE = 16777216 / 280896;
export const NDS_SLOT1_FRAMERATE = 59.8261;
export const NDS_SLOT2_FRAMERATE = 59.6555;

export const GBA_MS_PER_FRAME = 1000 / GBA_FRAMERATE;
export const NDS_SLOT1_MS_PER_FRAME = 1000 / NDS_SLOT1_FRAMERATE;
export const NDS_SLOT2_MS_PER_FRAME = 1000 / NDS_SLOT2_FRAMERATE;
