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


setlocal

set ORGHV_HOME=..\..
if "%ORGHV_HOME%"=="" goto err1
if NOT EXIST "%ORGHV_HOME%"  goto err2

set SRC_BASE=%ORGHV_HOME%\src
set BIN_BASE=%ORGHV_HOME%\classes
set LIB_BASE=%ORGHV_HOME%\..\..\lib
rem set LIB_BASE=%COUGAAR_INSTALL_PATH%

set LIBPATH=.
set LIBPATH=%LIBPATH%;%BIN_BASE%
set LIBPATH=%LIBPATH%;%ORGHV_HOME%\configs
set LIBPATH=%LIBPATH%;%ORGHV_HOME%\data
set LIBPATH=%LIBPATH%;%LIB_BASE%\core.jar
set LIBPATH=%LIBPATH%;%LIB_BASE%\xerces.jar
set LIBPATH=%LIBPATH%;%LIB_BASE%\vgj.jar

set APP=org.cougaar.lib.uiframework.ui.ohv.OrgHierApp
if "%1" == "editor" set APP=org.cougaar.lib.uiframework.ui.ohv.OrgHierEditorApp
echo %APP%

rem For class OrgHierApp:
rem Usage requires parameters: URL APP or defaultTest APP where APP is testciv or testdlv
rem The next-to-last word in the following command is the URL which supplies relational data

rem 
set JAVA_CMD=java -Xms100m -Xmx300m -classpath %LIBPATH%  %APP% defaultTest cview 
rem echo testciv
rem set JAVA_CMD=java -Xms100m -Xmx300m -classpath %LIBPATH% %APP% http://localhost:5555/$ohvagg/org/orgrels.psp  testdlv editor
set JAVA_CMD=java -Xms100m -Xmx300m -classpath %LIBPATH% %APP% http://localhost:5555/$ohvagg/org/orgrels.psp  cview 
rem editor


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

echo done.
@echo on
