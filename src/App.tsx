import { useRef, useState, useCallback, useEffect } from 'react';
import { useAppStore, useSettingsStore } from './store';
import { usePhaseRunner } from './hooks/usePhaseRunner';
import { useTheme } from './hooks/useTheme';
import { TimerDisplay } from './components/TimerDisplay';
import { Gen5Panel, type TimerPanelHandle } from './components/Gen5Panel';
import { Gen4Panel } from './components/Gen4Panel';
import { Gen3Panel } from './components/Gen3Panel';
import { CustomPanel } from './components/CustomPanel';
import { SettingsDialog } from './components/SettingsDialog';
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

  const { toggle, registerFlash } = usePhaseRunner();
  useTheme();

  const gen5Ref = useRef<TimerPanelHandle>(null);
  const gen4Ref = useRef<TimerPanelHandle>(null);
  const gen3Ref = useRef<TimerPanelHandle>(null);
  const customRef = useRef<TimerPanelHandle>(null);

  const refs = [gen5Ref, gen4Ref, gen3Ref, customRef];
  const currentRef = refs[tabIndex];

  const [settingsOpen, setSettingsOpen] = useState(false);
  const [statusMessage, setStatusMessage] = useState('Ready');

  // Update phases when tab or settings change
  const updatePhases = useCallback(() => {
    const ref = refs[useSettingsStore.getState().tabIndex];
    const phases = ref?.current?.createPhases();
    if (phases) setPhases(phases);
  }, [setPhases]);

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
        const phases = ref?.current?.createPhases();
        if (phases) setPhases(phases);
      }, 0);
    },
    [running, setTabIndex, setPhases],
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
    updatePhases();
  }, [running, currentRef, updatePhases]);

  const handleToggle = useCallback(() => {
    toggle();
  }, [toggle]);

  const handleSettingsClose = useCallback(
    (accepted: boolean) => {
      setSettingsOpen(false);
      if (accepted) updatePhases();
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

  return (
    <div className="app">
      <div className="app-container">
        {/* Timer display */}
        <TimerDisplay registerFlash={registerFlash} onToggle={handleToggle} onSettings={() => setSettingsOpen(true)} settingsDisabled={running} />

        {/* Tab panel */}
        <div className="app-tabs">
          <div className="tab-bar">
            {TAB_LABELS.map((label, i) => (
              <button
                key={label}
                className={`tab ${i === tabIndex ? 'active' : ''}`}
                onClick={() => handleTabChange(i)}
                disabled={running && i !== tabIndex}
                title={running && i !== tabIndex ? 'Stop the timer to switch modes' : TAB_TOOLTIPS[i]}
              >
                {label}
              </button>
            ))}
          </div>
          <div className="tab-content">
            <div style={{ display: tabIndex === 0 ? 'flex' : 'none', flex: 1, flexDirection: 'column' }}>
              <Gen5Panel ref={gen5Ref} onPhasesChange={updatePhases} disabled={running} />
            </div>
            <div style={{ display: tabIndex === 1 ? 'flex' : 'none', flex: 1, flexDirection: 'column' }}>
              <Gen4Panel ref={gen4Ref} onPhasesChange={updatePhases} disabled={running} />
            </div>
            <div style={{ display: tabIndex === 2 ? 'flex' : 'none', flex: 1, flexDirection: 'column' }}>
              <Gen3Panel ref={gen3Ref} onPhasesChange={updatePhases} disabled={running} />
            </div>
            <div style={{ display: tabIndex === 3 ? 'flex' : 'none', flex: 1, flexDirection: 'column' }}>
              <CustomPanel ref={customRef} onPhasesChange={updatePhases} disabled={running} />
            </div>
          </div>
        </div>

        {/* Reset / Update buttons */}
        <div className="app-action-bar">
          <button className="btn btn-icon" onClick={handleReset} disabled={running} title="Reset Timer (F5)">
            ↺
          </button>
          <button className="btn btn-update" onClick={handleUpdate} disabled={running} title="Apply calibration from hit values (F6)">
            Update
          </button>
        </div>

        {/* Status bar */}
        <div className="app-status-bar">
          <span>{statusMessage}</span>
          <span className="app-status-links">
            <a href="https://github.com/DasAmpharos/EonTimer" target="_blank" rel="noopener noreferrer" title="GitHub" className="status-link">
              <svg viewBox="0 0 16 16" width="14" height="14" fill="currentColor"><path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82a7.63 7.63 0 0 1 2-.27c.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.01 8.01 0 0 0 16 8c0-4.42-3.58-8-8-8z"/></svg>
            </a>
            <a href="https://github.com/sponsors/DasAmpharos" target="_blank" rel="noopener noreferrer" title="Sponsor" className="status-link sponsor-link">
              <svg viewBox="0 0 16 16" width="14" height="14" fill="currentColor"><path d="M4.25 2.5c-1.336 0-2.75 1.164-2.75 3 0 2.15 1.58 4.144 3.365 5.682A20.6 20.6 0 0 0 8 13.393a20.6 20.6 0 0 0 3.135-2.211C12.92 9.644 14.5 7.65 14.5 5.5c0-1.836-1.414-3-2.75-3-1.373 0-2.609.986-3.029 2.456a.749.749 0 0 1-1.442 0C6.859 3.486 5.623 2.5 4.25 2.5zM8 14.25l-.345.666-.002-.001-.006-.003-.018-.01a7.6 7.6 0 0 1-.31-.17 22.1 22.1 0 0 1-3.434-2.414C2.045 10.731 0 8.35 0 5.5 0 2.836 2.086 1 4.25 1 5.797 1 7.153 1.802 8 3.02 8.847 1.802 10.203 1 11.75 1 13.914 1 16 2.836 16 5.5c0 2.85-2.045 5.231-3.885 6.818a22.1 22.1 0 0 1-3.744 2.584l-.018.01-.006.003h-.002L8 14.25z"/></svg>
            </a>
          </span>
        </div>
      </div>

      {/* Settings dialog */}
      <SettingsDialog open={settingsOpen} onClose={handleSettingsClose} />
    </div>
  );
}
