
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

echo off

echo *************************************************************
echo * To use, first place classes12.zip (Oracle JDBC driver),   *
echo * core.jar, cuimap.jar, glm.jar, tops.jar, LocationInfo.jar *
echo * and xerces.jar (XML parser) in ..\..\lib.                 *
echo *************************************************************

set LIB_PATH=..\..\lib
rem set LIB_PATH=s:\alp70\alp\lib
set DATA_PATH=.\data

set CP=..\..\classes
set CP=%CP%;%LIB_PATH%\classes12.zip
set CP=%CP%;%LIB_PATH%\xerces.jar
set CP=%CP%;%LIB_PATH%\core.jar
set CP=%CP%;%LIB_PATH%\cuimap.jar
set CP=%CP%;%LIB_PATH%\glm.jar
set CP=%CP%;%LIB_PATH%\LocationInfo.jar
set CP=%CP%;%LIB_PATH%\tops.jar
set CP=%CP%;%DATA_PATH%

set SRCR=..\..\src\org\cougaar\lib\uiframework
set SRBJ=..\..\src\mil\darpa\log\alpine\blackjack

set JF=
set JF=%JF% %SRCR%\transducer\*.java
set JF=%JF% %SRCR%\transducer\configs\*.java
set JF=%JF% %SRCR%\transducer\elements\*.java
set JF=%JF% %SRCR%\transducer\dbsupport\*.java
set JF=%JF% %SRCR%\ui\models\*.java
set JF=%JF% %SRCR%\ui\themes\*.java
set JF=%JF% %SRCR%\ui\util\*.java
set JF=%JF% %SRCR%\ui\components\graph\*.java
set JF=%JF% %SRCR%\ui\components\mthumbslider\*.java
set JF=%JF% %SRCR%\ui\components\*.java
set JF=%JF% %SRCR%\ui\map\util\*.java
set JF=%JF% %SRCR%\ui\map\layer\*.java
set JF=%JF% %SRCR%\ui\map\app\*.java
set JF=%JF% %SRCR%\ui\map\query\*.java
set JF=%JF% %SRBJ%\assessui\client\*.java
set JF=%JF% %SRBJ%\assessui\util\BlackjackTableCreator.java
set JF=%JF% %SRBJ%\assessui\util\OrgXMLGenerator.java

if NOT EXIST ..\..\classes mkdir ..\..\classes

javac -classpath %CP% -d ..\..\classes %JF%
rem c:\jdk1.2.2\bin\javac -classpath %CP% -d ..\..\classes %JF%
