import { TimerType } from '../utils/types';
import type { TimerProfile } from '../store/profiles';

const TIMER_TYPES = new Set(Object.values(TimerType));

interface ExportedProfile {
  _format: 'eontimer-profile';
  _version: 1;
  profile: Omit<TimerProfile, 'id'>;
}

export function exportProfile(profile: TimerProfile): void {
  const data: ExportedProfile = {
    _format: 'eontimer-profile',
    _version: 1,
    profile: {
      name: profile.name,
      description: profile.description,
      timerType: profile.timerType,
      gen5: profile.gen5,
      gen4: profile.gen4,
      gen3: profile.gen3,
      custom: profile.custom,
      actionOverride: profile.actionOverride,
      timerOverride: profile.timerOverride,
      createdAt: profile.createdAt,
      updatedAt: profile.updatedAt,
    },
  };
  const json = JSON.stringify(data, null, 2);
  const blob = new Blob([json], { type: 'application/json' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = `${profile.name.replace(/[^a-zA-Z0-9_-]/g, '_')}.eontimer.json`;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
}

export function parseProfileImport(
  json: string,
): { ok: true; profile: Omit<TimerProfile, 'id'> } | { ok: false; error: string } {
  try {
    const data = JSON.parse(json) as ExportedProfile;
    if (data._format !== 'eontimer-profile') {
      return { ok: false, error: 'Not a valid EonTimer profile file.' };
    }
    if (data._version !== 1) {
      return { ok: false, error: `Unsupported profile version: ${data._version}` };
    }
    const p = data.profile;
    if (!p || typeof p.name !== 'string' || !TIMER_TYPES.has(p.timerType)) {
      return { ok: false, error: 'Invalid profile data.' };
    }
    return { ok: true, profile: p };
  } catch {
    return { ok: false, error: 'Failed to parse JSON file.' };
  }
}
