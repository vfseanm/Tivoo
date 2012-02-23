package parsers;

import java.util.*;
import model.*;
import org.dom4j.*;
import org.joda.time.*;

public abstract class TivooParser {

    public static enum attribute_type {
	TITLE, DESCRIPTION, STARTTIME, ENDTIME
    }
    
    private Map<attribute_type, String> noneedparsemap 
    	= new HashMap<attribute_type, String>();
    private String eventnodepath;
    private TivooEvent.event_type eventtype;
    
    public abstract boolean wellFormed(Document doc);

    protected abstract void topLevelParsing(Document doc);

    protected abstract void eventLevelParsing(Node n, Map<attribute_type, Object> grabdatamap
	    , List<List<DateTime>> recurringstartend);
    
    protected String getEventNodePath() {
	return eventnodepath;
    }
    
    protected void setEventNodePath(String path) {
	eventnodepath = path;
    }
    
    protected void setEventType(TivooEvent.event_type type) {
	eventtype = type;
    }
    
    protected void updateNoNeedParseMap(attribute_type key, String value) {
	noneedparsemap.put(key, value);
    }
    
    @SuppressWarnings("serial")
    private static HashMap<String, String> pollutant = new HashMap<String, String>() {{
	put("&lt;br /&gt;", " "); put("<br />", " "); put("&amp;", "&"); put("&#39;", "'");
	put("&nbsp;", " ");  put("<br>", " ");
    }};

    protected String sanitizeString(String polluted) {
	for (String s: pollutant.keySet())
	    polluted = polluted.replaceAll(s, pollutant.get(s));
	return polluted;
    }
    
    protected Node trySelectSingleNode(Node n, String xpath) {
	Node selected = n.selectSingleNode(xpath);
	if (selected == null) 
	   throw new TivooException("Field not found!", TivooException.Type.BAD_FORMAT); 
	return selected;
    }
    
    protected List<Node> trySelectNodes(Document doc, String xpath) {
	@SuppressWarnings("unchecked")
	List<Node> list = doc.selectNodes(xpath);
	if (list.isEmpty()) 
	   throw new TivooException("Malformated!", TivooException.Type.BAD_FORMAT); 
	return list;
    }
    
    protected String getNodeStringValue(Node n, String xpath) {
	Node field = trySelectSingleNode(n, xpath);
	return field.getStringValue();
    }
    
    public List<TivooEvent> convertToList(Document doc) {
	List<Node> list = trySelectNodes(doc, getEventNodePath());
	topLevelParsing(doc);
	List<TivooEvent> eventlist = new ArrayList<TivooEvent>();
	for (Node n: list) {
	    HashMap<attribute_type, Object> grabdatamap = new HashMap<attribute_type, Object>();
	    List<List<DateTime>> recurringstartend = new ArrayList<List<DateTime>>();
	    for (attribute_type t: noneedparsemap.keySet()) {
		String retrieved = getNodeStringValue(n, noneedparsemap.get(t));
		grabdatamap.put(t, sanitizeString(retrieved));
	    }
	    eventLevelParsing(n, grabdatamap, recurringstartend);
	    if (recurringstartend.isEmpty()) {
		eventlist.add(new TivooEvent(
			eventtype,
			(String) grabdatamap.get(attribute_type.TITLE),
			(String) grabdatamap.get(attribute_type.DESCRIPTION),
			(DateTime) grabdatamap.get(attribute_type.STARTTIME),
			(DateTime) grabdatamap.get(attribute_type.ENDTIME))
		);
		if(eventlist.get(eventlist.size() - 1).getStart() == null) {
		    TivooEvent e = eventlist.get(eventlist.size() - 1);
		    System.out.println(e.getTitle());
		}
	    }
	    else {
		eventlist.addAll(buildRecurringEvents(grabdatamap, recurringstartend));
	    }
	}
	return eventlist;
    }
    
    private List<TivooEvent> buildRecurringEvents(Map<attribute_type, Object> grabdatamap,
	    List<List<DateTime>> recurringstartend) {
	List<TivooEvent> augmentlist = new ArrayList<TivooEvent>();
	for (int i = 0; i < recurringstartend.get(0).size(); i++) {
	    augmentlist.add(new TivooEvent(
		    eventtype,
		    (String) grabdatamap.get(attribute_type.TITLE),
		    (String) grabdatamap.get(attribute_type.DESCRIPTION),
		    recurringstartend.get(0).get(i),
		    recurringstartend.get(1).get(i))
	    );
	}
	return augmentlist;
    }

}