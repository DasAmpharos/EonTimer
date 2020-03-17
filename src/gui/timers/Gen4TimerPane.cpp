//
// Created by Dylan Meadows on 2020-03-09.
//

#include "Gen4TimerPane.h"
#include <QVBoxLayout>
#include <QGroupBox>
#include <QFormLayout>
#include <QLabel>
#include <QSpinBox>

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
        void (QSpinBox::* valueChanged)(int) = &QSpinBox::valueChanged;
        const auto updateTimer = std::bind(&Gen4TimerPane::updateTimer, this);
        // --- fields ---
        {
            auto *group = new QGroupBox();
            auto *layout = new QFormLayout(group);
            layout->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            group->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Expanding
            );
            // ----- calibratedDelay -----
            {
                auto *label = new QLabel();
                label->setText("Calibrated Delay");
                calibratedDelay = new QSpinBox();
                calibratedDelay->setRange(-10000, 10000);
                calibratedDelay->setValue(settings->getCalibratedDelay());
                connect(calibratedDelay, valueChanged, updateTimer);
                calibratedDelay->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                layout->addRow(label, calibratedDelay);
            }
            // ----- calibratedSecond -----
            {
                auto *label = new QLabel();
                label->setText("Calibrated Second");
                calibratedSecond = new QSpinBox();
                calibratedSecond->setRange(-10000, 10000);
                calibratedSecond->setValue(settings->getCalibratedSecond());
                connect(calibratedSecond, valueChanged, updateTimer);
                calibratedSecond->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                layout->addRow(label, calibratedSecond);
            }
            // ----- targetDelay -----
            {
                auto *label = new QLabel();
                label->setText("Target Delay");
                targetDelay = new QSpinBox();
                targetDelay->setRange(0, 10000);
                targetDelay->setValue(settings->getTargetDelay());
                connect(targetDelay, valueChanged, updateTimer);
                targetDelay->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                layout->addRow(label, targetDelay);
            }
            // ----- targetSecond -----
            {
                auto *label = new QLabel();
                label->setText("Target Second");
                targetSecond = new QSpinBox();
                targetSecond->setRange(0, 10000);
                targetSecond->setValue(settings->getTargetSecond());
                connect(targetSecond, valueChanged, updateTimer);
                targetSecond->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                layout->addRow(label, targetSecond);
            }
            rootLayout->addWidget(group);
        }
        // ----- delayHit -----
        {
            auto *layout = new QFormLayout();
            layout->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            rootLayout->addLayout(layout);

            auto *label = new QLabel();
            delayHit = new QSpinBox();
            layout->addRow(label, delayHit);
            label->setText("Delay Hit");
            delayHit->setRange(0, 10000);
            delayHit->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
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