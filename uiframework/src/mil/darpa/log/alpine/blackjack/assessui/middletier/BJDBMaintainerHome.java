package mil.darpa.log.alpine.blackjack.assessui.middletier;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface BJDBMaintainerHome extends EJBHome
{
    public BJDBMaintainer create() throws RemoteException, CreateException;
}
