echo off

echo *************************************************************
echo * To use, first place class122.zip (Oracle JDBC driver) and *
echo * xml4j_2_0_11.jar (IMB XML parser) in this directory.      *
echo *************************************************************

rem java -DDBTYPE="access" -DDBURL="quicktableDemo" -DDBUSER="pfischer" -DDBPASSWORD="pfischer" -classpath ..\classes;class122.zip;xml4j_2_0_11.jar mil.darpa.log.alpine.blackjack.assessui.client.BJAssessmentDesktop
java -DDBTYPE="oracle" -DDBURL="alp-demo:1521:alp" -DDBUSER="pfischer" -DDBPASSWORD="pfischer" -classpath ..\classes;class122.zip;xml4j_2_0_11.jar mil.darpa.log.alpine.blackjack.assessui.client.BJAssessmentDesktop
