echo off

echo *************************************************************
echo * To use, first place xml4j_2_0_11.jar (IMB XML parser)     *
echo * in ..\..\lib.                                             *
echo *************************************************************

set LIB_PATH=..\..\lib

set CP=..\..\classes
set CP=%CP%;%LIB_PATH%\xml4j_2_0_11.jar

java -classpath %CP% mil.darpa.log.alpine.blackjack.assessui.util.OrgXMLGenerator %1
