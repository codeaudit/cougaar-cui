@echo off

setlocal

set CUIROOT=..\..

set CP=%CUIROOT%\classes
set CP=%CP%;e:\fgijars\xml4j_2_0_11.jar

set EX=org.cougaar.lib.uiframework.query.test.TestHarness

java -classpath %CP% %EX% %*

@echo on
