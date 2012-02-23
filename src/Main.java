import org.joda.time.*;
import model.*;
import controller.*;

public class Main {

    //public static final String TITLE = "TivooBrowser";
    //public static final String DEFAULT_START_PAGE = "http://www.cs.duke.edu/rcd";
    
    public static void main(String[] args) {
	TivooController controller = new TivooController(); 
    	// create program specific components
        //TivooViewer display = new TivooViewer(model);
        // create container that will work with Window manager
        //JFrame frame = new JFrame(TITLE);
        // add our user interface components to Frame and show it
        // start somewhere, less typing for debugging
        //display.showPage(DEFAULT_START_PAGE);
	
	String input = "googlecal.xml", outputsummary = "output/testhtml_google.html", 
		outputdetails = "output/details_google/";
	DateTime startdate = TivooTimeHandler.createTimeUTC("20110601T000000Z");
	DateTime enddate = startdate.plusDays(180);
	try {
		controller.read(input);
		controller.doFilterByTime(startdate, enddate);
		controller.doFilterByKeywordTitle("Meet");
		controller.doWriteVerticalTable(outputsummary, outputdetails);
	} 
	catch (Exception e) {
	    e.printStackTrace();
	}
	
    }
    
}