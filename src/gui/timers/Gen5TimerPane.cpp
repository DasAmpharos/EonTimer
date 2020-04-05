//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerPane.h"

#include <QLabel>
#include <QScrollArea>

namespace gui::timer {
    Gen5TimerPane::Gen5TimerPane(
        model::timer::Gen5TimerModel *model,
        const service::timer::DelayTimer *delayTimer,
        const service::timer::SecondTimer *secondTimer,
        const service::timer::EntralinkTimer *entralinkTimer,
        const service::timer::EnhancedEntralinkTimer *enhancedEntralinkTimer,
        const service::CalibrationService *calibrationService, QWidget *parent)
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
        void (*setVisible)(QGridLayout *, util::FieldSet<QSpinBox> &,
                           const bool) = util::setVisible;
        // ----- mode -----
        {
            auto *form = new QGridLayout();
            rootLayout->addLayout(form);

            util::FieldSet<QComboBox> mode(0, new QLabel("Mode"),
                                           new QComboBox);
            for (const auto currentMode : model::gen5TimerModes()) {
                mode.field->addItem(model::getName(currentMode), currentMode);
            }
            mode.field->setCurrentText(model::getName(model->getMode()));
            connect(mode.field,
                    QOverload<int>::of(&QComboBox::currentIndexChanged),
                    [this](const int currentIndex) {
                        model->setMode(model::gen5TimerModes()[currentIndex]);
                        emit timerChanged(createStages());
                    });
            util::addFieldSet(form, mode);
        }
        // ----- timer fields -----
        {
            auto *scrollPane = new QWidget();
            scrollPane->setProperty("class", "bg-transparent-white");
            scrollPane->setSizePolicy(QSizePolicy::Expanding,
                                      QSizePolicy::Expanding);
            auto *scrollPaneLayout = new QVBoxLayout(scrollPane);
            scrollPaneLayout->setContentsMargins(0, 0, 0, 0);
            scrollPaneLayout->setSpacing(10);
            auto *scrollArea = new QScrollArea();
            scrollArea->setFrameShape(QFrame::NoFrame);
            scrollArea->setProperty("class", "themeable");
            scrollArea->setVerticalScrollBarPolicy(
                Qt::ScrollBarPolicy::ScrollBarAsNeeded);
            scrollArea->setHorizontalScrollBarPolicy(
                Qt::ScrollBarPolicy::ScrollBarAsNeeded);
            scrollArea->setWidgetResizable(true);
            scrollArea->setWidget(scrollPane);
            rootLayout->addWidget(scrollArea);

            auto *formWidget = new QWidget();
            formWidget->setSizePolicy(QSizePolicy::Expanding,
                                      QSizePolicy::Fixed);
            scrollPaneLayout->addWidget(formWidget, 0, Qt::AlignTop);
            auto *form = new QGridLayout(formWidget);
            form->setSpacing(10);
            // ----- calibration -----
            {
                auto *calibration = new util::FieldSet<QSpinBox>(
                    0, new QLabel("Calibration"), new QSpinBox);
                calibration->field->setRange(INT_MIN, INT_MAX);
                calibration->field->setValue(model->getCalibration());
                connect(calibration->field, valueChanged,
                        [this](const int calibration) {
                            model->setCalibration(calibration);
                            emit timerChanged(createStages());
                        });
                util::addFieldSet(form, *calibration);
            }
            // ----- targetDelay -----
            {
                auto *targetDelay = new util::FieldSet<QSpinBox>(
                    1, new QLabel("Target Delay"), new QSpinBox);
                targetDelay->field->setRange(0, INT_MAX);
                targetDelay->field->setValue(model->getTargetDelay());
                connect(targetDelay->field, valueChanged,
                        [this](const int targetDelay) {
                            model->setTargetDelay(targetDelay);
                            emit timerChanged(createStages());
                        });
                connect(model, &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form,
                         targetDelay](const model::Gen5TimerMode mode) {
                            setVisible(form, *targetDelay,
                                       mode != model::Gen5TimerMode::STANDARD);
                        });
                util::addFieldSet(form, *targetDelay);
            }
            // ----- targetSecond -----
            {
                auto *targetSecond = new util::FieldSet<QSpinBox>(
                    2, new QLabel("Target Second"), new QSpinBox);
                targetSecond->field->setRange(0, 59);
                targetSecond->field->setValue(model->getTargetSecond());
                connect(targetSecond->field, valueChanged,
                        [this](const int targetSecond) {
                            model->setTargetSecond(targetSecond);
                            emit timerChanged(createStages());
                        });
                util::addFieldSet(form, *targetSecond);
            }
            // ----- entralinkCalibration -----
            {
                auto *entralinkCalibration = new util::FieldSet<QSpinBox>(
                    3, new QLabel("Entralink Calibration"), new QSpinBox);
                entralinkCalibration->field->setRange(INT_MIN, INT_MAX);
                entralinkCalibration->field->setValue(
                    model->getEntralinkCalibration());
                connect(
                    entralinkCalibration->field, valueChanged,
                    [this](const int entralinkCalibration) {
                        model->setEntralinkCalibration(entralinkCalibration);
                        emit timerChanged(createStages());
                    });
                connect(
                    model, &model::timer::Gen5TimerModel::modeChanged,
                    [setVisible, form,
                     entralinkCalibration](const model::Gen5TimerMode mode) {
                        setVisible(
                            form, *entralinkCalibration,
                            mode == model::Gen5TimerMode::ENTRALINK ||
                                mode == model::Gen5TimerMode::ENTRALINK_PLUS);
                    });
                util::addFieldSet(form, *entralinkCalibration);
            }
            // ----- frameCalibration -----
            {
                auto *frameCalibration = new util::FieldSet<QSpinBox>(
                    4, new QLabel("Frame Calibration"), new QSpinBox);
                frameCalibration->field->setRange(INT_MIN, INT_MAX);
                frameCalibration->field->setValue(model->getFrameCalibration());
                connect(frameCalibration->field, valueChanged,
                        [this](const int frameCalibration) {
                            model->setFrameCalibration(frameCalibration);
                            emit timerChanged(createStages());
                        });
                connect(model, &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form,
                         frameCalibration](const model::Gen5TimerMode mode) {
                            setVisible(
                                form, *frameCalibration,
                                mode == model::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                util::addFieldSet(form, *frameCalibration);
            }
            // ----- targetAdvances -----
            {
                auto *targetAdvances = new util::FieldSet<QSpinBox>(
                    5, new QLabel("Target Advances"), new QSpinBox);
                targetAdvances->field->setRange(0, INT_MAX);
                targetAdvances->field->setValue(model->getTargetAdvances());
                connect(targetAdvances->field, valueChanged,
                        [this](const int targetAdvances) {
                            model->setTargetAdvances(targetAdvances);
                            emit timerChanged(createStages());
                        });
                connect(model, &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form,
                         targetAdvances](const model::Gen5TimerMode mode) {
                            setVisible(
                                form, *targetAdvances,
                                mode == model::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                util::addFieldSet(form, *targetAdvances);
            }
        }
        // ----- calibration fields -----
        {
            auto *form = new QGridLayout();
            rootLayout->addLayout(form);
            form->setSpacing(10);
            // ----- delayHit -----
            {
                auto *delayHit = new util::FieldSet<QSpinBox>(
                    0, new QLabel("Delay Hit"), new QSpinBox);
                delayHit->field->setRange(0, INT_MAX);
                connect(delayHit->field, valueChanged,
                        [this](const int value) { model->setDelayHit(value); });
                connect(model, &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form,
                         delayHit](const model::Gen5TimerMode mode) {
                            setVisible(form, *delayHit,
                                       mode != model::Gen5TimerMode::STANDARD);
                        });
                util::addFieldSet(form, *delayHit);
            }
            // ----- secondHit -----
            {
                auto *secondHit = new util::FieldSet<QSpinBox>(
                    1, new QLabel("Second Hit"), new QSpinBox);
                secondHit->field->setRange(0, 59);
                connect(
                    secondHit->field, valueChanged,
                    [this](const int value) { model->setSecondHit(value); });
                connect(model, &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form,
                         secondHit](const model::Gen5TimerMode mode) {
                            setVisible(form, *secondHit,
                                       mode != model::Gen5TimerMode::C_GEAR);
                        });
                util::addFieldSet(form, *secondHit);
            }
            // ----- advancesHit -----
            {
                auto *advancesHit = new util::FieldSet<QSpinBox>(
                    2, new QLabel("Advances Hit"), new QSpinBox);
                advancesHit->field->setRange(0, INT_MAX);
                connect(
                    advancesHit->field, valueChanged,
                    [this](const int value) { model->setAdvancesHit(value); });
                connect(model, &model::timer::Gen5TimerModel::modeChanged,
                        [setVisible, form,
                         advancesHit](const model::Gen5TimerMode mode) {
                            setVisible(
                                form, *advancesHit,
                                mode == model::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                util::addFieldSet(form, *advancesHit);
            }
        }
        // force all fieldsets to update visibility
        emit model->modeChanged(model->getMode());
    }

    std::shared_ptr<std::vector<int>> Gen5TimerPane::createStages() {
        std::shared_ptr<std::vector<int>> stages;
        switch (model->getMode()) {
            case model::Gen5TimerMode::STANDARD:
                stages = secondTimer->createStages(
                    model->getTargetSecond(),
                    calibrationService->calibrateToMilliseconds(
                        model->getCalibration()));
                break;
            case model::Gen5TimerMode::C_GEAR:
                stages = delayTimer->createStages(
                    model->getTargetDelay(), model->getTargetSecond(),
                    calibrationService->calibrateToMilliseconds(
                        model->getCalibration()));
                break;
            case model::Gen5TimerMode::ENTRALINK:
                stages = entralinkTimer->createStages(
                    model->getTargetDelay(), model->getTargetSecond(),
                    calibrationService->calibrateToMilliseconds(
                        model->getCalibration()),
                    calibrationService->calibrateToMilliseconds(
                        model->getEntralinkCalibration()));
                break;
            case model::Gen5TimerMode::ENTRALINK_PLUS:
                stages = enhancedEntralinkTimer->createStages(
                    model->getTargetDelay(), model->getTargetSecond(),
                    model->getTargetAdvances(),
                    calibrationService->calibrateToMilliseconds(
                        model->getCalibration()),
                    calibrationService->calibrateToMilliseconds(
                        model->getEntralinkCalibration()),
                    model->getFrameCalibration());
                break;
        }
        return stages;
    }

    void Gen5TimerPane::calibrate() {
        switch (model->getMode()) {
            case model::Gen5TimerMode::STANDARD:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(
                                          getSecondCalibration()));
                break;
            case model::Gen5TimerMode::C_GEAR:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(
                                          getDelayCalibration()));
                break;
            case model::Gen5TimerMode::ENTRALINK:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(
                                          getSecondCalibration()));
                model->setEntralinkCalibration(
                    model->getEntralinkCalibration() +
                    calibrationService->calibrateToDelays(
                        getEntralinkCalibration()));
                break;
            case model::Gen5TimerMode::ENTRALINK_PLUS:
                model->setCalibration(model->getCalibration() +
                                      calibrationService->calibrateToDelays(
                                          getSecondCalibration()));
                model->setEntralinkCalibration(
                    model->getEntralinkCalibration() +
                    calibrationService->calibrateToDelays(
                        getEntralinkCalibration()));
                model->setFrameCalibration(model->getFrameCalibration() +
                                           getAdvancesCalibration());
                break;
        }
    }

    int Gen5TimerPane::getDelayCalibration() const {
        return delayTimer->calibrate(model->getTargetDelay(),
                                     model->getDelayHit());
    }

    int Gen5TimerPane::getSecondCalibration() const {
        return secondTimer->calibrate(model->getTargetSecond(),
                                      model->getSecondHit());
    }

    int Gen5TimerPane::getEntralinkCalibration() const {
        return entralinkTimer->calibrate(
            model->getTargetDelay(),
            model->getDelayHit() - getSecondCalibration());
    }

    int Gen5TimerPane::getAdvancesCalibration() const {
        return enhancedEntralinkTimer->calibrate(model->getTargetAdvances(),
                                                 model->getAdvancesHit());
    }
}  // namespace gui::timer
