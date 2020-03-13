cmake_minimum_required(VERSION 3.13)

project(EonTimer)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_AUTOMOC ON)

set(BASEPATH "${CMAKE_SOURCE_DIR}")
set(CMAKE_PREFIX_PATH "/usr/local/opt/qt/lib/cmake")
find_package(Qt5Widgets REQUIRED)
include_directories("${BASEPATH}")

add_executable(EonTimer main.cpp gui/Gen4TimerPane.cpp gui/Gen4TimerPane.h gui/ApplicationWindow.cpp gui/ApplicationWindow.h gui/TimerDisplayPane.cpp gui/TimerDisplayPane.h gui/util/FontHelper.h services/CalibrationService.h models/TimerState.h models/Console.h services/CalibrationService.cpp gui/ApplicationPane.cpp gui/ApplicationPane.h services/SettingsService.cpp services/SettingsService.h models/Console.cpp gui/util/FontHelper.cpp services/TimerService.cpp services/TimerService.h models/TimerState.cpp services/timer/SecondTimer.cpp services/timer/SecondTimer.h util/Functions.h util/Functions.cpp services/timer/DelayTimer.h services/timer/DelayTimer.cpp)
target_link_libraries(EonTimer Qt5::Widgets)