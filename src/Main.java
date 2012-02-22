import model.*;
import view.*;

import javax.swing.JFrame;

public class Main {

    public static final String TITLE = "TivooBrowser";
    public static final String DEFAULT_START_PAGE = "http://www.cs.duke.edu/rcd";
    
    public static void main(String[] args) {
	TivooModel model = new TivooModel();
    	// create program specific components
        //TivooViewer display = new TivooViewer(model);
        // create container that will work with Window manager
        //JFrame frame = new JFrame(TITLE);
        // add our user interface components to Frame and show it
        // start somewhere, less typing for debugging
        //display.showPage(DEFAULT_START_PAGE);
	
	String input = "dukecal.xml", outputsummary = "output/testhtml.html", 
		outputdetails = "output/details/";
	try {
	    model.initialize(input, outputsummary, outputdetails);	
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
    }
    
}