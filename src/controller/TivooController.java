package controller;

import java.io.IOException;
import java.util.*;
import org.dom4j.*;
import org.joda.time.*;

import writers.VerticalTableWriter;
import model.*;

public class TivooController {

    private TivooModel myModel;
    
    public TivooController() {
	myModel = new TivooModel();
    }
    
    public void read(String input) throws DocumentException {
    	myModel.read(input);
    }
    
    public void doFilterByTime(DateTime startdate, DateTime enddate) {
    	myModel.filterByTime(startdate, enddate);
    }
    
    public void doFilterByKeywordTitle(String keyword) {
    	myModel.filterByKeywordTitle(keyword);

    }
    
    public void doFilterByKeywordsAttributes(Set<String> keywords, boolean retain) {
	myModel.filterByKeywordsAttributes(keywords, retain);
    }
    
    public void doWriteVerticalTable(String outputsummary, 
	    String outputdetails) {
	try {
	    new VerticalTableWriter().write(myModel.getFilteredList(), outputsummary, outputdetails);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
}