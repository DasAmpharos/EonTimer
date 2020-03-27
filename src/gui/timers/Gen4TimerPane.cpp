//
// Created by Dylan Meadows on 2020-03-09.
//

#include "Gen4TimerPane.h"
#include <QVBoxLayout>
#include <QGroupBox>
#include <QFormLayout>
#include <QLabel>
#include <QSpinBox>
#include <limits.h>

namespace gui::timer {
    Gen4TimerPane::Gen4TimerPane(service::settings::Gen4TimerSettings *settings,
                                 const service::timer::DelayTimer *delayTimer,
                                 const service::CalibrationService *calibrationService,
                                 service::TimerService *timerService,
                                 QWidget *parent)
        : QWidget(parent),
          settings(settings),
          delayTimer(delayTimer),
          calibrationService(calibrationService),
          timerService(timerService) {
        initComponents();
        updateTimer();
    }

    void Gen4TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        rootLayout->setContentsMargins(10, 0, 10, 10);
        rootLayout->setSpacing(10);

        void (QSpinBox::* valueChanged)(int) = &QSpinBox::valueChanged;
        // --- timer fields ---
        {
            auto *group = new QGroupBox();
            group->setProperty("class", "themeable");
            group->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
            auto *timerForm = new QFormLayout(group);
            timerForm->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            // ----- calibratedDelay -----
            {
                calibratedDelay = new QSpinBox();
                calibratedDelay->setRange(INT_MIN, INT_MAX);
                calibratedDelay->setValue(settings->getCalibratedDelay());
                calibratedDelay->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(calibratedDelay, valueChanged, [this](const int calibratedDelay) {
                    settings->setCalibratedDelay(calibratedDelay);
                    updateTimer();
                });
                timerForm->addRow("Calibrated Delay", calibratedDelay);
            }
            // ----- calibratedSecond -----
            {
                calibratedSecond = new QSpinBox();
                calibratedSecond->setRange(INT_MIN, INT_MAX);
                calibratedSecond->setValue(settings->getCalibratedSecond());
                calibratedSecond->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(calibratedSecond, valueChanged, [this](const int calibratedSecond) {
                    settings->setCalibratedSecond(calibratedSecond);
                    updateTimer();
                });
                timerForm->addRow("Calibrated Second", calibratedSecond);
            }
            // ----- targetDelay -----
            {
                targetDelay = new QSpinBox();
                targetDelay->setRange(0, INT_MAX);
                targetDelay->setValue(settings->getTargetDelay());
                targetDelay->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(targetDelay, valueChanged, [this](const int targetDelay) {
                    settings->setTargetDelay(targetDelay);
                    updateTimer();
                });
                timerForm->addRow("Target Delay", targetDelay);
            }
            // ----- targetSecond -----
            {
                targetSecond = new QSpinBox();
                targetSecond->setRange(0, 59);
                targetSecond->setValue(settings->getTargetSecond());
                targetSecond->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(targetSecond, valueChanged, [this](const int targetSecond) {
                    settings->setTargetSecond(targetSecond);
                    updateTimer();
                });
                timerForm->addRow("Target Second", targetSecond);
            }
            rootLayout->addWidget(group);
        }
        // ----- delayHit -----
        {
            auto *layout = new QFormLayout();
            layout->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            rootLayout->addLayout(layout);

            delayHit = new QSpinBox();
            delayHit->setRange(0, INT_MAX);
            delayHit->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            layout->addRow("Delay Hit", delayHit);
        }
    }

    void Gen4TimerPane::calibrateTimer() {
        const int calibratedDelay = calibrationService->toDelays(
            delayTimer->calibrate(targetDelay->value(), delayHit->value()));
        this->calibratedDelay->setValue(this->calibratedDelay->value() + calibratedDelay);
    }

    void Gen4TimerPane::updateTimer() {
        const int calibration = calibrationService->createCalibration(
            calibratedDelay->value(), calibratedSecond->value());
        timerService->setStages(delayTimer->createStages(
            targetSecond->value(), targetDelay->value(), calibration));
    }
}