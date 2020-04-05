//
// Created by Dylan Meadows on 2020-03-09.
//

#include "ApplicationWindow.h"

#include <app.h>
#include <gui/dialogs/AboutDialog.h>
#include <gui/dialogs/SettingsDialog.h>
#include <util/Functions.h>

#include <QFile>
#include <QMenuBar>
#include <sstream>

namespace gui {
    const char *getTitle() {
        std::stringstream stream;
        stream << APP_NAME << " " << APP_VERSION;
        std::string title = stream.str();
        char *buffer = new char[title.capacity()];
        strcpy(buffer, title.c_str());
        return buffer;
    }

    void addStylesheet(QString &css, const char *resource) {
        QFile file(resource);
        file.open(QFile::ReadOnly);
        css.append(file.readAll());
    }

    ApplicationWindow::ApplicationWindow(QWidget *parent)
        : QMainWindow(parent) {
        settings = new QSettings(this);
        actionSettings = new model::settings::ActionSettingsModel(settings);
        timerSettings = new model::settings::TimerSettingsModel(settings);
        timerService =
            new service::TimerService(timerSettings, actionSettings, this);
        applicationPane = new ApplicationPane(
            settings, actionSettings, timerSettings, timerService, this);
        initComponents();
    }

    void ApplicationWindow::initComponents() {
        setWindowTitle(getTitle());
        setWindowFlags(Qt::Window | Qt::WindowTitleHint |
                       Qt::CustomizeWindowHint | Qt::WindowCloseButtonHint |
                       Qt::WindowMinimizeButtonHint);
        setCentralWidget(applicationPane);
        setFixedSize(525, 395);

        QString stylesheet;
        addStylesheet(stylesheet, ":/css/main.css");
#if defined(__APPLE__)
        addStylesheet(stylesheet, ":/css/macos.css");
#elif defined(__linux__)
        addStylesheet(stylesheet, ":/css/linux.css");
#elif defined(__MINGW32__)
        addStylesheet(stylesheet, ":/css/windows.css");
#endif
        setStyleSheet(stylesheet);

        // background image
        QPalette palette;
        QPixmap background(":/images/default_background_image.png");
        /*background = background.scaled(this->size(), Qt::IgnoreAspectRatio);*/
        palette.setBrush(QPalette::Window, background);
        setPalette(palette);

        // ----- menu -----
        {
            auto *menu = new QMenu();
            auto *menuBar = new QMenuBar();
            menuBar->addMenu(menu);
            // ----- preferences -----
            {
                auto *preferences = new QAction();
                preferences->setMenuRole(QAction::PreferencesRole);
                connect(preferences, SIGNAL(triggered(bool)), this,
                        SLOT(onPreferencesTriggered()));
                connect(timerService, &service::TimerService::activated,
                        [preferences](const bool activated) {
                            preferences->setEnabled(!activated);
                        });
                menu->addAction(preferences);
            }
        }
    }

    void ApplicationWindow::closeEvent(QCloseEvent *) { settings->sync(); }

    void ApplicationWindow::onPreferencesTriggered() {
        gui::dialog::SettingsDialog(timerSettings, actionSettings, this).exec();
    }
}  // namespace gui
