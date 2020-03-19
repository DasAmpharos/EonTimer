//
// Created by Dylan Meadows on 2020-03-10.
//

#include <QGroupBox>
#include <QVBoxLayout>
#include "TimerDisplayPane.h"
#include <QFontDatabase>

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
                [this](const std::chrono::minutes &minutesBeforeTarget) {
                    this->minutesBeforeTarget->setText(QString::number(minutesBeforeTarget.count()));
                });
        nextStage = new QLabel("0:000");
        connect(timerService, &service::TimerService::nextStageChanged,
                [this](const std::chrono::milliseconds &nextStage) {
                    this->nextStage->setText(formatTime(nextStage));
                });
        initComponents();
    }

    void TimerDisplayPane::initComponents() {
        // --- group ---
        {
            setProperty("class", "themeable");
            auto *rootLayout = new QVBoxLayout(this);
            rootLayout->setSpacing(5);
            // ----- currentStage ----
            {
                rootLayout->addWidget(currentStage);
                rootLayout->setAlignment(currentStage, Qt::AlignLeft);
                const int font = QFontDatabase::addApplicationFont(":/fonts/RobotoMono-Regular.ttf");
                const QString family = QFontDatabase::applicationFontFamilies(font)[0];
                currentStage->setFont(QFont(family, 36));
            }
            // ----- minutesBeforeTarget -----
            {
                auto *layout = new QHBoxLayout();
                layout->setSpacing(5);
                rootLayout->addLayout(layout);
                rootLayout->setAlignment(layout, Qt::AlignLeft);
                layout->addWidget(new QLabel("Minutes Before Target:"));
                layout->addWidget(minutesBeforeTarget);
            }
            // ----- nextStage -----
            {
                auto *layout = new QHBoxLayout();
                layout->setSpacing(5);
                rootLayout->addLayout(layout);
                rootLayout->setAlignment(layout, Qt::AlignLeft);
                layout->addWidget(new QLabel("Next Stage:"));
                layout->addWidget(nextStage);
            }
        }
    }

    const QString TimerDisplayPane::formatTime(const std::chrono::milliseconds &milliseconds) const {
        const auto ms = milliseconds.count();
        if (ms > 0) {
            return QString::number(ms / 1000) + ":" +
                   QString::number(ms % 1000).rightJustified(3, '0');
        } else if (ms < 0) {
            return "?:???";
        } else {
            return "0:000";
        }
    }
}
