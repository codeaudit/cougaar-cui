package mil.darpa.log.alpine.blackjack.assessui.society;

import java.util.*;
import java.io.*;

import org.cougaar.lib.aggagent.dictionary.glquery.samples.CustomQueryBaseAdapter;

import mil.darpa.log.alpine.blackjack.assessui.util.AggInfoStructure;
import mil.darpa.log.alpine.blackjack.assessui.util.AggInfoEncoder;

import org.cougaar.domain.glm.plugins.TaskUtils;

import org.cougaar.domain.planning.ldm.asset.Asset;
import org.cougaar.domain.planning.ldm.measure.CountRate;
import org.cougaar.domain.planning.ldm.measure.Rate;
import org.cougaar.domain.planning.ldm.plan.AspectType;
import org.cougaar.domain.planning.ldm.plan.Preference;
import org.cougaar.domain.planning.ldm.plan.PrepositionalPhrase;
import org.cougaar.domain.planning.ldm.plan.Task;

public class DemandQueryAdapter extends CustomQueryBaseAdapter {

  private static final String metric = "Demand";

  private String output_xml;
  private boolean send_xml = false;
  private AggInfoEncoder myEncoder = new AggInfoEncoder();
  private AggInfoStructure myStructure;

  public void execute (Collection matches) {

    Iterator iter = matches.iterator();
    int index;
    long earliest_time = 0;

    index = 0;

    output_xml = myEncoder.encodeStartOfXML();

    while (iter.hasNext()) {

      Object o = (Object) iter.next();

      if (o instanceof Task) {

        Task t = (Task) o;
        String org = null;
        String item = null;
        String start_time = null;
        String end_time = null;
        long time_long = 0;
        Double time_double = new Double(0.0);
        String rate = null;

        if (t.getVerb().equals("ProjectSupply") == true) {

          Asset direct_object = t.getDirectObject();

/*
          if (index < 5) {
            System.out.println ("**************************************************************************");
          }
*/

          Preference start_pref = t.getPreference (AspectType.START_TIME);
          if (start_pref != null) {
            time_double = new Double (start_pref.getScoringFunction().getBest().getAspectValue().getValue());
          }

          time_long = time_double.longValue ();
          start_time = String.valueOf (time_long);

          if (earliest_time == 0)
            earliest_time = time_long;
          else if (time_long < earliest_time)
            earliest_time = time_long;

          Preference end_pref = t.getPreference (AspectType.END_TIME);

          if (end_pref != null) {
            time_double = new Double (end_pref.getScoringFunction().getBest().getAspectValue().getValue());
          }

          time_long = time_double.longValue ();
          end_time = String.valueOf (time_long);

          Rate demand_rate = TaskUtils.getRate (t);

          if ((demand_rate != null) && (demand_rate instanceof CountRate)) {
            CountRate cr = (CountRate) demand_rate;
            rate = new String ("" + cr.getEachesPerDay());
          }
          else
            System.out.println ("No rate");

          PrepositionalPhrase for_phrase = t.getPrepositionalPhrase ("For");

          if (for_phrase != null) {
            org = (String) for_phrase.getIndirectObject();
            if (org.startsWith ("UIC/")) {
              org = org.substring (4);
            }
          }

          PrepositionalPhrase oftype_phrase = t.getPrepositionalPhrase ("OfType");

          if (oftype_phrase != null) {
            item = (String) oftype_phrase.getIndirectObject();
          }

/*
          if (index < 5) {
            System.out.println ("start_time is " + start_time);
            System.out.println ("end_time is " + end_time);
            System.out.println ("org is: " + org);
            System.out.println ("item is: " + item);
            System.out.println ("metric is: " + metric);
            System.out.println ("rate is: " + rate);
          }
*/

          myStructure = new AggInfoStructure (org, item, start_time, end_time, metric, rate);

          String output_string = myEncoder.encodeDataAtom(myStructure);

//          if (index < 25) {
          output_xml += output_string;
//          }

          send_xml = true;

          index++;

        } /* if verb is "ProjectSupply" */
      } /* if o is Task */
    } /* while iter */

    System.out.println ("**************************************************************************");
    System.out.println ("**************************************************************************");
    System.out.println ("**************************************************************************");
    System.out.println ("Sending " + index + " records, earliest start time is " + earliest_time);
    System.out.println ("**************************************************************************");
    System.out.println ("**************************************************************************");
    System.out.println ("**************************************************************************");

    output_xml += myEncoder.encodeEndOfXML();
  }

  public void returnVal (OutputStream out) {

    PrintWriter p = new PrintWriter (out);

    if (send_xml)
    {
      p.println (output_xml);
      p.flush ();
//      System.out.println (output_xml);
    }

    send_xml = false;
  }

}