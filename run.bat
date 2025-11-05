@echo off
echo ========================================
echo   Agenda Accesible - Aplicacion
echo ========================================
echo.
echo Compilando proyecto...
javac -d target/classes -cp "lib/h2-2.4.240.jar" -sourcepath src src/app/Main.java

if %errorlevel% neq 0 (
    echo.
    echo ERROR: No se pudo compilar el proyecto
    pause
    exit /b 1
)

echo.
echo Compilacion exitosa. Iniciando aplicacion...
echo.
java -cp "target/classes;lib/h2-2.4.240.jar" app.Main

pause

