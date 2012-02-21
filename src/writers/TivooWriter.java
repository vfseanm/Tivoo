package writers;
import static org.rendersnake.HtmlAttributesFactory.*;
import java.io.IOException;
import model.TivooEvent;
import org.rendersnake.HtmlCanvas;
import java.util.*;

public abstract class TivooWriter {

    public abstract void write(List<TivooEvent> eventlist, String outputsummary, String outputdetails)
	    throws IOException;
    
    protected void writeHeadWithCSS(HtmlCanvas target, String stylefile) throws IOException {
	target.head()
	  .link(href(stylefile).type("text/css").rel("stylesheet").media("screen"))
	._head().write("\n");
    }
    
    protected String buildDetailURL(TivooEvent e) {
        return e.getTitle()
        	.replaceAll("[^A-z0-9]", "").replaceAll("\\s+", "_").trim().concat(".html");
    }
    
    protected void writeTableCell(HtmlCanvas target, String content) 
	    throws IOException {
	target.td().write(content)._td();
    }
    
    protected void writeTableCell(HtmlCanvas target, String content, String cssclass) 
	    throws IOException {
	target.td(class_("cssclass")).write(content)._td();
    }
    
}