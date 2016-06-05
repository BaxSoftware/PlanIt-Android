@echo off
echo ADB over WIFI
C:
cd C:\Users\Farzad\AppData\Local\Android\Sdk\platform-tools
REM adb tcpip 5555
adb connect 192.168.1.70:5555
REM adb connect 192.168.1.81:5555
pause