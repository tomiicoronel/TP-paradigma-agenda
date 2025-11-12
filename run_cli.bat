@echo off
echo ========================================
echo  Agenda Accesible - Modo CLI
echo ========================================
echo.

REM Verificar que exista la carpeta de clases compiladas
if not exist "target\classes\app\Main.class" (
    echo ERROR: No se encuentran las clases compiladas.
    echo Por favor ejecuta primero: compile.bat
    pause
    exit /b 1
)

echo Iniciando aplicacion en modo CLI...
echo.

java -cp "target\classes;lib\h2-2.4.240.jar" app.Main --cli

echo.
echo Aplicacion cerrada.
pause
@echo off
echo ========================================
echo  Agenda Accesible - Interfaz Grafica
echo ========================================
echo.

REM Verificar que exista la carpeta de clases compiladas
if not exist "target\classes\app\Main.class" (
    echo ERROR: No se encuentran las clases compiladas.
    echo Por favor ejecuta primero: compile.bat
    pause
    exit /b 1
)

echo Iniciando aplicacion con interfaz grafica...
echo.

java -cp "target\classes;lib\h2-2.4.240.jar" app.Main

echo.
echo Aplicacion cerrada.
pause

