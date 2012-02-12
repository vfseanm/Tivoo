package model;

import org.joda.time.*;

public class TivooTimeHandler {

    public static DateTime createTimeUTC(String UTCrepresentation) {
	int year = Integer.parseInt(UTCrepresentation.substring(0, 4));
	int month = Integer.parseInt(UTCrepresentation.substring(4, 6));
	int date = Integer.parseInt(UTCrepresentation.substring(6, 8));
	int hour = Integer.parseInt(UTCrepresentation.substring(9, 11));
	int minute = Integer.parseInt(UTCrepresentation.substring(11, 13));
	return new DateTime(year, month, 
		date, hour, minute, DateTimeZone.UTC);
    }
    
    public static String createLocalTime(DateTime dt) {
	DateTime transformed = dt.toDateTime(DateTimeZone.getDefault());
	return transformed.toString("MM/dd/yyyy HH:mm");
    }
    
}