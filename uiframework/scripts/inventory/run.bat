@ECHO OFF

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


set CLASSPATH=

set LIBPATHS=.
set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\core.jar
set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\glm.jar

set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\uiframework.jar
set LIBPATHS=%LIBPATHS%;.\bin
rem set LIBPATHS=%LIBPATHS%;%COUGAAR_INSTALL_PATH%\lib\classes


@ECHO ON

java -Djava.compiler=NONE -classpath %LIBPATHS% org.cougaar.lib.uiframework.ui.inventory.InventoryChartUI l 4
REM java -classpath %LIBPATHS% org.cougaar.lib.uiframework.ui.inventory.InventoryChartUI l 4
