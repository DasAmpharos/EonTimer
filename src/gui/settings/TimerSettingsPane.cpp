//
// Created by Dylan Meadows on 2020-03-16.
//

#include "TimerSettingsPane.h"
#include <iostream>
#include <QFormLayout>
#include <QLabel>

namespace gui::settings {
    TimerSettingsPane::TimerSettingsPane(service::settings::TimerSettings *settings, QWidget *parent)
        : QWidget(parent),
          settings(settings) {
        initComponents();
    }

    void TimerSettingsPane::initComponents() {
        auto *layout = new QFormLayout(this);
        layout->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
        // ----- console -----
        {
            console = new QComboBox();
            layout->addRow("Console", console);
            for (auto &mConsole : model::consoles()) {
                console->addItem(model::getName(mConsole), mConsole);
            }
            console->setCurrentText(model::getName(settings->getConsole()));
            console->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
        }
        // ----- refreshInterval -----
        {
            refreshInterval = new QSpinBox();
            layout->addRow("Refresh Interval", refreshInterval);
            refreshInterval->setRange(1, 1000);
            refreshInterval->setValue(static_cast<int>(settings->getRefreshInterval().count()));
            refreshInterval->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
        }
        // ----- precisionCalibrationEnabled -----
        {
            precisionCalibrationEnabled = new QCheckBox();
            layout->addRow("Precision Calibration", precisionCalibrationEnabled);
            precisionCalibrationEnabled->setChecked(settings->isPrecisionCalibrationEnabled());
            precisionCalibrationEnabled->setTristate(false);
        }
    }

    void TimerSettingsPane::updateSettings() {
        settings->setConsole(model::consoles()[console->currentIndex()]);
        settings->setRefreshInterval(std::chrono::milliseconds(refreshInterval->value()));
        settings->setPrecisionCalibrationEnabled(precisionCalibrationEnabled->isChecked());
    }
}
