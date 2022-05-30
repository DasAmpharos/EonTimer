import sys

from PySide6 import QtWidgets

from gui.app_window import AppWindow

if __name__ == "__main__":
    app = QtWidgets.QApplication([])
    app.setApplicationName('EonTimer')
    app.setOrganizationName('DasAmpharos')
    app.setOrganizationDomain('io.github.dasampharos')

    window = AppWindow()
    window.show()

    sys.exit(app.exec())
