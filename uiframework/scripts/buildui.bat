echo off

echo *************************************************************
echo * To use, first place class122.zip (Oracle JDBC driver) and *
echo * xml4j_2_0_11.jar (IMB XML parser) in this directory.      *
echo *************************************************************

if NOT EXIST ..\classes mkdir ..\classes

javac -classpath ..\classes;class122.zip;xml4j_2_0_11.jar -d ..\classes ..\src\org\cougaar\lib\uiframework\ui\components\*.java ..\src\org\cougaar\lib\uiframework\ui\models\*.java ..\src\org\cougaar\lib\uiframework\ui\util\*.java ..\src\org\cougaar\lib\uiframework\ui\themes\*.java ..\src\org\cougaar\lib\uiframework\ui\components\graph\*.java ..\src\org\cougaar\lib\uiframework\ui\components\mthumbslider\*.java  ..\src\org\cougaar\lib\uiframework\transducer\*.java ..\src\org\cougaar\lib\uiframework\transducer\configs\*.java ..\src\org\cougaar\lib\uiframework\transducer\dbsupport\*.java ..\src\org\cougaar\lib\uiframework\transducer\elements\*.java  ..\src\mil\darpa\log\alpine\blackjack\assessui\client\*.java
