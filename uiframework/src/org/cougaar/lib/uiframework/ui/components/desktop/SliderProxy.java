/* 
 * <copyright>
 *  Copyright 1997-2003 Clark Software Engineering (CSE)
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

package org.cougaar.lib.uiframework.ui.components.desktop;

import javax.swing.event.ChangeListener;

import org.cougaar.lib.uiframework.ui.models.RangeModel;

/***********************************************************************************************************************
<b>Description</b>: This interface is used by the desktop Date Command Slider component to itentify controllable
                    sliders in other components and use this interface to control them.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
public interface SliderProxy
{
  public void dispose();
  public void enableSlider(boolean enable);
  public float getMinLimit();
  public float getMaxLimit();
  public void setRange(RangeModel range);
  public void setValue(float value);

  public void addChangeListener(ChangeListener listener);
  public void removeChangeListener(ChangeListener listener);
}
