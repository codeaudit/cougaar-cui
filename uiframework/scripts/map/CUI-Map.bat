@ECHO OFF
echo Running Map UI...

set CONFIG_DIR=.\data

set LIBPATHS=.
REM set LIBPATHS=%LIBPATHS%;.\mysql.jar
set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\sys\mm-mysql-2.jar

set LIBPATHS=%LIBPATHS%;..\..\src
set LIBPATHS=%LIBPATHS%;..\..\src\org\cougaar\lib\uiframework\ui\components

set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\sys\xml4j_2_0_11.jar
set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\core.jar
set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\sys\cuimap.jar
set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\glm.jar
set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\tops.jar
set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\uiframework.jar

REM set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\classes

set LIBPATHS=%LIBPATHS%;%CONFIG_DIR%

set JAVAFLAGS=-mx64m -DScenarioMap.configDir=%CONFIG_DIR% -DDBTYPE=%DBTYPE% -DDBURL=%DBURL% -DDBUSER=%DBUSER% -DDBPASSWORD=%DBPASSWORD%

@ECHO ON

java %JAVAFLAGS% -Djava.compiler=NONE -classpath %LIBPATHS% org.cougaar.lib.uiframework.ui.map.app.ScenarioMap

echo Finished Running Map UI.
