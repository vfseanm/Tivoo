package parsers;

import java.util.*;
import org.dom4j.*;
import org.joda.time.*;
import sharedattributes.*;

public class TVParser extends TivooParser {

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

}
