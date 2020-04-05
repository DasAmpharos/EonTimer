//
// Created by Dylan Meadows on 2020-03-27.
//

#ifndef EONTIMER_CUSTOMTIMERMODEL_H
#define EONTIMER_CUSTOMTIMERMODEL_H

#include <QObject>
#include <QSettings>
#include <memory>
#include <vector>

namespace model::timer {
    class CustomTimerModel : public QObject {
        Q_OBJECT
    private:
        QList<int> stages;

    public:
        explicit CustomTimerModel(QSettings *settings,
                                  QObject *parent = nullptr);

        void sync(QSettings *settings) const;

        QList<int> &getStages();

        void setStages(QList<int> &stages);
    };
}  // namespace model::timer

#endif  // EONTIMER_CUSTOMTIMERMODEL_H
