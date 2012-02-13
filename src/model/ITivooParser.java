package model;

import java.util.List;

import org.dom4j.Document;

public interface ITivooParser {

    public List<TivooEvent> convertToList(Document doc);

    
}
