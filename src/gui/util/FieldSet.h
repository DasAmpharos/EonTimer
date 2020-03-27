//
// Created by Dylan Meadows on 2020-03-25.
//

#ifndef EONTIMER_FIELDSET_H
#define EONTIMER_FIELDSET_H

#include <QWidget>
#include <QGridLayout>
#include <type_traits>
#include <iostream>

namespace gui::util {
    template<typename T>
    struct FieldSet {
        static_assert(std::is_base_of<QWidget, T>::value, "T must derive from QWidget");

        QGridLayout *layout;
        const int rowIndex;
        QWidget *label;
        T *field;

        FieldSet(const int rowIndex, QWidget *label, T *field)
            : layout(nullptr),
              rowIndex(rowIndex),
              label(label),
              field(field) {
        }
    };

    template<typename T>
    void addFieldSet(QGridLayout *layout, FieldSet<T> &fieldSet) {
        if (fieldSet.layout == nullptr) {
            fieldSet.field->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
            layout->addWidget(fieldSet.label, fieldSet.rowIndex, 0, Qt::AlignRight);
            layout->addWidget(fieldSet.field, fieldSet.rowIndex, 1);
            fieldSet.layout = layout;
        }
    }

    template<typename T>
    void removeFieldSet(QGridLayout *layout, FieldSet<T> &fieldSet) {
        if (fieldSet.layout == layout) {
            layout->removeWidget(fieldSet.label);
            layout->removeWidget(fieldSet.field);
            fieldSet.layout = nullptr;
        }
    }

    template<typename T>
    void setVisible(QGridLayout *layout, FieldSet<T> &fieldSet, const bool visible) {
        if (visible) {
            show(layout, fieldSet);
        } else {
            hide(layout, fieldSet);
        }
    }

    template<typename T>
    void hide(QGridLayout *layout, FieldSet<T> &fieldSet) {
        if (fieldSet.layout != nullptr) {
            removeFieldSet(layout, fieldSet);
            fieldSet.label->hide();
            fieldSet.field->hide();
        }
    }

    template<typename T>
    void show(QGridLayout *layout, FieldSet<T> &fieldSet) {
        if (fieldSet.layout == nullptr) {
            addFieldSet(layout, fieldSet);
            fieldSet.label->show();
            fieldSet.field->show();
        }
    }
}

#endif //EONTIMER_FIELDSET_H
