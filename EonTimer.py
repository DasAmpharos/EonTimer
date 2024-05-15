#!/usr/bin/env python3
import os
import signal
import sys

from PySide6.QtGui import QIcon
from PySide6.QtWidgets import QApplication

from eon_timer import app, resources
from eon_timer.app_window import AppWindow
from eon_timer.util import loggers
from eon_timer.util.injector.app_context import AppContext
from eon_timer.util.injector.provider import InstanceProvider


def main() -> int:
    loggers.init()
    logger = loggers.get_logger('EonTimer')
    logger.info('app.version == %s', app.get_version())
    logger.info('sys.version == %s', sys.version)
    uname = os.uname()
    logger.info('os.sysname == %s', uname.sysname)
    logger.info('os.machine == %s', uname.machine)
    logger.info('os.release == %s', uname.release)
    logger.info('os.version == %s', uname.version)

    _app = QApplication(sys.argv)
    _app.setApplicationName('EonTimer')
    _app.setOrganizationName('DasAmpharos')
    _app.setOrganizationDomain('io.github.dasampharos')
    icon_filepath = resources.get_filepath('icon-512.png')
    _app.setWindowIcon(QIcon(icon_filepath))

    context = AppContext(base_packages=['eon_timer'],
                         provided={QApplication: InstanceProvider(_app)})
    app_window = context.get_component(AppWindow)
    app_window.show()
    return _app.exec()


if __name__ == "__main__":
    signal.signal(signal.SIGINT, signal.SIG_DFL)
    os.environ['PYGAME_HIDE_SUPPORT_PROMPT'] = 'hide'
    sys.exit(main())
