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

package org.cougaar.lib.uiframework.ui.components.desktop;

import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;

import org.cougaar.lib.uiframework.ui.components.*;
import org.cougaar.lib.uiframework.ui.components.mthumbslider.*;
import org.cougaar.lib.uiframework.ui.models.*;

/***********************************************************************************************************************
<b>Description</b>: This is an implementation of a SliderProxy for a CDateLabeledSlider slider UI control.  This can be
                    returned from a desktop component that implements the DateControllableSliderUI interface to give
                    control of the components slider to the Date Command Slider desktop component.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
class CDateSliderProxy implements PropertyChangeListener, SliderProxy
{
  private CDateLabeledSlider slider = null;

  private boolean enabled = false;

  private ChangeSupport changeSupport = new ChangeSupport();

  public CDateSliderProxy(CDateLabeledSlider slider)
  {
    this.slider = slider;
  }

  public void addChangeListener(ChangeListener listener)
  {
    changeSupport.addChangeListener(listener);
  }

  public void removeChangeListener(ChangeListener listener)
  {
    changeSupport.removeChangeListener(listener);
  }

  public void enableSlider(boolean enable)
  {
    enabled = enable;
    if (enable)
    {
      slider.getSlider().addPropertyChangeListener(this);
    }
    else
    {
      slider.getSlider().removePropertyChangeListener(this);
    }
  }

  public void dispose()
  {
    slider.getSlider().removePropertyChangeListener(this);
  }

  public float getMinLimit()
  {
    return(slider.getMinValue());
  }

  public float getMaxLimit()
  {
    return(slider.getMaxValue());
  }

  public void setRange(RangeModel range)
  {
    if (enabled)
    {
      float newValue = range.getFMin() + (range.getFMax() - range.getFMin())/2.0f;
      slider.getSlider().removePropertyChangeListener(this);
      slider.setValue(newValue);
      slider.getSlider().addPropertyChangeListener(this);
    }
  }

  public void setValue(float value)
  {
    if (enabled)
    {
      slider.getSlider().removePropertyChangeListener(this);
      slider.setValue(value);
      slider.getSlider().addPropertyChangeListener(this);
    }
  }

  public void propertyChange(PropertyChangeEvent e)
  {
    changeSupport.fireChangeEvent(this);
  }
}
