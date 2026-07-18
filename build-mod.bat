@echo off
setlocal
cd /d "%~dp0"
call gradlew.bat clean build
if errorlevel 1 (
    echo.
    echo Build failed.
    pause
    exit /b 1
)
echo.
echo Build completed. Check build\libs\
pause
