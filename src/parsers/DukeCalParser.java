package parsers;
import java.util.*;
import org.dom4j.*;
import org.joda.time.*;
import model.*;

public class DukeCalParser implements ITivooParser {
    
    public List<TivooEvent> convertToList(Document doc) {
	@SuppressWarnings("unchecked")
	List<Node> list = doc.selectNodes("//*[name()='event']");
	List<TivooEvent> eventlist = new ArrayList<TivooEvent>();
	for (Node n: list) {
	    Node titlefield = n.selectSingleNode("./*[name()='summary']");
	    Node descriptionfield = n.selectSingleNode("./*[name()='description']");
	    Node startfield = n.selectSingleNode("./start/*[name()='utcdate']");
	    DateTime starttime = TivooTimeHandler.createTimeUTC(startfield.getStringValue());
	    Node endfield = n.selectSingleNode("./end/*[name()='utcdate']");
	    DateTime endtime = TivooTimeHandler.createTimeUTC(endfield.getStringValue());
	    eventlist.add(new TivooEvent(titlefield.getStringValue(), 
		    starttime, endtime, descriptionfield.getStringValue()));
	}
	return eventlist;
    }
    
}