
package org.cougaar.lib.uiframework.ui.orgui;

import java.util.*;

import org.cougaar.lib.aggagent.dictionary.GenericLogic;

/**
 *  For purposes of testing, this TestAdapter acts like an OrgSubAdapter,
 *  except that it generates a predetermined data set rather than reading
 *  one from a COUGAAR society (which may not exist in a test environment).
 */
public class TestAdapter extends OrgSubAdapter {
  // set the interval of the generated test relationships to a very long time.
  private long start = 0;
  private long end = 1000000000000000L;

  /**
   *  Override the normal function (of parsing the relationship schedule) with
   *  the generation of a test data set.
   *  @param matches ignored
   *  @param eventType the type of event--only ADD events generate output
   */
  public void execute (Collection matches, String eventType) {
    System.out.println("TestAdapter::execute");
    event = eventType;
    if (!event.equals(GenericLogic.collectionType_ADD))
      return;

    // invent some dummy data...      
    // relations.add(new Bond(name, relName, role.getName(), start, end));
    relations.add(new Bond("3ID", "1BDE", Const.SUBORDINATE, start, end));
    relations.add(new Bond("1BDE", "3-69-ARBN", Const.SUBORDINATE, start, end));
    relations.add(new Bond("1BDE", "Pakistan", Const.SUBORDINATE, start, end));
    relations.add(new Bond("1BDE", "3FSB", Const.SUBORDINATE, start, end));
  }
}