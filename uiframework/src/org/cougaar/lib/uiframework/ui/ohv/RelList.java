package org.cougaar.lib.uiframework.ui.ohv;

import java.util.Iterator;
import java.awt.event.ItemListener;
import java.awt.Choice;




 /**
  List of Relationship types in the model.
 **/

  public class RelList extends Choice {
    public RelList(OrgHierModel model, ItemListener itemListener) {
      for (Iterator itt=model.getRelationshipTypes().iterator();
            itt.hasNext(); ) {
            String tmp=(String)itt.next();
	    if (tmp!=null) {
		add(tmp);
	    }
      }
      addItemListener(itemListener);
    }     


  }    // end class

