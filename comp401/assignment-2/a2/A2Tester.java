package a2;

public class A2Tester {

	public static void main(String[] args) {
		boolean all_passed = true;
		if (!triangle_test()) {
			System.out.println("One or more triangle test not passed");
			all_passed = false;
		} 
		if (!rectangle_test()) {
			System.out.println("One or more rectangle test not passed");
			all_passed = false;
		}
		if (!polygon_test()) {
			System.out.println("One or more polygon test not passed");
			all_passed = false;
		}
		

		if (all_passed) {
			System.out.println("All tests passed");
		}
	}
	
	public static boolean triangle_test() {
		boolean tests_passed = true;
		Triangle t = new TriangleImpl(new Point(0,0), new Point(0,5), new Point(5,0));

		if ((!eps_point_compare(t.getA(), new Point(0,0))) ||
			(!eps_point_compare(t.getB(), new Point(0,5))) ||
			(!eps_point_compare(t.getC(), new Point(5,0)))) {
			System.out.println("T1: Points returned from getters do not match points provided in constructor");
			tests_passed = false;
		}
		
		if (!t.isRight()) {
			System.out.println("T2: Expected triangle to be a right triangle");
			tests_passed = false;
		}
		
		if (t.getCategory() != Triangle.Category.ISOSCELES) {
			System.out.println("T3: Expected triangle to be ISOSCELES");
			tests_passed = false;
		}
		
		Polygon p = (Polygon) t;

		if (!eps_compare(p.getArea(), 12.5)) {
			System.out.println("T4: Triangle area expected to be 12.5");
			tests_passed = false;
		}

		Point va = p.getVertexAverage();
		
		if (!eps_point_compare(va, p.getCentroid())) {
			System.out.println("T5: Expected vertex average and centroid to be the same");
			tests_passed = false;
		}
		
		if (!eps_point_compare(va, new Point(1.66666, 1.6666))) {
			System.out.println("T6: Expected vertex average to be (1.6666, 1.666)");
			tests_passed = false;
		}
		
		Rectangle r = p.getBoundingBox();
		
		if ((!eps_point_compare(r.getLowerLeft(), new Point(0,0))) ||
			(!eps_point_compare(r.getUpperRight(), new Point(5.0, 5.0)))) {

			System.out.println("T7: Bounding box of triangle is incorrect");
			tests_passed = false;
		}

		
		p.scale(2.0);
		
		if (!eps_compare(p.getArea(), 50)) {
			System.out.println("T8: Area after scale should be 50.0");
			tests_passed = false;
		}
		
		if (!t.isRight()) {
			System.out.println("T9: Triangle should remain right triangle after scale");
			tests_passed = false;
		}
		
		if (t.getCategory() != Triangle.Category.ISOSCELES) {
			System.out.println("T10: Triangle should remain ISOSCELES after scale");
			tests_passed = false;
		}

		if (!eps_point_compare(va, p.getVertexAverage())) {
			System.out.println("T11: Triangle vertex average should be the same after scale");
			tests_passed = false;
		}
		
		t = new TriangleImpl(new Point(0,0), new Point(4,0), new Point(2, 3.4641));
		p = (Polygon) t;
		
		if (t.getCategory() != Triangle.Category.EQUILATERAL) {
			System.out.println("T12: Triangle should be EQUILATERAL");
			tests_passed = false;
		}

		p.move(new Point(2,2));
		
		if (!eps_point_compare(p.getCentroid(), new Point(2,2))) {
			System.out.println("T13: Moving operation did not put centroid where expected");
			tests_passed = false;
		}
		
		p.scale(-0.5);
		
		if (!eps_compare(p.getArea(), 1.732)) {
			System.out.println("T14: Area after negative scale not correct");
			tests_passed = false;
		}
		
		if ((!eps_point_compare(p.getBoundingBox().getLowerLeft(), new Point(1.0, 0.845))) ||
			(!eps_point_compare(p.getBoundingBox().getUpperRight(), new Point(3.0, 2.577)))) {
			System.out.println("T15: Bounding box after negative scale not correct");			
			tests_passed = false;
		}
		
		return tests_passed;
	}

