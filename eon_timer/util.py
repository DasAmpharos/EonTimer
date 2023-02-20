from enum import Enum
import os


class StrEnum(str, Enum):
    def __str__(self) -> str:
<<<<<<< Updated upstream
        return str(self.value)
=======
        return self.value

def get_module_name(file: str) -> str:
    return os.path.basename(os.path.dirname(file))
>>>>>>> Stashed changes
