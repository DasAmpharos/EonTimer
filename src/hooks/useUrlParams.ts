import { useEffect } from 'react';
import { useSettingsStore, type Gen3Settings, type Gen4Settings, type Gen5Settings } from '../store';
import type { CustomPhase } from '../timers/customTimer';
import { Gen3Mode, Gen5Mode, CustomUnit } from '../utils/types';

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

/**
 * Reads the URL path and query parameters on mount and applies them to the
 * settings store, enabling pre-populated timer links.
 *
 * Tab selection (two equivalent forms):
 *   Path segment: /EonTimer/3?preTimer=10000
 *   Query param:  /EonTimer/?tab=3&preTimer=10000  (GitHub Pages compatible)
 *   Supported tab values: 3, 4, 5, custom
 *
 * Gen 3 params: mode, preTimer, targetFrame, calibration
 * Gen 4 params: targetDelay, targetSecond, calibratedDelay, calibratedSecond
 * Gen 5 params: mode, calibration, frameCalibration, entralinkCalibration,
 *               targetDelay, targetSecond, targetAdvances
 * Custom params: phases (comma-separated ms values, e.g. phases=5000,3000)
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
        // Custom — comma-separated ms values, e.g. phases=5000,3000,1000
        const phasesParam = params.get('phases');
        if (phasesParam) {
          const phases: CustomPhase[] = phasesParam
            .split(',')
            .map((s) => Number(s.trim()))
            .filter((n) => Number.isFinite(n) && n > 0)
            .map((target) => ({ unit: CustomUnit.MILLISECONDS, target, calibration: 0 }));
          if (phases.length) store.setCustomPhases(phases);
        }
        break;
      }
    }
  }, []);
}
