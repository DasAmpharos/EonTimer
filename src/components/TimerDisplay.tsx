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
          <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ display: 'block' }}>
            <circle cx="12" cy="12" r="3"/>
            <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83-2.83l.06-.06A1.65 1.65 0 0 0 4.68 15a1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 2.83-2.83l.06.06A1.65 1.65 0 0 0 9 4.68a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 2.83l-.06.06A1.65 1.65 0 0 0 19.4 9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"/>
          </svg>
        </button>
        <div className="timer-display-links">
          <a href="https://github.com/DasAmpharos/EonTimer" target="_blank" rel="noopener noreferrer" title="GitHub" className="timer-display-link">
            <svg viewBox="0 0 16 16" width="14" height="14" fill="currentColor" style={{ display: 'block' }}><path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82a7.63 7.63 0 0 1 2-.27c.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.01 8.01 0 0 0 16 8c0-4.42-3.58-8-8-8z"/></svg>
          </a>
          <span
            role="link"
            tabIndex={0}
            aria-label="Support this project"
            data-tooltip="Support this project"
            className="timer-display-link heart-link"
            onClick={() => window.open('https://github.com/sponsors/DasAmpharos', '_blank', 'noopener,noreferrer')}
            onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') window.open('https://github.com/sponsors/DasAmpharos', '_blank', 'noopener,noreferrer'); }}
          >
            <svg viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ display: 'block' }}><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg>
          </span>
        </div>
      </div>
    </div>
  );
}
