import { useMemo } from 'react';
import { useSettingsStore, type ActionSettings, type TimerSettings } from '../store';
import { useProfilesStore, DEFAULT_PROFILE_ID } from '../store/profiles';

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
