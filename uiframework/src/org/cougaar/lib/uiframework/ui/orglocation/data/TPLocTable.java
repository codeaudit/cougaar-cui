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

package org.cougaar.lib.uiframework.ui.orglocation.data;

import java.util.*;
import java.io.*;

public class TPLocTable {
  private Hashtable table = new Hashtable();
  private String name = null;

  public TPLocTable (String n) {
    name = n;
  }

  public String getName () {
    return name;
  }

  public boolean isEmpty () {
    return table.isEmpty();
  }

  public void addSchedule (SimpleTPLocation tp) {
    table.put(tp.getName(), tp);
  }

  public SimpleTPLocation removeSchedule (String org) {
    return (SimpleTPLocation) table.remove(org);
  }

  public SimpleTPLocation getSchedule (String org) {
    return (SimpleTPLocation) table.get(org);
  }

  public Enumeration getAllSchedules () {
    return table.elements();
  }

  public void toXml (PrintWriter out) {
    Const.openTag(out, Const.LOC_TABLE);
    for (Iterator i = table.entrySet().iterator(); i.hasNext(); )
      ((SimpleTPLocation) ((Map.Entry) i.next()).getValue()).toXml(out);
    Const.closeTag(out, Const.LOC_TABLE);
    out.flush();
  }
}