//
// Created by Dylan Meadows on 2020-03-14.
//

#ifndef EONTIMER_TIMERTHREAD_H
#define EONTIMER_TIMERTHREAD_H

#include <QObject>
#include <QThread>
#include <mutex>
#include <queue>

using Job = std::function<void(void)>;

namespace service {
    class TimerThread : public QObject {
    Q_OBJECT
    private:
        QThread *thread;
        std::mutex mutex;
        std::queue<Job> jobs;
        std::condition_variable conditional;
        bool shutdown;
    public:
        explicit TimerThread(QObject *parent = nullptr);

        ~TimerThread() override;

        void submit(Job job);

    private:
        void run();
    };
}


#endif //EONTIMER_TIMERTHREAD_H
