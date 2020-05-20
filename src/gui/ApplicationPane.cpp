//
// Created by Dylan Meadows on 2020-03-10.
//

#include "ApplicationPane.h"

#include <gui/dialogs/SettingsDialog.h>

#include <QFontDatabase>
#include <QPushButton>

namespace gui {
    const uint GEN5 = 0;
    const uint GEN4 = 1;
    const uint GEN3 = 2;
    const uint CUSTOM = 3;
    namespace Fields {
        const char *SELECTED_TAB = "selectedTab";
    }

    ApplicationPane::ApplicationPane(QSettings *settings,
                                     model::settings::ActionSettingsModel *actionSettings,
                                     model::settings::TimerSettingsModel *timerSettings,
                                     model::timer::Gen5TimerModel *gen5Timer,
                                     model::timer::Gen4TimerModel *gen4Timer,
                                     model::timer::Gen3TimerModel *gen3Timer,
                                     service::TimerService *timerService,
                                     QWidget *parent)
        : QWidget(parent),
          settings(settings),
          actionSettings(actionSettings),
          timerSettings(timerSettings),
          timerService(timerService) {
        const auto *calibrationService = new service::CalibrationService(timerSettings);
        const auto *secondTimer = new service::timer::SecondTimer();
        const auto *frameTimer = new service::timer::FrameTimer(calibrationService);
        const auto *delayTimer = new service::timer::DelayTimer(secondTimer, calibrationService);
        const auto *entralinkTimer = new service::timer::EntralinkTimer(delayTimer);
        const auto *enhancedEntralinkTimer = new service::timer::EnhancedEntralinkTimer(entralinkTimer);

        timerDisplayPane = new TimerDisplayPane(timerService, actionSettings);
        gen5TimerPane = new timer::Gen5TimerPane(gen5Timer,
                                                 delayTimer,
                                                 secondTimer,
                                                 entralinkTimer,
                                                 enhancedEntralinkTimer,
                                                 calibrationService);
        gen4TimerPane = new timer::Gen4TimerPane(gen4Timer, delayTimer, calibrationService);
        gen3TimerPane = new timer::Gen3TimerPane(gen3Timer, frameTimer, calibrationService);
        connect(gen5TimerPane,
                &timer::Gen5TimerPane::timerChanged,
                [timerService](std::shared_ptr<std::vector<int>> stages) { timerService->setStages(stages); });
        connect(gen4TimerPane,
                &timer::Gen4TimerPane::timerChanged,
                [timerService](std::shared_ptr<std::vector<int>> stages) { timerService->setStages(stages); });
        connect(gen3TimerPane,
                &timer::Gen3TimerPane::timerChanged,
                [timerService](std::shared_ptr<std::vector<int>> stages) { timerService->setStages(stages); });
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
            timerDisplayPane->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
        }
        // ----- tabPane -----
        {
            auto *tabPane = new QTabWidget();
            tabPane->setProperty("class", "themeable-panel themeable-border");
            connect(timerService, &service::TimerService::activated, [tabPane](const bool activated) {
                tabPane->setEnabled(!activated);
            });
            connect(tabPane, &QTabWidget::currentChanged, [this](const int index) { setSelectedTab((uint) index); });
            layout->addWidget(tabPane, 0, 1, 2, 2);
            tabPane->addTab(gen5TimerPane, "5");
            tabPane->addTab(gen4TimerPane, "4");
            tabPane->addTab(gen3TimerPane, "3");
            tabPane->setCurrentIndex(getSelectedTab());
        }
        // ----- settingsBtn -----
        {
            auto *settingsBtn = new QPushButton();
            settingsBtn->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);
            const auto id = QFontDatabase::addApplicationFont(":/fonts/FontAwesome.ttf");
            const auto family = QFontDatabase::applicationFontFamilies(id)[0];
            settingsBtn->setFont(QFont(family));
            settingsBtn->setText(QChar(0xf013));
            connect(timerService, &service::TimerService::activated, [settingsBtn](const bool activated) {
                settingsBtn->setEnabled(!activated);
            });
            connect(settingsBtn, &QPushButton::clicked, [this] {
                auto *settingsDialog = new dialog::SettingsDialog(timerSettings, actionSettings, this);
                if (settingsDialog->exec() == QDialog::Accepted) {
                    timerSettings->sync(settings);
                    actionSettings->sync(settings);
                    updateTimer();
                }
                delete settingsDialog;
            });
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
            updateBtn->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
        }
        // ----- startStopBtn -----
        {
            auto *startStopBtn = new QPushButton("Start");
            connect(timerService, &service::TimerService::activated, [startStopBtn](const bool activated) {
                startStopBtn->setText(activated ? "Stop" : "Start");
            });
            connect(startStopBtn, &QPushButton::clicked, [this] {
                if (!timerService->isRunning()) {
                    timerService->start();
                } else {
                    timerService->stop();
                }
            });
            layout->addWidget(startStopBtn, 2, 2);
            startStopBtn->setDefault(true);
            startStopBtn->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
        }
    }

    void ApplicationPane::updateTimer() {
        std::shared_ptr<std::vector<int>> stages;
        switch (getSelectedTab()) {
            case GEN5:
                stages = gen5TimerPane->createStages();
                break;
            case GEN4:
                stages = gen4TimerPane->createStages();
                break;
            case GEN3:
                stages = gen3TimerPane->createStages();
                break;
            case CUSTOM:
                stages = std::make_shared<std::vector<int>>(1);
                (*stages)[0] = 10000;
                break;
        }
        timerService->setStages(stages);
    }

    void ApplicationPane::onUpdate() {
        switch (getSelectedTab()) {
            case GEN5:
                gen5TimerPane->calibrate();
                break;
            case GEN4:
                gen4TimerPane->calibrate();
                break;
            case GEN3:
                gen3TimerPane->calibrate();
                break;
        }
    }

    uint ApplicationPane::getSelectedTab() const { return settings->value(Fields::SELECTED_TAB, GEN5).toUInt(); }

    void ApplicationPane::setSelectedTab(uint selectedTab) {
        settings->setValue(Fields::SELECTED_TAB, selectedTab);
        updateTimer();
    }
}  // namespace gui
