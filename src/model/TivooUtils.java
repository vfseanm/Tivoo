package model;
import java.io.File;
import java.util.*;

public class TivooUtils {
    
    private static HashMap<String, String> pollutant = new HashMap<String, String>();
    static {
	pollutant.put("&lt;br /&gt;", " ");
	pollutant.put("<br />", " ");
	pollutant.put("&amp;", "&");
	pollutant.put("&#39;", "'");
    }
    
    public static void deleteFile(String path){ 
	File file = new File(path);
	if (file.exists()) { 
	    if(file.isFile()) { 
		file.delete(); 
	    } 
	    else if(file.isDirectory()){ 
		File files[] = file.listFiles(); 
		for (int i=0;i<files.length;i++)
		    deleteFile(files[i].getPath()); 
	    } 
	    file.delete(); 
       	} else{ 
       	    throw new TivooException("File not found!");
       	} 
    } 
    
    public static void clearDirectory(String path){ 
	File file = new File(path);
	if (file.exists()) { 
	    if(file.isDirectory()){ 
		File files[] = file.listFiles(); 
		for (int i=0;i<files.length;i++)
		    deleteFile(files[i].getPath()); 
	    }
       	} else{ 
       	    throw new TivooException("Directory not found!");
       	} 
    } 
    
    public static String sanitizeString(String polluted) {
	for (String s: pollutant.keySet())
	    polluted = polluted.replaceAll(s, pollutant.get(s));
	return polluted;
    }
    
}
