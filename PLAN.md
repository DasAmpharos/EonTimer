# Plan: Test Infrastructure for EonTimer

## Problem
The project has zero tests and the PR workflow only runs lint, typecheck, and format. Dependency upgrades (e.g. vite/plugin-react major version mismatch) can merge without any signal that they break the app.

## Approach
Four phases in order of effort and ROI, from quickest win to deepest coverage.

---

## Phase 1: Add Build Check to PR Workflow (Quick Win)
Add a `build` job to `.github/workflows/pr.yml` that runs `npm run build`.
This alone would have caught the vite/plugin-react major version mismatch.
No new dependencies needed.

---

## Phase 2: Test Infrastructure Setup
Install and configure the test stack:
- **Vitest** — runs natively through vite.config.ts, no separate bundler needed
- **@testing-library/react** — component rendering
- **@testing-library/jest-dom** — DOM matchers (toBeInTheDocument, etc.)
- **@testing-library/user-event** — user interaction simulation
- **jsdom** — browser environment for component tests
- **@vitest/coverage-v8** — coverage reports

Changes:
- Add `test`, `test:run`, and `test:coverage` scripts to `package.json`
- Add a `test` section to `vite.config.ts` (vitest config lives here)
- Add `src/test/setup.ts` that imports `@testing-library/jest-dom`

---

## Phase 3: Unit Tests — Timer Logic (Highest Value)
Pure functions with no React/DOM dependencies. These catch TypeScript breakage,
logic regressions, and library API changes in zustand/vite at build time.

Files to test (one `.test.ts` per source file):
- `src/timers/calibrator.test.ts` — toDelays, toMilliseconds, createCalibration
- `src/timers/frameTimer.test.ts` — createFramePhases, createFramePhase, calibrateFrame
- `src/timers/secondTimer.test.ts` — createSecondPhases, calibrateSecond
- `src/timers/delayTimer.test.ts` — createDelayPhases, calibrateDelay
- `src/timers/gen3Timer.test.ts` — createGen3Phases, calibrateGen3
- `src/timers/gen4Timer.test.ts` — createGen4Phases, calibrateGen4, getGen4MinutesBeforeTarget
- `src/timers/gen5Timer.test.ts` — createGen5Phases, calibrateGen5, getGen5MinutesBeforeTarget
- `src/timers/customTimer.test.ts` — createCustomPhases, calibrateCustomPhase
- `src/utils/constants.test.ts` — toMinimumLength, getMinutesBeforeTarget

Focus: known inputs → expected outputs, edge cases (zero targets, max values, calibration deltas).

---

## Phase 4: Component Smoke Tests
Render each component, assert it mounts without throwing, and verify key elements are present.
Mocks needed: zustand store (pre-seeded with defaults), Web Audio API, Web Workers.

Components to smoke test:
- `src/components/App.tsx`
- `src/components/TimerDisplay.tsx`
- `src/components/Gen3Panel.tsx`
- `src/components/Gen4Panel.tsx`
- `src/components/Gen5Panel.tsx`
- `src/components/CustomPanel.tsx`
- `src/components/SettingsDialog.tsx`
- `src/components/common/IntInput.tsx`
- `src/components/common/FloatInput.tsx`
- `src/components/common/EnumSelect.tsx`

---

## Phase 5: Hook Tests
- `useUrlParams` — mock `window.location.search`, verify store state is applied correctly
- `usePhaseRunner` — mock Web Worker, test start/stop/toggle state transitions and worker message handling

---

## Phase 6: Add Test Job to PR Workflow
Update `.github/workflows/pr.yml` to add:
- `build` job (from Phase 1, combined here if not done already)
- `test` job running `npm run test:run` with coverage reporting

---

## Notes
- Test files live alongside source (`src/timers/gen3Timer.test.ts`) not in a separate `__tests__` dir
- Vitest globals (`describe`, `it`, `expect`) enabled in config so no import needed in test files
- Web Worker in `usePhaseRunner` will need a mock — Vitest supports `vi.mock` and a manual `__mocks__` file
- Web Audio API (`AudioContext`) needs `jsdom` polyfill or `vi.stubGlobal`

---

## Session State
Session UUID: `0e285e76-2895-44e1-a713-bf92516f37d1`
Full path: `~/.copilot/session-state/0e285e76-2895-44e1-a713-bf92516f37d1/`
