package mil.darpa.log.alpine.blackjack.assessui.util;

public class AggInfoStructure {

  private String org;
  private String item;
  private String time;
  private String start_time;
  private String end_time;
  private String value;
  private String rate;

  static private final String org_xml_string = "org";
  static private final String begin_org_xml_string = "<org>";
  static private final String end_org_xml_string = "</org>";
  static private final String item_xml_string = "item";
  static private final String begin_item_xml_string = "<item>";
  static private final String end_item_xml_string = "</item>";
  static private final String time_xml_string = "time";
  static private final String begin_time_xml_string = "<time>";
  static private final String stop_time_xml_string = "</time>";
  static private final String start_time_xml_string = "start_time";
  static private final String begin_start_time_xml_string = "<start_time>";
  static private final String end_start_time_xml_string = "</start_time>";
  static private final String end_time_xml_string = "end_time";
  static private final String begin_end_time_xml_string = "<end_time>";
  static private final String end_end_time_xml_string = "</end_time>";
  static private final String value_xml_string = "value";
  static private final String begin_value_xml_string = "<value>";
  static private final String end_value_xml_string = "</value>";
  static private final String rate_xml_string = "rate";
  static private final String begin_rate_xml_string = "<rate>";
  static private final String end_rate_xml_string = "</rate>";

  public AggInfoStructure (String new_org,
			 String new_item,
			 String new_time,
			 String new_value) {
    org = new_org;
    item = new_item;
    time = new_time;
    value = new_value;
    start_time = null;
    end_time = null;
    rate = null;
  }

  public AggInfoStructure (String new_org,
			 String new_item,
			 String new_start_time,
			 String new_end_time,
			 String new_rate) {
    org = new_org;
    item = new_item;
    start_time = new_start_time;
    end_time = new_end_time;
    rate = new_rate;
    time = null;
    value = null;
  }

  public String getOrg () {
    return org;
  }

  static public String getOrgXMLString () {
    return org_xml_string;
  }

  static public String getOrgStartXMLString () {
    return begin_org_xml_string;
  }

  static public String getOrgEndXMLString () {
    return end_org_xml_string;
  }

  public String getItem () {
    return item;
  }

  static public String getItemXMLString () {
    return item_xml_string;
  }

  static public String getItemStartXMLString () {
    return begin_item_xml_string;
  }

  static public String getItemEndXMLString () {
    return end_item_xml_string;
  }

  public String getTime () {
    return time;
  }

  static public String getTimeXMLString () {
    return time_xml_string;
  }

  static public String getTimeStartXMLString () {
    return begin_time_xml_string;
  }

  static public String getTimeEndXMLString () {
    return stop_time_xml_string;
  }

  public String getStartTime () {
    return start_time;
  }

  static public String getStartTimeXMLString () {
    return start_time_xml_string;
  }

  static public String getStartTimeStartXMLString () {
    return begin_start_time_xml_string;
  }

  static public String getStartTimeEndXMLString () {
    return end_start_time_xml_string;
  }

  public String getEndTime () {
    return end_time;
  }

  public void setEndTime (String new_end_time) {
    end_time = new_end_time;
  }

  static public String getEndTimeXMLString () {
    return end_time_xml_string;
  }

  static public String getEndTimeStartXMLString () {
    return begin_end_time_xml_string;
  }

  static public String getEndTimeEndXMLString () {
    return end_end_time_xml_string;
  }

  public String getValue () {
    return value;
  }

  public void setValue (String new_value) {
    value = new_value;
  }

  static public String getValueXMLString () {
    return value_xml_string;
  }

  static public String getValueStartXMLString () {
    return begin_value_xml_string;
  }

  static public String getValueEndXMLString () {
    return end_value_xml_string;
  }

  public String getRate () {
    return rate;
  }

  public void setRate (String new_rate) {
    rate = new_rate;
  }

  static public String getRateXMLString () {
    return rate_xml_string;
  }

  static public String getRateStartXMLString () {
    return begin_rate_xml_string;
  }

  static public String getRateEndXMLString () {
    return end_rate_xml_string;
  }

}
