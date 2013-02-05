package a2;

public class RectangleImpl implements Rectangle, Polygon {
	Point lowerLeft, upperRight;
	Point []pArr;

	public RectangleImpl(Point p1, Point p2){
		if(p1.getX() < p2.getX() && p1.getY() < p2.getX()){
			//lowerLeft and upperRight
			lowerLeft = new Point(p1.getX(), p1.getY());
			upperRight = new Point(p2.getX(), p2.getY());
		}else{
			//upper left and lower right
			lowerLeft = new Point(p1.getX(), p2.getY());
			upperRight = new Point(p2.getX(), p1.getY());
		}
		pArr = this.getPoints();
	}
	
	@Override
	public Point[] getPoints() {
		Point []pArr = new Point[4];
		pArr[0] = this.getLowerLeft();
		pArr[1] = this.getUpperLeft();
		pArr[2] = this.getUpperRight();
		pArr[3] = this.getLowerRight();
		return pArr;
	}

	@Override
	public int getNumSides() {
		return pArr.length;
		// TODO Auto-generated method stub
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
		Rectangle r = new RectangleImpl(this.lowerLeft, this.upperRight);
		return r;
	}

	@Override
	public Point getCentroid() {
		double x = (upperRight.getX() + lowerLeft.getX())/2;
		double y = (upperRight.getY() + lowerLeft.getY())/2;
		Point p = new Point(x, y);
		return p;
	}

	@Override
	public double getArea() {
		double x = Math.abs(upperRight.getX() - lowerLeft.getX());
		double y = Math.abs(upperRight.getY() - lowerLeft.getY());
		double area = x*y;
		return area;
	}

	@Override
	public void move(double dx, double dy) {
		lowerLeft = lowerLeft.translate(dx, dy);
		upperRight = upperRight.translate(dx, dy);
		pArr = this.getPoints();
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
		double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
		for(int i = 0; i < pArr.length; i++){
			pArr[i] = new Point(pArr[i].getX()*factor, pArr[i].getY()*factor);
			if(pArr[i].getX() > maxX){
				maxX = pArr[i].getX();
			}
			if(pArr[i].getY() > maxY){
				maxY = pArr[i].getY();
			}
			if(pArr[i].getX() < minX){
				minX = pArr[i].getX();
			}
			if(pArr[i].getY() < maxY){
				minY = pArr[i].getY();
			}
		}
		//Recheck lowerLeft and upperRight
		lowerLeft = new Point(minX, minY);
		upperRight = new Point(maxX, maxY);
		this.move(centroid);
		pArr = this.getPoints();
	}

	@Override
	public Point getLowerLeft() {
		return lowerLeft;
	}

	@Override
	public Point getLowerRight() {
		Point lowerRight = new Point(upperRight.getX(), lowerLeft.getY());
		return lowerRight;
	}

	@Override
	public Point getUpperRight() {
		return upperRight;
	}

	@Override
	public Point getUpperLeft() {
		Point upperLeft = new Point(lowerLeft.getX(), upperRight.getY());
		return upperLeft;
	}

	@Override
	public boolean isSquare() {
		double length = Math.abs(lowerLeft.getX() - upperRight.getX());
		double width = Math.abs(lowerLeft.getY() - upperRight.getY());
		if(length - width < 0.00001){
			return true;
		}
		return false;
	}

}