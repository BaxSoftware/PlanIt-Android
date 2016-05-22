@echo off

set source=.\Final
set androiddest=..\app\src\main\res\drawable

echo.
echo Copying assets to Android directory
echo -----------------------------------
xcopy %source% %androiddest% /s /y
echo ===================================
echo.
echo All Android assets have been copied
echo ===================================
echo.
pause
