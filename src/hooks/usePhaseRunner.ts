import { useRef, useCallback, useEffect } from 'react';
import { useAppStore, useSettingsStore } from '../store';
import { resumeAudio, getSoundPlayer } from '../audio/sounds';
import { ActionMode } from '../utils/types';

export function usePhaseRunner() {
  const workerRef = useRef<Worker | null>(null);
  const flashRef = useRef<(() => void) | null>(null);

  const running = useAppStore((s) => s.running);
  const setRunning = useAppStore((s) => s.setRunning);

  // Register visual flash callback
  const registerFlash = useCallback((fn: () => void) => {
    flashRef.current = fn;
  }, []);

  useEffect(() => {
    return () => {
      workerRef.current?.terminate();
    };
  }, []);

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

  const start = useCallback(() => {
    const { phases } = useAppStore.getState();
    const { action, timer } = useSettingsStore.getState();
    if (phases.length === 0) return;

    // Capture the click time as an absolute timestamp before any async work.
    // Sent to the worker so it can anchor scheduledTime to this moment rather
    // than to the worker's own (later) time origin.
    const absoluteStart = performance.timeOrigin + performance.now();

    console.info('[PhaseRunner] Starting phase runner');
    resumeAudio();

    const worker = new Worker(new URL('../workers/timerWorker.ts', import.meta.url), {
      type: 'module',
    });
    workerRef.current = worker;

    const actionMode = action.mode;
    const useAudio = actionMode === ActionMode.AV || actionMode === ActionMode.AUDIO;
    const useVisual = actionMode === ActionMode.AV || actionMode === ActionMode.VISUAL;
    const soundPlayer = useAudio ? getSoundPlayer(action.sound) : null;

    worker.onmessage = (e: MessageEvent) => {
      const { type } = e.data;
      switch (type) {
        case 'tick':
          useAppStore.getState().setCurrentPhaseElapsed(e.data.elapsed);
          break;
        case 'phaseAdvance': {
          const phaseIndex = e.data.phaseIndex;
          useAppStore.getState().setCurrentPhaseIndex(phaseIndex);
          useAppStore.getState().setCurrentPhaseElapsed(0);
          break;
        }
        case 'action':
          if (useAudio) {
            soundPlayer!();
          }
          if (useVisual) {
            flashRef.current?.();
          }
          break;
        case 'finished':
          useAppStore.getState().setRunning(false);
          break;
      }
    };

    setRunning(true);
    worker.postMessage({
      type: 'start',
      phases,
      absoluteStart,
      actionInterval: action.interval,
      actionCount: action.count,
      refreshInterval: timer.refreshInterval,
    });
  }, [setRunning]);

  const stop = useCallback(() => {
    if (workerRef.current) {
      console.info('[PhaseRunner] Stopping phase runner');
      workerRef.current.postMessage({ type: 'stop' });
      workerRef.current.terminate();
      workerRef.current = null;
    }
    setRunning(false);
  }, [setRunning]);

  const toggle = useCallback(() => {
    // Resume audio in the user-gesture call stack (required by iOS Safari / Chrome autoplay policy)
    resumeAudio();
    if (useAppStore.getState().running) {
      stop();
    } else {
      start();
    }
  }, [start, stop]);

  return { start, stop, toggle, running, registerFlash };
}
