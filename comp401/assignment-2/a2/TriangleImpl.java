package a2;

public class TriangleImpl implements Polygon, Triangle {
	Point a, b, c;
	Point []pArr;
	
	public TriangleImpl(Point a, Point b, Point c){
		this.a = new Point(a.getX(), a.getY());
		this.b = new Point(b.getX(), b.getY());
		this.c = new Point(c.getX(), c.getY());
		
		pArr = this.getPoints();
	}
	
	public boolean dCompare(double a, double b){
		double eps = 0.001;
		if(a - b < eps){
			return true;
		}
		return false;
	}
	
	@Override
	public Category getCategory() {
		double side1 = a.distanceTo(b);
		double side2 = b.distanceTo(c);
		double side3 = c.distanceTo(a);
		if(dCompare(side1, side2) && dCompare(side2, side3)){
			return Category.EQUILATERAL;
		}else if(dCompare(side1, side2) || dCompare(side2, side3) || dCompare(side1, side3)){
			return Category.ISOSCELES;
		}else{
			return Category.SCALENE;
		}
	}
	
	public double maxOf3(double a, double b, double c){
		double max = a;
		if(b > max){
			max = b;
		}
		if(c > max){
			max = c;
		}
		return max;
	}
	
	@Override
	public boolean isRight() {
		double side1 = a.distanceTo(b);
		double side2 = b.distanceTo(c);
		double side3 = c.distanceTo(a);
		double max = maxOf3(side1, side2, side3);
		if(dCompare(max, side1)){
			return dCompare(side1*side1, side2*side2+side3*side3);
		}else if(dCompare(max, side2)){
			return dCompare(side2*side2, side1*side1+side3*side3);
		}else{
			return dCompare(side3*side3, side2*side2+side1*side1);
		}
	}

	@Override
	public Point getA() {
		return a;
	}

	@Override
	public Point getB() {
		return b;
	}

	@Override
	public Point getC() {
		return c;
	}

	@Override
	public Point[] getPoints() {
		Point []pArr = new Point[3];
		pArr[0] = this.getA(); 
		pArr[1] = this.getB(); 
		pArr[2] = this.getC(); 
		return pArr;
	}

	@Override
	public int getNumSides() {
		return pArr.length;
	}

	@Override
	public Point getVertexAverage() {
		Point []polyPoints = this.pArr;
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
		Point []polyPoints = this.pArr;
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
		double x = (a.getX()+b.getX()+c.getX())/3;
		double y = (a.getY()+b.getY()+c.getY())/3;
		Point p = new Point(x, y);
		return p;
	}
	
	// Separate methods for calculating areas and perimeters of each shape
	public double getTriangleArea(double x1, double y1, double x2, double y2,
				double x3, double y3) {
			return Math
					.abs(((x1 * (y2 - y3)) + (x2 * (y3 - y1)) + (x3 * (y1 - y2))) / 2);
	}

	@Override
	public double getArea() {
		return this.getTriangleArea(a.getX(), a.getY(), b.getX(), b.getY(), c.getX(), c.getY());
	}

	@Override
	public void move(double dx, double dy) {
		a = a.translate(dx, dy);
		b = b.translate(dx, dy);
		c = c.translate(dx, dy);
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
		a = new Point(a.getX()*factor, a.getY()*factor);
		b = new Point(b.getX()*factor, b.getY()*factor);
		c = new Point(c.getX()*factor, c.getY()*factor);
		this.move(centroid);
		pArr = this.getPoints();
	}

}
