//
// Created by Dylan Meadows on 2020-03-09.
//

#include "Gen4TimerPane.h"

#include <gui/util/FieldSet.h>

#include <QFormLayout>
#include <QGroupBox>
#include <QLabel>
#include <QVBoxLayout>

namespace gui::timer {
    Gen4TimerPane::Gen4TimerPane(
        model::timer::Gen4TimerModel *model,
        const service::timer::DelayTimer *delayTimer,
        const service::CalibrationService *calibrationService, QWidget *parent)
        : QWidget(parent),
          model(model),
          delayTimer(delayTimer),
          calibrationService(calibrationService) {
        initComponents();
    }

    void Gen4TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        rootLayout->setContentsMargins(10, 0, 10, 10);
        rootLayout->setSpacing(10);

        void (QSpinBox::*valueChanged)(int) = &QSpinBox::valueChanged;
        // --- timer fields ---
        {
            // group
            auto *group = new QGroupBox();
            group->setProperty("class", "themeable-panel themeable-border");
            group->setSizePolicy(QSizePolicy::Expanding,
                                 QSizePolicy::Expanding);
            auto *groupLayout = new QVBoxLayout(group);
            groupLayout->setContentsMargins(0, 0, 0, 0);
            groupLayout->setSpacing(0);
            rootLayout->addWidget(group);

            // form
            auto *form = new QWidget();
            form->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            groupLayout->addWidget(form, 0, Qt::AlignTop);
            // form layout
            auto *formLayout = new QGridLayout(form);
            formLayout->setSpacing(10);
            // ----- calibratedDelay -----
            {
                auto calibratedDelay = util::FieldSet<QSpinBox>(
                    0, new QLabel("Calibrated Delay"), new QSpinBox);
                calibratedDelay.field->setRange(INT_MIN, INT_MAX);
                calibratedDelay.field->setValue(model->getCalibratedDelay());
                calibratedDelay.field->setSizePolicy(QSizePolicy::Expanding,
                                                     QSizePolicy::Fixed);
                connect(calibratedDelay.field, valueChanged,
                        [this](const int value) {
                            model->setCalibratedDelay(value);
                            emit timerChanged(createStages());
                        });
                util::addFieldSet(formLayout, calibratedDelay);
            }
            // ----- calibratedSecond -----
            {
                auto calibratedSecond = util::FieldSet<QSpinBox>(
                    1, new QLabel("Calibrated Second"), new QSpinBox);
                calibratedSecond.field->setRange(INT_MIN, INT_MAX);
                calibratedSecond.field->setValue(model->getCalibratedSecond());
                calibratedSecond.field->setSizePolicy(QSizePolicy::Expanding,
                                                      QSizePolicy::Fixed);
                connect(calibratedSecond.field, valueChanged,
                        [this](const int value) {
                            model->setCalibratedSecond(value);
                            emit timerChanged(createStages());
                        });
                util::addFieldSet(formLayout, calibratedSecond);
            }
            // ----- targetDelay -----
            {
                auto targetDelay = util::FieldSet<QSpinBox>(
                    2, new QLabel("Target Delay"), new QSpinBox);
                targetDelay.field->setRange(0, INT_MAX);
                targetDelay.field->setValue(model->getTargetDelay());
                targetDelay.field->setSizePolicy(QSizePolicy::Expanding,
                                                 QSizePolicy::Fixed);
                connect(model,
                        &model::timer::Gen4TimerModel::targetDelayChanged,
                        [targetDelay](const int value) {
                            targetDelay.field->setValue(value);
                        });
                connect(targetDelay.field, valueChanged,
                        [this](const int value) {
                            model->setTargetDelay(value);
                            emit timerChanged(createStages());
                        });
                util::addFieldSet(formLayout, targetDelay);
            }
            // ----- targetSecond -----
            {
                auto targetSecond = util::FieldSet<QSpinBox>(
                    3, new QLabel("Target Second"), new QSpinBox);
                targetSecond.field->setRange(0, 59);
                targetSecond.field->setValue(model->getTargetSecond());
                targetSecond.field->setSizePolicy(QSizePolicy::Expanding,
                                                  QSizePolicy::Fixed);
                connect(targetSecond.field, valueChanged,
                        [this](const int value) {
                            model->setTargetSecond(value);
                            emit timerChanged(createStages());
                        });
                util::addFieldSet(formLayout, targetSecond);
            }
            rootLayout->addWidget(group);
        }
        // ----- delayHit -----
        {
            // TODO
            auto *layout = new QFormLayout();
            layout->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            rootLayout->addLayout(layout);

            delayHit = new QSpinBox();
            delayHit->setRange(0, INT_MAX);
            delayHit->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            layout->addRow("Delay Hit", delayHit);
        }
        createStages();
    }

    void Gen4TimerPane::calibrate() {
        model->setCalibratedDelay(
            model->getCalibratedDelay() +
            calibrationService->toDelays(delayTimer->calibrate(
                model->getTargetDelay(), delayHit->value())));
        delayHit->setValue(0);
    }

    std::shared_ptr<std::vector<int>> Gen4TimerPane::createStages() {
        return delayTimer->createStages(model->getTargetDelay(),
                                        model->getTargetSecond(),
                                        getCalibration());
    }

    int Gen4TimerPane::getCalibration() const {
        return calibrationService->createCalibration(
            model->getCalibratedDelay(), model->getCalibratedSecond());
    }
}  // namespace gui::timer
