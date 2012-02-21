package writers;
import static org.rendersnake.HtmlAttributesFactory.class_;
import static org.rendersnake.HtmlAttributesFactory.colspan;
import static org.rendersnake.HtmlAttributesFactory.href;
import static org.rendersnake.HtmlAttributesFactory.width;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.rendersnake.HtmlCanvas;

import model.*;

public class WeeklyCalendarWriter extends TivooWriter {

    public void write(List<TivooEvent> eventlist, String outputsummary,
	    String outputdetails) throws IOException {
	if (!new File(outputdetails).isDirectory()) throw new TivooException("Not a directory!");
	TivooUtils.clearDirectory(outputdetails);
	FileWriter summarywriter = new FileWriter(outputsummary);
	Collections.sort(eventlist, TivooEvent.EventTimeComparator);
	HtmlCanvas summary = new HtmlCanvas(summarywriter);
	HashSet<Integer> writtenstartdate = new HashSet<Integer>();
	summary
	.html()
	  .head()
	    .link(href("styles/vert_table_style.css").type("text/css").rel("stylesheet").media("screen"))
	  ._head().write("\n");
	  summary.body().write("\n")
	    .table(width("70%").align("center"));
	    for (TivooEvent e: eventlist) {
		int h = Hours.hoursBetween(e.getStart(), e.getEnd()).getHours();
		if (h >= 24) continue;
		DateTime localstart = TivooTimeHandler.createLocalTime(e.getStart());
		DateTime localend = TivooTimeHandler.createLocalTime(e.getEnd());
		if (!writtenstartdate.contains(localstart.getDayOfYear())) {
		    summary.tr().write("\n")
		      .th(colspan("2").align("left").class_("day")).write(localstart.toString("EEE, MMM dd"))
		      ._th()
		    ._tr();
		    writtenstartdate.add(localstart.getDayOfYear());
		}
		summary.tr().write("\n")
		  .th(class_("time")).write(localstart.toString("HH:mm") + "-" + localend.toString("HH:mm"))
		  ._th().write("\n")
		  .td()
		    .a(href(outputdetails.substring(outputdetails.indexOf("/") + 1) 
			    + eventlist.indexOf(e) + ".html")).write(e.getTitle())
		    ._a()
		  ._td().write("\n")
		._tr().write("\n");
		ArrayList<TivooEvent> oneevent = new ArrayList<TivooEvent>();
		oneevent.add(e);
		String detailURL = outputdetails + eventlist.indexOf(e) + ".html";
		new DetailPageWriter().write(oneevent, outputsummary, detailURL);
	    }
	    summary._table().write("\n")
	  ._body()
       ._html();
    summarywriter.close();
    }

}