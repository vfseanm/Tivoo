package writers;
import static org.rendersnake.HtmlAttributesFactory.*;
import java.io.*;
import java.util.*;
import org.rendersnake.HtmlCanvas;
import model.*;

public class DetailPageWriter implements ITivooWriter {

    public void write(List<TivooEvent> eventlist, String outputsummary,
	    String outputdetails)
	    throws IOException {
	if (new File(outputdetails).isDirectory()) {
	    for (TivooEvent e: eventlist) {
		String detailURL = outputdetails + eventlist.indexOf(e) + ".html";
		writeOneDetailPage(e, outputsummary, detailURL);
	    }
	}
	else writeOneDetailPage(eventlist.get(0), outputsummary, outputdetails);
    }
    
    private void writeOneDetailPage(TivooEvent e, String outputsummary, String detailURL)  throws IOException {
	FileWriter detailwriter = new FileWriter(detailURL);
	HtmlCanvas detail = new HtmlCanvas(detailwriter);
	detail
	.html()
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
	         .td(class_("back")).a(href("../../" + outputsummary)).write("Back to summary")._a()._td()
	     ._tr()
	  ._table()
	 ._body()
       ._html();
       detailwriter.close();
    }
    
}