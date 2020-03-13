//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_CALIBRATIONHELPER_H
#define EONTIMER_CALIBRATIONHELPER_H

namespace service::CalibrationService {
    int toDelays(int milliseconds);

    int toMilliseconds(int delays);

    int createCalibration(int delays, int seconds);
}

#endif //EONTIMER_CALIBRATIONHELPER_H
