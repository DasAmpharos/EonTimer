import { useEffect } from 'react';
import {
  useSettingsStore,
  type Gen3Settings,
  type Gen4Settings,
  type Gen5Settings,
  type ActionSettings,
  type TimerSettings,
} from '../store';
import type { CustomPhase } from '../timers/customTimer';
import { Console, Gen3Mode, Gen5Mode, ActionMode, ActionSound, Theme, CustomUnit } from '../utils/types';

/** Map from URL segment or `tab` query param to tab index (0=Gen5, 1=Gen4, 2=Gen3, 3=Custom). */
const TAB_SEGMENT_MAP: Record<string, number> = {
  '5': 0,
  '4': 1,
  '3': 2,
  custom: 3,
};

function toInt(params: URLSearchParams, key: string): number | undefined {
  const val = params.get(key);
  if (val === null) return undefined;
  const n = Math.round(Number(val));
  return Number.isFinite(n) ? n : undefined;
}

function toFloat(params: URLSearchParams, key: string): number | undefined {
  const val = params.get(key);
  if (val === null) return undefined;
  const n = Number(val);
  return Number.isFinite(n) ? n : undefined;
}

function parseGen3Mode(raw: string | null): Gen3Mode | undefined {
  if (!raw) return undefined;
  const v = raw.toLowerCase().replace(/[-_\s]/g, '');
  if (v === 'standard') return Gen3Mode.STANDARD;
  if (v === 'variabletarget' || v === 'variable') return Gen3Mode.VARIABLE_TARGET;
  return undefined;
}

function parseGen5Mode(raw: string | null): Gen5Mode | undefined {
  if (!raw) return undefined;
  const v = raw.toLowerCase().replace(/[-_\s+]/g, '');
  if (v === 'standard') return Gen5Mode.STANDARD;
  if (v === 'cgear') return Gen5Mode.C_GEAR;
  if (v === 'entralink') return Gen5Mode.ENTRALINK;
  if (v === 'entralinkplus') return Gen5Mode.ENTRALINK_PLUS;
  return undefined;
}

function parseConsole(raw: string | null): Console | undefined {
  if (!raw) return undefined;
  const v = raw.toLowerCase().replace(/[-_\s]/g, '');
  if (v === 'gba') return Console.GBA;
  if (v === 'ndsslot1' || v === 'nds1') return Console.NDS_SLOT1;
  if (v === 'ndsslot2' || v === 'nds2') return Console.NDS_SLOT2;
  if (v === 'dsi') return Console.DSI;
  if (v === '3ds') return Console.THREE_DS;
  if (v === 'custom') return Console.CUSTOM;
  return undefined;
}

function parseActionMode(raw: string | null): ActionMode | undefined {
  if (!raw) return undefined;
  const v = raw.toLowerCase().replace(/[-_\s/]/g, '');
  if (v === 'av') return ActionMode.AV;
  if (v === 'audio') return ActionMode.AUDIO;
  if (v === 'visual') return ActionMode.VISUAL;
  return undefined;
}

function parseActionSound(raw: string | null): ActionSound | undefined {
  if (!raw) return undefined;
  const v = raw.toLowerCase();
  if (v === 'beep') return ActionSound.BEEP;
  if (v === 'ding') return ActionSound.DING;
  if (v === 'pop') return ActionSound.POP;
  if (v === 'tick') return ActionSound.TICK;
  return undefined;
}

function parseTheme(raw: string | null): Theme | undefined {
  if (!raw) return undefined;
  const v = raw.toLowerCase();
  if (v === 'light') return Theme.LIGHT;
  if (v === 'dark') return Theme.DARK;
  if (v === 'system') return Theme.SYSTEM;
  return undefined;
}

function toBool(params: URLSearchParams, key: string): boolean | undefined {
  const val = params.get(key);
  if (val === null) return undefined;
  if (val === 'true' || val === '1') return true;
  if (val === 'false' || val === '0') return false;
  return undefined;
}

