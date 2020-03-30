//
// Created by Dylan Meadows on 2020-03-29.
//

#ifndef EONTIMER_CUSTOMTIMERPANE_H
#define EONTIMER_CUSTOMTIMERPANE_H

#include <QWidget>
#include <models/timers/CustomTimerModel.h>

namespace gui::timer {
    class CustomTimerPane : public QWidget {
    Q_OBJECT
    private:
        model::timer::CustomTimerModel *model;
    public:
        explicit CustomTimerPane(model::timer::CustomTimerModel *model,
                                 QWidget *parent = nullptr);
    private:
        void initComponents();
    };
}

#endif //EONTIMER_CUSTOMTIMERPANE_H
