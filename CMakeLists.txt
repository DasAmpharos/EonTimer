cmake_minimum_required(VERSION 3.13)

project(EonTimer)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)

set(BASEPATH "${CMAKE_SOURCE_DIR}/src")
include_directories("${BASEPATH}")

set(CMAKE_PREFIX_PATH "/usr/local/opt/qt/lib/cmake")
find_package(Qt5Widgets REQUIRED)
find_package(Qt5Multimedia REQUIRED)

add_executable(EonTimer resources/resources.qrc src/main.cpp src/gui/timers/Gen4TimerPane.cpp src/gui/timers/Gen4TimerPane.h src/gui/ApplicationWindow.cpp src/gui/ApplicationWindow.h src/gui/TimerDisplayPane.cpp src/gui/TimerDisplayPane.h src/gui/util/FontHelper.h src/services/CalibrationService.h src/models/TimerState.h src/models/Console.h src/services/CalibrationService.cpp src/gui/ApplicationPane.cpp src/gui/ApplicationPane.h src/models/Console.cpp src/gui/util/FontHelper.cpp src/services/TimerService.cpp src/services/TimerService.h src/models/TimerState.cpp src/services/timers/SecondTimer.cpp src/services/timers/SecondTimer.h src/util/Functions.h src/util/Functions.cpp src/services/timers/DelayTimer.h src/services/timers/DelayTimer.cpp src/models/Sound.h src/services/SoundService.cpp src/services/SoundService.h src/services/TimerThread.cpp src/services/TimerThread.h src/services/settings/ActionSettings.cpp src/services/settings/ActionSettings.h src/services/settings/TimerSettings.cpp src/services/settings/TimerSettings.h src/gui/dialogs/SettingsDialog.cpp src/gui/dialogs/SettingsDialog.h src/gui/dialogs/AboutDialog.cpp src/gui/dialogs/AboutDialog.h src/gui/settings/ActionSettingsPane.cpp src/gui/settings/ActionSettingsPane.h src/gui/settings/TimerSettingsPane.cpp src/gui/settings/TimerSettingsPane.h)
target_link_libraries(EonTimer Qt5::Widgets Qt5::Multimedia)

SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++17 -pthread")