// Web Audio API playback from pre-rendered WAV files.

import beepUrl from './beep.wav';
import dingUrl from './ding.wav';
import popUrl from './pop.wav';
import tickUrl from './tick.wav';

const audioCtx: AudioContext = new (
  window.AudioContext ||
  (window as unknown as { webkitAudioContext: typeof AudioContext }).webkitAudioContext
)();

// ─── Keepalive media stream ───

/**
 * Signals the browser that media is playing, preventing background throttling.
 * Only the keepalive feeds into this — sounds go to audioCtx.destination
 * directly (MediaStreamDestination re-encodes and degrades quality).
 */
const streamDest: MediaStreamAudioDestinationNode = audioCtx.createMediaStreamDestination();
const keepAliveEl: HTMLAudioElement = new Audio();
keepAliveEl.srcObject = streamDest.stream;

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

// ─── Keepalive oscillator ───

/**
 * Keeps samples flowing through the MediaStream during silent phases.  
 * Without this, Chrome idles the audio hardware and swallows the first beep.
 */ 
let keepAliveOsc: OscillatorNode | null = null;
let keepAliveGain: GainNode | null = null;

function startKeepAlive(): void {
  if (keepAliveOsc) return;
  keepAliveOsc = audioCtx.createOscillator();
  keepAliveGain = audioCtx.createGain();
  keepAliveGain.gain.value = 1e-5; // non-zero — Chrome optimises away gain=0
  keepAliveOsc.connect(keepAliveGain);
  keepAliveGain.connect(streamDest);
  keepAliveGain.connect(audioCtx.destination);
  keepAliveOsc.start();
}

function stopKeepAlive(): void {
  if (keepAliveOsc) {
    keepAliveOsc.stop();
    keepAliveOsc.disconnect();
    keepAliveOsc = null;
  }
  if (keepAliveGain) {
    keepAliveGain.disconnect();
    keepAliveGain = null;
  }
}

/**
 * Resume the AudioContext and keep the audio pipeline active with a silent
 * oscillator so the hardware doesn't idle between the user click and the
 * first real beep.  Must be called from a user-gesture handler (click/tap).
 */
export async function resumeAudio(): Promise<void> {
  // Request "playback" audio session so audio is heard even when the iOS
  // silent switch is on (Safari 17+). No-ops on unsupported browsers.
  if ('audioSession' in navigator) {
    (navigator.audioSession as { type: string }).type = 'playback';
  }

  if (audioCtx.state === 'suspended') {
    await audioCtx.resume();
  }

  try {
    await keepAliveEl.play();
  } catch {
    // play() may reject outside a user gesture on first attempt.
  }
}

let resumePromise: Promise<void> | null = null;

/** Coordinate keepalive lifecycle with timer start/stop. */
export function setTimerRunning(active: boolean): void {
  if (active) {
    ensureRunning();
    startKeepAlive();
    keepAliveEl.play().catch(() => {});
  } else {
    stopKeepAlive();
  }
}

function ensureRunning(): Promise<void> {
  if (audioCtx.state !== 'suspended') {
    resumePromise = null;
    return Promise.resolve();
  }
  if (!resumePromise) {
    resumePromise = audioCtx.resume().then(() => {
      resumePromise = null;
    });
  }
  return resumePromise;
}

// ─── Playback (fire-and-forget) ───

function playBuffer(buffer: Promise<AudioBuffer>, label: string, receivedAt: number): void {
  const mark = performance.mark(`audio:${label}`);
  const play = (resolved: AudioBuffer) => {
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
  };

  if (audioCtx.state === 'running') {
    buffer.then(play);
  } else {
    ensureRunning().then(() => buffer.then(play));
  }
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
