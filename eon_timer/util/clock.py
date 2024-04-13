import time


class Clock:
    def __init__(self):
        now = time.perf_counter_ns()
        self.__initial_tick = now
        self.__last_tick = now

    def tick(self) -> float:
        now = time.perf_counter_ns()
        delta = now - self.__last_tick
        self.__last_tick = now
        return float(delta) / 1_000_000

    def since_start(self) -> float:
        return (time.perf_counter_ns() - self.__initial_tick) / 1_000_000
