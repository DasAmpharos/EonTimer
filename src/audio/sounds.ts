// Web Audio API playback from pre-rendered WAV files.

import beepUrl from './beep.wav';
import dingUrl from './ding.wav';
import popUrl from './pop.wav';
import tickUrl from './tick.wav';

const audioCtx: AudioContext = new (
  window.AudioContext ||
  (window as unknown as { webkitAudioContext: typeof AudioContext }).webkitAudioContext
)();

// ─── Buffer loading ───

async function loadBuffer(url: string): Promise<AudioBuffer> {
  const response = await fetch(url);
  const arrayBuffer = await response.arrayBuffer();
  return audioCtx.decodeAudioData(arrayBuffer);
}

const beepBuffer = loadBuffer(beepUrl);
const dingBuffer = loadBuffer(dingUrl);
const popBuffer = loadBuffer(popUrl);
const tickBuffer = loadBuffer(tickUrl);

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

/** Fire-and-forget wrapper for use in event handlers. */
export function resumeAudioSync(): void {
  resumeAudio();
}

// ─── Playback (fire-and-forget) ───

function playBuffer(buffer: Promise<AudioBuffer>, label: string, receivedAt: number): void {
  const mark = performance.mark(`audio:${label}`);
  buffer.then((resolved) => {
    const initiatedAt = performance.timeOrigin + performance.now();
    console.debug(
      `[Audio] ${label} play initiated, dispatch→init=${(initiatedAt - receivedAt).toFixed(3)}ms`,
    );
    const src = audioCtx.createBufferSource();
    src.buffer = resolved;
    src.connect(audioCtx.destination);
    src.onended = () => {
      const completedAt = performance.timeOrigin + performance.now();
      console.debug(
        `[Audio] ${label} play complete, duration=${(completedAt - initiatedAt).toFixed(3)}ms`,
      );
      performance.measure(`audio:${label}`, { start: mark.startTime, end: performance.now() });
    };
    src.start(audioCtx.currentTime);
  });
}

export function playBeep(receivedAt: number): void {
  playBuffer(beepBuffer, 'beep', receivedAt);
}

export function playDing(receivedAt: number): void {
  playBuffer(dingBuffer, 'ding', receivedAt);
}

export function playPop(receivedAt: number): void {
  playBuffer(popBuffer, 'pop', receivedAt);
}

export function playTick(receivedAt: number): void {
  playBuffer(tickBuffer, 'tick', receivedAt);
}

export type SoundPlayer = (receivedAt: number) => void;

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
