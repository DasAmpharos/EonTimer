//
// Created by Dylan Meadows on 2020-03-16.
//

#ifndef EONTIMER_ACTIONSETTINGSPANE_H
#define EONTIMER_ACTIONSETTINGSPANE_H

#include <models/settings/ActionSettingsModel.h>

#include <QColor>
#include <QComboBox>
#include <QPushButton>
#include <QSpinBox>
#include <QWidget>

namespace gui::settings {
    class ActionSettingsPane : public QWidget {
        Q_OBJECT
    private:
        model::settings::ActionSettingsModel *settings;
        int mode;
        int sound;
        QColor color;
        int interval;
        int count;

    public:
        explicit ActionSettingsPane(model::settings::ActionSettingsModel *settings, QWidget *parent = nullptr);

        void updateSettings();

    private:
        void initComponents();

        void setIconColor(QPushButton *btn, const QColor &color);

    signals:
        void modeChanged(int mode);

    private slots:
        void setMode(int mode);
        void setSound(int sound);
        void setInterval(int interval);
        void setCount(int count);
    };
}  // namespace gui::settings

#endif  // EONTIMER_ACTIONSETTINGSPANE_H
