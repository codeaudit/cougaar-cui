/* **********************************************************************
 *
 *    Use, duplication, or disclosure by the Government is subject to
 *              restricted rights as set forth in the DFARS.
 *
 *                            BBNT Solutions LLC
 *                               A Part of
 *                                  GTE
 *                           10 Moulton Street
 *                          Cambridge, MA 02138
 *                             (617) 873-3000
 *
 *           Copyright 1998, 2000 by BBNT Solutions LLC,
 *                 A part of GTE, all rights reserved.
 *
 * **********************************************************************
 *
 *
 * ***********************************************************************/

package org.cougaar.lib.uiframework.ui.map.app;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.border.BevelBorder;

//import com.bbn.openmap.MouseDelegator;
import com.bbn.openmap.InformationDelegator;
import com.bbn.openmap.Environment;
import com.bbn.openmap.LatLonPoint;
import com.bbn.openmap.Layer;
import com.bbn.openmap.gui.LayerHandler;
import com.bbn.openmap.MapBean;
import com.bbn.openmap.BufferedMapBean;
import com.bbn.openmap.event.*;
import com.bbn.openmap.gui.*;
import com.bbn.openmap.proj.*;
import com.bbn.openmap.util.Debug;
import org.cougaar.lib.uiframework.ui.components.CFrame;
import org.cougaar.lib.uiframework.ui.components.CLabeledSlider;
import org.cougaar.lib.uiframework.ui.components.CDateLabeledSlider;
import org.cougaar.lib.uiframework.ui.components.IconScalePanel;
import org.cougaar.lib.uiframework.ui.components.ScenarioOMToolSet;
import org.cougaar.lib.uiframework.ui.components.ScenarioMenuPanel;

import org.cougaar.lib.uiframework.ui.map.util.*; // only for JTIButton, etc.
import org.cougaar.lib.uiframework.ui.util.CougaarUI;

import org.cougaar.lib.uiframework.ui.map.layer.PspIconLayer;
import org.cougaar.lib.uiframework.ui.map.layer.PspIconLayerBase;
import org.cougaar.domain.glm.map.MapLocationInfo;
import org.cougaar.domain.planning.ldm.plan.LocationScheduleElement;

import org.cougaar.domain.glm.ldm.plan.GeolocLocationImpl;
import org.cougaar.domain.planning.ldm.measure.Latitude;
import org.cougaar.domain.planning.ldm.measure.Longitude;

/**
 * The ScenarioMap Viewer application.
 * <p>
 * This is a sample application using the MapBean.
 *
 */
public class ScenarioMap implements Serializable, CougaarUI {

    /** The name of the properties file to read. */
    public static String propsFileName = "scenariomap.properties";

    /** The name of the system directory containing a properties file. */
    public static String configDirProperty = "ScenarioMap.configDir";

    /** Starting X coordinate of window */
    public static transient final String xProperty = "openmap.x";

    /** Starting Y coordinate of window */
    public static transient final String yProperty = "openmap.y";

    /** The application properties. */
    protected Properties props;

    /** Map Window */
    public static MapBean map;
    /** The suite of control widgets. */
    protected ToolPanel controls;
    /** The Information and Status Manager. */
    protected InformationDelegator info;
    /** The suite of menus. */
    protected ScenarioMenuPanel menu;
    /** The layer handler, for dynamic adjustments of layers. */
    protected LayerHandler layerHandler;

    /** main application frame **/
    protected JFrame frame;

    protected ScenarioOMToolSet omts;

    final int LOCATIONS = 0;
    final int RELATIONSHIPS = 1;


  private JDialog sliderDialog = null;
  private JTextField monthField1 = new JTextField();
  private JTextField dayField1 = new JTextField();
  private JTextField yearField1 = new JTextField();

  private JTextField monthField2 = new JTextField();
  private JTextField dayField2 = new JTextField();
  private JTextField yearField2 = new JTextField();

  public static CDateLabeledSlider rangeSlider = null;
//  private CDateLabeledSlider rangeSlider = null;

  private JDialog changeLInfoDialog = null;
  private JTextField urlField = new JTextField();

  private JDialog cDateDialog = null;
  private JTextField monthField = new JTextField();
  private JTextField dayField = new JTextField();
  private JTextField yearField = new JTextField();


    /**
     * Install this user interface in the passed in JInternalFrame.
     * Required for implementation of CougaarUI interface.
     *
     * @param f internal frame to which this user interface should be added
     */
    public void install(JFrame f)
    {
      frame.setVisible(false);
      f.setContentPane(frame.getContentPane());
      f.setJMenuBar(frame.getJMenuBar());
    }

    /**
     * Install this user interface in the passed in JFrame.
     * Required for implementation of CougaarUI interface.
     *
     * @param f frame to which this user interface should be added
     */
    public void install(JInternalFrame f)
    {
        init((Applet)null);
        // start instance of ScenarioMap
        this.init();

        frame.setVisible(false);
        f.setContentPane(frame.getContentPane());

        // remove laf and themes menus from inner frame
        JMenuBar mb = frame.getJMenuBar();
        mb.remove(5);
        mb.remove(5);
        f.setJMenuBar(mb);
    }

    /**
     * Returns true if this UI supports pluggable look and feel.  Otherwise,
     * only Metal look and feel support is assumed.
     * Required for implementation of CougaarUI interface.
     *
     * @return true if UI supports pluggable look and feel.
     */
    public boolean supportsPlaf()
    {
        return true;
    }

    /**
     * Loads properties from a java resource.  This will load the
     * named resource identifier into the given properties instance.
     *
     * @param properties the Properties instance to receive the properties
     * @param resourceName the name of the resource to load
     * @param verbose indicates whether status messages should be printed
     */
    protected boolean loadPropertiesFromResource(Properties properties,
                                                 String resourceName,
                                                 boolean verbose)
    {
        InputStream propsIn = getClass().getResourceAsStream(resourceName);

        if (propsIn == null) {

            if (verbose) {
                Debug.error("ScenarioMap: Unable to locate resources: "
                            + resourceName);
            }
            return false;

        } else {

            try {
                properties.load(propsIn);
                return true;
            } catch (java.io.IOException e) {
                if (verbose) {
                    Debug.error("ScenarioMap: Caught IOException loading resources: "
                                + resourceName);
                }
                return false;
            }

        }
    }

