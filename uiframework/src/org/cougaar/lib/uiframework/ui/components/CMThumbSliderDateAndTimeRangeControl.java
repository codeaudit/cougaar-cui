package org.cougaar.lib.uiframework.ui.components;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import org.cougaar.lib.uiframework.ui.components.mthumbslider.COrderedLabeledMThumbSlider;
import org.cougaar.lib.uiframework.ui.models.RangeModel;

/**
 * A two thumbed slider bean that is used for selecting a range between two
 * values.  Area between thumbs that represents the selected range is
 * highlighted red.
 *
 * This bean has a bounded property:  "range"
 */
public class CMThumbSliderDateAndTimeRangeControl extends CMThumbSliderRangeControl
{
  private static final String[] monthList = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

  private static final long MS_IN_ONE_DAY = 1000*60*60*24;

  private static int FIDELITY = 1000;
  private static final int MAJOR_TICK_SPACING = FIDELITY/10;

  // Default time scale of ms
  private long timeScale = 1;
  private long baseTime = 0;

  private long cDate = 0;
  private boolean useCDate = false;

  /**
   * Default constructor.  Creates new range slider with minimum value 0 and
   * maximum value 30.
   */
  public CMThumbSliderDateAndTimeRangeControl()
  {
    super(0, 30);
  }

  /**
   * Creates a new range slider with given minimum and maximum values.
   *
   * @param minValue minimum setting for range
   * @param maxValue maximum value for range
   */
  public CMThumbSliderDateAndTimeRangeControl(float minValue, float maxValue)
  {
    super(minValue, maxValue);
  }

  public void setTimeScale(long scale)
  {
    timeScale = scale;
  }

  public long getTimeScale()
  {
    return(timeScale);
  }

  public void setBaseTime(long time)
  {
    baseTime = time;
  }

  public long getBaseTime()
  {
    return(baseTime);
  }

  public void setUseCDate(boolean use)
  {
    useCDate = use;
    setSliderRange(minValue, maxValue);
  }

  public boolean getUseCDate()
  {
    return(useCDate);
  }

  public void setCDate(long time)
  {
    cDate = time;
    if (useCDate)
    {
      setSliderRange(minValue, maxValue);
    }
  }

  public long getCDate()
  {
    return(cDate);
  }

  public void setSliderRange(float minValue, float maxValue)
  {
    // Try to maintain the same values if possible
/*    Vector currentValues = new Vector();
    for (int i = 0; i < numThumbs; i++)
    {
        currentValues.add(new Float(fromSlider(slider.getValueAt(i))));
    }*/
    RangeModel oldRange = getRange();

    this.minValue = minValue;
    this.maxValue = maxValue;
    unit = (maxValue - minValue) / FIDELITY;

    if (Math.abs(maxValue) > 10)
    {
        labelFormat = new DecimalFormat("####");
    }
    else
    {
        labelFormat = new DecimalFormat("##.##");
    }

    int sliderMin = toSlider(minValue);
    int sliderMax = toSlider(maxValue);
    for (int i = 0; i < numThumbs; i++)
    {
      BoundedRangeModel model = slider.getModelAt(i);
      model.setMaximum(sliderMax);
      model.setMinimum(sliderMin);
    }

    Hashtable valueLabels = new Hashtable(1);
    if (minValue == maxValue)
    {
      valueLabels.put(new Integer(0), new JLabel());
    }
    else if (useCDate)
    {
      setCDateLabels(valueLabels);
    }
    else
    {
      setMonthAndDayLabels(valueLabels);
    }

    slider.setLabelTable(valueLabels);

    // Set sliders to old current values
    /*for (int i = 0; i < currentValues.size(); i++)
    {
      Number currentValue = (Number)currentValues.elementAt(i);
      slider.setValueAt(toSlider(currentValue.floatValue()), i);
    }*/

    setRange(oldRange);

    SwingUtilities.updateComponentTreeUI(this);
  }

  private void setCDateLabels(Hashtable valueLabels)
  {
    String label = null;
    for (int i=0; i<=FIDELITY; i+=MAJOR_TICK_SPACING)
    {
      label = "" + ((baseTime + (long)(fromSlider(i)*timeScale))- cDate)/MS_IN_ONE_DAY;

      valueLabels.put(new Integer(i), new JLabel(label));
    }
  }

  private void setMonthAndDayLabels(Hashtable valueLabels)
  {
    GregorianCalendar cal = new GregorianCalendar();
    GregorianCalendar nextCal = new GregorianCalendar();
    long value = 0;
    int month = -1;
    String label = null;
    for (int i=0; i<=FIDELITY; i+=MAJOR_TICK_SPACING)
    {
      cal.setTime(new Date(baseTime + (long)(fromSlider(i)*timeScale)));
      nextCal.setTime(new Date(baseTime + (long)(fromSlider(i + MAJOR_TICK_SPACING)*timeScale)));
      label = "" + cal.get(Calendar.DAY_OF_MONTH);

      if (month != cal.get(Calendar.MONTH))
      {
        if (cal.get(Calendar.MONTH) == nextCal.get(Calendar.MONTH))
        {
          if ((cal.get(Calendar.DAY_OF_MONTH) == 15) || (nextCal.get(Calendar.DAY_OF_MONTH) > 15))
          {
            month = cal.get(Calendar.MONTH);
            label = label + " " + monthList[month];
          }
          else if (i == FIDELITY)
          {
            month = cal.get(Calendar.MONTH);
            label = label + " " + monthList[month];
          }
        }
        else
        {
          month = cal.get(Calendar.MONTH);
          label = label + " " + monthList[month];
        }
      }

      valueLabels.put(new Integer(i), new JLabel(label));
    }
  }
}
