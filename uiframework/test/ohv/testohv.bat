@echo off

setlocal

set ORGHV_HOME=..\..
if "%ORGHV_HOME%"=="" goto err1
if NOT EXIST "%ORGHV_HOME%"  goto err2

set SRC_BASE=%ORGHV_HOME%\src
set BIN_BASE=%ORGHV_HOME%\classes
rem set LIB_BASE=%ORGHV_HOME%\..\..\lib
rem set LIB_BASE=%COUGAAR_INSTALL_PATH%\lib
rem set UILIB_BASE=%COUGAAR_INSTALL_PATH%\uiframework\lib
set LIB_BASE=\dev\ui\fromcvs\cougaar\lib
set UILIB_BASE=\dev\ui\fromcvs\cougaar\uiframework\lib

set LIBPATH=.
set LIBPATH=%LIBPATH%;%BIN_BASE%
set LIBPATH=%LIBPATH%;%ORGHV_HOME%\configs
set LIBPATH=%LIBPATH%;%ORGHV_HOME%\data
set LIBPATH=%LIBPATH%;%LIB_BASE%\core.jar
set LIBPATH=%LIBPATH%;%UILIB_BASE%\xerces.jar
set LIBPATH=%LIBPATH%;%LIB_BASE%\uiframework.jar
set LIBPATH=%LIBPATH%;%UILIB_BASE%\vgj.jar

set APP=org.cougaar.lib.uiframework.ui.ohv.OrgHierApp
if "%1" == "editor" set APP=org.cougaar.lib.uiframework.ui.ohv.OrgHierEditorApp
echo %APP%

rem For class OrgHierApp:
rem Usage requires parameters: URL APP or defaultTest APP where APP is usually cview

set JAVA_CMD=java -Xms100m -Xmx300m -classpath %LIBPATH%  %APP% defaultTest cview 

rem The next-to-last word in the following command is the URL which supplies relational data
rem set JAVA_CMD=java -Xms100m -Xmx300m -classpath %LIBPATH% %APP% http://localhost:5555/$ohvagg/org/orgrels.psp  cview
rem set JAVA_CMD=java -Xms100m -Xmx300m -classpath %LIBPATH% %APP% http://alp-155:5555/$ohvagg/org/orgrels.psp  cview

if NOT EXIST %BIN_BASE% echo **** Warning: No directory for bytecode: %BIN_BASE%

rem echo on
%JAVA_CMD% 
rem echo off

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
