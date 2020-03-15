//
// Created by Dylan Meadows on 2020-03-13.
//

#include "ThreadPool.h"

namespace util {
    ThreadPool::ThreadPool(const uint8_t threads)
        : shutdown(false) {
        this->threads.reserve(threads);
        for (uint8_t i = 0; i < threads; i++) {
            this->threads.emplace_back(std::bind(&ThreadPool::createThread, this));
        }
    }

    ThreadPool::~ThreadPool() {
        {
            std::unique_lock<std::mutex> lock(mutex);
            shutdown = true;
            conditional.notify_all();
        }
        for (auto &thread : threads) {
            thread.join();
        }
    }

    void ThreadPool::submit(const Job job) {
        std::unique_lock<std::mutex> lock(mutex);
        jobs.emplace(job);
        conditional.notify_one();
    }

    void ThreadPool::createThread() {
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