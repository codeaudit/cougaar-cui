@echo off

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


setlocal

set NODE=AggDemandNode

set EXECLASS=org.cougaar.core.society.Node
set NODEARGS=-c -n %NODE%

set BASELIB=%ALP_INSTALL_PATH%\lib
set CPATH=%BASELIB%\core.jar
set CPATH=%CPATH%;%BASELIB%\glm.jar
set CPATH=%CPATH%;%BASELIB%\planserver.jar
set CPATH=%CPATH%;%BASELIB%\aggagent.jar
set CPATH=%CPATH%;%BASELIB%\xalan.jar
set CPATH=%CPATH%;%BASELIB%\xerces.jar
set CPATH=%CPATH%;%BASELIB%\xml4j_2_0_11.jar
set CPATH=%CPATH%;%BASELIB%\uiframework.jar

java -Xmx128M -cp %CPATH% %EXECLASS% %NODEARGS%

@echo on
