package model;

import java.util.*;
import org.joda.time.*;

public class TivooEvent {

    public enum event_type {
	DUKE_EVENT, GOOGLE_EVENT, TV_EVENT;
    }
    
    private HashSet<String> myAttributes;
    private String myTitle;
    private DateTime myStart;
    private DateTime myEnd;
    private String myDescription;
    private event_type myType;
    
    public TivooEvent(event_type type, String title, String description, DateTime starttime, DateTime endtime) {
	myAttributes = new HashSet<String>();
	myType = type;
	myTitle = title;
	myStart = starttime;
	myEnd = endtime;
	myDescription = description;
	myAttributes.add(myTitle.toLowerCase());
	myAttributes.add(myDescription.toLowerCase());
    }
    
    public Set<String> getAttributes() {
	return Collections.unmodifiableSet(myAttributes);
    }
    
    public event_type getType() {
	return myType;
    }
    
    public String getTitle() {
	return myTitle;
    }
    
    public DateTime getStart() {
	return myStart;
    }
    
    public DateTime getEnd() {
	return myEnd;
    }
    
    public String getDescription() {
	return myDescription;
    }
    
    public Interval getInterval() {
	return new Interval(myStart, myEnd);
    }
    
    public boolean hasConflict(TivooEvent other) {
	return getInterval().overlaps(other.getInterval());
	/*return ((other.getStart().compareTo(getEnd()) < 0 && getEnd().compareTo(other.getEnd()) <= 0) ||
		(getStart().compareTo(other.getEnd()) < 0 && other.getEnd().compareTo(getEnd()) <= 0));*/
    }
    
    public boolean hasKeyWord(String keyword) {
	String lower = keyword.toLowerCase();
	for (String s: getAttributes())
	    if (s.contains(lower)) return true;
	return false;
    }
    
    public boolean isLongEvent() {
	if (Hours.hoursBetween(getStart(), getEnd()).getHours() > 24) return true;
	return false;
    }
    
    public boolean equals(Object o) {
	TivooEvent other = (TivooEvent) o;
	return (myType.equals(other.getType()) &&
		myTitle.equals(other.getTitle()) &&
		myStart.equals(other.getStart()) &&
		myEnd.equals(other.getEnd()) &&
		myDescription.equals(other.getDescription()));
    }
    
    public static final Comparator<TivooEvent> EventTimeComparator = new Comparator<TivooEvent>() {
	public int compare(TivooEvent e1, TivooEvent e2) {
	    int startcomp = EventStartComparator.compare(e1, e2);
	    if (startcomp != 0) return startcomp;
	    Integer duration1 = Seconds.secondsBetween(e1.getStart(), e1.getEnd()).getSeconds();
	    Integer duration2 = Seconds.secondsBetween(e2.getStart(), e2.getEnd()).getSeconds();
	    int durationdiff = duration1.compareTo(duration2);
	    if (durationdiff != 0) return durationdiff;
	    return EventTitleComparator.compare(e1, e2);
	}
    };
    
    public static final Comparator<TivooEvent> EventStartComparator = new Comparator<TivooEvent>() {
	public int compare(TivooEvent e1, TivooEvent e2) {
	    return e1.getStart().compareTo(e2.getStart());
	}
    };
    
    public static final Comparator<TivooEvent> EventEndComparator = new Comparator<TivooEvent>() {
	public int compare(TivooEvent e1, TivooEvent e2) {
	    return e1.getEnd().compareTo(e2.getEnd());
	}
    };
    
    public static final Comparator<TivooEvent> EventTitleComparator = new Comparator<TivooEvent>() {
	public int compare(TivooEvent e1, TivooEvent e2) {
	    return e1.getTitle().compareTo(e2.getTitle());
	}
    };
    
}