import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import {
  Console, Gen5Mode, Gen3Mode,
  ActionMode, ActionSound, CustomUnit, Theme,
} from '../utils/types';
import { INFINITY } from '../utils/constants';
import type { CustomPhase } from '../timers/customTimer';

// ─── App runtime state (not persisted) ───
export interface AppState {
  phases: number[];
  minutesBeforeTarget: number | null;
  currentPhaseIndex: number;
  currentPhaseElapsed: number;
  running: boolean;

  setPhases: (phases: number[], minutesBeforeTarget?: number | null) => void;
  setPhase: (index: number, value: number) => void;
  setCurrentPhaseIndex: (index: number) => void;
  setCurrentPhaseElapsed: (elapsed: number) => void;
  setRunning: (running: boolean) => void;
  reset: () => void;
}

export const useAppStore = create<AppState>((set, get) => ({
  phases: [],
  minutesBeforeTarget: null,
  currentPhaseIndex: 0,
  currentPhaseElapsed: 0,
  running: false,

  setPhases: (phases, minutesBeforeTarget = null) => set({ phases, minutesBeforeTarget, currentPhaseIndex: 0, currentPhaseElapsed: 0 }),
  setPhase: (index, value) => {
    const phases = [...get().phases];
    phases[index] = value;
    set({ phases });
  },
  setCurrentPhaseIndex: (index) => set({ currentPhaseIndex: index }),
  setCurrentPhaseElapsed: (elapsed) => set({ currentPhaseElapsed: elapsed }),
  setRunning: (running) => {
    if (running && !get().running) {
      set({ running: true, currentPhaseIndex: 0, currentPhaseElapsed: 0 });
    } else if (!running && get().running) {
      set({ running: false, currentPhaseIndex: 0, currentPhaseElapsed: 0 });
    }
  },
  reset: () => set({ currentPhaseIndex: 0, currentPhaseElapsed: 0 }),
}));

// ─── Settings (persisted to localStorage) ───
export interface Gen5Settings {
  mode: Gen5Mode;
  calibration: number;
  frameCalibration: number;
  entralinkCalibration: number;
  targetDelay: number;
  targetSecond: number;
  targetAdvances: number;
}

export interface Gen4Settings {
  targetDelay: number;
  targetSecond: number;
  calibratedDelay: number;
  calibratedSecond: number;
}

export interface Gen3Settings {
  mode: Gen3Mode;
  preTimer: number;
  targetFrame: number;
  calibration: number;
}

export interface ActionSettings {
  mode: ActionMode;
  sound: ActionSound;
  color: string;
  interval: number;
  count: number;
}

export interface TimerSettings {
  console: Console;
  customFramerate: number;
  precisionCalibration: boolean;
  refreshInterval: number;
  minimumLength: number; // in seconds
}

export interface SettingsState {
  action: ActionSettings;
  timer: TimerSettings;
  gen5: Gen5Settings;
  gen4: Gen4Settings;
  gen3: Gen3Settings;
  custom: { phases: CustomPhase[] };
  tabIndex: number;
  theme: Theme;

  updateAction: (patch: Partial<ActionSettings>) => void;
  updateTimer: (patch: Partial<TimerSettings>) => void;
  updateGen5: (patch: Partial<Gen5Settings>) => void;
  updateGen4: (patch: Partial<Gen4Settings>) => void;
  updateGen3: (patch: Partial<Gen3Settings>) => void;
  setCustomPhases: (phases: CustomPhase[]) => void;
  setTabIndex: (index: number) => void;
  setTheme: (theme: Theme) => void;
  resetAll: () => void;
}

export const DEFAULT_ACTION: ActionSettings = {
  mode: ActionMode.AV,
  sound: ActionSound.BEEP,
  color: '#0000ff',
  interval: 500,
  count: 6,
};

export const DEFAULT_TIMER: TimerSettings = {
  console: Console.NDS_SLOT1,
  customFramerate: 60.0,
  precisionCalibration: false,
  refreshInterval: 8,
  minimumLength: 14,
};

export const DEFAULT_GEN5: Gen5Settings = {
  mode: Gen5Mode.STANDARD,
  calibration: -95,
  frameCalibration: 0,
  entralinkCalibration: 256,
  targetDelay: 1200,
  targetSecond: 50,
  targetAdvances: 100,
};

export const DEFAULT_GEN4: Gen4Settings = {
  targetDelay: 600,
  targetSecond: 50,
  calibratedDelay: 500,
  calibratedSecond: 14,
};

export const DEFAULT_GEN3: Gen3Settings = {
  mode: Gen3Mode.STANDARD,
  preTimer: 5000,
  targetFrame: 1000,
  calibration: 0,
};

export const useSettingsStore = create<SettingsState>()(
  persist(
    (set) => ({
      action: { ...DEFAULT_ACTION },
      timer: { ...DEFAULT_TIMER },
      gen5: { ...DEFAULT_GEN5 },
      gen4: { ...DEFAULT_GEN4 },
      gen3: { ...DEFAULT_GEN3 },
      custom: { phases: [] },
      tabIndex: 0,
      theme: Theme.SYSTEM,

      updateAction: (patch) => set((s) => ({ action: { ...s.action, ...patch } })),
      updateTimer: (patch) => set((s) => ({ timer: { ...s.timer, ...patch } })),
      updateGen5: (patch) => set((s) => ({ gen5: { ...s.gen5, ...patch } })),
      updateGen4: (patch) => set((s) => ({ gen4: { ...s.gen4, ...patch } })),
      updateGen3: (patch) => set((s) => ({ gen3: { ...s.gen3, ...patch } })),
      setCustomPhases: (phases) => set({ custom: { phases } }),
      setTabIndex: (index) => set({ tabIndex: index }),
      setTheme: (theme) => set({ theme }),
      resetAll: () =>
        set({
          action: { ...DEFAULT_ACTION },
          timer: { ...DEFAULT_TIMER },
          gen5: { ...DEFAULT_GEN5 },
          gen4: { ...DEFAULT_GEN4 },
          gen3: { ...DEFAULT_GEN3 },
          custom: { phases: [] },
          tabIndex: 0,
          theme: Theme.SYSTEM,
        }),
    }),
    { name: 'eontimer-settings',
      merge: (persisted: unknown, current: SettingsState): SettingsState => {
        const p = persisted as Partial<SettingsState>;
        return {
          ...current,
          ...p,
          action: { ...current.action, ...p.action },
          timer: { ...current.timer, ...p.timer },
          gen5: { ...current.gen5, ...p.gen5 },
          gen4: { ...current.gen4, ...p.gen4 },
          gen3: { ...current.gen3, ...p.gen3 },
        };
      },
    },
  ),
);
