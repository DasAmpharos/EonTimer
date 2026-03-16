import { useState, useCallback } from 'react';
import { useSettingsStore, type ActionSettings, type TimerSettings } from '../store';
import { ActionMode, ActionSound, Console, Theme } from '../utils/types';
import { INT_MAX } from '../utils/constants';
import { FormField } from './common/FormField';
import { IntInput } from './common/IntInput';
import { FloatInput } from './common/FloatInput';
import { EnumSelect } from './common/EnumSelect';
import { getSoundPlayer, resumeAudio } from '../audio/sounds';

const ACTION_MODES = Object.values(ActionMode) as ActionMode[];
const ACTION_SOUNDS = Object.values(ActionSound) as ActionSound[];
const CONSOLES = Object.values(Console) as Console[];
const THEMES = Object.values(Theme) as Theme[];

interface SettingsDialogProps {
  open: boolean;
  onClose: (accepted: boolean) => void;
}

export function SettingsDialog({ open, onClose }: SettingsDialogProps) {
  const storeAction = useSettingsStore((s) => s.action);
  const storeTimer = useSettingsStore((s) => s.timer);
  const storeTheme = useSettingsStore((s) => s.theme);
  const updateAction = useSettingsStore((s) => s.updateAction);
  const updateTimer = useSettingsStore((s) => s.updateTimer);
  const setTheme = useSettingsStore((s) => s.setTheme);
  const resetAll = useSettingsStore((s) => s.resetAll);

  // Local copies for editing
  const [action, setAction] = useState<ActionSettings>({ ...storeAction });
  const [timer, setTimer] = useState<TimerSettings>({ ...storeTimer });
  const [theme, setLocalTheme] = useState<Theme>(storeTheme);
  const [tab, setTab] = useState(0);

  // Reset local state when dialog opens
  const [lastOpen, setLastOpen] = useState(false);
  if (open && !lastOpen) {
    setAction({ ...storeAction });
    setTimer({ ...storeTimer });
    setLocalTheme(storeTheme);
    setTab(0);
  }
  if (open !== lastOpen) setLastOpen(open);

  const handleOk = useCallback(() => {
    updateAction(action);
    updateTimer(timer);
    setTheme(theme);
    onClose(true);
  }, [action, timer, theme, updateAction, updateTimer, setTheme, onClose]);

  const handleCancel = useCallback(() => {
    onClose(false);
  }, [onClose]);

  const handleReset = useCallback(() => {
    if (confirm('Are you sure you want to reset all settings? This operation cannot be undone.')) {
      resetAll();
      onClose(true);
    }
  }, [resetAll, onClose]);

  const handleTestAction = useCallback(() => {
    resumeAudio();
    getSoundPlayer(action.sound)();
  }, [action.sound]);

  if (!open) return null;

  return (
    <div className="dialog-overlay" onClick={handleCancel}>
      <div className="dialog" onClick={(e) => e.stopPropagation()}>
        <div className="dialog-title">Settings</div>
        <div className="dialog-tabs">
          {['Action', 'Timer'].map((label, i) => (
            <button
              key={label}
              className={`dialog-tab ${tab === i ? 'active' : ''}`}
              onClick={() => setTab(i)}
            >
              {label}
            </button>
          ))}
        </div>
        <div className="dialog-content">
          {tab === 0 && (
            <div className="settings-panel">
              <FormField
                label="Mode"
                tooltip="How the timer signals each phase transition — audio, visual flash, or both"
              >
                <EnumSelect
                  values={ACTION_MODES}
                  value={action.mode}
                  onChange={(v) => setAction({ ...action, mode: v })}
                />
              </FormField>
              <FormField label="Sound" tooltip="Sound effect played on each action signal">
                <EnumSelect
                  values={ACTION_SOUNDS}
                  value={action.sound}
                  onChange={(v) => setAction({ ...action, sound: v })}
                />
              </FormField>
              <FormField label="Color" tooltip="Flash color used for visual action alerts">
                <input
                  type="color"
                  value={action.color}
                  onChange={(e) => setAction({ ...action, color: e.target.value })}
                  className="color-input"
                  style={{ width: '100%' }}
                />
              </FormField>
              <FormField label="Interval" tooltip="Time between action signals in milliseconds">
                <IntInput
                  value={action.interval}
                  onChange={(v) => setAction({ ...action, interval: v ?? 500 })}
                  min={0}
                  max={INT_MAX}
                />
              </FormField>
              <FormField label="Count" tooltip="Number of action signals per phase transition">
                <IntInput
                  value={action.count}
                  onChange={(v) => setAction({ ...action, count: v ?? 1 })}
                  min={0}
                  max={INT_MAX}
                />
              </FormField>
              <button className="btn" style={{ width: '100%' }} onClick={handleTestAction}>
                Test Action
              </button>
            </div>
          )}
          {tab === 1 && (
            <div className="settings-panel">
              <FormField label="Theme" tooltip="Application color theme">
                <EnumSelect values={THEMES} value={theme} onChange={(v) => setLocalTheme(v)} />
              </FormField>
              <FormField
                label="Console"
                tooltip="Target console; determines the frame rate used for delay calculations"
              >
                <EnumSelect
                  values={CONSOLES}
                  value={timer.console}
                  onChange={(v) => setTimer({ ...timer, console: v })}
                />
              </FormField>
              <FormField
                label="Custom Framerate (FPS)"
                visible={timer.console === Console.CUSTOM}
                tooltip="Framerate of your custom console in frames per second"
              >
                <FloatInput
                  value={timer.customFramerate}
                  onChange={(v) => setTimer({ ...timer, customFramerate: v })}
                  min={0.001}
                  max={INT_MAX}
                />
              </FormField>
              <FormField
                label="Refresh Interval"
                tooltip="How often the timer updates in milliseconds; lower values are smoother but use more CPU"
              >
                <IntInput
                  value={timer.refreshInterval}
                  onChange={(v) => setTimer({ ...timer, refreshInterval: v ?? 8 })}
                  min={1}
                  max={INT_MAX}
                />
              </FormField>
              <FormField
                label="Minimum Length (s)"
                tooltip="Minimum total timer duration in seconds before a minute is added; reduce this if your target falls naturally within a short window (e.g. ~8s on DS/Lite)"
              >
                <IntInput
                  value={timer.minimumLength}
                  onChange={(v) => setTimer({ ...timer, minimumLength: v ?? 14 })}
                  min={0}
                  max={INT_MAX}
                />
              </FormField>
              <FormField
                label="Precision Calibration"
                tooltip="Store calibration in milliseconds rather than delays for sub-frame precision"
              >
                <label className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={timer.precisionCalibration}
                    onChange={(e) => setTimer({ ...timer, precisionCalibration: e.target.checked })}
                  />
                </label>
              </FormField>
            </div>
          )}
        </div>
        <div className="dialog-buttons">
          <button className="btn btn-icon" onClick={handleReset} title="Reset Settings">
            ↺
          </button>
          <button className="btn" onClick={handleCancel}>
            Cancel
          </button>
          <button className="btn btn-primary" onClick={handleOk}>
            OK
          </button>
        </div>
      </div>
    </div>
  );
}
