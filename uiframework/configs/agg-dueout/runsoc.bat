@echo off

setlocal

set NODE=AggDueOutNode

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
