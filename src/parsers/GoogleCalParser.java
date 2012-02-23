package parsers;

import java.util.*;
import org.dom4j.*;
import org.joda.time.*;
import org.joda.time.format.*;
import model.*;

public class GoogleCalParser extends TivooParser {

    private DateTimeZone timezone;
    
    public GoogleCalParser() {
	setEventNodePath("//*[name()='entry']");
	setEventType(TivooEvent.event_type.GOOGLE_EVENT);
	updateNoNeedParseMap(attribute_type.TITLE, "./*[name()='title']");
	updateNoNeedParseMap(attribute_type.DESCRIPTION, "./*[name()='content']");
    }
    
    protected void topLevelParsing(Document doc) {
	 timezone = DateTimeZone.forID(((Element) doc.selectSingleNode("//*[name()='gCal:timezone']"))
		.attributeValue("value"));
    }
    
    protected void eventLevelParsing(Node n, Map<attribute_type, Object> grabdatamap,
	    List<List<DateTime>> recurringstartend) {
	String timestring = sanitizeString(getNodeStringValue(n, "./*[name()='summary']"));
	if (timestring.startsWith("Recurring")) {
	    recurringstartend.addAll(parseRecurringEvent(timestring));
	    return;
	}
	List<DateTime> startend = parseOneTimeEvent(timestring);
	grabdatamap.put(attribute_type.STARTTIME, startend.get(0));
	grabdatamap.put(attribute_type.ENDTIME, startend.get(1));
    }
    
    public boolean wellFormed(Document doc) {
    	String rootname = doc.getRootElement().getName();
    	return (rootname.contentEquals("feed"));
    }
    
    private String[] fixTime(String[] splitted) {
	//When: Wed Sep 21, 2011 EDT Event Status: confirmed
	String[] fixed = new String[8];
	for (int i = 0; i < splitted.length; i++) fixed[i] = splitted[i];
	fixed[5] =  "12:01am"; fixed[7] = "11:59pm";
	return fixed;
    }
    
    private List<DateTime> parseOneTimeEvent(String timestring) {
	String[] splitted = timestring.split("\\s+");
	if (splitted.length < 12)
	    splitted = Arrays.copyOf(fixTime(splitted), 8);
	//When: Wed Sep 21, 2011 5:30pm to 6pm EDT Event Status: confirmed
	String month = splitted[2], 
		date = splitted[3].replaceAll(",", ""), 
		year = splitted[4], 
		starttime = splitted[5], 
		endtime = splitted[7];
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
		_date, _starthour, _startminute, timezone);
	DateTime end = new DateTime(_year, _month, 
		_date, _endhour, _endminute, timezone);
	List<DateTime> toreturn = new ArrayList<DateTime>();
	toreturn.add(start); toreturn.add(end);
	return toreturn;
    }
    
    private List<List<DateTime>> parseRecurringEvent (String timestring) {
	List<DateTime> recurringstart = new ArrayList<DateTime>();
	List<DateTime> recurringend = new ArrayList<DateTime>();
	//Recurring Event First start: 2011-08-30 15:00:00 EDT Duration: 3600 Event Status: confirmed
	String[] splitted = timestring.split("\\s+");
	String starttime = splitted[4].concat(splitted[5]);
	DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-ddHH:mm:ss")
		.withZone(timezone);
	DateTime firststart = formatter.parseDateTime(starttime);
	int durationsec = Integer.parseInt(splitted[8]);
	DateTime firstend = firststart.plusSeconds(durationsec);
	for (int i = 0; i < 52; i++) {
	    DateTime tempstart = new DateTime(firststart);
	    DateTime tempend = new DateTime(firstend);
	    recurringstart.add(tempstart);
	    recurringend.add(tempend);
	    firststart = firststart.plusWeeks(1);
	    firstend = firstend.plusWeeks(1);
	}
	List<List<DateTime>> toreturn = new ArrayList<List<DateTime>>();
	toreturn.add(recurringstart); toreturn.add(recurringend);
	return toreturn;
    }

}