@echo off
REM ============================================
REM Ejecutar aplicacion GUI (compilacion manual)
REM ============================================

echo.
echo === Agenda Accesible - Iniciando GUI ===
echo.

REM Verificar que este compilado
if not exist "bin\app\Main.class" (
    echo [ERROR] No se encontraron archivos compilados.
    echo Por favor ejecuta: compile_simple.bat
    echo.
    pause
    exit /b 1
)

REM Ejecutar aplicacion
java -cp "bin;lib\h2-2.4.240.jar" app.Main

echo.
echo === Aplicacion finalizada ===
pause

