@ECHO OFF

mkdir bin
set HOME=e:\Projects\BlackJackUI
set ALP_INSTALL_PATH=e:\Projects\delta

set DELTA_PATH=e:\Projects\delta
set LIBPATHS= %DELTA_PATH%\bin
set LIBPATHS=%LIBPATHS%;%HOME%\uiframework.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\aggagent.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\core.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\glm.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\toolkit.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\planserver.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\xerces.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\xalan.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\xml4j_2_0_11.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\fesi-111.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\classes12.zip
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\jcchart400K.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\xygraf.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\jess50.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\omcore.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\mail.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\activation.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\jsdk.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\delta.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\qslink.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\javaiopatch.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\jms.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\jndi.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\com.ibm.mq.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\com.ibm.mqjms.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\blackjack.jar

set MYMEMORY=-Xms100m -Xmx300m

set SOURCEFILES=
set SOURCEFILES=%SOURCEFILES% .\src\org\cougaar\lib\uiframework\ui\inventory\*.java
set SOURCEFILES=%SOURCEFILES% .\src\org\cougaar\lib\uiframework\ui\components\*.java
set SOURCEFILES=%SOURCEFILES% .\src\org\cougaar\lib\uiframework\ui\components\graph\*.java
set SOURCEFILES=%SOURCEFILES% .\src\org\cougaar\lib\uiframework\ui\components\mthumbslider\*.java

@ECHO ON

REM javac -deprecation -classpath %LIBPATHS% -d .\bin %SOURCEFILES%
javac -classpath %LIBPATHS% -d .\bin %SOURCEFILES%
