/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package mil.darpa.log.alpine.blackjack.assessui.middletier;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

public interface BJDBMaintainer extends EJBObject
{
    public void updateDatabase(String updateXML) throws RemoteException;
}
