package parsers;

import java.util.*;

import model.*;
import org.dom4j.*;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import sharedattributes.*;

public class DukeBasketBallParser extends TivooParser {

    public DukeBasketBallParser() {
	setEventNodePath("//*[name()='Calendar']");
	setEventType(new DukeBasketBallEventType());
	updateNoNeedParseMap(new Title(), "./*[name()='Subject']");
	updateNoNeedParseMap(new Description(), "./*[name()='Description']");
	updateNoNeedParseMap(new Location(), "./*[name()='Location']");

    }
    
    public boolean wellFormed(Document doc) {
    	String rootname = doc.getRootElement().getName();
    	return (rootname.contentEquals("dataroot"));
    }

    protected void topLevelParsing(Document doc) {}

    protected void eventLevelParsing(Node n, Map<TivooAttribute, Object> grabdatamap,
	    List<List<DateTime>> recurringstartend) {
	String startdatestring = getNodeStringValue(n, "./*[name()='StartDate']");
	String starttimestring = getNodeStringValue(n, "./*[name()='StartTime']");
	String enddatestring = getNodeStringValue(n, "./*[name()='EndDate']");
	String endtimestring = getNodeStringValue(n, "./*[name()='EndTime']");
	DateTime starttime = parseTime(startdatestring.concat(" " + starttimestring));
	DateTime endtime = parseTime(enddatestring.concat(" " + endtimestring));
	grabdatamap.put(new StartTime(), starttime);
	grabdatamap.put(new EndTime(), endtime);
    }
    
    private DateTime parseTime(String timestring) {
	//<StartDate>11/11/2011</StartDate>
	//<StartTime>9:00:00 PM</StartTime>
	    DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/YYYY hh:mm:ss aa");
	return formatter.parseDateTime(timestring);
    }

    private class DukeBasketBallEventType extends TivooEventType {

	private DukeBasketBallEventType() {
	    @SuppressWarnings("serial")
	    Set<TivooAttribute> toadd = new HashSet<TivooAttribute>() {{
		    add(new Location());
	    }};
	    addSpecialAttributes(toadd);
	}
	
    }
    
}
