@echo off
REM ============================================
REM Ejecutar aplicacion CLI (compilacion manual)
REM ============================================

echo.
echo === Agenda Accesible - Iniciando CLI ===
echo.

REM Verificar que este compilado
if not exist "bin\app\Main.class" (
    echo [ERROR] No se encontraron archivos compilados.
    echo Por favor ejecuta: compile_simple.bat
    echo.
    pause
    exit /b 1
)

REM Ejecutar aplicacion en modo CLI
java -cp "bin;lib\h2-2.4.240.jar" app.Main --cli

echo.
echo === Aplicacion finalizada ===
pause
@echo off
REM ============================================
REM Script de compilacion manual (sin Maven)
REM ============================================

echo.
echo === Agenda Accesible - Compilacion Manual ===
echo.

REM Crear directorio de salida
if not exist "bin" mkdir bin

echo [1/3] Limpiando directorio bin...
del /Q bin\*.class 2>nul
for /d %%p in (bin\*) do rmdir "%%p" /s /q 2>nul

echo [2/3] Compilando codigo fuente...
javac -encoding UTF-8 -d bin -cp "lib\h2-2.4.240.jar" ^
    src\app\*.java ^
    src\controller\*.java ^
    src\domain\*.java ^
    src\infra\dao\*.java ^
    src\infra\dao\impl\*.java ^
    src\infra\db\*.java ^
    src\service\*.java ^
    src\shared\observer\*.java ^
    src\ui\*.java ^
    src\ui\forms\*.java ^
    src\ui\menu\*.java ^
    src\ui\panels\*.java ^
    src\ui\utils\*.java ^
    src\test\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilacion fallida. Revisa los errores arriba.
    pause
    exit /b 1
)

echo [3/3] Compilacion exitosa!
echo.
echo === Archivos compilados en: bin\ ===
echo.
echo Para ejecutar la aplicacion:
echo   - GUI: run_simple.bat
echo   - CLI: run_simple_cli.bat
echo.
pause