	public static boolean rectangle_test() {
		boolean result = true;
		
		// Create a rectangle with lower left and upper right
		// given to constructor and test corner properties
		
		Rectangle r1 = new RectangleImpl(new Point(-1.2, -3), new Point(1.8, 0));
		
		if (!eps_point_compare(new Point(-1.2, -3), r1.getLowerLeft())) {
			System.out.println("R1: r1 lower left property not correct");
			result = false;
		}

		if (!eps_point_compare(new Point(-1.2, 0), r1.getUpperLeft())) {
			System.out.println("R2: r1 upper left property not correct");
			result = false;
		}
		
		if (!eps_point_compare(new Point(1.8, 0), r1.getUpperRight())) {
			System.out.println("R3: r1 upper right property not correct");
			result = false;
		}

		if (!eps_point_compare(new Point(1.8, -3), r1.getLowerRight())) {
			System.out.println("R4: r1 lower right property not correct");
			result = false;
		}

		if (!r1.isSquare()) {
			System.out.println("R5: rectangle r1 should be square");
			result = false;
		}

		Polygon p1 = (Polygon) r1;
		
		if (!eps_compare(p1.getArea(), 9.0)) {
			System.out.println("R6: rectangle r1 (as polygon p1) should have area of 9.0");
			result = false;
		}
		
		if (!eps_point_compare(new Point(0.3, -1.5), p1.getCentroid())) {
			System.out.println("R7: rectangle r1 (as polygon p1) has incorrect centroid");
			result = false;
		}
		
		if (!eps_point_compare(p1.getCentroid(), p1.getVertexAverage())) {
			System.out.println("R8: rectangle centroid should be same as rectangle vertex average");
			result = false;
		}
		
		Point p1_pre_scale_centroid = p1.getCentroid();
		
		p1.scale(0.6666666);
		
		if (!r1.isSquare()) {
			System.out.println("R9: rectangle r1 should still be square after scaling");
			result = false;
		}
		
		if (!eps_point_compare(p1_pre_scale_centroid, p1.getCentroid())) {
			System.out.println("R10: rectangle centroid should remain same after scaling");
		}

		
		// Create a rectangle with upper left and lower right
		// given to constructor and test corner properties
		
		Rectangle r2 = new RectangleImpl(new Point(-2.5, 2.5), new Point(1.5, -0.5));
		
		if (!eps_point_compare(new Point(-2.5, -0.5), r2.getLowerLeft())) {
			System.out.println("R11: lower left property not correct");
			result = false;
		}

		if (!eps_point_compare(new Point(-2.5, 2.5), r2.getUpperLeft())) {
			System.out.println("R12: upper left property not correct");
			result = false;
		}
		
		if (!eps_point_compare(new Point(1.5, 2.5), r2.getUpperRight())) {
			System.out.println("R13: upper right property not correct");
			result = false;
		}

		if (!eps_point_compare(new Point(1.5, -0.5), r2.getLowerRight())) {
			System.out.println("R14: lower right property not correct");
			result = false;
		}

		if (r2.isSquare()) {
			System.out.println("R15: rectangle 2 should NOT be square");
			result = false;
		}

		
		Polygon p2 = (Polygon) r2;
		
		Point old_upper_left = r2.getUpperLeft();
		Point old_upper_right = r2.getUpperRight();
		Point old_lower_left = r2.getLowerLeft();
		Point old_lower_right = r2.getLowerRight();
		
		p2.scale(-1.0);
		
		if (!eps_point_compare(r2.getUpperLeft(), old_upper_left)) {
			System.out.println("R16: incorrect upper left after negative scale");
			result = false;
		}
		
		if (!eps_point_compare(r2.getUpperRight(), old_upper_right)) {
			System.out.println("R17: incorrect upper left after negative scale");
			result = false;
		}

		if (!eps_point_compare(r2.getLowerLeft(), old_lower_left)) {
			System.out.println("R18: incorrect upper left after negative scale");
			result = false;
		}

		if (!eps_point_compare(r2.getLowerRight(), old_lower_right)) {
			System.out.println("R19: incorrect upper left after negative scale");
			result = false;
		}
		
		return result;
		
	}
	
