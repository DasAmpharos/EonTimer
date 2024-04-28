from abc import abstractmethod


class StartListener:
    @abstractmethod
    def _on_start(self):
        ...


class CloseListener:
    @abstractmethod
    def _on_close(self):
        ...
