//
// Created by Dylan Meadows on 2020-03-09.
//

#include "Gen4TimerPane.h"
#include <QVBoxLayout>
#include <QGroupBox>
#include <QFormLayout>
#include <QLabel>
#include <QSpinBox>

namespace ui {
    // @formatter:off
    const long MINIMUM_LENGTH = 14000L;
    long createStage1(service::CalibrationService *calibrationService, long targetSecond, long targetDelay, long calibration);
    long createStage2(service::CalibrationService *calibrationService, long targetDelay, long calibration);
    long toMinimumLength(long value);
    // @formatter:on

    Gen4TimerPane::Gen4TimerPane(service::TimerService *timerService,
                                 service::SettingsService *settingsService,
                                 service::CalibrationService *calibrationService)
        : QWidget(nullptr),
          timerService(timerService),
          settingsService(settingsService),
          calibrationService(calibrationService) {
        initComponents();
    }

    void Gen4TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        void (QSpinBox::* valueChanged)(int) = &QSpinBox::valueChanged;
        // --- fields ---
        {
            auto *group = new QGroupBox();
            auto *layout = new QFormLayout(group);
            layout->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            // ----- calibratedDelay -----
            {
                auto *label = new QLabel();
                label->setText("Calibrated Delay");
                auto *field = new QSpinBox();
                layout->addRow(label, field);
                connect(field, valueChanged, [=](int val) {
                    calibratedDelay = val;
                    updateTimer();
                });
                field->setRange(-10000, 10000);
                field->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
            }
            // ----- calibratedSecond -----
            {
                auto *label = new QLabel();
                label->setText("Calibrated Second");
                auto *field = new QSpinBox();
                connect(field, valueChanged, [=](int val) {
                    calibratedSecond = val;
                    updateTimer();
                });
                layout->addRow(label, field);
                field->setRange(-10000, 10000);
                field->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
            }
            // ----- targetDelay -----
            {
                auto *label = new QLabel();
                label->setText("Target Delay");
                auto *field = new QSpinBox();
                connect(field, valueChanged, [this](int val) {
                    targetDelay = val;
                    updateTimer();
                });
                layout->addRow(label, field);
                field->setRange(0, 10000);
                field->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
            }
            // ----- targetSecond -----
            {
                auto *label = new QLabel();
                label->setText("Target Second");
                auto *field = new QSpinBox();
                connect(field, valueChanged, [this](int val) {
                    targetSecond = val;
                    updateTimer();
                });
                layout->addRow(label, field);
                field->setRange(0, 10000);
                field->setSizePolicy(
                    QSizePolicy::Expanding,
                    QSizePolicy::Fixed
                );
            }
            rootLayout->addWidget(group);
        }
        // ----- delayHit -----
        {
            auto *layout = new QFormLayout();
            layout->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
            rootLayout->addLayout(layout);

            auto *label = new QLabel();
            label->setText("Delay Hit");
            auto *field = new QSpinBox();
            layout->addRow(label, field);
            field->setRange(0, 10000);
            field->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
        }
    }

    void Gen4TimerPane::updateTimer() {
        const long calibration = calibrationService->createCalibration(calibratedDelay, calibratedSecond);
        const long stage1 = createStage1(calibrationService, targetSecond, targetDelay, calibration);
        const long stage2 = createStage2(calibrationService, targetDelay, calibration);
        timerService->setCurrentStage(stage1);
        timerService->setNextStage(stage2);
    }

    long createStage1(
        service::CalibrationService *calibrationService,
        const long targetSecond,
        const long targetDelay,
        const long calibration
    ) {
        return toMinimumLength(
            toMinimumLength(targetSecond * 1000 + calibration + 200) -
            calibrationService->toMilliseconds(targetDelay)
        );
    }

    long createStage2(
        service::CalibrationService *calibrationService,
        const long targetDelay,
        const long calibration
    ) {
        return calibrationService->toMilliseconds(targetDelay) - calibration;
    }

    long toMinimumLength(const long value) {
        long normalized = value;
        while (normalized < MINIMUM_LENGTH) {
            normalized += 60000;
        }
        return normalized;
    }
}