cmake_minimum_required(VERSION 3.13)

project(EonTimer VERSION 3.0.0)
set(BASEPATH "${CMAKE_SOURCE_DIR}/src")
SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++17 -pthread")
include_directories("${BASEPATH}")
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)

if (EXISTS "${CMAKE_SOURCE_DIR}/.git")
    execute_process(
        COMMAND git rev-parse --abbrev-ref HEAD
        WORKING_DIRECTORY ${CMAKE_SOURCE_DIR}
        OUTPUT_VARIABLE GIT_BRANCH
        OUTPUT_STRIP_TRAILING_WHITESPACE
    )
    execute_process(
        COMMAND git log -1 --format=%h
        WORKING_DIRECTORY ${CMAKE_SOURCE_DIR}
        OUTPUT_VARIABLE GIT_COMMIT_HASH
        OUTPUT_STRIP_TRAILING_WHITESPACE
    )
else (EXISTS "${CMAKE_SOURCE_DIR}/.git")
    set(GIT_BRANCH "")
    set(GIT_COMMIT_HASH "")
endif (EXISTS "${CMAKE_SOURCE_DIR}/.git")

message(STATUS "Git current branch: ${GIT_BRANCH}")
message(STATUS "Git commit hash: ${GIT_COMMIT_HASH}")
message(STATUS "Generating version.h")

configure_file(
    ${CMAKE_SOURCE_DIR}/src/app.h.in
    ${CMAKE_BINARY_DIR}/generated/app.h
)
include_directories(${CMAKE_BINARY_DIR}/generated)

set(CMAKE_PREFIX_PATH "/usr/local/opt/qt/lib/cmake")
find_package(Qt5Widgets REQUIRED)
find_package(SFML 2.5 COMPONENTS audio REQUIRED)

add_executable(${CMAKE_PROJECT_NAME}
    resources/resources.qrc
    src/app.h.in
    src/main.cpp
    src/gui/ApplicationPane.cpp
    src/gui/ApplicationPane.h
    src/gui/ApplicationWindow.cpp
    src/gui/ApplicationWindow.h
    src/gui/dialogs/AboutDialog.cpp
    src/gui/dialogs/AboutDialog.h
    src/gui/dialogs/SettingsDialog.cpp
    src/gui/dialogs/SettingsDialog.h
    src/gui/settings/ActionSettingsPane.cpp
    src/gui/settings/ActionSettingsPane.h
    src/gui/settings/TimerSettingsPane.cpp
    src/gui/settings/TimerSettingsPane.h
    src/gui/TimerDisplayPane.cpp
    src/gui/TimerDisplayPane.h
    src/gui/timers/Gen4TimerPane.cpp
    src/gui/timers/Gen4TimerPane.h
    src/gui/util/FontHelper.cpp
    src/gui/util/FontHelper.h
    src/models/Console.cpp
    src/models/Console.h
    src/models/Sound.cpp
    src/models/Sound.h
    src/models/TimerState.cpp
    src/models/TimerState.h
    src/services/CalibrationService.cpp
    src/services/CalibrationService.h
    src/services/settings/ActionSettings.cpp
    src/services/settings/ActionSettings.h
    src/services/settings/Gen4TimerSettings.cpp
    src/services/settings/Gen4TimerSettings.h
    src/services/settings/TimerSettings.cpp
    src/services/settings/TimerSettings.h
    src/services/SoundService.cpp
    src/services/SoundService.h
    src/services/timers/DelayTimer.cpp
    src/services/timers/DelayTimer.h
    src/services/timers/SecondTimer.cpp
    src/services/timers/SecondTimer.h
    src/services/TimerService.cpp
    src/services/TimerService.h
    src/util/Functions.cpp
    src/util/Functions.h src/gui/timers/Gen5TimerPane.cpp src/gui/timers/Gen5TimerPane.h src/services/settings/Gen5TimerSettings.cpp src/services/settings/Gen5TimerSettings.h src/models/Gen5TimerMode.cpp src/models/Gen5TimerMode.h src/services/timers/EntralinkTimer.cpp src/services/timers/EntralinkTimer.h src/services/timers/FrameTimer.cpp src/services/timers/FrameTimer.h)
target_link_libraries(EonTimer Qt5::Widgets sfml-audio)