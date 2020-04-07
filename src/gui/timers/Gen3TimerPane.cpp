//
// Created by Dylan Meadows on 2020-03-26.
//

#include "Gen3TimerPane.h"

#include <gui/util/FieldSet.h>

#include <QGroupBox>
#include <QLabel>
#include <QVBoxLayout>

namespace gui::timer {
    Gen3TimerPane::Gen3TimerPane(
        model::timer::Gen3TimerModel *model,
        const service::timer::FrameTimer *frameTimer,
        const service::CalibrationService *calibrationService, QWidget *parent)
        : QWidget(parent),
          model(model),
          frameTimer(frameTimer),
          calibrationService(calibrationService) {
        initComponents();
    }

    void Gen3TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        rootLayout->setContentsMargins(10, 0, 10, 10);
        rootLayout->setSpacing(10);

        void (QSpinBox::*valueChanged)(int) = &QSpinBox::valueChanged;
        // ----- timer fields -----
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
            // ----- calibration -----
            {
                auto calibration = util::FieldSet<QSpinBox>(
                    0, new QLabel("Calibration"), new QSpinBox);
                calibration.field->setRange(INT_MIN, INT_MAX);
                calibration.field->setValue(model->getCalibration());
                calibration.field->setSizePolicy(QSizePolicy::Expanding,
                                                 QSizePolicy::Fixed);
                connect(model, SIGNAL(calibrationChanged(int)),
                        calibration.field, SLOT(setValue(int)));
                connect(calibration.field, valueChanged,
                        [this](const int calibration) {
                            model->setCalibration(calibration);
                            emit timerChanged(createStages());
                        });
                util::addFieldSet(formLayout, calibration);
            }
            // ----- preTimer -----
            {
                auto preTimer = util::FieldSet<QSpinBox>(
                    1, new QLabel("Pre-Timer"), new QSpinBox);
                preTimer.field->setRange(0, INT_MAX);
                preTimer.field->setValue(model->getPreTimer());
                connect(model, SIGNAL(preTimerChanged(int)), preTimer.field,
                        SLOT(setValue(int)));
                connect(preTimer.field, valueChanged,
                        [this](const int preTimer) {
                            model->setPreTimer(preTimer);
                            emit timerChanged(createStages());
                        });
                util::addFieldSet(formLayout, preTimer);
            }
            // ----- targetFrame -----
            {
                auto targetFrame = util::FieldSet<QSpinBox>(
                    2, new QLabel("Target Frame"), new QSpinBox);
                targetFrame.field->setRange(0, INT_MAX);
                targetFrame.field->setValue(model->getTargetFrame());
                connect(model, SIGNAL(targetFrameChanged(int)),
                        targetFrame.field, SLOT(setValue(int)));
                connect(targetFrame.field, valueChanged,
                        [this](const int targetFrame) {
                            model->setTargetFrame(targetFrame);
                            emit timerChanged(createStages());
                        });
                util::addFieldSet(formLayout, targetFrame);
            }
        }
        // ----- frameHit -----
        {
            auto *form = new QGridLayout();
            rootLayout->addLayout(form);
            form->setSpacing(10);

            auto frameHit = util::FieldSet<QSpinBox>(0, new QLabel("Frame Hit"),
                                                     new QSpinBox);
            frameHit.field->setRange(0, INT_MAX);
            connect(model, SIGNAL(frameHitChanged(int)), frameHit.field,
                    SLOT(setValue(int)));
            connect(frameHit.field, valueChanged,
                    [this](const int value) { model->setFrameHit(value); });
            util::addFieldSet(form, frameHit);
        }
    }

    std::shared_ptr<std::vector<int>> Gen3TimerPane::createStages() {
        return frameTimer->createStages(model->getPreTimer(),
                                        model->getTargetFrame(),
                                        model->getCalibration());
    }

    void Gen3TimerPane::calibrate() {
        model->setCalibration(model->getCalibration() + getCalibration());
        model->setFrameHit(0);
    }

    int Gen3TimerPane::getCalibration() const {
        return frameTimer->calibrate(model->getTargetFrame(),
                                     model->getFrameHit());
    }
}  // namespace gui::timer
