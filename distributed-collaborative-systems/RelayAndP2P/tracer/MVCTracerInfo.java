package tracer;

import util.annotations.ComponentWidth;
import util.annotations.DisplayToString;
import util.trace.TraceableInfo;
import util.trace.Tracer;

@DisplayToString(true)
@ComponentWidth(1000)
public class MVCTracerInfo extends TraceableInfo {

	public MVCTracerInfo(String aMessage, Object anAnnouncer) {
        super(aMessage, anAnnouncer);
        setDisplay(true); // Need the updated version of Object Editor for this line to work!
    }
    
    public static MVCTracerInfo newInfo(String aMessage, Object aFinder) {    
        MVCTracerInfo retVal = new MVCTracerInfo(aMessage, aFinder);
        retVal.announce();
        return retVal;
    }
}
