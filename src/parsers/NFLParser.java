package parsers;

import java.util.*;
import org.dom4j.*;
import org.joda.time.*;
import org.joda.time.format.*;
import model.*;
import sharedattributes.*;

public class NFLParser extends TivooParser {

    public NFLParser() {
	setEventNodePath("//*[name()='row']");
	setEventType(new NFLEventType());
	updateNoNeedParseMap(new Title(), "./*[name()='Col1']");
	updateNoNeedParseMap(new Description(), "./*[name()='Col2']");
	updateNoNeedParseMap(new Location(), "./*[name()='Col15']");
    }
    
    public boolean wellFormed(Document doc) {
    	String rootname = doc.getRootElement().getName();
    	return (rootname.contentEquals("document"));
    }
    
    public TivooEventType getEventType() {
	return new NFLEventType();
    }

    protected void topLevelParsing(Document doc) {}

    protected void eventLevelParsing(Node n,
	    Map<TivooAttribute, Object> grabdatamap,
	    List<List<DateTime>> recurringstartend) {
	DateTime starttime = parseTime(n, "./*[name()='Col8']");
	DateTime endtime = parseTime(n, "./*[name()='Col9']");
	grabdatamap.put(new StartTime(), starttime);
	grabdatamap.put(new EndTime(), endtime);
    }
    
    private DateTime parseTime(Node n, String xpath) {
	String timestring = getNodeStringValue(n, xpath);
	DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
	return formatter.parseDateTime(timestring).plusHours(12);
    }
    
    private class NFLEventType extends TivooEventType {
	
	private NFLEventType() {
	    @SuppressWarnings("serial")
	    Set<TivooAttribute> localSpecialAttributes = new HashSet<TivooAttribute>() {{
		add(new Location());
	    }};
	    addSpecialAttributes(localSpecialAttributes);
	}

	public String toString() {
	    return "NFL";
	}
	
    }

}
