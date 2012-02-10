
public class Main {
	
    public static void main(String[] args) {
	ProtoModel p = new ProtoModel();
	String inputfilename = "test.xml", outputfilename = "testhtml.html";
	try {
	    p.initialize(inputfilename, outputfilename);	
	} catch (Exception e) {
	    System.out.println(e);
	}
    }

}