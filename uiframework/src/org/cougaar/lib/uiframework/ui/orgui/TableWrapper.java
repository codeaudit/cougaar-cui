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

package org.cougaar.lib.uiframework.ui.orgui;

import java.util.*;

/**
 *  An instance of this class serves as a container for a Hashtable floating on
 *  the logplan.  It has a name attribute so that subscribers can tell which
 *  table is which, in case there is more than one of them. 
 */
public class TableWrapper {
  private Hashtable table = null;
  private String name = null;

  /**
   *  Report the name given to this TableWrapper.  Different tables should have
   *  different names so that they can be told apart.
   *  @return the table's name
   */
  public String getName () {
    return name;
  }

  /**
   *  Retrieve the table wrapped within this TableWrapper.
   *  @return the resident Hashtable
   */
  public Hashtable getTable () {
    return table;
  }

  /**
   *  Wrap a Hashtable with a new TableWrapper and affix the associated name.
   *  @param n the name of the resident table
   *  @param t the Hashtable being wrapped
   */
  public TableWrapper (String n, Hashtable t) {
    table = t;
    name = n;
  }
}