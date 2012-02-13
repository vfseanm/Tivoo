package model;
import java.io.*;
import java.util.*;
import org.joda.time.*;
import org.rendersnake.*;
import static org.rendersnake.HtmlAttributesFactory.*;
import org.dom4j.*;
import org.dom4j.io.*;

import controller.TivooController;

import writers.*;	
import filters.Filter;

public class TivooModel {

    private List<TivooEvent> eventlist;
    
    public void initialize(String input, String outputsummary, String outputdetails) throws Exception {
	eventlist = TivooReader.read(input);
	//write(eventlist, outputsummary, outputdetails);
	TivooController tc = new TivooController();
	DateTime startdate = TivooTimeHandler.createTimeUTC("20110701T0000");
	DateTime enddate = startdate.plusDays(180);
	List<TivooEvent> filtered = tc.doFilterByTime(eventlist, startdate, enddate);
	List<TivooEvent> filteredagain = tc.doFilterByKeyword(filtered, "Duke");
	tc.doWriteVerticalTable(filteredagain, outputsummary, outputdetails, 
		startdate, enddate);

    }
    
}