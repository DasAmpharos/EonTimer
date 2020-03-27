//
// Created by Dylan Meadows on 2020-03-09.
//

#include "Gen4TimerPane.h"
#include <QVBoxLayout>
#include <QGroupBox>
#include <QFormLayout>
#include <gui/util/FieldSet.h>
#include <QLabel>

namespace gui::timer {
    Gen4TimerPane::Gen4TimerPane(service::settings::Gen4TimerSettings *settings,
                                 const service::timer::DelayTimer *delayTimer,
                                 const service::CalibrationService *calibrationService,
                                 QWidget *parent)
        : QWidget(parent),
          settings(settings),
          delayTimer(delayTimer),
          calibrationService(calibrationService) {
        initComponents();
    }

    void Gen4TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        rootLayout->setContentsMargins(10, 0, 10, 10);
        rootLayout->setSpacing(10);

        void (QSpinBox::* valueChanged)(int) = &QSpinBox::valueChanged;
        // --- timer fields ---
        {
            auto *group = new QGroupBox();
            group->setProperty("class", "themeable");
            group->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
            auto *timerForm = new QGridLayout(group);
            // ----- calibratedDelay -----
            {
                auto calibratedDelay = util::FieldSet<QSpinBox>(0, new QLabel("Calibrated Delay"), new QSpinBox);
                calibratedDelay.field->setRange(INT_MIN, INT_MAX);
                calibratedDelay.field->setValue(settings->getCalibratedDelay());
                calibratedDelay.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(calibratedDelay.field, valueChanged,
                        [this](const int value) {
                            settings->setCalibratedDelay(value);
                            createStages();
                        });
                util::addFieldSet(timerForm, calibratedDelay);
            }
            // ----- calibratedSecond -----
            {
                auto calibratedSecond = util::FieldSet<QSpinBox>(1, new QLabel("Calibrated Second"), new QSpinBox);
                calibratedSecond.field->setRange(INT_MIN, INT_MAX);
                calibratedSecond.field->setValue(settings->getCalibratedSecond());
                calibratedSecond.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(calibratedSecond.field, valueChanged,
                        [this](const int value) {
                            settings->setCalibratedSecond(value);
                            createStages();
                        });
                util::addFieldSet(timerForm, calibratedSecond);
            }
            // ----- targetDelay -----
            {
                auto targetDelay = util::FieldSet<QSpinBox>(2, new QLabel("Target Delay"), new QSpinBox);
                targetDelay.field->setRange(0, INT_MAX);
                targetDelay.field->setValue(settings->getTargetDelay());
                targetDelay.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(settings, &service::settings::Gen4TimerSettings::targetDelayChanged,
                        [targetDelay](const int value) {
                            targetDelay.field->setValue(value);
                        });
                connect(targetDelay.field, valueChanged,
                        [this](const int value) {
                            settings->setTargetDelay(value);
                            createStages();
                        });
                util::addFieldSet(timerForm, targetDelay);
            }
            // ----- targetSecond -----
            {
                auto targetSecond = util::FieldSet<QSpinBox>(3, new QLabel("Target Second"), new QSpinBox);
                targetSecond.field->setRange(0, 59);
                targetSecond.field->setValue(settings->getTargetSecond());
                targetSecond.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(targetSecond.field, valueChanged,
                        [this](const int value) {
                            settings->setTargetSecond(value);
                            createStages();
                        });
                util::addFieldSet(timerForm, targetSecond);
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
        settings->setCalibratedDelay(
            settings->getCalibratedDelay() +
            calibrationService->toDelays(
                delayTimer->calibrate(
                    settings->getTargetDelay(),
                    delayHit->value()
                )
            )
        );
        delayHit->setValue(0);
    }

    std::shared_ptr<std::vector<int>> Gen4TimerPane::createStages() {
        return delayTimer->createStages(
            settings->getTargetDelay(),
            settings->getTargetSecond(),
            getCalibration()
        );
    }

    int Gen4TimerPane::getCalibration() const {
        return calibrationService->createCalibration(
            settings->getCalibratedDelay(),
            settings->getCalibratedSecond()
        );
    }
}