import { useCallback, useEffect, useRef, useState } from 'react';
import {
  AUDIO_TRIGGER_MAX_DB,
  AUDIO_TRIGGER_MIN_DB,
  describeMicError,
  openMicListener,
  type MicListener,
} from '../audio/audioTrigger';

const TRIGGER_FLASH_MS = 400;

function dbToPercent(db: number): number {
  const clamped = Math.min(Math.max(db, AUDIO_TRIGGER_MIN_DB), AUDIO_TRIGGER_MAX_DB);
  return ((clamped - AUDIO_TRIGGER_MIN_DB) / (AUDIO_TRIGGER_MAX_DB - AUDIO_TRIGGER_MIN_DB)) * 100;
}

function formatDb(db: number): string {
  return Number.isFinite(db) ? `${Math.round(db)} dB` : '—';
}

interface AudioTriggerMeterProps {
  thresholdDb: number;
}

/** Live mic level against the audio trigger threshold, for tuning it in Settings. */
export function AudioTriggerMeter({ thresholdDb }: AudioTriggerMeterProps) {
  const [testing, setTesting] = useState(false);
  const [levelDb, setLevelDb] = useState(-Infinity);
  const [loudestDb, setLoudestDb] = useState(-Infinity);
  const [triggered, setTriggered] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const listenerRef = useRef<MicListener | null>(null);
  const generationRef = useRef(0);
  const flashTimeoutRef = useRef(0);
  const thresholdRef = useRef(thresholdDb);

  useEffect(() => {
    thresholdRef.current = thresholdDb;
    listenerRef.current?.setThreshold(thresholdDb);
  }, [thresholdDb]);

  const stopTest = useCallback(() => {
    generationRef.current++;
    listenerRef.current?.close();
    listenerRef.current = null;
    clearTimeout(flashTimeoutRef.current);
    setTesting(false);
    setTriggered(false);
    setLevelDb(-Infinity);
  }, []);

  const startTest = useCallback(async () => {
    if (listenerRef.current) return;
    const generation = ++generationRef.current;
    setTesting(true);
    setError(null);
    setLoudestDb(-Infinity);
    try {
      const listener = await openMicListener({
        thresholdDb: thresholdRef.current,
        onLevel: (db) => {
          setLevelDb(db);
          setLoudestDb((prev) => Math.max(prev, db));
        },
        onOnset: () => {
          setTriggered(true);
          clearTimeout(flashTimeoutRef.current);
          flashTimeoutRef.current = window.setTimeout(() => setTriggered(false), TRIGGER_FLASH_MS);
        },
        onEnded: (reason) => {
          stopTest();
          setError(
            reason === 'interrupted' ? 'Audio was interrupted.' : 'Microphone disconnected.',
          );
        },
      });
      if (generationRef.current !== generation) {
        listener.close();
        return;
      }
      listenerRef.current = listener;
    } catch (err) {
      if (generationRef.current !== generation) return;
      stopTest();
      setError(describeMicError(err));
    }
  }, [stopTest]);

  useEffect(() => stopTest, [stopTest]);

  const above = levelDb >= thresholdDb;

  return (
    <div className="audio-meter">
      <div
        className="audio-meter-bar"
        role="meter"
        aria-label="Microphone level"
        aria-valuemin={AUDIO_TRIGGER_MIN_DB}
        aria-valuemax={AUDIO_TRIGGER_MAX_DB}
        aria-valuenow={Number.isFinite(levelDb) ? Math.round(levelDb) : AUDIO_TRIGGER_MIN_DB}
      >
        <div
          className={`audio-meter-fill${above ? ' audio-meter-fill--above' : ''}`}
          style={{ transform: `scaleX(${dbToPercent(levelDb) / 100})` }}
        />
        <div
          className="audio-meter-threshold"
          style={{ left: `${dbToPercent(thresholdDb)}%` }}
          title={`Threshold: ${thresholdDb} dB`}
        />
      </div>
      <div className="audio-meter-readout">
        {error ? (
          <span className="audio-meter-error">{error}</span>
        ) : testing ? (
          <>
            <span className="mono">Level {formatDb(levelDb)}</span>
            <span className="mono">Loudest {formatDb(loudestDb)}</span>
            <span className={`audio-meter-hit${triggered ? ' audio-meter-hit--on' : ''}`}>
              Triggered
            </span>
          </>
        ) : (
          <span>
            Test, make the sound you&apos;ll trigger with, then set the threshold a little under its
            loudest level.
          </span>
        )}
      </div>
      <button
        className="btn"
        style={{ width: '100%' }}
        onClick={testing ? stopTest : startTest}
        aria-pressed={testing}
      >
        {testing ? 'Stop Test' : 'Test Microphone'}
      </button>
    </div>
  );
}
