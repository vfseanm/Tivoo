package model;
import java.util.*;
import org.joda.time.*;

public class TivooEvent {

    private String myTitle;
    private DateTime myCanonicalStartTime;
    private DateTime myCanonicalEndTime;
    private String myDescription;
    
    public TivooEvent(String title, DateTime starttime, DateTime endtime) {
	myTitle = title;
	myCanonicalStartTime = starttime;
	myCanonicalEndTime = endtime;
    }
    
    public TivooEvent(String title, DateTime starttime, DateTime endtime, String description) {
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
    
    public static final Comparator<TivooEvent> EventDateComparator = new Comparator<TivooEvent>() {
	public int compare(TivooEvent e1, TivooEvent e2) {
	    int startdiff = e1.getStart().compareTo(e2.getStart());
	    if (startdiff != 0) return startdiff;
	    Integer duration1 = Seconds.secondsBetween(e1.getStart(), e1.getEnd()).getSeconds();
	    Integer duration2 = Seconds.secondsBetween(e2.getStart(), e2.getEnd()).getSeconds();
	    int durationdiff = duration1.compareTo(duration2);
	    if (durationdiff != 0) return durationdiff;
	    return EventTitleComparator.compare(e1, e2);
	}
    };
    
    public static final Comparator<TivooEvent> EventTitleComparator = new Comparator<TivooEvent>() {
	public int compare(TivooEvent e1, TivooEvent e2) {
	    return e1.getTitle().compareTo(e2.getTitle());
	}
    };
    
}