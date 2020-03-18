//
// Created by Dylan Meadows on 2020-03-09.
//

#include "ApplicationWindow.h"
#include <QMenuBar>
#include <QWindow>
#include <gui/dialogs/SettingsDialog.h>
#include <iostream>
#include <sstream>
#include <string.h>
#include <app.h>

namespace gui {
    const char *getTitle();

    ApplicationWindow::ApplicationWindow(QWidget *parent)
        : QMainWindow(parent) {
        settings = new QSettings(this);
        actionSettings = new service::settings::ActionSettings(settings);
        timerSettings = new service::settings::TimerSettings(settings);
        timerService = new service::TimerService(timerSettings, actionSettings, this);
        applicationPane = new ApplicationPane(settings, actionSettings, timerSettings, timerService, this);

        QPalette palette;
        QPixmap background(":/images/default_background_image.png");
        /*background = background.scaled(this->size(), Qt::IgnoreAspectRatio);*/
        palette.setBrush(QPalette::Window, background);
        setPalette(palette);
        initComponents();
    }

    void ApplicationWindow::initComponents() {
        setWindowTitle(getTitle());
        setCentralWidget(applicationPane);
        setWindowFlags(Qt::Window | Qt::WindowTitleHint | Qt::CustomizeWindowHint | Qt::WindowCloseButtonHint |
                       Qt::WindowMinimizeButtonHint);
        setFixedSize(525, 395);
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
                connect(timerService, &service::TimerService::activated, [about](const bool activated) {
                    about->setEnabled(!activated);
                });
                menu->addAction(about);
            }
            // ----- preferences -----
            {
                auto *preferences = new QAction();
                preferences->setMenuRole(QAction::PreferencesRole);
                connect(preferences, SIGNAL(triggered(bool)), this, SLOT(onPreferencesTriggered()));
                connect(timerService, &service::TimerService::activated, [preferences](const bool activated) {
                    preferences->setEnabled(!activated);
                });
                menu->addAction(preferences);
            }
        }
    }

    void ApplicationWindow::closeEvent(QCloseEvent *event) {
        settings->sync();
    }

    void ApplicationWindow::onAboutTriggered() {
        std::cout << "about" << std::endl;
    }

    void ApplicationWindow::onPreferencesTriggered() {
        gui::dialog::SettingsDialog settings(timerSettings, actionSettings, this);
        const int rval = settings.exec();
    }

    bool equalsIgnoreCase(const char *s1, const char *s2) {
        std::string a(s1);
        std::string b(s2);
        return std::equal(a.begin(), a.end(),
                          b.begin(), b.end(),
                          [](char a, char b) {
                              return tolower(a) == tolower(b);
                          });
    }

    const char *getTitle() {
        std::stringstream sstream;
        sstream << APP_NAME << " " << APP_VERSION;
        if (!equalsIgnoreCase(BUILD_TYPE, "release")) {
            sstream << " - git#" << GIT_COMMIT_HASH;
        }
        std::string title = sstream.str();
        char* buffer = new char[title.capacity()];
        strcpy(buffer, title.c_str());
        return buffer;
    }
}
