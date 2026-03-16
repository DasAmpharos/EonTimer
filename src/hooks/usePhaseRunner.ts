import { useRef, useCallback, useEffect } from 'react';
import { useAppStore, useSettingsStore } from '../store';
import { resumeAudio, schedulePhaseActions, cancelAllScheduled } from '../audio/sounds';
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
      cancelAllScheduled();
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
    cancelAllScheduled();

    const worker = new Worker(new URL('../workers/timerWorker.ts', import.meta.url), {
      type: 'module',
    });
    workerRef.current = worker;

    const actionMode = action.mode;
    const actionInterval = action.interval;
    const actionCount = action.count;
    const actionSound = action.sound;
    const useAudio = actionMode === ActionMode.AV || actionMode === ActionMode.AUDIO;
    const useVisual = actionMode === ActionMode.AV || actionMode === ActionMode.VISUAL;

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
          // Pre-schedule audio for the new phase on the Web Audio timeline
          if (useAudio) {
            const currentPhases = useAppStore.getState().phases;
            schedulePhaseActions(
              currentPhases[phaseIndex],
              actionInterval,
              actionCount,
              actionSound,
            );
          }
          break;
        }
        case 'phaseResolved':
          // Infinite phase resolved to a finite value: schedule audio cues
          // using the remaining time so they land at the correct absolute moment.
          if (useAudio) {
            schedulePhaseActions(e.data.remaining, actionInterval, actionCount, actionSound);
          }
          break;
        case 'action':
          // Audio is pre-scheduled on the Web Audio timeline for
          // sample-accurate timing; action messages only drive visual flash
          if (useVisual) {
            flashRef.current?.();
          }
          break;
        case 'finished':
          // Delay cleanup so the last pre-scheduled audio cue finishes
          // playing — it fires at approximately the same wall-clock moment
          // this message arrives, creating a race with cancelAllScheduled.
          setTimeout(() => cancelAllScheduled(), 500);
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

    // Pre-schedule audio for the first phase
    if (useAudio) {
      schedulePhaseActions(phases[0], actionInterval, actionCount, actionSound);
    }
  }, [setRunning]);

  const stop = useCallback(() => {
    cancelAllScheduled();
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
