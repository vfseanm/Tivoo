package parsers;

import java.util.*;
import model.*;
import org.dom4j.*;

public abstract class TivooParser {

    private static HashMap<String, String> pollutant = new HashMap<String, String>();
    static {
	pollutant.put("&lt;br /&gt;", " ");
	pollutant.put("<br />", " ");
	pollutant.put("&amp;", "&");
	pollutant.put("&#39;", "'");
    }
    
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
    
    public abstract List<TivooEvent> convertToList(Document doc);
    
    public abstract boolean wellFormed(Document doc);

}