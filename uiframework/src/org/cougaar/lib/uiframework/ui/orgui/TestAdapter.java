
package org.cougaar.lib.uiframework.ui.orgui;

import java.util.*;

import org.cougaar.lib.aggagent.dictionary.GenericLogic;

public class TestAdapter extends OrgSubAdapter {
  private long start = 0;
  private long end = 1000000000000000L;

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