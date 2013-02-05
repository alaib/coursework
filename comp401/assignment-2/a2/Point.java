package a2;

public class Point {
	private double x;
    private double y;
    
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public Point translate(double dx, double dy) {
        return new Point(x+dx, y+dy);
    }
    
    public double distanceTo(Point p) {
        return Math.sqrt((p.x - x)*(p.x -x) + (p.y-y)*(p.y-y));
    }
    
}
