package org.cougaar.lib.uiframework.ui.components;

import java.text.DecimalFormat;

import java.util.Hashtable;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class CDateLabeledSlider extends CLabeledSlider
{

  private static final String[] monthList = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


  public static final long MS_IN_ONE_DAY = 1000*60*60*24;

  private static int FIDELITY = 1000;
  private static final int MAJOR_TICK_SPACING = FIDELITY/10;

  // Default time scale of seconds
  private long timeScale = 1000;
  private long baseTime = 0;

  private long cDate = 0;
  private boolean useCDate = false;


  public CDateLabeledSlider(String labelString, int labelWidth, float minValue,
                         float maxValue)
  {
    super(labelString, labelWidth, minValue, maxValue);
  }

  public void setUseCDate(boolean value)
  {
    useCDate = value;
    setSliderRange(minValue, maxValue);
  }

  public void setCDate(long time)
  {
    cDate = time;
    setSliderRange(minValue, maxValue);
  }

  public long getCDate()
  {
    return(cDate);
  }

  public void setSliderRange(float minValue, float maxValue)
  {

        // Try to maintain the same value if possible
        float currentValue = getValue();

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

//        setValue(currentValue);
    }

  private void setCDateLabels(Hashtable valueLabels)
  {
    String label = null;
    int lastDay = -1;
    int day = -1;
    for (int i=0; i<=FIDELITY; i+=MAJOR_TICK_SPACING)
    {
      day = (int)(((baseTime + (long)(fromSlider(i)*timeScale))- cDate)/MS_IN_ONE_DAY);

      label = "";
      
      if (lastDay != day)
      {
        label = label + day;
        lastDay = day;
      }

      valueLabels.put(new Integer(i), new JLabel(label));
    }
  }

  private void setMonthAndDayLabels(Hashtable valueLabels)
  {
    GregorianCalendar cal = new GregorianCalendar();
    GregorianCalendar nextCal = new GregorianCalendar();
    long value = 0;
    int day = -1;
    int month = -1;
    String label = null;
    for (int i=0; i<=FIDELITY; i+=MAJOR_TICK_SPACING)
    {
      cal.setTime(new Date(baseTime + (long)(fromSlider(i)*timeScale)));

      nextCal.setTime(new Date(baseTime + (long)(fromSlider(i + MAJOR_TICK_SPACING)*timeScale)));

      label = "";
      if (day != cal.get(Calendar.DAY_OF_MONTH))
      {
        label += cal.get(Calendar.DAY_OF_MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
      }
/*      else if (month != cal.get(Calendar.MONTH))
      {
        label += cal.get(Calendar.DAY_OF_MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
      }*/

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

  protected void init(String labelString, int labelWidth, float minValue,
                      float maxValue)
    {

        labelPanel = new JPanel(new BorderLayout());
        label = new JLabel(labelString + ": ");
        label.setVerticalAlignment(JLabel.TOP);
        labelPanel.add(label, BorderLayout.WEST);
        valueLabel = new JLabel();
        labelPanel.add(valueLabel, BorderLayout.CENTER);
        setLabelWidth(labelWidth);
        valueLabel.setVerticalAlignment(JLabel.TOP);
        valueLabel.setHorizontalAlignment(JLabel.RIGHT);
        add(labelPanel, BorderLayout.WEST);

        slider = new JSlider(0, fidelity);
        majorTickSpacing = fidelity/10;
        slider.setMinorTickSpacing(majorTickSpacing/2);
        slider.setMajorTickSpacing(majorTickSpacing);
        add(slider, BorderLayout.CENTER);

        slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e)
                {
//                    float oldValue = Float.parseFloat(valueLabel.getText());
                    float oldValue = 0.0f;

                    // valueLabel.setText(getStringValue());
                    LabeledSlider:firePropertyChange("value", oldValue,
                                                     getValue());
                }
            });

        setSliderRange(minValue, maxValue);
    }

} 
