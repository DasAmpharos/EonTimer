//
// Created by Dylan Meadows on 2020-03-16.
//

#include "ActionSettingsPane.h"

#include <QColorDialog>
#include <QFormLayout>
#include <QLabel>
#include <QPushButton>

namespace gui::settings {

    ActionSettingsPane::ActionSettingsPane(model::settings::ActionSettingsModel *settings, QWidget *parent)
        : QWidget(parent), settings(settings) {
        color = settings->getColor();
        initComponents();
    }

    void ActionSettingsPane::initComponents() {
        auto *layout = new QFormLayout(this);
        layout->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
        // ----- mode -----
        {
            auto *label = new QLabel();
            label->setText("Mode");
            mode = new QComboBox();
            for (auto &mMode : model::actionModes()) {
                mode->addItem(model::getName(mMode), mMode);
            }
            mode->setCurrentText(model::getName(settings->getMode()));
            mode->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            layout->addRow(label, mode);
        }
        // ----- sound -----
        {
            auto *label = new QLabel();
            label->setText("Sound");
            sound = new QComboBox();
            for (auto &mSound : model::sounds()) {
                sound->addItem(model::getName(mSound), mSound);
            }
            sound->setCurrentText(model::getName(settings->getSound()));
            sound->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            layout->addRow(label, sound);
        }
        // ----- color -----
        {
            auto *label = new QLabel("Color");
            auto *colorBtn = new QPushButton();
            colorBtn->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            setIconColor(colorBtn, color);
            connect(colorBtn, &QPushButton::clicked, [this, colorBtn] {
                setIconColor(colorBtn, QColorDialog::getColor(color, this));
            });
            layout->addRow(label, colorBtn);
        }
        // ----- interval -----
        {
            auto *label = new QLabel();
            label->setText("Interval");
            interval = new QSpinBox();
            interval->setRange(1, 1000);
            interval->setValue(static_cast<int>(settings->getInterval().count()));
            interval->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            layout->addRow(label, interval);
        }
        // ----- count -----
        {
            auto *label = new QLabel();
            label->setText("Count");
            count = new QSpinBox;
            count->setRange(1, 50);
            count->setValue(settings->getCount());
            count->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            layout->addRow(label, count);
        }
    }

    void ActionSettingsPane::updateSettings() {
        settings->setMode(model::actionModes()[mode->currentIndex()]);
        settings->setSound(model::sounds()[sound->currentIndex()]);
        settings->setColor(color);
        settings->setInterval(std::chrono::milliseconds(interval->value()));
        settings->setCount(static_cast<uint>(count->value()));
    }

    void ActionSettingsPane::setIconColor(QPushButton *btn, const QColor &color) {
        if (color.isValid()) {
            QPixmap pixmap(64, 64);
            pixmap.fill(color);
            QIcon icon(pixmap);
            btn->setIcon(icon);
        }
    }

}  // namespace gui::settings
