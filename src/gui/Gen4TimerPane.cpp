//
// Created by Dylan Meadows on 2020-03-09.
//

#include "Gen4TimerPane.h"
#include <QVBoxLayout>
#include <QGroupBox>
#include <QFormLayout>
#include <QLabel>
#include <QSpinBox>

using namespace service;
using namespace service::timer;

namespace gui {
    Gen4TimerPane::Gen4TimerPane(const service::timer::DelayTimer *delayTimer,
                                 const service::CalibrationService *calibrationService,
                                 service::TimerService *timerService)
        : QWidget(nullptr),
          delayTimer(delayTimer),
          calibrationService(calibrationService),
          timerService(timerService) {
        targetDelay = new QSpinBox();
        targetSecond = new QSpinBox();
        calibratedDelay = new QSpinBox();
        calibratedSecond = new QSpinBox();
        delayHit = new QSpinBox();
        initComponents();
    }

    void Gen4TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        void (QSpinBox::* valueChanged)(int) = &QSpinBox::valueChanged;
        // --- fields ---
        {
            auto *group = new QGroupBox();
            auto *layout = new QFormLayout(group);
            layout->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            // ----- targetDelay -----
            {
                auto *label = new QLabel();
                label->setText("Target Delay");
                layout->addRow(label, targetDelay);
                connect(targetDelay, valueChanged, [this]() {
                    updateTimer();
                });
                targetDelay->setRange(0, 10000);
                targetDelay->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
            }
            // ----- targetSecond -----
            {
                auto *label = new QLabel();
                label->setText("Target Second");
                layout->addRow(label, targetSecond);
                connect(targetSecond, valueChanged, [this]() {
                    updateTimer();
                });
                targetSecond->setRange(0, 10000);
                targetSecond->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
            }
            // ----- calibratedDelay -----
            {
                auto *label = new QLabel();
                label->setText("Calibrated Delay");
                layout->addRow(label, calibratedDelay);
                connect(calibratedDelay, valueChanged, [this]() {
                    updateTimer();
                });
                calibratedDelay->setRange(-10000, 10000);
                calibratedDelay->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
            }
            // ----- calibratedSecond -----
            {
                auto *label = new QLabel();
                label->setText("Calibrated Second");
                layout->addRow(label, calibratedSecond);
                connect(calibratedSecond, valueChanged, [this]() {
                    updateTimer();
                });
                calibratedSecond->setRange(-10000, 10000);
                calibratedSecond->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
            }
            rootLayout->addWidget(group);
        }
        // ----- delayHit -----
        {
            auto *layout = new QFormLayout();
            layout->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            rootLayout->addLayout(layout);

            auto *label = new QLabel();
            label->setText("Delay Hit");
            layout->addRow(label, delayHit);
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