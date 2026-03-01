import { useRef, useCallback, useEffect } from 'react';
import { useAppStore, useSettingsStore } from '../store';
import { getSoundPlayer, resumeAudio } from '../audio/sounds';
import { ActionMode } from '../utils/types';

export function usePhaseRunner() {
  const workerRef = useRef<Worker | null>(null);
  const flashRef = useRef<(() => void) | null>(null);

  const running = useAppStore((s) => s.running);
  const setRunning = useAppStore((s) => s.setRunning);
  const setCurrentPhaseIndex = useAppStore((s) => s.setCurrentPhaseIndex);
  const setCurrentPhaseElapsed = useAppStore((s) => s.setCurrentPhaseElapsed);

  // Register visual flash callback
  const registerFlash = useCallback((fn: () => void) => {
    flashRef.current = fn;
  }, []);

  useEffect(() => {
    return () => {
      workerRef.current?.terminate();
    };
  }, []);

  const start = useCallback(() => {
    const { phases } = useAppStore.getState();
    const { action, timer } = useSettingsStore.getState();
    if (phases.length === 0) return;

    console.info('[PhaseRunner] Starting phase runner');
    resumeAudio();

    const worker = new Worker(
      new URL('../workers/timerWorker.ts', import.meta.url),
      { type: 'module' },
    );
    workerRef.current = worker;

    const soundPlayer = getSoundPlayer(action.sound);
    const actionMode = action.mode;

    worker.onmessage = (e: MessageEvent) => {
      const { type } = e.data;
      switch (type) {
        case 'tick':
          useAppStore.getState().setCurrentPhaseElapsed(e.data.elapsed);
          break;
        case 'phaseAdvance':
          useAppStore.getState().setCurrentPhaseIndex(e.data.phaseIndex);
          useAppStore.getState().setCurrentPhaseElapsed(0);
          break;
        case 'action':
          if (actionMode === ActionMode.AV || actionMode === ActionMode.AUDIO) {
            soundPlayer();
          }
          if (actionMode === ActionMode.AV || actionMode === ActionMode.VISUAL) {
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
      actionInterval: action.interval,
      actionCount: action.count,
      refreshInterval: timer.refreshInterval,
    });
  }, [setRunning, setCurrentPhaseIndex, setCurrentPhaseElapsed]);

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
    if (useAppStore.getState().running) {
      stop();
    } else {
      start();
    }
  }, [start, stop]);

  return { start, stop, toggle, running, registerFlash };
}
