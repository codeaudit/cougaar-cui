/*
 * <copyright>
 *  
 *  Copyright 1997-2004 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects
 *  Agency (DARPA).
 * 
 *  You can redistribute this software and/or modify it under the
 *  terms of the Cougaar Open Source License as published on the
 *  Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
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
