// EonTimer Web Worker — precision phase runner
// Uses performance.now() with spin-wait for sub-millisecond action accuracy.

const SPINWAIT_MS = 2.0;
const UI_UPDATE_INTERVAL = 32; // ~30 fps

let running = false;
let globalStart = 0;

function sinceStart(): number {
  return performance.now() - globalStart;
}

function sleep(ms: number): Promise<void> {
  return new Promise((resolve) => setTimeout(resolve, Math.max(0, Math.floor(ms))));
}

function buildActions(phase: number, elapsed: number, interval: number, count: number): number[] {
  const actions: number[] = [];
  const remaining = phase - elapsed;
  for (let i = 0; i < count; i++) {
    const action = interval * i;
    if (action < remaining) {
      actions.push(action);
    }
  }
  // Don't reverse: pop() naturally yields highest value first (matching Python's iter(reversed()) + next())
  return actions;
}

async function executePhase(
  phase: number,
  phaseIndex: number,
  totalPhases: number,
  actionInterval: number,
  actionCount: number,
  refreshInterval: number,
): Promise<void> {
  const phaseStart = sinceStart();
  let lastUiUpdate = 0;
  let actions = buildActions(phase, 0, actionInterval, actionCount);
  let nextAction = actions.length > 0 ? actions.pop()! : 0;

  while (running) {
    let elapsed = sinceStart() - phaseStart;
    const remainingUntilAction = phase - elapsed - nextAction;

    if (remainingUntilAction > SPINWAIT_MS) {
      const sleepMs = Math.min(refreshInterval, remainingUntilAction - SPINWAIT_MS);
      await sleep(sleepMs);
    } else if (phase - elapsed > 0) {
      // Spin-wait for precision near action triggers
      while (running && sinceStart() - phaseStart < phase - nextAction) {
        // tight loop — acceptable in a Worker
      }
    }

    elapsed = sinceStart() - phaseStart;
    const remaining = phase - elapsed;

    if (remaining <= nextAction) {
      self.postMessage({ type: 'action' });
      nextAction = actions.length > 0 ? actions.pop()! : 0;
    }

    if (elapsed - lastUiUpdate >= UI_UPDATE_INTERVAL) {
      self.postMessage({
        type: 'tick',
        elapsed,
        phaseIndex,
        totalPhases,
      });
      lastUiUpdate = elapsed;
    }

    if (elapsed >= phase) {
      break;
    }
  }

  // Final elapsed
  const actualElapsed = sinceStart() - phaseStart;
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
  for (let i = 0; i < phases.length && running; i++) {
    const phase = phases[i];
    if (phase === Infinity) {
      // Infinite phase: just tick until stopped
      const phaseStart = sinceStart();
      while (running) {
        const elapsed = sinceStart() - phaseStart;
        self.postMessage({ type: 'tick', elapsed, phaseIndex: i, totalPhases: phases.length });
        await sleep(UI_UPDATE_INTERVAL);
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

    await executePhase(phase, i, phases.length, actionInterval, actionCount, refreshInterval);

    if (!running || i + 1 >= phases.length) break;

    // Advance to next phase
    self.postMessage({ type: 'phaseAdvance', phaseIndex: i + 1 });
  }

  running = false;
  const totalElapsed = sinceStart();
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
  }
};
