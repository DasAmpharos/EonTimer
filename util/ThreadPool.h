//
// Created by Dylan Meadows on 2020-03-13.
//

#ifndef EONTIMER_THREADPOOL_H
#define EONTIMER_THREADPOOL_H

#include <cstddef>
#include <functional>
#include <mutex>
#include <thread>
#include <vector>
#include <queue>

using Job = std::function<void(void)>;

namespace util {
    class ThreadPool {
    private:
        std::mutex mutex;
        std::condition_variable conditional;
        std::vector<std::thread> threads;
        std::queue<Job> jobs;
        bool shutdown;
    public:
        explicit ThreadPool(uint8_t threads);

        ~ThreadPool();

        void submit(Job job);

    private:
        void createThread();
    };
}


#endif //EONTIMER_THREADPOOL_H
