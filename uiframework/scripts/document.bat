set SRCPATH=..\src
set DSTPATH=..\docs

echo off
if NOT EXIST %DSTPATH% mkdir %DSTPATH%
echo on

javadoc -package -author -version -sourcepath %SRCPATH% -d %DSTPATH% org.cougaar.lib.uiframework.ui.components org.cougaar.lib.uiframework.ui.models org.cougaar.lib.uiframework.ui.util org.cougaar.lib.uiframework.ui.themes org.cougaar.lib.uiframework.ui.components.graph org.cougaar.lib.uiframework.ui.components.mthumbslider  org.cougaar.lib.uiframework.transducer org.cougaar.lib.uiframework.transducer.configs org.cougaar.lib.uiframework.transducer.dbsupport org.cougaar.lib.uiframework.transducer.elements  mil.darpa.log.alpine.blackjack.assessui.client
