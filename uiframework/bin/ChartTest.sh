#!/bin/sh

CLASSPATH=

LIBPATHS=.

LIBPATHS=$LIBPATHS:$COUGAAR_INSTALL_PATH/lib/uiframework.jar
LIBPATHS=$LIBPATHS:./bin

MYCLASSES=org.cougaar.lib.uiframework.ui.components.CChart

echo "exec java -Djava.compiler=NONE -classpath $LIBPATHS $MYCLASSES"

exec java -Djava.compiler=NONE -classpath $LIBPATHS $MYCLASSES
