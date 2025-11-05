@echo off
echo Ejecutando test...
java -cp target/classes;lib/h2-2.4.240.jar test.TestConexionSimple
pause
