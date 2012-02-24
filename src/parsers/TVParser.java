package parsers;

import java.util.*;

import model.TivooEventType;

import org.dom4j.*;
import org.joda.time.*;
import sharedattributes.*;

public class TVParser extends TivooParser {

    public TVParser() {
	setEventNodePath("//*[name()='event']");
	setEventType(new TVEventType());
	updateNoNeedParseMap(new Title(), "./*[name()='summary']");
	updateNoNeedParseMap(new Description(), "./*[name()='description']");
	updateNoNeedParseMap(new Location(), "./location/*[name()='address']");
    }
    
    public boolean wellFormed(Document doc) {
    	String rootname = doc.getRootElement().getName();
    	return (rootname.contentEquals("tv"));
    }

    protected void topLevelParsing(Document doc) {
	
    }

    protected void eventLevelParsing(Node n,
	    Map<TivooAttribute, Object> grabdatamap,
	    List<List<DateTime>> recurringstartend) {
	
    }

    private class TVEventType extends TivooEventType {}

}
