package model;

import java.io.*;
import java.util.*;

public interface ITivooWriter {

    public void write(List<TivooEvent> eventlist, String outputsummary, String outputdetails)
    throws IOException;
    
}