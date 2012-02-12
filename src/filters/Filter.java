package filters;

import java.util.*;
import model.*;
import org.dom4j.*;
import org.joda.time.*;

public class Filter {
    
    @SuppressWarnings("unchecked")
    public static List<Event> convertToList(Document doc) {
	List<Node> list = doc.selectNodes("//event");
	List<Event> eventlist = new ArrayList<Event>();
	for (Node n: list) {
	    Node titlefield = n.selectSingleNode("./summary");
	    Node descriptionfield = n.selectSingleNode("./description");
	    Node startfield = n.selectSingleNode("./start/utcdate");
	    DateTime starttime = TivooTimeHandler.createTimeUTC(startfield.getStringValue());
	    Node endfield = n.selectSingleNode("./end/utcdate");
	    DateTime endtime = TivooTimeHandler.createTimeUTC(endfield.getStringValue());
	    eventlist.add(new Event(titlefield.getStringValue(), 
		    starttime, endtime, descriptionfield.getStringValue()));
	}
	return eventlist;
    }
    
    public static List<Event> filterByTime(List<Event> eventlist, DateTime start, DateTime end) {
	List<Event> filtered = new ArrayList<Event>();
	for (Event e: eventlist) {
	    DateTime eventstart = e.getStart();
	    DateTime eventend = e.getEnd();
	    DateTimeComparator comp = DateTimeComparator.getInstance();
	    if (comp.compare(start, eventstart) <= 0 && comp.compare(eventend, end) <= 0)
		filtered.add(e);
	}
	return filtered;
    }
    
    public static List<Event> filterByTitle(List<Event> eventlist, String keyword) {
	List<Event> filtered = new ArrayList<Event>();
	for (Event e: eventlist) {
	    if (e.getTitle().toLowerCase().contains(keyword.toLowerCase())) 
		filtered.add(e);
	}
	return filtered;
    }
    
}