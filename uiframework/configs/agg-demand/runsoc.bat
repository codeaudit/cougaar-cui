@echo off

setlocal

set NODE=MyNode

set EXECLASS=org.cougaar.core.society.Node
set NODEARGS=-c -n %NODE%

set BASELIB=..\..\lib
set CPATH=%BASELIB%\core.jar
set CPATH=%CPATH%;%BASELIB%\glm.jar
set CPATH=%CPATH%;%BASELIB%\planserver.jar
set CPATH=%CPATH%;%BASELIB%\aggagent.jar
set CPATH=%CPATH%;%BASELIB%\xalan.jar
set CPATH=%CPATH%;%BASELIB%\xerces.jar
set CPATH=%CPATH%;%BASELIB%\xml4j_2_0_11.jar
set CPATH=%CPATH%;..\..\classes

java -cp %CPATH% %EXECLASS% %NODEARGS%

@echo on
