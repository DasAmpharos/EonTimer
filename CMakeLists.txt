cmake_minimum_required(VERSION 3.13)

project(EonTimer VERSION 3.0.0)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)

set(BASEPATH "${CMAKE_SOURCE_DIR}/src")
include_directories("${BASEPATH}")

configure_file(
    ${CMAKE_SOURCE_DIR}/src/app.h.in
    ${CMAKE_BINARY_DIR}/generated/app.h
)
include_directories(${CMAKE_BINARY_DIR}/generated)

if (UNIX AND NOT APPLE)
    message("-- Linux build detected")
    set(Qt5Widgets_DIR "/opt/qt514/lib/cmake/Qt5Widgets")
    list(APPEND CMAKE_MODULE_PATH "${CMAKE_MODULE_PATH};/usr/share/SFML/cmake/Modules")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -pthread")
endif()

if (WIN32)
    message("-- Windows build detected")
    set(SFML_DIR "./SFML-2.5.1/lib/cmake/SFML")
endif()

set(CMAKE_PREFIX_PATH "/usr/local/opt/qt/lib/cmake")
find_package(Qt5Widgets REQUIRED)
find_package(SFML 2.4 COMPONENTS audio REQUIRED)

set(SOURCES 
    resources/resources.qrc
    src/gui/ApplicationPane.cpp
    src/gui/ApplicationPane.h
    src/gui/ApplicationWindow.cpp
    src/gui/ApplicationWindow.h
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
    src/models/settings/ActionSettingsModel.cpp
    src/models/settings/ActionSettingsModel.h
    src/models/timers/Gen4TimerModel.cpp
    src/models/timers/Gen4TimerModel.h
    src/models/settings/TimerSettingsModel.cpp
    src/models/settings/TimerSettingsModel.h
    src/services/SoundService.cpp
    src/services/SoundService.h
    src/services/timers/DelayTimer.cpp
    src/services/timers/DelayTimer.h
    src/services/timers/SecondTimer.cpp
    src/services/timers/SecondTimer.h
    src/services/TimerService.cpp
    src/services/TimerService.h
    src/util/Functions.cpp
    src/util/Functions.h
    src/gui/timers/Gen5TimerPane.cpp
    src/gui/timers/Gen5TimerPane.h
    src/models/timers/Gen5TimerModel.cpp
    src/models/timers/Gen5TimerModel.h
    src/models/Gen5TimerMode.cpp
    src/models/Gen5TimerMode.h
    src/services/timers/EntralinkTimer.cpp
    src/services/timers/EntralinkTimer.h
    src/services/timers/FrameTimer.cpp
    src/services/timers/FrameTimer.h
    src/services/timers/EnhancedEntralinkTimer.cpp
    src/services/timers/EnhancedEntralinkTimer.h
    src/gui/util/FieldSet.h
    src/models/timers/Gen3TimerModel.cpp
    src/models/timers/Gen3TimerModel.h
    src/gui/timers/Gen3TimerPane.cpp
    src/gui/timers/Gen3TimerPane.h
    src/models/timers/CustomTimerModel.cpp
    src/models/timers/CustomTimerModel.h
    src/gui/timers/CustomTimerPane.cpp
    src/gui/timers/CustomTimerPane.h
    src/app.h.in
    src/main.cpp
)

if (APPLE)
    add_executable(${PROJECT_NAME} MACOSX_BUNDLE ${SOURCES})
    set_target_properties(${PROJECT_NAME} PROPERTIES MACOSX_BUNDLE_INFO_PLIST ${CMAKE_SOURCE_DIR}/resources/macos/Info.plist)
else()
    add_executable(${CMAKE_PROJECT_NAME} ${SOURCES})
endif()

target_link_libraries(EonTimer Qt5::Widgets sfml-audio)