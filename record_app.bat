@echo off
setlocal

:: Set colors
set "GREEN=[32m"
set "YELLOW=[33m"
set "CYAN=[36m"
set "RESET=[0m"

:: Get current date and time for filename
for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /value') do set datetime=%%I
set "FILENAME=app_recording_%datetime:~0,4%-%datetime:~4,2%-%datetime:~6,2%_%datetime:~8,2%-%datetime:~10,2%-%datetime:~12,2%.mp4"

echo %GREEN%Android App Recording Script%RESET%
echo %GREEN%=========================%RESET%
echo.
echo %CYAN%This script will record your app screen.%RESET%
echo %CYAN%Press Ctrl+C when you want to stop recording.%RESET%
echo.
echo %YELLOW%Starting recording...%RESET%

:: Start recording
adb shell screenrecord /sdcard/%FILENAME%

:: After Ctrl+C is pressed and recording stops
echo.
echo %YELLOW%Recording stopped. Transferring file from device...%RESET%

:: Pull the recording from the device
adb pull /sdcard/%FILENAME% .

:: Delete the file from the device
adb shell rm /sdcard/%FILENAME%

:: Check if file exists
if exist %FILENAME% (
    echo.
    echo %GREEN%Recording saved as: %FILENAME%%RESET%
    echo %GREEN%Location: %CD%\%FILENAME%%RESET%
) else (
    echo.
    echo %RED%Error: Recording file not found%RESET%
)

pause 