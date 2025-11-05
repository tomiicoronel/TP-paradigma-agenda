@echo off
echo ========================================
echo Compilando Agenda Accesible
echo ========================================

if not exist "target\classes" mkdir "target\classes"

echo.
echo [1/2] Compilando clases...
javac -d target\classes ^
  -cp "lib\h2-2.4.240.jar" ^
  -sourcepath src ^
  src\app\Main.java ^
  src\domain\*.java ^
  src\infra\db\*.java ^
  src\infra\dao\*.java ^
  src\infra\dao\impl\*.java ^
  src\shared\observer\*.java ^
  src\ui\*.java ^
  src\ui\menu\*.java ^
  src\ui\utils\*.java

if errorlevel 1 (
    echo.
    echo ❌ Error de compilacion
    pause
    exit /b 1
)

echo.
echo ✓ Compilacion exitosa
echo.
pause

