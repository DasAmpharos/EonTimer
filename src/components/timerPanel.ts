export interface TimerPanelData {
  phases: number[];
  minutesBeforeTarget: number | null;
}

export interface TimerPanelHandle {
  createDisplayData: () => TimerPanelData;
  calibrate: () => void;
  canCalibrate: () => boolean;
  reset: () => void;
}
