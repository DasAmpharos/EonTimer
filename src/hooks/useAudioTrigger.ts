import { useCallback, useEffect, useRef, useState } from 'react';
import { useSettingsStore } from '../store';
import {
  describeMicError,
  openMicListener,
  type MicEndReason,
  type MicListener,
} from '../audio/audioTrigger';

function describeMicEnd(reason: MicEndReason): string {
  return reason === 'interrupted'
    ? 'Audio was interrupted — audio trigger off.'
    : 'Microphone disconnected — audio trigger off.';
}

export type AudioTriggerState = 'off' | 'arming' | 'armed';

interface UseAudioTriggerOptions {
  /** Called once with the sound's onset time (main-thread absolute ms). */
  onTrigger: (onsetAt: number) => void;
  /** Called with a user-facing message when the mic can't be opened or is lost. */
  onError: (message: string) => void;
}

/**
 * Arm the microphone so the next sound over the threshold triggers once.
 * The mic is released on detection, on `disarm()` and on unmount.
 */
export function useAudioTrigger({ onTrigger, onError }: UseAudioTriggerOptions) {
  const [state, setState] = useState<AudioTriggerState>('off');
  const listenerRef = useRef<MicListener | null>(null);
  // Bumped on every arm/disarm so a slow getUserMedia can't revive a stale arm.
  const generationRef = useRef(0);
  const busyRef = useRef(false);
  const callbacksRef = useRef({ onTrigger, onError });

  useEffect(() => {
    callbacksRef.current = { onTrigger, onError };
  }, [onTrigger, onError]);

  const disarm = useCallback(() => {
    generationRef.current++;
    busyRef.current = false;
    listenerRef.current?.close();
    listenerRef.current = null;
    setState('off');
  }, []);

  /**
   * Must be called synchronously from a user gesture. Resolves to true once
   * listening, or false if it failed or was disarmed meanwhile.
   */
  const arm = useCallback(async (): Promise<boolean> => {
    if (busyRef.current) return false; // already arming or armed
    busyRef.current = true;
    const generation = ++generationRef.current;
    setState('arming');
    try {
      const listener = await openMicListener({
        thresholdDb: useSettingsStore.getState().timer.audioTriggerThreshold,
        onOnset: (onsetAt) => {
          if (generationRef.current !== generation) return;
          disarm();
          callbacksRef.current.onTrigger(onsetAt);
        },
        onEnded: (reason) => {
          if (generationRef.current !== generation) return;
          disarm();
          callbacksRef.current.onError(describeMicEnd(reason));
        },
      });
      if (generationRef.current !== generation) {
        listener.close(); // disarmed while waiting for permission
        return false;
      }
      listenerRef.current = listener;
      setState('armed');
      return true;
    } catch (err) {
      if (generationRef.current === generation) {
        disarm();
        callbacksRef.current.onError(describeMicError(err));
      }
      return false;
    }
  }, [disarm]);

  useEffect(() => disarm, [disarm]);

  return { state, arm, disarm };
}
