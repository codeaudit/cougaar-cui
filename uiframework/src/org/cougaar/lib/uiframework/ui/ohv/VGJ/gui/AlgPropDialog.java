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
 * File: AlgPropDialog.java
 *
 * 2/25/97   Larry Barowski


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
 * A dialog class that allows the user to specify parameters
 * used by algorithms.
 * </p>Here is the <a href="../gui/AlgPropDialog.java">source</a>.
 *
 *@author	Larry Barowski
**/


   public class AlgPropDialog extends Dialog
   {
      private GraphCanvas graphCanvas_;
      private Frame frame_;
   
      private TextField vertical_, horizontal_;
   
   
      public AlgPropDialog(Frame frame, GraphCanvas graph_canvas)
      {
         super(frame, "Spacing (for some algorithms)", true);
      
         graphCanvas_ = graph_canvas;
         frame_ = frame;
	 LPanel p = new LPanel();

	 p.addLabel("Vertical Spacing (pixels)", 1, 1, 0.0, 1.0, 0, 2);
	 vertical_ = p.addTextField(8, 0, -1, 1.0, 1.0, 1, 1);
	 p.addLabel("Horizontal Spacing (pixels)", 1, 1, 0.0, 1.0, 0, 2);
	 horizontal_ = p.addTextField(8, 0, -1, 1.0, 1.0, 1, 1);
	 p.addButtonPanel("Apply Cancel", 0);
     
         p.finish();
	 add("Center", p);
         showMe();
      }
   
   
   
   
      public void showMe()
      {
        System.err.println("AlgPropDialog showme v,h:"+graphCanvas_.vSpacing+", "+graphCanvas_.hSpacing);
         pack();
      
         vertical_.setText(String.valueOf(graphCanvas_.vSpacing));
         horizontal_.setText(String.valueOf(graphCanvas_.hSpacing));
      
         show();
      }
   
   
   
      public boolean action(Event event, Object object)
      {
         if(event.target instanceof Button)
         {
            if("Apply".equals(object))
            {
               boolean ok = true;
               try
               {
                  graphCanvas_.hSpacing = Double.valueOf
                     (horizontal_.getText()).doubleValue();
               }
                  catch (NumberFormatException e)
                  {
                     new MessageDialog(frame_, "Error",
                        "Bad format for horizontal spacing.", true);
                     ok = false;
                  }
               try
               {
                  graphCanvas_.vSpacing = Double.valueOf
                     (vertical_.getText()).doubleValue();
               }
                  catch (NumberFormatException e)
                  {
                     new MessageDialog(frame_, "Error",
                        "Bad format for vertical spacing.", true);
                     ok = false;
                  }
            
               if(ok)
                  hide();
            }
            else if("Cancel".equals(object))
            {
               hide();
            }
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
