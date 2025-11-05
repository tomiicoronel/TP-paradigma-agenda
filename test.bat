@echo off
echo ========================================
echo   Ejecutando Tests
echo ========================================
echo.

echo Test 1: Conexion y CRUD basico
echo ----------------------------------------
java -cp "target/classes;lib/h2-2.4.240.jar" test.TestConexionSimple
echo.

echo.
echo Test 2: DAOs completos
echo ----------------------------------------
java -cp "target/classes;lib/h2-2.4.240.jar" test.TestDAOs
echo.

echo ========================================
echo   Tests completados
echo ========================================
pause
@echo off
echo ========================================
echo   Compilando proyecto completo...
echo ========================================
echo.

REM Limpiar compilaciones anteriores
if exist "target\classes" (
    echo Limpiando target/classes...
    rmdir /s /q "target\classes"
    mkdir "target\classes"
)

echo.
echo Compilando todos los archivos fuente...
javac -d target/classes -cp "lib/h2-2.4.240.jar" -sourcepath src src/app/Main.java src/test/TestDAOs.java src/test/TestConexionSimple.java

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Fallos en la compilacion
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Compilacion EXITOSA
echo ========================================
echo.
echo Para ejecutar la aplicacion: run.bat
echo Para ejecutar tests: test.bat
echo.
pause

