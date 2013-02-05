package a2;

public class PolygonImpl implements Polygon {
	Point []polyPoints;
	
	public PolygonImpl(Point[] points){
		polyPoints = new Point[points.length];
		for(int i = 0; i < points.length; i++){
			//Copy (Clone)
			polyPoints[i] = new Point(points[i].getX(), points[i].getY());
			//Reference
			//polyPoints[i] = points[i];
		}
	}
	
	@Override
	public Point[] getPoints() {
		Point []copyPolyPoints = new Point[polyPoints.length];
		for(int i = 0; i < polyPoints.length; i++){
			copyPolyPoints[i] = new Point(polyPoints[i].getX(), polyPoints[i].getY());
		}
		return copyPolyPoints;
	}

	@Override
	public int getNumSides() {
		return polyPoints.length;
	}

	@Override
	public Point getVertexAverage() {
		Point p;
		double x = 0.0, y = 0.0;
		for(int i = 0; i < polyPoints.length; i++){
			x += polyPoints[i].getX();
			y += polyPoints[i].getY();
		}
		x /= polyPoints.length;
		y /= polyPoints.length;
		p = new Point(x, y);
		return p;
	}

	@Override
	public Rectangle getBoundingBox() {
		//Compute (minx, miny) and (maxx, maxy), these are the two opposite points of rectangle
		double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
		double x, y;
		
		for(int i = 0; i < polyPoints.length; i++){
			x = polyPoints[i].getX();
			y = polyPoints[i].getY();
			if(x < minX){
				minX = x;
			}
			if(y < minY){
				minY = y;
			}
			if(x > maxX){
				maxX = x;
			}
			if(y > maxY){
				maxY = y;
			}
		}
		Point lowerLeft = new Point(minX, minY);
		Point upperRight = new Point(maxX, maxY);
		Rectangle rec = new RectangleImpl(lowerLeft, upperRight);
		return rec;
	}

	@Override
	public Point getCentroid() {
		double X = 0, Y = 0, A = 0;
		double xi,xi1,yi,yi1;
		int currIndex, nextIndex;
		for(int i = 0; i < polyPoints.length; i++){
			currIndex = i;
			nextIndex = i+1;
			if(i == polyPoints.length - 1){
				nextIndex = 0;
			}
			xi = polyPoints[currIndex].getX();
			xi1 = polyPoints[nextIndex].getX();
			yi = polyPoints[currIndex].getY();
			yi1 = polyPoints[nextIndex].getY();
			
			A += (xi * yi1) - (xi1 * yi);
			X += (xi + xi1) * (xi*yi1 - xi1*yi);
			Y += (yi + yi1) * (xi*yi1 - xi1*yi);
		}
		A = A / 2;
		X = X / (6 * A);
		Y = Y / (6 * A);
		Point p = new Point(X, Y);
		return p;
	}

	@Override
	public double getArea() {
		double A = 0;
		double xi,xi1,yi,yi1;
		int currIndex, nextIndex;
		for(int i = 0; i < polyPoints.length; i++){
			currIndex = i;
			nextIndex = i+1;
			if(i == polyPoints.length - 1){
				nextIndex = 0;
			}
			xi = polyPoints[currIndex].getX();
			xi1 = polyPoints[nextIndex].getX();
			yi = polyPoints[currIndex].getY();
			yi1 = polyPoints[nextIndex].getY();
			
			A += (xi * yi1) - (xi1 * yi);
		}
		A = Math.abs(A) / 2;
		return A;
	}

	@Override
	public void move(double dx, double dy) {
		for(int i = 0; i < polyPoints.length; i++){
			polyPoints[i] = polyPoints[i].translate(dx, dy);
		}
	}

	@Override
	public void move(Point c) {
		Point centroid = this.getCentroid();
		double dx = c.getX() - centroid.getX();
		double dy = c.getY() - centroid.getY();
		this.move(dx, dy);
	}

	@Override
	public void scale(double factor) {
		Point centroid = this.getCentroid();
		Point origin = new Point(0, 0);
		this.move(origin);
		for(int i = 0; i < polyPoints.length; i++){
			polyPoints[i] = new Point(polyPoints[i].getX()*factor, polyPoints[i].getY()*factor);
		}
		this.move(centroid);
	}

}
