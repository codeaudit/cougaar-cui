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
<b>Description</b>: This is an implementation of a SliderProxy for a CMThumbSliderDateAndTimeRangeControl slider UI
                    control.  This can be returned from a desktop component that implements the DateControllableSliderUI
                    interface to give control of the components slider to the Date Command Slider desktop component.

@author Eric B. Martin, &copy;2001 Clark Software Engineering, Ltd. & Defense Advanced Research Projects Agency (DARPA)
@version 1.0
***********************************************************************************************************************/
class CDateRangeSliderProxy implements PropertyChangeListener, SliderProxy
{
  private CMThumbSliderDateAndTimeRangeControl slider = null;

  private boolean enabled = false;

  private float lastMin = 0;
  private float lastMax = 0;

  private ChangeSupport changeSupport = new ChangeSupport();

  public CDateRangeSliderProxy(CMThumbSliderDateAndTimeRangeControl slider)
  {
    this.slider = slider;
    
    lastMin = slider.getMinValue();
    lastMax = slider.getMaxValue();
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
      slider.addPropertyChangeListener("range", this);
    }
    else
    {
      slider.removePropertyChangeListener("range", this);
    }
  }

  public void dispose()
  {
    slider.removePropertyChangeListener("range", this);
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
      slider.removePropertyChangeListener("range", this);
      slider.setRange(range);
      slider.addPropertyChangeListener("range", this);
    }
  }

  public void setValue(float value)
  {
    if (enabled)
    {
      RangeModel range = slider.getRange();
      float diff = (range.getFMax() - range.getFMin())/2.0f;
      RangeModel newRange = new RangeModel(value - diff, value + diff);
      slider.removePropertyChangeListener("range", this);
      slider.setRange(range);
      slider.addPropertyChangeListener("range", this);
    }
  }

  public void propertyChange(PropertyChangeEvent e)
  {
    if (lastMin != slider.getMinValue() || lastMax != slider.getMaxValue())
    {
      lastMin = slider.getMinValue();
      lastMax = slider.getMaxValue();

      changeSupport.fireChangeEvent(this);
    }
  }
}
