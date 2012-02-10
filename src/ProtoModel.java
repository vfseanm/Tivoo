import java.io.*;
import java.util.*;
import org.rendersnake.*;
import org.dom4j.*;
import org.dom4j.io.*;

public class ProtoModel {

    private FileWriter myWriter;
    private File myFile;

    public void initialize(String inputfilename, String outputfilename) {
	try {
	    myFile = new File(inputfilename);
	    myWriter = new FileWriter(outputfilename);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	Element root = read();
	filter(root);
	write(root);
    }

    private Element read() {
	SAXReader reader = new SAXReader();
	Element root = null;
	try {
	    Document doc = reader.read(myFile);
	    root = doc.getRootElement();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return root;
    }

    private void filter(Element root) {
    }

    private void write(Element root) {
	HtmlCanvas html = null;
	try {
	    html = new HtmlCanvas(myWriter);
	    Element foo = null;
	    html.html().body();
	    html.write("\n");
	    for (Iterator<?> i = root.elementIterator("node"); i.hasNext();) {
		foo = (Element) i.next();
		html.h1().write(foo.elementText("name"))._h1().h1().write("\n")
			.write(foo.elementText("space"))._h1();
		html.write("\n");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		html._body()._html();
		myWriter.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

}