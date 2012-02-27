package model;
import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;
import org.xml.sax.*;

import parsers.*;

public class TivooReader {

    private static List<TivooParser> parsers = new ArrayList<TivooParser>();
    static {
	parsers.add(new DukeCalParser());
	parsers.add(new GoogleCalParser());
	parsers.add(new DukeBasketBallParser());
	parsers.add(new TVParser());
	parsers.add(new NFLParser());
    }
    
    public static Document read(File input) {
	SAXReader reader = new SAXReader();
	reader.setEntityResolver(new EntityResolver() {
	    public InputSource resolveEntity(String publicID, String systemID) {
		return new InputSource(new ByteArrayInputStream
			("<?xml version='1.0' encoding='GBK'?>".getBytes()));  
	    }
	});
	Document doc = null;
	try {
	    doc = reader.read(input);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
	return doc;
    }
    
    public static List<TivooEvent> convertToList(Document doc, TivooParser p) {
	return p.convertToList(doc);
    }
    
    public static TivooParser findParser(Document doc) {
	TivooParser theparser = null;
    	for(TivooParser p: parsers) {
    	    if (p.wellFormed(doc))
    		theparser = p;
    	}
    	if (theparser == null)
	    throw new TivooException("Malformed XML file!");
    	return theparser;
    }

}