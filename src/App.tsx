import { useRef, useState, useCallback, useEffect, useMemo } from 'react';
import { useAppStore, useSettingsStore } from './store';
import { useProfilesStore, DEFAULT_PROFILE_ID, type TimerProfile } from './store/profiles';
import { usePhaseRunner } from './hooks/usePhaseRunner';
import { useWakeLock } from './hooks/useWakeLock';
import { useTheme } from './hooks/useTheme';
import { useUrlParams } from './hooks/useUrlParams';
import { TimerDisplay } from './components/TimerDisplay';
import { Gen5Panel } from './components/Gen5Panel';
import type { TimerPanelHandle } from './components/timerPanel';
import { Gen4Panel } from './components/Gen4Panel';
import { Gen3Panel } from './components/Gen3Panel';
import { CustomPanel } from './components/CustomPanel';
import { SettingsDialog } from './components/SettingsDialog';
import { ProfileSelector } from './components/ProfileSelector';
import { NewProfileDialog } from './components/NewProfileDialog';
import { TimerType } from './utils/types';
import { DEFAULT_GEN5, DEFAULT_GEN4, DEFAULT_GEN3 } from './store';
import { parseProfileImport } from './utils/profileIO';
import './App.css';

const TAB_LABELS = ['Gen 5', 'Gen 4', 'Gen 3', 'Custom'];
const TAB_TOOLTIPS = [
  'Black / White / Black 2 / White 2',
  'Diamond / Pearl / Platinum / HeartGold / SoulSilver',
  'Ruby / Sapphire / Emerald / FireRed / LeafGreen',
  'Define your own phase sequence',
];

// Maps a TimerType to the tab index in the classic layout
function timerTypeToTabIndex(timerType: TimerType): number | null {
  switch (timerType) {
    case TimerType.GEN5:
      return 0;
    case TimerType.GEN4:
      return 1;
    case TimerType.GEN3:
      return 2;
    case TimerType.CUSTOM:
      return 3;
    default:
      return null;
  }
}

function getDefaultSettingsForType(timerType: TimerType) {
  return {
    gen5: timerType === TimerType.GEN5 ? { ...DEFAULT_GEN5 } : null,
    gen4: timerType === TimerType.GEN4 ? { ...DEFAULT_GEN4 } : null,
    gen3: timerType === TimerType.GEN3 ? { ...DEFAULT_GEN3 } : null,
    custom: timerType === TimerType.CUSTOM ? { phases: [] } : null,
  };
}

