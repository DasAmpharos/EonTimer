//
// Created by dmeadows on 5/18/20.
//

#include "ActionMode.h"

namespace model {
    const std::vector<ActionMode> VALUES{ActionMode::AUDIO, ActionMode::VISUAL,
                                         ActionMode::AV};

    namespace names {
        const char* AUDIO = "Audio";
        const char* VISUAL = "Visual";
        const char* AV = "A/V";
    }  // namespace names

    const char* getName(const ActionMode mode) {
        switch (mode) {
            case AUDIO:
                return names::AUDIO;
            case VISUAL:
                return names::VISUAL;
            case AV:
                return names::AV;
            default:
                return "";
        }
    }

    ActionMode actionMode(const unsigned int index) { return VALUES[index]; }

    const std::vector<ActionMode>& actionModes() { return VALUES; }

    int indexOf(const ActionMode mode) {
        for (unsigned int i = 0; i < VALUES.size(); i++) {
            if (mode == VALUES[i]) return i;
        }
        return -1;
    }
}  // namespace model
