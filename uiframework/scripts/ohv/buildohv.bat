@rem **** echo off

@rem **** echo *************************************************************
@rem **** echo * To use, first place class122.zip (Oracle JDBC driver) and *
@rem **** echo * xml4j_2_0_11.jar (IMB XML parser) in this directory.      *
@rem **** echo *************************************************************

@rem **** if NOT EXIST ..\classes mkdir ..\classes

set ORGHV_HOME=c:\dev\ui\fromcvs\cougaar\uiframework
if "%ORGHV_HOME%"=="" goto err1
if NOT EXIST "%ORGHV_HOME%"  goto err2

set LIBPATH=
set LIBPATH=%LIBPATH%;%ORGHV_HOME%\bin;%ORGHV_HOME%\config;;%ORGHV_HOME%\data
set LIBPATH=%LIBPATH%;%ORGHV_HOME%\lib\core_v7.jar
set LIBPATH=%LIBPATH%;%ORGHV_HOME%\lib\xerces.jar
@rem ***** set LIBPATH=%LIBPATH%;%COUGAAR_INSTALL_PATH%\core.jar
@rem ***** set LIBPATH=%LIBPATH%;%COUGAAR_INSTALL_PATH%\xerces.jar
set LIBPATH=%LIBPATH%;c:\integ\disptool\origFromZip\vgj.jar

set SRC_BASE=%ORGHV_HOME%\src
set BIN_BASE=%ORGHV_HOME%\bin
set JAVA_CMD=c:\jdk1.2.2\bin\javac %deprec% -classpath %LIBPATH%;  -d %BIN_BASE%

if NOT EXIST %BIN_BASE% echo **** Warning: Creating directory for bytecode: %BIN_BASE%
if NOT EXIST %BIN_BASE% mkdir %BIN_BASE%

set SRC_FILES=
set SRC_FILES= %SRC_FILES%

set SRC_FILES= %SRC_FILES% %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\algorithm\*.java
set SRC_FILES= %SRC_FILES%  %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\algorithm\tree\*.java
set SRC_FILES= %SRC_FILES%  %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\algorithm\cgd\*.java
set SRC_FILES= %SRC_FILES%  %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\algorithm\cartegw\*.java
set SRC_FILES= %SRC_FILES%  %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\algorithm\shawn\*.java
set SRC_FILES= %SRC_FILES%  %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\examplealg\*.java
set SRC_FILES= %SRC_FILES%  %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\gui\*.java 
set SRC_FILES= %SRC_FILES%  %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\graph\*.java
set SRC_FILES= %SRC_FILES%  %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\VGJ\*.java

set SRC_FILES= %SRC_FILES%  %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\*.java
set SRC_FILES= %SRC_FILES%  %SRC_BASE%\org\cougaar\lib\uiframework\ui\ohv\util\*.java


%JAVA_CMD% %SRC_FILES%


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
