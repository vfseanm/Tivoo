package writers;

import static org.rendersnake.HtmlAttributesFactory.*;
import java.io.*;
import java.util.*;
import org.rendersnake.HtmlCanvas;
import sharedattributes.*;
import model.*;

public class DetailPageWriter extends TivooWriter {

    public void write(List<TivooEvent> eventlist, String outputsummary,
	    String outputdetails)
	    throws IOException {
	if (new File(outputdetails).isDirectory()) {
	    for (TivooEvent e: eventlist) {
		writeOneDetailPage(e, outputsummary, buildDetailURL(e));
	    }
	}
	else writeOneDetailPage(eventlist.get(0), outputsummary, outputdetails);
    }
    
    private void writeOneDetailPage(TivooEvent e, String outputsummary, String detailURL)
	    throws IOException {
	FileWriter detailwriter = new FileWriter(detailURL);
	HtmlCanvas detail = new HtmlCanvas(detailwriter);
	detail
	.html();
	  writeHeadWithCSS(detail, "../styles/detail_page_style.css");
	  detail.body().write("\n")
	    .table(width("70%").align("center"))
	      .tr()
	        .th(colspan("2").class_("title")).write(e.getTitle())._th().write("\n")
	     ._tr()
	      .tr()
	        .td().write(e.getDescription())._td()
	     ._tr();
	     Map<TivooAttribute, Object> specialAttributes = e.getSpecialAttributes();
	     for (TivooAttribute attr: specialAttributes.keySet()) {
		 detail.tr()
		    .td().write(attr.toString() + ": " + specialAttributes.get(attr).toString())._td()
		 ._tr();
	     }
	      detail.tr()
	         .td(class_("back"))
	           .a(href("../../" + outputsummary))
	             .write("Back to summary")
	           ._a()
	         ._td()
	     ._tr()
	  ._table()
	 ._body()
       ._html();
       detailwriter.close();
    }
    
}