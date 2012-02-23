package writers;

import java.io.*;
import java.util.*;
import org.joda.time.*;
import org.rendersnake.*;
import static org.rendersnake.HtmlAttributesFactory.*;
import model.*;

public class VerticalTableWriter extends TivooWriter {

    public void write(List<TivooEvent> eventlist, String outputsummary, String outputdetails) 
		    throws IOException {
	if (!new File(outputdetails).isDirectory())
	    throw new TivooException("Output path not a directory!");
	TivooUtils.clearDirectory(outputdetails);
	FileWriter summarywriter = new FileWriter(outputsummary);
	Collections.sort(eventlist, TivooEvent.EventTimeComparator);
	HtmlCanvas summary = new HtmlCanvas(summarywriter);
	HashSet<Integer> writtenstartdate = new HashSet<Integer>();
	summary
	.html();
	  writeHeadWithCSS(summary, "styles/vert_table_style.css");
	  summary.body().write("\n")
	    .table(width("70%").align("center"));
	    for (TivooEvent e: eventlist) {
		if (e.isLongEvent()) continue;
		DateTime localstart = TivooTimeHandler.createLocalTime(e.getStart());
		DateTime localend = TivooTimeHandler.createLocalTime(e.getEnd());
		checkDuplicateStartDate(summary, localstart, writtenstartdate);
		summary.tr().write("\n")
		  .th(class_("time")).write(formatStartEnd(localstart, localend))
		  ._th().write("\n")
		  .td()
		    .a(href(formatDetailURL(e, outputdetails))).write(e.getTitle())
		    ._a()
		  ._td().write("\n")
		._tr().write("\n");
		doWriteDetailPage(e, outputsummary, outputdetails);
	    }
	    summary._table().write("\n")
	  ._body()
       ._html();
    summarywriter.close();
    }
    
    private String formatDetailURL(TivooEvent e, String outputdetails) {
	return outputdetails.substring(outputdetails.indexOf("/") + 1) 
		    + buildDetailURL(e);
    }
    
    private String formatStartEnd(DateTime start, DateTime end) {
	return start.toString("HH:mm") + "-" + end.toString("HH:mm");
    }
    
    private void checkDuplicateStartDate(HtmlCanvas target, DateTime startdate
	    , Set<Integer> writtenstartdate) throws IOException {
	if (!writtenstartdate.contains(startdate.getDayOfYear())) {
	    target.tr().write("\n")
	      .th(colspan("2").align("left").class_("day")).write(startdate.toString("EEE, MMM dd"))
	      ._th()
	    ._tr();
	    writtenstartdate.add(startdate.getDayOfYear());
	}
    }
    
}