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
    const color = useSettingsStore.getState().action.color;
    el.style.backgroundColor = color;
    clearTimeout(flashTimeoutRef.current);
    flashTimeoutRef.current = window.setTimeout(() => {
      el.style.backgroundColor = '';
    }, 150);
  }, []);

  useEffect(() => {
    registerFlash(flash);
  }, [registerFlash, flash]);

  // Reset progress bar on stop
  const progressValue = running ? progress : 0;
  const displayZone = running ? zone : 'normal';

  return (
    <div className="timer-display" ref={panelRef}>
      <div className="timer-current-phase">{formatTime(Math.max(0, displayValue))}</div>
      <div className={`timer-progress-bar zone-${displayZone}`}>
        <div className="timer-progress-fill" style={{ transform: `scaleX(${progressValue / 100})` }} />
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
      <div className="timer-display-footer">
        <button className="timer-settings-btn" onClick={onSettings} disabled={settingsDisabled} title="Open Settings (Ctrl+,)">
          ⚙
        </button>
        <div className="timer-display-links">
          <a href="https://github.com/DasAmpharos/EonTimer" target="_blank" rel="noopener noreferrer" title="GitHub" className="timer-display-link">
            <svg viewBox="0 0 16 16" width="14" height="14" fill="currentColor"><path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82a7.63 7.63 0 0 1 2-.27c.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.01 8.01 0 0 0 16 8c0-4.42-3.58-8-8-8z"/></svg>
          </a>
          <a href="https://github.com/sponsors/DasAmpharos" target="_blank" rel="noopener noreferrer" title="Sponsor" className="timer-display-link sponsor-link">
            <svg viewBox="0 0 16 16" width="14" height="14" fill="currentColor"><path d="M4.25 2.5c-1.336 0-2.75 1.164-2.75 3 0 2.15 1.58 4.144 3.365 5.682A20.6 20.6 0 0 0 8 13.393a20.6 20.6 0 0 0 3.135-2.211C12.92 9.644 14.5 7.65 14.5 5.5c0-1.836-1.414-3-2.75-3-1.373 0-2.609.986-3.029 2.456a.749.749 0 0 1-1.442 0C6.859 3.486 5.623 2.5 4.25 2.5zM8 14.25l-.345.666-.002-.001-.006-.003-.018-.01a7.6 7.6 0 0 1-.31-.17 22.1 22.1 0 0 1-3.434-2.414C2.045 10.731 0 8.35 0 5.5 0 2.836 2.086 1 4.25 1 5.797 1 7.153 1.802 8 3.02 8.847 1.802 10.203 1 11.75 1 13.914 1 16 2.836 16 5.5c0 2.85-2.045 5.231-3.885 6.818a22.1 22.1 0 0 1-3.744 2.584l-.018.01-.006.003h-.002L8 14.25z"/></svg>
          </a>
        </div>
      </div>
    </div>
  );
}
