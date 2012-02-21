package model;
import java.util.*;

import org.joda.time.*;
import controller.*;

public class TivooModel {

    private List<TivooEvent> eventlist;
    private String myHome;
    private String myCurrentURL;
    private int myCurrentIndex;
    private List<String> myHistory;
    private Map<String, String> myFavorites;
    
    public void initialize(String input, String outputsummary, String outputdetails) throws Exception {
	eventlist = TivooReader.read(input);
	//write(eventlist, outputsummary, outputdetails);
	TivooController tc = new TivooController();
	DateTime startdate = TivooTimeHandler.createTimeUTC("20110301T000000Z");
	DateTime enddate = startdate.plusDays(180);
	//List<TivooEvent> filtered = tc.doFilterByTime(eventlist, startdate, enddate);
	//List<TivooEvent> filteredagain = tc.doFilterByKeyword(filtered, "Duke");
	tc.doWriteVerticalTable(eventlist, outputsummary, outputdetails, 
		startdate, enddate);

    }
    
    /**
     * Creates an empty model.
     */
    public TivooModel()
    {
        myHome = null;
        myCurrentURL = "";
        myCurrentIndex = -1;
        myHistory = new ArrayList<String>();
        myFavorites = new HashMap<String, String>();
    }


    /**
     * Returns the first page in next history, null if next history is empty.
     */
    public String next ()
    {
        if (hasNext())
        {
            myCurrentIndex++;
            return myHistory.get(myCurrentIndex);
        }
        return null;
    }


    /**
     * Returns the first page in back history, null if back history is empty.
     */
    public String back ()
    {
        if (hasPrevious())
        {
            myCurrentIndex--;
            return myHistory.get(myCurrentIndex);
        }
        return null;
    }
    
    
    public void go (String url)
    {
        myCurrentURL = url;
        if (hasNext())
        {
            myHistory = myHistory.subList(0, myCurrentIndex + 1); 
        }
        myHistory.add(url);
        myCurrentIndex++;
    }
    

    /**
     * Returns true if there is a next URL available
     */
    public boolean hasNext ()
    {
        return myCurrentIndex < (myHistory.size() - 1);
    }


    /**
     * Returns true if there is a previous URL available
     */
    public boolean hasPrevious ()
    {
        return myCurrentIndex > 0;
    }


    /**
     * Returns URL of the current home page or null if none is set.
     */
    public String getHome ()
    {
        return myHome;
    }


    /**
     * Sets current home page to the current URL being viewed.
     */
    public void setHome ()
    {
        myHome = myCurrentURL;
    }


    /**
     * Adds current URL being viewed to favorites collection with given name.
     */
    public void addFavorite (String name)
    {
    	myFavorites.put(name, myCurrentURL);
    }


    /**
     * Returns URL from favorites associated with given name, null if none set.
     */
    public String getFavorite (String name)
    {
    	return myFavorites.get(name);
    }
}