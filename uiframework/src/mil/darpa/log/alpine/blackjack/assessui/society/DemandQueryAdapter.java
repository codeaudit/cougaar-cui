package mil.darpa.log.alpine.blackjack.assessui.society;

import java.util.*;
import java.io.*;

import org.cougaar.core.society.UID;

import org.cougaar.lib.aggagent.dictionary.glquery.samples.CustomQueryBaseAdapter;
import org.cougaar.domain.glm.ldm.Constants;

import mil.darpa.log.alpine.blackjack.assessui.util.AggInfoStructure;
import mil.darpa.log.alpine.blackjack.assessui.util.AggInfoEncoder;

import org.cougaar.domain.glm.plugins.TaskUtils;

import org.cougaar.domain.planning.ldm.asset.Asset;
import org.cougaar.domain.planning.ldm.asset.TypeIdentificationPG;

import org.cougaar.domain.planning.ldm.measure.CountRate;
import org.cougaar.domain.planning.ldm.measure.FlowRate;
import org.cougaar.domain.planning.ldm.measure.Rate;
import org.cougaar.domain.planning.ldm.plan.AspectType;
import org.cougaar.domain.planning.ldm.plan.PlanElement;
import org.cougaar.domain.planning.ldm.plan.Preference;
import org.cougaar.domain.planning.ldm.plan.PrepositionalPhrase;
import org.cougaar.domain.planning.ldm.plan.Task;

public class DemandQueryAdapter extends CustomQueryBaseAdapter {

  private static final String METRIC = "Demand";

  private String output_xml;
  private boolean send_xml = false;
  private AggInfoEncoder myEncoder = new AggInfoEncoder();
  private AggInfoStructure myStructure;

  public void execute (Collection matches, String eventName) {

    Iterator iter = matches.iterator();
    int index;

    index = 0;

    output_xml = myEncoder.encodeStartOfXML(METRIC);

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

        if (t.getVerb().equals(Constants.Verb.PROJECTSUPPLY) == true) {

          // Pull out the start time and put it in a string

          Preference start_pref = t.getPreference (AspectType.START_TIME);
          if (start_pref != null) {
            time_double = new Double (start_pref.getScoringFunction().getBest().getAspectValue().getValue());
          }

          time_long = time_double.longValue ();
          start_time = String.valueOf (time_long);

          // Pull out the end time and put it in a string

          Preference end_pref = t.getPreference (AspectType.END_TIME);

          if (end_pref != null) {
            time_double = new Double (end_pref.getScoringFunction().getBest().getAspectValue().getValue());
          }

          time_long = time_double.longValue ();
          end_time = String.valueOf (time_long);

          // Find the organization in the "For" preposition

          PrepositionalPhrase for_phrase = t.getPrepositionalPhrase ("For");

          if (for_phrase != null) {
            org = (String) for_phrase.getIndirectObject();
            if (org.startsWith ("UIC/")) {
              org = org.substring (4);
            }
          }

          // Is this right?
          // Is this right?
          PlanElement pe = t.getPlanElement();
          UID cluster_object_name = pe.getUID();

          if (cluster_object_name == null) {
            System.out.println ("WARNING: no UID for plan element of demand task");
            continue;
          }

          System.out.print ("demand cluster is " +
                              cluster_object_name.getOwner() + ", ");

          // Find the item name in direct object's type identification

          Asset direct_object = t.getDirectObject();

          if (direct_object == null) {
            System.out.println ("WARNING: No direct object found");
            continue;
          }

          TypeIdentificationPG type_id_pg = direct_object.getTypeIdentificationPG();

          if (type_id_pg == null) {
            System.out.println ("WARNING: no typeIdentificationPG for asset");
            continue;
          }

          item = type_id_pg.getTypeIdentification();

          // Find what the demand rate is and put it in a string

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
            System.out.println ("WARNING: No rate for org " + org + ", item " + item);
            continue;
          }

          System.out.println ("org is: " + org + ", item is: " + item);

/*
          if (index < 5) {
            System.out.println ("start_time is " + start_time);
            System.out.println ("end_time is " + end_time);
            System.out.println ("org is: " + org);
            System.out.println ("item is: " + item);
            System.out.println ("rate is: " + rate);
          }
*/

          myStructure = new AggInfoStructure (org, item, start_time, end_time, rate);

          String output_string = myEncoder.encodeDataAtom(myStructure);

          output_xml += output_string;

          send_xml = true;

          index++;

        } /* if verb is "ProjectSupply" */
      } /* if o is Task */
    } /* while iter */

    System.out.println ("**************************************************************************");
    System.out.println ("DemandQueryAdapter sending " + index + " records");
    System.out.println ("**************************************************************************");

    output_xml += myEncoder.encodeEndOfXML();
  } /* end of execute */

  public void returnVal (OutputStream out) {

    PrintWriter p = new PrintWriter (out);

    if (send_xml)
    {
      p.println (output_xml);
      p.flush ();
//      System.out.println (output_xml);
    }

    send_xml = false;
  } /* end of returnVal */

}
