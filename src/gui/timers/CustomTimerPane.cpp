//
// Created by Dylan Meadows on 2020-03-29.
//

#include "CustomTimerPane.h"

#include <QGridLayout>
#include <QListView>
#include <QListWidget>
#include <QPushButton>
#include <QSpinBox>
#include <QStringListModel>
#include <iostream>

namespace gui::timer {
    CustomTimerPane::CustomTimerPane(model::timer::CustomTimerModel *model,
                                     QWidget *parent)
        : QWidget(parent), model(model) {
        initComponents();
    }

    void CustomTimerPane::initComponents() {
        auto *rootLayout = new QGridLayout(this);
        rootLayout->setContentsMargins(10, 0, 10, 10);
        rootLayout->setSpacing(10);

        auto *list = new QStringList();
        auto *model = new QStringListModel();
        for (const auto stage : this->model->getStages()) {
            list->append(QString::number(stage));
        }
        model->setStringList(*list);

        auto *listView = new QListView();
        listView->setModel(model);
        rootLayout->addWidget(listView, 0, 0, 1, 3);

        auto *inputField = new QSpinBox();
        inputField->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
        rootLayout->addWidget(inputField, 1, 0);

        auto *addButton = new QPushButton("+");
        connect(addButton, &QPushButton::clicked, [list, model, inputField] {
            auto index = model->index(list->size(), 0);
            model->setData(index, inputField->value(), Qt::DisplayRole);
        });
        addButton->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);
        rootLayout->addWidget(addButton, 1, 1);

        auto *removeButton = new QPushButton("-");
        removeButton->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);
        rootLayout->addWidget(removeButton, 1, 2);
    }
}  // namespace gui::timer
