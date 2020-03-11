//
// Created by Dylan Meadows on 2020-03-10.
//

#include "Console.h"

namespace model {
    const double GBA_FPS = 59.7271;
    const double NDS_FPS = 59.8261;
    const double GBA_FRAMERATE = 1000 / GBA_FPS;
    const double NDS_FRAMERATE = 1000 / NDS_FPS;

    double getFps(const Console console) {
        switch (console) {
            case GBA:
                return GBA_FPS;
            case NDS:
                return NDS_FPS;
            default:
                return -1.0;
        }
    }

    double getFramerate(const Console console) {
        switch (console) {
            case GBA:
                return GBA_FRAMERATE;
            case NDS:
                return NDS_FRAMERATE;
            default:
                return -1.0;
        }
    }
}
