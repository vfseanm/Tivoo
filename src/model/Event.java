package model;
import java.util.*;
import org.joda.time.*;

public class Event {

    private String myTitle;
    private DateTime myCanonicalStartTime;
    private DateTime myCanonicalEndTime;
    private String myDescription;
    
    public Event(String title, DateTime starttime, DateTime endtime) {
	myTitle = title;
	myCanonicalStartTime = starttime;
	myCanonicalEndTime = endtime;
    }
    
    public Event(String title, DateTime starttime, DateTime endtime, String description) {
	myTitle = title;
	myCanonicalStartTime = starttime;
	myCanonicalEndTime = endtime;
	myDescription = description;
    }
    
    public String getTitle() {
	return myTitle;
    }
    
    public DateTime getStart() {
	return myCanonicalStartTime;
    }
    
    public DateTime getEnd() {
	return myCanonicalEndTime;
    }
    
    public String getDescription() {
	return myDescription;
    }
    
    public static class EventDateComparator implements Comparator<Event> {

	public int compare(Event e1, Event e2) {
	    return e1.getStart().compareTo(e2.getStart());
	}
	
    }
    
    public static class EventTitleComparator implements Comparator<Event> {

	public int compare(Event e1, Event e2) {
	    return e1.getTitle().compareTo(e2.getTitle());
	}
	
    }
    
}