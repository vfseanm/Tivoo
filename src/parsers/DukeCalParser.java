package parsers;
import java.util.*;
import org.dom4j.*;
import org.joda.time.*;
import model.*;

public class DukeCalParser extends TivooParser {
    
    public List<TivooEvent> convertToList(Document doc) {
	@SuppressWarnings("unchecked")
	List<Node> list = doc.selectNodes("//*[name()='event']");
	List<TivooEvent> eventlist = new ArrayList<TivooEvent>();
	for (Node n: list) {
	    Node titlefield = trySelectSingleNode(n, "./*[name()='summary']");
	    Node descriptionfield = trySelectSingleNode(n, "./*[name()='description']");
	    Node startfield = trySelectSingleNode(n, "./start/*[name()='utcdate']");
	    DateTime starttime = TivooTimeHandler.createTimeUTC(startfield.getStringValue());
	    Node endfield = n.selectSingleNode("./end/*[name()='utcdate']");
	    DateTime endtime = TivooTimeHandler.createTimeUTC(endfield.getStringValue());
	    eventlist.add(new TivooEvent(titlefield.getStringValue(), 
		    starttime, endtime, descriptionfield.getStringValue()));
	}
	return eventlist;
    }
    
}