function parseCustomUnit(raw: string | null): CustomUnit {
  if (!raw) return CustomUnit.MILLISECONDS;
  const v = raw.toLowerCase().trim();
  if (v === 'advances' || v === 'adv') return CustomUnit.ADVANCES;
  if (v === 'hex' || v === 'seed') return CustomUnit.HEX;
  return CustomUnit.MILLISECONDS;
}

/**
 * Reads the URL path and query parameters on mount and applies them to the
 * settings store, enabling pre-populated timer links.
 *
 * Tab selection (two equivalent forms):
 *   Path segment: /EonTimer/3?preTimer=10000
 *   Query param:  /EonTimer/?tab=3&preTimer=10000  (GitHub Pages compatible)
 *   Supported tab values: 3, 4, 5, custom
 *
 * Global settings (applied regardless of active tab):
 *   console, customFramerate, precisionCalibration, refreshInterval, minimumLength
 *   actionMode, actionSound, actionColor, actionInterval, actionCount
 *   theme
 *
 * Gen 3 params: mode, preTimer, targetFrame, calibration
 * Gen 4 params: targetDelay, targetSecond, calibratedDelay, calibratedSecond
 * Gen 5 params: mode, calibration, frameCalibration, entralinkCalibration,
 *               targetDelay, targetSecond, targetAdvances
 * Custom params: phases (comma-separated, optional :unit suffix — ms, advances/adv, hex/seed)
 *               e.g. phases=5000,100:advances,1A2B:hex
 */
