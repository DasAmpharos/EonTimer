//
// Created by Dylan Meadows on 2020-03-16.
//

#include "QRunnableFunction.h"

namespace util {
    QRunnableFunction::QRunnableFunction(const std::function<void()> &function) : function(function) {}

    void QRunnableFunction::run() {
        function();
    }
}
