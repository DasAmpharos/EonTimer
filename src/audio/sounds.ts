// Web Audio API sound synthesis — pre-rendered buffers for minimal playback latency.

const audioCtx: AudioContext = new (
  window.AudioContext ||
  (window as unknown as { webkitAudioContext: typeof AudioContext }).webkitAudioContext
)();

// ─── Buffer synthesis (runs once at module load) ───

function renderTone(
  frequency: number,
  duration: number,
  gain: number,
): AudioBuffer {
  const length = Math.ceil(audioCtx.sampleRate * duration);
  const buffer = audioCtx.createBuffer(1, length, audioCtx.sampleRate);
  const data = buffer.getChannelData(0);
  // Exponential gain ramp from `gain` → 0.001 over `duration`
  const decayRate = Math.log(0.001 / gain) / duration;
  for (let i = 0; i < length; i++) {
    const t = i / audioCtx.sampleRate;
    data[i] = Math.sin(2 * Math.PI * frequency * t) * gain * Math.exp(decayRate * t);
  }
  return buffer;
}

function renderPop(): AudioBuffer {
  const duration = 0.08;
  const sweepDuration = 0.06;
  const gain = 0.4;
  const length = Math.ceil(audioCtx.sampleRate * duration);
  const buffer = audioCtx.createBuffer(1, length, audioCtx.sampleRate);
  const data = buffer.getChannelData(0);
  const decayRate = Math.log(0.001 / gain) / sweepDuration;
  let phase = 0;
  for (let i = 0; i < length; i++) {
    const t = i / audioCtx.sampleRate;
    // Frequency sweeps 400 → 100 Hz exponentially over sweepDuration
    const freq = 400 * Math.pow(100 / 400, Math.min(t, sweepDuration) / sweepDuration);
    const envelope = t <= sweepDuration ? gain * Math.exp(decayRate * t) : 0;
    data[i] = Math.sin(phase) * envelope;
    phase += (2 * Math.PI * freq) / audioCtx.sampleRate;
  }
  return buffer;
}

function renderTick(): AudioBuffer {
  const gain = 0.3;
  const length = Math.ceil(audioCtx.sampleRate * 0.02);
  const buffer = audioCtx.createBuffer(1, length, audioCtx.sampleRate);
  const data = buffer.getChannelData(0);
  for (let i = 0; i < length; i++) {
    data[i] = (Math.random() * 2 - 1) * Math.exp(-i / (length * 0.1)) * gain;
  }
  return buffer;
}

const beepBuffer = renderTone(800, 0.15, 0.4);
const dingBuffer = renderTone(1200, 0.3, 0.3);
const popBuffer = renderPop();
const tickBuffer = renderTick();

// ─── Keepalive & resume ───

/**
 * Resume the AudioContext and keep the audio pipeline active with a silent
 * oscillator so the hardware doesn't idle between the user click and the
 * first real beep.  Must be called from a user-gesture handler (click/tap).
 */
export function resumeAudio(): void {
  // Request "playback" audio session so audio is heard even when the iOS
  // silent switch is on (Safari 17+). No-ops on unsupported browsers.
  if ('audioSession' in navigator) {
    (navigator.audioSession as { type: string }).type = 'playback';
  }

  if (audioCtx.state === 'suspended') {
    audioCtx.resume();
  }
}

// ─── Playback (fire-and-forget) ───

function playBuffer(buffer: AudioBuffer): void {
  const src = audioCtx.createBufferSource();
  src.buffer = buffer;
  src.connect(audioCtx.destination);
  src.start(audioCtx.currentTime);
}

export function playBeep(): void {
  playBuffer(beepBuffer);
}

export function playDing(): void {
  playBuffer(dingBuffer);
}

export function playPop(): void {
  playBuffer(popBuffer);
}

export function playTick(): void {
  playBuffer(tickBuffer);
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