export function useUrlParams(): void {
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);

    // Derive the path segment after the base URL, e.g. "3" from "/EonTimer/3"
    const base = import.meta.env.BASE_URL.replace(/\/$/, ''); // e.g. "/EonTimer"
    const segment = window.location.pathname
      .slice(base.length) // strip base
      .replace(/^\/|\/$/g, ''); // strip surrounding slashes

    // Path-based tab wins; fall back to ?tab= query param
    const tabIndex =
      TAB_SEGMENT_MAP[segment] ??
      (params.has('tab') ? (TAB_SEGMENT_MAP[params.get('tab')!] ?? null) : null);

    if (tabIndex === null && !params.toString()) return;

    const store = useSettingsStore.getState();

    if (tabIndex !== null) {
      store.setTabIndex(tabIndex);
    }

    // ── Global settings (applied regardless of active tab) ──────────────────

    const timerPatch: Partial<TimerSettings> = {};
    const consoleVal = parseConsole(params.get('console'));
    if (consoleVal !== undefined) timerPatch.console = consoleVal;
    const customFramerate = toFloat(params, 'customFramerate');
    if (customFramerate !== undefined) timerPatch.customFramerate = customFramerate;
    const precisionCalibration = toBool(params, 'precisionCalibration');
    if (precisionCalibration !== undefined) timerPatch.precisionCalibration = precisionCalibration;
    const refreshInterval = toInt(params, 'refreshInterval');
    if (refreshInterval !== undefined) timerPatch.refreshInterval = refreshInterval;
    const minimumLength = toInt(params, 'minimumLength');
    if (minimumLength !== undefined) timerPatch.minimumLength = minimumLength;
    if (Object.keys(timerPatch).length) store.updateTimer(timerPatch);

    const actionPatch: Partial<ActionSettings> = {};
    const actionMode = parseActionMode(params.get('actionMode'));
    if (actionMode !== undefined) actionPatch.mode = actionMode;
    const actionSound = parseActionSound(params.get('actionSound'));
    if (actionSound !== undefined) actionPatch.sound = actionSound;
    const actionColor = params.get('actionColor');
    if (actionColor) actionPatch.color = actionColor;
    const actionInterval = toInt(params, 'actionInterval');
    if (actionInterval !== undefined) actionPatch.interval = actionInterval;
    const actionCount = toInt(params, 'actionCount');
    if (actionCount !== undefined) actionPatch.count = actionCount;
    if (Object.keys(actionPatch).length) store.updateAction(actionPatch);

    const theme = parseTheme(params.get('theme'));
    if (theme !== undefined) store.setTheme(theme);

    // ── Tab-specific timer fields ────────────────────────────────────────────

    const effectiveTab = tabIndex ?? store.tabIndex;
    switch (effectiveTab) {
      case 0: {
        // Gen 5
        const patch: Partial<Gen5Settings> = {};
        const mode = parseGen5Mode(params.get('mode'));
        if (mode !== undefined) patch.mode = mode;
        const calibration = toInt(params, 'calibration');
        if (calibration !== undefined) patch.calibration = calibration;
        const frameCalibration = toInt(params, 'frameCalibration');
        if (frameCalibration !== undefined) patch.frameCalibration = frameCalibration;
        const entralinkCalibration = toInt(params, 'entralinkCalibration');
        if (entralinkCalibration !== undefined) patch.entralinkCalibration = entralinkCalibration;
        const targetDelay = toInt(params, 'targetDelay');
        if (targetDelay !== undefined) patch.targetDelay = targetDelay;
        const targetSecond = toInt(params, 'targetSecond');
        if (targetSecond !== undefined) patch.targetSecond = targetSecond;
        const targetAdvances = toInt(params, 'targetAdvances');
        if (targetAdvances !== undefined) patch.targetAdvances = targetAdvances;
        if (Object.keys(patch).length) store.updateGen5(patch);
        break;
      }
      case 1: {
        // Gen 4
        const patch: Partial<Gen4Settings> = {};
        const targetDelay = toInt(params, 'targetDelay');
        if (targetDelay !== undefined) patch.targetDelay = targetDelay;
        const targetSecond = toInt(params, 'targetSecond');
        if (targetSecond !== undefined) patch.targetSecond = targetSecond;
        const calibratedDelay = toInt(params, 'calibratedDelay');
        if (calibratedDelay !== undefined) patch.calibratedDelay = calibratedDelay;
        const calibratedSecond = toInt(params, 'calibratedSecond');
        if (calibratedSecond !== undefined) patch.calibratedSecond = calibratedSecond;
        if (Object.keys(patch).length) store.updateGen4(patch);
        break;
      }
      case 2: {
        // Gen 3
        const patch: Partial<Gen3Settings> = {};
        const mode = parseGen3Mode(params.get('mode'));
        if (mode !== undefined) patch.mode = mode;
        const preTimer = toInt(params, 'preTimer');
        if (preTimer !== undefined) patch.preTimer = preTimer;
        const targetFrame = toInt(params, 'targetFrame');
        if (targetFrame !== undefined) patch.targetFrame = targetFrame;
        const calibration = toFloat(params, 'calibration');
        if (calibration !== undefined) patch.calibration = calibration;
        if (Object.keys(patch).length) store.updateGen3(patch);
        break;
      }
      case 3: {
        // Custom — comma-separated phases with optional :unit suffix.
        // Format: target[:unit] where unit is ms, advances/adv, or hex/seed.
        // Defaults to ms when unit is omitted. e.g. phases=5000,100:advances,1A2B:hex
        const phasesParam = params.get('phases');
        if (phasesParam) {
          const phases: CustomPhase[] = phasesParam
            .split(',')
            .map((s): CustomPhase | null => {
              const colonIdx = s.lastIndexOf(':');
              const rawValue = colonIdx !== -1 ? s.slice(0, colonIdx).trim() : s.trim();
              const rawUnit = colonIdx !== -1 ? s.slice(colonIdx + 1).trim() : null;
              const unit = parseCustomUnit(rawUnit);
              const target =
                unit === CustomUnit.HEX ? parseInt(rawValue, 16) : Number(rawValue);
              if (!Number.isFinite(target) || target < 0) return null;
              return { unit, target, calibration: 0 };
            })
            .filter((p): p is CustomPhase => p !== null);
          if (phases.length) store.setCustomPhases(phases);
        }
        break;
      }
    }
  }, []);
}
