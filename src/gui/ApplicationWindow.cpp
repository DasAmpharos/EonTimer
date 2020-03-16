//
// Created by Dylan Meadows on 2020-03-09.
//

#include "ApplicationWindow.h"
#include "ApplicationPane.h"
#include <QMenuBar>
#include <QWindow>
#include <iostream>
#include <QSoundEffect>
#include <gui/dialogs/SettingsDialog.h>

namespace gui {
    ApplicationWindow::ApplicationWindow(QWidget *parent)
        : QMainWindow(parent) {
        settings = new QSettings(this);
        actionSettings = new service::settings::ActionSettings(settings);
        timerSettings = new service::settings::TimerSettings(settings);
        applicationPane = new ApplicationPane(actionSettings, timerSettings, this);

        QPalette palette;
        QPixmap background(":/images/default_background_image.png");
        /*background = background.scaled(this->size(), Qt::IgnoreAspectRatio);*/
        palette.setBrush(QPalette::Window, background);
        setPalette(palette);
        initComponents();
    }

    void ApplicationWindow::initComponents() {
        setWindowTitle("EonTimer");
        setCentralWidget(applicationPane);
        // ----- menu -----
        {
            auto *menu = new QMenu();
            auto *menuBar = new QMenuBar();
            menuBar->addMenu(menu);
            // ----- about -----
            {
                auto *about = new QAction();
                about->setMenuRole(QAction::AboutRole);
                connect(about, SIGNAL(triggered(bool)), this, SLOT(onAboutTriggered()));
                menu->addAction(about);
            }
            // ----- preferences -----
            {
                auto *preferences = new QAction();
                preferences->setMenuRole(QAction::PreferencesRole);
                connect(preferences, SIGNAL(triggered(bool)), this, SLOT(onPreferencesTriggered()));
                menu->addAction(preferences);
            }
        }
    }

    void ApplicationWindow::onAboutTriggered() {
        std::cout << "about" << std::endl;
    }

    void ApplicationWindow::onPreferencesTriggered() {
        gui::dialog::SettingsDialog settings(timerSettings, actionSettings, this);
        const int rval = settings.exec();
    }
}
