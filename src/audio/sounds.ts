// Web Audio API sound synthesis — no external files needed.

let audioCtx: AudioContext | null = null;
let audioUnlocked = false;

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
  // Request "playback" audio session so audio is heard even when the iOS
  // silent switch is on (Safari 17+). No-ops on unsupported browsers.
  if ('audioSession' in navigator) {
    (navigator.audioSession as { type: string }).type = 'playback';
  }

  const ctx = getContext();
  if (ctx.state === 'suspended') {
    ctx.resume();
  }
  // iOS requires playing a buffer from a user gesture to fully unlock audio.
  // Also serves as a warmup to prime the audio pipeline so the first real
  // beep doesn't suffer extra latency from oscillator creation.
  if (!audioUnlocked) {
    audioUnlocked = true;
    const buf = ctx.createBuffer(1, 1, ctx.sampleRate);
    const src = ctx.createBufferSource();
    src.buffer = buf;
    src.connect(ctx.destination);
    src.start(0);
  }
}

// ─── Playback (fire-and-forget) ───

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
