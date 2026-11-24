// AudioWorklet processor that finds the exact sample where the mic signal
// first crosses a threshold. It runs on the audio rendering thread, so the
// onset frame is sample-accurate and unaffected by main-thread jank or
// background-tab throttling. Loaded via `?worker&url` in audioTrigger.ts.

// AudioWorkletGlobalScope globals (not part of the DOM lib).
declare const sampleRate: number;
declare const currentFrame: number;
declare class AudioWorkletProcessor {
  readonly port: MessagePort;
}
declare function registerProcessor(
  name: string,
  processorCtor: new (options?: {
    processorOptions?: OnsetDetectorOptions;
  }) => AudioWorkletProcessor,
): void;

export interface OnsetDetectorOptions {
  /** Linear amplitude (0–1) a sample must reach to count as an onset. */
  threshold: number;
  /** Ignore onsets during the first N seconds (e.g. the arm-button click). */
  settleSeconds: number;
}

export type OnsetDetectorMessage =
  /** `frame` is the context sample frame of the first sample over the threshold. */
  | { type: 'onset'; frame: number; peak: number }
  /** Peak absolute amplitude since the previous level message. */
  | { type: 'level'; peak: number };

// After an onset, ignore further crossings for this long (the tail of the same click).
const REFRACTORY_SECONDS = 0.25;
const LEVEL_REPORTS_PER_SECOND = 30;

class OnsetDetectorProcessor extends AudioWorkletProcessor {
  private threshold: number;
  private quietUntilFrame: number;
  private readonly levelFrames = Math.round(sampleRate / LEVEL_REPORTS_PER_SECOND);
  private levelPeak = 0;
  private levelCount = 0;

  constructor(options?: { processorOptions?: OnsetDetectorOptions }) {
    super();
    const opts = options?.processorOptions;
    this.threshold = opts?.threshold ?? 0.1;
    this.quietUntilFrame = currentFrame + Math.round((opts?.settleSeconds ?? 0) * sampleRate);
    this.port.onmessage = (e: MessageEvent<{ type: 'threshold'; threshold: number }>) => {
      if (e.data.type === 'threshold') this.threshold = e.data.threshold;
    };
  }

  process(inputs: Float32Array[][]): boolean {
    const channels = inputs[0];
    if (!channels || channels.length === 0) return true;
    const length = channels[0].length;

    for (let i = 0; i < length; i++) {
      let sample = 0;
      for (let c = 0; c < channels.length; c++) {
        const v = Math.abs(channels[c][i]);
        if (v > sample) sample = v;
      }
      if (sample > this.levelPeak) this.levelPeak = sample;

      const frame = currentFrame + i;
      if (sample >= this.threshold && frame >= this.quietUntilFrame) {
        this.quietUntilFrame = frame + Math.round(REFRACTORY_SECONDS * sampleRate);
        this.port.postMessage({
          type: 'onset',
          frame,
          peak: sample,
        } satisfies OnsetDetectorMessage);
      }
    }

    this.levelCount += length;
    if (this.levelCount >= this.levelFrames) {
      this.port.postMessage({ type: 'level', peak: this.levelPeak } satisfies OnsetDetectorMessage);
      this.levelPeak = 0;
      this.levelCount = 0;
    }
    return true;
  }
}

registerProcessor('onset-detector', OnsetDetectorProcessor);
