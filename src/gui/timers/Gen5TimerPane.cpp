//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerPane.h"
#include <models/Gen5TimerMode.h>
#include <QGridLayout>
#include <QFormLayout>
#include <QGroupBox>
#include <QScrollArea>

namespace gui::timer {
    Gen5TimerPane::Gen5TimerPane(service::settings::Gen5TimerSettings *settings,
                                 const service::CalibrationService *calibrationService,
                                 QWidget *parent)
        : QWidget(parent),
          settings(settings),
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

            /*auto *group = new QGroupBox();
            group->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);*/

            auto *scrollPane = new QWidget(scrollArea);
            scrollPane->setProperty("class", "bg-transparent-white");
            scrollArea->setWidget(scrollPane);

            auto *form = new QFormLayout(scrollPane);
            form->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            // ----- calibration -----
            {
                calibrationField = new QSpinBox();
                calibrationField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Calibration", calibrationField);
            }
            // ----- targetDelay -----
            {
                targetDelayField = new QSpinBox();
                targetDelayField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Target Delay", targetDelayField);
            }
            // ----- targetSecond -----
            {
                targetSecondField = new QSpinBox();
                targetSecondField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Target Second", targetSecondField);
            }
            // ----- entralinkCalibration -----
            {
                entralinkCalibrationField = new QSpinBox();
                entralinkCalibrationField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Entralink Calibration", entralinkCalibrationField);
            }
            // ----- frameCalibration -----
            {
                frameCalibrationField = new QSpinBox();
                frameCalibrationField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Frame Calibration", frameCalibrationField);
            }
            // ----- targetAdvances -----
            {
                targetAdvancesField = new QSpinBox();
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
                delayHitField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Delay Hit", delayHitField);
            }
            // ----- secondHit -----
            {
                secondHitField = new QSpinBox();
                secondHitField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Second Hit", secondHitField);
            }
            // ----- actualAdvances -----
            {
                actualAdvancesField = new QSpinBox();
                actualAdvancesField->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
                form->addRow("Actual Advances", actualAdvancesField);
            }
        }
    }
}
