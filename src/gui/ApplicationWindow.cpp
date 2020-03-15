//
// Created by Dylan Meadows on 2020-03-09.
//

#include "ApplicationWindow.h"
#include "ApplicationPane.h"
#include <QMenuBar>
#include <QWindow>
#include <iostream>
#include <models/Sound.h>
#include <QSoundEffect>

namespace gui {
    ApplicationWindow::ApplicationWindow(QWidget *parent)
        : QMainWindow(parent) {
        settings = new QSettings(this);
        applicationPane = new ApplicationPane(settings, this);
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
        std::cout << "preferences" << std::endl;
    }
}
