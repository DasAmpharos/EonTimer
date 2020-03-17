//
// Created by Dylan Meadows on 2020-03-16.
//

#ifndef EONTIMER_WORKERTHREAD_H
#define EONTIMER_WORKERTHREAD_H

#include <QObject>
#include <QThread>
#include <functional>
#include <queue>

namespace util {
    class WorkerThread : public QObject {
    Q_OBJECT
        using job = std::function<void(void)>;
    public:
        QThread *thread;
    private:
        std::mutex mutex;
        std::queue<job> jobs;
        std::condition_variable cv;
        bool shutdown;
    public:
        explicit WorkerThread(QObject *parent = nullptr);

        ~WorkerThread() override;

        void moveOnto(QObject *object) const;

        void submit(const job &job);

    private:
        void run();
    };
}

#endif //EONTIMER_WORKERTHREAD_H
