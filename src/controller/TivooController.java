package controller;

import java.util.*;
import org.dom4j.*;
import org.joda.time.*;
import model.*;

public class TivooController {

    private TivooModel myModel;
    
    public TivooController() {
	myModel = new TivooModel();
    }
    
    public void read(String input) throws DocumentException {
    	myModel.read(input);
    }
    
    public void dofilterByTime(DateTime startdate, DateTime enddate) {
    	myModel.filterByTime(startdate, enddate);
    }
    
    public void dofilterByKeywordTitle(String keyword) {
    	myModel.filterByKeywordTitle(keyword);

    }
    
    public void doFilterByKeywordsAttributes(Set<String> keywords, boolean retain) {
	myModel.filterByKeywordsAttributes(keywords, retain);
    }
    
    public void doWriteVerticalTable(String outputsummary, 
	    String outputdetails) {
	myModel.writeVerticalTable(outputsummary, outputdetails);
    }
    
}