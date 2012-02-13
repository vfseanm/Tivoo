package model;

import java.io.*;
import java.util.List;

import org.joda.time.DateTime;

public interface ITivooWriter {

    public void write(List<TivooEvent> eventlist, 
	    String outputsummary, String outputdetails, DateTime startdate, DateTime enddate)
    throws IOException;
    
}