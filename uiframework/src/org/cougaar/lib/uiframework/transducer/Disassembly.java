
package org.cougaar.lib.uiframework.transducer;

import org.cougaar.lib.uiframework.transducer.elements.*;

/**
 *  The abstract SqlTransducer uses this interface to store Structures in the
 *  database.  Concrete implementations of this interface that are consistent
 *  with a particular SqlTransducer instance should be supplied by the instance
 *  in question.
 */
public interface Disassembly {
  /**
   *  Parse a Structure and save its components in the database.  Before this
   *  method can be called, the implementation must already be aware of which
   *  Structure is to be saved and where it should go in the DB.  Different
   *  implementations will be required for different transducers.
   */
  public void writeToDb ();
}