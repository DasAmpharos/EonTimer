//
// Created by Dylan Meadows on 2020-03-10.
//

#include "ApplicationPane.h"
#include "Gen4TimerPane.h"
#include "TimerDisplayPane.h"
#include <QGridLayout>
#include <QTabWidget>
#include <QPushButton>

namespace gui {
    ApplicationPane::ApplicationPane(QWidget *parent)
        : QWidget(parent) {
        auto *timerService = new service::TimerService();
        gen4TimerPane = new Gen4TimerPane(timerService);
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

    void ApplicationPane::onUpdate() {
        gen4TimerPane->calibrateTimer();
    }
}