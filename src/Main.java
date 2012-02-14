import java.awt.Dimension;

import controller.TivooController;
import model.TivooModel;

public class Main {

    public static void main(String[] args) {
	TivooModel model = new TivooModel();
	String input = "dukecal.xml", outputsummary = "output/testhtml.html", 
		outputdetails = "output/details/";
	try {
	    model.initialize(input, outputsummary, outputdetails);	
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
    }

}