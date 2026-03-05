import time


class Clock:
    def __init__(self):
        self.__initial_tick = time.perf_counter_ns()

    def since_start(self) -> float:
        return (time.perf_counter_ns() - self.__initial_tick) / 1_000_000
