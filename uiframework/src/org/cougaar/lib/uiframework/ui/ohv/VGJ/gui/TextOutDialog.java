/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
 */
/*
 * File: TextOutDialog.java
 *
 * 12/10/96   Larry Barowski


  The following comment is to comply with GPLv2:
     This source file was modified during February 2001.

 *
*/


   package org.cougaar.lib.uiframework.ui.ohv.VGJ.gui;



   import java.awt.Dialog;
   import java.awt.Button;
   import java.awt.Frame;
   import java.awt.Event;
   import java.awt.TextArea;
   import java.awt.Color;



/**
 *  A dialog class for output of a single string.
 *  </p>Here is the <a href="../gui/TextOutDialog.java">source</a>.
 *
 *@author	Larry Barowski
**/

   public class TextOutDialog extends Dialog
   {
      private TextArea	text;
   
   
      public TextOutDialog(Frame frame, String title, String text_in,
		boolean modal)
      {
         super(frame, title, modal);
      
         int oldpos = -1, pos;
         int rows = 0, columns = 0;
         while((pos = text_in.indexOf('\n', oldpos + 1)) != -1)
         {
            if(pos - oldpos > columns)
               columns = pos - oldpos;
            rows++;
            oldpos = pos;
         }
         columns += 2;
         rows += 2;
      
         if(rows > 35)
            rows = 35;
         if(columns > 80)
            columns = 80;
      
         Construct_(frame, title, text_in, rows, columns, modal);
      }
   
   
   
   
   
      public TextOutDialog(Frame frame, String title, String text_in, int rows,
      int columns, boolean modal)
      {
         super(frame, title, modal);
         Construct_(frame, title, text_in, rows, columns, modal);
      }
   
   
   
   
   
      private void Construct_(Frame frame, String title, String text_in, int rows,
      int columns, boolean modal)
      {
	 LPanel p = new LPanel();
         text = new TextArea(text_in, rows, columns);
         text.setEditable(false);
         text.setBackground(Color.white);
	 p.addComponent(text, 0, 0, 1.0, 1.0, 3, 0);
	 p.addButtonPanel("OK", 0);
            
         p.finish();
	 add("Center", p);
	 pack();
         show();
      }
   
   
   
      public boolean action(Event event, Object object)
      {
         if(event.target instanceof Button)
         {
            hide();
            dispose();
            return true;
         }
         return false;
      }
   }
