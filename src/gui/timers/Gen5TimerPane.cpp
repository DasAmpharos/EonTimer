//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerPane.h"

#include <QLabel>
#include <QScrollArea>

namespace gui::timer {
    Gen5TimerPane::Gen5TimerPane(model::timer::Gen5TimerModel *model,
                                 const service::timer::DelayTimer *delayTimer,
                                 const service::timer::SecondTimer *secondTimer,
                                 const service::timer::EntralinkTimer *entralinkTimer,
                                 const service::timer::EnhancedEntralinkTimer *enhancedEntralinkTimer,
                                 const service::CalibrationService *calibrationService,
                                 QWidget *parent)
        : QWidget(parent),
          model(model),
          delayTimer(delayTimer),
          secondTimer(secondTimer),
          entralinkTimer(entralinkTimer),
          enhancedEntralinkTimer(enhancedEntralinkTimer),
          calibrationService(calibrationService) {
        initComponents();
    }

    void Gen5TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        rootLayout->setContentsMargins(10, 0, 10, 10);
        rootLayout->setSpacing(10);

        void (QSpinBox::*valueChanged)(int) = &QSpinBox::valueChanged;
        void (*setVisible)(QGridLayout *, util::FieldSet<QSpinBox> &, const bool) = util::setVisible;
        // ----- mode -----
        {
            auto *form = new QGridLayout();
            rootLayout->addLayout(form);

            util::FieldSet<QComboBox> mode(0, new QLabel("Mode"), new QComboBox);
            for (const auto currentMode : model::gen5TimerModes()) {
                mode.field->addItem(model::getName(currentMode), currentMode);
            }
            mode.field->setCurrentText(model::getName(model->getMode()));
            connect(mode.field, QOverload<int>::of(&QComboBox::currentIndexChanged), [this](const int currentIndex) {
                model->setMode(model::gen5TimerModes()[currentIndex]);
                emit timerChanged(createStages());
            });
            util::addFieldSet(form, mode);
        }
        // ----- timer fields -----
        {
            auto *scrollPane = new QWidget();
            scrollPane->setProperty("class", "themeable-panel");
            scrollPane->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
            auto *scrollPaneLayout = new QVBoxLayout(scrollPane);
            scrollPaneLayout->setContentsMargins(0, 0, 0, 0);
            scrollPaneLayout->setSpacing(10);
            auto *scrollArea = new QScrollArea();
            scrollArea->setFrameShape(QFrame::NoFrame);
            scrollArea->setProperty("class", "themeable-panel themeable-border");
            scrollArea->setVerticalScrollBarPolicy(Qt::ScrollBarPolicy::ScrollBarAsNeeded);
            scrollArea->setHorizontalScrollBarPolicy(Qt::ScrollBarPolicy::ScrollBarAsNeeded);
            scrollArea->setWidgetResizable(true);
            scrollArea->setWidget(scrollPane);
            rootLayout->addWidget(scrollArea);

            auto *formWidget = new QWidget();
            formWidget->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            scrollPaneLayout->addWidget(formWidget, 0, Qt::AlignTop);
            auto *form = new QGridLayout(formWidget);
            form->setSpacing(10);
            // ----- calibration -----
            {
                auto *field = new QSpinBox;
                auto *fieldSet = new util::FieldSet<QSpinBox>(0, new QLabel("Calibration"), field);
                field->setRange(INT_MIN, INT_MAX);
                field->setValue(model->getCalibration());
                connect(model, SIGNAL(calibrationChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const int value) {
                    model->setCalibration(value);
                    emit timerChanged(createStages());
                });
                util::addFieldSet(form, *fieldSet);
            }
            // ----- targetDelay -----
            {
                auto *field = new QSpinBox;
                auto *fieldSet = new util::FieldSet<QSpinBox>(1, new QLabel("Target Delay"), field);
                field->setRange(0, INT_MAX);
                field->setValue(model->getTargetDelay());
                connect(model, SIGNAL(targetDelayChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const int value) {
                    model->setTargetDelay(value);
                    emit timerChanged(createStages());
                });
                connect(model,
                        &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const model::Gen5TimerMode value) {
                            setVisible(form, *fieldSet, value != model::Gen5TimerMode::STANDARD);
                        });
                util::addFieldSet(form, *fieldSet);
            }
            // ----- targetSecond -----
            {
                auto *field = new QSpinBox;
                auto *fieldSet = new util::FieldSet<QSpinBox>(2, new QLabel("Target Second"), field);
                field->setRange(0, 59);
                field->setValue(model->getTargetSecond());
                connect(model, SIGNAL(targetSecondChanged(int)), field, SLOT(setValue(int)));
                connect(fieldSet->field, valueChanged, [this](const int value) {
                    model->setTargetSecond(value);
                    emit timerChanged(createStages());
                });
                util::addFieldSet(form, *fieldSet);
            }
            // ----- entralinkCalibration -----
            {
                auto *field = new QSpinBox;
                auto *fieldSet = new util::FieldSet<QSpinBox>(3, new QLabel("Entralink Calibration"), field);
                field->setRange(INT_MIN, INT_MAX);
                field->setValue(model->getEntralinkCalibration());
                connect(model, SIGNAL(entralinkCalibrationChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const int value) {
                    model->setEntralinkCalibration(value);
                    emit timerChanged(createStages());
                });
                connect(model,
                        &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const model::Gen5TimerMode value) {
                            setVisible(form,
                                       *fieldSet,
                                       value == model::Gen5TimerMode::ENTRALINK ||
                                           value == model::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                util::addFieldSet(form, *fieldSet);
            }
            // ----- frameCalibration -----
            {
                auto *field = new QSpinBox;
                auto *fieldSet = new util::FieldSet<QSpinBox>(4, new QLabel("Frame Calibration"), field);
                field->setRange(INT_MIN, INT_MAX);
                field->setValue(model->getFrameCalibration());
                connect(model, SIGNAL(frameCalibrationChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const int frameCalibration) {
                    model->setFrameCalibration(frameCalibration);
                    emit timerChanged(createStages());
                });
                connect(model,
                        &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const model::Gen5TimerMode value) {
                            setVisible(form, *fieldSet, value == model::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                util::addFieldSet(form, *fieldSet);
            }
            // ----- targetAdvances -----
            {
                auto *field = new QSpinBox;
                auto *fieldSet = new util::FieldSet<QSpinBox>(5, new QLabel("Target Advances"), field);
                field->setRange(0, INT_MAX);
                field->setValue(model->getTargetAdvances());
                connect(model, SIGNAL(targetAdvancesChanged(int)), field, SLOT(setValue(int)));
                connect(fieldSet->field, valueChanged, [this](const int value) {
                    model->setTargetAdvances(value);
                    emit timerChanged(createStages());
                });
                connect(model,
                        &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const model::Gen5TimerMode value) {
                            setVisible(form, *fieldSet, value == model::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                util::addFieldSet(form, *fieldSet);
            }
        }
        // ----- calibration fields -----
        {
            auto *form = new QGridLayout();
            rootLayout->addLayout(form);
            form->setSpacing(10);
            // ----- delayHit -----
            {
                auto *field = new QSpinBox;
                auto *fieldSet = new util::FieldSet<QSpinBox>(0, new QLabel("Delay Hit"), field);
                field->setRange(0, INT_MAX);
                field->setSpecialValueText("");
                connect(model, SIGNAL(delayHitChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const int value) { model->setDelayHit(value); });
                connect(model,
                        &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const model::Gen5TimerMode value) {
                            setVisible(form, *fieldSet, value != model::Gen5TimerMode::STANDARD);
                        });
                util::addFieldSet(form, *fieldSet);
            }
            // ----- secondHit -----
            {
                auto *field = new QSpinBox;
                auto *fieldSet = new util::FieldSet<QSpinBox>(1, new QLabel("Second Hit"), field);
                field->setRange(0, 59);
                connect(model, SIGNAL(secondHitChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const int value) { model->setSecondHit(value); });
                connect(model,
                        &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const model::Gen5TimerMode value) {
                            setVisible(form, *fieldSet, value != model::Gen5TimerMode::C_GEAR);
                        });
                util::addFieldSet(form, *fieldSet);
            }
            // ----- advancesHit -----
            {
                auto *field = new QSpinBox;
                auto *fieldSet = new util::FieldSet<QSpinBox>(2, new QLabel("Advances Hit"), field);
                field->setRange(0, INT_MAX);
                connect(model, SIGNAL(advancesHitChanged(int)), field, SLOT(setValue(int)));
                connect(field, valueChanged, [this](const int value) { model->setAdvancesHit(value); });
                connect(model,
                        &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form, fieldSet](const model::Gen5TimerMode value) {
                            setVisible(form, *fieldSet, value == model::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                util::addFieldSet(form, *fieldSet);
            }
        }
        // force all fieldsets to update visibility
        emit model->modeChanged(model->getMode());
    }

    std::shared_ptr<std::vector<int>> Gen5TimerPane::createStages() {
        std::shared_ptr<std::vector<int>> stages;
        switch (model->getMode()) {
            case model::Gen5TimerMode::STANDARD:
                stages =
                    secondTimer->createStages(model->getTargetSecond(),
                                              calibrationService->calibrateToMilliseconds(model->getCalibration()));
                break;
            case model::Gen5TimerMode::C_GEAR:
                stages = delayTimer->createStages(model->getTargetDelay(),
                                                  model->getTargetSecond(),
                                                  calibrationService->calibrateToMilliseconds(model->getCalibration()));
                break;
            case model::Gen5TimerMode::ENTRALINK:
                stages = entralinkTimer->createStages(
                    model->getTargetDelay(),
                    model->getTargetSecond(),
                    calibrationService->calibrateToMilliseconds(model->getCalibration()),
                    calibrationService->calibrateToMilliseconds(model->getEntralinkCalibration()));
                break;
            case model::Gen5TimerMode::ENTRALINK_PLUS:
                stages = enhancedEntralinkTimer->createStages(
                    model->getTargetDelay(),
                    model->getTargetSecond(),
                    model->getTargetAdvances(),
                    calibrationService->calibrateToMilliseconds(model->getCalibration()),
                    calibrationService->calibrateToMilliseconds(model->getEntralinkCalibration()),
                    model->getFrameCalibration());
                break;
        }
        return stages;
    }

    void Gen5TimerPane::calibrate() {
        switch (model->getMode()) {
            case model::Gen5TimerMode::STANDARD:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(getSecondCalibration()));
                break;
            case model::Gen5TimerMode::C_GEAR:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(getDelayCalibration()));
                break;
            case model::Gen5TimerMode::ENTRALINK:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(getSecondCalibration()));
                model->setEntralinkCalibration(model->getEntralinkCalibration() +
                                               calibrationService->calibrateToDelays(getEntralinkCalibration()));
                break;
            case model::Gen5TimerMode::ENTRALINK_PLUS:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(getSecondCalibration()));
                model->setEntralinkCalibration(model->getEntralinkCalibration() +
                                               calibrationService->calibrateToDelays(getEntralinkCalibration()));
                model->setFrameCalibration(model->getFrameCalibration() + getAdvancesCalibration());
                break;
        }
        model->setDelayHit(0);
        model->setSecondHit(0);
        model->setAdvancesHit(0);
    }

    int Gen5TimerPane::getDelayCalibration() const {
        return delayTimer->calibrate(model->getTargetDelay(), model->getDelayHit());
    }

    int Gen5TimerPane::getSecondCalibration() const {
        return secondTimer->calibrate(model->getTargetSecond(), model->getSecondHit());
    }

    int Gen5TimerPane::getEntralinkCalibration() const {
        return entralinkTimer->calibrate(model->getTargetDelay(), model->getDelayHit() - getSecondCalibration());
    }

    int Gen5TimerPane::getAdvancesCalibration() const {
        return enhancedEntralinkTimer->calibrate(model->getTargetAdvances(), model->getAdvancesHit());
    }
}  // namespace gui::timer
