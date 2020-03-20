//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerPane.h"
#include <models/Gen5TimerMode.h>
#include <QGridLayout>
#include <QFormLayout>
#include <QScrollArea>

namespace gui::timer {
    Gen5TimerPane::Gen5TimerPane(service::settings::Gen5TimerSettings *settings,
                                 const service::timer::DelayTimer *delayTimer,
                                 const service::timer::SecondTimer *secondTimer,
                                 const service::timer::EntralinkTimer *entralinkTimer,
                                 const service::CalibrationService *calibrationService,
                                 QWidget *parent)
        : QWidget(parent),
          settings(settings),
          delayTimer(delayTimer),
          secondTimer(secondTimer),
          entralinkTimer(entralinkTimer),
          calibrationService(calibrationService) {
        initComponents();
    }

    void Gen5TimerPane::initComponents() {
        auto *layout = new QVBoxLayout(this);
        layout->setSpacing(10);
        // ----- mode -----
        {
            auto *form = new QFormLayout();
            form->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            modeField = new QComboBox();
            for (auto mode : model::gen5TimerModes()) {
                modeField->addItem(model::getName(mode), mode);
            }
            modeField->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
            form->addRow("Mode", modeField);
            layout->addLayout(form);
        }
        // ----- fields -----
        {
            auto *scrollArea = new QScrollArea();
            scrollArea->setFrameShape(QFrame::NoFrame);
            scrollArea->setProperty("class", "themeable");
            scrollArea->setVerticalScrollBarPolicy(Qt::ScrollBarPolicy::ScrollBarAsNeeded);
            scrollArea->setHorizontalScrollBarPolicy(Qt::ScrollBarPolicy::ScrollBarAlwaysOff);
            scrollArea->setWidgetResizable(true);
            layout->addWidget(scrollArea);

            auto *scrollPane = new QWidget(scrollArea);
            scrollPane->setProperty("class", "bg-transparent-white");
            scrollArea->setWidget(scrollPane);

            auto *form = new QFormLayout(scrollPane);
            form->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            // ----- calibration -----
            {
                calibrationField = new QSpinBox();
                calibrationField->setRange(INT_MIN, INT_MAX);
                calibrationField->setValue(settings->getCalibration());
                calibrationField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Calibration", calibrationField);
            }
            // ----- targetDelay -----
            {
                targetDelayField = new QSpinBox();
                targetDelayField->setRange(0, INT_MAX);
                targetDelayField->setValue(settings->getTargetDelay());
                targetDelayField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Target Delay", targetDelayField);
            }
            // ----- targetSecond -----
            {
                targetSecondField = new QSpinBox();
                targetSecondField->setRange(0, 59);
                targetSecondField->setValue(settings->getTargetSecond());
                targetSecondField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Target Second", targetSecondField);
            }
            // ----- entralinkCalibration -----
            {
                entralinkCalibrationField = new QSpinBox();
                entralinkCalibrationField->setRange(INT_MIN, INT_MAX);
                entralinkCalibrationField->setValue(settings->getEntralinkCalibration());
                entralinkCalibrationField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Entralink Calibration", entralinkCalibrationField);
            }
            // ----- frameCalibration -----
            {
                frameCalibrationField = new QSpinBox();
                frameCalibrationField->setRange(INT_MIN, INT_MAX);
                frameCalibrationField->setValue(settings->getFrameCalibration());
                frameCalibrationField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Frame Calibration", frameCalibrationField);
            }
            // ----- targetAdvances -----
            {
                targetAdvancesField = new QSpinBox();
                targetAdvancesField->setRange(0, INT_MAX);
                targetAdvancesField->setValue(settings->getTargetAdvances());
                targetAdvancesField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Target Advances", targetAdvancesField);
            }
        }
        // ----- actual fields -----
        {
            auto *form = new QFormLayout();
            form->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            layout->addLayout(form);
            // ----- delayHit -----
            {
                delayHitField = new QSpinBox();
                delayHitField->setRange(0, INT_MAX);
                delayHitField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Delay Hit", delayHitField);
            }
            // ----- secondHit -----
            {
                secondHitField = new QSpinBox();
                secondHitField->setRange(0, 59);
                secondHitField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Second Hit", secondHitField);
            }
            // ----- actualAdvances -----
            {
                actualAdvancesField = new QSpinBox();
                actualAdvancesField->setRange(0, INT_MAX);
                actualAdvancesField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Actual Advances", actualAdvancesField);
            }
        }
    }

    void Gen5TimerPane::calibrateTimer() {

    }

    void Gen5TimerPane::updateTimer() {

    }
}