    /**
     * Loads properties from a java resource.  This will load the
     * named resource identifier into the given properties instance.
     *
     * @param properties the Properties instance to receive the properties
     * @param resourceName the name of the resource to load
     * @param verbose indicates whether status messages should be printed
     */
    public boolean loadProperties(URL url, boolean verbose)
    {

        try {
            InputStream propsIn = url.openStream();
            props.load(propsIn);
            return true;
        } catch (java.io.IOException e) {
            if (verbose) {
                Debug.error("ScenarioMap Caught IOException loading resources: "
                            + url);
            }
            return false;
        }

    }

    /**
     * Load the named file from the named directory into the given
     * <code>Properties</code> instance.  If the file is not found
     * a warning is issued.  If an IOExceptio occurs, a fatal error
     * is printed and the application will exit.
     *
     * @param props the instance to receive the loaded properties
     * @param dir the directory where the properties file resides
     * @param file the name of the file
     */
    public void loadProps(Properties props, String dir, String file) {
        File propsFile = new File(dir, file);

        try {
            InputStream propsStream = new FileInputStream(propsFile);
            props.load(propsStream);
        } catch (java.io.FileNotFoundException e) {
            if (Debug.debugging("basic")){
                Debug.output("Unable to read configuration file \""
                             + propsFile + "\"");
            }
        } catch (java.io.IOException e) {
            Debug.error("Caught IO Exception reading "
                        + "configuration file \""
                        + propsFile + "\"");
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (Debug.debugging("properties")){
        //System.out.println("\n\nproperties in loadProps(props, "+dir+","+file+": ");
        //System.out.println(props);
      }
  }
    }

    /**
     * Calls initApplication(true), which will cause the application
     * to look for the properties as a resource in the jar file, or in
     * the user's home directory.  Called by default by the Application.
     */
    public void initApplication() {
        initApplication(true);
    }

    /**
     * Initializes this application.  This is a 3 step process:
     * <ol>
     * <li>
     * Read base properties from the resource file
     * com.bbn.openmap.app.openmap.properties.
     * </li>
     * <li>
     * Read properties from the scenariomap installion, if they exist.
     * This is usually <scenariomapInstallDir>/scenariomap.properties.
     * </li>
     * <li>
     * Read user properties from $HOME/scenariomap.properties.  This is
     * based on the JDK system property <code>user.home</code>
     * </li>
     * </ol>
     * @param loadProperties true if you want to look in
     * certain locations for the scenariomap.properties file, overwriting
     * any properties that may already be set.
     */
    public void initApplication(boolean loadProperties) {

        // load properties from resource file, overwriting anything
        // that may already be set.
        if (loadProperties){
            loadPropertiesFromResource(props, propsFileName, false);
        }

        if (Environment.isApplication()) {
            // load properties from system area
            String configDir = System.getProperty(configDirProperty);
            if (loadProperties && configDir != null) {
                loadProps(props, configDir, propsFileName);
            } else {
                // For now, don't complain if the system level properties
                // File is missing.
            }

            // load properties from home directory
            String homeDir = System.getProperty("user.home");
            if (loadProperties && homeDir != null) {
                loadProps(props, homeDir, propsFileName);
            } else {
                // For now, don't complain if the home directory level
                // properties File is missing.
                System.err.println("No properties file in default directory" );
                System.err.println("Default directory:  "+homeDir );
                System.err.println("Properties file name:  "+propsFileName );
            }

            // create a properties list that contains the user properties
            // as defaults.
            // bug alert -- props = new Properties(props);
            // bug alert -- actually the above line prevents the Environment class
            //              from containing the properties from the config file


            // System properties are the absolute toplevel
            Properties sysProps = System.getProperties();
            Enumeration keys = sysProps.keys();
            int len = sysProps.size();
            for (int i=0; i<len; i++) {
                Object key = keys.nextElement();
                props.put(key, sysProps.get(key));
            }
        }

  // Give the environment class its own copy
  Properties propsClone= new Properties(props);
        // initialize the Environment with the hierarchical properties
        // list
        // System.err.println("initApplication using these properties: "+propsClone);
        //Environment.init(props);
        Environment.init(propsClone);
    }

    /**
     * Initialize the ScenarioMap Environment.
     *
     * @param applet an applet or null
     */
    public static void init(Applet applet) {

        if (applet == null) {

            // do if we're running as an application

            Properties p = System.getProperties();

            // First initialize debugging
            Debug.init(p);

            Environment.init(p);

        } else {

            // Initialize as an applet
            Debug.init(applet,
                       new String[] {"debug.basic",
                                     "debug.cspec",
                                     "debug.layer",
                                     "debug.mapbean",
                                     "debug.plugin"
                       });
            Environment.init(applet);

        }
    }


    // kwr

    /**
     * Start ScenarioMap Viewer as a standalone application.
     *
     * @param args String[] curently ignored
     */
    public static void main_prev (String[] args) {

        // static initializations of Debugging and Environment
        init(null);
        // start instance of ScenarioMap
        new ScenarioMap().init();
    }



    public static void main (String[] args) {
      System.out.println("alp scenariomap") ;
            // static initializations of Debugging and Environment
            init(null);
            // start instance of ScenarioMap
            new ScenarioMap().init();

    }


    public static ScenarioMap generateMapBean(JPanel panel)
    {
        ScenarioMap om;
        init(null);
        om=new ScenarioMap();
        om.initApplication(true);
        om.prepareMap(panel);  // similar to start()

        return om;
    }

