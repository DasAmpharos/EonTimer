#!/usr/bin/env python3

import sys

from PySide6.QtWidgets import QApplication

from eon_timer.gui.app_window import AppWindow


def main() -> int:
    app = QApplication([])
    app.setApplicationName('EonTimer')
    app.setOrganizationName('DasAmpharos')
    app.setOrganizationDomain('io.github.dasampharos')

    window = AppWindow()
    window.show()

    return app.exec()


if __name__ == "__main__":
    exit_code = main()
    sys.exit(exit_code)
