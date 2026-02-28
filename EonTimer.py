#!/usr/bin/env python3
import platform
import signal
import sys

from PySide6.QtGui import QIcon
from PySide6.QtWidgets import QApplication

from eon_timer import app, container, resources
from eon_timer.util import loggers


def main() -> int:
    loggers.init()
    logger = loggers.get_logger('EonTimer')
    logger.info(f'app.version             == {app.get_version()}')
    logger.info(f'platform.system         == {platform.system()}')
    logger.info(f'platform.release        == {platform.release()}')
    logger.info(f'platform.version        == {platform.version()}')
    logger.info(f'platform.machine        == {platform.machine()}')
    logger.info(f'platform.processor      == {platform.processor()}')
    logger.info(f'platform.python_version == {platform.python_version()}')

    _app = QApplication(sys.argv)
    _app.setApplicationName('EonTimer')
    _app.setOrganizationName('DasAmpharos')
    _app.setOrganizationDomain('io.github.dasampharos')
    icon_filepath = resources.get_filepath('icon-512.png')
    _app.setWindowIcon(QIcon(icon_filepath))

    app_window = container.build(_app)
    app_window.show()
    return _app.exec()


if __name__ == "__main__":
    signal.signal(signal.SIGINT, signal.SIG_DFL)
    sys.exit(main())
