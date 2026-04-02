/**
 * True when the device has a touch screen or coarse pointer (finger/stylus).
 * Evaluated once at module load — sufficient since hardware rarely changes
 * mid-session, and avoids repeated DOM queries on every render.
 *
 * - `navigator.maxTouchPoints > 0` catches iOS, Android, and Windows touch
 *   devices even when a mouse is also present.
 * - `(pointer: coarse)` catches devices where touch is the *primary* pointer
 *   and maxTouchPoints may not be exposed (some older Android browsers).
 */
export const isTouchDevice: boolean =
  navigator.maxTouchPoints > 0 || window.matchMedia('(pointer: coarse)').matches;
