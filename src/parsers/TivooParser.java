package parsers;

import java.util.*;

import model.*;

import org.dom4j.*;
import org.joda.time.*;

import sharedattributes.*;


public abstract class TivooParser {
    
    private Map<TivooAttribute, String> noneedparsemap = new HashMap<TivooAttribute, String>();
    private String eventnodepath;
    private TivooEventType eventtype;
    
    public abstract boolean wellFormed(Document doc);

    protected abstract void topLevelParsing(Document doc);

    protected abstract void eventLevelParsing(Node n, Map<TivooAttribute, Object> grabdatamap
	    , List<List<DateTime>> recurringstartend);
    
    protected String getEventNodePath() {
	
	return eventnodepath;
    }
    
    protected void setEventNodePath(String path) {
	eventnodepath = path;
    }
    
    protected void setEventType(TivooEventType type) {
	eventtype = type;
    }
    
    protected void updateNoNeedParseMap(TivooAttribute key, String value) {
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
	    Map<TivooAttribute, Object> grabdatamap = new HashMap<TivooAttribute, Object>();
	    List<List<DateTime>> recurringstartend = new ArrayList<List<DateTime>>();
	    for (TivooAttribute t: noneedparsemap.keySet()) {
		String retrieved = getNodeStringValue(n, noneedparsemap.get(t));
		grabdatamap.put(t, sanitizeString(retrieved));
	    }
	    eventLevelParsing(n, grabdatamap, recurringstartend);
	    if (recurringstartend.isEmpty()) {
		eventlist.add(new TivooEvent(eventtype,
			new HashMap<TivooAttribute, Object>(grabdatamap)));
	    }
	    else 
		eventlist.addAll(buildRecurringEvents(grabdatamap, recurringstartend));
	}
	return eventlist;
    }
    
    private List<TivooEvent> buildRecurringEvents(Map<TivooAttribute, Object> grabdatamap,
	    List<List<DateTime>> recurringstartend) {
	List<TivooEvent> augmentlist = new ArrayList<TivooEvent>();
	for (int i = 0; i < recurringstartend.get(0).size(); i++) {
	    Map<TivooAttribute, Object> toadd = new HashMap<TivooAttribute, Object>(grabdatamap);
	    toadd.put(new StartTime(), recurringstartend.get(0).get(i));
	    toadd.put(new EndTime(), recurringstartend.get(1).get(i));
	    augmentlist.add(new TivooEvent(eventtype, toadd));
	}
	return augmentlist;
    }

}