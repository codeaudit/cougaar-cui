/* **********************************************************************
 * 
 *    Use, duplication, or disclosure by the Government is subject to
 * 	     restricted rights as set forth in the DFARS.
 *  
 * 			   BBNT Solutions LLC
 * 			      A Part of  
 * 			         GTE      
 * 			  10 Moulton Street
 * 			 Cambridge, MA 02138
 * 			    (617) 873-3000
 *  
 * 	  Copyright 1998, 2000 by BBNT Solutions LLC,
 * 		A part of GTE, all rights reserved.
 *  
 * **********************************************************************
 * 
 * $Source: /opt/rep/cougaar/cui/uiframework/src/org/cougaar/lib/uiframework/ui/map/app/Attic/OpenMap.java,v $
 * $Revision: 1.1 $
 * $Date: 2001-02-15 13:22:34 $
 * $Author: krotherm $
 * 
 * ***********************************************************************/

package org.cougaar.lib.uiframework.ui.map.app;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.beans.Beans;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import com.bbn.openmap.MouseDelegator;
import com.bbn.openmap.InformationDelegator;
import com.bbn.openmap.Environment;
import com.bbn.openmap.LatLonPoint;
import com.bbn.openmap.Layer;
import com.bbn.openmap.MapBean;
import com.bbn.openmap.BufferedMapBean;
import com.bbn.openmap.event.*;
import com.bbn.openmap.gui.*;
import com.bbn.openmap.proj.*;
import com.bbn.openmap.util.Debug;
// import assessment.*;
import org.cougaar.lib.uiframework.ui.map.util.*; // only for JTIButton, etc.
import javax.swing.border.*;

import org.cougaar.lib.uiframework.ui.map.layer.TimedXmlLayer;
import org.cougaar.lib.uiframework.ui.map.layer.XmlLayer;
import org.cougaar.lib.uiframework.ui.map.layer.XmlLayerBase;
//import org.cougaar.lib.uiframework.ui.map.layer.ATestLayer;
//import org.cougaar.lib.uiframework.ui.map.layer.KMTLayer;

/**
 * The OpenMap Viewer application.
 * <p>
 * This is a sample application using the MapBean.
 *
 */
public class OpenMap implements Serializable {

    /** The name of the properties file to read. */
    public static String propsFileName = "openmap.properties";

    /** The name of the system directory containing a properties file. */
    public static String configDirProperty = "openmap.configDir";

    /** Starting X coordinate of window */
    public static transient final String xProperty = "openmap.x";

    /** Starting Y coordinate of window */
    public static transient final String yProperty = "openmap.y";

    /** The application properties. */
    protected Properties props;

    /** Map Window */
    protected MapBean map;
    /** The suite of control widgets. */
    protected ToolPanel controls;
    /** The Information and Status Manager. */
    protected InformationDelegator info;
    /** The suite of menus. */
    protected MenuPanel menu;
    /** The layer handler, for dynamic adjustments of layers. */
    protected LayerHandler layerHandler;

