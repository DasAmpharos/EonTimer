// Web Audio API sound synthesis — no external files needed.
// Supports both immediate playback (play*) and pre-scheduled playback
// (schedule*At) for sample-accurate timing on the hardware audio clock.

let audioCtx: AudioContext | null = null;
let audioUnlocked = false;

// Track scheduled audio nodes so they can be cancelled on stop/phase change
const scheduledSources: Set<AudioScheduledSourceNode> = new Set();

function getContext(): AudioContext {
  if (!audioCtx) {
    audioCtx = new (
      window.AudioContext ||
      (window as unknown as { webkitAudioContext: typeof AudioContext }).webkitAudioContext
    )();
  }
  return audioCtx;
}

/**
 * Resume the AudioContext and play a silent buffer to unlock audio on iOS.
 * Must be called from a user-gesture event handler (click/tap/keydown).
 */
export function resumeAudio(): void {
  const ctx = getContext();
  if (ctx.state === 'suspended') {
    ctx.resume();
  }
  // iOS requires playing a buffer from a user gesture to fully unlock audio
  if (!audioUnlocked) {
    audioUnlocked = true;
    const buf = ctx.createBuffer(1, 1, ctx.sampleRate);
    const src = ctx.createBufferSource();
    src.buffer = buf;
    src.connect(ctx.destination);
    src.start(0);
  }
}

// ─── Immediate playback (fire-and-forget) ───

function playTone(
  frequency: number,
  duration: number,
  type: OscillatorType = 'sine',
  gain = 0.3,
): void {
  const ctx = getContext();
  const osc = ctx.createOscillator();
  const vol = ctx.createGain();
  osc.type = type;
  osc.frequency.value = frequency;
  vol.gain.value = gain;
  vol.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + duration);
  osc.connect(vol);
  vol.connect(ctx.destination);
  osc.start(ctx.currentTime);
  osc.stop(ctx.currentTime + duration);
}

export function playBeep(): void {
  playTone(800, 0.15, 'sine', 0.4);
}

export function playDing(): void {
  playTone(1200, 0.3, 'sine', 0.3);
}

export function playPop(): void {
  const ctx = getContext();
  const osc = ctx.createOscillator();
  const vol = ctx.createGain();
  osc.type = 'sine';
  osc.frequency.value = 400;
  osc.frequency.exponentialRampToValueAtTime(100, ctx.currentTime + 0.06);
  vol.gain.value = 0.4;
  vol.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + 0.06);
  osc.connect(vol);
  vol.connect(ctx.destination);
  osc.start(ctx.currentTime);
  osc.stop(ctx.currentTime + 0.08);
}

export function playTick(): void {
  const ctx = getContext();
  const bufferSize = Math.ceil(ctx.sampleRate * 0.02);
  const buffer = ctx.createBuffer(1, bufferSize, ctx.sampleRate);
  const data = buffer.getChannelData(0);
  for (let i = 0; i < bufferSize; i++) {
    data[i] = (Math.random() * 2 - 1) * Math.exp(-i / (bufferSize * 0.1));
  }
  const source = ctx.createBufferSource();
  const vol = ctx.createGain();
  source.buffer = buffer;
  vol.gain.value = 0.3;
  source.connect(vol);
  vol.connect(ctx.destination);
  source.start(ctx.currentTime);
}

export type SoundPlayer = () => void;

export function getSoundPlayer(sound: string): SoundPlayer {
  switch (sound) {
    case 'Ding':
      return playDing;
    case 'Pop':
      return playPop;
    case 'Tick':
      return playTick;
    case 'Beep':
    default:
      return playBeep;
  }
}

// ─── Pre-scheduled playback (sample-accurate on the audio hardware clock) ───

function trackNode(node: AudioScheduledSourceNode): void {
  scheduledSources.add(node);
  node.onended = () => scheduledSources.delete(node);
}

