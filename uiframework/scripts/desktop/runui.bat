echo off

echo *************************************************************
echo * To use, first place class122.zip (Oracle JDBC driver),    *
echo * core.jar, openmap.jar, and                                *
echo * xml4j_2_0_11.jar (IMB XML parser) in ..\..\lib.           *
echo *************************************************************

set LIB_PATH=..\..\lib
set DATA_PATH=.\data

set CP=..\..\classes
set CP=%CP%;%LIB_PATH%\class122.zip
set CP=%CP%;%LIB_PATH%\xml4j_2_0_11.jar
set CP=%CP%;%LIB_PATH%\core.jar
set CP=%CP%;%LIB_PATH%\openmap.jar
set CP=%CP%;%DATA_PATH%

set DBTYPE="oracle"
rem set DBTYPE="access"

rem set DBURL="quicktableDemo"
set DBURL="alp-demo:1521:alp"
rem set DBURL="eiger.alpine.bbn.com:1521:alp"

set DBUSER="pfischer"
rem set DBUSER="jmeyer"
rem set DBUSER="blackjack8"

set DBPASSWORD="pfischer"
rem set DBPASSWORD="jmeyer"
rem set DBPASSWORD="init1389"

java -DDBTYPE=%DBTYPE% -DDBURL=%DBURL% -DDBUSER=%DBUSER% -DDBPASSWORD=%DBPASSWORD% -Dopenmap.configDir=%DATA_PATH% -classpath %CP% mil.darpa.log.alpine.blackjack.assessui.client.BJAssessmentDesktop