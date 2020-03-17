//
// Created by Dylan Meadows on 2020-03-16.
//

#include "WorkerThread.h"

namespace util {
    WorkerThread::WorkerThread(QObject *parent)
        : QObject(parent),
          shutdown(false) {
        thread = QThread::create(&WorkerThread::run, this);
        thread->start();
    }

    WorkerThread::~WorkerThread() {
        {
            std::unique_lock<std::mutex> lock(mutex);
            shutdown = true;
            cv.notify_all();
        }
        thread->quit();
        thread->wait();
    }

    void WorkerThread::moveOnto(QObject *object) const {
        if (object != nullptr) {
            object->moveToThread(thread);
        }
    }

    void WorkerThread::submit(const job &job) {
        std::unique_lock<std::mutex> lock(mutex);
        jobs.emplace(job);
        cv.notify_one();
    }

    void WorkerThread::run() {
        job job;
        while (true) {
            {
                std::unique_lock<std::mutex> lock(mutex);
                while (!shutdown && jobs.empty())
                    cv.wait(lock);
                if (jobs.empty()) return;
                job = std::move(jobs.front());
                jobs.pop();
            }
            job();
        }
    }
}