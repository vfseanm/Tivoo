package parsers;

import java.util.*;
import org.dom4j.*;
import org.joda.time.*;
import sharedattributes.*;
import model.*;

public class DukeCalParser extends TivooParser {
    
    public DukeCalParser() {
	setEventNodePath("//*[name()='event']");
	setEventType(new DukeCalEventType());
	updateNoNeedParseMap(new Title(), "./*[name()='summary']");
	updateNoNeedParseMap(new Description(), "./*[name()='description']");
	updateNoNeedParseMap(new Location(), "./location/*[name()='address']");
    }
    
    public TivooEventType getEventType() {
	return new DukeCalEventType();
    }
    
    protected void topLevelParsing(Document doc) {}
    
    protected void eventLevelParsing(Node n, Map<TivooAttribute, Object> grabdatamap,
	    List<List<DateTime>> recurringstartend) {
	DateTime starttime = parseTime(n, "./start/*[name()='utcdate']");
	DateTime endtime = parseTime(n, "./end/*[name()='utcdate']");
	grabdatamap.put(new StartTime(), starttime);
	grabdatamap.put(new EndTime(), endtime);
    }
    
    public boolean wellFormed(Document doc) {
    	String rootname = doc.getRootElement().getName();
    	return (rootname.contentEquals("events"));
    }
    
    private DateTime parseTime(Node n, String xpath) {
	String timestring = getNodeStringValue(n, xpath);
	return TivooTimeHandler.createTimeUTC(timestring);
    }

    private class DukeCalEventType extends TivooEventType {
	
	private DukeCalEventType() {
	    @SuppressWarnings("serial")
	    Set<TivooAttribute> localSpecialAttributes = new HashSet<TivooAttribute>() {{
		add(new Location());
	    }};
	    addSpecialAttributes(localSpecialAttributes);
	}
	
	public String toString() {
	    return "Duke Calendar";
	}
	
    }
    
}