    public void prepareMap (JPanel panel){

        // JFrame frame = null;
        //        JRootPane rootPane = null;
        boolean addLayerButton = true;
        boolean addOverviewButton = true;


        if (Debug.debugging("version")){
        // output version information
            Debug.output("ScenarioMap Viewer " +
                         Environment.get(Environment.Version));
            Debug.output("Build " +
                         Environment.get(Environment.BuildDate, "<no build tag>"));
            Debug.output("ScenarioMap Viewer running inside " +
                         Environment.get("java.vendor") + " Java VM on " +
                         Environment.get("os.name"));
        }

        // instantiate the MapBean and other gui beans by sending them
        // off to be set.  Make the call to set the specific type of
        // beans to use.  If something was set to null, fill in the
        // default.
        setWidgets(props);

        map = getMapBean();
        if (map == null){
            map = new BufferedMapBean();
        }

        /* ===========
           =========== */
        // moved lower in method
        // controls = getToolPanel();
        //  If controls aren't set, seems silly to try and set an
        //  empty default.

        info = getInformationDelegator();
        if (info == null) {
            info = new InformationDelegator();
            // InformationDelegator handles the display of popup text/html
            // information.
            info.setMap(map);
            info.setFloatable(false);
        }

        menu = getMenuPanel();
        if (menu == null) {
            menu = new ScenarioMenuPanel(this);
        }

        // add the menu
        // no rootpane -- add in again later
        // rootPane.setJMenuBar(menu);

        // no rootpane -- add in again later
        //final JLayeredPane desktop = rootPane.getLayeredPane();
        //desktop.setOpaque(true);

        layerHandler = getLayerHandler();
        if (layerHandler == null){
            layerHandler = new LayerHandler("scenariomap", props);
        } else {
            layerHandler.init("scenariomap", props);
        }

        findPspIconLayer().setScenerioMap(this);

          menu.add(layerHandler.getLayersMenu("Edit Layers..."));


        // Add an extra help menu to the menubar.  Have the InformationDelegator
        // show the help pages.
        JMenu helpMenu =  new JMenu("Help");
        helpMenu.setMnemonic('H');
        JMenuItem mi = helpMenu.add(new JMenuItem("ScenarioMap"));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                info.displayURL(Environment.get(Environment.HelpURL,
                "http://javamap.bbn.com/projects/openmap/openmap_maindes.html"));
            }
        });
        mi.setActionCommand("showHelp");
        //menu.setHelpMenu(helpMenu);Use this when Sun decides to implement it
        menu.add(helpMenu);


        addToolPanelControls() ;

        controls = getToolPanel();
        // Add the toolbar to the top of the window
        if (controls != null)
            // rootPane.getContentPane().add("North", controls);
            panel.add("North", controls);

        // Initialize the map projection, scale, center with user prefs or
        // defaults
        String projName = Environment.get(Environment.Projection, Mercator.MercatorName);
        int projType = ProjectionFactory.getProjType(projName);
        map.setProjectionType(projType);
        map.setScale(Environment.getFloat(Environment.Scale, Float.POSITIVE_INFINITY));
        map.setCenter(new LatLonPoint(
                Environment.getFloat(Environment.Latitude, 0f),
                Environment.getFloat(Environment.Longitude, 0f)
                ));

        // Add the map and then the status line to the window
        //rootPane.getContentPane().add("Center", map);
        //rootPane.getContentPane().add("South", info);
        panel.add("Center", map);
        panel.add("South", info);

        // Make it look a little prettier
        if (Environment.isApplication()) {
            map.setBorder(new BevelBorder(BevelBorder.LOWERED));
        } else {
            // hack to work around a java bug with borders in applets
            map.setBorder(BorderFactory.createMatteBorder(2, //top
                                                          2, //left
                                                          2, //bottom
                                                          2, //right
                                                          Color.gray));
        }

        // show the window
        if (Environment.isApplication()) {
            // get starting width and height
            int w = Integer.parseInt(Environment.get(Environment.Width, "640"));
            int h = Integer.parseInt(Environment.get(Environment.Height, "480"));

            // get starting x and y position.  default to center of
            // screen.
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            int x = Environment.getInteger(xProperty, -1);
            int y = Environment.getInteger(yProperty, -1);
            if (x < 0)
                x = d.width/2 - w/2;
            if (y < 0)
                y = d.height/2 -h/2;

            // compose the frame, but don't show it here
            //frame.setBounds(x, y, w, h);
            //frame.show();
        }

        // set up the listeners
        // map listens for LayerEvents
        layerHandler.addLayerListener(map);
        menu.setupListeners(map);

        // Need to be able to access timeSlider from XmlLayerBase
    }

    // end -- kwr
    public ScenarioMap () {
        props = new Properties();
    }

    private boolean showUponInit = true;

    public ScenarioMap(boolean initApp)
    {
        showUponInit = false;
        if (initApp) init(null);
        props = new Properties();
        if (initApp) init();
    }

    /**
     * Load default properties, and then launch ScenarioMap.  Kept here to
     * preserve the API.
     */
    public void init () {
        initApplication(true);
        start();
    }

    /**
     * An init() that gives the option of whether or not to have the
     * application search for the scenariomap.properties file located in
     * the scenariomap.jar file, or in the user's home directory, or in
     * the CLASSPATH.  Set the argument to false if you are setting
     * the properties (for layers, startup projection, etc) manually,
     * and don't want your values overwritten by the default values in
     * an scenariomap.properties file.
     * @param loadResourceProperties false if you've set the
     * properties manually, true if you want the properties to be set
     * off the scenariomap.properties file.
     */
    public void init (boolean loadResourceProperties) {

        initApplication(loadResourceProperties);
        start();
    }

    /**
     * Launch ScenarioMap, assuming that all the properties have been set.
     */

    // JRootPane rootPane = null;

    public void startApplet (){

        frame = null;
        JRootPane rootPane = null;
        boolean addLayerButton = true;
        boolean addOverviewButton = true;
        rootPane = ((JApplet)(Environment.getApplet())).getRootPane();

        if (Debug.debugging("version")){
            // output version information
            Debug.output("ScenarioMap Viewer " +
                         Environment.get(Environment.Version));
            Debug.output("Build " +
                         Environment.get(Environment.BuildDate, "<no build tag>"));
            Debug.output("ScenarioMap Viewer running inside " +
                         Environment.get("java.vendor") + " Java VM on " +
                         Environment.get("os.name"));
        }

        // instantiate the MapBean and other gui beans by sending them
        // off to be set.  Make the call to set the specific type of
        // beans to use.  If something was set to null, fill in the
        // default.
        setWidgets(props);

        map = getMapBean();
        if (map == null){
            map = new BufferedMapBean();
        }

        controls = getToolPanel();
        //  If controls aren't set, seems silly to try and set an
        //  empty default.

        info = getInformationDelegator();
        if (info == null) {
            info = new InformationDelegator();
            // InformationDelegator handles the display of popup text/html
            // information.
            info.setMap(map);
            info.setFloatable(false);
        }

        menu = getMenuPanel();
        if (menu == null) {
            menu = new ScenarioMenuPanel(this);
        }

        // add the menu
        rootPane.setJMenuBar(menu);

        final JLayeredPane desktop = rootPane.getLayeredPane();
        desktop.setOpaque(true);

        layerHandler = getLayerHandler();
        if (layerHandler == null){
            layerHandler = new LayerHandler("scenariomap", props);
        } else {
            layerHandler.init("scenariomap", props);
        }

        findPspIconLayer().setScenerioMap(this);

          menu.add(layerHandler.getLayersMenu("Edit Layers..."));

        // Add an extra help menu to the menubar.  Have the InformationDelegator
        // show the help pages.
        JMenu helpMenu =  new JMenu("Help");
        helpMenu.setMnemonic('H');
        JMenuItem mi = helpMenu.add(new JMenuItem("ScenarioMap"));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                info.displayURL(Environment.get(Environment.HelpURL,
                "http://javamap.bbn.com/projects/openmap/openmap_maindes.html"));
            }
        });
        mi.setActionCommand("showHelp");
        //menu.setHelpMenu(helpMenu);Use this when Sun decides to implement it
        menu.add(helpMenu);

        addToolPanelControls() ;

        // Add the toolbar to the top of the window
        if (controls != null)
            rootPane.getContentPane().add("North", controls);

        // Initialize the map projection, scale, center with user prefs or
        // defaults
        String projName = Environment.get(Environment.Projection, Mercator.MercatorName);
        int projType = ProjectionFactory.getProjType(projName);
        map.setProjectionType(projType);
        map.setScale(Environment.getFloat(Environment.Scale, Float.POSITIVE_INFINITY));
        map.setCenter(new LatLonPoint(
                Environment.getFloat(Environment.Latitude, 0f),
                Environment.getFloat(Environment.Longitude, 0f)
                ));

        // Add the map and then the status line to the window
        rootPane.getContentPane().add("Center", map);
        rootPane.getContentPane().add("South", info);

        // Make it look a little prettier
        if (Environment.isApplication()) {
            map.setBorder(new BevelBorder(BevelBorder.LOWERED));
        } else {
            // hack to work around a java bug with borders in applets
            map.setBorder(BorderFactory.createMatteBorder(2, //top
                                                          2, //left
                                                          2, //bottom
                                                          2, //right
                                                          Color.gray));
        }

        // show the window
        if (Environment.isApplication()) {
            // get starting width and height
            int w = Integer.parseInt(Environment.get(Environment.Width, "640"));
            int h = Integer.parseInt(Environment.get(Environment.Height, "480"));

            // get starting x and y position.  default to center of
            // screen.
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            int x = Environment.getInteger(xProperty, -1);
            int y = Environment.getInteger(yProperty, -1);
            if (x < 0)
                x = d.width/2 - w/2;
            if (y < 0)
                y = d.height/2 -h/2;

            // compose the frame, but don't show it here
            frame.setBounds(x, y, w, h);
            frame.setVisible(showUponInit);
        }

        // set up the listeners
        // map listens for LayerEvents
        layerHandler.addLayerListener(map);
        menu.setupListeners(map);

    }

    public void start (){

        frame = null;
        JRootPane rootPane = null;
        boolean addLayerButton = true;
        boolean addOverviewButton = true;

        // get the Root window
        if (Environment.isApplication()) {
            //frame = new JFrame(Environment.get(Environment.Title));
            frame = new CFrame(Environment.get(Environment.Title), true);
            rootPane = frame.getRootPane();

            // listen for window close event
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    // need a shutdown event to notify other gui beans and
                    // then exit.
                    System.exit(0);
                }
            });
        } else {
            rootPane = ((JApplet)(Environment.getApplet())).getRootPane();
        }

        if (Debug.debugging("version")){
        // output version information
            Debug.output("ScenarioMap Viewer " +
                         Environment.get(Environment.Version));
            Debug.output("Build " +
                         Environment.get(Environment.BuildDate, "<no build tag>"));
            Debug.output("ScenarioMap Viewer running inside " +
                         Environment.get("java.vendor") + " Java VM on " +
                         Environment.get("os.name"));
        }

        // instantiate the MapBean and other gui beans by sending them
        // off to be set.  Make the call to set the specific type of
        // beans to use.  If something was set to null, fill in the
        // default.
        setWidgets(props);

        map = getMapBean();
        if (map == null){
            map = new BufferedMapBean();
        }

        controls = getToolPanel();
        //  If controls aren't set, seems silly to try and set an
        //  empty default.

        info = getInformationDelegator();
        if (info == null) {
            info = new InformationDelegator();
            // InformationDelegator handles the display of popup text/html
            // information.
            info.setMap(map);
            info.setFloatable(false);
        }

        menu = getMenuPanel();
        if (menu == null) {
            menu = new ScenarioMenuPanel(this);
        }

        // add the menu
        rootPane.setJMenuBar(menu);

        final JLayeredPane desktop = rootPane.getLayeredPane();
        desktop.setOpaque(true);

        layerHandler = getLayerHandler();
        if (layerHandler == null){
            layerHandler = new LayerHandler("scenariomap", props);
        } else {
            layerHandler.init("scenariomap", props);
        }

        findPspIconLayer().setScenerioMap(this);

          menu.add(layerHandler.getLayersMenu("Edit Layers..."));


        JMenu viewMenu =  new JMenu("View");
        viewMenu.setMnemonic('V');
        menu.add(viewMenu);

        JMenuItem menuItem = viewMenu.add(new JMenuItem("All Unit Visibility"));
        menuItem.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              findPspIconLayer().showIconDialog();
            }
          });

        menuItem = viewMenu.add(new JMenuItem("Selected Unit Visibility"));
        menuItem.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              findPspIconLayer().showSelectionDialog(map);
            }
          });



        sliderDialog = new JDialog(frame, "Set Slider Range", true);
        sliderDialog.setResizable(false);
        sliderDialog.getContentPane().setLayout(new BorderLayout());
        sliderDialog.getContentPane().add(getSliderDialogPanel(), BorderLayout.CENTER);
        sliderDialog.pack();
        menuItem = viewMenu.add(new JMenuItem("Slider Range"));
        menuItem.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              sliderDialog.setLocationRelativeTo(frame);

              GregorianCalendar cal = new GregorianCalendar();
              Date date = new Date();

              date.setTime(((long)rangeSlider.getMinValue())*1000L);
              cal.setTime(date);
              monthField1.setText("" + (cal.get(Calendar.MONTH)+1));
              dayField1.setText("" + cal.get(Calendar.DAY_OF_MONTH));
              yearField1.setText("" + cal.get(Calendar.YEAR));

              date.setTime(((long)rangeSlider.getMaxValue())*1000L);
              cal.setTime(date);
              monthField2.setText("" + (cal.get(Calendar.MONTH)+1));
              dayField2.setText("" + cal.get(Calendar.DAY_OF_MONTH));
              yearField2.setText("" + cal.get(Calendar.YEAR));

              sliderDialog.show();
            }
          });


        menuItem = (JCheckBoxMenuItem)viewMenu.add(new JCheckBoxMenuItem("Use C-Date", false));
        menuItem.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              rangeSlider.setUseCDate(((JCheckBoxMenuItem)e.getSource()).isSelected());
            }
          });


        cDateDialog = new JDialog((JFrame)null, "Set C-Date", true);
        cDateDialog.setResizable(false);
        cDateDialog.getContentPane().setLayout(new BorderLayout());
        cDateDialog.getContentPane().add(getCDateDialogPanel(), BorderLayout.CENTER);
        cDateDialog.pack();
        menuItem = viewMenu.add(new JMenuItem("Enter C-Date"));
        menuItem.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              sliderDialog.setLocationRelativeTo(frame);

              GregorianCalendar cal = new GregorianCalendar();
              Date date = new Date();

              date.setTime(rangeSlider.getCDate());
              cal.setTime(date);
              monthField.setText("" + (cal.get(Calendar.MONTH)+1));
              dayField.setText("" + cal.get(Calendar.DAY_OF_MONTH));
              yearField.setText("" + cal.get(Calendar.YEAR));

              cDateDialog.show();
            }
          });


        menuItem = viewMenu.add(new JMenuItem("Icon +200%"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
              findPspIconLayer().changeIconScale (2.0f);
            }
        });

        menuItem = viewMenu.add(new JMenuItem("Icon -200%"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
              findPspIconLayer().changeIconScale (-2.0f);
            }
        });

        menuItem = viewMenu.add(new JMenuItem("Clear Route Displays"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
              findPspIconLayer().clearAllRouteGraphics();
            }
        });


        // reuse theme and look and feel selection menus from CFrame
        if (frame instanceof CFrame)
        {
            CFrame cFrame = (CFrame)frame;
            menu.add(cFrame.getLookAndFeelPulldown());
            menu.add(cFrame.getThemesPulldown());
        }

        //
        // Remote Node pulldown
        //

        JMenu remoteMenu =  new JMenu("Remote Node");
        remoteMenu.setMnemonic('R');
        menu.add(remoteMenu);

        changeLInfoDialog = new JDialog(frame, "LocInfo Node", true);
        changeLInfoDialog.setResizable(false);
        changeLInfoDialog.getContentPane().setLayout(new BorderLayout());
        changeLInfoDialog.getContentPane().add(getChangeLINodePanel(), BorderLayout.CENTER);
        changeLInfoDialog.pack();



        JMenuItem chgli = remoteMenu.add(new JMenuItem("Change Location Info Node"));
        chgli.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
              changeLInfoDialog.setLocationRelativeTo(frame);

              changeLInfoDialog.show();

            }
        });
        chgli.setActionCommand("changeLINode");


        //
        // Add a debug menu to the menubar.
        //

        JMenu debugMenu =  new JMenu("Debug");
        debugMenu.setMnemonic('D');
        JMenuItem miloc = debugMenu.add(new JMenuItem("Dump Locations"));
        miloc.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
              String command = e.getActionCommand();
              PspIconLayer pl = findPspIconLayer();
              Hashtable mlih = pl.myState.mostRecentlyLoaded;
              dumpToOutput (mlih, LOCATIONS);
            }
        });
        miloc.setActionCommand("dumpLocs");

        JMenuItem mirel = debugMenu.add(new JMenuItem("Dump Relationships"));
        mirel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                String command = e.getActionCommand();
                PspIconLayer pl = findPspIconLayer();
                Hashtable mlih = pl.myState.mostRecentlyLoaded;
                dumpToOutput (mlih, RELATIONSHIPS);
            }
        });
        miloc.setActionCommand("dumpRels");

        menu.add(debugMenu);

        // Add an extra help menu to the menubar.  Have the InformationDelegator
        // show the help pages.
        JMenu helpMenu =  new JMenu("Help");
        helpMenu.setMnemonic('H');
        JMenuItem mi = helpMenu.add(new JMenuItem("OpenMap"));
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                info.displayURL(Environment.get(Environment.HelpURL,
                "http://javamap.bbn.com/projects/openmap/openmap_maindes.html"));
            }
        });
        mi.setActionCommand("showHelp");
        //menu.setHelpMenu(helpMenu);Use this when Sun decides to implement it
        menu.add(helpMenu);

        addToolPanelControls() ;

        // Add the toolbar to the top of the window
        if (controls != null)
            rootPane.getContentPane().add("North", controls);

        // Initialize the map projection, scale, center with user prefs or
        // defaults
        String projName = Environment.get(Environment.Projection, Mercator.MercatorName);
        int projType = ProjectionFactory.getProjType(projName);
        map.setProjectionType(projType);
        map.setScale(Environment.getFloat(Environment.Scale, Float.POSITIVE_INFINITY));
        map.setCenter(new LatLonPoint(
                Environment.getFloat(Environment.Latitude, 0f),
                Environment.getFloat(Environment.Longitude, 0f)
                ));

        // Add the map and then the status line to the window
        rootPane.getContentPane().add("Center", map);
        rootPane.getContentPane().add("South", info);

        // Make it look a little prettier
        if (Environment.isApplication()) {
            map.setBorder(new BevelBorder(BevelBorder.LOWERED));
        } else {
            // hack to work around a java bug with borders in applets
            map.setBorder(BorderFactory.createMatteBorder(2, //top
                                                          2, //left
                                                          2, //bottom
                                                          2, //right
                                                          Color.gray));
        }

        // show the window
        if (Environment.isApplication()) {
            // get starting width and height
            int w = Integer.parseInt(Environment.get(Environment.Width, "640"));
            int h = Integer.parseInt(Environment.get(Environment.Height, "480"));

            // get starting x and y position.  default to center of
            // screen.
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            int x = Environment.getInteger(xProperty, -1);
            int y = Environment.getInteger(yProperty, -1);
            if (x < 0)
                x = d.width/2 - w/2;
            if (y < 0)
                y = d.height/2 -h/2;

            // compose the frame, but don't show it here
            frame.setBounds(x, y, w, h);
            frame.setVisible(showUponInit);
        }

        // set up the listeners
        // map listens for LayerEvents
        layerHandler.addLayerListener(map);
        menu.setupListeners(map);
    }

  private JPanel getSliderDialogPanel()
  {

    JPanel panel = new JPanel ( new GridLayout(3, 4) );
    JButton button = new JButton("Set Range");

    button.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            int month1 = Integer.parseInt(monthField1.getText());
            int day1 = Integer.parseInt(dayField1.getText());
            int year1 = Integer.parseInt(yearField1.getText());


            int month2 = Integer.parseInt(monthField2.getText());
            int day2 = Integer.parseInt(dayField2.getText());
            int year2 = Integer.parseInt(yearField2.getText());

            long time1 = dateToMillis(month1+"/"+day1+"/"+year1, ((long)rangeSlider.getMinValue()) * 1000L)/1000L;
            long time2 = dateToMillis(month2+"/"+day2+"/"+year2, ((long)rangeSlider.getMaxValue()) * 1000L)/1000L;

            rangeSlider.setSliderRange(time1, time2);
            rangeSlider.setValue(rangeSlider.getValue());

            findPspIconLayer().myState.buildDailyNLUnits(((long)rangeSlider.getMinValue()) * 1000L, ((long)rangeSlider.getMaxValue()) * 1000L);
            findPspIconLayer().setTime("" + (((long)rangeSlider.getValue()) * 1000L));
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }

          sliderDialog.hide();
        }
      });

    panel.add(new JLabel(" ", SwingConstants.CENTER));
    panel.add(new JLabel("Month", SwingConstants.CENTER));
    panel.add(new JLabel("Day", SwingConstants.CENTER));
    panel.add(new JLabel("Year", SwingConstants.CENTER));

    panel.add(new JLabel("Starting Date"));
    panel.add(monthField1);
    panel.add(dayField1);
    panel.add(yearField1);

    panel.add(new JLabel("Ending Date"));
    panel.add(monthField2);
    panel.add(dayField2);
    panel.add(yearField2);

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BorderLayout());
    panel2.add(panel, BorderLayout.CENTER);
    panel2.add(button, BorderLayout.SOUTH);

    return(panel2);
  }

  private long dateToMillis(String dateString, long defaultTime)
  {
    long time = defaultTime;
    
    try
    {
      int month = 0;
      int day = 0;
      int year = 0;
      
      StringTokenizer tokenizer = new StringTokenizer(dateString.trim(), "/", false);
      
      month = Integer.parseInt(tokenizer.nextToken()) -1;
      day = Integer.parseInt(tokenizer.nextToken());
      year = Integer.parseInt(tokenizer.nextToken());
      
      GregorianCalendar cal = new GregorianCalendar();
      cal.set(year, month, day);
      
      time = cal.getTime().getTime();
    }
    catch (Exception e)
    {
      System.err.println("Unable to convert date to millis: " + e);
    }
    
    return(time);
  }


  private JPanel getCDateDialogPanel()
  {
    JPanel panel = new JPanel(new GridLayout(2, 3));
    JButton button = new JButton("Set C-Date");

    button.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            int month = Integer.parseInt(monthField.getText());
            int day = Integer.parseInt(dayField.getText());
            int year = Integer.parseInt(yearField.getText());

            GregorianCalendar cal = new GregorianCalendar();
            cal.set(year, month-1, day, 0, 0, 0);

            rangeSlider.setCDate(cal.getTime().getTime());
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }

          cDateDialog.hide();
        }
      });

    panel.add(new JLabel("Month", SwingConstants.CENTER));
    panel.add(new JLabel("Day", SwingConstants.CENTER));
    panel.add(new JLabel("Year", SwingConstants.CENTER));

    panel.add(monthField);
    panel.add(dayField);
    panel.add(yearField);

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BorderLayout());
    panel2.add(panel, BorderLayout.CENTER);
    panel2.add(button, BorderLayout.SOUTH);

    return(panel2);
  }


  private JPanel getChangeLINodePanel()
  {
    JPanel panel = new JPanel(new GridLayout(3, 4));
    JButton button = new JButton("OK");

    button.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            String urlText = urlField.getText();
            // tell the psp icon keep alive about the new location
            findPspIconLayer().myState.changeLocationInfoNode(urlText);
           }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }

          changeLInfoDialog.hide();
        }
      });

    panel.add(new JLabel(" ", SwingConstants.CENTER));
    panel.add(new JLabel("Change Location Info Node", SwingConstants.CENTER));

    panel.add(new JLabel("New URI"));
    panel.add(urlField);
    panel.add(button,BorderLayout.SOUTH);
    
    return(panel);
  }

    public long getCurrentTime()
    {
      return(((long)rangeSlider.getValue()) * 1000L);
    }


    public void setWidgets()
    {
      setWidgets(new Properties());
    }

    void addToolPanelControls() {
        ToolPanel tp = getToolPanel();

        // IconScale
        ActionListener ispl = new ActionListener()
        {
              public void actionPerformed(java.awt.event.ActionEvent e)
              {
                String command = e.getActionCommand();

                if (command.equals(IconScalePanel.scaleUpCmd))
                {
                  findPspIconLayer().changeIconScale (2.0f);
                }
	              else if (command.equals(IconScalePanel.scaleDownCmd))
                {
                  findPspIconLayer().changeIconScale (-2.0f);
                }
              }
          };

        IconScalePanel isp = new IconScalePanel (ispl);
        omts.add (isp);


        String[] choices = {"Eritrea", "Pakistan", "CONUS", "World"};
        ActionListener cbl = new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            JComboBox combo=(JComboBox)e.getSource();
            String newValue = (String)combo.getSelectedItem();
            setMapDisplayFor(newValue);
          }
        };

        CTitledComboBox mycb = new CTitledComboBox("Jump to", choices, cbl);
        omts.add(mycb);


    //        ================================
    /* add this in for timesControl    */
    JPanel timePanel = new JPanel();
    timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.X_AXIS));

