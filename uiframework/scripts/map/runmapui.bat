@echo off
echo Running Map UI...

setlocal

set BASE=..\..
set CUR_BIN_DIR=%BASE%\classes
set LIB_HOME=..\..\lib

set LIB_PATH=%CUR_BIN_DIR%
set LIB_PATH=%LIB_PATH%;%LIB_HOME%\xml4j_2_0_11.jar
set LIB_PATH=%LIB_PATH%;%LIB_HOME%\core.jar
set LIB_PATH=%LIB_PATH%;%LIB_HOME%\openmap.jar
set LIB_PATH=%LIB_PATH%;.\data

set JAVAC=d:\jdk1.2.2\bin\java
set JAVAFLAGS=  -mx64m  %Debugging% -Dopenmap.configDir=.\data -classpath %LIB_PATH%

%JAVAC% %JAVAFLAGS% org.cougaar.lib.uiframework.ui.map.app.OpenMap

echo Finished Running Map UI.
echo on