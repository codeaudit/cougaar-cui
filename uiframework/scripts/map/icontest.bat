
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

rem This will launch the OpenMap application
rem YOU MUST EDIT THE SETTINGS IN THIS FILE TO MATCH YOUR CONFIGURATION

rem Java Virtual Machine
rem CHANGE THIS TO MATCH YOUR CONFIGURATION
rem set JAVABIN=F:\jdk1.2.2\bin\java.exe
set JAVABIN=java 

rem OpenMap top-level directory
rem CHANGE THIS TO MATCH YOUR CONFIGURATION
rem set OM_HOME=F:\alp\openmap-4.0
set LIB_HOME=../../lib
set BIN_HOME=../../classes
set CONFIG_HOME=./data/icontest

rem CLASSPATH points to toplevel OpenMap directory and share subdirectory
set CLASSPATH=%BIN_HOME%;%CONFIG_HOME%;%LIB_HOME%\openmap.jar;

rem OK, now run iconTest
%JAVABIN% -mx64m -classpath %CLASSPATH% -Dopenmap.configDir=%CONFIG_HOME% com.bbn.openmap.app.OpenMap
