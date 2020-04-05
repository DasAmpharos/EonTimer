if "%OS%"=="win64" (goto build64Bit)
if "%OS%"=="win32" (goto build32Bit)
echo -- Invalid platform: %OS%
goto :eof

:build64Bit
echo -- :build64Bit
set QT_PATH=C:\Qt\5.14\msvc2017_64
set PATH=%QT_PATH%\bin;C:\Qt\Tools\QtCreator\bin;%PATH%

call C:\"Program Files (x86)\Microsoft Visual Studio"\2019\Community\VC\Auxiliary\Build\vcvarsall.bat x64
cmake -G "NMake Makefiles JOM" .
jom -j %NUMBER_OF_PROCESSORS%
exit

:build32Bit
echo -- :build32Bit
set QT_PATH=C:\Qt\5.14\msvc2017
set PATH=%QT_PATH%\bin;C:\Qt\Tools\QtCreator\bin;%PATH%

call C:\"Program Files (x86)\Microsoft Visual Studio"\2019\Community\VC\Auxiliary\Build\vcvarsall.bat x86
cmake -G "NMake Makefiles JOM" .
jom -j %NUMBER_OF_PROCESSORS%
exit