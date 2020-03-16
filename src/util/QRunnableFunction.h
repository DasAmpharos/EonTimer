//
// Created by Dylan Meadows on 2020-03-16.
//

#ifndef EONTIMER_QRUNNABLEFUNCTION_H
#define EONTIMER_QRUNNABLEFUNCTION_H

#include <QRunnable>
#include <functional>

namespace util {
    class QRunnableFunction : public QRunnable {
    private:
        const std::function<void(void)> function;
    public:
        explicit QRunnableFunction(const std::function<void()> &function);

        void run() override;
    };
}


#endif //EONTIMER_QRUNNABLEFUNCTION_H
