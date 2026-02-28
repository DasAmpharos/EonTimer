from PySide6.QtCore import Qt
from PySide6.QtWidgets import (
    QDialog,
    QDialogButtonBox,
    QLabel,
    QPushButton,
    QStackedWidget,
    QVBoxLayout,
    QWidget,
)

_STEPS = [
    (
        'Welcome to EonTimer',
        (
            'EonTimer is a precision timer for Pokémon RNG manipulation.\n\n'
            'Step 1 — Select your generation using the tabs at the top:\n'
            '  • Gen 5  — Black / White / Black 2 / White 2\n'
            '  • Gen 4  — Diamond / Pearl / Platinum / HG / SS\n'
            '  • Gen 3  — Ruby / Sapphire / Emerald / FR / LG\n'
            '  • Custom — Define your own phase sequence'
        ),
    ),
    (
        'Enter Targets and Start',
        (
            'Step 2 — Fill in the target values for your desired seed or frame,\n'
            'then press Start (or Space) to begin the timer.\n\n'
            'The timer counts down each phase and plays an audio/visual cue\n'
            'when it is time to press A.'
        ),
    ),
    (
        'Calibrate with Hit Values',
        (
            'Step 3 — After each run, enter the delay or frame you actually hit\n'
            'in the hit field, then press Update (or F6) to apply calibration.\n\n'
            'EonTimer will adjust your targets so future runs land closer to\n'
            'the desired result.  Keep repeating until your hits are consistent.'
        ),
    ),
]


class WelcomeDialog(QDialog):
    def __init__(self, parent=None):
        super().__init__(parent)
        self.setWindowTitle('Welcome to EonTimer')
        self.setMinimumWidth(480)
        self.__step = 0

        layout = QVBoxLayout(self)
        layout.setSpacing(12)

        # title label
        self.__title_lbl = QLabel()
        self.__title_lbl.setAlignment(Qt.AlignmentFlag.AlignCenter)
        font = self.__title_lbl.font()
        font.setPointSize(14)
        font.setBold(True)
        self.__title_lbl.setFont(font)
        layout.addWidget(self.__title_lbl)

        # stacked pages
        self.__stack = QStackedWidget()
        for _, body in _STEPS:
            page = QWidget()
            page_layout = QVBoxLayout(page)
            body_lbl = QLabel(body)
            body_lbl.setWordWrap(True)
            body_lbl.setAlignment(Qt.AlignmentFlag.AlignTop | Qt.AlignmentFlag.AlignLeft)
            page_layout.addWidget(body_lbl)
            self.__stack.addWidget(page)
        layout.addWidget(self.__stack)

        # buttons
        self.__btn_box = QDialogButtonBox()
        self.__back_btn = self.__btn_box.addButton('Back', QDialogButtonBox.ButtonRole.ActionRole)
        self.__next_btn = self.__btn_box.addButton('Next', QDialogButtonBox.ButtonRole.ActionRole)
        self.__back_btn.clicked.connect(self.__prev_step)
        self.__next_btn.clicked.connect(self.__next_step)
        layout.addWidget(self.__btn_box)

        self.__refresh()

    def __refresh(self):
        title, _ = _STEPS[self.__step]
        self.__title_lbl.setText(title)
        self.__stack.setCurrentIndex(self.__step)
        self.__back_btn.setVisible(self.__step > 0)
        is_last = self.__step == len(_STEPS) - 1
        self.__next_btn.setText('Get Started' if is_last else 'Next')

    def __next_step(self):
        if self.__step < len(_STEPS) - 1:
            self.__step += 1
            self.__refresh()
        else:
            self.accept()

    def __prev_step(self):
        if self.__step > 0:
            self.__step -= 1
            self.__refresh()