export default function App() {
  const tabIndex = useSettingsStore((s) => s.tabIndex);
  const setTabIndex = useSettingsStore((s) => s.setTabIndex);
  const running = useAppStore((s) => s.running);
  const setPhases = useAppStore((s) => s.setPhases);

  const profiles = useProfilesStore((s) => s.profiles);
  const activeProfileId = useProfilesStore((s) => s.activeProfileId);
  const autoSave = useProfilesStore((s) => s.autoSave);
  const setAutoSave = useProfilesStore((s) => s.setAutoSave);
  const createProfile = useProfilesStore((s) => s.createProfile);
  const updateProfile = useProfilesStore((s) => s.updateProfile);

  const activeProfile = useMemo(
    () => profiles.find((p) => p.id === activeProfileId),
    [profiles, activeProfileId],
  );
  const isDefault = activeProfileId === DEFAULT_PROFILE_ID;

  const { toggle, registerFlash } = usePhaseRunner();
  useWakeLock();
  useUrlParams();
  useTheme();

  const gen5Ref = useRef<TimerPanelHandle>(null);
  const gen4Ref = useRef<TimerPanelHandle>(null);
  const gen3Ref = useRef<TimerPanelHandle>(null);
  const customRef = useRef<TimerPanelHandle>(null);

  const refs = useMemo(() => [gen5Ref, gen4Ref, gen3Ref, customRef], []);

  // Determine which panel is active
  const singleTabIndex = isDefault
    ? null
    : timerTypeToTabIndex(activeProfile?.timerType ?? TimerType.DEFAULT);

  const effectiveTabIndex = isDefault ? tabIndex : (singleTabIndex ?? 0);
  const currentRef = refs[effectiveTabIndex];

  const [settingsOpen, setSettingsOpen] = useState(false);
  const [newProfileOpen, setNewProfileOpen] = useState(false);
  const [statusMessage, setStatusMessage] = useState('Ready');
  const [isDirty, setIsDirty] = useState(false);

  // Track unsaved changes for non-default profiles when auto-save is off
  const savedSnapshotRef = useRef<string | null>(null);

  const getSettingsSnapshot = useCallback((tabIdx: number | null) => {
    if (tabIdx === null) return null;
    const s = useSettingsStore.getState();
    if (tabIdx === 0) return JSON.stringify(s.gen5);
    if (tabIdx === 1) return JSON.stringify(s.gen4);
    if (tabIdx === 2) return JSON.stringify(s.gen3);
    if (tabIdx === 3) return JSON.stringify(s.custom);
    return null;
  }, []);

  // Dirty state tracking: reset baseline and subscribe to settings changes
  useEffect(() => {
    if (isDefault || autoSave) {
      savedSnapshotRef.current = null;
      // Defer state update to avoid synchronous setState in effect
      const t = setTimeout(() => setIsDirty(false), 0);
      return () => clearTimeout(t);
    }
    // Capture baseline snapshot after profile settings are applied
    const t = setTimeout(() => {
      savedSnapshotRef.current = getSettingsSnapshot(singleTabIndex);
      setIsDirty(false);
    }, 50);
    const unsub = useSettingsStore.subscribe(() => {
      const current = getSettingsSnapshot(singleTabIndex);
      if (savedSnapshotRef.current !== null && current !== savedSnapshotRef.current) {
        setIsDirty(true);
      } else {
        setIsDirty(false);
      }
    });
    return () => {
      clearTimeout(t);
      unsub();
    };
  }, [isDefault, autoSave, activeProfileId, singleTabIndex, getSettingsSnapshot]);

  // Apply profile settings to the store when profile changes (non-default)
  const lastAppliedProfileRef = useRef<string | null>(null);
  useEffect(() => {
    if (isDefault || !activeProfile) {
      lastAppliedProfileRef.current = null;
      return;
    }
    if (lastAppliedProfileRef.current === activeProfile.id) return;
    lastAppliedProfileRef.current = activeProfile.id;

    const settings = useSettingsStore.getState();

    if (activeProfile.timerType === TimerType.GEN5 && activeProfile.gen5) {
      settings.updateGen5(activeProfile.gen5);
    }
    if (activeProfile.timerType === TimerType.GEN4 && activeProfile.gen4) {
      settings.updateGen4(activeProfile.gen4);
    }
    if (activeProfile.timerType === TimerType.GEN3 && activeProfile.gen3) {
      settings.updateGen3(activeProfile.gen3);
    }
    if (activeProfile.timerType === TimerType.CUSTOM && activeProfile.custom) {
      settings.setCustomPhases(activeProfile.custom.phases);
    }
  }, [activeProfile, isDefault]);

  // Update phases when tab or settings change
  const updatePhases = useCallback(() => {
    const idx = isDefault ? useSettingsStore.getState().tabIndex : (singleTabIndex ?? 0);
    const ref = refs[idx];
    const displayData = ref?.current?.createDisplayData();
    if (displayData) setPhases(displayData.phases, displayData.minutesBeforeTarget);
  }, [refs, setPhases, isDefault, singleTabIndex]);

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
    toggle();
  }, [toggle]);

  const handleSettingsClose = useCallback(
    (accepted: boolean) => {
      setSettingsOpen(false);
      if (accepted) setTimeout(updatePhases, 0);
    },
    [updatePhases],
  );

  const handleProfileChange = useCallback(() => {
    // Reset the applied-profile tracker so the effect will re-apply settings
    lastAppliedProfileRef.current = null;
    setTimeout(updatePhases, 0);
  }, [updatePhases]);

  const handleCreateProfile = useCallback(
    (data: { name: string; timerType: TimerType; description: string }) => {
      const defaults = getDefaultSettingsForType(data.timerType);
      createProfile({
        name: data.name,
        timerType: data.timerType,
        description: data.description,
        gen5: defaults.gen5 ?? undefined,
        gen4: defaults.gen4 ?? undefined,
        gen3: defaults.gen3 ?? undefined,
        custom: defaults.custom ?? undefined,
      });
      // Reset so the new profile's settings get applied
      lastAppliedProfileRef.current = null;
      setTimeout(updatePhases, 0);
    },
    [createProfile, updatePhases],
  );

  const handleImport = useCallback(() => {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = '.json,.eontimer.json';
    input.onchange = () => {
      const file = input.files?.[0];
      if (!file) return;
      const reader = new FileReader();
      reader.onload = () => {
        const result = parseProfileImport(reader.result as string);
        if (!result.ok) {
          alert(result.error);
          return;
        }
        const p = result.profile;
        createProfile({
          name: p.name,
          timerType: p.timerType,
          description: p.description,
          gen5: p.gen5 ?? undefined,
          gen4: p.gen4 ?? undefined,
          gen3: p.gen3 ?? undefined,
          custom: p.custom ?? undefined,
        });
        lastAppliedProfileRef.current = null;
        setTimeout(updatePhases, 0);
        setStatusMessage(`Imported profile "${p.name}".`);
        setTimeout(() => setStatusMessage('Ready'), 4000);
      };
      reader.readAsText(file);
    };
    input.click();
  }, [createProfile, updatePhases]);

  // Save current settings to profile
  const handleSaveProfile = useCallback(() => {
    if (isDefault || !activeProfile) return;
    const settings = useSettingsStore.getState();
    const tabIdx = singleTabIndex ?? 0;
    const patch: Partial<TimerProfile> = {};
    if (tabIdx === 0) patch.gen5 = { ...settings.gen5 };
    if (tabIdx === 1) patch.gen4 = { ...settings.gen4 };
    if (tabIdx === 2) patch.gen3 = { ...settings.gen3 };
    if (tabIdx === 3) patch.custom = { phases: [...settings.custom.phases] };
    updateProfile(activeProfile.id, patch);
    // Reset dirty state after save
    savedSnapshotRef.current = getSettingsSnapshot(singleTabIndex);
    setIsDirty(false);
    setStatusMessage('Profile saved.');
    setTimeout(() => setStatusMessage('Ready'), 4000);
  }, [isDefault, activeProfile, singleTabIndex, updateProfile, getSettingsSnapshot]);

  // Auto-save on settings changes
  useEffect(() => {
    if (!autoSave || isDefault || !activeProfile) return;
    const unsub = useSettingsStore.subscribe(() => {
      const settings = useSettingsStore.getState();
      const tabIdx = singleTabIndex ?? 0;
      const patch: Partial<TimerProfile> = {};
      if (tabIdx === 0) patch.gen5 = { ...settings.gen5 };
      if (tabIdx === 1) patch.gen4 = { ...settings.gen4 };
      if (tabIdx === 2) patch.gen3 = { ...settings.gen3 };
      if (tabIdx === 3) patch.custom = { phases: [...settings.custom.phases] };
      updateProfile(activeProfile.id, patch);
    });
    return unsub;
  }, [autoSave, isDefault, activeProfile, singleTabIndex, updateProfile]);

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
      } else if ((e.ctrlKey || e.metaKey) && e.code === 'KeyS') {
        e.preventDefault();
        handleSaveProfile();
      }
    };
    window.addEventListener('keydown', handler);
    return () => window.removeEventListener('keydown', handler);
  }, [handleToggle, handleReset, handleUpdate, handleSaveProfile]);

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
        <TimerDisplay
          registerFlash={registerFlash}
          onToggle={handleToggle}
          onSettings={() => setSettingsOpen(true)}
          settingsDisabled={running}
        />

        {/* Profile selector */}
        <ProfileSelector
          disabled={running}
          isDirty={isDirty}
          onProfileChange={handleProfileChange}
          onNewProfile={() => setNewProfileOpen(true)}
          onImport={handleImport}
        />

        {/* Profile description */}
        {activeProfile?.description && (
          <div className="profile-description">{activeProfile.description}</div>
        )}

        {/* Tab panel */}
        {isDefault ? (
          // Default profile: show all tabs (classic layout)
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
        ) : (
          // Non-default profile: show single panel
          <div className="app-tabs">
            <div className="tab-content">
              {singleTabIndex === 0 && (
                <div style={{ display: 'flex', flex: 1, flexDirection: 'column' }}>
                  <Gen5Panel ref={gen5Ref} onPhasesChange={updatePhases} disabled={running} />
                </div>
              )}
              {singleTabIndex === 1 && (
                <div style={{ display: 'flex', flex: 1, flexDirection: 'column' }}>
                  <Gen4Panel ref={gen4Ref} onPhasesChange={updatePhases} disabled={running} />
                </div>
              )}
              {singleTabIndex === 2 && (
                <div style={{ display: 'flex', flex: 1, flexDirection: 'column' }}>
                  <Gen3Panel ref={gen3Ref} onPhasesChange={updatePhases} disabled={running} />
                </div>
              )}
              {singleTabIndex === 3 && (
                <div style={{ display: 'flex', flex: 1, flexDirection: 'column' }}>
                  <CustomPanel ref={customRef} onPhasesChange={updatePhases} disabled={running} />
                </div>
              )}
            </div>
          </div>
        )}

        {/* Reset / Update / Save buttons */}
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
          {!isDefault && (
            <div className="save-controls">
              <button
                className={`btn${isDirty ? ' btn-dirty' : ''}`}
                onClick={handleSaveProfile}
                disabled={running}
                title="Save profile (Ctrl+S)"
              >
                Save{isDirty ? ' •' : ''}
              </button>
              <label className="auto-save-label" title="Auto-save changes to this profile">
                <input
                  type="checkbox"
                  checked={autoSave}
                  onChange={(e) => setAutoSave(e.target.checked)}
                />
                Auto
              </label>
            </div>
          )}
        </div>

        {/* Status bar */}
        <div className="app-status-bar">
          <span>{statusMessage}</span>
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

      {/* New Profile dialog */}
      <NewProfileDialog
        open={newProfileOpen}
        onClose={() => setNewProfileOpen(false)}
        onCreate={handleCreateProfile}
      />
    </div>
  );
}
