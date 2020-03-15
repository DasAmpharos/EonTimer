//
// Created by Dylan Meadows on 2020-03-09.
//

#ifndef EONTIMER_APPLICATIONWINDOW_H
#define EONTIMER_APPLICATIONWINDOW_H

#include <QMainWindow>
#include <QSettings>

namespace gui {
    class ApplicationPane;

    class ApplicationWindow : public QMainWindow {
    Q_OBJECT
    private:
        QSettings *settings;
        ApplicationPane *applicationPane;
    public:
        explicit ApplicationWindow(QWidget *parent = nullptr);

    private:
        void initComponents();

        // @formatter:off
    private slots:
        void onAboutTriggered();
        void onPreferencesTriggered();
        // @formatter:on
    };
}


#endif //EONTIMER_APPLICATIONWINDOW_H
