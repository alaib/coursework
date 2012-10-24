package tracer;

import util.annotations.ComponentWidth;
import util.annotations.DisplayToString;
import util.trace.TraceableBus;
import util.trace.TraceableInfo;

@DisplayToString(true)
@ComponentWidth(1000)
public class MVCTracerInfo extends TraceableInfo {
	//static TraceableListenerMod aListener = new TraceableListenerMod();
	public MVCTracerInfo(String aMessage, Object anAnnouncer) {
        super(aMessage, anAnnouncer);
        setDisplay(true); // Need the updated version of Object Editor for this line to work!
        //Add listener
        //TraceableBus.addTraceableListener(aListener);
    }
    
    public static MVCTracerInfo newInfo(String aMessage, Object aFinder) {    
        MVCTracerInfo retVal = new MVCTracerInfo(aMessage, aFinder);
        retVal.announce();
        return retVal;
    }
}
