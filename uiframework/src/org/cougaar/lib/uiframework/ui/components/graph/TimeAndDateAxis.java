package org.cougaar.lib.uiframework.ui.components.graph;

import java.awt.*;
import java.applet.*;
import java.util.*;
import java.lang.*;

public class TimeAndDateAxis extends Axis
{
  private static final String[] monthList = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

  private static final long MS_IN_ONE_DAY = 1000*60*60*24;

  // Default time scale of ms
  public long timeScale = 1;
  public long baseTime = 0;

  public long cDate = 0;
  public boolean useCDate = false;

  private String[] majorTicLabel = null;

  private String titleText = null;

  public TimeAndDateAxis()
  {
  }

  public void setTitleText(String s)
  {
    titleText = s;
  }

  public int getAxisWidth(Graphics g)
  {
    int i;
    width = 0;

//    if (minimum == maximum)    return 0;
//    if (dataset.size() == 0)   return 0;

    if (useCDate)
    {
      title.setText("C-Date: " + new Date(cDate));
    }
    else
    {
      title.setText(titleText);
    }

//    calculateGridLabels(g);

//    width = label.getRHeight(g) + label.getLeading(g);
    width = label.getHeight(g);

    if(!title.isNull())
    {
//      width += title.getRHeight(g);
      width += title.getHeight(g);
    }

/*    int maxWidth = 0;
    for(i=0; i<majorTicLabel.length; i++)
    {
      if (majorTicLabel[i] != null)
      {
        label.setText(" "+majorTicLabel[i]);
        maxWidth = Math.max(label.getRHeight(g),maxWidth);
      }
    }
    width += maxWidth;*/

    return(width);
  }

  // Show labels based on date (year/month/day)
  public void calculateGridLabels(Graphics g)
  {
    if (useCDate)
    {
      title.setText("C-Date: " + new Date(cDate));
      doCDateGridLabels(g);
    }
    else
    {
      title.setText(titleText);
      doDayMonthGridLabels(g);
    }
  }

  // Show labels based on date (month/day)
  public void doDayMonthGridLabels(Graphics g)
  {
    GregorianCalendar cal = null;
    GregorianCalendar nextCal = null;

    if ((getInteger(maximum) != 0) || (getInteger(maximum) != 0))
    {
      cal = new GregorianCalendar();
      nextCal = new GregorianCalendar();
      cal.setTime(new Date(baseTime + (long)(minimum*timeScale)));
      if (cal.get(Calendar.HOUR_OF_DAY) != 0 || cal.get(Calendar.MINUTE) != 0 || cal.get(Calendar.MILLISECOND) != 0)
      {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        // Add one day
        cal.add(Calendar.DATE, 1);
      }

      long oneDayValue = MS_IN_ONE_DAY/timeScale;
      long numberOfDaysInRange = (long)((maximum-minimum)/oneDayValue);

      // Pick number of values to show and step size
      int pixelRange = getInteger(maximum) - getInteger(minimum);
      int maxCharWidth = label.charWidth(g, 'W');
      long numberOfLabels = (long)(pixelRange/(maxCharWidth*3));
      numberOfLabels = ((numberOfLabels > numberOfDaysInRange) || (numberOfLabels == 0)) ? numberOfDaysInRange : numberOfLabels;

      double modifier = numberOfDaysInRange/(double)numberOfLabels;

      modifier = (modifier == 0.0) ? 1.0 : RoundUp(modifier);
      label_step = oneDayValue*modifier;

      label_start = (cal.getTime().getTime() - baseTime)/timeScale;

      double val = label_start;
      label_count = 0;
      while (val < maximum ) { val += label_step; label_count++; }
    }
    else
    {
      label_count = 0;
      label_step = 0;
      label_start = 0;
    }

    label_string = new String[label_count];
    majorTicLabel = new String[label_count];
    label_value  = new double[label_count];

    int month = -1;
    double val = 0.0;
    for (int i=0; i<label_count; i++)
    {
      val = label_start + i*label_step;
      cal.setTime(new Date(baseTime + (long)(val*timeScale)));
      nextCal.setTime(new Date(baseTime + (long)((label_start + (i+1)*label_step)*timeScale)));

      if (month != cal.get(Calendar.MONTH))
      {
        if (cal.get(Calendar.MONTH) == nextCal.get(Calendar.MONTH))
        {
          if ((cal.get(Calendar.DAY_OF_MONTH) == 15) || (nextCal.get(Calendar.DAY_OF_MONTH) > 15))
          {
            month = cal.get(Calendar.MONTH);
//            majorTicLabel[i] = monthList[month] + " " + cal.get(Calendar.YEAR);
            majorTicLabel[i] = monthList[month];
          }
          else if (i == label_count-1)
          {
            month = cal.get(Calendar.MONTH);
//            majorTicLabel[i] = monthList[month] + " " + cal.get(Calendar.YEAR);
            majorTicLabel[i] = monthList[month];
          }
        }
        else
        {
          month = cal.get(Calendar.MONTH);
//          majorTicLabel[i] = monthList[month] + " " + cal.get(Calendar.YEAR);
          majorTicLabel[i] = monthList[month];
        }
      }

      label_string[i] = "" + cal.get(Calendar.DAY_OF_MONTH);

      label_value[i] = val;
    }
  }