    protected OMToolSet omts;

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
		Debug.error("OpenMap: Unable to locate resources: "
			    + resourceName);
	    }
	    return false;

	} else {

	    try {
		properties.load(propsIn);
		return true;
	    } catch (java.io.IOException e) {
		if (verbose) {
		    Debug.error("OpenMap: Caught IOException loading resources: "
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
		Debug.error("OpenMap Caught IOException loading resources: "
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
        System.out.println("\n\nproperties in loadProps(props, "+dir+","+file+": ");
        System.out.println(props);
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
     * Read properties from the openmap installion, if they exist.
     * This is usually <openmapInstallDir>/openmap.properties.
     * </li>
     * <li>
     * Read user properties from $HOME/openmap.properties.  This is
     * based on the JDK system property <code>user.home</code>
     * </li>
     * </ol>
     * @param loadProperties true if you want to look in
     * certain locations for the openmap.properties file, overwriting
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
     * Initialize the OpenMap Environment.
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
     * Start OpenMap Viewer as a standalone application.
     *
     * @param args String[] curently ignored
     */
    public static void main_prev (String[] args) {

	// static initializations of Debugging and Environment
	init(null);
	// start instance of OpenMap
	new OpenMap().init();
    }



    public static void main (String[] args) {
      System.out.println("alp openmap") ;  
	    // static initializations of Debugging and Environment
	    init(null);
	    // start instance of OpenMap
	    new OpenMap().init();

    }


    public static OpenMap generateMapBean(JPanel panel) 
    {
	OpenMap om;
	init(null);
	om=new OpenMap();
	om.initApplication(true);
	om.prepareMap(panel);  // similar to start()
	
	return om;
    }

    public void prepareMap (JPanel panel){

	// JFrame frame = null;
	//	JRootPane rootPane = null;
	boolean addLayerButton = true;
	boolean addOverviewButton = true;


	if (Debug.debugging("version")){
	// output version information
	    Debug.output("OpenMap Viewer " + 
			 Environment.get(Environment.Version));
	    Debug.output("Build " + 
			 Environment.get(Environment.BuildDate, "<no build tag>"));
	    Debug.output("OpenMap Viewer running inside " +
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
	    menu = new MenuPanel();
	}

	// add the menu
	// no rootpane -- add in again later 
	// rootPane.setJMenuBar(menu);

	// no rootpane -- add in again later 
        //final JLayeredPane desktop = rootPane.getLayeredPane();
	//desktop.setOpaque(true);

	layerHandler = getLayerHandler();
	if (layerHandler == null){
	    layerHandler = new LayerHandler("openmap", props);
	} else {
	    layerHandler.init("openmap", props);
	}

  	menu.add(layerHandler.getLayersMenu("Edit Layers..."));

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
    }

    // end -- kwr
    public OpenMap () {
	props = new Properties();
    }

    /**
     * Load default properties, and then launch OpenMap.  Kept here to
     * preserve the API.
     */
    public void init () {
	initApplication(true);
	start();
    }

    /**
     * An init() that gives the option of whether or not to have the
     * application search for the openmap.properties file located in
     * the openmap.jar file, or in the user's home directory, or in
     * the CLASSPATH.  Set the argument to false if you are setting
     * the properties (for layers, startup projection, etc) manually,
     * and don't want your values overwritten by the default values in
     * an openmap.properties file.
     * @param loadResourceProperties false if you've set the
     * properties manually, true if you want the properties to be set
     * off the openmap.properties file.
     */
    public void init (boolean loadResourceProperties) {

	initApplication(loadResourceProperties);
	start();
    }

    /** 
     * Launch OpenMap, assuming that all the properties have been set.
     */

    // JRootPane rootPane = null;

    public void startApplet (){
	
	JFrame frame = null;
	JRootPane rootPane = null;
	boolean addLayerButton = true;
	boolean addOverviewButton = true;
	rootPane = ((JApplet)(Environment.getApplet())).getRootPane();

	if (Debug.debugging("version")){
	    // output version information
	    Debug.output("OpenMap Viewer " + 
			 Environment.get(Environment.Version));
	    Debug.output("Build " + 
			 Environment.get(Environment.BuildDate, "<no build tag>"));
	    Debug.output("OpenMap Viewer running inside " +
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
	    menu = new MenuPanel();
	}

	// add the menu
	rootPane.setJMenuBar(menu);

        final JLayeredPane desktop = rootPane.getLayeredPane();
	desktop.setOpaque(true);

	layerHandler = getLayerHandler();
	if (layerHandler == null){
	    layerHandler = new LayerHandler("openmap", props);
	} else {
	    layerHandler.init("openmap", props);
	}

  	menu.add(layerHandler.getLayersMenu("Edit Layers..."));

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
	    frame.show();
	}

	// set up the listeners
	// map listens for LayerEvents
	layerHandler.addLayerListener(map);
	menu.setupListeners(map);

    }

    public void start (){

	JFrame frame = null;
	JRootPane rootPane = null;
	boolean addLayerButton = true;
	boolean addOverviewButton = true;

	// get the Root window
	if (Environment.isApplication()) {
	    frame = new JFrame(Environment.get(Environment.Title));
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
	    Debug.output("OpenMap Viewer " + 
			 Environment.get(Environment.Version));
	    Debug.output("Build " + 
			 Environment.get(Environment.BuildDate, "<no build tag>"));
	    Debug.output("OpenMap Viewer running inside " +
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
	    menu = new MenuPanel();
	}

	// add the menu
	rootPane.setJMenuBar(menu);

        final JLayeredPane desktop = rootPane.getLayeredPane();
	desktop.setOpaque(true);

	layerHandler = getLayerHandler();
	if (layerHandler == null){
	    layerHandler = new LayerHandler("openmap", props);
	} else {
	    layerHandler.init("openmap", props);
	}

  	menu.add(layerHandler.getLayersMenu("Edit Layers..."));

    ThemesMenu tmenu=new ThemesMenu(frame);
    menu.add(tmenu);

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
	    frame.show();
	}

	// set up the listeners
	// map listens for LayerEvents
	layerHandler.addLayerListener(map);
	menu.setupListeners(map);
    }

    public void setWidgets(){
	setWidgets(new Properties());
    }

    void addToolPanelControls() {
	ToolPanel tp = getToolPanel();
	
	String[] choices = {"Eritrea", "Pakistan", "World"};
	ActionListener cbl = new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
		    JComboBox combo=(JComboBox)e.getSource();
		    String newValue =
			(String)combo.getSelectedItem();
		    setMapDisplayFor(newValue);
		}
	    };
	
	TitledComboBox mycb = new TitledComboBox("Jump to", choices, cbl);
	omts.add(mycb);
    //	================================
    /* add this in for timesControl    */

    String[] choices2 = {TimedXmlLayer.EPOCH
                          , "100"
                          , "125"
                          , "150"
                          , "175"
                          , "200"
                          , "225"
                          , "250"
                          , "275"
                          , "300"
                          , "325"
                          , "350"
                          , "375"
                          , "400"
                          , TimedXmlLayer.LAST_TIME};
    ActionListener cbl2 = new ActionListener() {
	public void actionPerformed(ActionEvent e)
	    {
		JComboBox combo=(JComboBox)e.getSource();
		String newValue =
		    (String)combo.getSelectedItem();
 		LayerHandler layerHandler = getLayerHandler();
 		Layer[] layers=layerHandler.getLayers();
 		TimedXmlLayer myLayer=null;
 		int idx;
 		for(idx=0; idx<layers.length; idx++) {
      //System.out.println("layer #"+idx+": "+layers[idx].getClass().getName());
 		    if (layers[idx] instanceof TimedXmlLayer) {
 			myLayer=(TimedXmlLayer)layers[idx];
      //System.out.println("break");
 			break;
 		    }
 		}
 		if (myLayer!=null) {
 		    System.err.println("setting time on layer: "+myLayer);
 		    System.err.println("new time: "+newValue);
 		    myLayer.setTime(newValue);

        combo.removeAllItems();
        Collection transitionTimes=myLayer.getTransitionTimes();
        for (Iterator it=transitionTimes.iterator(); it.hasNext(); ) {
          Long ttime=(Long)it.next();
          String timeStr=""+ttime;
          if (ttime.longValue()==Long.MIN_VALUE) timeStr=TimedXmlLayer.EPOCH;
          else if (ttime.longValue()==Long.MAX_VALUE) timeStr=TimedXmlLayer.LAST_TIME;
          combo.addItem(timeStr);
          // System.out.println("add comboelement for time: "+ttime+": ");
        }

        combo.setSelectedItem(newValue);
 		    MapBean mymap = getMapBean();
        mymap.setScale(mymap.getScale());
 		} else {
 		    System.err.println("cannot set time on layer -- myLayer is null");
 		}
 		//System.out.println("jb colorcode " +e);
	    }
	};

    TitledComboBox mycb2 = new TitledComboBox("Time", choices2, cbl2);
    omts.add(mycb2);
    //	================================

   /*  End of times control */

    //* ================================

    final JTIButtonAction jtibaction = new JTIButtonAction() {
	    public void act(String sel) {
		System.err.println("jtibaction for selection: "+sel);
		String metricString;
    try {
      metricString="metric"+sel.substring(0,1);
    } catch (Exception e) {
      metricString="metric1";
    }
		System.err.println("jtibaction metricString: "+metricString);

		// get layer of interest (atestlayer)
		LayerHandler layerHandler = getLayerHandler();
		Layer[] layers=layerHandler.getLayers();
		//		ATestLayer myLayer=null;
		XmlLayer myxLayer=null;
		XmlLayerBase myxbLayer=null;
		int idx;
		for(idx=0; idx<layers.length; idx++) {
// 		    if (layers[idx] instanceof ATestLayer) {
// 			myLayer=(ATestLayer)layers[idx];
// 		    }
		    if (layers[idx] instanceof XmlLayer) {
			myxLayer=(XmlLayer)layers[idx];
		    }
		    if (layers[idx] instanceof XmlLayerBase) {
			myxbLayer=(XmlLayerBase)layers[idx];
		    }
		}
//		if (myLayer!=null||myxLayer!=null||myxbLayer!=null) {
		if (myxLayer!=null||myxbLayer!=null) {
// 		    if (myLayer!=null) {
// 			System.err.println("color coding on myLayer: "+myLayer);
// 			myLayer.colorCodeUnits(metricString);
// 		    }
		    if (myxLayer!=null) {
			System.err.println("color coding on myxLayer: "+myxLayer);
			myxLayer.colorCodeUnits(metricString);
		    }
		    if (myxbLayer!=null) {
			System.err.println("color coding on myxLayer: "+myxLayer);
			myxbLayer.colorCodeUnits(metricString);
		    }
		    MapBean mymap = getMapBean();
		    mymap.setScale(mymap.getScale());

		} else {
		    System.err.println("cannot colorcode -- myLayer is null");
		}
		System.out.println("jbit colorcode ");
	    }
	};
    String bfile=Environment.get("xml.metrics.menu.file", "\\dev\\ui\\menus\\jtmetrics.xml");
    System.out.println("bfile: "+bfile);
    JTIButton jtib=new JTIButton(bfile,
				 jtibaction);
    jtib.setLabel("Select A Metric");
    // jtib.setSize(400, jtib.getHeight()); // not having any effect
    JFrame jf = jtib.getMenuFrame();
    jf.setSize(400,200);
    jf.setLocation(610,120);
    jf.setTitle("Color Code for Metric");
    omts.add(jtib);
    

    tp.add(omts);
    tp.setFloatable(false);// cannot detach
    setToolPanel(tp);

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
	} else if (location.equalsIgnoreCase("World")) {
	    lat=0f;
	    lon=0f;
	    zoom=165000000f;
	} else {
	    System.err.println("Do no know about location: "+location);
	    location="Default location";
	}
	MapBean mymap = getMapBean();
	mymap.setScale(zoom);
	mymap.setCenter(new LatLonPoint(lat,lon));
	System.out.println("Move to " +location);

    }

    /**  
     * Sets the type of gui components to be used in the OpenMap
     * application.  You are responsible for hooking them up here,
     * too, if you want them to communication with each other.
     *
     * @param props the Properties object created from the
     * openmap.properties file.
     */
    public void setWidgets(Properties props){
	MapBean map = new BufferedMapBean();
	setMapBean(map);

	// MouseDelegator multiplexes the mouse input to layers and beans
	// which are MapMouseListeners
	MouseDelegator md = new MouseDelegator(map);
 	md.setDefaultMouseModes();

	setLayerHandler(new LayerHandler());
	ToolPanel tp = new ToolPanel();
	omts = new OMToolSet();
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

	DistanceMouseMode distMode = 
	    new DistanceMouseMode(true, id, DistanceMouseMode.DISTANCE_MILE);
	// Add the distance mouse mode to the mouse delegator
	md.addMouseMode(distMode);

	// Get the wholine to not update lat lons if something else
	// is displayed.
	md.addPropertyChangeListener(id);
	setInformationDelegator(id);

	MenuPanel mp = new MenuPanel();
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
    protected void setMenuPanel(MenuPanel newMenu){
	menu = newMenu;
    }

    /**
     * Get the menus to use - called in init(), in case you wanted
     * to use something slightly different.
     * @return MenuPanel
     */
    protected MenuPanel getMenuPanel(){
	return menu;
    }

    /** 
     * Set the layer handler for the cale - called in init(), in case
     * you want something different.  
     * @param newLayerHandler
     */
    public void setLayerHandler(LayerHandler newLayerHandler){
	layerHandler = newLayerHandler;
    }

    /**
     * Get the layer handler to use.
     * @return LayerHandler
     */
    public LayerHandler getLayerHandler(){
	return layerHandler;
    }
}


class TitledComboBox extends JPanel {
    JComboBox combo;
    TitledComboBox(String label, String[] choices, ActionListener cbl) {
	super(new FlowLayout());
	setBorder(BorderFactory
		  .createTitledBorder(BorderFactory
				      .createEtchedBorder(), label));
	

	combo = new JComboBox(choices);
	combo.setSelectedIndex(0);
	addActionListener(cbl);
	add(combo);
    }

    public void addActionListener(ActionListener al) {
	combo.addActionListener(al);
    }
}




 
