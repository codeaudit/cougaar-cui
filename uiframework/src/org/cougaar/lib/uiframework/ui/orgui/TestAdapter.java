
package org.cougaar.lib.uiframework.ui.orgui;

import java.util.*;

import org.cougaar.lib.aggagent.dictionary.GenericLogic;

/**
 *  For purposes of testing, this TestAdapter acts like an OrgSubAdapter,
 *  except that it generates a predetermined data set rather than reading
 *  one from a COUGAAR society (which may not exist in a test environment).
 */
public class TestAdapter extends OrgSubAdapter {
  // the name of the subordinate role in a command relationship
  private static String SELF_RELATION = "Self";
  private static String SUBORDINATE = "AdministrativeSubordinate";
  private static String SUPERIOR = "AdministrativeSuperior";
  private static String FOOD_PROVIDER = "SubsistenceSupplyProvider";
  private static String FOOD_CUSTOMER = "SubsistenceSupplyCustomer";

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
    event = eventType;
    if (!event.equals(GenericLogic.collectionType_ADD))
      return;

    // invent some dummy data...
    selfRelation("DLAHQ");
    selfRelation("3ID-HHC");
    selfRelation("1-BDE-3ID-HHC");
    selfRelation("3-69-ARBN");
    selfRelation("1-24-INFBN");
    selfRelation("3-FSB");
    commandRelation("3ID-HHC", "1-BDE-3ID-HHC");
    commandRelation("1-BDE-3ID-HHC", "3-69-ARBN");
    commandRelation("1-BDE-3ID-HHC", "1-24-INFBN");
    commandRelation("1-BDE-3ID-HHC", "3-FSB");
    foodSupport("3-FSB", "3ID-HHC");
    foodSupport("3-FSB", "1-BDE-3ID-HHC");
    foodSupport("3-FSB", "3-69-ARBN");
    foodSupport("3-FSB", "1-24-INFBN");
  }

  private void commandRelation (String superior, String subordinate) {
    relations.add(new Bond(superior, subordinate, SUBORDINATE, start, end));
    relations.add(new Bond(subordinate, superior, SUPERIOR, start, end));
  }

  private void foodSupport (String provider, String customer) {
    relations.add(new Bond(provider, customer, FOOD_CUSTOMER, start, end));
    relations.add(new Bond(customer, provider, FOOD_PROVIDER, start, end));
  }

  private void selfRelation (String self) {
    relations.add(new Bond(self, self, SELF_RELATION, start, end));
  }
}