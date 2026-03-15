// EonTimer Web Worker — precision phase runner
// Uses performance.now() with spin-wait for sub-millisecond action accuracy.

const SPINWAIT_MS = 2.0;
const UI_UPDATE_INTERVAL = 32; // ~30 fps

let running = false;
let globalStart = 0;
// Pending phase update from the main thread (used for Variable Target mode)
let pendingPhaseUpdate: { index: number; value: number } | null = null;

function sinceStart(): number {
  return performance.now() - globalStart;
}

function sleep(ms: number): Promise<void> {
  return new Promise((resolve) => setTimeout(resolve, Math.max(0, Math.floor(ms))));
}

function buildActions(scheduledTime: number, elapsed: number, interval: number, count: number): number[] {
  const actions: number[] = [];
  // const remaining = phase - elapsed;
  for (let i = 0; i < count; i++) {
    const action = scheduledTime - (interval * i);
    // if (action < remaining) {
    actions.push(action);
    // }
  }
  // Don't reverse: pop() naturally yields highest value first (matching Python's iter(reversed()) + next())
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
  let lastUiUpdate = performance.now();
  let actions = buildActions(scheduledTime, 0, actionInterval, actionCount);
  let nextAction = actions.length > 0 ? actions.pop()! : 0;

  while (running) {
    let now = performance.now();

    let elapsed = now - start;
    const remainingUntilAction = nextAction - now;

    if (remainingUntilAction > SPINWAIT_MS) {
      const sleepMs = Math.min(refreshInterval, remainingUntilAction - SPINWAIT_MS);
      await sleep(sleepMs);
    } else {
      // Spin-wait for precision near action triggers
      while (running && now < nextAction) {
        // tight loop — acceptable in a Worker
        now = performance.now();
      }
      self.postMessage({ type: 'action' });
      nextAction = actions.length > 0 ? actions.pop()! : 0;
    }

    if (now - lastUiUpdate >= UI_UPDATE_INTERVAL) {
      self.postMessage({
        type: 'tick',
        elapsed,
        phaseIndex,
        totalPhases,
      });
      lastUiUpdate = now;
    }

    if (now >= scheduledTime) {
      break;
    }
  }

  // Final elapsed
  const actualElapsed = performance.now() - start;
  console.info(`[PhaseRunner] Finished executing phase ${phaseIndex + 1}/${totalPhases}: expected=${phase.toFixed(3)}ms, actual=${actualElapsed.toFixed(3)}ms`);
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
): Promise<void> {
  let scheduledTime = 0;
  for (let i = 0; i < phases.length && running; i++) {
    const phase = phases[i];
    if (phase === Infinity) {
      // Infinite phase: tick until stopped or phase is updated to a finite value
      const phaseStart = sinceStart();
      while (running) {
        // Check for phase update (e.g. Gen3 Variable Target "Set Target Frame")
        if (pendingPhaseUpdate && pendingPhaseUpdate.index === i) {
          phases[i] = pendingPhaseUpdate.value;
          pendingPhaseUpdate = null;
          // Re-enter the loop with the now-finite phase
          break;
        }
        const elapsed = sinceStart() - phaseStart;
        self.postMessage({ type: 'tick', elapsed, phaseIndex: i, totalPhases: phases.length });
        await sleep(UI_UPDATE_INTERVAL);
      }
      // If phase was updated to a finite value, execute it normally
      if (running && phases[i] !== Infinity) {
        const remaining = phases[i] - (sinceStart() - phaseStart);
        if (remaining > 0) {
          await executePhase(remaining, i, remaining, phases.length, actionInterval, actionCount, refreshInterval);
        }
        if (!running || i + 1 >= phases.length) continue;
        self.postMessage({ type: 'phaseAdvance', phaseIndex: i + 1 });
        continue;
      }
      const actualElapsed = sinceStart() - phaseStart;
      console.info(`[PhaseRunner] Finished executing phase ${i + 1}/${phases.length}: expected=∞, actual=${actualElapsed.toFixed(3)}ms (stopped by user)`);
      self.postMessage({
        type: 'tick',
        elapsed: actualElapsed,
        phaseIndex: i,
        totalPhases: phases.length,
      });
      break;
    }

    scheduledTime += phase;
    await executePhase(phase, i, scheduledTime, phases.length, actionInterval, actionCount, refreshInterval);

    if (!running || i + 1 >= phases.length) break;

    // Advance to next phase
    self.postMessage({ type: 'phaseAdvance', phaseIndex: i + 1 });
  }

  running = false;
  const totalElapsed = performance.now();
  console.info(`[PhaseRunner] Run complete: total time=${totalElapsed.toFixed(3)}ms`);
  self.postMessage({ type: 'finished' });
}

self.onmessage = (e: MessageEvent) => {
  const { type } = e.data;
  if (type === 'start') {
    running = true;
    globalStart = performance.now();
    const { phases, actionInterval, actionCount, refreshInterval } = e.data;
    runPhases(phases, actionInterval, actionCount, refreshInterval);
  } else if (type === 'stop') {
    running = false;
  } else if (type === 'updatePhase') {
    const { index, value } = e.data;
    pendingPhaseUpdate = { index, value };
  }
};
