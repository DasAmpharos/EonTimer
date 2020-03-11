cmake_minimum_required(VERSION 3.13)
project(EonTimer)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_PREFIX_PATH "/usr/local/opt/qt/lib/cmake")
set(FRUIT_INSTALLED_DIR "/usr/local/Cellar/fruit/3.4.0")
set(CMAKE_AUTOMOC ON)

find_package(Qt5Widgets REQUIRED)

add_executable(EonTimer main.cpp ui/Gen4TimerPane.cpp ui/Gen4TimerPane.h ui/ApplicationWindow.cpp ui/ApplicationWindow.h ui/TimerDisplayPane.cpp ui/TimerDisplayPane.h ui/Gen5TimerPane.cpp ui/Gen5TimerPane.h ui/Gen3TimerPane.cpp ui/Gen3TimerPane.h ui/CustomTimerPane.cpp ui/CustomTimerPane.h ui/util/FontHelper.h services/CalibrationService.h models/TimerState.h models/Console.h services/CalibrationService.cpp ui/ApplicationPane.cpp ui/ApplicationPane.h services/SettingsService.cpp services/SettingsService.h models/Console.cpp ui/util/FontHelper.cpp services/TimerService.cpp services/TimerService.h)
target_link_libraries(EonTimer Qt5::Widgets)