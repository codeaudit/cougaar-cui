
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
rem set DBURL="alp-3.alp.isotic.org:1521:alp"

set DBUSER="pfischer"
rem set DBUSER="jmeyer"
rem set DBUSER="blackjack8"
rem set DBUSER="blackjack"

set DBPASSWORD="pfischer"
rem set DBPASSWORD="jmeyer"
rem set DBPASSWORD="init1389"
rem set DBPASSWORD="blackjack"

set JAVAC=java
set JAVAFLAGS=  -mx64m  %Debugging% -Dcmap.configDir=.\data  -DDBTYPE=%DBTYPE% -DDBURL=%DBURL% -DDBUSER=%DBUSER% -DDBPASSWORD=%DBPASSWORD% -classpath %LIB_PATH%

%JAVAC% %JAVAFLAGS% org.cougaar.lib.uiframework.ui.map.app.CMap

echo Finished Running Map UI.
echo on