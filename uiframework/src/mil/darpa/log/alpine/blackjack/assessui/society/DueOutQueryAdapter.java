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

  private static final String METRIC = "DueOuts";

  private String output_xml;
  private boolean send_xml = false;
  private int xml_count;
  private AggInfoEncoder myEncoder = new AggInfoEncoder();
  private AggInfoStructure nextStructure = null;

  public void execute (Collection matches, String eventName) {

    Iterator iter = matches.iterator();
    int index;
    int inventory_count = 1;

    index = 0;
    xml_count = 0;

    output_xml = myEncoder.encodeStartOfXML(METRIC);

try {

    while (iter.hasNext()) {

      Object o = (Object) iter.next();

      if (o instanceof Inventory) {

        Inventory in = (Inventory) o;

        String org = null;
        String item = null;
        String start_time = null;
        String end_time = null;
        long temp_long = 0;
        long start_time_long = 0;
        long end_time_long = 0;
        Double temp_double = new Double(0.0);
        String quantity = null;

        UID inventory_object_name = in.getUID();

        if (inventory_object_name == null) {
          System.out.println ("WARNING: no UID for inventory asset");
          continue;
        }

        org = inventory_object_name.getOwner();

        System.out.print ("org: " + org);

        RoleSchedule s1 = in.getRoleSchedule();

        if (s1 == null) {
          System.out.println ("WARNING: no schedule in scheduledContentPG");
          continue;
        }

        Enumeration schedule_list = s1.getRoleScheduleElements();

        inventory_count++;

        int looping = 0;

        while (schedule_list.hasMoreElements()) {
          PlanElementImpl plan_element;
          Task t;

          plan_element = (PlanElementImpl) schedule_list.nextElement();
          t = plan_element.getTask();

          // Only want to look at projected withdraw and acutal withdraw tasks

          if ((t.getVerb().equals(Constants.Verb.PROJECTWITHDRAW) == true) ||
              (t.getVerb().equals(Constants.Verb.WITHDRAW) == true)) {

              looping++;
              System.out.print (" " + looping + " ");

              System.out.print ("" + t.getVerb());

              Preference start_pref = t.getPreference (AspectType.START_TIME);
              if (start_pref != null) {
                temp_double = new Double (start_pref.getScoringFunction().getBest().getAspectValue().getValue());
              }
              else
                  System.out.println ("No start preference");

              start_time_long = temp_double.longValue ();
              start_time = String.valueOf (start_time_long);

              // Pull out the end time and put it in a string

              Preference end_pref = t.getPreference (AspectType.END_TIME);

              if (end_pref != null) {
                temp_double = new Double (end_pref.getScoringFunction().getBest().getAspectValue().getValue());
              }
              else
                  System.out.println ("No end preference");

              end_time_long = temp_double.longValue ();

              // If the times are equal, set the end time to one day in the
              // future.  This is so the withdrawal is computed to happen on
              // the start day.  (86400000 = 1000 * 24 * 60 * 60, i.e. number
              // of milliseconds in a day)
              if (start_time_long == end_time_long) {
                  end_time_long += 86400000;
              }
              end_time = String.valueOf (end_time_long);

              System.out.print (" start" + start_time + ", end " + end_time);

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

              System.out.print (", item is: " + item);

              // Find out how many there are and put it in a string
              Preference quantity_pref = t.getPreference (AspectType.QUANTITY);
              if (quantity_pref != null) {
                temp_double = new Double (quantity_pref.getScoringFunction().getBest().getAspectValue().getValue());
              }
              else {
                  System.out.println ("No quantity, skipping record");
                  continue;
              }

              temp_long = temp_double.longValue ();
              quantity = String.valueOf (temp_long);

              System.out.println (", quantity " + quantity);

              // If the structure is null, create one
              if (nextStructure == null) {
                nextStructure = new AggInfoStructure (org, item, start_time, end_time, quantity);
              }
              // If the rates are the same, and the start time of the new record
              // is the same as the end time of the old record, then combine
              // the records
              else if ((quantity.compareTo (nextStructure.getRate ()) == 0) &&
                       (start_time.compareTo (nextStructure.getEndTime()) == 0)) {
                nextStructure.setEndTime (end_time);
              }
              // Output the record and start a new one
              else {
                writeStructureToXML (nextStructure);
                nextStructure = new AggInfoStructure (org, item, start_time, end_time, quantity);
              }

              // One more record parsed
              index++;
          } /* end of if verb is right */
        } /* end of while */

        System.out.println ("");

      } /* if o is Inventory */
    } /* while iter */
}
catch (Exception e) {

    System.out.println ("Exception caught!");
    e.printStackTrace();
}

    if (nextStructure != null) {
      writeStructureToXML (nextStructure);
    }

    System.out.println ("**************************************************************************");
    System.out.println ("DueOuts sending " + index + " records, amounts to " + xml_count + " xml records");
    System.out.println ("**************************************************************************");

    output_xml += myEncoder.encodeEndOfXML();
  } /* end of execute */

  public void returnVal (OutputStream out) {

    PrintWriter p = new PrintWriter (out);

    if (send_xml)
    {
      p.println (output_xml);
      p.flush ();
    }

/*
    if (xml_count > 0)
      System.out.println (output_xml);
*/

    send_xml = false;
  } /* end of returnVal */

  private void writeStructureToXML (AggInfoStructure new_structure) {
    String output_string = myEncoder.encodeDataAtom (new_structure);

    output_xml += output_string;

    xml_count++;

    send_xml = true;

  } /* end of writeStructureToXML */

}
