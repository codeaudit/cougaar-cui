@ECHO OFF

set CLASSPATH=

set LIBPATHS=.
set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\core.jar
set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\glm.jar

rem set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\uiframework.jar
set LIBPATHS=%LIBPATHS%;.\bin
set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\classes


@ECHO ON

java -Djava.compiler=NONE -classpath %LIBPATHS% org.cougaar.lib.uiframework.ui.inventory.InventoryChartUI l 4
REM java -classpath %LIBPATHS% org.cougaar.lib.uiframework.ui.inventory.InventoryChartUI l 4
