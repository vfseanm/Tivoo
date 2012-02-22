package controller;
import java.io.*;
import java.util.*;
import org.joda.time.*;
import model.*;
import writers.*;
import filters.*;

public class TivooController {

    public List<TivooEvent> doFilterByTime(List<TivooEvent> eventlist, DateTime startdate, 
	    DateTime enddate) {
	return Filter.filterByTime(eventlist, startdate, enddate);
    }
    
    public List<TivooEvent> doFilterByKeyword(List<TivooEvent> eventlist, String keyword) {
	return Filter.filterByKeywordTitle(eventlist, keyword);
    }
    
    public List<TivooEvent> doFilterByKeywordsAttributes(List<TivooEvent> eventlist, 
	    Set<String> keywords, boolean retain) {
	return Filter.filterByKeywordsAttributes(eventlist, keywords, retain);
    }
    
    public void doWriteVerticalTable(List<TivooEvent> eventlist, String outputsummary, 
	    String outputdetails) {
	try {
	    new VerticalTableWriter().write(eventlist, outputsummary, outputdetails);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
}