/* 
 * <copyright> 
 *  Copyright 1997-2001 Clark Software Engineering (CSE)
 *  under sponsorship of the Defense Advanced Research Projects 
 *  Agency (DARPA). 
 *  
 *  This program is free software; you can redistribute it and/or modify 
 *  it under the terms of the Cougaar Open Source License as published by 
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).  
 *  
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS  
 *  PROVIDED "AS IS" WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR  
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF  
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT  
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT  
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL  
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,  
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR  
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.  
 *  
 * </copyright> 
 */

package org.cougaar.lib.uiframework.ui.components.drilldown;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DrillDownTest
{
  public static void main(String[] args)
  {
    JFrame frame = new JFrame("DrillDown Test");
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    DrillableLabel label = new DrillableLabel("Stack");
    DrillDownStack stack = new DrillDownStack(label);
    stack.addDrillDown(label, label, null);
    
    panel.add(stack, BorderLayout.CENTER);
    
    frame.getContentPane().add(panel);
    
    frame.setSize(300, 300);

    frame.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        System.exit(0);
      }
    });

    frame.show();
  }
}

class DrillableLabel extends JLabel implements DrillDown
{
  private static long count = 0;

  private String displayString = null;

  public DrillableLabel()
  {
  }

  public DrillableLabel(String display)
  {
    displayString = display + ": " + count++;
    setText(displayString);
  }

  public DrillDown getNextDrillDown(MouseEvent e)
  {
    DrillableLabel label = new DrillableLabel();
    label.setData(displayString);
    
    return(label);
  }

  public void setData(Object data)
  {
    displayString = data + ": " + count++;
    setText(displayString);
  }

  public Component activate(DrillDownStack drillDownStack)
  {
    drillDownStack.addDrillDown(this, this, null);

    return(this);
  }

  public String toString()
  {
    return(getClass().getName() + "@" + hashCode() + " " + displayString);
  }
}
