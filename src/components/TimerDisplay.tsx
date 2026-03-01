import { useRef, useEffect, useCallback } from 'react';
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

interface TimerDisplayProps {
  registerFlash: (fn: () => void) => void;
  onToggle: () => void;
  onSettings: () => void;
  settingsDisabled: boolean;
}

export function TimerDisplay({ registerFlash, onToggle, onSettings, settingsDisabled }: TimerDisplayProps) {
  const phases = useAppStore((s) => s.phases);
  const currentPhaseIndex = useAppStore((s) => s.currentPhaseIndex);
  const currentPhaseElapsed = useAppStore((s) => s.currentPhaseElapsed);
  const running = useAppStore((s) => s.running);
  const actionInterval = useSettingsStore((s) => s.action.interval);
  const actionCount = useSettingsStore((s) => s.action.count);
  const actionColor = useSettingsStore((s) => s.action.color);

  const panelRef = useRef<HTMLDivElement>(null);
  const flashTimeoutRef = useRef<number>(0);

  const currentPhase = phases[currentPhaseIndex] ?? 0;
  const nextPhase = phases[currentPhaseIndex + 1] ?? 0;
  const isLast = currentPhaseIndex + 1 >= phases.length;

  const displayValue =
    currentPhase === INFINITY
      ? currentPhaseElapsed
      : currentPhase - currentPhaseElapsed;

  // Progress
  let progress = 0;
  if (currentPhase > 0 && currentPhase !== INFINITY) {
    progress = Math.min((currentPhaseElapsed / currentPhase) * 100, 100);
  }

  // Progress bar zone
  const actionZoneStart = actionCount > 0 ? actionInterval * Math.max(actionCount - 1, 0) : 0;
  const remaining = currentPhase - currentPhaseElapsed;
  let zone: 'normal' | 'action' | 'complete' = 'normal';
  if (running) {
    if (remaining <= 0) zone = 'complete';
    else if (actionZoneStart > 0 && remaining <= actionZoneStart) zone = 'action';
  }

  // Visual flash
  const flash = useCallback(() => {
    const el = panelRef.current;
    if (!el) return;
    el.style.backgroundColor = actionColor;
    clearTimeout(flashTimeoutRef.current);
    flashTimeoutRef.current = window.setTimeout(() => {
      el.style.backgroundColor = '';
    }, 50);
  }, [actionColor]);

  useEffect(() => {
    registerFlash(flash);
  }, [registerFlash, flash]);

  // Reset progress bar on stop
  const progressValue = running ? progress : 0;
  const displayZone = running ? zone : 'normal';

  return (
    <div className="timer-display" ref={panelRef}>
      <button className="timer-settings-btn" onClick={onSettings} disabled={settingsDisabled} title="Open Settings (Ctrl+,)">
        ⚙
      </button>
      <div className="timer-current-phase">{formatTime(Math.max(0, displayValue))}</div>
      <div className={`timer-progress-bar zone-${displayZone}`}>
        <div className="timer-progress-fill" style={{ width: `${progressValue}%` }} />
      </div>
      <div className="timer-meta">
        <span className="timer-info-item">
          <span className="timer-info-label">Phase:</span>
          <span className="mono">{currentPhaseIndex + 1} of {phases.length || 1}</span>
        </span>
        <span className="timer-info-item">
          <span className="timer-info-label">Next:</span>
          <span className="mono">{isLast ? '—' : nextPhase === INFINITY ? '∞' : `${formatTime(nextPhase)}s`}</span>
        </span>
        <span className="timer-info-item">
          <span className="timer-info-label">Total:</span>
          <span className="mono">{formatTotal(phases)}</span>
        </span>
      </div>
      <button className="timer-play-stop" onClick={onToggle} title={running ? 'Stop (Space)' : 'Start (Space)'}>
        {running ? (
          <svg viewBox="0 0 24 24" width="32" height="32" fill="currentColor">
            <rect x="6" y="5" width="4" height="14" rx="1" />
            <rect x="14" y="5" width="4" height="14" rx="1" />
          </svg>
        ) : (
          <svg viewBox="0 0 24 24" width="32" height="32" fill="currentColor">
            <path d="M8 5.14v13.72a1 1 0 0 0 1.5.86l11.04-6.86a1 1 0 0 0 0-1.72L9.5 4.28A1 1 0 0 0 8 5.14z" />
          </svg>
        )}
      </button>
    </div>
  );
}
