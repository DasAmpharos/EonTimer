//
// Created by Dylan Meadows on 2020-03-16.
//

#include "TimerSettingsPane.h"
#include <iostream>

namespace gui::settings {
    TimerSettingsPane::TimerSettingsPane(service::settings::TimerSettings *settings, QWidget *parent)
        : QWidget(parent),
          settings(settings) {
        initComponents();
    }

    void TimerSettingsPane::initComponents() {
    }

    void TimerSettingsPane::updateSettings() {
        std::cout << "gui::settings::TimerSettingsPane::updateSettings" << std::endl;
    }
}
