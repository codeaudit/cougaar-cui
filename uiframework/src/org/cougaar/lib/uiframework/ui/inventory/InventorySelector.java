/*
 * <copyright>
 *  Copyright 1997-2000 Defense Advanced Research Projects
 *  Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 *  Raytheon Systems Company (RSC) Consortium).
 *  This software to be used only in accordance with the
 *  COUGAAR licence agreement.
 * </copyright>
 */

package org.cougaar.lib.uiframework.ui.inventory;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.border.*;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JInternalFrame;

import java.net.URL;

import org.cougaar.util.OptionPane;

import org.cougaar.lib.uiframework.ui.components.CFrame;
import org.cougaar.lib.uiframework.ui.components.CSplitPane;

import org.cougaar.lib.uiframework.ui.components.CChartLegend;

import org.cougaar.lib.uiframework.ui.components.graph.*;
import org.cougaar.lib.uiframework.ui.models.*;
import org.cougaar.lib.uiframework.ui.util.CougaarUI;

import org.cougaar.domain.mlm.ui.data.UISimpleInventory;
import org.cougaar.domain.mlm.ui.planviewer.ConnectionHelper;
import org.cougaar.domain.mlm.ui.planviewer.XMLClientConfiguration;
import org.cougaar.domain.glm.execution.eg.ClusterInfo;

import org.cougaar.domain.mlm.ui.planviewer.inventory.InventoryExecutionTimeStatusHandler;
import org.cougaar.domain.mlm.ui.planviewer.inventory.InventoryExecutionListener;


public class InventorySelector implements CougaarUI
{
  private static final int FILL_PATTERN = -2;
  private static final int VISIBLE      = -1;
  private static final int BAR          = 0;
  private static final int FILLED_BAR   = 1;
  private static final int STEP         = 2;
  private static final int FILLED_STEP  = 3;
  private static final int LINE         = 4;
  private static final int FILLED_LINE  = 5;

//  Container container;
  //  south - control panel
  JPanel queryPanel = new JPanel();
  //  center pane - split pane
//  CSplitPane outerSplit = new CSplitPane(JSplitPane.VERTICAL_SPLIT);
//  CSplitPane innerSplit = new CSplitPane(JSplitPane.VERTICAL_SPLIT);
  JComboBox clusterNameBox = new JComboBox();
  JComboBox assetNameBox = new JComboBox();
  boolean doDisplayTable = true;
  boolean isApplet;
  boolean listFilled = false;
  boolean buildFile = true;
  String clusterHost = "localhost";
  String clusterPort = "5555";
  String SET_ASSET = "Set Asset";
  String SET_CLUSTER = "Set Cluster";
  String hostAndPort = null; // defaults to http://localhost:5555/
  String buttonFileText = "Read Clusters From File";
  String buttonPortText = "Read Clusters From Port";
  Hashtable clusterURLs;
  Hashtable clusterContainer = new Hashtable(1);
  Hashtable clusterData = null;
  Hashtable assetInventories = null;
  String clusterName; // set from code base if called as applet
  String fileName = null;
  String cacheFileName = "inventoryCache.inv";
  boolean useCache = false;
  int startParam = 0;
  int endParam = 0;
  static Object messageString = null;
  Container frame = null;
  Vector assetNames;
  DoQuery queryListener = new DoQuery();
  static final String PSP_id = "ALPINVENTORY.PSP";

  InventoryExecutionTimeStatusHandler timeStatusHandler=null;
  InventoryExecutionListener executionListener=null;

  private BlackJackInventoryChart chart = new BlackJackInventoryChart("", null, "Quantity", true);
  private CChartLegend legend = new CChartLegend();
  private JTable table = new JTable(new InventoryTableModel());
//  private JScrollPane tableScrollPane = new JScrollPane(table);
  private FileOutputStream ostream = null;
  private ObjectOutputStream objectOutputStream = null;

  private boolean fileBased = false;

  JCheckBoxMenuItem yRangeScrollerMI = null;
  JCheckBoxMenuItem yRangeTickLabelsMI = null;
  JCheckBoxMenuItem yRangeScrollLockMI = null;

  private JMenu inventoryChartMenu = null;
  private JMenu supplierChartMenu = null;
  private JMenu consumerChartMenu = null;
  private Vector dataSetList = null;

  private JLabel dataTipLabel = new JLabel(" ", SwingConstants.LEFT);

  private JDialog cDataDialog = null;
  private JTextField monthField = new JTextField();
  private JTextField dayField = new JTextField();
  private JTextField yearField = new JTextField();
  private String cluster = null;
  private String asset = null;

