package mil.darpa.log.alpine.blackjack.assessui.society;

import mil.darpa.log.alpine.blackjack.assessui.util.AggInfoEncoder;

import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

import org.cougaar.lib.aggagent.ldm.XMLPlanObject;

import org.cougaar.core.plugin.SimplePlugIn;
import org.cougaar.core.cluster.IncrementalSubscription;

import org.cougaar.util.UnaryPredicate;

import org.w3c.dom.*;

class XMLStringPredicate implements UnaryPredicate {
  public boolean execute (Object o) {
    boolean ret = false;
    if (o instanceof XMLPlanObject) {
      ret = true;
    }
    return ret;
  }
}

public class ProcessXMLPlugIn extends SimplePlugIn {

  private IncrementalSubscription strings;
  private StringBuffer xml_output;

  private String http_address;

  protected void setupSubscriptions() {
    strings = (IncrementalSubscription) subscribe (new XMLStringPredicate());

    http_address = getStringParameter ("httpaddress=", getParameters().elements(), "http://192.233.51.155:8000/BJAssessment/DBMaintainer");

    System.out.println ("http_address for the bean is " + http_address);
  }

  protected void execute() {

    Enumeration string_enum = strings.elements();

    while (string_enum.hasMoreElements()) {

      XMLPlanObject xml_po = (XMLPlanObject) string_enum.nextElement();

      String xml_string = xml_po.getDocument();

      if (xml_string != null) {

        xml_output = new StringBuffer(xml_string);

        SendXMLOutputString ();

        System.out.println ("*****Done with transmit of XML to EJB*****");

        // Remove the object from the log plan
        publishRemove (xml_po);

      } /* end of if */
    } /* end of while */
  } /* end of execute */

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

      System.out.println ("-----Sending XML to EJB-----");

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

