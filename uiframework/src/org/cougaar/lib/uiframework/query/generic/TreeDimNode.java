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

public class TreeDimNode extends DimNode {
  private Hashtable childMap = new Hashtable();
  private Vector childList = new Vector();

  public void addChild (TreeDimNode node) {
    String name = node.getName();
    TreeDimNode current = (TreeDimNode) childMap.get(name);
    if (current != null)
      childList.setElementAt(node, childList.indexOf(current));
    else
      childList.add(node);
    childMap.put(name, node);
  }

  public void removeChild (String s) {
    TreeDimNode node = (TreeDimNode) childMap.remove(s);
    if (node != null)
      childList.remove(node);
  }

  public void removeChildAt (int i) {
    TreeDimNode node = (TreeDimNode) childList.elementAt(i);
    childList.remove(node);
    childMap.remove(node.getName());
  }

  public boolean hasChildren () {
    return childList.size() > 0;
  }

  public DimNode hasChild (String name) {
    return (DimNode) childMap.get(name);
  }

  public Enumeration getChildren () {
    return childList.elements();
  }
}