package parsers;

import java.util.*;
import org.dom4j.*;
import org.joda.time.*;
import org.joda.time.format.*;
import model.*;

public class GoogleCalParser extends TivooParser {

    public List<TivooEvent> convertToList(Document doc) {
	List<Node> list = trySelectNodes(doc, "//*[name()='entry']");
	String timezone = 
	  ((Element) doc.selectSingleNode("//*[name()='gCal:timezone']")).attributeValue("value");
	List<TivooEvent> eventlist = new ArrayList<TivooEvent>();
	for (Node n: list) {
	    String title = getNodeStringValue(n, "./*[name()='title']");
	    String description = getNodeStringValue(n, "./*[name()='content']");
	    Node timefield = trySelectSingleNode(n, "./*[name()='summary']");
	    String timestring = timefield.getStringValue();
	    if (timestring.startsWith("Recurring")) {
		parseRecurringEvent();
		continue;
	    }
	    List<DateTime> startend = parseOneTimeEvent(timestring, timezone);
	    if (startend == null) continue;
	    eventlist.add(new TivooEvent(sanitizeString(title), startend.get(0), startend.get(1), 
		    sanitizeString(description)));
	}
	return eventlist;
    }
    
    public boolean wellFormed(Document doc) {
    	String rootname = doc.getRootElement().getName();
    	return (rootname.contentEquals("feed"));
    }
    
    private List<DateTime> parseOneTimeEvent(String timestring, String timezone) {
	DateTimeZone thezone = DateTimeZone.forID(timezone);
	String[] splitted = timestring.split(" ");
	if (splitted.length < 8) return null;
	//When: Wed Sep 21, 2011 5:30pm to 6pm&nbsp; EDT<br> <br>Event Status: confirmed
	String month = splitted[2], 
		date = splitted[3].substring(0, splitted[3].length() - 1), 
		year = splitted[4], starttime = splitted[5], 
		endtime = splitted[7].split("&")[0];
	DateTimeFormatter monthformat = DateTimeFormat.forPattern("MMM");
	int _month = monthformat.parseDateTime(month).getMonthOfYear();
	int _date = Integer.parseInt(date);
	int _year = Integer.parseInt(year);
	DateTimeParser[] parsers = { 
	        DateTimeFormat.forPattern("hh:mmaa").getParser(),
	        DateTimeFormat.forPattern("hhaa").getParser() };
	DateTimeFormatter hourformat = new DateTimeFormatterBuilder()
		.append(null, parsers).toFormatter();
	int _starthour = hourformat.parseDateTime(starttime).getHourOfDay();
	int _startminute = hourformat.parseDateTime(starttime).getMinuteOfHour();
	int _endhour = hourformat.parseDateTime(endtime).getHourOfDay();
	int _endminute = hourformat.parseDateTime(endtime).getMinuteOfHour();
	DateTime start = new DateTime(_year, _month, 
		_date, _starthour, _startminute, thezone);
	DateTime end = new DateTime(_year, _month, 
		_date, _endhour, _endminute, thezone);
	List<DateTime> toreturn = new ArrayList<DateTime>();
	toreturn.add(start); toreturn.add(end);
	return toreturn;
    }
    
    private DateTime parseRecurringEvent() {
	//TODO
	return null;
    }
    
}