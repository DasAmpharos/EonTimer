import { useEffect, useRef } from 'react';
import { useAppStore, useSettingsStore } from '../store';

/**
 * Acquires a Screen Wake Lock while the timer is running and the
 * "Keep Screen Awake" setting is enabled. Automatically releases
 * the lock when the timer stops or the component unmounts.
 */
export function useWakeLock(): void {
  const running = useAppStore((s) => s.running);
  const keepAwake = useSettingsStore((s) => s.timer.keepAwake);
  const lockRef = useRef<WakeLockSentinel | null>(null);

  useEffect(() => {
    if (!running || !keepAwake || !('wakeLock' in navigator)) return;

    let released = false;
    navigator.wakeLock.request('screen').then((sentinel) => {
      if (released) {
        sentinel.release();
      } else {
        lockRef.current = sentinel;
      }
    }).catch(() => {
      // Wake Lock not available (e.g. unsupported browser, low battery)
    });

    return () => {
      released = true;
      lockRef.current?.release();
      lockRef.current = null;
    };
  }, [running, keepAwake]);
}
