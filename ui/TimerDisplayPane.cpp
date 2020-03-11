//
// Created by Dylan Meadows on 2020-03-10.
//

#include <QGroupBox>
#include <QVBoxLayout>
#include "TimerDisplayPane.h"
#include "util/FontHelper.h"

namespace ui {
    TimerDisplayPane::TimerDisplayPane(service::TimerService *timerService)
        : QGroupBox(nullptr) {
        currentStage = new QLabel("0:000");
        connect(timerService, &service::TimerService::currentStageChanged, [=](long val) {
            currentStage->setText(QString::number(val));
        });
        minutesBeforeTarget = new QLabel("0");
        connect(timerService, &service::TimerService::minutesBeforeTargetChanged, [=](long val) {
            minutesBeforeTarget->setText(QString::number(val));
        });
        nextStage = new QLabel("0:000");
        connect(timerService, &service::TimerService::nextStageChanged, [=](long val) {
            nextStage->setText(QString::number(val));
        });
        initComponents();
    }

    void TimerDisplayPane::initComponents() {
        // --- group ---
        {
            auto *rootLayout = new QVBoxLayout(this);
            // ----- currentStage ----
            {
                rootLayout->addWidget(currentStage);
                rootLayout->setAlignment(currentStage, Qt::AlignCenter);
                util::font::setFontSize(currentStage, 36);
            }
            // ----- minutesBeforeTarget -----
            {
                auto *layout = new QHBoxLayout();
                layout->setSpacing(5);
                rootLayout->addLayout(layout);
                rootLayout->setAlignment(layout, Qt::AlignCenter);
                layout->addWidget(new QLabel("Minutes Before Target:"));
                layout->addWidget(minutesBeforeTarget);
            }
            // ----- nextStage -----
            {
                auto *layout = new QHBoxLayout();
                layout->setSpacing(5);
                rootLayout->addLayout(layout);
                rootLayout->setAlignment(layout, Qt::AlignCenter);
                layout->addWidget(new QLabel("Next Stage:"));
                layout->addWidget(nextStage);
            }
        }
    }
}
