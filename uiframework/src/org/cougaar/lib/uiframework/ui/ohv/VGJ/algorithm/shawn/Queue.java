/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.ohv.VGJ.algorithm.shawn;

// The following comment is to comply with GPLv2:
//    This source file was modified during February 2001.

import java.util.Vector;

/**
 *  Class to implement a simple queue of integers.

 * </p>Here is the <a href="../algorithm/shawn/Queue.java">source</a>.
 */

public class Queue
  {
   private static int size;
   Vector array;

   public Queue()
     {
      array = new Vector();
     }

   public int push(int item)
     {
      int num;
      array.addElement(new Integer(item));
      num = array.size();
      return num;
     }

   public int pop()
     {
      int item;
      item = ((Integer)array.elementAt(0)).intValue();
      array.removeElementAt(0);
      return item;
     }

   public boolean isEmpty()
     {
      return(array.isEmpty());
     }
  }