function scheduleToneAt(
  audioTime: number,
  frequency: number,
  duration: number,
  type: OscillatorType = 'sine',
  gain = 0.3,
): void {
  const ctx = getContext();
  const osc = ctx.createOscillator();
  const vol = ctx.createGain();
  osc.type = type;
  osc.frequency.setValueAtTime(frequency, audioTime);
  vol.gain.setValueAtTime(gain, audioTime);
  vol.gain.exponentialRampToValueAtTime(0.001, audioTime + duration);
  osc.connect(vol);
  vol.connect(ctx.destination);
  osc.start(audioTime);
  osc.stop(audioTime + duration);
  trackNode(osc);
}

function scheduleBeepAt(audioTime: number): void {
  scheduleToneAt(audioTime, 800, 0.15, 'sine', 0.4);
}

function scheduleDingAt(audioTime: number): void {
  scheduleToneAt(audioTime, 1200, 0.3, 'sine', 0.3);
}

function schedulePopAt(audioTime: number): void {
  const ctx = getContext();
  const osc = ctx.createOscillator();
  const vol = ctx.createGain();
  osc.type = 'sine';
  osc.frequency.setValueAtTime(400, audioTime);
  osc.frequency.exponentialRampToValueAtTime(100, audioTime + 0.06);
  vol.gain.setValueAtTime(0.4, audioTime);
  vol.gain.exponentialRampToValueAtTime(0.001, audioTime + 0.06);
  osc.connect(vol);
  vol.connect(ctx.destination);
  osc.start(audioTime);
  osc.stop(audioTime + 0.08);
  trackNode(osc);
}

function scheduleTickAt(audioTime: number): void {
  const ctx = getContext();
  const bufferSize = Math.ceil(ctx.sampleRate * 0.02);
  const buffer = ctx.createBuffer(1, bufferSize, ctx.sampleRate);
  const data = buffer.getChannelData(0);
  for (let i = 0; i < bufferSize; i++) {
    data[i] = (Math.random() * 2 - 1) * Math.exp(-i / (bufferSize * 0.1));
  }
  const source = ctx.createBufferSource();
  const vol = ctx.createGain();
  source.buffer = buffer;
  vol.gain.setValueAtTime(0.3, audioTime);
  source.connect(vol);
  vol.connect(ctx.destination);
  source.start(audioTime);
  trackNode(source);
}

function scheduleSoundAt(sound: string, audioTime: number): void {
  switch (sound) {
    case 'Ding':
      scheduleDingAt(audioTime);
      break;
    case 'Pop':
      schedulePopAt(audioTime);
      break;
    case 'Tick':
      scheduleTickAt(audioTime);
      break;
    case 'Beep':
    default:
      scheduleBeepAt(audioTime);
      break;
  }
}

/**
 * Pre-schedule all audio actions for a phase on the Web Audio timeline.
 * Uses the same action-time calculation as the worker's buildActions():
 * actions are spaced `interval` ms apart, counting back from phase end.
 * Scheduling on the hardware audio clock provides sample-accurate timing
 * (~0.02ms at 48kHz) independent of JavaScript execution jitter.
 */
export function schedulePhaseActions(
  phaseDurationMs: number,
  actionIntervalMs: number,
  actionCount: number,
  sound: string,
): void {
  if (!isFinite(phaseDurationMs) || phaseDurationMs <= 0) return;

  const ctx = getContext();
  const now = ctx.currentTime;

  for (let i = 0; i < actionCount; i++) {
    const offsetFromEndMs = actionIntervalMs * i;
    if (offsetFromEndMs < phaseDurationMs) {
      const triggerMs = phaseDurationMs - offsetFromEndMs;
      scheduleSoundAt(sound, now + triggerMs / 1000);
    }
  }
}

/**
 * Cancel all pre-scheduled audio nodes (e.g. on timer stop or phase change).
 */
export function cancelAllScheduled(): void {
  for (const node of scheduledSources) {
    try {
      node.stop();
      node.disconnect();
    } catch {
      // Node may already be stopped/disconnected
    }
  }
  scheduledSources.clear();
}
