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
package org.cougaar.lib.uiframework.ui.ohv.util;

import java.util.Vector;

public  class ArgVector extends Vector {
  private Vector argsVIgnore = new Vector();

  public ArgVector(String args[]) {
    for (int idx=0; idx<args.length; idx++) {
      argsVIgnore.add(args[idx].toLowerCase());
      add(args[idx]);
      System.out.println("Argument #"+(idx+1)+": ["+args[idx]+"]");
    }
  }

  public boolean containsIgnoreCase(String value) {
    return argsVIgnore.contains(value.toLowerCase());
  }
  public boolean contains(String value) {
    return super.contains(value);
  }

  /*     impelement when desired
  public int countStartsWith(String value) {
  }

  public int countStartsWithIgnoreCase(String value) {
  }

  private int countStartsWith(Vector v, String str) {
  }

  public Collection startsWith(String str) {
  }

  public Collection startsWithIgnoreCase(String str) {
  }
  */
}
