package parsers;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;
import org.joda.time.DateTime;

import model.*;

public class DukeCalParser implements ITivooParser {
    
    public DukeCalParser() {}
    
    public List<TivooEvent> convertToList(Document doc) {
	@SuppressWarnings("unchecked")
	List<Node> list = doc.selectNodes("//event");
	List<TivooEvent> eventlist = new ArrayList<TivooEvent>();
	for (Node n: list) {
	    Node titlefield = n.selectSingleNode("./summary");
	    Node descriptionfield = n.selectSingleNode("./description");
	    Node startfield = n.selectSingleNode("./start/utcdate");
	    DateTime starttime = TivooTimeHandler.createTimeUTC(startfield.getStringValue());
	    Node endfield = n.selectSingleNode("./end/utcdate");
	    DateTime endtime = TivooTimeHandler.createTimeUTC(endfield.getStringValue());
	    eventlist.add(new TivooEvent(titlefield.getStringValue(), 
		    starttime, endtime, descriptionfield.getStringValue()));
	}
	return eventlist;
    }
    
}
