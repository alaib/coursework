package a6;

public abstract class ChessException extends Exception {
	protected ChessException(String message) {
		super(message);
	}
}

class IllegalMove extends ChessException {
	
	public IllegalMove(ChessPiece p, ChessPosition from, ChessPosition to) {
		super("Illegal move: piece " + p.toString() + " can not move from " + from.toString() + " to " + to.toString());
	}
}