import { useRef, useState, useCallback, useEffect, useMemo } from 'react';
import { useAppStore, useSettingsStore } from './store';
import { usePhaseRunner } from './hooks/usePhaseRunner';
import { useAudio } from './hooks/useAudio';
import { useTheme } from './hooks/useTheme';
import { useUrlParams } from './hooks/useUrlParams';
import { TimerDisplay } from './components/TimerDisplay';
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
  const { isListening, isDetected, startListening, stopListening } = useAudio({
    onDetect: () => {
      if (useAppStore.getState().running) return;
      start();
      stopListening();
      setStatusMessage('Sound detected — timer started.');
      setTimeout(() => setStatusMessage('Ready'), 4000);
    },
  });
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

  // Update phases when tab or settings change
  const updatePhases = useCallback(() => {
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
      updatePhases();
      setStatusMessage('Calibration applied.');
      setTimeout(() => setStatusMessage('Ready'), 4000);
    }
  }, [running, currentRef, updatePhases]);

  const handleReset = useCallback(() => {
    if (running) return;
    if (!confirm('Are you sure you want to reset the current timer to defaults?')) return;
    const ref = currentRef?.current;
    ref?.reset();
    setTimeout(updatePhases, 0);
  }, [running, currentRef, updatePhases]);

  const handleToggle = useCallback(() => {
    if (!running && isListening) {
      stopListening();
    }
    toggle();
  }, [running, isListening, stopListening, toggle]);

  const handleAudioToggle = useCallback(() => {
    if (running) return;
    if (isListening) {
      stopListening();
      setStatusMessage('Audio trigger stopped.');
    } else {
      resumeAudio();
      startListening().then(
        () => setStatusMessage('Listening for sound to start timer...'),
        (err: unknown) => setStatusMessage(`Microphone error: ${err instanceof Error ? err.message : err}`),
      );
    }
  }, [running, isListening, startListening, stopListening]);

  const handleSettingsClose = useCallback(
    (accepted: boolean) => {
      setSettingsOpen(false);
      if (accepted) setTimeout(updatePhases, 0);
    },
    [updatePhases],
  );

  // Keyboard shortcuts
  useEffect(() => {
    const handler = (e: KeyboardEvent) => {
      const tag = (e.target as HTMLElement)?.tagName;
      if (tag === 'INPUT' || tag === 'SELECT' || tag === 'TEXTAREA') return;

      if (e.code === 'Space') {
        e.preventDefault();
        handleToggle();
      } else if (e.code === 'F5') {
        e.preventDefault();
        handleReset();
      } else if (e.code === 'F6') {
        e.preventDefault();
        handleUpdate();
      }
    };
    window.addEventListener('keydown', handler);
    return () => window.removeEventListener('keydown', handler);
  }, [handleToggle, handleReset, handleUpdate]);

  // Update status on run complete
  const prevRunning = useRef(running);
  useEffect(() => {
    if (prevRunning.current && !running) {
      setStatusMessage('Run complete — enter values hit and press Update to calibrate.');
      const t = setTimeout(() => setStatusMessage('Ready'), 8000);
      return () => clearTimeout(t);
    }
    prevRunning.current = running;
  }, [running]);

  // Keep microphone off while a run is active.
  useEffect(() => {
    if (running && isListening) {
      stopListening();
    }
  }, [running, isListening, stopListening]);

  return (
    <div className="app">
      <div className="app-container">
        {/* Timer display */}
        <TimerDisplay
          registerFlash={registerFlash}
          onToggle={handleToggle}
          onSettings={() => setSettingsOpen(true)}
          onToggleAudioListening={handleAudioToggle}
          settingsDisabled={running}
          audioListening={isListening}
          audioDetected={isDetected}
          settingsDisabled={running}
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
          <span>{statusMessage}</span>
        </div>
      </div>

      {/* Settings dialog */}
      <SettingsDialog open={settingsOpen} onClose={handleSettingsClose} />
    </div>
  );
}
