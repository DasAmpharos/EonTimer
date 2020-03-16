//
// Created by Dylan Meadows on 2020-03-15.
//

#include "SettingsDialog.h"
#include <QVBoxLayout>
#include <QTabWidget>
#include <QPushButton>

namespace gui::dialog {
    SettingsDialog::SettingsDialog(service::settings::TimerSettings *timerSettings,
                                   service::settings::ActionSettings *actionSettings,
                                   QWidget *parent)
        : QDialog(parent),
          timerSettings(timerSettings),
          actionSettings(actionSettings) {
        initComponents();
    }

    void SettingsDialog::initComponents() {
        auto *layout = new QVBoxLayout(this);
        layout->setSpacing(10);
        // ----- tabPane -----
        {
            auto *tabPane = new QTabWidget();
            layout->addWidget(tabPane);
        }
        // ----- buttonBar -----
        {
            auto *buttonBar = new QHBoxLayout();
            // ----- cancelBtn -----
            {
                auto *cancelButton = new QPushButton("Cancel");
                buttonBar->addWidget(cancelButton);
            }
            // ----- okBtn -----
            {
                auto *okButton = new QPushButton("OK");
                buttonBar->addWidget(okButton);
                okButton->setDefault(true);
            }
            layout->addLayout(buttonBar);
        }
    }
}
