package parsers;

import java.util.*;
import org.dom4j.*;
import org.joda.time.*;
import model.*;

public class DukeCalParser extends TivooParser {
    
    public DukeCalParser() {
	setEventNodePath("//*[name()='event']");
	setEventType(TivooEvent.event_type.DUKE_EVENT);
	updateNoNeedParseMap(attribute_type.TITLE, "./*[name()='summary']");
	updateNoNeedParseMap(attribute_type.DESCRIPTION, "./*[name()='description']");
    }
    
    protected void topLevelParsing(Document doc) {}
    
    protected void eventLevelParsing(Node n, Map<attribute_type, Object> grabdatamap,
	    List<List<DateTime>> recurringstartend) {
	DateTime starttime = parseTime(n, "./start/*[name()='utcdate']");
	DateTime endtime = parseTime(n, "./end/*[name()='utcdate']");
	grabdatamap.put(attribute_type.STARTTIME, starttime);
	grabdatamap.put(attribute_type.ENDTIME, endtime);
    }
    
    public boolean wellFormed(Document doc) {
    	String rootname = doc.getRootElement().getName();
    	return (rootname.contentEquals("events"));
    }
    
    private DateTime parseTime(Node n, String xpath) {
	String timestring = getNodeStringValue(n, xpath);
	return TivooTimeHandler.createTimeUTC(timestring);
    }

}