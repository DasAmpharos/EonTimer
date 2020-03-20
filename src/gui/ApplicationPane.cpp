//
// Created by Dylan Meadows on 2020-03-10.
//

#include "ApplicationPane.h"
#include <QGridLayout>
#include <QTabWidget>
#include <QPushButton>
#include <QFontDatabase>
#include <QDebug>
#include <gui/dialogs/SettingsDialog.h>

namespace gui {
    ApplicationPane::ApplicationPane(QSettings *settings,
                                     service::settings::ActionSettings *actionSettings,
                                     service::settings::TimerSettings *timerSettings,
                                     service::TimerService *timerService,
                                     QWidget *parent)
        : QWidget(parent),
          timerSettings(timerSettings),
          actionSettings(actionSettings),
          timerService(timerService) {
        auto *calibrationService = new service::CalibrationService(timerSettings);
        auto *delayTimer = new service::timer::DelayTimer(calibrationService, new service::timer::SecondTimer());

        timerDisplayPane = new TimerDisplayPane(timerService);
        auto *gen5TimerSettings = new service::settings::Gen5TimerSettings(settings);
        gen5TimerPane = new timer::Gen5TimerPane(gen5TimerSettings, calibrationService);
        auto *gen4TimerSettings = new service::settings::Gen4TimerSettings(settings);
        gen4TimerPane = new timer::Gen4TimerPane(gen4TimerSettings, delayTimer, calibrationService, timerService);
        connect(timerService, &service::TimerService::activated, [this](const bool activated) {
            this->gen4TimerPane->setEnabled(!activated);
        });
        initComponents();
    }

    void ApplicationPane::initComponents() {
        auto *layout = new QGridLayout(this);
        layout->setColumnMinimumWidth(0, 215);
        layout->setHorizontalSpacing(10);
        layout->setVerticalSpacing(10);
        // ----- timerDisplayPane -----
        {
            layout->addWidget(timerDisplayPane, 0, 0);
            timerDisplayPane->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
        }
        // ----- tabPane -----
        {
            auto *tabPane = new QTabWidget();
            tabPane->setProperty("class", "themeable");
            layout->addWidget(tabPane, 0, 1, 2, 2);
            tabPane->addTab(gen5TimerPane, "5");
            tabPane->addTab(gen4TimerPane, "4");
            // tabPane->addTab(gen5TimerPane, "5");
            // tabPane->addTab(gen3TimerPane, "3");
            // tabPane->addTab(customTimerPane, "C");
            // tabPane->setCurrentIndex(1);
        }
        // ----- settingsBtn -----
        {
            auto *settingsBtn = new QPushButton();
            connect(timerService, &service::TimerService::activated, [settingsBtn](const bool activated) {
                settingsBtn->setEnabled(!activated);
            });
            connect(settingsBtn, &QPushButton::clicked, [this]() {
                dialog::SettingsDialog dialog(timerSettings, actionSettings, this);
                if (dialog.exec() == 0) {
                }
            });
            const auto id = QFontDatabase::addApplicationFont(":/fonts/FontAwesome.ttf");
            const auto family = QFontDatabase::applicationFontFamilies(id)[0];
            settingsBtn->setFont(QFont(family));
            settingsBtn->setText("\uf013");
            settingsBtn->setSizePolicy(
                QSizePolicy::Fixed,
                QSizePolicy::Fixed
            );
            layout->addWidget(settingsBtn, 2, 0);
        }
        // ----- updateBtn -----
        {
            auto *updateBtn = new QPushButton("Update");
            connect(updateBtn, SIGNAL(clicked(bool)), this, SLOT(onUpdate()));
            connect(timerService, &service::TimerService::activated, [updateBtn](const bool activated) {
                updateBtn->setEnabled(!activated);
            });
            layout->addWidget(updateBtn, 2, 1);
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
            layout->addWidget(startStopBtn, 2, 2);
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