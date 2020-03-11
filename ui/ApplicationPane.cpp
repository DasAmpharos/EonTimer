//
// Created by Dylan Meadows on 2020-03-10.
//

#include "ApplicationPane.h"
#include "Gen5TimerPane.h"
#include "Gen4TimerPane.h"
#include "Gen3TimerPane.h"
#include "CustomTimerPane.h"
#include "TimerDisplayPane.h"
#include "../services/CalibrationService.h"
#include <QGridLayout>
#include <QTabWidget>
#include <QPushButton>

namespace ui {
    ApplicationPane::ApplicationPane(QWidget *parent)
        : QWidget(parent) {
        auto *timerService = new service::TimerService();
        auto *settingsService = new service::SettingsService();
        auto *calibrationService = new service::CalibrationService(settingsService);

        gen5TimerPane = new Gen5TimerPane();
        gen4TimerPane = new Gen4TimerPane(timerService, settingsService, calibrationService);
        gen3TimerPane = new Gen3TimerPane();
        customTimerPane = new CustomTimerPane();
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
            // tabPane->addTab(gen5TimerPane, "5");
            tabPane->addTab(gen4TimerPane, "4");
            // tabPane->addTab(gen3TimerPane, "3");
            // tabPane->addTab(customTimerPane, "C");
            // tabPane->setCurrentIndex(1);
        }
        // ----- updateBtn -----
        {
            auto *updateBtn = new QPushButton("Update");
            layout->addWidget(updateBtn, 2, 0);
            updateBtn->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
        }
        // ----- startStopBtn -----
        {
            auto *startStopBtn = new QPushButton("Start");
            layout->addWidget(startStopBtn, 2, 1);
            startStopBtn->setDefault(true);
            startStopBtn->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
        }
    }
}