//
// Created by Dylan Meadows on 2020-03-09.
//

#ifndef EONTIMER_APPLICATIONWINDOW_H
#define EONTIMER_APPLICATIONWINDOW_H

#include <QMainWindow>

namespace gui {
    class ApplicationPane;

    class ApplicationWindow : public QMainWindow {
    Q_OBJECT
    public:
        explicit ApplicationWindow(QWidget *parent = nullptr);

    private slots:

        void onAboutTriggered();

        void onPreferencesTriggered();

    private:
        void initComponents();

    private:
        ApplicationPane *applicationPane;
    };
}


#endif //EONTIMER_APPLICATIONWINDOW_H
