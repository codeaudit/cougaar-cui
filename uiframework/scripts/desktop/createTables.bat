echo off

echo *************************************************************
echo * To use, first place class122.zip (Oracle JDBC driver),    *
echo * and xml4j_2_0_11.jar (IMB XML parser) in ..\..\lib.       *
echo *************************************************************

set LIB_PATH=..\..\lib

set CP=..\..\classes
rem set CP=%CP%;%LIB_PATH%\class122.zip
set CP=%CP%;%LIB_PATH%\classes12.zip
set CP=%CP%;%LIB_PATH%\xml4j_2_0_11.jar

set DBTYPE="oracle"
rem set DBTYPE="access"

rem set DBURL="quicktableDemo"
rem set DBURL="alp-demo:1521:alp"
rem set DBURL="eiger.alpine.bbn.com:1521:alp"
set DBURL="alp-3.alp.isotic.org:1521:alp"

rem set DBUSER="pfischer"
rem set DBUSER="jmeyer"
rem set DBUSER="blackjack8"
rem set DBUSER="blackjack"
set DBUSER="blackjacka"

rem set DBPASSWORD="pfischer"
rem set DBPASSWORD="jmeyer"
rem set DBPASSWORD="init1389"
rem set DBPASSWORD="blackjack"
set DBPASSWORD="blackjacka"

java -DSTARTTIME=0 -DENDTIME=1 -DCREATEITEMUNITTABLE=true -DCREATEDATA=false -DCREATEORGS=false -DCREATEITEMS=false -DCREATEMETRICS=false -DRANDOMDATA=false -classpath %CP% mil.darpa.log.alpine.blackjack.assessui.util.BlackjackTableCreator %DBTYPE% %DBURL% %DBUSER% %DBPASSWORD%
