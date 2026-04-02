import { useRef } from 'react';
import { useAppStore, useSettingsStore } from '../store';
import { INFINITY } from '../utils/constants';

function formatTime(milliseconds: number): string {
  if (milliseconds === INFINITY) return '∞';
  const ms = Math.max(0, Math.round(milliseconds));
  const seconds = Math.floor(ms / 1000);
  const msPart = ms % 1000;
  if (seconds >= 1000) return `${seconds}...`;
  return `${seconds}.${String(msPart).padStart(3, '0')}`;
}

function formatTotal(phases: number[]): string {
  if (phases.some((p) => p === INFINITY)) return '∞';
  const totalS = phases.reduce((a, b) => a + b, 0) / 1000;
  const mins = Math.floor(totalS / 60);
  const secs = totalS - mins * 60;
  return `${mins}m ${secs.toFixed(3)}s`;
}

interface TimerOverlayDialogProps {
  open: boolean;
  onTrigger: (absoluteStart: number) => void;
  onClose: () => void;
}

export function TimerOverlayDialog({ open, onTrigger, onClose }: TimerOverlayDialogProps) {
  const touchStartTimeRef = useRef<number | null>(null);
  const phases = useAppStore((s) => s.phases);
  const currentPhaseIndex = useAppStore((s) => s.currentPhaseIndex);
  const currentPhaseElapsed = useAppStore((s) => s.currentPhaseElapsed);
  const minutesBeforeTarget = useAppStore((s) => s.minutesBeforeTarget);
  const triggerOnRelease = useSettingsStore((s) => s.timer.triggerOnRelease);

  if (!open) return null;

  const currentPhase = phases[currentPhaseIndex] ?? 0;
  const nextPhase = phases[currentPhaseIndex + 1] ?? 0;
  const isLast = currentPhaseIndex + 1 >= phases.length;
  const displayValue =
    currentPhase === INFINITY ? currentPhaseElapsed : currentPhase - currentPhaseElapsed;

  return (
    <div
      className="timer-overlay-dialog"
      role="button"
      tabIndex={0}
      aria-label="Tap to start timer"
      onTouchStart={() => {
        if (!triggerOnRelease) {
          touchStartTimeRef.current = performance.timeOrigin + performance.now();
        }
      }}
      onPointerUp={() => {
        if (triggerOnRelease) {
          onTrigger(performance.timeOrigin + performance.now());
        }
      }}
      onClick={() => {
        if (!triggerOnRelease) {
          const ts = touchStartTimeRef.current ?? performance.timeOrigin + performance.now();
          touchStartTimeRef.current = null;
          onTrigger(ts);
        }
      }}
      onKeyDown={(e) => {
        if (e.key === 'Enter' || e.key === ' ') {
          e.preventDefault();
          if (!triggerOnRelease) {
            onTrigger(performance.timeOrigin + performance.now());
          }
        }
      }}
      onKeyUp={(e) => {
        if ((e.key === 'Enter' || e.key === ' ') && triggerOnRelease) {
          onTrigger(performance.timeOrigin + performance.now());
        }
      }}
    >
      <div className="timer-overlay-display">{formatTime(Math.max(0, displayValue))}</div>

      <div className="timer-overlay-progress-bar zone-normal">
        <div className="timer-progress-fill" style={{ transform: 'scaleX(0)' }} />
      </div>

      <div
        className="timer-overlay-meta"
        style={
          minutesBeforeTarget !== null ? { gridTemplateColumns: '1fr 1fr 1fr 1fr' } : undefined
        }
      >
        <span className="timer-overlay-meta-item">
          <span className="timer-overlay-meta-label">Phase</span>
          <span className="mono">
            {currentPhaseIndex + 1} of {phases.length || 1}
          </span>
        </span>
        <span className="timer-overlay-meta-item">
          <span className="timer-overlay-meta-label">Next</span>
          <span className="mono">
            {isLast ? '—' : nextPhase === INFINITY ? '∞' : `${formatTime(nextPhase)}s`}
          </span>
        </span>
        <span className="timer-overlay-meta-item">
          <span className="timer-overlay-meta-label">Total</span>
          <span className="mono">{formatTotal(phases)}</span>
        </span>
        {minutesBeforeTarget !== null && (
          <span
            className="timer-overlay-meta-item"
            title="Set your clock this many minutes before your target time"
          >
            <span className="timer-overlay-meta-label">Mins Before</span>
            <span className="mono">{minutesBeforeTarget}</span>
          </span>
        )}
      </div>

      <div className="timer-overlay-hint">
        {triggerOnRelease ? 'Hold and release to start' : 'Tap anywhere to start'}
      </div>

      <button
        className="timer-overlay-close"
        onClick={(e) => {
          e.stopPropagation();
          onClose();
        }}
        title="Close overlay"
        aria-label="Close overlay"
      >
        ✕
      </button>
    </div>
  );
}
