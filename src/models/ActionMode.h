//
// Created by dmeadows on 5/18/20.
//

#ifndef EONTIMER_ACTIONMODE_H
#define EONTIMER_ACTIONMODE_H

#include <vector>

namespace model {
    enum ActionMode { AUDIO, VISUAL, AV };

    const char *getName(ActionMode mode);

    ActionMode actionMode(unsigned int index);

    const std::vector<ActionMode> &actionModes();

    int indexOf(const ActionMode &mode);
}  // namespace model

#endif  // EONTIMER_ACTIONMODE_H
