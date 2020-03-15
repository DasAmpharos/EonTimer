//
// Created by Dylan Meadows on 2020-03-14.
//

#include "TimerThread.h"

namespace service {
    TimerThread::TimerThread(QObject *parent)
        : QObject(parent),
          thread(QThread::create(&TimerThread::run, this)),
          shutdown(false) {
        thread->setParent(this);
        thread->start();
    }

    TimerThread::~TimerThread() {
        {
            std::unique_lock<std::mutex> lock(mutex);
            shutdown = true;
            conditional.notify_all();
        }
        thread->quit();
        thread->wait();
    }

    void TimerThread::submit(Job job) {
        std::unique_lock<std::mutex> lock(mutex);
        jobs.emplace(job);
        conditional.notify_one();
    }

    void TimerThread::run() {
        Job job;
        while (true) {
            {
                std::unique_lock<std::mutex> lock(mutex);
                while (!shutdown && jobs.empty())
                    conditional.wait(lock);
                if (jobs.empty()) return;

                job = std::move(jobs.front());
                jobs.pop();
            }
            job();
        }
    }
}
