package writers;
import java.io.*;
import java.util.*;

import org.joda.time.*;
import org.rendersnake.*;

import static org.rendersnake.HtmlAttributesFactory.*;

import org.dom4j.*;
import org.dom4j.io.*;

import model.*;

public class VerticalTableWriter implements ITivooWriter {

    public void write(List<TivooEvent> eventlist, 
	    String outputsummary, String outputdetails, DateTime startdate, DateTime enddate) 
		    throws IOException {
	TivooUtils.clearDirectory(outputdetails);
	FileWriter summarywriter = new FileWriter(outputsummary);
	Collections.sort(eventlist, TivooEvent.EventDateComparator);
	HtmlCanvas summary = new HtmlCanvas(summarywriter);
	HashSet<DateTime> writtenstartdate = new HashSet<DateTime>();
	summary
	.html()
	  .head()	
	    .link(href("styles/vert_table_style.css").type("text/css").rel("stylesheet").media("screen"))
	  ._head().write("\n")
	  .body().write("\n")
	    .table(width("70%").align("center"));
	    for (TivooEvent e: eventlist) {
		int h = Hours.hoursBetween(e.getStart(), e.getEnd()).getHours();
		if (h >= 24) continue;
		String detailURL = outputdetails + eventlist.indexOf(e) + ".html";
		DateTime localstart = TivooTimeHandler.createLocalTime(e.getStart());
		DateTime localend = TivooTimeHandler.createLocalTime(e.getEnd());
		if (!writtenstartdate.contains(localstart)) {
		    summary.tr().write("\n");
		    summary.th(colspan("2").align("left").class_("day")).write(localstart.toString("EEE, MMM dd"))._th()._tr();
		    writtenstartdate.add(localstart);
		}
		summary.tr().write("\n")
		.th(class_("time")).write(localstart.toString("HH:mm") + "-" + localend.toString("HH:mm"))
		._th().write("\n")
		.td().a(href("details/" + eventlist.indexOf(e) + ".html")).write(e.getTitle())._a()
		._td().write("\n")
		._tr().write("\n");
		writeDetail(detailURL, outputsummary, e);
	    }
	    summary._table().write("\n")
	    ._body()._html();
	    summarywriter.close();
    }
    
    private void writeDetail(String detailURL, String summaryURL, TivooEvent e) throws IOException {
	FileWriter detailwriter = new FileWriter(detailURL);
	HtmlCanvas detail = new HtmlCanvas(detailwriter);
	detail.html()
	.head()
	  .link(href("../styles/detail_page_style.css").type("text/css").rel("stylesheet").media("screen"))
	._head()
	.body().write("\n")
	  .table(width("70%").align("center"))
	    .tr()
	    .th(colspan("2").class_("title")).write(e.getTitle())._th().write("\n")
	    ._tr()
	    .tr()
	    .td().write(e.getDescription())._td()
	    ._tr()
	    .tr()
	    .td(class_("back")).a(href("../../" + summaryURL)).write("Back to summary")._a()._td()
	    ._tr()
	  ._table()
	    ._body()._html();
	detailwriter.close();
    }
    
    
}
