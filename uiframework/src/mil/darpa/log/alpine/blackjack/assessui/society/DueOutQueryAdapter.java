/*
 * <copyright>
 *  Copyright 2001 BBNT Solutions, LLC
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
package mil.darpa.log.alpine.blackjack.assessui.society;

import java.util.*;
import java.io.*;

import org.cougaar.lib.aggagent.dictionary.glquery.samples.CustomQueryBaseAdapter;
import org.cougaar.domain.glm.ldm.Constants;

import mil.darpa.log.alpine.blackjack.assessui.util.AggInfoStructure;
import mil.darpa.log.alpine.blackjack.assessui.util.AggInfoEncoder;

import org.cougaar.core.society.UID;

import org.cougaar.domain.glm.plugins.TaskUtils;
import org.cougaar.domain.glm.ldm.asset.Inventory;
import org.cougaar.domain.planning.ldm.asset.TypeIdentificationPG;

import org.cougaar.domain.planning.ldm.measure.CountRate;
import org.cougaar.domain.planning.ldm.measure.FlowRate;
import org.cougaar.domain.planning.ldm.measure.Rate;
import org.cougaar.domain.planning.ldm.plan.AspectType;
import org.cougaar.domain.planning.ldm.plan.Preference;
import org.cougaar.domain.planning.ldm.plan.PrepositionalPhrase;
import org.cougaar.domain.planning.ldm.plan.RoleSchedule;
import org.cougaar.domain.planning.ldm.plan.PlanElementImpl;
import org.cougaar.domain.planning.ldm.plan.Task;
import org.cougaar.domain.planning.ldm.asset.Asset;

public class DueOutQueryAdapter extends CustomQueryBaseAdapter {

  private static final String METRIC = "DueOut";

  private StringBuffer output_xml;
  private boolean send_xml;
  private int xml_count;
  private AggInfoEncoder myEncoder = new AggInfoEncoder();
  private AggInfoStructure nextStructure;

  private long c_time_msec = 0L;
  private static long dayMillis = 24L * 60L * 60L * 1000L;

  public void execute (Collection matches, String eventName) {

    Iterator iter = matches.iterator();
    int index;

    index = 0;
    xml_count = 0;
    output_xml = null;
    send_xml = false;
    nextStructure = null;
    String org = null;

    extractCDateFromSociety ();

    System.out.print ("(0o)");

try {

    while (iter.hasNext()) {

      Object o = (Object) iter.next();

      if (o instanceof Inventory) {

        Inventory in = (Inventory) o;

        String item = null;
        String start_time = null;
        String end_time = null;
        long temp_long = 0;
        long start_time_long = 0;
        long end_time_long = 0;
        Double temp_double = new Double(0.0);
        String rate = null;

        UID inventory_object_name = in.getUID();

        if (inventory_object_name == null) {
//          System.out.println ("WARNING: no UID for inventory asset");
          continue;
        }

        org = inventory_object_name.getOwner();

//        System.out.print ("org: " + org);

        RoleSchedule s1 = in.getRoleSchedule();

        if (s1 == null) {
//          System.out.println ("WARNING: no schedule in scheduledContentPG");
          continue;
        }

        Enumeration schedule_list = s1.getRoleScheduleElements();

        int looping = 0;

        while (schedule_list.hasMoreElements()) {
          PlanElementImpl plan_element;
          Task t;

          plan_element = (PlanElementImpl) schedule_list.nextElement();
          t = plan_element.getTask();

          // Only want to look at projected withdraw and acutal withdraw tasks

          if (t.getVerb().equals(Constants.Verb.PROJECTWITHDRAW) == true) {

              looping++;
//              System.out.print (" " + looping + " ");

//              System.out.print ("" + t.getVerb());

              Preference start_pref = t.getPreference (AspectType.START_TIME);
              if (start_pref != null) {
                temp_double = new Double (start_pref.getScoringFunction().getBest().getAspectValue().getValue());
              }
              else {
//                  System.out.println ("No start preference");
              }

              start_time_long = temp_double.longValue ();
              start_time = String.valueOf (convertMSecToCDate(start_time_long));

              // Pull out the end time and put it in a string

              Preference end_pref = t.getPreference (AspectType.END_TIME);

              if (end_pref != null) {
                temp_double = new Double (end_pref.getScoringFunction().getBest().getAspectValue().getValue());
              }
              else {
//                  System.out.println ("No end preference");
              }

              end_time_long = temp_double.longValue ();

              // If the times are equal, set the end time to one day in the
              // future.  This is so the withdrawal is computed to happen on
              // the start day.  (86400000 = 1000 * 24 * 60 * 60, i.e. number
              // of milliseconds in a day)
              if (start_time_long == end_time_long) {
                  end_time_long += 86400000;
              }
              end_time = String.valueOf (convertMSecToCDate(end_time_long));

//              System.out.print (" start" + start_time + ", end " + end_time);

              // Find the item name in direct object's type identification

              Asset direct_object = t.getDirectObject();

              if (direct_object == null) {
//                System.out.println ("WARNING: No direct object found");
                continue;
              }

              TypeIdentificationPG type_id_pg = direct_object.getTypeIdentificationPG();

              if (type_id_pg == null) {
//                System.out.println ("WARNING: no typeIdentificationPG for asset");
                continue;
              }

              item = type_id_pg.getTypeIdentification();

//              System.out.print (", item is: " + item);

              Rate demand_rate = TaskUtils.getRate (t);

              if ((demand_rate != null) && (demand_rate instanceof CountRate)) {
                CountRate cr = (CountRate) demand_rate;
                rate = new String ("" + cr.getEachesPerDay());
              }
              else if ((demand_rate != null) && (demand_rate instanceof FlowRate)) {
                FlowRate fr = (FlowRate) demand_rate;
                rate = new String ("" + fr.getGallonsPerDay());
              }
              else
              {
//                System.out.println ("WARNING: No rate for org " + org + ", item " + item);
                continue;
              }

//              System.out.print (", rate " + rate);

              // If the structure is null, create one
              if (nextStructure == null) {
                nextStructure = new AggInfoStructure (item, start_time, end_time, rate);
              }
              else if ((item.compareTo (nextStructure.getItem()) == 0) &&
                       (start_time.compareTo (nextStructure.getStartTime()) == 0) &&
                       (end_time.compareTo (nextStructure.getEndTime()) == 0)) {
//                  System.out.print (" adding to previous " + nextStructure.getRate());
                  double temp_rate = Double.parseDouble (nextStructure.getRate());
                  temp_rate += Double.parseDouble (rate);
                  rate = String.valueOf (temp_rate);
//                  System.out.print (" now " + rate);
                  nextStructure.setRate (rate);
              }
              // Output the record and start a new one
              else {
//                System.out.println ("");
                writeStructureToXML (org, nextStructure);
                nextStructure = new AggInfoStructure (item, start_time, end_time, rate);
              }

              // One more record parsed
              index++;
          } /* end of if verb is right */
        } /* end of while */

