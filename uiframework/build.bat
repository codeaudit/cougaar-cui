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

set SP=.\src

set OP=.\classes

rem *** alp-demo **** set LIB=e:\DELTAInst\lib
rem *** TIC **** set LIB=.\lib
rem the following is for when the build.bat is in uiframework directory
set LIB=..\..\lib

set CP=
set CP=%CP%;%LIB%\xerces.jar
set CP=%CP%;%LIB%\aggagent.jar
set CP=%CP%;%LIB%\core.jar
set CP=%CP%;%LIB%\glm.jar
set CP=%CP%;%LIB%\planserver.jar
set CP=%CP%;%LIB%\xml4j_2_0_11.jar
set CP=%CP%;%LIB%\j2ee.jar
set CP=%CP%;%LIB%\cuimap.jar

set CP0=%OP%
set CP0=%CP0%;%LIB%\xerces.jar
set CP0=%CP0%;%LIB%\vgj.jar
set CP0=%CP0%;%LIB%\core.jar

set SRCR=src\org\cougaar\lib\uiframework
set SRBJ=src\mil\darpa\log\alpine\blackjack

set JF0=
set JF1=
set JF2=
set JF3=

set JF1=%JF1% %SRCR%\query\*.java
set JF1=%JF1% %SRCR%\query\generic\*.java
set JF1=%JF1% %SRCR%\query\test\*.java
set JF1=%JF1% %SRCR%\transducer\*.java
set JF1=%JF1% %SRCR%\transducer\configs\*.java
set JF1=%JF1% %SRCR%\transducer\elements\*.java
set JF1=%JF1% %SRCR%\transducer\dbsupport\*.java
set JF1=%JF1% %SRCR%\ui\orglocation\data\*.java
set JF1=%JF1% %SRCR%\ui\orglocation\plugin\*.java
set JF1=%JF1% %SRCR%\ui\orglocation\psp\*.java
set JF1=%JF1% %SRCR%\ui\orglocation\psp\xmlservice\*.java
set JF1=%JF1% %SRCR%\ui\orgui\*.java

set JF0=%JF0% %SRCR%\ui\ohv\VGJ\algorithm\*.java
set JF0=%JF0% %SRCR%\ui\ohv\VGJ\algorithm\tree\*.java
set JF0=%JF0% %SRCR%\ui\ohv\VGJ\algorithm\cgd\*.java
set JF0=%JF0% %SRCR%\ui\ohv\VGJ\algorithm\cartegw\*.java
set JF0=%JF0% %SRCR%\ui\ohv\VGJ\algorithm\shawn\*.java
set JF0=%JF0% %SRCR%\ui\ohv\VGJ\examplealg\*.java
set JF0=%JF0% %SRCR%\ui\ohv\VGJ\gui\*.java 
set JF0=%JF0% %SRCR%\ui\ohv\VGJ\graph\*.java
set JF0=%JF0% %SRCR%\ui\ohv\VGJ\*.java
set JF0=%JF0% %SRCR%\ui\ohv\*.java
set JF0=%JF0% %SRCR%\ui\ohv\util\*.java

set JF2=%JF2% %SRCR%\ui\components\*.java
set JF2=%JF2% %SRCR%\ui\components\graph\*.java
set JF2=%JF2% %SRCR%\ui\components\mthumbslider\*.java

set JF3=%JF3% %SRCR%\ui\map\app\*.java
set JF3=%JF3% %SRCR%\ui\map\layer\*.java
set JF3=%JF3% %SRCR%\ui\map\util\*.java
set JF3=%JF3% %SRCR%\ui\map\query\*.java
set JF3=%JF3% %SRCR%\ui\models\*.java
set JF3=%JF3% %SRCR%\ui\themes\*.java
set JF3=%JF3% %SRCR%\ui\util\*.java
set JF3=%JF3% %SRCR%\ui\map\layer\cgmicon\*.java
set JF3=%JF3% %SRCR%\ui\map\layer\cgmicon\cgm\*.java

set JF3=%JF3% %SRBJ%\assessui\middletier\*.java
set JF3=%JF3% %SRBJ%\assessui\webtier\*.java
set JF3=%JF3% %SRBJ%\assessui\client\*.java
set JF3=%JF3% %SRBJ%\assessui\util\*.java
set JF3=%JF3% %SRBJ%\assessui\society\*.java

javac -classpath %CP0% -sourcepath %SP% -d %OP% %JF0%
javac -classpath %CP% -sourcepath %SP% -d %OP% %JF1%
javac -classpath %CP% -sourcepath %SP% -d %OP% %JF2%
javac -classpath %CP% -sourcepath %SP% -d %OP% %JF3%

@echo on
