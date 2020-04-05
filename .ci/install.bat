if "%OS%"=="win64" (goto install64Bit)
if "%OS%"=="win32" (goto install32Bit)
echo -- Invalid platform: %OS%
goto :eof

:install64Bit
echo -- :install64Bit
set SFML_URL=https://www.sfml-dev.org/files/SFML-2.5.1-windows-vc15-64-bit.zip
appveyor DownloadFile "%SFML_URL%" -FileName "SFML-2.5.1-%OS%.zip"
7z x SFML-2.5.1-%OS%.zip
exit

:install32Bit
echo -- :install32Bit
set SFML_URL=https://www.sfml-dev.org/files/SFML-2.5.1-windows-vc15-32-bit.zip
appveyor DownloadFile "%SFML_URL%" -FileName "SFML-2.5.1-%OS%.zip"
7z x SFML-2.5.1-%OS%.zip
exit
