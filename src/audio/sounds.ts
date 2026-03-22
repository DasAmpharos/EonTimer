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

// ─── Playback (fire-and-forget) ───

function playBuffer(buffer: Promise<AudioBuffer>): void {
  buffer.then((resolved) => {
    const src = audioCtx.createBufferSource();
    src.buffer = resolved;
    src.connect(audioCtx.destination);
    src.start(audioCtx.currentTime);
  });
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
