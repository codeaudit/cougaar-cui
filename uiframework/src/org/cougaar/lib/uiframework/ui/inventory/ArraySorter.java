/*
 * <copyright>
 *  Copyright 2003 BBNT Solutions, LLC
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
package org.cougaar.lib.uiframework.ui.inventory;
public class ArraySorter {
   /*
   ** Sort in the same array
   */
   public static void sort(Object[] a, Comparer comparer) {
     sort(a, null, 0, a.length - 1, true, comparer);
   }

   /*
   ** Sort a and b, using a as the reference
   */
   public static void sort(Object[] a, Object[] b,
     int from, int to, boolean ascending, Comparer comparer) {
     // No sort
     if (a == null || a.length < 2) return;

     // sort using Quicksort
     int i = from, j = to;
     Object center = a[ (from + to) / 2 ];
     do {
       if (ascending) {
         while( (i < to) && (comparer.compare(  center, a[i]) > 0) ) i++;
         while( (j > from) && (comparer.compare(center, a[j]) < 0) ) j--;
         }
       else {
         // Decending sort
         while( (i < to) && (comparer.compare(  center, a[i]) < 0) ) i++;
         while( (j > from) && (comparer.compare(center, a[j]) > 0) ) j--;
         }
       if (i < j) {
         // Swap elements
         Object temp = a[i]; a[i] = a[j]; a[j] = temp;
         // Swap in b array if needed
         if (b != null) {
           temp = b[i]; b[i] = b[j]; b[j] = temp;
         }
       }
       if (i <= j) { i++; j--; }
       } while(i <= j);
     // Sort the rest
     if (from < j) sort(a, b, from, j, ascending, comparer);
     if (i < to) sort(a, b, i, to, ascending, comparer);
     }

 public static interface Comparer {
   /**
    * The interface implementation should compare the two
    * objects and return an int using these rules:
    * if (a > b)  return > 0;
    * if (a == b) return 0;
    * if (a < b)  return < 0;
    */
    public int compare(Object a, Object b);
    }
 }






