import { useEffect, useRef } from 'react';
import { useAppStore, useSettingsStore } from '../store';

/**
 * Acquires a Screen Wake Lock while the timer is running (or `armed`, e.g.
 * the audio trigger is waiting for a sound) and the "Keep Screen Awake"
 * setting is enabled. Automatically releases the lock when both stop or the
 * component unmounts.
 */
export function useWakeLock(armed = false): void {
  const running = useAppStore((s) => s.running);
  const active = running || armed;
  const keepAwake = useSettingsStore((s) => s.timer.keepAwake);
  const lockRef = useRef<WakeLockSentinel | null>(null);

  useEffect(() => {
    if (!active || !keepAwake || !('wakeLock' in navigator)) return;

    let released = false;
    navigator.wakeLock
      .request('screen')
      .then((sentinel) => {
        if (released) {
          sentinel.release();
        } else {
          lockRef.current = sentinel;
        }
      })
      .catch(() => {
        // Wake Lock not available (e.g. unsupported browser, low battery)
      });

    return () => {
      released = true;
      lockRef.current?.release();
      lockRef.current = null;
    };
  }, [active, keepAwake]);
}
