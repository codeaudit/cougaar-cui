
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

echo *************************************************************
echo * To use, first place class122.zip (Oracle JDBC driver),    *
echo * and xml4j_2_0_11.jar (IMB XML parser) in ..\..\lib.       *
echo *************************************************************

set LIB_PATH=..\..\lib

set CP=..\..\classes
rem set CP=%CP%;%LIB_PATH%\class122.zip
set CP=%CP%;%LIB_PATH%\classes12.zip
set CP=%CP%;%LIB_PATH%\xml4j_2_0_11.jar

set DBTYPE="oracle"
rem set DBTYPE="access"

rem set DBURL="quicktableDemo"
rem set DBURL="alp-demo:1521:alp"
rem set DBURL="eiger.alpine.bbn.com:1521:alp"
rem set DBURL="alp-3.alp.isotic.org:1521:alp"
set DBURL="alp-92.alp.isotic.org:1521:alp"

rem set DBUSER="pfischer"
rem set DBUSER="jmeyer"
rem set DBUSER="blackjack8"
rem set DBUSER="blackjack"
set DBUSER="blackjacka"

rem set DBPASSWORD="pfischer"
rem set DBPASSWORD="jmeyer"
rem set DBPASSWORD="blackjack"
set DBPASSWORD="blackjacka"

java -DSTARTTIME=0 -DENDTIME=-1 -DCREATEITEMUNITTABLE=false -DCREATEDATA=false -DCREATEORGS=false -DCREATEMETRICS=true -DRANDOMDATA=false -classpath %CP% mil.darpa.log.alpine.blackjack.assessui.util.BlackjackTableCreator %DBTYPE% %DBURL% %DBUSER% %DBPASSWORD%
