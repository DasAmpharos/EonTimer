//
// Created by Dylan Meadows on 2020-03-10.
//

#include <QGroupBox>
#include <QVBoxLayout>
#include "TimerDisplayPane.h"
#include "util/FontHelper.h"
#include <models/TimerState.h>

namespace gui {
    TimerDisplayPane::TimerDisplayPane(service::TimerService *timerService)
        : QGroupBox(nullptr) {
        currentStage = new QLabel("0:000");
        connect(timerService, &service::TimerService::stateChanged,
                [this](const model::TimerState &state) {
                    this->currentStage->setText(formatTime(state.remaining));
                });
        minutesBeforeTarget = new QLabel("0");
        connect(timerService, &service::TimerService::minutesBeforeTargetChanged,
                [this](const int minutesBeforeTarget) {
                    this->minutesBeforeTarget->setText(QString::number(minutesBeforeTarget));
                });
        nextStage = new QLabel("0:000");
        connect(timerService, &service::TimerService::nextStageChanged,
                [this](const int nextStage) {
                    this->nextStage->setText(formatTime(nextStage));
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

    const QString TimerDisplayPane::formatTime(const int milliseconds) const {
        if (milliseconds > 0) {
            return QString::number(milliseconds / 1000) + ":" +
                   QString::number(milliseconds % 1000).rightJustified(3, '0');
        } else if (milliseconds < 0) {
            return "?:???";
        } else {
            return "0:000";
        }
    }
}
