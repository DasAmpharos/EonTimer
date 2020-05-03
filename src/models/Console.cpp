//
// Created by Dylan Meadows on 2020-03-10.
//

#include "Console.h"

namespace model {
    const double GBA_FPS = 59.7275;
    const double NDS_FPS = 59.8261;
    const double NDS_GBA_FPS = 59.6555;
    const double GBA_FRAMERATE = 1000 / GBA_FPS;
    const double NDS_FRAMERATE = 1000 / NDS_FPS;
    const double NDS_GBA_FRAMERATE = 1000 / NDS_GBA_FPS;
    const std::vector<Console> VALUES{Console::GBA, Console::NDS,
                                      Console::NDS_GBA, Console::DSI,
                                      Console::_3DS};
    namespace names {
        const char *GBA = "GBA";
        const char *NDS = "NDS";
        const char *NDS_GBA = "NDS-GBA";
        const char *DSI = "DSI";
        const char *_3DS = "3DS";
    }  // namespace names

    double getFps(const Console console) {
        switch (console) {
            case GBA:
                return GBA_FPS;
            case NDS_GBA:
                return NDS_GBA_FPS;
            case NDS:
            case DSI:
            case _3DS:
                return NDS_FPS;
            default:
                return -1.0;
        }
    }

    double getFramerate(const Console console) {
        switch (console) {
            case GBA:
                return GBA_FRAMERATE;
            case NDS_GBA:
                return NDS_GBA_FRAMERATE;
            case NDS:
            case DSI:
            case _3DS:
                return NDS_FRAMERATE;
            default:
                return -1.0;
        }
    }

    const char *getName(const Console console) {
        switch (console) {
            case GBA:
                return names::GBA;
            case NDS:
                return names::NDS;
            case NDS_GBA:
                return names::NDS_GBA;
            case DSI:
                return names::DSI;
            case _3DS:
                return names::_3DS;
            default:
                return nullptr;
        }
    }

    Console console(int index) { return VALUES[index]; }

    const std::vector<Console> &consoles() { return VALUES; }

    int indexOf(Console console) {
        for (int i = 0; i < VALUES.size(); i++) {
            if (console == VALUES[i]) return i;
        }
        return -1;
    }
}  // namespace model
