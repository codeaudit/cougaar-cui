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

set DEVELOPMENT_PATH=C:\alpine\aggregationAgent\blackjack\classes
set LIB_PATH=%COUGAAR_INSTALL_PATH%\lib
set SYS_PATH=%COUGAAR_INSTALL_PATH%\sys

set CP=%DEVELOPMENT_PATH%
set CP=%CP%;%LIB_PATH%\blackjack.jar
set CP=%CP%;%SYS_PATH%\oracle12.zip

set TABLENAME="itemweights"

set SOURCEDBDRIVER="oracle.jdbc.driver.OracleDriver"
rem set SOURCEDBDRIVER="sun.jdbc.odbc.JdbcOdbcDriver"
set SOURCEDBURL="jdbc:oracle:thin:@eiger.alpine.bbn.com:1521:alp"
rem set SOURCEDBURL="jdbc:odbc:accessAssessment"
set SOURCEDBUSER="blackjack"
set SOURCEDBPASSWORD="######"

rem set TARGETDBDRIVER="oracle.jdbc.driver.OracleDriver"
set TARGETDBDRIVER="sun.jdbc.odbc.JdbcOdbcDriver"
rem set TARGETDBURL="jdbc:oracle:thin:@eiger.alpine.bbn.com:1521:alp"
rem set TARGETDBURL="jdbc:odbc:accessAssessment"
set TARGETDBURL="jdbc:odbc:UnitOfIssueHelper"
set TARGETDBUSER="blackjack"
set TARGETDBPASSWORD="blackjack"

java -classpath %CP% mil.darpa.log.alpine.blackjack.assessui.util.CopyTable  %TABLENAME% %SOURCEDBDRIVER% %SOURCEDBURL% %SOURCEDBUSER% %SOURCEDBPASSWORD% %TARGETDBDRIVER% %TARGETDBURL% %TARGETDBUSER% %TARGETDBPASSWORD%
