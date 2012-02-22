package parsers;

import java.util.*;
import org.dom4j.*;
import org.joda.time.*;
import model.*;

public class DukeCalParser extends TivooParser {
    
    public List<TivooEvent> convertToList(Document doc) {
	List<Node> list = trySelectNodes(doc, "//*[name()='event']");
	List<TivooEvent> eventlist = new ArrayList<TivooEvent>();
	for (Node n: list) {
	    String title = getNodeStringValue(n, "./*[name()='summary']");
	    String description = getNodeStringValue(n, "./*[name()='description']");
	    DateTime starttime = parseTime(n, "./start/*[name()='utcdate']");
	    DateTime endtime = parseTime(n, "./end/*[name()='utcdate']");
	    eventlist.add(new TivooEvent(title, starttime, endtime, description));
	}
	return eventlist;
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