import { useRef, useCallback, useEffect } from 'react';
import { useAppStore, useSettingsStore } from '../store';
import { resumeAudio, getSoundPlayer, setTimerRunning } from '../audio/sounds';
import { ActionMode } from '../utils/types';

function createWorker(): Worker {
  return new Worker(new URL('../workers/timerWorker.ts', import.meta.url), {
    type: 'module',
  });
}

function mainNow(): number {
  return performance.timeOrigin + performance.now();
}

const CLOCK_SYNC_PINGS = 5;
// Skew below this is treated as zero so normal runs keep exact timing.
const CLOCK_SKEW_THRESHOLD_MS = 5;

/**
 * Estimate (main clock − worker clock) with a few ping round-trips, keeping
 * the sample with the smallest round-trip time (error ≤ RTT / 2).
 *
 * Each context's clock is its own `timeOrigin + performance.now()`. They
 * normally agree, but after the machine sleeps a worker created on wake can
 * disagree with the page by about the sleep length. Start times are sent in
 * the main thread's clock, so that skew made the countdown jump (#263).
 */
function measureClockSkew(worker: Worker, onResult: (skew: number) => void): void {
  let best = { rtt: Infinity, skew: 0 };
  let received = 0;
  const listener = (e: MessageEvent) => {
    if (e.data.type !== 'pong') return;
    const t1 = mainNow();
    const rtt = t1 - e.data.t0;
    if (rtt < best.rtt) best = { rtt, skew: (e.data.t0 + t1) / 2 - e.data.workerNow };
    if (++received < CLOCK_SYNC_PINGS) {
      worker.postMessage({ type: 'ping', t0: mainNow() });
    } else {
      worker.removeEventListener('message', listener);
      onResult(best.skew);
    }
  };
  worker.addEventListener('message', listener);
  worker.postMessage({ type: 'ping', t0: mainNow() });
}

export function usePhaseRunner() {
  const workerRef = useRef<Worker | null>(null);
  const flashRef = useRef<(() => void) | null>(null);
  // Main clock − worker clock for the current worker; 0 when unmeasured or negligible.
  const clockSkewRef = useRef(0);

  const running = useAppStore((s) => s.running);
  const setRunning = useAppStore((s) => s.setRunning);

  // Register visual flash callback
  const registerFlash = useCallback((fn: () => void) => {
    flashRef.current = fn;
  }, []);

  const syncClock = useCallback((worker: Worker) => {
    measureClockSkew(worker, (skew) => {
      if (workerRef.current !== worker) return; // replaced meanwhile
      clockSkewRef.current = Math.abs(skew) > CLOCK_SKEW_THRESHOLD_MS ? skew : 0;
      if (clockSkewRef.current !== 0) {
        console.info(`[PhaseRunner] worker clock skew=${skew.toFixed(3)}ms, correcting`);
      }
    });
  }, []);

  const spawnWorker = useCallback(() => {
    const worker = createWorker();
    workerRef.current = worker;
    clockSkewRef.current = 0;
    syncClock(worker);
    return worker;
  }, [syncClock]);

  // Create the worker eagerly on mount so it's ready for the first start
  useEffect(() => {
    spawnWorker();
    return () => {
      workerRef.current?.terminate();
      workerRef.current = null;
    };
  }, [spawnWorker]);

  // Re-check the clock when the page becomes visible again (e.g. after wake).
  useEffect(() => {
    const onVisible = () => {
      const worker = workerRef.current;
      if (document.visibilityState === 'visible' && worker && !useAppStore.getState().running) {
        syncClock(worker);
      }
    };
    document.addEventListener('visibilitychange', onVisible);
    return () => document.removeEventListener('visibilitychange', onVisible);
  }, [syncClock]);

  // Forward phase updates to the running worker (e.g. Gen3 Variable Target "Set Target Frame")
  useEffect(() => {
    if (!running || !workerRef.current) return;
    const unsubscribe = useAppStore.subscribe((state, prevState) => {
      if (state.phases !== prevState.phases && workerRef.current) {
        for (let i = 0; i < state.phases.length; i++) {
          if (state.phases[i] !== prevState.phases[i]) {
            workerRef.current.postMessage({
              type: 'updatePhase',
              index: i,
              value: state.phases[i],
            });
          }
        }
      }
    });
    return unsubscribe;
  }, [running]);

  const start = useCallback(
    (providedAbsoluteStart?: number) => {
      const { phases } = useAppStore.getState();
      const { action, timer } = useSettingsStore.getState();
      if (phases.length === 0) return;

      const worker = workerRef.current;
      if (!worker) return;

      // Use the provided timestamp (e.g. from a touchstart event) if available,
      // otherwise fall back to capturing now. This avoids the ~300ms click delay
      // on mobile browsers.
      const absoluteStart = providedAbsoluteStart ?? mainNow();
      // The worker measures time with its own clock; convert the start time.
      const clockSkew = clockSkewRef.current;

      console.info('[PhaseRunner] Starting phase runner');

      const actionMode = action.mode;
      const useAudio = actionMode === ActionMode.AV || actionMode === ActionMode.AUDIO;
      const useVisual = actionMode === ActionMode.AV || actionMode === ActionMode.VISUAL;
      const soundPlayer = useAudio ? getSoundPlayer(action.sound) : null;

      worker.onmessage = (e: MessageEvent) => {
        const { type } = e.data;
        switch (type) {
          case 'tick':
            // Ticks carry the worker's phase index; trust it so the display
            // can't drift from the phase actually running.
            useAppStore.getState().setProgress(e.data.phaseIndex, e.data.elapsed);
            break;
          case 'phaseAdvance': {
            const phaseIndex = e.data.phaseIndex;
            useAppStore.getState().setCurrentPhaseIndex(phaseIndex);
            useAppStore.getState().setCurrentPhaseElapsed(0);
            break;
          }
          case 'action': {
            const receivedAt = mainNow();
            console.debug(
              `[PhaseRunner] received action, ipc latency=${(receivedAt - (e.data.postedAt + clockSkew)).toFixed(3)}ms`,
            );
            if (useAudio) {
              soundPlayer!(receivedAt);
            }
            if (useVisual) {
              flashRef.current?.();
            }
            break;
          }
          case 'finished':
            if (useAudio) setTimerRunning(false);
            useAppStore.getState().setRunning(false);
            break;
        }
      };

      if (useAudio) setTimerRunning(true);
      setRunning(true);
      worker.postMessage({
        type: 'start',
        phases,
        absoluteStart: absoluteStart - clockSkew,
        actionInterval: action.interval,
        actionCount: action.count,
        refreshInterval: timer.refreshInterval,
      });
    },
    [setRunning],
  );

  const stop = useCallback(() => {
    if (workerRef.current) {
      console.info('[PhaseRunner] Stopping phase runner');
      workerRef.current.postMessage({ type: 'stop' });
      workerRef.current.terminate();
      // Immediately create a fresh worker so it's ready for the next start
      spawnWorker();
    }
    setTimerRunning(false);
    setRunning(false);
  }, [setRunning, spawnWorker]);

  const toggle = useCallback(
    async (providedAbsoluteStart?: number) => {
      // Resume audio in the user-gesture call stack (required by iOS Safari /
      // Chrome autoplay policy). Also warms up the AudioContext so it is fully
      // active by the time the first action fires.
      const resumePromise = resumeAudio();
      if (useAppStore.getState().running) {
        stop();
      } else {
        await resumePromise;
        if (!useAppStore.getState().running) {
          start(providedAbsoluteStart);
        }
      }
    },
    [start, stop],
  );

  return { start, stop, toggle, running, registerFlash };
}
