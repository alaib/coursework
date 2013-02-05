package a2;

public interface Triangle {

    // The following enumeration is defined in order
    // to name the triangle category returned by the 
    // method getCategory();
    
    public enum Category {ISOSCELES, EQUILATERAL, SCALENE};
    
    // getCategory() should return on of the three possible categories
    // enumerated above as appropriate.
    
    Category getCategory();
    
    // isRight() should return true if the triangle is a right triangle.
    // Whether or not a triangle is a right triangle can be checked by
    // confirming whether or not the square of the longest side is equal
    // to the sum of the squares of the other two sides (i.e., confirming
    // the Pythagorean Theorem).
    
    boolean isRight();
    
    // getA(), getB(), and getC() should return Point objects that represent
    // the three points of the triangle.
    
    Point getA();
    Point getB();
    Point getC();    
}