  public InventorySelector()
  {
  }

  public InventorySelector(URL codeBase, Container container)
  {

  }

  public InventorySelector(String host, String port, String file,
                           String incluster, String inasset, long sTime, long eTime)
  {
    hostAndPort = "http://" + host + ":" + port + "/";
    clusterHost = host;
    clusterPort = port;
    cluster = incluster;
    asset = inasset;
    TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    startParam = (int)sTime;
    endParam = (int)eTime;
    assetInventories = new Hashtable(1);

    if(file != null )
    {
      getFileData(file);
      fileBased = true;
    }

  }
  public void install(JFrame installFrame)
  {
    frame = installFrame;

    if (installFrame.getJMenuBar() == null)
    {
      installFrame.setJMenuBar(new JMenuBar());
    }

    buildControlPanel(installFrame.getContentPane(), installFrame.getJMenuBar());

    if (cluster != null)
    {
      loadInitialData (cluster, asset);
    }

    installFrame.addWindowListener(new WindowAdapter()
      {
        public void windowClosing(WindowEvent e)
        {
          if(buildFile)
            saveObject();
          System.exit(0);
        }
      });

    installFrame.show();

    installFrame.validate();
  }
  public void install(JInternalFrame installFrame)
  {
    frame = installFrame;

    if (installFrame.getJMenuBar() == null)
    {
      installFrame.setJMenuBar(new JMenuBar());
    }

    buildControlPanel(installFrame.getContentPane(), installFrame.getJMenuBar());

    if (cluster != null)
    {
      loadInitialData (cluster, asset);
    }

    installFrame.validate();
  }

  public boolean supportsPlaf()
  {
    return true;
  }

  public void populate(String newCluster, String newPort)
  {
    loadInitialData(newCluster, newPort);
  }

  public void getFileData(String filename)
  {
    ObjectInputStream in = null;
   try {
   in = new ObjectInputStream (new
                              FileInputStream(filename));
   }
   catch (Exception e)
   { //file not found or something wrong with opening it
    System.err.println("error opening  file"  +
                        filename );
    System.exit(1);
   }

   try{
    clusterData = (Hashtable) in.readObject();
    }
   catch (Exception e)
   {
    System.err.println("unable to read data object from " +
                        filename + e);
   }
    try {
       in.close();
    }
    catch (Exception g)
    {
      System.err.println("unable to close profile.dat  : " + g);
    }
  }


  public void buildControlPanel(Container contentPane, JMenuBar menuBar)
  {
    createMenuAndDialogs(contentPane, menuBar);
    JPanel container = new JPanel(new BorderLayout());
    queryPanel.setBorder(new LineBorder(Color.blue));
    assetNameBox.setPreferredSize(new Dimension(400,25));
    queryPanel.setLayout(new FlowLayout());
    queryPanel.add(new JLabel("Org"));


    contentPane.add(container, BorderLayout.CENTER);
    contentPane.add(queryPanel, BorderLayout.SOUTH);
//    tableScrollPane.setMinimumSize(new Dimension(10,10));

//    innerSplit.setOneTouchExpandable(true);
//    innerSplit.setTopComponent(chart);
    container.add(chart, BorderLayout.CENTER);
    container.add(dataTipLabel, BorderLayout.SOUTH);
    chart.setDataTipLabel(dataTipLabel);
//    innerSplit.setBottomComponent(tableScrollPane);

//    outerSplit.setOneTouchExpandable(true);
//    outerSplit.setTopComponent(innerSplit);
//    outerSplit.setBottomComponent(legend);


    legend.addPropertyChangeListener(chart);

    legend.setMinimumSize(new Dimension(0,0));
//    tableScrollPane.setMinimumSize(new Dimension(0,8));
    chart.setMinimumSize(new Dimension(0,0));

    GregorianCalendar cal = new GregorianCalendar();
    cal.set(Calendar.MILLISECOND, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.HOUR, 0);
    monthField.setText("" + (cal.get(Calendar.MONTH) + 1));
    dayField.setText("" + cal.get(Calendar.DAY_OF_MONTH));
    yearField.setText("" + cal.get(Calendar.YEAR));
    chart.setCDate(cal.getTime().getTime());

    chart.setMinimumSize(new Dimension(0,0));
    chart.setShowRightYAxis(false);
    chart.setShowYRangeScroller(false);

//    container.add(outerSplit, BorderLayout.CENTER);

    if(!fileBased)
      addClusterList();
    else
      addClustersFromHash();

    clusterNameBox.addActionListener(new FillAssetList());

    assetNameBox.addActionListener(queryListener);
    queryPanel.add(clusterNameBox);
    queryPanel.add(new JLabel("Items"));
    queryPanel.add(assetNameBox);


    queryPanel.setPreferredSize(new Dimension(20,50));
//    container.add(queryPanel, BorderLayout.SOUTH);

    contentPane.setSize(800,600);
  }

