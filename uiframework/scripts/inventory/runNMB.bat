@ECHO OFF

set ALP_INSTALL_PATH=E:\Projects\blackjack\blackjack
set CLASSPATH=
set DELTA_PATH=e:\Projects\delta

set LIBPATHS= %DELTA_PATH%\bin
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\aggagent.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\core.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\toolkit.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\planserver.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\xerces.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\xalan.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\xml4j_2_0_11.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\fesi-111.jar
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\classes12.zip
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
set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\jcchart400K.jar


set LIBPATHS=%LIBPATHS%;.\bin
set LIBPATHS=%LIBPATHS%;.\uiframework.jar


set LIBPATHS=%LIBPATHS%;%DELTA_PATH%\lib\glm.jar

set ALP3RDPARTY=d:\opt\alp-jars

rem set MYMEMORY=-Xms100m -Xmx300m
set MYMEMORY=-Xms16m -Xmx32m

@ECHO ON

java -Djava.compiler=NONE -classpath %LIBPATHS% org.cougaar.lib.uiframework.ui.inventory.InventoryChartUI H 65.84.104.67:5555
