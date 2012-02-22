package model;

import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.joda.time.*;
import writers.*;
import filters.Filter;

public class TivooModel {

    private List<TivooEvent> eventlist;
    private List<TivooEvent> filteredlist;
    
    public void writeVerticalTable(String outputsummary, 
	    String outputdetails) {
    	try {
    	    new VerticalTableWriter().write(filteredlist, outputsummary, outputdetails);
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    }
    
    public void read(String input) throws DocumentException {
    	eventlist = TivooReader.read(input);
    	filteredlist = new ArrayList<TivooEvent>();
    	Collections.copy(filteredlist, eventlist);
    }
    
    public void clearFilter() {
	filteredlist.clear();
    	Collections.copy(filteredlist, eventlist);
    }
    
    public void filterByTime(DateTime startdate, DateTime enddate) {
	filteredlist = Filter.filterByTime(filteredlist, startdate, enddate);
    }
    
    public void filterByKeywordTitle(String keyword) {
	filteredlist = Filter.filterByKeywordTitle(filteredlist, keyword);
    }
    
    public void filterByKeywordsAttributes(Set<String> keywords, boolean retain) {
	filteredlist = Filter.filterByKeywordsAttributes(filteredlist, keywords, retain);
    }
    
}