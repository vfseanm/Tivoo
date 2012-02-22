package model;
import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;
import parsers.*;

public class TivooReader {

    private static ArrayList<TivooParser> parsers = new ArrayList<TivooParser>();
    static {
	parsers.add(new DukeCalParser());
	parsers.add(new GoogleCalParser());
    }
    
    public static List<TivooEvent> read(String input) throws DocumentException {
	SAXReader reader = new SAXReader();
	Document doc = reader.read(new File(input));
	TivooParser theparser = findParser(doc);
	if (theparser == null)
	    throw new TivooException("Malformed XML file!");
	return theparser.convertToList(doc);
    }
    
    private static TivooParser findParser(Document doc) {
    	for(TivooParser p: parsers) {
    	    if (p.wellFormed(doc))
    		return p;
    	}
    	return null; 
    }

}