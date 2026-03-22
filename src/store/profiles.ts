import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { TimerType } from '../utils/types';
import type { Gen5Settings, Gen4Settings, Gen3Settings, ActionSettings, TimerSettings } from '.';
import type { CustomPhase } from '../timers/customTimer';

// ─── Profile data model ───

export interface TimerProfile {
  id: string;
  name: string;
  description: string;
  timerType: TimerType;
  gen5: Gen5Settings | null;
  gen4: Gen4Settings | null;
  gen3: Gen3Settings | null;
  custom: { phases: CustomPhase[] } | null;
  actionOverride: ActionSettings | null;
  timerOverride: TimerSettings | null;
  createdAt: string;
  updatedAt: string;
}

export interface ProfilesState {
  profiles: TimerProfile[];
  activeProfileId: string;
  autoSave: boolean;

  createProfile: (init: {
    name: string;
    timerType: TimerType;
    description?: string;
    gen5?: Gen5Settings;
    gen4?: Gen4Settings;
    gen3?: Gen3Settings;
    custom?: { phases: CustomPhase[] };
  }) => string;
  updateProfile: (id: string, patch: Partial<Omit<TimerProfile, 'id'>>) => void;
  deleteProfile: (id: string) => void;
  duplicateProfile: (id: string) => string | null;
  setActiveProfile: (id: string) => void;
  setAutoSave: (autoSave: boolean) => void;
  renameProfile: (id: string, name: string) => void;
}

function generateId(): string {
  return crypto.randomUUID();
}

export const DEFAULT_PROFILE_ID = 'default';

function createDefaultProfile(): TimerProfile {
  return {
    id: DEFAULT_PROFILE_ID,
    name: 'Default',
    description: '',
    timerType: TimerType.DEFAULT,
    gen5: null,
    gen4: null,
    gen3: null,
    custom: null,
    actionOverride: null,
    timerOverride: null,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
  };
}

export const useProfilesStore = create<ProfilesState>()(
  persist(
    (set, get) => ({
      profiles: [createDefaultProfile()],
      activeProfileId: DEFAULT_PROFILE_ID,
      autoSave: false,

      createProfile: (init) => {
        const id = generateId();
        const now = new Date().toISOString();
        const profile: TimerProfile = {
          id,
          name: init.name,
          description: init.description ?? '',
          timerType: init.timerType,
          gen5: init.gen5 ?? null,
          gen4: init.gen4 ?? null,
          gen3: init.gen3 ?? null,
          custom: init.custom ?? null,
          actionOverride: null,
          timerOverride: null,
          createdAt: now,
          updatedAt: now,
        };
        set((s) => ({
          profiles: [...s.profiles, profile],
          activeProfileId: id,
        }));
        return id;
      },

      updateProfile: (id, patch) => {
        set((s) => ({
          profiles: s.profiles.map((p) =>
            p.id === id ? { ...p, ...patch, updatedAt: new Date().toISOString() } : p,
          ),
        }));
      },

      deleteProfile: (id) => {
        if (id === DEFAULT_PROFILE_ID) return;
        set((s) => {
          const profiles = s.profiles.filter((p) => p.id !== id);
          const activeProfileId = s.activeProfileId === id ? DEFAULT_PROFILE_ID : s.activeProfileId;
          return { profiles, activeProfileId };
        });
      },

      duplicateProfile: (id) => {
        const source = get().profiles.find((p) => p.id === id);
        if (!source) return null;
        const newId = generateId();
        const now = new Date().toISOString();
        const profile: TimerProfile = {
          ...structuredClone(source),
          id: newId,
          name: `${source.name} (Copy)`,
          createdAt: now,
          updatedAt: now,
        };
        set((s) => ({
          profiles: [...s.profiles, profile],
          activeProfileId: newId,
        }));
        return newId;
      },

      setActiveProfile: (id) => {
        set({ activeProfileId: id });
      },

      setAutoSave: (autoSave) => {
        set({ autoSave });
      },

      renameProfile: (id, name) => {
        set((s) => ({
          profiles: s.profiles.map((p) =>
            p.id === id ? { ...p, name, updatedAt: new Date().toISOString() } : p,
          ),
        }));
      },
    }),
    {
      name: 'eontimer-profiles',
      merge: (persisted: unknown, current: ProfilesState): ProfilesState => {
        const p = persisted as Partial<ProfilesState>;
        const profiles = p.profiles ?? current.profiles;
        // Ensure default profile always exists
        if (!profiles.find((pr) => pr.id === DEFAULT_PROFILE_ID)) {
          profiles.unshift(createDefaultProfile());
        }
        return {
          ...current,
          ...p,
          profiles,
          activeProfileId: p.activeProfileId ?? current.activeProfileId,
          autoSave: p.autoSave ?? current.autoSave,
        };
      },
    },
  ),
);
