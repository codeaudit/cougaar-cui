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
 * File: GroupControl.java
 *
 * 6/7/97   Larry Barowski


  The following comment is to comply with GPLv2:
     This source file was modified during February 2001.
 *
*/



   package org.cougaar.lib.uiframework.ui.ohv.VGJ.gui;


   import java.awt.Dialog;
   import java.awt.TextField;
   import java.awt.Button;
   import java.awt.Label;
   import java.awt.Frame;
   import java.awt.Event;

   import java.lang.System;




/**
 * A dialog class that allows the user to enter group commands.
 * </p>Here is the <a href="../gui/GroupControl.java">source</a>.
 *
 *@author	Larry Barowski
**/


   public class GroupControl extends Dialog
   {
      private GraphCanvas graphCanvas_;
      private Frame frame_;
   
   
   
      public GroupControl(Frame frame, GraphCanvas graph_canvas)
      {
         super(frame, "Group Control", false);
      
         graphCanvas_ = graph_canvas;
         frame_ = frame;
         LPanel p = new LPanel();
      
         p.addButton("Create Group (selected nodes)  [c]", 0, 0, 1.0, 1.0, 1, 0);
         p.constraints.insets.top = 0;
         p.addButton("Destroy Groups (selected groups)  [d]", 0, 0, 1.0, 1.0, 1, 0);
         p.constraints.insets.top = 0;
         p.addButton("Group (selected nodes)  [g]", 0, 0, 1.0, 1.0, 1, 0);
         p.constraints.insets.top = 0;
         p.addButton("Ungroup (selected groups)  [u]", 0, 0, 1.0, 1.0, 1, 0);
         p.addButtonPanel("Cancel", 0);
      
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
            else if("Create Group (selected nodes)  [c]".equals(object)) {
               graphCanvas_.groupControl('c'); }
            else if("Destroy Groups (selected groups)  [d]".equals(object)) {
               graphCanvas_.groupControl('d'); }
            else if("Group (selected nodes)  [g]".equals(object)) {
               graphCanvas_.groupControl('g'); }
            else if("Ungroup (selected groups)  [u]".equals(object)) {
               graphCanvas_.groupControl('u'); }
         }
      
         return false;
      }
   
   
      public boolean handleEvent(Event event) {
	 // Avoid having everything destroyed.
         if (event.id == Event.WINDOW_DESTROY)
         {
            hide();
            return true;
         }
         return super.handleEvent(event);
      }
  
   }
