@rem **** 
echo off

@rem set ORGHV_HOME=c:\dev\ui\fromcvs\cougaar\uiframework
if "%ORGHV_HOME%"=="" goto err1
if NOT EXIST "%ORGHV_HOME%"  goto err2

set COUGAAR_INSTALL_PATH=c:\alpbuild\alp7\alp-20010215
set LIBPATH=
set LIBPATH=%LIBPATH%;%ORGHV_HOME%\bin;%ORGHV_HOME%\config;;%ORGHV_HOME%\data
set LIBPATH=%LIBPATH%;%COUGAAR_INSTALL_PATH%\lib\core.jar
set LIBPATH=%LIBPATH%;%COUGAAR_INSTALL_PATH%\lib\xerces.jar
set LIBPATH=%LIBPATH%;c:\integ\disptool\origFromZip\vgj.jar

set SRC_BASE=%ORGHV_HOME%\src
set BIN_BASE=%ORGHV_HOME%\bin


set APP=org.cougaar.lib.uiframework.ui.ohv.OrgHierApp
if "%1" == "editor" set APP=org.cougaar.lib.uiframework.ui.ohv.OrgHierEditorApp
echo %APP%
if "%2" == "" echo Usage requires parameters: URL APP or defaultTest APP where APP is testciv or testdlv

set JAVA_CMD=c:\jdk1.2.2\bin\java   -Xms100m -Xmx300m  -classpath %LIBPATH%  %APP% %1 %2

if NOT EXIST %BIN_BASE% echo **** Error: No directory for bytecode: %BIN_BASE%


%JAVA_CMD% 


goto end

:err2
 echo ORGHV_HOME points to a non-existant directory
 echo ORGHV_HOME is %ORGHV_HOME%

:err1
 echo Set ORGHV_HOME environment variable.
 goto end

:end
popd
echo done.
