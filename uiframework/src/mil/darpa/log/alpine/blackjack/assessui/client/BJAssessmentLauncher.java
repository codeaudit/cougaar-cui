/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package mil.darpa.log.alpine.blackjack.assessui.client;

import org.cougaar.lib.uiframework.ui.components.CFrameLauncher;
import org.cougaar.lib.uiframework.ui.map.app.CMap;

/**
 * Blackjack Assessment UI main application class.  Includes
 * selectable stoplight and lineplot data views.<BR><BR>
 *
 * The following system properies must be set for proper configuration:<BR><BR>
 *
 * DBURL - JDBC url to use to access the database<BR>
 * DBDRIVER - JDBC driver classname to use to access the database<BR>
 * DBUSER - User account to use to access the database<BR>
 * DBPASSWORD - User password to use to access the database<BR>
 * PLAF - "true" if dynamic pluggable look and feel must be supported<BR>
 *        (otherwise best Metal look and feel components are used)<BR>
 */
public class BJAssessmentLauncher extends CFrameLauncher
{
    /**
     * Default constructor.  Creates a new Blackjack Assessment launcher UI
     * with selectable stoplight and lineplot views.
     */
    public BJAssessmentLauncher()
    {
        super("BlackJack Assessment UI");

        boolean plaf = Boolean.getBoolean("PLAF"); // will be used in future

        addTool("Map View", 'M', CMap.class,
                new Class[]{boolean.class}, new Object[]{new Boolean(true)});
        addTool(UIConstants.STOPLIGHT_UI_NAME, 'S',
                StoplightPanel.class,
                new Class[]{boolean.class}, new Object[]{new Boolean(false)});
        addTool(UIConstants.LINEPLOT_UI_NAME, 'L',
                LinePlotPanel.class,
                new Class[]{boolean.class}, new Object[]{new Boolean(false)});
        //addTool(UIConstants.STOPLIGHT_UI_NAME +  " (PLAF)", 't',
        //        StoplightPanel.class,
        //        new Class[]{boolean.class}, new Object[]{new Boolean(true)});
        //addTool(UIConstants.LINEPLOT_UI_NAME + " (PLAF)", 'i',
        //        LinePlotPanel.class,
        //        new Class[]{boolean.class}, new Object[]{new Boolean(true)});
        pack();
        setVisible(true);

        // load item and org trees
        Object dummy = DBInterface.orgTree;
    }

    /**
     * Main for launching application.
     *
     * @param args ignored
     */
     public static void main(String[] args)
     {
        if ((System.getProperty("DBTYPE") == null) ||
            (System.getProperty("DBURL") == null) ||
            (System.getProperty("DBUSER") == null) ||
            (System.getProperty("DBPASSWORD") == null))
        {
            System.out.println("You need to set the following property" +
                               " variables:  DBTYPE, DBURL, DBUSER, and " +
                               "DBPASSWORD");
            return;
        }

        new BJAssessmentLauncher();
    }
}