#!/usr/bin/env python3

import sys

import pkg_resources
from PySide6.QtQml import QQmlApplicationEngine
from PySide6.QtWidgets import QApplication


def main() -> int:
    app = QApplication([])
    app.setApplicationName('EonTimer')
    app.setOrganizationName('DasAmpharos')
    app.setOrganizationDomain('io.github.dasampharos')

    path = pkg_resources.resource_filename('eon_timer.res', 'main.qml')
    engine = QQmlApplicationEngine()
    engine.quit.connect(app.quit)
    engine.load(path)
    return app.exec()


if __name__ == "__main__":
    exit_code = main()
    sys.exit(exit_code)
