#!/bin/sh

source $COUGAAR_INSTALL_PATH/bin/setlibpath.sh
source $COUGAAR_INSTALL_PATH/bin/setarguments.sh

MYCLASSES="org.cougaar.lib.uiframework.ui.inventory.InventoryChartUI l 4 "

exec java $MYPROPERTIES -classpath $LIBPATHS $BOOTSTRAPPER $MYCLASSES $*
