@echo off

setlocal

set SP=.\src

set OP=.\classes

set LIB=e:\DELTAInst\lib

set CP=
set CP=%CP%;%LIB%\aggagent.jar
set CP=%CP%;%LIB%\core.jar
set CP=%CP%;%LIB%\glm.jar
set CP=%CP%;%LIB%\planserver.jar
set CP=%CP%;%LIB%\xml4j_2_0_11.jar
set CP=%CP%;e:\j2sdkee1.2.1\lib\j2ee.jar

set SRCR=src\org\cougaar\lib\uiframework
set SRBJ=src\mil\darpa\log\alpine\blackjack

set JF=
set JF=%JF% %SRCR%\query\*.java
set JF=%JF% %SRCR%\query\generic\*.java
set JF=%JF% %SRCR%\query\test\*.java
set JF=%JF% %SRCR%\transducer\*.java
set JF=%JF% %SRCR%\transducer\configs\*.java
set JF=%JF% %SRCR%\transducer\elements\*.java
set JF=%JF% %SRCR%\transducer\dbsupport\*.java
set JF=%JF% %SRCR%\ui\orglocation\data\*.java
set JF=%JF% %SRCR%\ui\orglocation\plugin\*.java
set JF=%JF% %SRCR%\ui\orglocation\psp\*.java
set JF=%JF% %SRCR%\ui\orglocation\psp\xmlservice\*.java
set JF=%JF% %SRBJ%\assessui\middletier\*.java
set JF=%JF% %SRBJ%\assessui\client\*.java
set JF=%JF% %SRBJ%\assessui\util\*.java
set JF=%JF% %SRBJ%\assessui\society\*.java

javac -classpath %CP% -sourcepath %SP% -d %OP% %JF%

@echo on
