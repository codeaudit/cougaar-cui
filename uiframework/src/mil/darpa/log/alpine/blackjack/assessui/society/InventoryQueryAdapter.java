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
import org.cougaar.domain.glm.ldm.asset.ScheduledContentPG;
import org.cougaar.domain.glm.ldm.plan.QuantityScheduleElementImpl;

import org.cougaar.domain.planning.ldm.measure.CountRate;
import org.cougaar.domain.planning.ldm.measure.Rate;
import org.cougaar.domain.planning.ldm.plan.AspectType;
import org.cougaar.domain.planning.ldm.plan.Preference;
import org.cougaar.domain.planning.ldm.plan.PrepositionalPhrase;
import org.cougaar.domain.planning.ldm.plan.Schedule;
import org.cougaar.domain.planning.ldm.asset.Asset;
import org.cougaar.domain.planning.ldm.asset.TypeIdentificationPG;

public class InventoryQueryAdapter extends CustomQueryBaseAdapter {

  private static final String metric = "Inventory";

  private String output_xml;
  private boolean send_xml = false;
  private int xml_count;
  private AggInfoEncoder myEncoder = new AggInfoEncoder();
  private AggInfoStructure nextStructure = null;

  public void execute (Collection matches, String eventName) {

    Iterator iter = matches.iterator();
    int index;
    long earliest_time = 0;

    index = 0;
    xml_count = 0;

    output_xml = myEncoder.encodeStartOfXML();

    while (iter.hasNext()) {

      Object o = (Object) iter.next();

      if (o instanceof Inventory) {

        Inventory in = (Inventory) o;
        String org = null;
        String item = null;
        String start_time = null;
        String end_time = null;
        long start_time_long = 0;
        long end_time_long = 0;
        Double time_double = new Double(0.0);
        String rate = null;

        ScheduledContentPG scheduled_content =
                                    in.getScheduledContentPG();

        if (scheduled_content == null)
          continue;

        Asset a1 = scheduled_content.getAsset();
        Schedule s1 = scheduled_content.getSchedule();

        if (a1 == null) {
          System.out.println ("WARNING: no asset in scheduledContentPG");
          continue;
        }

        if (s1 == null) {
          System.out.println ("WARNING: no schedule in scheduledContentPG");
          continue;
        }

        TypeIdentificationPG type_id_pg = a1.getTypeIdentificationPG();

        if (type_id_pg == null) {
          System.out.println ("WARNING: no typeIdentificationPG for asset");
          continue;
        }

        item = getItemId (type_id_pg.getTypeIdentification());

        UID object_name = a1.getUID();

        if (object_name == null) {
          System.out.println ("WARNING: no UID for asset");
          continue;
        }

        org = object_name.getOwner();

        Enumeration schedule_list = s1.getAllScheduleElements();

        while (schedule_list.hasMoreElements()) {
          QuantityScheduleElementImpl element;

          element = (QuantityScheduleElementImpl) schedule_list.nextElement();

          rate = new String ("" + element.getQuantity());

          // Pull out the start time and put it in a string

          time_double = new Double (element.getStartTime());

          start_time_long = time_double.longValue ();
          start_time = String.valueOf (start_time_long);

          if (earliest_time == 0)
            earliest_time = start_time_long;
          else if (start_time_long < earliest_time)
            earliest_time = start_time_long;

          // Pull out the end time and put it in a string

          time_double = new Double (element.getEndTime());

          end_time_long = time_double.longValue ();
          end_time = String.valueOf (end_time_long);

          // One more record parsed
          index++;

          // If the structure is null, create one
          if (nextStructure == null) {
            nextStructure = new AggInfoStructure (org, item, start_time, end_time, metric, rate);
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
            writeStructureToXML (nextStructure);
            nextStructure = new AggInfoStructure (org, item, start_time, end_time, metric, rate);
          }
        } /* end of while */

      } /* if o is Inventory */
    } /* while iter */

    if (nextStructure != null) {
      writeStructureToXML (nextStructure);
    }

    System.out.println ("**************************************************************************");
    System.out.println ("Inventory sending " + index + " records, earliest start time is " + earliest_time + ", amounts to " + xml_count + " xml records");
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

/*
    if (xml_count == 1) {
      System.out.println ("item is " + new_structure.getItem());
      System.out.println ("org is " + new_structure.getOrg());
      System.out.println ("start_time is " + new_structure.getStartTime());
      System.out.println ("end_time is " + new_structure.getEndTime());
      System.out.println ("rate is " + new_structure.getRate());
    }
*/

  } /* end of writeStructureToXML */

  private String getItemId(String id) {

    String itemType;
    String itemId;

    if (id.startsWith("NSN/")) {
      itemType = "NSN";
      itemId = id.substring(itemType.length() + 1); // Mind the '/'
      return itemId;
    }
    else if (id.startsWith("CAGEPN/")) {
      itemType = "CAGEPN";
      itemId = id.substring(itemType.length() + 1); // Mind the '/'
      return itemId;
    }
    else
      return id;
  } /* end of getItemId */

}