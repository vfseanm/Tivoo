package parsers;

import java.util.*;
import org.dom4j.*;
import org.joda.time.*;
import sharedattributes.*;
import model.*;

public class TVParser extends TivooParser {

    private Set<String> channels;
    
    public TVParser() {
	setEventNodePath("//*[name()='programme']");
	setEventType(new TVEventType());
	updateNoNeedParseMap(new Title(), "./*[name()='title']");
	updateNoNeedParseMap(new Description(), "./*[name()='desc']");
    }
    
    public boolean wellFormed(Document doc) {
    	String rootname = doc.getRootElement().getName();
    	return (rootname.contentEquals("tv"));
    }

    public TivooEventType getEventType() {
	return new TVEventType();
    }
    
    protected void topLevelParsing(Document doc) {
	channels = new HashSet<String>();
	
//	  <channel id="I10021.labs.zap2it.com">
//	    <display-name>622 AMC</display-name>
//	    <display-name>622 AMC NC32461:X</display-name>
//	    <display-name>622</display-name>
//	    <display-name>AMC</display-name>
//	    <display-name>AMC</display-name>
//	    <display-name>Satellite</display-name>
//	  </channel>

	
    }

    protected void eventLevelParsing(Node n,
	    Map<TivooAttribute, Object> grabdatamap,
	    List<List<DateTime>> recurringstartend) {
	
    }

    private class TVEventType extends TivooEventType {
	
	public String toString() {
	    return "TV";
	}
    }
    
}