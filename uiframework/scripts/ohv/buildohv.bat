@echo off

REM "<copyright>"
REM " Copyright 2001-2003 BBNT Solutions, LLC"
REM " under sponsorship of the Defense Advanced Research Projects Agency (DARPA)."
REM ""
REM " This program is free software; you can redistribute it and/or modify"
REM " it under the terms of the Cougaar Open Source License as published by"
REM " DARPA on the Cougaar Open Source Website (www.cougaar.org)."
REM ""
REM " THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS"
REM " PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR"
REM " IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF"
REM " MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT"
REM " ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT"
REM " HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL"
REM " DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,"
REM " TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR"
REM " PERFORMANCE OF THE COUGAAR SOFTWARE."
REM "</copyright>"


setlocal

set ORGHV_HOME=..\..
if "%ORGHV_HOME%"=="" goto err1
if NOT EXIST "%ORGHV_HOME%"  goto err2

set SRC_BASE=%ORGHV_HOME%\src
set BIN_BASE=%ORGHV_HOME%\classes

set LIB_BASE=%ORGHV_HOME%\..\..\lib
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
