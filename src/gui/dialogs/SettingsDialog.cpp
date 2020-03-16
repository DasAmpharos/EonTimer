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
        setWindowTitle("Preferences");
        auto *layout = new QGridLayout(this);
        layout->setVerticalSpacing(10);
        // ----- tabPane -----
        {
            auto *tabPane = new QTabWidget();
            actionSettingsPane = new settings::ActionSettingsPane(actionSettings);
            timerSettingsPane = new settings::TimerSettingsPane(timerSettings);
            tabPane->addTab(actionSettingsPane, "Action");
            tabPane->addTab(timerSettingsPane, "Timer");
            layout->addWidget(tabPane, 0, 0, 1, 2);
        }
        // ----- cancelButton -----
        {
            auto *cancelButton = new QPushButton("Cancel");
            connect(cancelButton, &QPushButton::clicked, [this]() {
               done(QDialog::Rejected);
            });
            layout->addWidget(cancelButton, 1, 0);
        }
        // ----- okButton -----
        {
            auto *okButton = new QPushButton("OK");
            connect(okButton, &QPushButton::clicked, [this]() {
                actionSettingsPane->sync();
                timerSettingsPane->sync();
                done(QDialog::Accepted);
            });
            layout->addWidget(okButton, 1, 1);
            okButton->setDefault(true);
        }
    }
}
