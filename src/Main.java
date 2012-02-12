import model.ProtoModel;

public class Main {
	
    public static void main(String[] args) {
	ProtoModel p = new ProtoModel();
	String input = "dukecal.xml", outputsummary = "output/testhtml.html", 
		outputdetails = "output/details/";
	try {
	    p.initialize(input, outputsummary, outputdetails);	
	} catch (Exception e) {
	    System.out.println(e);
	}
    }

}