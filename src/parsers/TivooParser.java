package parsers;

import java.util.HashMap;
import java.util.List;

import model.TivooEvent;
import model.TivooException;

import org.dom4j.Document;
import org.dom4j.Node;

public abstract class TivooParser {

    private static HashMap<String, String> pollutant = new HashMap<String, String>();
    static {
	pollutant.put("&lt;br /&gt;", " ");
	pollutant.put("<br />", " ");
	pollutant.put("&amp;", "&");
	pollutant.put("&#39;", "'");
    }
    
    public abstract List<TivooEvent> convertToList(Document doc);
    
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
    
}