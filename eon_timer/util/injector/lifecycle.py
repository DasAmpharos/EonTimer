from abc import abstractmethod


class CloseListener:
    @abstractmethod
    def _on_close(self):
        ...
