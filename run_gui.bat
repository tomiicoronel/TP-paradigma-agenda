@echo off
echo === Compilando proyecto ===
call mvn clean compile

if %ERRORLEVEL% NEQ 0 (
    echo Error en compilacion
    pause
    exit /b 1
)

echo.
echo === Iniciando GUI ===
call mvn exec:java -Dexec.mainClass="app.Main"
pause

