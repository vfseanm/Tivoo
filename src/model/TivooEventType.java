package model;

import java.util.*;

import sharedattributes.*;

public abstract class TivooEventType {

    @SuppressWarnings("serial")
    private static Set<TivooAttribute> commonAttributes = new HashSet<TivooAttribute>() {{
	add(new Title()); add(new Description()); add(new StartTime()); add(new EndTime());
    }};
    
    private Set<TivooAttribute> specialAttributes = new HashSet<TivooAttribute>();
    
    protected static Set<TivooAttribute> getCommonAttributes() {
	return Collections.unmodifiableSet(commonAttributes);
    }
    
    protected Set<TivooAttribute> getSpecialAttributes() {
	return Collections.unmodifiableSet(specialAttributes);
    }
    
    protected void addSpecialAttributes(Set<TivooAttribute> theset) {
	specialAttributes.addAll(theset);
    }
    
}