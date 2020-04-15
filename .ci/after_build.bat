md EonTimer
move EonTimer.exe EonTimer\EonTimer.exe
windeployqt --release --no-translations --no-angle --no-plugins --no-opengl-sw EonTimer\EonTimer.exe
xcopy /I %QT_PATH%\plugins\platforms\qwindows.dll EonTimer\platforms\
xcopy /I %QT_PATH%\plugins\styles\qwindowsvistastyle.dll EonTimer\styles\
xcopy /I SFML-2.5.1\bin\openal32.dll EonTimer\
xcopy /I SFML-2.5.1\bin\sfml-audio-2.dll EonTimer\
xcopy /I SFML-2.5.1\bin\sfml-system-2.dll EonTimer\
7z a EonTimer-%OS%.zip EonTimer\
sha256sum EonTimer-%OS%.zip > EonTimer-%OS%.zip.sha256
