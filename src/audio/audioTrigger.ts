// Microphone listener for the audio trigger: detects when the input crosses a
// threshold and reports the onset in the main thread's
// `performance.timeOrigin + performance.now()` clock.

import onsetDetectorUrl from './onsetDetector.worklet.ts?worker&url';
import type { OnsetDetectorMessage, OnsetDetectorOptions } from './onsetDetector.worklet';

export const AUDIO_TRIGGER_MIN_DB = -60;
export const AUDIO_TRIGGER_MAX_DB = 0;

// Ignore the start of the stream so the click that armed the trigger (or a
// pop from the mic opening) doesn't fire it.
const SETTLE_SECONDS = 0.3;

export interface MicListenerOptions {
  thresholdDb: number;
  /** Called with the onset time (main-thread absolute ms) for each detected sound. */
  onOnset?: (onsetAt: number) => void;
  /** Called ~30×/s with the peak level in dBFS. */
  onLevel?: (peakDb: number) => void;
  /** Called if the mic stops on its own (unplugged, permission revoked). */
  onEnded?: () => void;
}

export interface MicListener {
  setThreshold: (thresholdDb: number) => void;
  close: () => void;
}

function mainNow(): number {
  return performance.timeOrigin + performance.now();
}

export function dbToAmplitude(db: number): number {
  return Math.pow(10, db / 20);
}

export function amplitudeToDb(amplitude: number): number {
  return amplitude > 0 ? 20 * Math.log10(amplitude) : -Infinity;
}

// How far back the context↔main clock offset samples are kept.
const CLOCK_WINDOW_MS = 2000;

/**
 * Maps AudioContext time to main-thread absolute time with a measured offset.
 *
 * Each sample is `mainNow() − currentTime`. `currentTime` is the end of the
 * last rendered block, so a sample taken right after a render callback is
 * smallest and later ones overshoot by up to one callback period; the minimum
 * over a short window is a stable mapping. (`getOutputTimestamp()` was tried
 * first, but on Chrome/Linux its offset drifted several ms per second.)
 * Constant latencies the browser doesn't report are absorbed by calibration.
 */
export class ContextClock {
  private samples: { at: number; offset: number }[] = [];

  constructor(private readonly ctx: BaseAudioContext) {}

  sample(): void {
    const at = mainNow();
    const offset = at - this.ctx.currentTime * 1000;
    while (this.samples.length > 0 && this.samples[0].at < at - CLOCK_WINDOW_MS) {
      this.samples.shift();
    }
    this.samples.push({ at, offset });
  }

  /** Main-thread time of the sample at `contextTime`, never later than now. */
  toMainTime(contextTime: number): number {
    this.sample();
    let offset = Infinity;
    for (const s of this.samples) offset = Math.min(offset, s.offset);
    return Math.min(offset + contextTime * 1000, mainNow());
  }
}

/** User-facing message for a failure to open the mic. */
export function describeMicError(err: unknown): string {
  if (!window.isSecureContext) {
    return 'Audio trigger needs a secure connection (HTTPS).';
  }
  if (err instanceof DOMException) {
    switch (err.name) {
      case 'NotAllowedError':
      case 'SecurityError':
        return 'Microphone permission denied.';
      case 'NotFoundError':
      case 'OverconstrainedError':
        return 'No microphone found.';
      case 'NotReadableError':
      case 'AbortError':
        return 'Microphone is unavailable (in use by another app?).';
    }
  }
  return `Microphone error: ${err instanceof Error ? err.message : String(err)}`;
}

/**
 * Open the microphone and start listening. Call synchronously from a user
 * gesture: the AudioContext is created and resumed before the first await.
 */
export async function openMicListener(options: MicListenerOptions): Promise<MicListener> {
  if (!window.isSecureContext || !navigator.mediaDevices?.getUserMedia) {
    throw new Error('Audio trigger needs a secure connection (HTTPS).');
  }
  if (typeof AudioWorkletNode === 'undefined') {
    throw new Error('This browser does not support the audio trigger (no AudioWorklet).');
  }

  const ctx = new AudioContext({ latencyHint: 'interactive' });
  void ctx.resume();
  let stream: MediaStream | null = null;
  let closed = false;

  const close = () => {
    if (closed) return;
    closed = true;
    stream?.getTracks().forEach((track) => track.stop());
    void ctx.close();
  };

  try {
    stream = await navigator.mediaDevices.getUserMedia({
      audio: {
        // Browser voice processing adds latency and can swallow short clicks.
        echoCancellation: false,
        noiseSuppression: false,
        autoGainControl: false,
      },
    });
    await ctx.audioWorklet.addModule(onsetDetectorUrl);
    await ctx.resume();
  } catch (err) {
    close();
    throw err;
  }

  const track = stream.getAudioTracks()[0];
  // Chrome reports capture latency; others leave it undefined.
  const inputLatency = (track?.getSettings() as { latency?: number } | undefined)?.latency ?? 0;
  track?.addEventListener('ended', () => {
    if (closed) return;
    close();
    options.onEnded?.();
  });

  const clock = new ContextClock(ctx);
  clock.sample();
  const source = ctx.createMediaStreamSource(stream);
  const detector = new AudioWorkletNode(ctx, 'onset-detector', {
    numberOfInputs: 1,
    numberOfOutputs: 1,
    outputChannelCount: [1],
    processorOptions: {
      threshold: dbToAmplitude(options.thresholdDb),
      settleSeconds: SETTLE_SECONDS,
    } satisfies OnsetDetectorOptions,
  });
  detector.port.onmessage = (e: MessageEvent<OnsetDetectorMessage>) => {
    if (closed) return;
    const msg = e.data;
    if (msg.type === 'level') {
      clock.sample();
      options.onLevel?.(amplitudeToDb(msg.peak));
    } else if (msg.type === 'onset') {
      const receivedAt = mainNow();
      // A captured sample reaches the graph after it entered the mic.
      const onsetAt = clock.toMainTime(msg.frame / ctx.sampleRate) - inputLatency * 1000;
      console.info(
        `[AudioTrigger] onset at ${onsetAt.toFixed(3)} (${amplitudeToDb(msg.peak).toFixed(1)} dBFS), ` +
          `reported ${(receivedAt - onsetAt).toFixed(3)}ms later`,
      );
      options.onOnset?.(onsetAt);
    }
  };
  source.connect(detector);
  // The output is silent; connecting it keeps the node rendering in every browser.
  detector.connect(ctx.destination);

  console.info(
    `[AudioTrigger] listening at ${options.thresholdDb} dBFS ` +
      `(sampleRate=${ctx.sampleRate}, inputLatency=${(inputLatency * 1000).toFixed(1)}ms)`,
  );

  return {
    setThreshold: (thresholdDb) =>
      detector.port.postMessage({ type: 'threshold', threshold: dbToAmplitude(thresholdDb) }),
    close,
  };
}
