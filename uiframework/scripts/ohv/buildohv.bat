@echo off

setlocal

set ORGHV_HOME=..\..
if "%ORGHV_HOME%"=="" goto err1
if NOT EXIST "%ORGHV_HOME%"  goto err2

set SRC_BASE=%ORGHV_HOME%\src
set BIN_BASE=%ORGHV_HOME%\classes

set LIB_BASE=%ORGHV_HOME%\lib
rem set LIB_BASE=%COUGAAR_INSTALL_PATH%

set LIBPATH=%BIN_BASE%
set LIBPATH=%LIBPATH%;%ORGHV_HOME%\data
set LIBPATH=%LIBPATH%;%LIB_BASE%\core.jar
set LIBPATH=%LIBPATH%;%LIB_BASE%\xerces.jar
set LIBPATH=%LIBPATH%;%LIB_BASE%\vgj.jar

set JAVA_CMD=javac %deprec% -classpath %LIBPATH% -d %BIN_BASE%

if NOT EXIST %BIN_BASE% echo **** Warning: Creating directory for bytecode: %BIN_BASE%
if NOT EXIST %BIN_BASE% mkdir %BIN_BASE%

set SRC_FILES=
set SRC_FILES= %SRC_FILES% %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\algorithm\*.java
set SRC_FILES= %SRC_FILES% %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\algorithm\tree\*.java
set SRC_FILES= %SRC_FILES% %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\algorithm\cgd\*.java
set SRC_FILES= %SRC_FILES% %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\algorithm\cartegw\*.java
set SRC_FILES= %SRC_FILES% %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\algorithm\shawn\*.java
set SRC_FILES= %SRC_FILES% %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\examplealg\*.java
set SRC_FILES= %SRC_FILES% %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\gui\*.java 
set SRC_FILES= %SRC_FILES% %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\graph\*.java
set SRC_FILES= %SRC_FILES% %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\*.java

set SRC_FILES= %SRC_FILES% %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\*.java
set SRC_FILES= %SRC_FILES% %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\util\*.java


%JAVA_CMD% %SRC_FILES%


goto end

:err2
 echo ORGHV_HOME points to a non-existant directory
 echo ORGHV_HOME is %ORGHV_HOME%

:err1
 echo Set ORGHV_HOME environment variable.
 goto end

:end

echo done.
@echo on
