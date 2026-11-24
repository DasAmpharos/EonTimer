import { useRef, useState, useCallback, useEffect, useMemo } from 'react';
import { useAppStore, useSettingsStore } from './store';
import { usePhaseRunner } from './hooks/usePhaseRunner';
import { useAudioTrigger } from './hooks/useAudioTrigger';
import { useWakeLock } from './hooks/useWakeLock';
import { useTheme } from './hooks/useTheme';
import { useUrlParams } from './hooks/useUrlParams';
import { TimerDisplay } from './components/TimerDisplay';
import { TimerOverlayDialog } from './components/TimerOverlayDialog';
import { Gen5Panel } from './components/Gen5Panel';
import type { TimerPanelHandle } from './components/timerPanel';
import { Gen4Panel } from './components/Gen4Panel';
import { Gen3Panel } from './components/Gen3Panel';
import { CustomPanel } from './components/CustomPanel';
import { SettingsDialog } from './components/SettingsDialog';
import { resumeAudio } from './audio/sounds';
import './App.css';

const TAB_LABELS = ['Gen 5', 'Gen 4', 'Gen 3', 'Custom'];
const TAB_TOOLTIPS = [
  'Black / White / Black 2 / White 2',
  'Diamond / Pearl / Platinum / HeartGold / SoulSilver',
  'Ruby / Sapphire / Emerald / FireRed / LeafGreen',
  'Define your own phase sequence',
];

