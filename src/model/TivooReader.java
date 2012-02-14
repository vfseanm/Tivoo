package model;
import java.io.File;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import parsers.*;


public class TivooReader {

    private static Map<String, ITivooParser> parsermap = new HashMap<String, ITivooParser>();
    static {
	parsermap.put("events", new DukeCalParser());
	parsermap.put("feed", new GoogleCalParser());

    }
    
    public static List<TivooEvent> read(String input) throws DocumentException {
	SAXReader reader = new SAXReader();
	Document doc = reader.read(new File(input));
	String rootname = doc.getRootElement().getName();
	ITivooParser theparser = parsermap.get(rootname);
	if (theparser == null)
	    throw new TivooException("Malformed XML file!");
	return theparser.convertToList(doc);
    }

}