package model;
import java.util.*;

import org.joda.time.*;
import controller.*;

public class TivooModel {

    private List<TivooEvent> eventlist;
    
    public void initialize(String input, String outputsummary, String outputdetails) throws Exception {
	eventlist = TivooReader.read(input);
	//write(eventlist, outputsummary, outputdetails);
	TivooController tc = new TivooController();
	//DateTime startdate = TivooTimeHandler.createTimeUTC("20110301T000000Z");
	//DateTime enddate = startdate.plusDays(180);
	//List<TivooEvent> filtered = tc.doFilterByTime(eventlist, startdate, enddate);
	//List<TivooEvent> filteredagain = tc.doFilterByKeyword(filtered, "Duke");
	tc.doWriteVerticalTable(eventlist, outputsummary, outputdetails);

    }

}