/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
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
  private StringBuffer xml_output;

  private String http_address;

  protected void setupSubscriptions() {
    documents = (IncrementalSubscription) subscribe (new DocumentPredicate());

    http_address = getStringParameter ("httpaddress=", getParameters().elements(), "http://192.233.51.155:8000/BJAssessment/DBMaintainer");

    System.out.println ("http_address for the bean is " + http_address);
  }

  protected void execute() {

    Enumeration doc_enum = documents.elements();

    while (doc_enum.hasMoreElements()) {

      PlanObject po = (PlanObject) doc_enum.nextElement();

      Document dom = po.getDocument();

      if (dom != null) {

        NodeList nl = dom.getElementsByTagName (AggInfoEncoder.getDataSetXMLString());

        if (nl.getLength() > 0) {

          xml_output = new StringBuffer(AggInfoEncoder.getStartXMLString());
          CreateXMLOutputString (dom.getDocumentElement());

          SendXMLOutputString ();

          System.out.println ("***************************************");
          System.out.println ("Sent XML to EJB");
          System.out.println ("***************************************");

          // Remove the object from the log plan
          publishRemove (po);

        } /* end of if */
      } /* end of if */
    } /* end of while */
  } /* end of execute */

  private void CreateXMLOutputString (Node mynode) {

    int index;

    if (mynode.getNodeType() == Node.TEXT_NODE)
      xml_output.append(mynode.getNodeValue());
    else {
      xml_output.append("<");
      xml_output.append(mynode.getNodeName());
      xml_output.append(">");
    }

    NodeList mylist = mynode.getChildNodes();
    index = 0;

    while (index < mylist.getLength()) {
      CreateXMLOutputString (mylist.item (index));
      index++;
    }

    if (mynode.getNodeType() != Node.TEXT_NODE)
    {
      xml_output.append("</");
      xml_output.append(mynode.getNodeName());
      xml_output.append(">");
    }

  } /* end of CreateXMLOutputString */

  private void SendXMLOutputString () {

    try {
//      URL maintainerURL = new URL("http://192.233.51.155:8000/BJAssessment/DBMaintainer");
      URL maintainerURL = new URL(http_address);
      URLConnection con = maintainerURL.openConnection();
      con.setDoInput(true);
      con.setDoOutput(true);
      con.setUseCaches(false);
      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      DataOutputStream out = new DataOutputStream(con.getOutputStream());
      String content = "updateXML=" + xml_output.toString();

      System.out.println ("***************************************");
      System.out.println ("Sending XML to EJB");
      System.out.println ("***************************************");

      out.writeBytes(content);
      out.flush();
      out.close();

      System.out.println ("Waiting for EJB response");

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

    /**
   * Return a String parameter from the head of a list of plugin parameters
   * @param Enumeration of Plugin command line parameters
   * @param integer default value if no numeric value found
   * @return int value parsed from first numeric argument
   */
  public static String getStringParameter(String prefix, Enumeration parameters,
                                          String default_value)
  {
    while(parameters.hasMoreElements()) {
      String param = (String)parameters.nextElement();
      if (param.startsWith(prefix)) {
        String sVal = param.substring(prefix.length()).trim();
        return sVal;
      }
    }
    return default_value;
  }

} /* end of class ProcessDOMPlugIn */

