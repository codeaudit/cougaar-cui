@ECHO OFF

set CLASSPATH=

set LIBPATHS=.
REM set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\core.jar
REM set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\glm.jar

set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\uiframework.jar
set LIBPATHS=%LIBPATHS%;.\bin
rem set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\classes


@ECHO ON

java -Djava.compiler=NONE -classpath %LIBPATHS% org.cougaar.lib.uiframework.ui.components.CChart
pause
