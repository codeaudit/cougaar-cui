package org.cougaar.lib.uiframework.ui.components;

import java.beans.*;
import java.beans.beancontext.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import com.bbn.openmap.*;
import com.bbn.openmap.image.*;

import com.bbn.openmap.gui.*;

import org.cougaar.lib.uiframework.ui.map.app.ScenarioMap;

public class ScenarioViewMenu extends AbstractOpenMapMenu
    implements MenuBarMenu {

    private String defaultText = "View";
    private int defaultMnemonic= 'V';

    private JDialog sliderDialog = null;
    private JTextField monthField1 = new JTextField();
    private JTextField dayField1 = new JTextField();
    private JTextField yearField1 = new JTextField();

    private JTextField monthField2 = new JTextField();
    private JTextField dayField2 = new JTextField();
    private JTextField yearField2 = new JTextField();

    public CDateLabeledSlider rangeSlider = RangeSliderPanel.rangeSlider;  // replace with events when we have time

    private JDialog cDateDialog = null;
    private JTextField monthField = new JTextField();
    private JTextField dayField = new JTextField();
    private JTextField yearField = new JTextField();

    /**
     * Create and add menuitems(About, SaveProperties,SaveAsImage and
     * Exit)
     */
    public ScenarioViewMenu() {
        super();
        setText(defaultText);
        setMnemonic(defaultMnemonic);
        createAndAdd();
    }
    
    /** Create and add default menu items */
    public void createAndAdd()
    {

        JMenuItem menuItem = new JMenuItem("All Unit Visibility");
        add (menuItem);

        menuItem.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              ScenarioMap.mapBean.findPspIconLayer().showIconDialog();
            }
          });

        menuItem = new JMenuItem("Selected Unit Visibility");
        add (menuItem);
        menuItem.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              ScenarioMap.mapBean.findPspIconLayer().showSelectionDialog(ScenarioMap.mapBean);
            }
          });

//        sliderDialog = new JDialog(frame, "Set Slider Range", true);
        sliderDialog = new JDialog ((JFrame)null, "Set Slider Range", true);
        sliderDialog.setResizable(false);
        sliderDialog.getContentPane().setLayout(new BorderLayout());
        sliderDialog.getContentPane().add(getSliderDialogPanel(), BorderLayout.CENTER);
        sliderDialog.pack();
        menuItem = add (new JMenuItem("Slider Range"));

        menuItem.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
//              sliderDialog.setLocationRelativeTo(frame);

              GregorianCalendar cal = new GregorianCalendar();
              Date date = new Date();

              date.setTime(((long)rangeSlider.getMinValue())*1000L);
              cal.setTime(date);
              monthField1.setText("" + (cal.get(Calendar.MONTH)+1));
              dayField1.setText("" + cal.get(Calendar.DAY_OF_MONTH));
              yearField1.setText("" + cal.get(Calendar.YEAR));

              date.setTime(((long)rangeSlider.getMaxValue())*1000L);
              cal.setTime(date);
              monthField2.setText("" + (cal.get(Calendar.MONTH)+1));
              dayField2.setText("" + cal.get(Calendar.DAY_OF_MONTH));
              yearField2.setText("" + cal.get(Calendar.YEAR));

              sliderDialog.show();
            }
          });


        menuItem = (JCheckBoxMenuItem) add(new JCheckBoxMenuItem("Use C-Date", false));
        menuItem.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              rangeSlider.setUseCDate(((JCheckBoxMenuItem)e.getSource()).isSelected());
            }
          });


        cDateDialog = new JDialog((JFrame)null, "Set C-Date", true);
        cDateDialog.setResizable(false);
        cDateDialog.getContentPane().setLayout(new BorderLayout());
        cDateDialog.getContentPane().add(getCDateDialogPanel(), BorderLayout.CENTER);
        cDateDialog.pack();
        menuItem = add(new JMenuItem("Enter C-Date"));
        menuItem.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
