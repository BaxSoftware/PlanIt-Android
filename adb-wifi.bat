@echo off
echo ADB over WIFI
C:
cd C:\Users\Farzad\AppData\Local\Android\Sdk\platform-tools
adb tcpip 5555
adb connect 192.168.1.65:5555
pause