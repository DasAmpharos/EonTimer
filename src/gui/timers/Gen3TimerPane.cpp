//
// Created by Dylan Meadows on 2020-03-26.
//

#include "Gen3TimerPane.h"
#include <QVBoxLayout>
#include <QGroupBox>
#include <QLabel>

namespace gui::timer {
    Gen3TimerPane::Gen3TimerPane(service::settings::Gen3TimerSettings *settings,
                                 const service::timer::FrameTimer *frameTimer,
                                 const service::CalibrationService *calibrationService,
                                 service::TimerService *timerService,
                                 QWidget *parent)
        : QWidget(parent),
          settings(settings),
          frameTimer(frameTimer),
          calibrationService(calibrationService),
          timerService(timerService) {
        initComponents();
    }

    void Gen3TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        rootLayout->setContentsMargins(10, 0, 10, 10);
        rootLayout->setSpacing(10);

        void (QSpinBox::* valueChanged)(int) = &QSpinBox::valueChanged;
        // ----- timer fields -----
        {
            // group
            auto *group = new QGroupBox();
            group->setProperty("class", "themeable");
            group->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
            rootLayout->addWidget(group);
            // group layout
            auto *groupLayout = new QVBoxLayout(group);
            groupLayout->setContentsMargins(0, 0, 0, 0);
            groupLayout->setSpacing(0);
            // form
            auto *form = new QWidget();
            form->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            groupLayout->addWidget(form, 0, Qt::AlignTop);
            // form layout
            auto *formLayout = new QGridLayout(form);
            formLayout->setSpacing(10);
            // ----- calibration -----
            {
                calibration = new QSpinBox();
                calibration->setRange(INT_MIN, INT_MAX);
                calibration->setValue(settings->getCalibration());
                calibration->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(calibration, valueChanged, [this](const int calibration) {
                    settings->setCalibration(calibration);
                    updateTimer();
                });
                formLayout->addWidget(new QLabel("Calibration"), 0, 0);
                formLayout->addWidget(calibration, 0, 1);
            }
            // ----- preTimer -----
            {
                preTimer = new QSpinBox();
                preTimer->setRange(0, INT_MAX);
                preTimer->setValue(settings->getPreTimer());
                preTimer->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(preTimer, valueChanged, [this](const int preTimer) {
                    settings->setPreTimer(preTimer);
                    updateTimer();
                });
                formLayout->addWidget(new QLabel("Pre-Timer"), 1, 0);
                formLayout->addWidget(preTimer, 1, 1);
            }
            // ----- targetFrame -----
            {
                targetFrame = new QSpinBox();
                targetFrame->setRange(0, INT_MAX);
                targetFrame->setValue(settings->getTargetFrame());
                targetFrame->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(targetFrame, valueChanged, [this](const int targetFrame) {
                    settings->setTargetFrame(targetFrame);
                    updateTimer();
                });
                formLayout->addWidget(new QLabel("Target Frame"), 2, 0);
                formLayout->addWidget(targetFrame, 2, 1);
            }
        }
        // ----- frameHit -----
        {
            auto *form = new QGridLayout();
            rootLayout->addLayout(form);
            form->setSpacing(10);

            frameHit = new QSpinBox();
            frameHit->setRange(0, INT_MAX);
            frameHit->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            form->addWidget(new QLabel("Frame Hit"), 0, 0);
            form->addWidget(frameHit, 0, 1);
        }
    }

    void Gen3TimerPane::calibrateTimer() {
        const int calibration = frameTimer->calibrate(
            targetFrame->value(),
            frameHit->value()
        );
        this->calibration->setValue(this->calibration->value() + calibration);
    }

    void Gen3TimerPane::updateTimer() {
        timerService->setStages(
            frameTimer->createStages(preTimer->value(), targetFrame->value(), calibration->value())
        );
    }
}