	public static boolean polygon_test() {
		boolean result = true;
		
		Point a = new Point(-1, -2);
		Point b = new Point(-2.5, -3.5);
		Point c = new Point(-3.1, -1.4);
		Point d = new Point(-2, 1.3);
		Point e = new Point(1, 1);
		Point f = new Point(2, 3);
		Point g = new Point(3, 0.5);
		Point h = new Point(5, 2.5);
		Point i = new Point(4, -2);
		Point j = new Point(1.5, -1.5);
		
		Point[] poly_points = new Point[] {a, b, c, d, e, f, g, h, i, j};
		
		Polygon p1 = new PolygonImpl(poly_points);

		if (p1.getNumSides() != 10) {
			System.out.println("P1: polygon does not have correct number of sides");
			result = false;
		}
		
		if (!eps_compare(p1.getArea(), 24.24)) {
			System.out.println("P2: polygon area is wrong");
			result = false;
		}

		if (!eps_point_compare(p1.getCentroid(), new Point(0.7646, -0.3067))) {
			System.out.println("P3: polygon centroid is wrong");
			result = false;
		}
		
		if (!eps_point_compare(p1.getVertexAverage(), new Point(0.79, -0.21))) {
			System.out.println("P4: polygon vertex average is wrong");
			result = false;
		}

		// Test encapsulation by changing poly_points. This should change nothing
		// about the polygon.
		poly_points[1] = new Point(100,100);
		poly_points[2] = new Point(200,200);
		
		if (!eps_compare(p1.getArea(), 24.24)) {
			System.out.println("P5: Changing array passed to constructor changes polygon. Perhaps you forgot to clone the array passed in?");
			result = false;
		}
		
		// Test encapsulation by changing array returned by getPoint(). Should change
		// nothing about the polygon
		
		poly_points = p1.getPoints();
		poly_points[1] = new Point(100,100);
		poly_points[2] = new Point(200,200);
		
		if (!eps_compare(p1.getArea(), 24.24)) {
			System.out.println("P6: Changing array received from getPoints() changes polygon. Perhaps you forgot to clone an array before returning it?");
			result = false;
		}
		
		// Should be able to create same polygon from same points given in different 
		// order (i.e., counter clockwise) starting from a different point.
		
		poly_points = new Point[] {e, d, c, b, a, j, i, h, g, f};
		Polygon p2 = new PolygonImpl(poly_points);
		
		if (!eps_compare(p1.getArea(), p2.getArea())) {
			System.out.println("P7: two polygons from same points but provided in different order should have same area");
			result = false;
		}

		if (!eps_point_compare(p1.getCentroid(), p2.getCentroid())) {
			System.out.println("P8: two polygons from same points but provided in different order should have same centroid");
			result = false;
		}

		Point old_p2_centroid = p2.getCentroid();

		p2.move(4,4);
		if (!eps_point_compare(p2.getCentroid(), new Point(old_p2_centroid.getX()+4, old_p2_centroid.getY()+4))) {
			System.out.println("P9: moving polygon by 4 in both x and y should move centroid by sqme amount");
			result = false;
		}
		
		p2.scale(3.0);
		
		if (!eps_compare(p2.getArea(), 24.24*9)) {
			System.out.println("P10: Scaling by factor of 3 should scale area by factor of 9");
			result = false;
		}
		
		p2.move(new Point(1,1));
		if (!eps_point_compare(p2.getCentroid(), new Point(1,1))) {
			System.out.println("P11: moving to (1,1) should put centroid at (1,1)");
			result = false;
		}

		p2.scale(1.0/3.0);
		if (!eps_point_compare(p2.getCentroid(), new Point(1,1))) {
			System.out.println("P12: scaling should not affect centroid");
			result = false;
		}
		
		if (!eps_compare(p2.getArea(), 24.24)) {
			System.out.println("P13: rescaling by 1/3 should restore original area");
			result = false;
		}
		
		return result;
	}
	
	public static boolean eps_compare(double a, double b) {
		return (Math.abs(a-b) < 0.01);
	}
	
	public static boolean eps_point_compare(Point a, Point b) {
		return (eps_compare(a.getX(), b.getX()) && eps_compare(a.getY(), b.getY()));
	}
}