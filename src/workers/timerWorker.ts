// EonTimer Web Worker — precision phase runner
// Uses performance.now() with spin-wait for sub-millisecond action accuracy.

const SPINWAIT_MS = 4.0;
const UI_UPDATE_INTERVAL = 1000 / 30;

let running = false;
// Pending phase update from the main thread (used for Variable Target mode)
let pendingPhaseUpdate: { index: number; value: number } | null = null;

function sleep(ms: number): Promise<void> {
  return new Promise((resolve) => setTimeout(resolve, Math.max(0, Math.floor(ms))));
}

function now(): number {
  return performance.timeOrigin + performance.now();
}

function buildActions(scheduledTime: number, interval: number, count: number): number[] {
  const t = now();
  const actions: number[] = [];
  for (let i = 0; i < count; i++) {
    const action = scheduledTime - interval * i;
    if (action > t) {
      actions.push(action);
    }
  }
  // Array is descending; pop() yields the earliest (lowest) action time first.
  return actions;
}

async function executePhase(
  phase: number,
  phaseIndex: number,
  scheduledTime: number,
  totalPhases: number,
  actionInterval: number,
  actionCount: number,
  refreshInterval: number,
): Promise<void> {
  const start = scheduledTime - phase;
  let lastUiUpdate = now();
  const actions = buildActions(scheduledTime, actionInterval, actionCount);
  let nextAction = actions.length > 0 ? actions.pop()! : Infinity;

  while (running) {
    let t = now();
    let elapsed = t - start;
    const remainingUntilAction = nextAction - t;

    if (remainingUntilAction > SPINWAIT_MS) {
      const sleepMs = Math.min(refreshInterval, remainingUntilAction - SPINWAIT_MS);
      await sleep(sleepMs);
      t = now();
      elapsed = t - start;
    } else {
      // Spin-wait for precision near action triggers
      while (running && t < nextAction) {
        // tight loop — acceptable in a Worker
        t = now();
      }
    }

    // Fire all actions that became due — handles both the spin-wait path and
    // sleep overshoot (setTimeout resolves late, waking past the action time).
    while (t >= nextAction) {
      const postedAt = now();
      console.debug(`[Worker] posting action @ ${postedAt.toFixed(3)}`);
      self.postMessage({ type: 'action', postedAt });
      nextAction = actions.length > 0 ? actions.pop()! : Infinity;
    }

    if (t - lastUiUpdate >= UI_UPDATE_INTERVAL) {
      self.postMessage({
        type: 'tick',
        elapsed,
        phaseIndex,
        totalPhases,
      });
      lastUiUpdate = t;
    }

    if (t >= scheduledTime) {
      break;
    }
  }

  // Final elapsed
  const actualElapsed = now() - start;
  console.info(
    `[PhaseRunner] Finished executing phase ${phaseIndex + 1}/${totalPhases}: expected=${phase.toFixed(3)}ms, actual=${actualElapsed.toFixed(3)}ms`,
  );
  self.postMessage({
    type: 'tick',
    elapsed: actualElapsed,
    phaseIndex,
    totalPhases,
  });
}

async function runPhases(
  phases: number[],
  actionInterval: number,
  actionCount: number,
  refreshInterval: number,
  startTime: number,
): Promise<void> {
  let scheduledTime = startTime;
  for (let i = 0; i < phases.length && running; i++) {
    const phase = phases[i];
    if (phase === Infinity) {
      // Infinite phase: tick until stopped or phase is updated to a finite value
      // scheduledTime is the scheduled end of the previous phase = scheduled start of this one
      const phaseStart = scheduledTime;
      while (running) {
        // Check for phase update (e.g. Gen3 Variable Target "Set Target Frame")
        if (pendingPhaseUpdate && pendingPhaseUpdate.index === i) {
          phases[i] = pendingPhaseUpdate.value;
          pendingPhaseUpdate = null;
          // Re-enter the loop with the now-finite phase
          break;
        }
        const elapsed = now() - phaseStart;
        self.postMessage({ type: 'tick', elapsed, phaseIndex: i, totalPhases: phases.length });
        await sleep(UI_UPDATE_INTERVAL);
      }
      // If phase was updated to a finite value, execute it using scheduled time.
      // Pass the full phase duration (not remaining) so that elapsed is measured
      // from phaseStart, giving the UI the correct already-elapsed offset.
      if (running && phases[i] !== Infinity) {
        scheduledTime = phaseStart + phases[i];
        const remaining = scheduledTime - now();
        // Notify the UI so it can schedule audio cues for the remaining time
        self.postMessage({
          type: 'phaseResolved',
          phaseIndex: i,
          remaining: Math.max(0, remaining),
        });
        await executePhase(
          phases[i],
          i,
          scheduledTime,
          phases.length,
          actionInterval,
          actionCount,
          refreshInterval,
        );
        if (!running || i + 1 >= phases.length) continue;
        self.postMessage({ type: 'phaseAdvance', phaseIndex: i + 1 });
        continue;
      }
      const actualElapsed = now() - phaseStart;
      console.info(
        `[PhaseRunner] Finished executing phase ${i + 1}/${phases.length}: expected=∞, actual=${actualElapsed.toFixed(3)}ms (stopped by user)`,
      );
      self.postMessage({
        type: 'tick',
        elapsed: actualElapsed,
        phaseIndex: i,
        totalPhases: phases.length,
      });
      break;
    }

    scheduledTime += phase;
    await executePhase(
      phase,
      i,
      scheduledTime,
      phases.length,
      actionInterval,
      actionCount,
      refreshInterval,
    );

    if (!running || i + 1 >= phases.length) break;

    // Advance to next phase
    self.postMessage({ type: 'phaseAdvance', phaseIndex: i + 1 });
  }

  running = false;
  console.info(`[PhaseRunner] Run complete: total time=${(now() - startTime).toFixed(3)}ms`);
  self.postMessage({ type: 'finished' });
}

self.onmessage = (e: MessageEvent) => {
  const { type } = e.data;
  if (type === 'start') {
    running = true;
    const { phases, absoluteStart, actionInterval, actionCount, refreshInterval } = e.data;
    runPhases(phases, actionInterval, actionCount, refreshInterval, absoluteStart);
  } else if (type === 'stop') {
    running = false;
  } else if (type === 'updatePhase') {
    const { index, value } = e.data;
    pendingPhaseUpdate = { index, value };
  }
};
