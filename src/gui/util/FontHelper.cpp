//
// Created by Dylan Meadows on 2020-03-10.
//

#include "FontHelper.h"

namespace gui::util::font {
    void setFontSize(QWidget *widget, const int fontSize) {
        QFont font = widget->font();
        font.setPointSize(fontSize);
        widget->setFont(font);
    }
}
