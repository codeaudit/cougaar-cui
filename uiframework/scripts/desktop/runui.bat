echo off

echo *************************************************************
echo * To use, first place classes12.zip (Oracle JDBC driver),   *
echo * core.jar, cuimap.jar, glm.jar and                         *
echo * xml4j_2_0_11.jar (IMB XML parser) in ..\..\lib.           *
echo *************************************************************

copy marker.txt ..\map
cd ..\map

set LIB_PATH=..\..\lib
rem set LIB_PATH=s:\alp70\alp\lib
set DATA_PATH=.\data

set CP=..\..\classes
set CP=%CP%;%LIB_PATH%\classes12.zip
set CP=%CP%;%LIB_PATH%\xml4j_2_0_11.jar
set CP=%CP%;%LIB_PATH%\core.jar
set CP=%CP%;%LIB_PATH%\cuimap.jar
set CP=%CP%;%LIB_PATH%\glm.jar
set CP=%CP%;%DATA_PATH%

set DBTYPE="oracle"
rem set DBTYPE="access"

rem set DBURL="pfischer:1521:alp"
rem set DBURL="quicktableDemo"
rem set DBURL="alp-demo:1521:alp"
rem set DBURL="eiger.alpine.bbn.com:1521:alp"
rem set DBURL="alp-3.alp.isotic.org:1521:alp"
set DBURL="alp-92.alp.isotic.org:1521:alp"

rem set DBUSER="pfischer"
rem set DBUSER="jmeyer"
rem set DBUSER="blackjack8"
rem set DBUSER="blackjack"
set DBUSER="blackjacka"

rem set DBPASSWORD="pfischer"
rem set DBPASSWORD="jmeyer"
rem set DBPASSWORD="blackjack"
set DBPASSWORD="blackjacka"

java -DDBTYPE=%DBTYPE% -DDBURL=%DBURL% -DDBUSER=%DBUSER% -DDBPASSWORD=%DBPASSWORD% -Dcmap.configDir=%DATA_PATH% -classpath %CP% mil.darpa.log.alpine.blackjack.assessui.client.BJAssessmentLauncher
rem d:\jdk1.3\bin\java -DDBTYPE=%DBTYPE% -DDBURL=%DBURL% -DDBUSER=%DBUSER% -DDBPASSWORD=%DBPASSWORD% -Dcmap.configDir=%DATA_PATH% -classpath %CP% mil.darpa.log.alpine.blackjack.assessui.client.BJAssessmentLauncher
rem java -DDBTYPE=%DBTYPE% -DDBURL=%DBURL% -DDBUSER=%DBUSER% -DDBPASSWORD=%DBPASSWORD% -Dcmap.configDir=%DATA_PATH% -classpath %CP% mil.darpa.log.alpine.blackjack.assessui.client.BJAssessmentDesktop

del marker.txt
cd ..\desktop