package mil.darpa.log.alpine.blackjack.assessui.util;

import java.io.*;

import com.ibm.xml.parser.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class AggInfoDecoder {

  Document dom;
  NodeList element_list;
  int current_element = -1;

  public String startXMLDecoding (String new_xml_string) {

//    System.out.println ("In startXMLDecoding");

    String metric;
    StringReader sr = new StringReader (new_xml_string);
    InputSource is = new InputSource (sr);
    Parser p = new Parser (".");

    try {
      p.parse (is);
      dom = p.getDocument();
    }
    catch (Exception e) {
      System.out.println ("Could not parse XML string");
    }

    NodeList metric_list = dom.getElementsByTagName (AggInfoEncoder.getMetricXMLString());

    // Grab the first element
    Node current_node = metric_list.item (0);

    if (current_node == null)
    {
      System.out.println ("It's null");
      return null;
    }

    metric = current_node.getFirstChild().getNodeValue();

    System.out.println ("metric is " + metric);

    // Set up the list of <data-atom> nodes

    element_list = dom.getElementsByTagName (AggInfoEncoder.getDataAtomXMLString());
    current_element = 0;

    return metric;
  }

  public boolean doneXMLDecoding () {
//    System.out.println ("In doneXMLDecoding");

    if (current_element < 0)
    {
      System.out.println ("Initialize Decoder with call to startXMLDecoding");
      return true;
    }

    return (current_element >= element_list.getLength());
  }

  public AggInfoStructure getNextDataAtom () {
//    System.out.println ("In getNextDataAtom");

    if (doneXMLDecoding())
      return null;

    Node current_node = element_list.item (current_element);

    if (current_node == null)
      return null;

    NodeList child_list = current_node.getChildNodes();
    String org = null;
    String item = null;
    String time = null;
    String start_time = null;
    String end_time = null;
    String value = null;
    String rate = null;

    for (int index = 0; index < child_list.getLength(); index++)
    {
      Node this_node = child_list.item(index);
      if (this_node.getNodeName().compareTo(AggInfoStructure.getOrgXMLString()) == 0) {
        org = this_node.getFirstChild().getNodeValue();
      }
      else if (this_node.getNodeName().compareTo(AggInfoStructure.getItemXMLString()) == 0) {
        item = this_node.getFirstChild().getNodeValue();
      }
      else if (this_node.getNodeName().compareTo(AggInfoStructure.getTimeXMLString()) == 0) {
        time = this_node.getFirstChild().getNodeValue();
      }
      else if (this_node.getNodeName().compareTo(AggInfoStructure.getStartTimeXMLString()) == 0) {
        start_time = this_node.getFirstChild().getNodeValue();
      }
      else if (this_node.getNodeName().compareTo(AggInfoStructure.getEndTimeXMLString()) == 0) {
        end_time = this_node.getFirstChild().getNodeValue();
      }
      else if (this_node.getNodeName().compareTo(AggInfoStructure.getValueXMLString()) == 0) {
        value = this_node.getFirstChild().getNodeValue();
      }
      else if (this_node.getNodeName().compareTo(AggInfoStructure.getRateXMLString()) == 0) {
        rate = this_node.getFirstChild().getNodeValue();
      }
    }

    AggInfoStructure ret;

    if (time != null)
      ret = new AggInfoStructure(org, item, time, value);
    else
      ret = new AggInfoStructure(org, item, start_time, end_time, rate);

    current_element++;

    return ret;
  }

  public static void main (String args[]) {
    AggInfoDecoder myDecoder = new AggInfoDecoder();
    AggInfoEncoder myEncoder = new AggInfoEncoder ();
    AggInfoStructure myStruct = new AggInfoStructure ("DEPT8H", "PEOPLE", "1", "65");
    AggInfoStructure myStruct2 = new AggInfoStructure ("DEPT8H", "COMPUTERS", "1", "1.5");
    AggInfoStructure myStruct3 = new AggInfoStructure ("DEPT4B", "LAMPS", "2", "5");
    AggInfoStructure myStruct4 = new AggInfoStructure ("DEPT4B", "LAMPS", "1", "5", "2");

    StringBuffer line = myEncoder.encodeStartOfXML("DEMAND");
    myEncoder.encodeDataAtom(line, myStruct);
    myEncoder.encodeDataAtom(line, myStruct2);
    myEncoder.encodeDataAtom(line, myStruct3);
    myEncoder.encodeDataAtom(line, myStruct4);
    myEncoder.encodeEndOfXML(line);

    System.out.print (line.toString());

    String metric = null;

    metric = myDecoder.startXMLDecoding (line.toString());

//    myDecoder.startXMLDecoding ("<?XML bull?>\n<data-set>\n</data-set>");
//    myDecoder.startXMLDecoding ("/ata-set>");
//    String text = "<?xml version=1.0 encoding=UTF-8?><metric>DEMAND</metric><data-set><data-atom><org>MyOrg </org><item>MyItem </item><time>MyTime </time><value>MyValue </value></data-atom></data-set>";

//    myDecoder.startXMLDecoding (text);

    System.out.println ("Metric is " + metric);

    while (!myDecoder.doneXMLDecoding())
    {
      System.out.println ("Trying...");
      AggInfoStructure mystruct = myDecoder.getNextDataAtom();

      System.out.println ("Org is " + mystruct.getOrg());
      System.out.println ("Item is " + mystruct.getItem());

      if (mystruct.getTime() != null) {
        System.out.println ("Time is " + mystruct.getTime());
      }
      else {
        System.out.println ("Start Time is " + mystruct.getStartTime());
        System.out.println ("End Time is " + mystruct.getEndTime());
      }

      if (mystruct.getValue() != null) {
        System.out.println ("Value is " + mystruct.getValue());
      }
      else {
        System.out.println ("Rate is " + mystruct.getRate());
      }
    }
    System.out.println ("DONE!");
  }

}
