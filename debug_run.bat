@echo off
echo ========================================
echo  TEST - Agenda Accesible
echo ========================================
echo.

cd /d "C:\Users\gokuc\OneDrive\Desktop\TP paradigmas AgendaAccesible"

echo Verificando clases...
if not exist "target\classes\app\Main.class" (
    echo ERROR: No existe Main.class
    pause
    exit /b 1
)

echo Clases encontradas. Ejecutando...
echo.

java -cp "target\classes;lib\h2-2.4.240.jar" app.Main

echo.
echo Codigo de salida: %ERRORLEVEL%
pause