     public void updateInventoryBox()
     {
       assetNameBox.removeActionListener(queryListener);
       if(!fileBased)
       {
         if (assetNameBox.getItemCount() != 0)
           assetNameBox.removeAllItems();

         if (clusterNameBox != null)
            clusterName = (String)clusterNameBox.getSelectedItem();
         assetNames = getAssets(clusterName);
         if (assetNames != null)
            for (int i = 0; i < assetNames.size(); i++)
              assetNameBox.addItem(assetNames.elementAt(i));
         else
            System.err.println("assets are null");
       }
       else  //  all data coming from file object
       {
         if (assetNameBox.getItemCount() != 0)
           assetNameBox.removeAllItems();
         if (clusterNameBox == null)
         {
           return;
         }
         clusterName = (String) (clusterNameBox.getSelectedItem());
         if (clusterName != null)
         {
//           System.out.println("clusterName " + clusterName);
           Object assets = (Object) clusterData.get(clusterName);
           if(assets != null)
           {
             if(!assets.getClass().getName().equals("java.util.Hashtable"))
               return;
             Enumeration e = ((Hashtable)assets).keys();
             while(e.hasMoreElements())
             {
                String asset = (String) e.nextElement();
                assetNameBox.addItem(asset);
             }
           }
           else
           {
            JOptionPane.showMessageDialog(null, "No Data in File", "alert", JOptionPane.ERROR_MESSAGE);
           }
        }

       }
      assetNameBox.addActionListener(queryListener);
      return;
    }

    private void loadInitialData (String clusterName, String assetName)
    {
      clusterNameBox.setSelectedItem(clusterName);

      if (assetName != null)
      {
        assetNameBox.setSelectedItem(assetName);
      }
      else
      {
        assetNameBox.setSelectedIndex(0);
      }

    }


  /* Send request to the cluster to get the list of assets
     with scheduled content property groups.
     */

  private Vector getAssets(String clusterName)
  {
    //System.out.println("Submitting: ASSET to: " + clusterName +
    //                   " for: " + PSP_id);
    String clusterURL = (String)clusterURLs.get(clusterName);
    InputStream is = null;
    try
    {
      ConnectionHelper connection =
        new ConnectionHelper(clusterURL,
                             XMLClientConfiguration.PSP_package, PSP_id);
      connection.sendData("ASSET");
      is = connection.getInputStream();
    } catch (Exception e)
    {
      System.err.println("error getting assets");
      e.printStackTrace();
      return null;
    }
    Vector assetNames = null;
    try
    {
      ObjectInputStream p = new ObjectInputStream(is);
      assetNames = (Vector)p.readObject();
    } catch (Exception e)
    {
      //displayErrorString("Object read exception: " + e.toString());
      System.err.println("error getting asstenames from stream");
    }
    Collections.sort(assetNames);
    //System.out.println("assetsNames " + assetNames);
    return assetNames;
  }

  private void addClustersFromHash()
  {
    Enumeration e = clusterData.keys();
    clusterNameBox.removeAllItems();
//    System.out.println("clusterfromhash");
    while(e.hasMoreElements())
    {
      String clusterName = (String)e.nextElement();
      clusterNameBox.addItem(clusterName);
//      System.out.println("clusterfromhas" + clusterName);
    }
  }

