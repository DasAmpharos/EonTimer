lessThan(QT_MAJOR_VERSION, 5):error("You need at least Qt 5.9 to build EonTimer")
equals(QT_MAJOR_VERSION, 5):lessThan(QT_MINOR_VERSION, 9):error("You need at least Qt 5.9 to build EonTimer")

QT += widgets

TARGET = EonTimer
TEMPLATE = app
VERSION = 3.0.0
QMAKE_TARGET_DESCRIPTION = EonTimer
QMAKE_TARGET_COPYRIGHT = dylmeadows

DEFINES += APP_NAME=\\\"$$TARGET\\\"
DEFINES += VERSION=\\\"$$VERSION\\\"

CONFIG += c++1z
CONFIG += conan_basic_setup
include(conanbuildinfo.pri)
INCLUDEPATH += src

RESOURCES += \
    resources/resources.qrc

HEADERS += \
    src/gui/dialogs/SettingsDialog.h \
    src/gui/ApplicationPane.h \
    src/gui/settings/ActionSettingsPane.h \
    src/gui/settings/TimerSettingsPane.h \
    src/gui/timers/Gen3TimerPane.h \
    src/gui/timers/Gen4TimerPane.h \
    src/gui/timers/Gen5TimerPane.h \
    src/gui/util/FieldSet.h \
    src/gui/util/FontHelper.h \
    src/gui/ApplicationPane.h \
    src/gui/ApplicationWindow.h \
    src/gui/TimerDisplayPane.h \
    src/models/settings/ActionSettingsModel.h \
    src/models/settings/TimerSettingsModel.h \
    src/models/timers/Gen3TimerModel.h \
    src/models/timers/Gen4TimerModel.h \
    src/models/timers/Gen5TimerModel.h \
    src/models/Console.h \
    src/models/Gen5TimerMode.h \
    src/models/Sound.h \
    src/models/TimerState.h \
    src/services/timers/DelayTimer.h \
    src/services/timers/EnhancedEntralinkTimer.h \
    src/services/timers/EntralinkTimer.h \
    src/services/timers/FrameTimer.h \
    src/services/timers/SecondTimer.h \
    src/services/CalibrationService.h \
    src/services/SoundService.h \
    src/services/TimerService.h \
    src/util/Functions.h

SOURCES += \
    src/gui/dialogs/SettingsDialog.cpp \
    src/gui/ApplicationPane.cpp \
    src/gui/settings/ActionSettingsPane.cpp \
    src/gui/settings/TimerSettingsPane.cpp \
    src/gui/timers/Gen3TimerPane.cpp \
    src/gui/timers/Gen4TimerPane.cpp \
    src/gui/timers/Gen5TimerPane.cpp \
    src/gui/util/FontHelper.cpp \
    src/gui/ApplicationPane.cpp \
    src/gui/ApplicationWindow.cpp \
    src/gui/TimerDisplayPane.cpp \
    src/models/settings/ActionSettingsModel.cpp \
    src/models/settings/TimerSettingsModel.cpp \
    src/models/timers/Gen3TimerModel.cpp \
    src/models/timers/Gen4TimerModel.cpp \
    src/models/timers/Gen5TimerModel.cpp \
    src/models/Console.cpp \
    src/models/Gen5TimerMode.cpp \
    src/models/Sound.cpp \
    src/models/TimerState.cpp \
    src/services/timers/DelayTimer.cpp \
    src/services/timers/EnhancedEntralinkTimer.cpp \
    src/services/timers/EntralinkTimer.cpp \
    src/services/timers/FrameTimer.cpp \
    src/services/timers/SecondTimer.cpp \
    src/services/CalibrationService.cpp \
    src/services/SoundService.cpp \
    src/services/TimerService.cpp \
    src/util/Functions.cpp \
    src/main.cpp