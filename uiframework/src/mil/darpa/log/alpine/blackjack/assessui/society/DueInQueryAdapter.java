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
import org.cougaar.domain.glm.ldm.plan.AlpineAspectType;
import org.cougaar.domain.planning.ldm.plan.AspectType;
import org.cougaar.domain.planning.ldm.plan.AspectRate;
import org.cougaar.domain.planning.ldm.plan.Preference;
import org.cougaar.domain.planning.ldm.plan.Expansion;
import org.cougaar.domain.planning.ldm.plan.Task;
import org.cougaar.domain.planning.ldm.plan.Workflow;
import org.cougaar.domain.planning.ldm.asset.Asset;

public class DueInQueryAdapter extends CustomQueryBaseAdapter {

  private static final String METRIC = "DueIn";

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

    System.out.print ("(0in)");

try {

    while (iter.hasNext()) {

      Object o = (Object) iter.next();

      if (o instanceof Task) {

        Task t = (Task) o;

        String item = null;
        String start_time = null;
        String end_time = null;

        long start_time_long = 0;
        long end_time_long = 0;
        double rate_double;
        Double temp_double = new Double(0.0);
        String rate = null;

        if (t.getVerb().equals (Constants.Verb.MAINTAININVENTORY) == true) {

          // The plan element is actually an expansion
          Expansion ex = (Expansion) t.getPlanElement();

          if (ex == null) {
            System.out.println ("Expansion is null");
            continue;
          }

          // Grab the workflow
          Workflow w = ex.getWorkflow();

          if (w == null) {
            System.out.println ("Workflow is null");
            continue;
          }

          // Fill in the org
          if (org == null) {
            UID duein_object_name = t.getUID();

            if (duein_object_name == null) {
              System.out.println ("No UID");
              continue;
            }

            org = duein_object_name.getOwner();

//            System.out.println ("Org is " + org);
          } /* end of if org is null */

          // Get a list of the child tasks for the workflow of the expansion
          Enumeration child_task_list = w.getTasks();

          // Each one contains a Refill task that is a DueIn
          while (child_task_list.hasMoreElements()) {

            Task refill = (Task) child_task_list.nextElement();

            Asset direct_object = refill.getDirectObject();
            if (direct_object == null) {
              System.out.println ("No direct object for refill");
              continue;
            }

            TypeIdentificationPG type_id_pg = direct_object.getTypeIdentificationPG();
            if (type_id_pg == null) {
              System.out.println ("No typeIdentificationPG for refill do");
              continue;
            }

            item = type_id_pg.getTypeIdentification();

            // Pull out the rate and put it in a string

            Preference rate_pref = refill.getPreference (AlpineAspectType.DEMANDRATE);
            if (rate_pref != null) {
                AspectRate myAspectRate = (AspectRate) rate_pref.getScoringFunction().getBest().getAspectValue();
                Rate myRate = (Rate) myAspectRate.getRateValue();

                if ((myRate != null) && (myRate instanceof CountRate)) {
                    CountRate cr = (CountRate) myRate;
                    rate_double = cr.getUnitsPerSecond();
                    // rate_double is in units per second; make it units per day
                    rate_double *= 60.0 * 60.0 * 24.0;
                }
                else if ((myRate != null) && (myRate instanceof FlowRate)) {
                    FlowRate fr = (FlowRate) myRate;
                    rate_double = fr.getGallonsPerDay();
                }
                else {
                    System.out.println ("No rate information");
                    continue;
                }
            }
            else {
              System.out.println ("No rate information");
              continue;
            }

            rate = String.valueOf (rate_double);

            // Pull out the start time and put it in a string

            Preference start_pref = refill.getPreference (AspectType.START_TIME);
            if (start_pref != null) {
              temp_double = new Double (start_pref.getScoringFunction().getBest().getAspectValue().getValue());
            }

            start_time_long = temp_double.longValue ();
            start_time = String.valueOf (convertMSecToCDate(start_time_long));

            // Pull out the end time and put it in a string

            Preference end_pref = refill.getPreference (AspectType.END_TIME);

            if (end_pref != null) {
              temp_double = new Double (end_pref.getScoringFunction().getBest().getAspectValue().getValue());
            }

            end_time_long = temp_double.longValue ();
            end_time = String.valueOf (convertMSecToCDate(end_time_long));

//            System.out.println ("(" + index + ") item: " + item +
//                                ", rate: " + rate + ", start: " + start_time +
//                                ", end: " + end_time);

            // One more record parsed
            index++;


            // If the structure is null, create one
            if (nextStructure == null) {
              nextStructure = new AggInfoStructure (item, start_time, end_time, rate);
            }
            // If the rates are the same, and the start time of the new record
            // is the same as the end time of the old record, then combine
            // the records
            else if ((rate.compareTo (nextStructure.getRate ()) == 0) &&
                     (start_time.compareTo (nextStructure.getEndTime()) == 0)) {
              nextStructure.setEndTime (end_time);
            }
            // Output the record and start a new one
            else {
              writeStructureToXML (org, nextStructure);
              nextStructure = new AggInfoStructure (item, start_time, end_time, rate);
            }

          } /* end of while */

        } /* end of if verb is right */

      } /* if o is Task */
    } /* while iter */
}
catch (Exception e) {

    System.out.println ("Exception caught!");
    e.printStackTrace();
}

    if (nextStructure != null) {
      writeStructureToXML (org, nextStructure);
    }

    System.out.println ("**************************************************************************");
    System.out.println ("DueInQueryAdapter: " + index + " records, " + xml_count + " xml records, for org " + org);
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
      System.out.print ("(" + xml_count + "in)");

  } /* end of writeStructureToXML */

  // All start and stop times should be converted to C-date so they
  // can be displayed meaningfully to the user interfaces
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
