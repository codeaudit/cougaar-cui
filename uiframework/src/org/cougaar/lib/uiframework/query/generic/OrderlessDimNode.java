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

package org.cougaar.lib.uiframework.query.generic;

import java.util.*;

/**
 *  An orderless discrete dimension can use nodes of this type.  In this
 *  implementation, there is no restriction whatsoever on the virtual hierarchy.
 *  The node does not advertise any children, but it will dynamically create a
 *  child for any name upon request.
 */
public class OrderlessDimNode extends DimNode {
  public OrderlessDimNode (String n) {
    setName(n);
  }

  public boolean hasChildren () {
    return false;
  }

  public DimNode hasChild (String name) {
    OrderlessDimNode node = new OrderlessDimNode(name);
    node.setDimension(getDimension());
    return node;
  }

  public Enumeration getChildren () {
    return empty;
  }

  public static class EmptyEnumeration implements Enumeration {
    public boolean hasMoreElements () {
      return false;
    }

    public Object nextElement () {
      return null;
    }
  }
  private static Enumeration empty = new EmptyEnumeration();
}