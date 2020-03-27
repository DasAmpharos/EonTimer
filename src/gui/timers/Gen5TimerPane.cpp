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
          calibrationService(calibrationService),
          mode({0, new QLabel("Mode"), new QComboBox}),
          calibration({0, new QLabel("Calibration"), new QSpinBox}),
          targetDelay({1, new QLabel("Target Delay"), new QSpinBox}),
          targetSecond({2, new QLabel("Target Second"), new QSpinBox}),
          entralinkCalibration({3, new QLabel("Entralink Calibration"), new QSpinBox}),
          frameCalibration({4, new QLabel("Frame Calibration"), new QSpinBox}),
          targetAdvances({5, new QLabel("Target Advances"), new QSpinBox}),
          delayHit({0, new QLabel("Delay Hit"), new QSpinBox}),
          secondHit({1, new QLabel("Second Hit"), new QSpinBox}),
          advancesHit({2, new QLabel("Advances Hit"), new QSpinBox}) {
        initComponents();
    }

    void Gen5TimerPane::initComponents() {
        auto *rootLayout = new QVBoxLayout(this);
        rootLayout->setContentsMargins(10, 0, 10, 10);
        rootLayout->setSpacing(10);

        void (QSpinBox::* valueChanged)(int) = &QSpinBox::valueChanged;
        // ----- mode -----
        {
            auto *form = new QGridLayout();
            rootLayout->addLayout(form);

            mode.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            for (const auto mode : model::gen5TimerModes()) {
                this->mode.field->addItem(model::getName(mode), mode);
            }
            mode.field->setCurrentText(model::getName(settings->getMode()));
            connect(mode.field, QOverload<int>::of(&QComboBox::currentIndexChanged),
                    [this](const int currentIndex) {
                        settings->setMode(model::gen5TimerModes()[currentIndex]);
                        emit shouldUpdate();
                        updateComponents();
                    });
            util::addFieldSet(form, mode);
        }
        // ----- timer fields -----
        {
            auto *scrollArea = new QScrollArea();
            scrollArea->setFrameShape(QFrame::NoFrame);
            scrollArea->setProperty("class", "themeable");
            scrollArea->setVerticalScrollBarPolicy(Qt::ScrollBarPolicy::ScrollBarAsNeeded);
            scrollArea->setHorizontalScrollBarPolicy(Qt::ScrollBarPolicy::ScrollBarAsNeeded);
            scrollArea->setWidgetResizable(true);
            rootLayout->addWidget(scrollArea);

            auto *scrollPane = new QWidget(scrollArea);
            auto *scrollPaneLayout = new QVBoxLayout(scrollPane);
            scrollPaneLayout->setSpacing(10);
            scrollPaneLayout->setContentsMargins(0, 0, 0, 0);
            scrollPane->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
            scrollPane->setProperty("class", "bg-transparent-white");
            scrollArea->setWidget(scrollPane);

            auto *formWidget = new QWidget();
            formWidget->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            scrollPaneLayout->addWidget(formWidget, 0, Qt::AlignTop);
            timerForm = new QGridLayout(formWidget);
            timerForm->setSpacing(10);
            // ----- calibration -----
            {
                calibration.field->setRange(INT_MIN, INT_MAX);
                calibration.field->setValue(settings->getCalibration());
                calibration.label->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);
                calibration.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(calibration.field, valueChanged, [this](const int calibration) {
                    settings->setCalibration(calibration);
                    createStages();
                });
                util::addFieldSet(timerForm, calibration);
            }
            // ----- targetDelay -----
            {
                targetDelay.field->setRange(0, INT_MAX);
                targetDelay.field->setValue(settings->getTargetDelay());
                targetDelay.label->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);
                targetDelay.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(targetDelay.field, valueChanged, [this](const int targetDelay) {
                    settings->setTargetDelay(targetDelay);
                    createStages();
                });
                util::addFieldSet(timerForm, targetDelay);
            }
            // ----- targetSecond -----
            {
                targetSecond.field->setRange(0, 59);
                targetSecond.field->setValue(settings->getTargetSecond());
                targetSecond.label->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);
                targetSecond.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(targetSecond.field, valueChanged, [this](const int targetSecond) {
                    settings->setTargetSecond(targetSecond);
                    createStages();
                });
                util::addFieldSet(timerForm, targetSecond);
            }
            // ----- entralinkCalibration -----
            {
                entralinkCalibration.field->setRange(INT_MIN, INT_MAX);
                entralinkCalibration.field->setValue(settings->getEntralinkCalibration());
                entralinkCalibration.label->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);
                entralinkCalibration.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(entralinkCalibration.field, valueChanged, [this](const int entralinkCalibration) {
                    settings->setEntralinkCalibration(entralinkCalibration);
                    createStages();
                });
                util::addFieldSet(timerForm, entralinkCalibration);
            }
            // ----- frameCalibration -----
            {
                frameCalibration.field->setRange(INT_MIN, INT_MAX);
                frameCalibration.field->setValue(settings->getFrameCalibration());
                frameCalibration.label->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);
                frameCalibration.field->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);
                connect(frameCalibration.field, valueChanged, [this](const int frameCalibration) {
                    settings->setFrameCalibration(frameCalibration);
                    createStages();
                });
                util::addFieldSet(timerForm, frameCalibration);
            }
            // ----- targetAdvances -----
            {
                targetAdvances.field->setRange(0, INT_MAX);
                targetAdvances.field->setValue(settings->getTargetAdvances());
                targetAdvances.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                connect(targetAdvances.field, valueChanged, [this](const int targetAdvances) {
                    settings->setFrameCalibration(targetAdvances);
                    createStages();
                });
                util::addFieldSet(timerForm, targetAdvances);
            }
        }
        // ----- calibration fields -----
        {
            calibrationForm = new QGridLayout();
            calibrationForm->setSpacing(10);
            rootLayout->addLayout(calibrationForm);
            // ----- delayHit -----
            {
                delayHit.field->setRange(0, INT_MAX);
                delayHit.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                util::addFieldSet(calibrationForm, delayHit);
            }
            // ----- secondHit -----
            {
                secondHit.field->setRange(0, 59);
                secondHit.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                util::addFieldSet(calibrationForm, secondHit);
            }
            // ----- advancesHit -----
            {
                advancesHit.field->setRange(0, INT_MAX);
                advancesHit.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
                util::addFieldSet(calibrationForm, advancesHit);
            }
        }
        updateComponents();
    }

    void Gen5TimerPane::updateComponents() {
        const auto mode = settings->getMode();
        const bool isCGear = mode == model::Gen5TimerMode::C_GEAR;
        const bool isStandard = mode == model::Gen5TimerMode::STANDARD;
        const bool isEntralink = mode == model::Gen5TimerMode::ENTRALINK;
        const bool isEnhancedEntralink = mode == model::Gen5TimerMode::ENTRALINK_PLUS;

        util::setVisible(timerForm, targetDelay, !isStandard);
        util::setVisible(timerForm, entralinkCalibration, isEntralink || isEnhancedEntralink);
        util::setVisible(timerForm, frameCalibration, isEnhancedEntralink);
        util::setVisible(timerForm, targetAdvances, isEnhancedEntralink);

        util::setVisible(calibrationForm, delayHit, !isStandard);
        util::setVisible(calibrationForm, secondHit, !isCGear);
        util::setVisible(calibrationForm, advancesHit, isEnhancedEntralink);
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
                break;
        }
    }

    int Gen5TimerPane::getDelayCalibration() const {
        return delayTimer->calibrate(targetDelay.field->value(), delayHit.field->value());
    }

    int Gen5TimerPane::getSecondCalibration() const {
        return secondTimer->calibrate(targetSecond.field->value(), secondHit.field->value());
    }

    int Gen5TimerPane::getEntralinkCalibration() const {
        return entralinkTimer->calibrate(targetDelay.field->value(), delayHit.field->value() - getSecondCalibration());
    }
}