//        System.out.println ("");

      } /* if o is Inventory */
    } /* while iter */
}
catch (Exception e) {

    System.out.println ("Exception caught!");
    e.printStackTrace();
}

    if (nextStructure != null) {
      writeStructureToXML (org, nextStructure);
    }

    System.out.println ("");
    System.out.println ("**************************************************************************");
    System.out.println ("DueOutQueryAdapter: " + index + " records, " + xml_count + " xml records, for org " + org);
    System.out.println ("**************************************************************************");

    if (send_xml) {
      myEncoder.encodeEndOfXML(output_xml);
    }
  } /* end of execute */

  public void returnVal (OutputStream out) {

    PrintWriter p = new PrintWriter (out);

    if (send_xml)
    {
      p.println (output_xml.toString());
      p.flush ();
    }

/*
    if (xml_count > 0)
      System.out.println (output_xml);
*/

    send_xml = false;
  } /* end of returnVal */

  private void writeStructureToXML (String org, AggInfoStructure new_structure) {

    // We are getting ready to send a new one
    if (!send_xml) {
      output_xml = myEncoder.encodeStartOfXML(org, METRIC);
    }

    myEncoder.encodeDataAtom (output_xml, new_structure);

    xml_count++;

    send_xml = true;

    if ((xml_count % 500) == 0)
      System.out.print ("(" + xml_count + "o)");

  } /* end of writeStructureToXML */

  private void extractCDateFromSociety () {

    String cdate_property = System.getProperty("org.cougaar.core.cluster.startTime");
    String timezone_property = System.getProperty("user.timezone");

    TimeZone tz = TimeZone.getTimeZone(timezone_property);

    GregorianCalendar gc = new GregorianCalendar (tz);

    StringTokenizer st = new StringTokenizer (cdate_property, "/");

    String c_time_month_string = st.nextToken();
    String c_time_day_string = st.nextToken();
    String c_time_year_string = st.nextToken();

    // Month is offset from zero, others are not
    // Last three are hour, minute, second

    gc.set (Integer.parseInt (c_time_year_string),
            Integer.parseInt (c_time_month_string) - 1,
            Integer.parseInt (c_time_day_string),
            0, 0, 0);

    c_time_msec = gc.getTime().getTime();

    // This was needed to ensure that the milliseconds were set to 0
    c_time_msec = c_time_msec / 1000;
    c_time_msec *= 1000;

  } /* end of extractCDateFromSociety */

  private int convertMSecToCDate (long current_time_msec) {
    return (int) ((current_time_msec - c_time_msec) / dayMillis);
  }

}
