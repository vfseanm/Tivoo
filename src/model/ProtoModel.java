package model;
import java.io.*;
import java.util.*;
import org.joda.time.*;
import org.rendersnake.*;
import org.dom4j.*;
import org.dom4j.io.*;

import filters.Filter;

public class ProtoModel {

    public void initialize(String input, String outputsummary, String outputdetails) throws Exception {
	Document doc = read(input);
	List<Event> eventlist = Filter.convertToList(doc);
	DateTime startdate = TivooTimeHandler.createTimeUTC("20110101T0000");
	DateTime enddate = startdate.plusDays(6);
	eventlist = Filter.filterByTime(eventlist, startdate, enddate);
	write(eventlist, outputsummary, outputdetails);
    }

    private Document read(String input) throws DocumentException {
	SAXReader reader = new SAXReader();
	Document doc = reader.read(new File(input));
	return doc;
    }

    private void write(List<Event> eventlist, 
	    String outputsummary, String outputdetails) throws IOException {
	FileWriter summarywriter = new FileWriter(outputsummary);
	Collections.sort(eventlist, new Event.EventDateComparator());
	HtmlCanvas summary = new HtmlCanvas(summarywriter);
	summary.html().body().write("\n");
	for (Event e: eventlist) {
	   // HtmlCanvas test = new HtmlCanvas(new StringWriter());
	    String detailURL = outputdetails + eventlist.indexOf(e) + ".html";
	    summary.a(new HtmlAttributes().href("details/" + eventlist.indexOf(e) + ".html")).write(e.getTitle())._a();
	    summary.h1().write(TivooTimeHandler.createLocalTime(e.getStart()))._h1().write("\n")
	    .h1().write(TivooTimeHandler.createLocalTime(e.getEnd()))._h1().write("\n");
	    FileWriter detailwriter = new FileWriter(detailURL);
	    HtmlCanvas detail = new HtmlCanvas(detailwriter);
	    detail.html().body().write("\n")
	    .h1().write(e.getDescription())._h1().write("\n")
	    ._body()._html();
	    detailwriter.close();
	}
	summary._body()._html();
	summarywriter.close();
    }
    
}