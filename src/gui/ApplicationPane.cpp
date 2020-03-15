//
// Created by Dylan Meadows on 2020-03-10.
//

#include "ApplicationPane.h"
#include "Gen4TimerPane.h"
#include "TimerDisplayPane.h"
#include <QGridLayout>
#include <QTabWidget>
#include <QPushButton>
#include <services/timer/SecondTimer.h>
#include <services/timer/DelayTimer.h>

namespace gui {
    ApplicationPane::ApplicationPane(QSettings *settings, QWidget *parent)
        : QWidget(parent) {
        auto *timerSettings = new service::settings::TimerSettings(settings);
        auto *actionSettings = new service::settings::ActionSettings(settings);

        auto *soundService = new service::SoundService(actionSettings, this);
        auto *calibrationService = new service::CalibrationService(timerSettings);
        auto *delayTimer = new service::timer::DelayTimer(calibrationService, new service::timer::SecondTimer());

        timerService = new service::TimerService(timerSettings, actionSettings, soundService, this);
        gen4TimerPane = new Gen4TimerPane(delayTimer, calibrationService, timerService);
        connect(timerService, &service::TimerService::activated, [this](const bool activated) {
            this->gen4TimerPane->setEnabled(!activated);
        });
        timerDisplayPane = new TimerDisplayPane(timerService);
        initComponents();
    }

    void ApplicationPane::initComponents() {
        auto *layout = new QGridLayout(this);
        layout->addWidget(timerDisplayPane, 0, 0, 1, 2);
        // ----- tabPane -----
        {
            auto *tabPane = new QTabWidget();
            layout->addWidget(tabPane, 1, 0, 1, 2);
            tabPane->addTab(gen4TimerPane, "4");
            // tabPane->addTab(gen5TimerPane, "5");
            // tabPane->addTab(gen3TimerPane, "3");
            // tabPane->addTab(customTimerPane, "C");
            // tabPane->setCurrentIndex(1);
        }
        // ----- updateBtn -----
        {
            auto *updateBtn = new QPushButton("Update");
            connect(updateBtn, SIGNAL(clicked(bool)), this, SLOT(onUpdate()));
            connect(timerService, &service::TimerService::activated, [updateBtn](const bool activated) {
                updateBtn->setEnabled(!activated);
            });
            layout->addWidget(updateBtn, 2, 0);
            updateBtn->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
        }
        // ----- startStopBtn -----
        {
            auto *startStopBtn = new QPushButton("Start");
            connect(timerService, &service::TimerService::activated, [startStopBtn](const bool activated) {
                if (activated) {
                    startStopBtn->setText("Stop");
                } else {
                    startStopBtn->setText("Start");
                }
            });
            connect(startStopBtn, &QPushButton::clicked, [this, startStopBtn]() {
                if (!timerService->isRunning()) {
                    timerService->start();
                } else {
                    timerService->stop();
                }
            });
            layout->addWidget(startStopBtn, 2, 1);
            startStopBtn->setDefault(true);
            startStopBtn->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
        }
    }

    void ApplicationPane::onUpdate() {
        gen4TimerPane->calibrateTimer();
    }
}