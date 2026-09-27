@echo off
chcp 65001 >nul
setlocal EnableDelayedExpansion
cd /d "%~dp0"

echo ============================================
echo   No Boring Arrows - збірка під усі версії
echo ============================================
echo.

where java >nul 2>nul
if errorlevel 1 (
    echo [ПОМИЛКА] Java не знайдено. Встанови Java 25 і запусти ще раз.
    pause
    exit /b 1
)

if exist out rmdir /s /q out
mkdir out

set VERSIONS=
for %%F in (versions\*.properties) do set VERSIONS=!VERSIONS! %%~nF

set OK=
set FAILED=
for %%V in (%VERSIONS%) do (
    echo.
    echo ---- Minecraft %%V ----
    call gradlew.bat clean build -PmcVersion=%%V --no-daemon
    if errorlevel 1 (
        echo [ПОМИЛКА] Збірка під %%V не вдалася, дивись лог вище.
        set FAILED=!FAILED! %%V
    ) else (
        copy /y "build\libs\noboringarrows-*+%%V.jar" out\ >nul
        set OK=!OK! %%V
    )
)

echo.
echo ============================================
if defined OK echo Готово:!OK!
if defined FAILED echo Не вдалося:!FAILED!
echo Готові моди лежать у папці: %~dp0out
echo ============================================
pause
