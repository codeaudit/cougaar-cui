package mil.darpa.log.alpine.blackjack.assessui.util;

public class AggInfoEncoder {

  private int start_state = 0; 
  private int atom_state = 1;

  private int state = 0;

  private static final String start_xml_string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  private static final String start_begin_xml_string = "<xml_begin>";
  private static final String end_begin_xml_string = "</xml_begin>";
  private static final String data_set_xml_string = "data-set";
  private static final String data_atom_xml_string = "data-atom";
  private static final String metric_xml_string = "metric";

  public static String getStartXMLString() {
    return start_xml_string;
  }

  public static String getStartBeginXMLString() {
    return start_begin_xml_string;
  }

  public static String getEndBeginXMLString() {
    return end_begin_xml_string;
  }

  public static String getDataSetXMLString () {
    return data_set_xml_string;
  }

  public static String getStartDataSetXMLString () {
    return "<" + data_set_xml_string + ">";
  }

  public static String getEndDataSetXMLString () {
    return "</" + data_set_xml_string + ">";
  }

  public static String getDataAtomXMLString () {
    return data_atom_xml_string;
  }

  public static String getStartDataAtomXMLString () {
    return "<" + data_atom_xml_string + ">";
  }

  public static String getEndDataAtomXMLString () {
    return "</" + data_atom_xml_string + ">";
  }

  public static String getMetricXMLString () {
    return metric_xml_string;
  }

  public static String getStartMetricXMLString () {
    return "<" + metric_xml_string + ">";
  }

  public static String getEndMetricXMLString () {
    return "</" + metric_xml_string + ">";
  }

  public String encodeStartOfXML (String metric) {
    if (state != start_state) {
      System.out.println ("Must finish previous XML before starting another one");
      return null;
    }

    String ret = new String();

    ret = ret + getStartXMLString();

    ret = ret + getStartBeginXMLString();

    ret = ret + getStartMetricXMLString();
    ret = ret + metric;
    ret = ret + getEndMetricXMLString();

    ret = ret + getStartDataSetXMLString();

    state = atom_state;

    return ret;
  }

  public String encodeDataAtom (AggInfoStructure next_structure) {

     if (state != atom_state) {
       System.out.println ("Need to have written a start of XML message first");
       return null;
     }

     String ret = new String();

     ret = ret + getStartDataAtomXMLString();
     ret = ret + AggInfoStructure.getOrgStartXMLString() + next_structure.getOrg() + AggInfoStructure.getOrgEndXMLString();
     ret = ret + AggInfoStructure.getItemStartXMLString() + next_structure.getItem() + AggInfoStructure.getItemEndXMLString();

     if (next_structure.getTime() != null) {
       ret = ret + AggInfoStructure.getTimeStartXMLString() + next_structure.getTime() + AggInfoStructure.getTimeEndXMLString();
     }
     else {
       ret = ret + AggInfoStructure.getStartTimeStartXMLString() + next_structure.getStartTime() + AggInfoStructure.getStartTimeEndXMLString();
       ret = ret + AggInfoStructure.getEndTimeStartXMLString() + next_structure.getEndTime() + AggInfoStructure.getEndTimeEndXMLString();
     }

     if (next_structure.getValue() != null) {
       ret = ret + AggInfoStructure.getValueStartXMLString() + next_structure.getValue() + AggInfoStructure.getValueEndXMLString();
     }
     else {
       ret = ret + AggInfoStructure.getRateStartXMLString() + next_structure.getRate() + AggInfoStructure.getRateEndXMLString();
     }

     ret = ret + getEndDataAtomXMLString();

     return ret;
  }

  public String encodeEndOfXML () {

    if (state != atom_state) {
       System.out.println ("Need to have written a start of XML message first");
       return null;
    }

    String ret = new String();

    ret = ret + getEndDataSetXMLString();
    ret = ret + getEndBeginXMLString();

    state = start_state;

    return ret;
  }

  public static void main (String args[]) {
    AggInfoStructure myStruct = new AggInfoStructure ("DEPT8H", "PEOPLE", "1", "65");
    AggInfoStructure myStruct2 = new AggInfoStructure ("DEPT8H", "COMPUTERS", "1", "1.5");
    AggInfoStructure myStruct3 = new AggInfoStructure ("DEPT8H", "COMPUTERS", "1", "5", "5");
    AggInfoEncoder myEncoder = new AggInfoEncoder();

    System.out.print (myEncoder.encodeStartOfXML("DEMAND"));
    System.out.print (myEncoder.encodeDataAtom(myStruct));
    System.out.print (myEncoder.encodeDataAtom(myStruct2));
    System.out.print (myEncoder.encodeDataAtom(myStruct3));
    System.out.print (myEncoder.encodeEndOfXML());
  }

}