  // Show labels based on date (month/day)
  public void doCDateGridLabels(Graphics g)
  {
    GregorianCalendar cal = null;
    GregorianCalendar nextCal = null;

    if ((getInteger(maximum) != 0) || (getInteger(maximum) != 0))
    {
      cal = new GregorianCalendar();
      nextCal = new GregorianCalendar();
      cal.setTime(new Date(baseTime + (long)(minimum*timeScale)));
      if (cal.get(Calendar.HOUR_OF_DAY) != 0 || cal.get(Calendar.MINUTE) != 0 || cal.get(Calendar.MILLISECOND) != 0)
      {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        // Add one day
        cal.add(Calendar.DATE, 1);
      }

      long oneDayValue = MS_IN_ONE_DAY/timeScale;
      long numberOfDaysInRange = (long)((maximum-minimum)/oneDayValue);

      // Pick number of values to show and step size
      int pixelRange = getInteger(maximum) - getInteger(minimum);
      int maxCharWidth = label.charWidth(g, 'W');
      long numberOfLabels = (long)(pixelRange/(maxCharWidth*3));
      numberOfLabels = ((numberOfLabels > numberOfDaysInRange) || (numberOfLabels == 0)) ? numberOfDaysInRange : numberOfLabels;

      double modifier = numberOfDaysInRange/(double)numberOfLabels;

      modifier = (modifier == 0.0) ? 1.0 : RoundUp(modifier);
      label_step = oneDayValue*modifier;

      label_start = (cal.getTime().getTime() - baseTime)/timeScale;

      double val = label_start;
      label_count = 1;
      while (val < maximum ) { val += label_step; label_count++; }
    }
    else
    {
      label_count = 0;
      label_step = 0;
      label_start = 0;
    }

    label_string = new String[label_count];
    majorTicLabel = new String[label_count];
    label_value  = new double[label_count];

    Date date = new Date();
    int month = -1;
    double val = 0.0;
    long time = 0;
    date.setTime(cDate);
    cal.setTime(date);
    long currentCDate = truncateTimeToDays(cal);
    for (int i=0; i<label_count; i++)
    {
      val = label_start + i*label_step;
      date.setTime(baseTime + (long)val*timeScale);
      cal.setTime(date);
      time = truncateTimeToDays(cal);

      date.setTime(time);
      label_string[i] = "" + (long)((time - currentCDate)/MS_IN_ONE_DAY);
      label_value[i] = val;
    }
  }

  private long truncateTimeToDays(GregorianCalendar cal)
  {
    cal.set(Calendar.MILLISECOND, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.HOUR, 0);

    return(cal.getTime().getTime());
  }

