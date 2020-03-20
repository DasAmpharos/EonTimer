//
// Created by Dylan Meadows on 2020-03-16.
//

#include "ActionSettingsPane.h"
#include <QFormLayout>
#include <QLabel>

namespace gui::settings {
    ActionSettingsPane::ActionSettingsPane(service::settings::ActionSettings *settings, QWidget *parent)
        : QWidget(parent),
          settings(settings) {
        initComponents();
    }

    void ActionSettingsPane::initComponents() {
        auto *layout = new QFormLayout(this);
        layout->setFieldGrowthPolicy(QFormLayout::ExpandingFieldsGrow);
        // ----- sound -----
        {
            auto *label = new QLabel();
            label->setText("Sound");
            sound = new QComboBox();
            for (auto &mSound : model::sounds()) {
                sound->addItem(model::getName(mSound), mSound);
            }
            sound->setCurrentText(model::getName(settings->getSound()));
            sound->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
            layout->addRow(label, sound);
        }
        // ----- interval -----
        {
            auto *label = new QLabel();
            label->setText("Interval");
            interval = new QSpinBox();
            interval->setRange(1, 1000);
            interval->setValue(static_cast<int>(settings->getInterval().count()));
            interval->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
            layout->addRow(label, interval);
        }
        // ----- count -----
        {
            auto *label = new QLabel();
            label->setText("Count");
            count = new QSpinBox;
            count->setRange(1, 50);
            count->setValue(settings->getCount());
            count->setSizePolicy(
                QSizePolicy::Expanding,
                QSizePolicy::Fixed
            );
            layout->addRow(label, count);
        }
    }

    void ActionSettingsPane::updateSettings() {
        settings->setSound(model::sounds()[sound->currentIndex()]);
        settings->setInterval(std::chrono::milliseconds(interval->value()));
        settings->setCount(static_cast<uint>(count->value()));
    }
}