//    long currSeconds = System.currentTimeMillis() / 1000;
//    long endSeconds = currSeconds + (30 * 24 * 60 * 60);

    long currSeconds = findPspIconLayer().initialStart / 1000L;
    long endSeconds = findPspIconLayer().initialEnd / 1000L;

    rangeSlider = new CDateLabeledSlider ( "Date", 10, (float) currSeconds, (float) endSeconds );

    rangeSlider.setValue(currSeconds);
    rangeSlider.setSliderRange(currSeconds, endSeconds);
 
    rangeSlider.setCDate(findPspIconLayer().initialCDate);

    rangeSlider.setLabelWidth(rangeSlider.getMinimumLabelWidth(rangeSlider.getLabel()));
    rangeSlider.setShowTicks(true);
    rangeSlider.setPreferredSize(new Dimension(350, rangeSlider.getPreferredSize().height));

    findPspIconLayer().myState.buildDailyNLUnits((long)rangeSlider.getMinValue() * 1000L, (long)rangeSlider.getMaxValue() * 1000L);

    timePanel.add((CLabeledSlider)rangeSlider);

    timePanel.add(Box.createHorizontalStrut(5));
    Dimension buttonSize = new Dimension(20, 15);
    Insets buttonInsets = new Insets(0, 0, 0, 0);

    JButton stepDownButton = new JButton("<");
    stepDownButton.setMargin(buttonInsets);
    stepDownButton.setPreferredSize(buttonSize);
    timePanel.add(stepDownButton);

    JButton stepUpButton = new JButton(">");
    stepUpButton.setMargin(buttonInsets);
    stepUpButton.setPreferredSize(buttonSize);
    timePanel.add(stepUpButton);

    timePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Time"));

    updateTime (currSeconds);

    stepDownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                rangeSlider.setValue(rangeSlider.getValue() - (60 * 60 * 24) );
            }
        });

    stepUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                rangeSlider.setValue(rangeSlider.getValue() + (60 * 60 * 24));
            }
        });

    // For constant updating as slider is adjusted
    rangeSlider.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                long newValue = ((Number)e.getNewValue()).longValue();
                updateTime( newValue );
            }
        });


    omts.add(timePanel);

     PspIconLayerBase pib = findPspIconLayerBase();
    pib.setTimeSlider(rangeSlider);

    //        ================================

   /*  End of times control */


    tp.add(omts);
    tp.setFloatable(false);// cannot detach
    setToolPanel(tp);
    }
   
    private void updateTime(long newSecs)
    {

      long newMillis = newSecs * 1000;
      PspIconLayer myLayer=findPspIconLayer();
//        System.out.println ("update for milliseconds " + newMillis);
      if (myLayer!=null)
      {
//        System.err.println("new time: " + newValue);
        myLayer.setTime(String.valueOf(newMillis));

        MapBean mymap = getMapBean();
        mymap.setScale(mymap.getScale()); // make openmap redraw
      }

      else
      {
        System.err.println("cannot set time on layer -- myLayer is null");
      }
    }

    private PspIconLayerBase findPspIconLayerBase()
    {

      LayerHandler layerHandler = getLayerHandler();
      Layer[] layers=layerHandler.getLayers();
      PspIconLayerBase myLayer=null;
      int idx;

      for(idx=0; idx<layers.length; idx++)
      {
        if (layers[idx] instanceof PspIconLayerBase)
        {
          myLayer=(PspIconLayerBase)layers[idx];
          break;
        }
      }

      return myLayer;
    }

    public PspIconLayer findPspIconLayer()
    {

      LayerHandler layerHandler = getLayerHandler();
      Layer[] layers=layerHandler.getLayers();
      PspIconLayer myLayer=null;
      int idx;

      for(idx=0; idx<layers.length; idx++)
      {
        if (layers[idx] instanceof PspIconLayer)
        {
          myLayer=(PspIconLayer)layers[idx];
          break;
        }
      }

      return myLayer;
    }

    void setMapDisplayFor(String location) {
        float lat=15, lon=39, zoom=3500000;
        if (location.equalsIgnoreCase("Eritrea")) {
            lat=15.5f;
            lon=39.5f;
            zoom=4000000f;
        } else if (location.equalsIgnoreCase("Pakistan")) {
            lat=30.2f;
            lon=70.2f;
            zoom=9000000f;
        } else if (location.equalsIgnoreCase("CONUS")) {
            lat=40f;
            lon=-100f;
            zoom=25000000f;
        } else if (location.equalsIgnoreCase("World")) {
            lat=0f;
            lon=0f;
            zoom=165000000f;
        } else {
            System.err.println("Do not know about location: "+location);
            location="Default location";
        }
        MapBean mymap = getMapBean();
        mymap.setScale(zoom);
        mymap.setCenter(new LatLonPoint(lat,lon));
        // System.out.println("Move to " +location);

    }

    /**
     * Sets the type of gui components to be used in the OpenMap
     * application.  You are responsible for hooking them up here,
     * too, if you want them to communication with each other.
     *
     * @param props the Properties object created from the
     * scenariomap.properties file.
     */
     
    public static DnDMouseDelegator md = null;

    public void setWidgets(Properties props){
        MapBean map = new BufferedMapBean();
        setMapBean(map);

        // MouseDelegator multiplexes the mouse input to layers and beans
        // which are MapMouseListeners
//        MouseDelegator md = new MouseDelegator(map);
        md = new DnDMouseDelegator(map);
        md.setDefaultMouseModes();
//        md.addMouseMode(new DragAndDropMouseMode());

        setLayerHandler(new LayerHandler());
        ToolPanel tp = new ToolPanel();
        omts = new ScenarioOMToolSet();
        omts.setupListeners(map); // Hook up the map as a listener
        omts.addLayerPanelButton(getLayerHandler());
        omts.addOverviewMapButton(map, props);
        omts.addMouseModes(md);


        tp.add(omts);
        tp.setFloatable(false);// cannot detach
        setToolPanel(tp);

        InformationDelegator id = new InformationDelegator();
        // InformationDelegator handles the display of popup text/html
        // information.
        id.setMap(map);
        id.setFloatable(false);

        // Removed the following to make this compatible with OM v3.7
        // Will probably put it back when upgrading to OM 4.0
//         DistanceMouseMode distMode =
//             new DistanceMouseMode(true, id, DistanceMouseMode.DISTANCE_MILE);
//         // Add the distance mouse mode to the mouse delegator
//         md.addMouseMode(distMode);

        // Get the wholine to not update lat lons if something else
        // is displayed.
        md.addPropertyChangeListener(id);
        setInformationDelegator(id);

        ScenarioMenuPanel mp = new ScenarioMenuPanel(this);
        mp.setMouseDelegator(md);
        setMenuPanel(mp);
    }

    /**
     * Set the MapBean to be this map.  This should be called in the
     * setWidgets() method, if you want to use some MapBean other than
     * the BufferedMapBean.
     * @param newMap the MapBean to use.
     */
    protected void setMapBean(MapBean newMap){
        map = newMap;
    }

    /**
     * Get the MapBean to use - called in init(), in case you wanted
     * to use something slightly different.
     * @return MapBean
     */
    protected MapBean getMapBean(){
        return map;
    }

    /**
     * Set the ToolPanel to use.  This should be called in the
     * setWidgets() method, if you want to use some ToolPanel other than
     * the regular ToolPanel.
     * @param newControls the tool panel to use.
     */
    protected void setToolPanel(ToolPanel newControls){
        controls = newControls;
    }

    /**
     * Get the tool panel to Use - called in init(), in case you
     * wanted to use something slightly different.
     */
    protected ToolPanel getToolPanel(){
        return controls;
    }

    /**
     * Set the Information Delegator to use.  This should be called in the
     * setWidgets() method, if you want to use something other than
     * the regular InformationDelegator.
     * @param newInfo the information delegator to use.
     */
    protected void setInformationDelegator(InformationDelegator newInfo){
        info = newInfo;
    }

    /**
     * Get the information delegator to use - called in init(), in
     * case you wanted to use something slightly different.
     * @return InformationDelegator
     */
    protected InformationDelegator getInformationDelegator(){
        return info;
    }

    /**
     * Set the Menus to use.  This should be called in the
     * setWidgets() method, if you want to use something other than
     * the regular set of menus..
     * @param newMenu the menus to use.
     */
    protected void setMenuPanel(ScenarioMenuPanel newMenu){
        menu = newMenu;
    }

    /**
     * Get the menus to use - called in init(), in case you wanted
     * to use something slightly different.
     * @return MenuPanel
     */
    protected ScenarioMenuPanel getMenuPanel(){
        return menu;
    }

    /**
     * Set the layer handler for the cale - called in init(), in case
     * you want something different.
     * @param newLayerHandler
     */
    public void setLayerHandler(LayerHandler newLayerHandler)
    {
      layerHandler = newLayerHandler;
    }


    /**
     * Get the layer handler to use.
     * @return LayerHandler
     */
    public LayerHandler getLayerHandler()
    {
      return layerHandler;
    }


    private void dumpToOutput (Hashtable ht, int flag)
    {
    
//      System.out.println ("The raw hashtable \n\t" + ht);
      for (Enumeration eht = ht.keys(); eht.hasMoreElements(); )
      {
        String cid = (String) eht.nextElement();

        MapLocationInfo mli = (MapLocationInfo) ht.get(cid);

        if (flag == RELATIONSHIPS)
        {
           System.out.println ("\nRelationships received from LocationCluster");
           System.out.println (cid);
           Vector relVec = mli.getRelationshipSchedule();
           for (int ii = 0; ii < relVec.size(); ii ++)
           {
             System.out.println ("\t" + relVec.get(ii) );
           }
           System.out.println (" ");
        }
        else if (flag == LOCATIONS)
        {
           System.out.println ("\nLocations received from LocationCluster");
           System.out.println (cid);
           Vector schedVec = mli.getScheduleElements();
           for (int ii = 0; ii < schedVec.size(); ii ++)
           {
             LocationScheduleElement locSched = (LocationScheduleElement) schedVec.get(ii);
             GeolocLocationImpl loc = (GeolocLocationImpl) locSched.getLocation();
             Latitude lat = loc.getLatitude();
             Longitude lon = loc.getLongitude();

             System.out.println ("\tstart time \t" + locSched.getStartDate().toString());
             System.out.println ("\tlat lon    \t" + lat.getDegrees() + " " + lon.getDegrees() );
             System.out.println ("\tend time   \t" + locSched.getEndDate().toString() + "\n");
           }
        }
      }

   }
}

