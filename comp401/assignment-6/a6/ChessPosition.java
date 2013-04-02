package a6;

public class ChessPosition {
	private int x;
	private int y;
	
	public ChessPosition(int x, int y) {
		if ((x < 0) || (x > 7)) {
			throw new IllegalArgumentException("x value of chess position out of range: " + x);
		}
		
		if ((y < 0) || (y > 7)) {
			throw new IllegalArgumentException("y value of chess position out of range: " + y);
		}
		
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		ChessPosition p = (ChessPosition) o;
		return ((p.getX() == x) && (p.getY() == y));
	}
	/*
	public boolean equals(Object other){
		System.out.println("Equals method called");
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof ChessPosition))return false;
		ChessPosition p = (ChessPosition) other;
		return ((p.getX() == x) && (p.getY() == y));
	}
	*/
}
