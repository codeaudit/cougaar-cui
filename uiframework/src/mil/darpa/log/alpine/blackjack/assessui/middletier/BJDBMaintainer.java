package mil.darpa.log.alpine.blackjack.assessui.middletier;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

public interface BJDBMaintainer extends EJBObject
{
    public void updateDatabase(String updateXML) throws RemoteException;
}