  private void addClusterList()
  {
    //System.out.println("Querying for cluster list");
    if(clusterHost == null || clusterPort == null)
      return;
    try {
      ConnectionHelper connection = new ConnectionHelper(hostAndPort);
      //System.out.println("add cluster list host and port " + hostAndPort);
      clusterURLs = connection.getClusterIdsAndURLs();
      if (clusterURLs == null) {
        System.err.println("No clusters");
        System.exit(0);
      }
    } catch (Exception e) {
      System.err.println(e);
      System.exit(0);
    }
    Enumeration names = clusterURLs.keys();
    clusterNameBox.removeAllItems();
    Vector vNames = new Vector();
    while (names.hasMoreElements())
      vNames.addElement(names.nextElement());
    Collections.sort(vNames);
    for (int i = 0; i < vNames.size(); i++)
    {
        clusterNameBox.addItem(vNames.elementAt(i));
        clusterContainer.put(vNames.elementAt(i), new Hashtable(1));
//        System.out.println("clusteritem " + vNames.elementAt(i));
    }
    if (!vNames.isEmpty())
  clusterName = (String)vNames.elementAt(0);
    //add("Display schedules from:", clusterNameBox, SET_CLUSTER);
    // if applet, modify all cluster URLs to use the code base host and port
    // note that this doesn't sort
    if (isApplet) {
      Hashtable tmpHT = new Hashtable();
      names = clusterURLs.keys();
      while (names.hasMoreElements()) {
        String name = (String)names.nextElement();
        String clusterURL = (String)clusterURLs.get(name);
        int startIndex = clusterURL.indexOf('$');
        if (startIndex == -1)
          continue;
        int endIndex = clusterURL.indexOf('/', startIndex);
        if (endIndex == -1)
          continue;
        clusterURL = hostAndPort + clusterURL.substring(startIndex, endIndex+1);
        tmpHT.put(name, clusterURL);
      }
      clusterURLs = tmpHT;
    }
  }