export default function App() {
  const tabIndex = useSettingsStore((s) => s.tabIndex);
  const setTabIndex = useSettingsStore((s) => s.setTabIndex);
  const running = useAppStore((s) => s.running);
  const setPhases = useAppStore((s) => s.setPhases);

  const { start, toggle, registerFlash } = usePhaseRunner();
  useUrlParams();
  useTheme();

  const gen5Ref = useRef<TimerPanelHandle>(null);
  const gen4Ref = useRef<TimerPanelHandle>(null);
  const gen3Ref = useRef<TimerPanelHandle>(null);
  const customRef = useRef<TimerPanelHandle>(null);

  const refs = useMemo(() => [gen5Ref, gen4Ref, gen3Ref, customRef], []);
  const currentRef = refs[tabIndex];

  const [settingsOpen, setSettingsOpen] = useState(false);
  const [statusMessage, setStatusMessage] = useState('Ready');
  const [overlayOpen, setOverlayOpen] = useState(false);
  const overlayMode = useSettingsStore((s) => s.timer.overlayMode);
  const triggerOnRelease = useSettingsStore((s) => s.timer.triggerOnRelease);
  const updateTimer = useSettingsStore((s) => s.updateTimer);

  // Status messages that revert to "Ready" after a while.
  const statusTimeoutRef = useRef(0);
  const showStatus = useCallback((message: string, durationMs = 4000) => {
    clearTimeout(statusTimeoutRef.current);
    setStatusMessage(message);
    statusTimeoutRef.current = window.setTimeout(() => setStatusMessage('Ready'), durationMs);
  }, []);

  const handleAudioTrigger = useCallback(
    (onsetAt: number) => {
      if (useAppStore.getState().running) return;
      // Start right away: audio was resumed when the trigger was armed, and
      // action sounds wait for the context anyway. Awaiting resume here could
      // stall if the browser suspended it outside a user gesture.
      void resumeAudio();
      start(onsetAt);
      if (!useAppStore.getState().running) return;
      // The run is under way; an open easy-tap overlay would stop it on the next tap.
      setOverlayOpen(false);
      showStatus('Sound detected — timer started.');
    },
    [start, showStatus],
  );

  const handleAudioTriggerError = useCallback(
    (message: string) => showStatus(message, 8000),
    [showStatus],
  );

  const audioTrigger = useAudioTrigger({
    onTrigger: handleAudioTrigger,
    onError: handleAudioTriggerError,
  });
  const { arm: armAudioTrigger, disarm: disarmAudioTrigger } = audioTrigger;
  // Keep the screen on while waiting for the sound, not just once the run starts.
  useWakeLock(audioTrigger.state !== 'off');
  const audioTriggerThreshold = useSettingsStore((s) => s.timer.audioTriggerThreshold);

  // Update phases when tab or settings change
  const updatePhases = useCallback(() => {
    // Phases are frozen while running; the only mid-run change is Gen3
    // "Set Target Frame", which goes through setPhase. They're recomputed
    // when the run ends (see the run-complete effect below).
    if (useAppStore.getState().running) return;
    const tab = useSettingsStore.getState().tabIndex;
    const ref = refs[tab];
    const displayData = ref?.current?.createDisplayData();
    if (displayData) setPhases(displayData.phases, displayData.minutesBeforeTarget);
  }, [refs, setPhases]);

  // Initial phases
  useEffect(() => {
    const t = setTimeout(updatePhases, 0);
    return () => clearTimeout(t);
  }, [updatePhases]);

  const handleTabChange = useCallback(
    (index: number) => {
      if (running) return;
      setTabIndex(index);
      setTimeout(() => {
        const ref = refs[index];
        const displayData = ref?.current?.createDisplayData();
        if (displayData) setPhases(displayData.phases, displayData.minutesBeforeTarget);
      }, 0);
    },
    [refs, running, setTabIndex, setPhases],
  );

  const handleUpdate = useCallback(() => {
    if (running) return;
    const ref = currentRef?.current;
    if (ref && ref.canCalibrate()) {
      ref.calibrate();
      setTimeout(updatePhases, 0);
      showStatus('Calibration applied.');
    }
  }, [running, currentRef, updatePhases, showStatus]);

  const handleReset = useCallback(() => {
    if (running) return;
    if (!confirm('Are you sure you want to reset the current timer to defaults?')) return;
    const ref = currentRef?.current;
    ref?.reset();
    setTimeout(updatePhases, 0);
  }, [running, currentRef, updatePhases]);

  const handleAudioTriggerToggle = useCallback(() => {
    if (running) return;
    if (audioTrigger.state !== 'off') {
      disarmAudioTrigger();
      showStatus('Audio trigger off.');
      return;
    }
    // Resume in the click handler so action sounds play when the mic starts the run.
    void resumeAudio();
    void armAudioTrigger();
  }, [running, audioTrigger.state, armAudioTrigger, disarmAudioTrigger, showStatus]);

  const handleOpenSettings = useCallback(() => {
    // Don't let a sound start the timer behind the dialog (or fight its mic test).
    disarmAudioTrigger();
    setSettingsOpen(true);
  }, [disarmAudioTrigger]);

  const handleToggle = useCallback(() => {
    if (overlayMode && !running) {
      setOverlayOpen(true);
    } else {
      toggle();
    }
  }, [overlayMode, running, toggle]);

  const handleOverlayTrigger = useCallback(
    async (absoluteStart: number) => {
      setOverlayOpen(false);
      await toggle(absoluteStart);
    },
    [toggle],
  );

  const handleOverlayClose = useCallback(() => {
    setOverlayOpen(false);
  }, []);

  const handleOverlayModeToggle = useCallback(() => {
    const next = !overlayMode;
    updateTimer({ overlayMode: next });
    if (!next) setOverlayOpen(false);
  }, [overlayMode, updateTimer]);

  const handleSettingsClose = useCallback(
    (accepted: boolean) => {
      setSettingsOpen(false);
      if (accepted) setTimeout(updatePhases, 0);
    },
    [updatePhases],
  );

  // Keyboard shortcuts
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      // Shortcuts must not act on the timer behind the Settings dialog.
      if (settingsOpen) return;
      const tag = (e.target as HTMLElement)?.tagName;
      if (tag === 'INPUT' || tag === 'SELECT' || tag === 'TEXTAREA') return;

      if (e.code === 'Space') {
        e.preventDefault();
        // Holding Space auto-repeats keydown; only the first press counts.
        if (!triggerOnRelease && !e.repeat) {
          if (overlayOpen) {
            handleOverlayTrigger(performance.timeOrigin + performance.now());
          } else {
            toggle();
          }
        }
      } else if (e.code === 'F5') {
        e.preventDefault();
        if (!e.repeat) handleReset();
      } else if (e.code === 'F6') {
        e.preventDefault();
        if (!e.repeat) handleUpdate();
      }
    };

    const handleKeyUp = (e: KeyboardEvent) => {
      if (settingsOpen) return;
      const tag = (e.target as HTMLElement)?.tagName;
      if (tag === 'INPUT' || tag === 'SELECT' || tag === 'TEXTAREA') return;

      if (e.code === 'Space' && triggerOnRelease) {
        if (overlayOpen) {
          handleOverlayTrigger(performance.timeOrigin + performance.now());
        } else {
          toggle();
        }
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    window.addEventListener('keyup', handleKeyUp);
    return () => {
      window.removeEventListener('keydown', handleKeyDown);
      window.removeEventListener('keyup', handleKeyUp);
    };
  }, [
    toggle,
    handleOverlayTrigger,
    overlayOpen,
    handleReset,
    handleUpdate,
    triggerOnRelease,
    settingsOpen,
  ]);

  // Release the mic once a run starts, however it was started.
  useEffect(() => {
    if (running) disarmAudioTrigger();
  }, [running, disarmAudioTrigger]);

  // Update status on run complete
  const prevRunning = useRef(running);
  useEffect(() => {
    const wasRunning = prevRunning.current;
    prevRunning.current = running;
    if (wasRunning && !running) {
      setTimeout(() => {
        setOverlayOpen(false);
        showStatus('Run complete — enter values hit and press Update to calibrate.', 8000);
        // Restore the panel's phases (e.g. Gen3 Variable Target back to ∞).
        updatePhases();
      }, 0);
    }
  }, [running, updatePhases, showStatus]);

  return (
    <div className="app">
      <div className="app-container">
        {/* Timer display */}
        <TimerDisplay
          registerFlash={registerFlash}
          onToggle={handleToggle}
          onSettings={handleOpenSettings}
          settingsDisabled={running}
          audioTriggerState={audioTrigger.state}
          onAudioTriggerToggle={handleAudioTriggerToggle}
          overlayMode={overlayMode}
          onOverlayModeToggle={handleOverlayModeToggle}
        />

        {/* Tab panel */}
        <div className="app-tabs">
          <div className="tab-bar">
            {TAB_LABELS.map((label, i) => (
              <button
                key={label}
                className={`tab ${i === tabIndex ? 'active' : ''}`}
                onClick={() => handleTabChange(i)}
                disabled={running && i !== tabIndex}
                title={
                  running && i !== tabIndex ? 'Stop the timer to switch modes' : TAB_TOOLTIPS[i]
                }
              >
                {label}
              </button>
            ))}
          </div>
          <div className="tab-content">
            <div
              style={{
                display: tabIndex === 0 ? 'flex' : 'none',
                flex: 1,
                flexDirection: 'column',
              }}
            >
              <Gen5Panel ref={gen5Ref} onPhasesChange={updatePhases} disabled={running} />
            </div>
            <div
              style={{
                display: tabIndex === 1 ? 'flex' : 'none',
                flex: 1,
                flexDirection: 'column',
              }}
            >
              <Gen4Panel ref={gen4Ref} onPhasesChange={updatePhases} disabled={running} />
            </div>
            <div
              style={{
                display: tabIndex === 2 ? 'flex' : 'none',
                flex: 1,
                flexDirection: 'column',
              }}
            >
              <Gen3Panel ref={gen3Ref} onPhasesChange={updatePhases} disabled={running} />
            </div>
            <div
              style={{
                display: tabIndex === 3 ? 'flex' : 'none',
                flex: 1,
                flexDirection: 'column',
              }}
            >
              <CustomPanel ref={customRef} onPhasesChange={updatePhases} disabled={running} />
            </div>
          </div>
        </div>

        {/* Reset / Update buttons */}
        <div className="app-action-bar">
          <button
            className="btn btn-icon"
            onClick={handleReset}
            disabled={running}
            title="Reset Timer (F5)"
          >
            ↺
          </button>
          <button
            className="btn btn-update"
            onClick={handleUpdate}
            disabled={running}
            title="Apply calibration from hit values (F6)"
          >
            Update
          </button>
        </div>

        {/* Status bar */}
        <div className="app-status-bar">
          <span>
            {audioTrigger.state === 'arming'
              ? 'Waiting for microphone…'
              : audioTrigger.state === 'armed'
                ? `Listening — the timer starts on a sound above ${audioTriggerThreshold} dB.`
                : statusMessage}
          </span>
        </div>

        {/* Version footer */}
        <div className="app-version">
          {__APP_VERSION__} ({__COMMIT_HASH__})
          {__PR_NUMBER__ && (
            <>
              {' · '}
              <a
                href={`https://github.com/DasAmpharos/EonTimer/pull/${__PR_NUMBER__}`}
                target="_blank"
                rel="noreferrer"
              >
                PR #{__PR_NUMBER__}
              </a>
            </>
          )}
        </div>
      </div>

      {/* Settings dialog */}
      <SettingsDialog open={settingsOpen} onClose={handleSettingsClose} />

      {/* Easy-tap overlay */}
      <TimerOverlayDialog
        open={overlayOpen}
        onTrigger={handleOverlayTrigger}
        onClose={handleOverlayClose}
      />
    </div>
  );
}
