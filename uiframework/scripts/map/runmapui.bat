rem @echo off
echo Running Map UI...

setlocal

set BASE=..\..
set CUR_BIN_DIR=%BASE%\classes
set LIB_HOME=..\..\lib

set LIB_PATH=%CUR_BIN_DIR%
set LIB_PATH=%LIB_PATH%;%LIB_HOME%\xml4j_2_0_11.jar
set LIB_PATH=%LIB_PATH%;%LIB_HOME%\core.jar
set LIB_PATH=%LIB_PATH%;%LIB_HOME%\openmap.jar
set LIB_PATH=%LIB_PATH%;%LIB_HOME%\classes122.zip
set LIB_PATH=%LIB_PATH%;.\data



set DBTYPE="oracle"
rem set DBTYPE="access"

rem set DBURL="quicktableDemo"
set DBURL="alp-demo:1521:alp"
rem set DBURL="eiger.alpine.bbn.com:1521:alp"

set DBUSER="pfischer"
rem set DBUSER="jmeyer"
rem set DBUSER="blackjack8"

set DBPASSWORD="pfischer"
rem set DBPASSWORD="jmeyer"
rem set DBPASSWORD="init1389"

set JAVAC=c:\jdk1.2.2\bin\java
set JAVAFLAGS=  -mx64m  %Debugging% -Dcmap.configDir=.\data  -DDBTYPE=%DBTYPE% -DDBURL=%DBURL% -DDBUSER=%DBUSER% -DDBPASSWORD=%DBPASSWORD% -classpath %LIB_PATH%

%JAVAC% %JAVAFLAGS% org.cougaar.lib.uiframework.ui.map.app.CMap

echo Finished Running Map UI.
echo on