     protected void drawHAxis(Graphics g)
     {
          Graphics lg;
          int i;
          int j;
          int x0,y0,x1,y1;
          int direction;
          int offset;
          double minor_step;

          Color c;

//          double vmin = minimum*1.001;
//          double vmax = maximum*1.001;
          double vmin = minimum;
          double vmax = maximum;

          double scale  = (amax.x - amin.x)/(maximum - minimum);
          double val;
          double minor;

//          System.out.println("Drawing Horizontal Axis!");


          if( axiscolor != null) g.setColor(axiscolor);

          g.drawLine(amin.x,amin.y,amax.x,amax.y);

          if(position == TOP )     direction =  1;
          else                     direction = -1;

          minor_step = label_step/(minor_tic_count+1);
          val = label_start;
          for(i=0; i<label_count; i++)
          {
              if( val >= vmin && val <= vmax )
              {
                 y0 = amin.y;
                 x0 = amin.x + (int)( ( val - minimum ) * scale);
                 if( Math.abs(label_value[i]) <= 0.0001 && drawzero )
                 {
                      c = g.getColor();
                      if(zerocolor != null) g.setColor(zerocolor);
                      g.drawLine(x0,y0,x0,y0+data_window.height*direction);
                      g.setColor(c);

                 }
                 else if( drawgrid )
                 {
                      c = g.getColor();
                      if(gridcolor != null) g.setColor(gridcolor);
                      g.drawLine(x0,y0,x0,y0+data_window.height*direction);
                      g.setColor(c);
                 }
                 x1 = x0;
                 y1 = y0 + major_tic_size*direction;
                 g.drawLine(x0,y0,x1,y1);
              }

              minor = val + minor_step;
              for(j=0; j<minor_tic_count; j++)
              {
                 if( minor >= vmin && minor <= vmax )
                 {
                    y0 = amin.y;
                    x0 = amin.x + (int)( ( minor - minimum ) * scale);
                    if( drawgrid )
                    {
                      c = g.getColor();
                      if(gridcolor != null) g.setColor(gridcolor);
                      g.drawLine(x0,y0,x0,y0+data_window.height*direction);
                      g.setColor(c);
                    }
                    x1 = x0;
                    y1 = y0 + minor_tic_size*direction;
                    g.drawLine(x0,y0,x1,y1);
                 }
                minor += minor_step;
              }

              val += label_step;
          }


          if (dividers != null)
          {
            for (i=0; i<dividers.length; i++)
            {
              val = ((Double)dividers[i][0]).doubleValue();
              if( val >= vmin && val <= vmax )
              {
                y0 = amin.y;
                x0 = amin.x + (int)( ( val - minimum ) * scale);
                c = g.getColor();
                if(dividers[i][1] != null) g.setColor((Color)dividers[i][1]);
                if (g instanceof Graphics2D)
                {
                  Graphics2D graphics2D = (Graphics2D)g;
                  Stroke stroke = graphics2D.getStroke();
                  BasicStroke newStroke = new BasicStroke(dividerThickness);
                  graphics2D.setStroke(newStroke);
                  g.drawLine(x0,y0,x0,y0+data_window.height*direction);
                  graphics2D.setStroke(stroke);
                }
                else
                {
                  g.drawLine(x0,y0,x0,y0+data_window.height*direction);
                }
                g.setColor(c);
              }
            }
          }


          if(position == TOP ) {
             offset = - label.getLeading(g) - label.getDescent(g);
          } else {
             offset = + label.getLeading(g) + label.getAscent(g);
          }


          val = label_start;
          for(i=0; i<label_count; i++)
          {
              if( val >= vmin && val <= vmax )
              {
                 y0 = amin.y + offset;
                 x0 = amin.x + (int)(( val - minimum ) * scale);
                 label.setText(label_string[i]);
                 label.draw(g,x0,y0,TextLine.CENTER);

                  if (majorTicLabel[i] != null)
                  {
                   if(position == TOP)
                   {
                      y0 = amin.y + offset*2;
                   }
                   else
                   {
                      y0 = amax.y + offset*2;
                   }
                   x0 = amin.x + (int)((val - minimum)*scale);
                   label.setText(majorTicLabel[i]);
                   label.draw(g,x0,y0,TextLine.CENTER);
                  }
              }
              val += label_step;

          }

          if(!title.isNull())
          {
             if(position == TOP)
             {
                y0 = amin.y - (label.getLeading(g) - label.getDescent(g))*2 - title.getLeading(g) - title.getDescent(g);
             }
             else
             {
                y0 = amax.y + (label.getLeading(g) + label.getAscent(g))*2 + title.getLeading(g) + title.getAscent(g);
             }
              x0 = amin.x + ( amax.x - amin.x)/2;
              title.draw(g,x0,y0,TextLine.CENTER);
          }
     }

  public String getPointAsString(double val)
  {
    String string = null;
    Date date = new Date(baseTime + (long)val*timeScale);
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);

    if (useCDate)
    {
      long time = truncateTimeToDays(cal);
      date.setTime(cDate);
      cal.setTime(date);
      long currentCDate = truncateTimeToDays(cal);

      string = "" + (long)((time - currentCDate)/MS_IN_ONE_DAY);
    }
    else
    {
      string = monthList[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.DAY_OF_MONTH);
    }

    return(string);
  }
}
