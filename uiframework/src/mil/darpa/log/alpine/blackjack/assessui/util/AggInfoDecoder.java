package mil.darpa.log.alpine.blackjack.assessui.util;

import java.io.*;

import com.ibm.xml.parser.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class AggInfoDecoder {

  Document dom;
  NodeList element_list;
  int current_element = -1;

  public void startXMLDecoding (String new_xml_string) {

//    System.out.println ("In startXMLDecoding");

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

    // Set up the list of <data-atom> nodes

    element_list = dom.getElementsByTagName (AggInfoEncoder.getDataAtomXMLString());
    current_element = 0;
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
    String fieldname = null;
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
      else if (this_node.getNodeName().compareTo(AggInfoStructure.getFieldnameXMLString()) == 0) {
        fieldname = this_node.getFirstChild().getNodeValue();
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
      ret = new AggInfoStructure(org, item, time, fieldname, value);
    else
      ret = new AggInfoStructure(org, item, start_time, end_time, fieldname, rate);

    current_element++;

    return ret;
  }

  public static void main (String args[]) {
    AggInfoDecoder myDecoder = new AggInfoDecoder();
    AggInfoEncoder myEncoder = new AggInfoEncoder ();
    AggInfoStructure myStruct = new AggInfoStructure ("DEPT8H", "PEOPLE", "1", "SUM", "65");
    AggInfoStructure myStruct2 = new AggInfoStructure ("DEPT8H", "COMPUTERS", "1", "AVERAGE PER PERSON", "1.5");
    AggInfoStructure myStruct3 = new AggInfoStructure ("DEPT4B", "LAMPS", "2", "TOTAL PER OFFICE", "5");
    AggInfoStructure myStruct4 = new AggInfoStructure ("DEPT4B", "LAMPS", "1", "5", "DEMAND", "2");

    String line1 = myEncoder.encodeStartOfXML();
    String line2 = myEncoder.encodeDataAtom(myStruct);
    String line3 = myEncoder.encodeDataAtom(myStruct2);
    String line4 = myEncoder.encodeDataAtom(myStruct3);
    String line5 = myEncoder.encodeDataAtom(myStruct4);
    String line6 = myEncoder.encodeEndOfXML();

    System.out.print (line1);
    System.out.print (line2);
    System.out.print (line3);
    System.out.print (line4);
    System.out.print (line5);
    System.out.print (line6);

    myDecoder.startXMLDecoding (line1 + line2 + line3 + line4 + line5 + line6);
//    myDecoder.startXMLDecoding ("<?XML bull?>\n<data-set>\n</data-set>");
//    myDecoder.startXMLDecoding ("/ata-set>");
//    String text = "<?xml version=1.0 encoding=UTF-8?><data-set><data-atom><org>MyOrg </org><item>MyItem </item><time>MyTime </time><fieldname>MyFieldName </fieldname><value>MyValue </value></data-atom></data-set>";

//    myDecoder.startXMLDecoding (text);

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

      System.out.println ("Fieldname is " + mystruct.getFieldname());

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