  private void createMenuAndDialogs(final Container contentPane, JMenuBar menuBar)
  {
    JMenu fileMenu = (JMenu) menuBar.add(new JMenu("Connections"));
    JMenu menu = null;
    Action action = null;

    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          useCache = ((JCheckBoxMenuItem)e.getSource()).getState();
        }
      };

    MenuUtility.addCheckMenuItem(fileMenu, "Use Cache", 'U', action, false);
    //createMenuItem(frame.fileMenu, "Open", 'O', "", new GetFile());  // open file selection
    //createMenuItem(frame.fileMenu, "Connect", 'T', "", new GetConnectionData());
    // ***** create File menu

   fileMenu.setMnemonic('Z');
   //frame.createMenuItem(fileMenu, "Open", 'O', "", new GetFile());  // open file selection
   //frame.createMenuItem(fileMenu, "Connect", 'T', "", new GetConnectionData());
   JMenuItem openItem = (JMenuItem) fileMenu.add(new JMenuItem("Open"));
   openItem.setMnemonic('O');
   openItem.addActionListener(new GetFile());

   JMenuItem connectItem = (JMenuItem) fileMenu.add(new JMenuItem("Connect"));
   connectItem.setMnemonic('T');
   connectItem.addActionListener(new GetConnectionData());

   //JMenuItem cacheItem = (JMenuItem) fileMenu.add(new JMenuItem("Use Cache"));
   //cacheItem.setMnemonic('U');



    menu = (JMenu)menuBar.add(new JMenu("Charts"));
    inventoryChartMenu = (JMenu)menu.add(new JMenu("Inventory Chart"));
    supplierChartMenu = (JMenu)menu.add(new JMenu("Supplier Chart"));
    consumerChartMenu = (JMenu)menu.add(new JMenu("Consumer Chart"));
    menu.setMnemonic('C');




    menu = (JMenu)menuBar.add(new JMenu("Chart Options"));
    menu.setMnemonic('O');


    ButtonGroup group = new ButtonGroup();
    ChartViewsCheckListener listener = new ChartViewsCheckListener();
    JMenu chartViews = (JMenu)menu.add(new JMenu("Chart Views"));

    group.add(MenuUtility.addRadioButtonMenuItem(chartViews, "All Charts", listener, "" + BlackJackInventoryChart.SHOW_ALL_CHARTS, true));
    group.add(MenuUtility.addRadioButtonMenuItem(chartViews, "Inventory Chart", listener, "" + BlackJackInventoryChart.SHOW_INVENTORY_CHART, false));
    group.add(MenuUtility.addRadioButtonMenuItem(chartViews, "Supplier Chart", listener, "" + BlackJackInventoryChart.SHOW_SUPPLIER_CHART, false));
    group.add(MenuUtility.addRadioButtonMenuItem(chartViews, "Consumer Chart", listener, "" + BlackJackInventoryChart.SHOW_CONSUMER_CHART, false));





    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setScrollMainChart(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    MenuUtility.addCheckMenuItem(menu, "Scroll Inventory Chart (3 Chart View)", action, true);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setShowTitle(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    MenuUtility.addCheckMenuItem(menu, "Show Chart Legends", action, true);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setShowLeftYAxis(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    MenuUtility.addCheckMenuItem(menu, "Left Y-Axis", 'L', action, true);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setShowRightYAxis(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    MenuUtility.addCheckMenuItem(menu, "Right Y-Axis", 'R', action, false);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setShowXRangeScroller(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    MenuUtility.addCheckMenuItem(menu, "X-Range Scroller", action, true);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setShowXRangeTickLabels(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    MenuUtility.addCheckMenuItem(menu, "X-Range Tick Labels", action, true);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setXRangeScrollLock(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    MenuUtility.addCheckMenuItem(menu, "Lock X-Range Scroller", action, false);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          boolean state = ((JCheckBoxMenuItem)e.getSource()).getState();

          yRangeScrollerMI.setEnabled(!state);
          yRangeTickLabelsMI.setEnabled(!state);
          yRangeScrollLockMI.setEnabled(!state);

          if (state == false)
          {
            chart.setShowYRangeScroller(yRangeScrollerMI.getState());
          }
          else
          {
            chart.setShowYRangeScroller(false);
          }

          chart.setAutoYRange(state);
        }
      };
    MenuUtility.addCheckMenuItem(menu, "Auto Y-Range", action, false);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setShowYRangeScroller(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    yRangeScrollerMI = MenuUtility.addCheckMenuItem(menu, "Y-Range Scroller", action, false);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setShowYRangeTickLabels(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    yRangeTickLabelsMI = MenuUtility.addCheckMenuItem(menu, "Y-Range Tick Labels", action, true);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setYRangeScrollLock(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    yRangeScrollLockMI = MenuUtility.addCheckMenuItem(menu, "Lock Y-Range Scroller", action, false);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setShowGrid(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    MenuUtility.addCheckMenuItem(menu, "Display Grid", action, true);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setGridOnTop(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    MenuUtility.addCheckMenuItem(menu, "Grid On Top", action, false);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setShowDataTips(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    MenuUtility.addCheckMenuItem(menu, "Show Data Tips", action, true);


    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          chart.setUseCDate(((JCheckBoxMenuItem)e.getSource()).getState());
        }
      };
    MenuUtility.addCheckMenuItem(menu, "Use C-Date", 'U', action, false);


//    cDataDialog = new JDialog(frame, "Enter C-Date", true);
    cDataDialog = new JDialog((JFrame)null, "Enter C-Date", true);
    cDataDialog.setResizable(false);
    cDataDialog.getContentPane().setLayout(new BorderLayout());
    cDataDialog.getContentPane().add(getCDateDialogPanel(), BorderLayout.CENTER);
    cDataDialog.pack();
    action = new AbstractAction()
      {
        public void actionPerformed(ActionEvent e)
        {
          cDataDialog.setLocationRelativeTo(contentPane);
          //cDataDialog.setLocationRelativeTo(InventorySelector.this);
          cDataDialog.show();
        }
      };
    MenuUtility.addMenuItem(menu, "Set C-Date ...", 'S', action);
  }

  private JPanel getCDateDialogPanel()
  {
    JPanel panel = new JPanel(new GridLayout(1, 4));
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

            chart.setCDate(cal.getTime().getTime());
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }

          cDataDialog.hide();
        }
      });

    panel.add(monthField);
    panel.add(dayField);
    panel.add(yearField);
    panel.add(button);

    return(panel);
  }









  public void saveObject()
  {
    try
    {
      if(clusterContainer.size() > 0)
      {
        ostream = new FileOutputStream(cacheFileName);
        objectOutputStream = new ObjectOutputStream(ostream);
        objectOutputStream.writeObject(clusterContainer);
        objectOutputStream.flush();
        System.err.println("save object " + clusterContainer + " " + cacheFileName);
      }
      else
        System.err.println("don't save object");
    }
    catch(Exception e)
    {
       e.printStackTrace();
    }
  }

  private boolean setupTimeStatusHandler() {

      //System.out.println("InventorySelector::Setting up Time status Handler");

      if(executionListener == null) {
    timeStatusHandler = new InventoryExecutionTimeStatusHandler();
    Object[] handlers = {timeStatusHandler};
    ClusterInfo ci = new ClusterInfo(clusterName,hostAndPort);

    //System.out.println("InventorySelector::Trying to create InventoryExecutionListener");

    try {
        executionListener = new InventoryExecutionListener(ci,handlers);
        if(executionListener!=null) {
      executionListener.start();
        }
    }
    catch(Exception e) {
        timeStatusHandler=null;
        System.err.println("WARNING::Inventory Selector:  Could not connect to PSP_ExecutionWatcher - movie mode will not be available. - Exeception was: " + e.toString());
        //        throw new RuntimeException(e.toString());
        return false;
    }
      }
      return true;
  }

  private void setDataSetMenu()
  {
    DataSet[] dataSets = null;
    JMenu menu = null;

    dataSetList = chart.getDataSets();

    inventoryChartMenu.removeAll();
    dataSets = (DataSet[])dataSetList.elementAt(0);
    for (int i=0; i<dataSets.length; i++)
    {
      menu = (JMenu)inventoryChartMenu.add(new JMenu(dataSets[i].dataName));
      addDataSetTypeRadioButtons((PolygonFillableDataSet)dataSets[i], menu);
    }

    supplierChartMenu.removeAll();
    dataSets = (DataSet[])dataSetList.elementAt(1);
    for (int i=0; i<dataSets.length; i++)
    {
      menu = (JMenu)supplierChartMenu.add(new JMenu(dataSets[i].dataName));
      DataSetTypeCheckListener listener = new DataSetTypeCheckListener((PolygonFillableDataSet)dataSets[i]);
      MenuUtility.addCheckMenuItem(menu, "Visible", "" + VISIBLE, listener, dataSets[i].visible);
      MenuUtility.addCheckMenuItem(menu, "Use Fill Pattern", "" + FILL_PATTERN, listener, ((dataSets[i] instanceof PolygonFillableDataSet) && ((PolygonFillableDataSet)dataSets[i]).useFillPattern));
    }

    consumerChartMenu.removeAll();
    dataSets = (DataSet[])dataSetList.elementAt(2);
    for (int i=0; i<dataSets.length; i++)
    {
      menu = (JMenu)consumerChartMenu.add(new JMenu(dataSets[i].dataName));
      DataSetTypeCheckListener listener = new DataSetTypeCheckListener((PolygonFillableDataSet)dataSets[i]);
      MenuUtility.addCheckMenuItem(menu, "Visible", "" + VISIBLE, listener, dataSets[i].visible);
      MenuUtility.addCheckMenuItem(menu, "Use Fill Pattern", "" + FILL_PATTERN, listener, ((dataSets[i] instanceof PolygonFillableDataSet) && ((PolygonFillableDataSet)dataSets[i]).useFillPattern));
    }
  }

  private void addDataSetTypeRadioButtons(PolygonFillableDataSet dataSet, JMenu menu)
  {
    ButtonGroup group = new ButtonGroup();

    DataSetTypeCheckListener listener = new DataSetTypeCheckListener(dataSet);

    MenuUtility.addCheckMenuItem(menu, "Visible", "" + VISIBLE, listener, dataSet.visible);
    MenuUtility.addCheckMenuItem(menu, "Use Fill Pattern", "" + FILL_PATTERN, listener, ((dataSet instanceof PolygonFillableDataSet) && ((PolygonFillableDataSet)dataSet).useFillPattern));

    group.add(MenuUtility.addRadioButtonMenuItem(menu, "Bar Chart", listener, "" + BAR, (dataSet instanceof BarDataSet) && (!dataSet.polygonFill)));

    group.add(MenuUtility.addRadioButtonMenuItem(menu, "Filled Bar Chart", listener, "" + FILLED_BAR, (dataSet instanceof BarDataSet) && (dataSet.polygonFill)));

    group.add(MenuUtility.addRadioButtonMenuItem(menu, "Step Chart", listener, "" + STEP, (dataSet instanceof StepDataSet) && (!dataSet.polygonFill)));

    group.add(MenuUtility.addRadioButtonMenuItem(menu, "Filled Step Chart", listener, "" + FILLED_STEP, (dataSet instanceof StepDataSet) && (dataSet.polygonFill)));

    group.add(MenuUtility.addRadioButtonMenuItem(menu, "Line Chart", listener, "" + LINE, (dataSet.getClass().equals(PolygonFillableDataSet.class)) && (!dataSet.polygonFill)));

    group.add(MenuUtility.addRadioButtonMenuItem(menu, "Filled Line Chart", listener, "" + FILLED_LINE, (dataSet.getClass().equals(PolygonFillableDataSet.class)) && (dataSet.polygonFill)));
  }


  class ChartViewsCheckListener extends AbstractAction
  {
    public ChartViewsCheckListener()
    {
    }

    public void actionPerformed(ActionEvent e)
    {
      int view = Integer.parseInt(e.getActionCommand());

      switch(view)
      {
        case BlackJackInventoryChart.SHOW_ALL_CHARTS:
          chart.showAllCharts();
        break;

        case BlackJackInventoryChart.SHOW_INVENTORY_CHART:
          chart.showFullInventoryChart();
        break;

        case BlackJackInventoryChart.SHOW_SUPPLIER_CHART:
          chart.showFullSupplierChart();
        break;

        case BlackJackInventoryChart.SHOW_CONSUMER_CHART:
          chart.showFullConsumerChart();
        break;
      }
    }
  }

  class DataSetTypeCheckListener extends AbstractAction
  {
    private PolygonFillableDataSet dataSet = null;

    public DataSetTypeCheckListener(PolygonFillableDataSet dataSet)
    {
      this.dataSet = dataSet;
    }

    public void actionPerformed(ActionEvent e)
    {
      try
      {
        int newType = Integer.parseInt(e.getActionCommand());
        double[] data = dataSet.getData();

        switch(newType)
        {
          case BAR:
            if (!setPolygonFilled(dataSet, BarDataSet.class, false))
            {
              dataSet = replace(dataSet, new BarDataSet(data, data.length/2, false, InventoryTableModel.barWidth));
            }
          break;

          case FILLED_BAR:
            if (!setPolygonFilled(dataSet, BarDataSet.class, true))
            {
              dataSet = replace(dataSet, new BarDataSet(data, data.length/2, true, InventoryTableModel.barWidth));
            }
          break;

          case STEP:
            if (!setPolygonFilled(dataSet, StepDataSet.class, false))
            {
              dataSet = replace(dataSet, new StepDataSet(data, data.length/2, false));
            }
          break;

          case FILLED_STEP:
            if (!setPolygonFilled(dataSet, StepDataSet.class, true))
            {
              dataSet = replace(dataSet, new StepDataSet(data, data.length/2, true));
            }
          break;

          case LINE:
            if (!setPolygonFilled(dataSet, PolygonFillableDataSet.class, false))
            {
              dataSet = replace(dataSet, new PolygonFillableDataSet(data, data.length/2, false));
            }
          break;

          case FILLED_LINE:
            if (!setPolygonFilled(dataSet, PolygonFillableDataSet.class, true))
            {
              dataSet = replace(dataSet, new PolygonFillableDataSet(data, data.length/2, true));
            }
          break;

          case VISIBLE:
            if (e.getSource() instanceof JCheckBoxMenuItem)
            {
              chart.setVisible(dataSet, ((JCheckBoxMenuItem)e.getSource()).getState());
            }
          break;

          case FILL_PATTERN:
            if (e.getSource() instanceof JCheckBoxMenuItem)
            {
              dataSet.useFillPattern = ((JCheckBoxMenuItem)e.getSource()).getState();
            }
          break;
        }

        resetChartDataSets();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }

      chart.repaint();
    }

    private boolean setPolygonFilled(PolygonFillableDataSet dataSet, Class dataSetClass, boolean filled)
    {
      if (dataSet.getClass().equals(dataSetClass))
      {
        dataSet.polygonFill = filled;
        return(true);
      }

      return(false);
    }

    private void resetChartDataSets()
    {
      chart.detachAllDataSets();
      legend.removeAllDataSets();

      DataSet[] dataSets = null;

      dataSets = (DataSet[])dataSetList.elementAt(0);
      for (int i=0; i<dataSets.length; i++)
      {
        chart.attachDataSet(dataSets[i], 0);
        legend.addDataSet(dataSets[i]);
      }

      dataSets = (DataSet[])dataSetList.elementAt(1);
      for (int i=0; i<dataSets.length; i++)
      {
        chart.attachDataSet(dataSets[i], 1);
      }

      dataSets = (DataSet[])dataSetList.elementAt(2);
      for (int i=0; i<dataSets.length; i++)
      {
        chart.attachDataSet(dataSets[i], 2);
      }
    }

    private PolygonFillableDataSet replace(PolygonFillableDataSet oldSet, PolygonFillableDataSet newSet)
    {
      DataSet[] dataSets = null;

      for (int i=0, isize=dataSetList.size(); i<isize; i++)
      {
        dataSets = (DataSet[])dataSetList.elementAt(i);

        for (int j=0; j<dataSets.length; j++)
        {
          if (dataSets[j].equals(oldSet))
          {
            newSet.visible = oldSet.visible;
            newSet.dataGroup = oldSet.dataGroup;
            newSet.dataName = oldSet.dataName;
            newSet.automaticallySetColor = oldSet.automaticallySetColor;
            newSet.colorNumber = oldSet.colorNumber;
            newSet.linecolor = oldSet.linecolor;
            newSet.useFillPattern = oldSet.useFillPattern;
            newSet.xValueOffset = oldSet.xValueOffset;
            newSet.yValueOffset = oldSet.yValueOffset;
            dataSets[j] = newSet;

            return(newSet);
          }
        }
      }

      return(null);
    }
  }

  class DoQuery implements ActionListener // listens on assetnamebox selecteditemchange
  {
    public void actionPerformed(ActionEvent e)
    {
      System.err.println("doquery filebased: " + fileBased + " usecache " + useCache);
      try
      {
        String assetName = (String)assetNameBox.getSelectedItem();
        String clusterName = (String) clusterNameBox.getSelectedItem();
        InventoryQuery query = null;

        if(!fileBased && !useCache)
        {
          setupTimeStatusHandler();
          query = new InventoryQuery(assetName, clusterName, clusterContainer, assetInventories, startParam, endParam);
          new QueryHelper(query, hostAndPort + "$" + clusterName + "/", isApplet, chart, table, legend, doDisplayTable, timeStatusHandler);
        }
        else if(fileBased)
        {
          Hashtable assetList = (Hashtable) clusterData.get(clusterName);
          UISimpleInventory inventory = (UISimpleInventory) assetList.get(assetName);
          query = new InventoryQuery(inventory, clusterName, clusterContainer, startParam, endParam);
          new QueryHelper(query, chart, table, legend);
        }
        else if(useCache)
        {
          if(clusterContainer.size() > 0)
          {
            Hashtable assetList = (Hashtable) clusterContainer.get(clusterName);
            if(assetList.containsKey(assetName))
            {
              System.err.println("from cache");
              UISimpleInventory inventory = (UISimpleInventory) assetList.get(assetName);
              query = new InventoryQuery(inventory, clusterName, clusterContainer, startParam, endParam);
              new QueryHelper(query, chart, table, legend);
            }
            else
            {
              System.err.println("from port");
              setupTimeStatusHandler();
              query = new InventoryQuery(assetName, clusterName, clusterContainer, assetInventories, startParam, endParam);
              new QueryHelper(query, hostAndPort + "$" + clusterName + "/", isApplet, chart, table, legend, doDisplayTable, timeStatusHandler);
            }
          }
        }

        if (frame instanceof JFrame)
        {
          ((JFrame)frame).setTitle(query.model.getAssetName());
        }
        else if (frame instanceof JInternalFrame)
        {
          ((JInternalFrame)frame).setTitle(query.model.getAssetName());
        }

        setDataSetMenu();
        frame.validate();
      }
      catch(Throwable ex)
      {
        ex.printStackTrace();
      }
    }
  }

  class InvFilter extends FileFilter
  {
    String suffix = ".inv";
    public boolean accept(File name)
    {
      String f = name.getName();
      return f.indexOf(suffix) != -1;
    }
    public String getDescription()
    {
      return "filefilter";
    }
  }

  class FillAssetList implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {

//      System.out.println("fillassetlist ");
      listFilled = false;
      if(clusterNameBox.getSelectedItem() != null )
        updateInventoryBox();
    }
  }



  public class GetFile extends AbstractAction
   {

     public void actionPerformed(ActionEvent e)
     {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir") );
        fc.setFileFilter(new InvFilter());
        if(fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
        {
          System.err.println("file is " + fc.getSelectedFile());
          fileName = fc.getSelectedFile().getName();
          getFileData(fileName);
          fileBased = true;
          addClustersFromHash();
        }
     }
   }

   public class GetConnectionData extends AbstractAction
   {
     public void actionPerformed(ActionEvent e)
     {
        String msg = "Enter cluster Log Plan Server location as host:port";

        String host = "65.84.104.67";
        String port = "5555";
        String defaultString = host + ":" + port;
        messageString = OptionPane.showInputDialog(null, msg, "Cluster Location", 3, null, null, defaultString);
        String s = (String)messageString;
        s = s.trim();
        if (s.length() != 0)
        {
          int i = s.indexOf(":");
          if (i != -1)
          {
            host = s.substring(0, i);
            port = s.substring(i+1);
          }
        }
        clusterHost = host;
        clusterPort = port;
        hostAndPort = "http://" + clusterHost + ":" + clusterPort + "/";
        fileBased = false;
        addClusterList();
     }
   }
}
