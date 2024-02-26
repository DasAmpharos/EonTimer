#!/usr/bin/env python3

import atexit
import json
import sys
from typing import Callable

from PySide6.QtWidgets import QApplication

from eon_timer.app_config import AppConfig
from eon_timer.app_window import AppWindow


def main() -> int:
    app = QApplication([])
    app.setApplicationName('EonTimer')
    app.setOrganizationName('DasAmpharos')
    app.setOrganizationDomain('io.github.dasampharos')

    with open('config.json', 'r') as file:
        config = json.load(file)
        config = AppConfig(**config)
    atexit.register(on_exit(config))
    window = AppWindow(config)
    window.show()

    return app.exec()


def on_exit(config: AppConfig) -> Callable[[], None]:
    def implementation():
        with open('config.json', 'w') as file:
            file.write(config.model_dump_json())

    return implementation


if __name__ == "__main__":
    exit_code = main()
    sys.exit(exit_code)
