//
// Created by Dylan Meadows on 2020-03-16.
//

#include "ActionSettingsPane.h"

#include <gui/util/FieldSet.h>
#include <gui/util/WidgetHelper.h>

#include <QColorDialog>
#include <QFormLayout>
#include <QLabel>
#include <QPushButton>

namespace gui::settings {
    ActionSettingsPane::ActionSettingsPane(model::settings::ActionSettingsModel *settings, QWidget *parent)
        : QWidget(parent), settings(settings) {
        mode = settings->getMode();
        sound = settings->getSound();
        color = settings->getColor();
        interval = settings->getInterval();
        count = settings->getCount();
        initComponents();
    }

    void ActionSettingsPane::initComponents() {
        auto *form = new QGridLayout(this);
        // ----- mode -----
        {
            auto *field = new QComboBox;
            auto *fieldSet = new util::FieldSet<QComboBox>(0, new QLabel("Mode"), field);
            gui::util::widget::addItems<model::ActionMode>(field, model::actionModes(), [](model::ActionMode mode) {
                return model::getName(mode);
            });
            field->setCurrentText(model::getName(settings->getMode()));
            util::addFieldSet(form, *fieldSet);

            connect(field, SIGNAL(currentIndexChanged(int)), this, SLOT(setMode(int)));
        }
        // ----- sound -----
        {
            auto *field = new QComboBox;
            auto *fieldSet = new util::FieldSet<QComboBox>(1, new QLabel("Sound"), field);
            gui::util::widget::addItems<model::Sound>(field, model::sounds(), [](model::Sound sound) {
                return model::getName(sound);
            });
            field->setCurrentText(model::getName(settings->getSound()));
            util::addFieldSet(form, *fieldSet);

            connect(field, SIGNAL(currentIndexChanged(int)), this, SLOT(setSound(int)));
            connect(this, &ActionSettingsPane::modeChanged, [form, fieldSet](const int mode) {
                const bool visible = mode == model::ActionMode::AUDIO || mode == model::ActionMode::AV;
                util::setVisible(form, *fieldSet, visible);
            });
        }
        // ----- color -----
        {
            auto *field = new QPushButton;
            auto *fieldSet = new util::FieldSet<QPushButton>(2, new QLabel("Color"), field);
            setIconColor(field, color);
            connect(field, &QPushButton::clicked, [this, field] {
                const QColor selectedColor = QColorDialog::getColor(color, this);
                if (selectedColor.isValid()) {
                    setIconColor(field, selectedColor);
                    color = selectedColor;
                }
            });
            connect(this, &ActionSettingsPane::modeChanged, [form, fieldSet](const int mode) {
                const bool visible = mode == model::ActionMode::VISUAL || mode == model::ActionMode::AV;
                util::setVisible(form, *fieldSet, visible);
            });
            util::addFieldSet(form, *fieldSet);
        }
        // ----- interval -----
        {
            auto *field = new QSpinBox;
            auto *fieldSet = new util::FieldSet<QSpinBox>(3, new QLabel("Interval"), field);
            gui::util::widget::setModel(field, 1, 1000, settings->getInterval());
            util::addFieldSet(form, *fieldSet);

            connect(field, SIGNAL(valueChanged(int)), this, SLOT(setInterval(int)));
        }
        // ----- count -----
        {
            auto *field = new QSpinBox;
            auto *fieldSet = new util::FieldSet<QSpinBox>(4, new QLabel("Count"), field);
            gui::util::widget::setModel(field, 1, 50, settings->getCount());
            util::addFieldSet(form, *fieldSet);

            connect(field, SIGNAL(valueChanged(int)), this, SLOT(setCount(int)));
        }
        // force all fieldsets to update visibility
        emit modeChanged(mode);
    }

    void ActionSettingsPane::updateSettings() {
        settings->setMode(model::actionMode(static_cast<unsigned int>(mode)));
        settings->setSound(model::sound(static_cast<unsigned int>(sound)));
        settings->setInterval(static_cast<unsigned int>(interval));
        settings->setCount(static_cast<unsigned int>(count));
        settings->setColor(color);
    }

    void ActionSettingsPane::setMode(int mode) {
        if (this->mode != mode) {
            emit modeChanged(mode);
            this->mode = mode;
        }
    }

    void ActionSettingsPane::setSound(int sound) { this->sound = sound; }

    void ActionSettingsPane::setInterval(int interval) { this->interval = interval; }

    void ActionSettingsPane::setCount(int count) { this->count = count; }

    void ActionSettingsPane::setIconColor(QPushButton *btn, const QColor &color) {
        QPixmap pixmap(64, 64);
        pixmap.fill(color);
        QIcon icon(pixmap);
        btn->setIcon(icon);
    }
}  // namespace gui::settings
