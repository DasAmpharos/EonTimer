//
// Created by Dylan Meadows on 2020-03-27.
//

#include "CustomTimerModel.h"

namespace model::timer {
    namespace CustomTimerFields {
        const char *STAGES = "custom/stages";
    }

    CustomTimerModel::CustomTimerModel(QSettings *settings, QObject *parent)
        : QObject(parent) {
        stages = settings->value(CustomTimerFields::STAGES)
            .value<QList<int>>();
    }

    void CustomTimerModel::sync(QSettings *settings) const {
        settings->setValue(CustomTimerFields::STAGES, QVariant::fromValue(stages));
    }

    QList<int> &CustomTimerModel::getStages() {
        return stages;
    }

    void CustomTimerModel::setStages(QList<int> &stages) {
        this->stages = QList<int>(stages);
    }
}
