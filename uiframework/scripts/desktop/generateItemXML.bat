
REM "<copyright>"
REM " Copyright 2001 BBNT Solutions, LLC"
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

echo off

echo *******************************************************************
echo * To use, set COUGAAR_INSTALL_PATH.  Set DEVELOPMENT_PATH only if *
echo * you wish to override classes in the cougaar distribution.       *
echo *******************************************************************
set COUGAAR_INSTALL_PATH=c:\alpine\aggregationAgent\cougaar
echo COUGAAR_INSTALL_PATH is set to %COUGAAR_INSTALL_PATH%

set LIB_PATH=c:\alpine\lib
rem set LIB_PATH=..\..\lib
rem set LIB_PATH=s:\alp70\alp\lib

set DEVELOPMENT_PATH=..\..\classes
set DEVELOPMENT_PATH=%DEVELOPMENT_PATH%;C:\alpine\aggregationAgent\blackjack\classes
set LIB_PATH=%COUGAAR_INSTALL_PATH%\lib
set SYS_PATH=%COUGAAR_INSTALL_PATH%\sys

set CP=%DEVELOPMENT_PATH%
set CP=%CP%;%SYS_PATH%\xerces.jar
set CP=%CP%;%LIB_PATH%\uiframework.jar
set CP=%CP%;%LIB_PATH%\blackjack.jar
set CP=%CP%;%SYS_PATH%\classes12.zip

rem set DBTYPE="oracle"
set DBTYPE="access"

rem set DBURL="pfischer:1521:alp"
set DBURL="accessAssessment"
rem set DBURL="alp-demo:1521:alp"
rem set DBURL="eiger.alpine.bbn.com:1521:alp"
rem set DBURL="alp-3.alp.isotic.org:1521:alp"
rem set DBURL="alp-92.alp.isotic.org:1521:alp"

rem set DBUSER="pfischer"
rem set DBUSER="jmeyer"
rem set DBUSER="blackjack8"
rem set DBUSER="blackjack"
set DBUSER="blackjacka"

rem set DBPASSWORD="pfischer"
rem set DBPASSWORD="jmeyer"
rem set DBPASSWORD="blackjack"
set DBPASSWORD="blackjacka"

java -DDBTYPE=%DBTYPE% -DDBURL=%DBURL% -DDBUSER=%DBUSER% -DDBPASSWORD=%DBPASSWORD% -Dcmap.configDir=%DATA_PATH% -classpath %CP% mil.darpa.log.alpine.blackjack.assessui.util.ItemXMLGenerator
