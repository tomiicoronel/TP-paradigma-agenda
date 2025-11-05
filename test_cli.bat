@echo off
echo ========================================
echo TEST: Ejecutando Agenda Accesible
echo ========================================
echo.
echo Si ves el menu, presiona 0 para salir
echo.

echo 0 | java -cp "target\classes;lib\h2-2.4.240.jar" app.Main

echo.
echo ========================================
echo Test completado
echo ========================================
pause

