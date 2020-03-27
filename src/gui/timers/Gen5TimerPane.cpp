//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerPane.h"
#include <QScrollArea>
#include <QLabel>

namespace gui::timer {
    Gen5TimerPane::Gen5TimerPane(service::settings::Gen5TimerSettings *settings,
                                 const service::timer::DelayTimer *delayTimer,
                                 const service::timer::SecondTimer *secondTimer,
                                 const service::timer::EntralinkTimer *entralinkTimer,
                                 const service::timer::EnhancedEntralinkTimer *enhancedEntralinkTimer,
                                 const service::CalibrationService *calibrationService,
                                 QWidget *parent)
        : QWidget(parent),
          settings(settings),
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

        void (QSpinBox::* valueChanged)(int) = &QSpinBox::valueChanged;
        void (*setVisible)(QGridLayout *, util::FieldSet<QSpinBox> &, const bool) = util::setVisible;
        // ----- mode -----
        {
            auto *form = new QGridLayout();
            rootLayout->addLayout(form);

            util::FieldSet<QComboBox> mode(0, new QLabel("Mode"), new QComboBox);
            for (const auto currentMode : model::gen5TimerModes()) {
                mode.field->addItem(model::getName(currentMode), currentMode);
            }
            mode.field->setCurrentText(model::getName(settings->getMode()));
            connect(mode.field, QOverload<int>::of(&QComboBox::currentIndexChanged),
                    [this](const int currentIndex) {
                        settings->setMode(model::gen5TimerModes()[currentIndex]);
                    });
            util::addFieldSet(form, mode);
        }
        // ----- timer fields -----
        {
            auto *scrollPane = new QWidget();
            scrollPane->setProperty("class", "bg-transparent-white");
            scrollPane->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
            auto *scrollPaneLayout = new QVBoxLayout(scrollPane);
            scrollPaneLayout->setContentsMargins(0, 0, 0, 0);
            scrollPaneLayout->setSpacing(10);
            auto *scrollArea = new QScrollArea();
            scrollArea->setFrameShape(QFrame::NoFrame);
            scrollArea->setProperty("class", "themeable");
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
                auto *calibration = new util::FieldSet<QSpinBox>(0, new QLabel("Calibration"), new QSpinBox);
                calibration->field->setRange(INT_MIN, INT_MAX);
                calibration->field->setValue(settings->getCalibration());
                connect(calibration->field, valueChanged,
                        [this](const int calibration) {
                            settings->setCalibration(calibration);
                            createStages();
                        });
                util::addFieldSet(form, *calibration);
            }
            // ----- targetDelay -----
            {
                auto *targetDelay = new util::FieldSet<QSpinBox>(1, new QLabel("Target Delay"), new QSpinBox);
                targetDelay->field->setRange(0, INT_MAX);
                targetDelay->field->setValue(settings->getTargetDelay());
                connect(targetDelay->field, valueChanged,
                        [this](const int targetDelay) {
                            settings->setTargetDelay(targetDelay);
                            createStages();
                        });
                connect(settings, &service::settings::Gen5TimerSettings::modeChanged,
                        [setVisible, form, targetDelay](const model::Gen5TimerMode mode) {
                            setVisible(form, *targetDelay, mode != model::Gen5TimerMode::STANDARD);
                        });
                util::addFieldSet(form, *targetDelay);
            }
            // ----- targetSecond -----
            {
                auto *targetSecond = new util::FieldSet<QSpinBox>(2, new QLabel("Target Second"), new QSpinBox);
                targetSecond->field->setRange(0, 59);
                targetSecond->field->setValue(settings->getTargetSecond());
                connect(targetSecond->field, valueChanged,
                        [this](const int targetSecond) {
                            settings->setTargetSecond(targetSecond);
                            createStages();
                        });
                util::addFieldSet(form, *targetSecond);
            }
            // ----- entralinkCalibration -----
            {
                auto *entralinkCalibration = new util::FieldSet<QSpinBox>(3, new QLabel("Entralink Calibration"),
                                                                          new QSpinBox);
                entralinkCalibration->field->setRange(INT_MIN, INT_MAX);
                entralinkCalibration->field->setValue(settings->getEntralinkCalibration());
                connect(entralinkCalibration->field, valueChanged,
                        [this](const int entralinkCalibration) {
                            settings->setEntralinkCalibration(entralinkCalibration);
                            createStages();
                        });
                connect(settings, &service::settings::Gen5TimerSettings::modeChanged,
                        [setVisible, form, entralinkCalibration](const model::Gen5TimerMode mode) {
                            setVisible(form, *entralinkCalibration, mode == model::Gen5TimerMode::ENTRALINK ||
                                                                    mode == model::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                util::addFieldSet(form, *entralinkCalibration);
            }
            // ----- frameCalibration -----
            {
                auto *frameCalibration = new util::FieldSet<QSpinBox>(4, new QLabel("Frame Calibration"), new QSpinBox);
                frameCalibration->field->setRange(INT_MIN, INT_MAX);
                frameCalibration->field->setValue(settings->getFrameCalibration());
                connect(frameCalibration->field, valueChanged,
                        [this](const int frameCalibration) {
                            settings->setFrameCalibration(frameCalibration);
                            createStages();
                        });
                connect(settings, &service::settings::Gen5TimerSettings::modeChanged,
                        [setVisible, form, frameCalibration](const model::Gen5TimerMode mode) {
                            setVisible(form, *frameCalibration, mode == model::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                util::addFieldSet(form, *frameCalibration);
            }
            // ----- targetAdvances -----
            {
                auto *targetAdvances = new util::FieldSet<QSpinBox>(5, new QLabel("Target Advances"), new QSpinBox);
                targetAdvances->field->setRange(0, INT_MAX);
                targetAdvances->field->setValue(settings->getTargetAdvances());
                connect(targetAdvances->field, valueChanged,
                        [this](const int targetAdvances) {
                            settings->setTargetAdvances(targetAdvances);
                            createStages();
                        });
                connect(settings, &service::settings::Gen5TimerSettings::modeChanged,
                        [setVisible, form, targetAdvances](const model::Gen5TimerMode mode) {
                            setVisible(form, *targetAdvances, mode == model::Gen5TimerMode::ENTRALINK_PLUS);
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
                delayHit = new QSpinBox();
                delayHit->setRange(0, INT_MAX);
                auto *delayHit = new util::FieldSet<QSpinBox>(0, new QLabel("Delay Hit"), this->delayHit);
                connect(settings, &service::settings::Gen5TimerSettings::modeChanged,
                        [setVisible, form, delayHit](const model::Gen5TimerMode mode) {
                            setVisible(form, *delayHit, mode != model::Gen5TimerMode::STANDARD);
                        });
                util::addFieldSet(form, *delayHit);
            }
            // ----- secondHit -----
            {
                secondHit = new QSpinBox();
                secondHit->setRange(0, 59);
                auto *secondHit = new util::FieldSet<QSpinBox>(1, new QLabel("Second Hit"), this->secondHit);
                connect(settings, &service::settings::Gen5TimerSettings::modeChanged,
                        [setVisible, form, secondHit](const model::Gen5TimerMode mode) {
                            setVisible(form, *secondHit, mode != model::Gen5TimerMode::C_GEAR);
                        });
                util::addFieldSet(form, *secondHit);
            }
            // ----- advancesHit -----
            {
                advancesHit = new QSpinBox();
                advancesHit->setRange(0, INT_MAX);
                auto *advancesHit = new util::FieldSet<QSpinBox>(2, new QLabel("Advances Hit"), this->advancesHit);
                connect(settings, &service::settings::Gen5TimerSettings::modeChanged,
                        [setVisible, form, advancesHit](const model::Gen5TimerMode mode) {
                            setVisible(form, *advancesHit, mode != model::Gen5TimerMode::ENTRALINK_PLUS);
                        });
                util::addFieldSet(form, *advancesHit);
            }
        }
        // force all fieldsets to update visibility
        emit settings->modeChanged(settings->getMode());
    }

    std::shared_ptr<std::vector<int>> Gen5TimerPane::createStages() {
        std::shared_ptr<std::vector<int>> stages;
        switch (settings->getMode()) {
            case model::Gen5TimerMode::C_GEAR:
                stages = delayTimer->createStages(
                    settings->getTargetDelay(),
                    settings->getTargetSecond(),
                    settings->getCalibration()
                );
                break;
            case model::Gen5TimerMode::STANDARD:
                stages = secondTimer->createStages(
                    settings->getTargetSecond(),
                    settings->getCalibration()
                );
                break;
            case model::Gen5TimerMode::ENTRALINK:
                stages = entralinkTimer->createStages(
                    settings->getTargetDelay(),
                    settings->getTargetSecond(),
                    settings->getCalibration(),
                    settings->getEntralinkCalibration()
                );
                break;
            case model::Gen5TimerMode::ENTRALINK_PLUS:
                stages = enhancedEntralinkTimer->createStages(
                    settings->getTargetDelay(),
                    settings->getTargetSecond(),
                    settings->getTargetAdvances(),
                    settings->getCalibration(),
                    settings->getEntralinkCalibration(),
                    settings->getFrameCalibration()
                );
                break;
        }
        return stages;
    }

    void Gen5TimerPane::calibrate() {
        switch (settings->getMode()) {
            case model::Gen5TimerMode::C_GEAR:
                settings->setCalibration(settings->getCalibration() + getDelayCalibration());
                break;
            case model::Gen5TimerMode::STANDARD:
                settings->setCalibration(settings->getCalibration() + getSecondCalibration());
                break;
            case model::Gen5TimerMode::ENTRALINK:
                settings->setCalibration(settings->getCalibration() + getSecondCalibration());
                settings->setEntralinkCalibration(settings->getEntralinkCalibration() + getEntralinkCalibration());
                break;
            case model::Gen5TimerMode::ENTRALINK_PLUS:
                settings->setCalibration(settings->getCalibration() + getSecondCalibration());
                settings->setEntralinkCalibration(settings->getEntralinkCalibration() + getEntralinkCalibration());
                settings->setFrameCalibration(settings->getFrameCalibration() + getAdvancesCalibration());
                break;
        }
    }

    int Gen5TimerPane::getDelayCalibration() const {
        return delayTimer->calibrate(
            settings->getTargetDelay(),
            delayHit->value()
        );
    }

    int Gen5TimerPane::getSecondCalibration() const {
        return secondTimer->calibrate(
            settings->getTargetSecond(),
            secondHit->value()
        );
    }

    int Gen5TimerPane::getEntralinkCalibration() const {
        return entralinkTimer->calibrate(
            settings->getTargetDelay(),
            delayHit->value() - getSecondCalibration()
        );
    }

    int Gen5TimerPane::getAdvancesCalibration() const {
        return enhancedEntralinkTimer->calibrate(
            settings->getTargetAdvances(),
            advancesHit->value()
        );
    }
}
