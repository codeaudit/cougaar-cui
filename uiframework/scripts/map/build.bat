@echo off

REM "<copyright>"
REM " "
REM " Copyright 2001-2004 BBNT Solutions, LLC"
REM " under sponsorship of the Defense Advanced Research Projects"
REM " Agency (DARPA)."
REM ""
REM " You can redistribute this software and/or modify it under the"
REM " terms of the Cougaar Open Source License as published on the"
REM " Cougaar Open Source Website (www.cougaar.org)."
REM ""
REM " THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS"
REM " "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT"
REM " LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR"
REM " A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT"
REM " OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,"
REM " SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT"
REM " LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,"
REM " DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY"
REM " THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT"
REM " (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE"
REM " OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE."
REM " "
REM "</copyright>"

echo Building Map UI classes...

setlocal

set BASE=..\..
set SRC_BASE=%BASE%\src
set CUR_BIN_DIR=%BASE%\classes
set LIB_HOME=..\..\lib

if NOT EXIST "%CUR_BIN_DIR%" echo Non-existant bin directory... Creating %CUR_BIN_DIR%
if NOT EXIST "%CUR_BIN_DIR%" mkdir %CUR_BIN_DIR%

REM the following is a convenient var which contains much of the package location
set MAP_SRC_HOME=%SRC_BASE%\org\cougaar\lib\uiframework\ui\map

set LIB_PATH=%CUR_BIN_DIR%
set LIB_PATH=%LIB_PATH%;%LIB_HOME%\xml4j_2_0_11.jar
set LIB_PATH=%LIB_PATH%;%LIB_HOME%\core.jar
set LIB_PATH=%LIB_PATH%;%LIB_HOME%\openmap.jar
set LIBPATH=%LIBPATH%;%LIB_HOME%\glm.jar
set LIBPATH=%LIBPATH%;%LIB_HOME%\aggagent.jar
set LIBPATH=%LIBPATH%;%LIB_HOME%\planserver.jar

if not exist %LIB_HOME%\xml4j_2_0_11.jar goto err1
if not exist %LIB_HOME%\core.jar goto err1
if not exist %LIB_HOME%\openmap.jar goto err1

set SRC_FILES=%MAP_SRC_HOME%\util\*.java
set SRC_FILES=%SRC_FILES%  %MAP_SRC_HOME%\layer\*.java
set SRC_FILES=%SRC_FILES%  %MAP_SRC_HOME%\query\*.java
set SRC_FILES=%SRC_FILES%  %MAP_SRC_HOME%\app\*.java

REM ****  CGM Icons
set SRC_FILES=%SRC_FILES%  %MAP_SRC_HOME%\layer\cgmicon\*.java
set SRC_FILES=%SRC_FILES%  %MAP_SRC_HOME%\layer\cgmicon\cgm\*.java


@rem Here's where the compiler lives and the flags we like to give it
@rem set JAVAC=c:\jdk1.2.2\bin\javac
set JAVAC=javac
set JAVAFLAGS= -g  %deprec% -classpath %LIB_PATH% -d %CUR_BIN_DIR%

echo This should be compiled with Javac version 1.2.2 

echo on
%JAVAC% %JAVAFLAGS% %SRC_FILES%
rem echo off

goto ok

:err1
 echo You are missing a required JAR file.
 echo You need the following JAR files:
 echo    %LIB_HOME%\xml4j_2_0_11.jar
 echo    %LIB_HOME%\core.jar
 echo    %LIB_HOME%\openmap.jar
 echo Cannot build.
 echo Done.

 goto end

:ok
echo Finished Building Map UI classes.
echo on

:end
