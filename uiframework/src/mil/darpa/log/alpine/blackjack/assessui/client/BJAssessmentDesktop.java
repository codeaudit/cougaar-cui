package mil.darpa.log.alpine.blackjack.assessui.client;

import org.cougaar.lib.uiframework.ui.components.CDesktopFrame;
import org.cougaar.lib.uiframework.ui.map.app.OpenMap;

/**
 * Blackjack Assessment Desktop UI main application class.  Includes
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
public class BJAssessmentDesktop extends CDesktopFrame
{
    /**
     * Default constructor.  Creates a new Blackjack Assessment Desktop UI with
     * selectable stoplight and lineplot views.
     */
    public BJAssessmentDesktop()
    {
        super("BlackJack Assessment UI");

        boolean plaf = Boolean.getBoolean("PLAF"); // will be used in future

        addTool("Map View", 'M', OpenMap.class,
                new Class[]{boolean.class}, new Object[]{new Boolean(true)});
        addTool("Stoplight View", 'S', StoplightPanel.class,
                new Class[]{boolean.class}, new Object[]{new Boolean(false)});
        addTool("Lineplot View", 'L', LinePlotPanel.class,
                new Class[]{boolean.class}, new Object[]{new Boolean(false)});
        addTool("Stoplight View (PLAF)", 't', StoplightPanel.class,
                new Class[]{boolean.class}, new Object[]{new Boolean(true)});
        addTool("Lineplot View (PLAF)", 'i', LinePlotPanel.class,
                new Class[]{boolean.class}, new Object[]{new Boolean(true)});
        addTool("Stoplight View (Old Style)", 'o', StoplightPanel.class,
                new Class[]{boolean.class, boolean.class},
                new Object[]{new Boolean(false), new Boolean(false)});
        addTool("Lineplot View (Old Style)", 'n', LinePlotPanel.class,
                new Class[]{boolean.class, boolean.class},
                new Object[]{new Boolean(false), new Boolean(false)});
        setVisible(true);
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

        new BJAssessmentDesktop();
    }
}