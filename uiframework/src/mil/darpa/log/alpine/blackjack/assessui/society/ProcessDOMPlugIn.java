package mil.darpa.log.alpine.blackjack.assessui.society;

import mil.darpa.log.alpine.blackjack.assessui.util.AggInfoEncoder;

import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

import org.cougaar.lib.aggagent.ldm.PlanObject;

import org.cougaar.core.plugin.SimplePlugIn;
import org.cougaar.core.cluster.IncrementalSubscription;

import org.cougaar.util.UnaryPredicate;

import org.w3c.dom.*;

class DocumentPredicate implements UnaryPredicate {
  public boolean execute (Object o) {
    boolean ret = false;
    if (o instanceof PlanObject) {
      ret = true;
    }
    return ret;
  }
}

public class ProcessDOMPlugIn extends SimplePlugIn {

  private IncrementalSubscription documents;
  private String xml_output;

  protected void setupSubscriptions() {
    documents = (IncrementalSubscription) subscribe (new DocumentPredicate());
  }

  protected void execute() {

    Enumeration doc_enum = documents.elements();

    int index = 1;

    while (doc_enum.hasMoreElements()) {

      PlanObject po = (PlanObject) doc_enum.nextElement();

      Document dom = po.getDocument();

      if (dom != null) {

        NodeList nl = dom.getElementsByTagName (AggInfoEncoder.getDataSetXMLString());

        if (nl.getLength() > 0) {

          xml_output = AggInfoEncoder.getStartXMLString();
          CreateXMLOutputString (dom.getDocumentElement());

          System.out.println ("***************************************");
          System.out.println ("Sending XML to EJB");
          System.out.println ("***************************************");
//          System.out.println (xml_output);
          index++;

          SendXMLOutputString ();

          // Remove the object from the log plan
          publishRemove (po);

        } /* end of if */
      } /* end of if */
    } /* end of while */
  } /* end of execute */

  private void CreateXMLOutputString (Node mynode) {

    int index;

    if (mynode.getNodeType() == Node.TEXT_NODE)
      xml_output += mynode.getNodeValue();
    else
      xml_output += "<" + mynode.getNodeName() + ">";

    NodeList mylist = mynode.getChildNodes();
    index = 0;

    while (index < mylist.getLength()) {
      CreateXMLOutputString (mylist.item (index));
      index++;
    }

    if (mynode.getNodeType() != Node.TEXT_NODE)
    {
      xml_output += "</" + mynode.getNodeName() + ">";
    }

  } /* end of CreateXMLOutputString */

  private void SendXMLOutputString () {

    try {
      URL maintainerURL = new URL("http://192.233.51.155:8000/BJAssessment/DBMaintainer");
      URLConnection con = maintainerURL.openConnection();
      con.setDoInput(true);
      con.setDoOutput(true);
      con.setUseCaches(false);
      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      DataOutputStream out = new DataOutputStream(con.getOutputStream());
      String content = "updateXML=" + xml_output;
      out.writeBytes(content);
      out.flush();
      out.close();

      BufferedReader d = new BufferedReader(new InputStreamReader (con.getInputStream()));
      StringBuffer returnString = new StringBuffer();
      while (true)
      {
        String line = d.readLine();
        if (line == null) break;
        returnString.append(line);
      }
      System.out.println(returnString);
    } /* end of try */
    catch (Exception e)
    {
      e.printStackTrace();
    } /* end of catch */

  } /* end of SendXMLOutputString */

} /* end of class ProcessDOMPlugIn */

