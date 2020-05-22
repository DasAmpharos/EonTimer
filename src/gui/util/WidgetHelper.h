//
// Created by dmeadows on 5/22/20.
//

#ifndef EONTIMER_FIELDHELPER_H
#define EONTIMER_FIELDHELPER_H

#include <QComboBox>
#include <functional>

namespace gui::util::widget {
    template <typename T>
    void addItems(QComboBox* comboBox, std::vector<T> items, std::function<QString(T)> textMapper) {
        comboBox->clear();
        for (auto item : items) {
            comboBox->addItem(textMapper(item), item);
        }
    }

    void setModel(QSpinBox* spinBox, int min, int max, int value) {
        spinBox->setRange(min, max);
        spinBox->setValue(value);
    }
}  // namespace gui::util::widget

#endif  // EONTIMER_FIELDHELPER_H
