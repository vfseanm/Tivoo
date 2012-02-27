package controller;

import java.io.*;
import java.util.*;
import org.joda.time.*;
import writers.*;
import model.*;
//import view.*;

public class TivooController {

    
    private TivooModel myModel;
    //private TivooGenerator myView;

    public TivooController() {
	myModel = new TivooModel();
        //myView = new TivooGenerator(myModel, this);
    }
    
    public void initialize() {
	//myView.showPage(DEFAULT_START_PAGE);
	String input = "tv.xml", outputsummary = "output/testhtml_tv.html", 
		outputdetails = "output/details_tv/";
//	String input = "dukecal.xml", outputsummary = "output/testhtml.html", 
//		outputdetails = "output/details/";
	//DateTime startdate = TivooTimeHandler.createTimeUTC("20110601T000000Z");
	//DateTime enddate = startdate.plusDays(180);
	doRead(new File(input));
	//doFilterByTime(startdate, enddate);
	//doFilterByKeywordTitle("Meet");
	//doWrite(new SortedListWriter(), outputsummary, outputdetails);
	//doWrite(new DailyCalendarWriter(), outputsummary, outputdetails);
	//doWrite(new WeeklyCalendarWriter(), outputsummary, outputdetails);
	//doWrite(new MonthlyCalendarWriter(), outputsummary, outputdetails);
	doWrite(new ConflictingEventsWriter(), outputsummary, outputdetails);

    }
    
    public void doRead(File input) {
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
    
    public void doWrite(TivooWriter writer, String outputsummary, 
	    String outputdetails) {
	try {
	    writer.write(myModel.getFilteredList(), outputsummary, outputdetails);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
}