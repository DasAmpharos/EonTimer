import { useMemo } from 'react';
import { useSettingsStore, type ActionSettings, type TimerSettings } from '../store';
import { useProfilesStore, DEFAULT_PROFILE_ID } from '../store/profiles';

/**
 * Non-hook version: resolves effective action + timer settings
 * by checking the active profile for overrides. Safe to call
 * outside of React render (e.g. inside callbacks / workers).
 */
export function getEffectiveSettings(): {
  action: ActionSettings;
  timer: TimerSettings;
} {
  const { action, timer } = useSettingsStore.getState();
  const { profiles, activeProfileId } = useProfilesStore.getState();

  if (activeProfileId === DEFAULT_PROFILE_ID) {
    return { action, timer };
  }
  const profile = profiles.find((p) => p.id === activeProfileId);
  if (!profile) {
    return { action, timer };
  }
  return {
    action: profile.actionOverride ?? action,
    timer: profile.timerOverride ?? timer,
  };
}

/**
 * Hook version: subscribes to both stores so the component
 * re-renders when the effective settings change.
 */
export function useEffectiveSettings(): {
  action: ActionSettings;
  timer: TimerSettings;
} {
  const globalAction = useSettingsStore((s) => s.action);
  const globalTimer = useSettingsStore((s) => s.timer);
  const profiles = useProfilesStore((s) => s.profiles);
  const activeProfileId = useProfilesStore((s) => s.activeProfileId);

  return useMemo(() => {
    if (activeProfileId === DEFAULT_PROFILE_ID) {
      return { action: globalAction, timer: globalTimer };
    }
    const profile = profiles.find((p) => p.id === activeProfileId);
    if (!profile) {
      return { action: globalAction, timer: globalTimer };
    }
    return {
      action: profile.actionOverride ?? globalAction,
      timer: profile.timerOverride ?? globalTimer,
    };
  }, [globalAction, globalTimer, profiles, activeProfileId]);
}
