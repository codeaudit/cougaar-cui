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

import java.io.File;
import java.io.InputStream;
import java.util.Vector;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JSplitPane;

import org.cougaar.lib.uiframework.ui.components.CChartLegend;


/**
 * Define an interface that must be implemented to add a query
 * to the user interface.
 */

public interface Query {

  /* Get the query to send to the cluster.
   */

  String getQueryToSend();

  /* Read the reply sent from the clusters.
   */

  void readReply(InputStream is);

  /* Save the reply sent from the clusters.
   */

  void save(File file);

  /* Create chart.
     */

  JPanel createChart(String title, JSplitPane split);

  boolean setChartData(String title, BlackJackInventoryChart chart, CChartLegend legend);

  /* Create table.
   */

  JPanel reinitializeAndUpdateChart(String title);
    /* Reinit Chart
     */

  JTable createTable(String title);

  void buildTableModel();

  boolean setTableData(String title, JTable table, CChartLegend legend);

  /** Get chart created.
   */

//  JCChart getChart();

  void resetChart();

  void setToCDays(boolean useCDays);

  /* Get the identifier of the PlanServiceProvider (PSP)
     that this client wants to communicate with.
   */

  String getPSP_id();

}

