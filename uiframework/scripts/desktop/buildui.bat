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
