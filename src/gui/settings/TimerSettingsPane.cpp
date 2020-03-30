//
// Created by Dylan Meadows on 2020-03-16.
//

#include "TimerSettingsPane.h"
#include <iostream>
#include <QFormLayout>
#include <QLabel>

namespace gui::settings {
    TimerSettingsPane::TimerSettingsPane(model::settings::TimerSettingsModel *model, QWidget *parent)
        : QWidget(parent),
          model(model) {
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
            console->setCurrentText(model::getName(model->getConsole()));
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
            refreshInterval->setValue(static_cast<int>(model->getRefreshInterval().count()));
            refreshInterval->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
        }
        // ----- precisionCalibrationEnabled -----
        {
            precisionCalibrationEnabled = new QCheckBox();
            layout->addRow("Precision Calibration", precisionCalibrationEnabled);
            precisionCalibrationEnabled->setChecked(model->isPrecisionCalibrationEnabled());
            precisionCalibrationEnabled->setTristate(false);
        }
    }

    void TimerSettingsPane::updateSettings() {
        model->setConsole(model::consoles()[console->currentIndex()]);
        model->setRefreshInterval(std::chrono::milliseconds(refreshInterval->value()));
        model->setPrecisionCalibrationEnabled(precisionCalibrationEnabled->isChecked());
    }
}
