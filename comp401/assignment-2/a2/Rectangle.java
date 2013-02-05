package a2;

public interface Rectangle {

    // getLowerLeft()
    // getLowerRight()
    // getUpperLeft()
    // getUpperRight()
    // These methods return Point objects that represent the respectively
    // named corners of the rectangle. Note, the rectangle is assumed to
    // be axis-aligned (i.e., not tilted or rotated).
    
    Point getLowerLeft();
    Point getLowerRight();
    Point getUpperRight();
    Point getUpperLeft();
    
    // isSquare() should return true if the rectangle is square (i.e., width
    // is equal to height).
    
    boolean isSquare();
}
