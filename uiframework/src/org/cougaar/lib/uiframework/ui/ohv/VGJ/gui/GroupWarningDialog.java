/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
/*
 * File: GroupWarningDialog.java
 *
 * 6/7/97   Larry Barowski


  The following comment is to comply with GPLv2:
     This source file was modified during February 2001.
 *
*/



   package org.cougaar.lib.uiframework.ui.ohv.VGJ.gui;


   import java.awt.Dialog;
   import java.awt.Button;
   import java.awt.Label;
   import java.awt.Frame;
   import java.awt.Event;

   import java.lang.System;




/**
 * An O.K. / Cancel dialog for group deletion.
 * </p>Here is the <a href="../gui/GroupWarningDialog.java">source</a>.
 *
 *@author	Larry Barowski
**/


   public class GroupWarningDialog extends Dialog
   {
      private GraphCanvas graphCanvas_;
      private Frame frame_;
   
   
   
      public GroupWarningDialog(Frame frame, GraphCanvas graph_canvas)
      {
         super(frame, "Delete?", true);
      
         graphCanvas_ = graph_canvas;
         frame_ = frame;
         LPanel p = new LPanel();
      
         p.constraints.insets.bottom = 0;
         p.addLabel("Selected items include one or more group nodes.",
                    0, 0, 1.0, 1.0, 0, 0);
         p.constraints.insets.top = 0;
         p.constraints.insets.bottom = 0;
         p.addLabel("All children of group nodes will be recursively deleted.",
                    0, 0, 1.0, 1.0, 0, 0);
         p.constraints.insets.top = 0;
         p.addLabel("Delete anyway?", 0, 0, 1.0, 1.0, 0, 0);
         p.addButtonPanel("Delete Cancel", 0);
      
         p.finish();
         add("Center", p);
         showMe();
      }
   
   
   
   
      public void showMe()
      {
         pack();
         show();
      }
   
   
   
      public boolean action(Event event, Object object)
      {
         if(event.target instanceof Button)
         {
            if("Cancel".equals(object)) {
               hide(); }
            else if("Delete".equals(object)) {
               graphCanvas_.deleteSelected(false);
               hide(); }
         }
      
         return false;
      }
   
   
   
   }
