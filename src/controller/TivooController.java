package controller;
import java.io.File; 

import java.io.IOException;
import java.util.*;

import model.TivooEvent;
import model.TivooException;
import model.TivooTimeHandler;

import org.joda.time.DateTime;

import writers.VerticalTableWriter;

import filters.Filter;

public class TivooController {

    public List<TivooEvent> doFilterByTime(List<TivooEvent> eventlist, DateTime startdate, 
	    DateTime enddate) {
	return Filter.filterByTime(eventlist, startdate, enddate);
    }
    
    public List<TivooEvent> doFilterByKeyword(List<TivooEvent> eventlist, String keyword) {
	return Filter.filterByKeyword(eventlist, keyword);
    }
    
    public void doWriteVerticalTable(List<TivooEvent> eventlist, String outputsummary, 
	    String outputdetails, DateTime startdate, DateTime enddate) {
	try {
	    new VerticalTableWriter().write(eventlist, outputsummary, outputdetails, startdate, enddate);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
}