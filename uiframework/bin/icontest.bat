rem This will launch the OpenMap application
rem YOU MUST EDIT THE SETTINGS IN THIS FILE TO MATCH YOUR CONFIGURATION

rem Java Virtual Machine
rem CHANGE THIS TO MATCH YOUR CONFIGURATION
rem set JAVABIN=F:\jdk1.2.2\bin\java.exe
set JAVABIN=java 

rem OpenMap top-level directory
rem CHANGE THIS TO MATCH YOUR CONFIGURATION
rem set OM_HOME=F:\alp\openmap-4.0
set LIB_HOME=../../lib
set BIN_HOME=../../classes
set CONFIG_HOME=./data/icontest

rem CLASSPATH points to toplevel OpenMap directory and share subdirectory
set CLASSPATH=%BIN_HOME%;%CONFIG_HOME%;%LIB_HOME%\openmap.jar;

rem OK, now run iconTest
%JAVABIN% -mx64m -classpath %CLASSPATH% -Dopenmap.configDir=%CONFIG_HOME% com.bbn.openmap.app.OpenMap
