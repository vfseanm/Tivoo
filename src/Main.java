import model.TivooModel;

public class Main {

    public static void main(String[] args) {
	TivooModel model = new TivooModel();
	String input = "googlecal.xml", outputsummary = "output/testhtml_google.html", 
		outputdetails = "output/details_google/";
	try {
	    model.initialize(input, outputsummary, outputdetails);	
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
    }

}