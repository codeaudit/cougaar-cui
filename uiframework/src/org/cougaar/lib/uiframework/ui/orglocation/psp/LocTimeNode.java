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

package org.cougaar.lib.uiframework.ui.orglocation.psp;

import java.util.*;

import org.cougaar.lib.uiframework.query.generic.*;
import org.cougaar.lib.uiframework.ui.orglocation.data.*;

public class LocTimeNode extends DimNode {
  private static Enumeration EMPTY = new EmptyEnumeration();
  private static String NAME = "TimeLoc";

  private TimeLocation tloc = null;

  public LocTimeNode (TimeLocation tl, QueryDimension d) {
    tloc = tl;
    setName(NAME);
    setDimension(d);
  }

  public TimeLocation getTimeLoc () {
    return tloc;
  }

  public boolean hasChildren () {
    return false;
  }

  public DimNode hasChild (String name) {
    return null;
  }

  public Enumeration getChildren () {
    return EMPTY;
  }

  private static class EmptyEnumeration implements Enumeration {
    public boolean hasMoreElements () {
      return false;
    }

    public Object nextElement () {
      return null;
    }
  }
}