//              sliderDialog.setLocationRelativeTo(frame);

              GregorianCalendar cal = new GregorianCalendar();
              Date date = new Date();

              date.setTime(rangeSlider.getCDate());
              cal.setTime(date);
              monthField.setText("" + (cal.get(Calendar.MONTH)+1));
              dayField.setText("" + cal.get(Calendar.DAY_OF_MONTH));
              yearField.setText("" + cal.get(Calendar.YEAR));

              cDateDialog.show();
            }
          });


        menuItem = add(new JMenuItem("Icon +200%"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
             ScenarioMap.mapBean.findPspIconLayer().changeIconScale (2.0f);
            }
        });

        menuItem = add(new JMenuItem("Icon -200%"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
              ScenarioMap.mapBean.findPspIconLayer().changeIconScale (-2.0f);
            }
        });

        menuItem = add (new JMenuItem("Clear Route Displays"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
              ScenarioMap.mapBean.findPspIconLayer().clearAllRouteGraphics();
            }
        });


    }


  private JPanel getSliderDialogPanel()
  {

    JPanel panel = new JPanel ( new GridLayout(3, 4) );
    JButton button = new JButton("Set Range");

    button.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            int month1 = Integer.parseInt(monthField1.getText());
            int day1 = Integer.parseInt(dayField1.getText());
            int year1 = Integer.parseInt(yearField1.getText());


            int month2 = Integer.parseInt(monthField2.getText());
            int day2 = Integer.parseInt(dayField2.getText());
            int year2 = Integer.parseInt(yearField2.getText());

            long time1 = dateToMillis(month1+"/"+day1+"/"+year1, ((long)rangeSlider.getMinValue()) * 1000L)/1000L;
            long time2 = dateToMillis(month2+"/"+day2+"/"+year2, ((long)rangeSlider.getMaxValue()) * 1000L)/1000L;

            rangeSlider.setSliderRange(time1, time2);
            rangeSlider.setValue(rangeSlider.getValue());

            ScenarioMap.mapBean.findPspIconLayer().myState.buildDailyNLUnits(((long)rangeSlider.getMinValue()) * 1000L, ((long)rangeSlider.getMaxValue()) * 1000L);
            ScenarioMap.mapBean.findPspIconLayer().setTime("" + (((long)rangeSlider.getValue()) * 1000L));
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }

          sliderDialog.hide();
        }
      });

    panel.add(new JLabel(" ", SwingConstants.CENTER));
    panel.add(new JLabel("Month", SwingConstants.CENTER));
    panel.add(new JLabel("Day", SwingConstants.CENTER));
    panel.add(new JLabel("Year", SwingConstants.CENTER));

    panel.add(new JLabel("Starting Date"));
    panel.add(monthField1);
    panel.add(dayField1);
    panel.add(yearField1);

    panel.add(new JLabel("Ending Date"));
    panel.add(monthField2);
    panel.add(dayField2);
    panel.add(yearField2);

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BorderLayout());
    panel2.add(panel, BorderLayout.CENTER);
    panel2.add(button, BorderLayout.SOUTH);

    return(panel2);
  }



  private long dateToMillis(String dateString, long defaultTime)
  {
    long time = defaultTime;

    try
    {
      int month = 0;
      int day = 0;
      int year = 0;

      StringTokenizer tokenizer = new StringTokenizer(dateString.trim(), "/", false);

      month = Integer.parseInt(tokenizer.nextToken()) -1;
      day = Integer.parseInt(tokenizer.nextToken());
      year = Integer.parseInt(tokenizer.nextToken());

      GregorianCalendar cal = new GregorianCalendar();
      cal.set(year, month, day);

      time = cal.getTime().getTime();
    }
    catch (Exception e)
    {
      System.err.println("Unable to convert date to millis: " + e);
    }

    return(time);
  }



  private JPanel getCDateDialogPanel()
  {
    JPanel panel = new JPanel(new GridLayout(2, 3));
    JButton button = new JButton("Set C-Date");

    button.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            int month = Integer.parseInt(monthField.getText());
            int day = Integer.parseInt(dayField.getText());
            int year = Integer.parseInt(yearField.getText());

            GregorianCalendar cal = new GregorianCalendar();
            cal.set(year, month-1, day, 0, 0, 0);

            rangeSlider.setCDate(cal.getTime().getTime());
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }

          cDateDialog.hide();
        }
      });

    panel.add(new JLabel("Month", SwingConstants.CENTER));
    panel.add(new JLabel("Day", SwingConstants.CENTER));
    panel.add(new JLabel("Year", SwingConstants.CENTER));

    panel.add(monthField);
    panel.add(dayField);
    panel.add(yearField);

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BorderLayout());
    panel2.add(panel, BorderLayout.CENTER);
    panel2.add(button, BorderLayout.SOUTH);

    return(panel2);
  }



    /**
     * This method does nothing, but is required as a part of
     * MenuInterface
     */
    public void findAndUnInit(Iterator it){}

    /**
     * This method does nothing, but is required as a part of
     * MenuInterface
     */
    public void findAndInit(Iterator it){}
    
    /** 
     * When this method is called, it sets the given BeanContext on
     * menu items that need to find objects to get their work done.
     * Note: Menuitems are not added to beancontext 
     */
    public void setBeanContext(BeanContext in_bc) throws PropertyVetoException {
        super.setBeanContext(in_bc);
        if(!Environment.isApplication()) { //running as an Applet
            return;
        }
//        if (spMenu != null) spMenu.setMapHandler(getMapHandler());
//        if (jpegMenuItem != null) jpegMenuItem.setMapHandler(getMapHandler());
//        if (gifMenuItem != null) gifMenuItem.setMapHandler(getMapHandler());
    }
}