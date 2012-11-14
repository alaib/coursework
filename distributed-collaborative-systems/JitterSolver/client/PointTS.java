package client;

import java.awt.Point;

public class PointTS {
	public Point p;
	public long ts;
	
	public PointTS(Point np, long timestamp){
		p = np;
		ts = timestamp;
	}
	
	public PointTS(){
		p = new Point(20, 50);
		ts = -1;
	}
}
