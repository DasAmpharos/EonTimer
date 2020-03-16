//
// Created by Dylan Meadows on 2020-03-16.
//

#include "ActionSettingsPane.h"
#include <iostream>

namespace gui::settings {
    ActionSettingsPane::ActionSettingsPane(service::settings::ActionSettings *settings, QWidget *parent)
        : QWidget(parent),
          settings(settings) {
        initComponents();
    }

    void ActionSettingsPane::initComponents() {
    }

    void ActionSettingsPane::sync() {
        std::cout << "gui::settings::ActionSettingsPane::sync" << std::endl;
    }
}
