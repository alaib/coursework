package a2;

public interface Polygon {
	  
    // getPoints() is expected to return an array of Point objects 
    // that represent a closed polygon. 
    // Each consecutive pair of points in this array represents a side of the
    // polygon (with the last side of the polygon defined as going from the 
    // last point in the array back to the first point in the array).
    // The polygon is assumed to be a simple polygon (i.e., not self intersecting),
    // and points in the array can be given in either clockwise or 
    // counterclockwise order.
    
    Point[] getPoints();
    
    // getNumSides() returns the number of sides of the polygon. 

    int getNumSides();
    
    // getVertexAverage() returns a Point object that represents the
    // average X value and average Y value of the points that make up
    // the polygon
    
    Point getVertexAverage();

    // getBoundingBox() should return a Rectangle object that represents
    // the tightest enclosing axis-aligned rectangle that can be drawn around
    // the polygon
    
    Rectangle getBoundingBox();
    
    // getCentroid() should return a Point object that represents the centroid of
    // the polygon. Note that for arbitrary simple polygons, this value is NOT
    // the same as the vertex average described above.
    // The formula for the centroid of an arbitrary simple polygon is described
    // here: http://en.wikipedia.org/wiki/Polygon#Area_and_centroid
    
    Point getCentroid();
    
    // getArea() should return the area of the polygon. The formula for the
    // area of a simple polygon is described here:
    // http://en.wikipedia.org/wiki/Polygon#Area_and_centroid
    
    double getArea();
    
    // move(double dx, double dy) should move the polygon by the delta x and delta y
    // values specified as dx and dy.
    
    void move(double dx, double dy);
    
    // move(Point c) should move the polygon so that it's centroid in its new position
    // is located at Point c.
    
    void move(Point c);
    
    // scale(double factor) should shrink/grow the polygon by the factor specified
    // such that its centroid remains the same. For example, given a polygon p, the
    // result of calling p.scale(2.0) should be that the polygon grows by a factor of 2.
    // In other words, all of its sides are now twice as long but the overall shape 
    // remains the same and the centroid remains still. 
    // Hint: this function can be implemented by first moving the polygon so that
    // the centroid is at the origin (0,0); then transforming all of the points by
    // multiplying their x and y values by the factor; then moving the transformed
    // polygon so that its centroid is back to the original centroid position.
    
    void scale(double factor);
}
