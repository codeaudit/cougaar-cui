@echo off

set EXECLASS=org.cougaar.lib.uiframework.ui.orglocation.psp.TestClient

set BASELIB=d:\home\alpine\lib
set CPATH=%BASELIB%\core.jar
set CPATH=%CPATH%;%BASELIB%\glm.jar
set CPATH=%CPATH%;%BASELIB%\planserver.jar
set CPATH=%CPATH%;%BASELIB%\aggagent.jar
set CPATH=%CPATH%;%BASELIB%\xalan.jar
set CPATH=%CPATH%;%BASELIB%\xerces.jar
set CPATH=%CPATH%;%BASELIB%\xml4j_2_0_11.jar
set CPATH=%CPATH%;d:\cui\uiframework\classes

java -cp %CPATH% %EXECLASS% %*

@echo on
