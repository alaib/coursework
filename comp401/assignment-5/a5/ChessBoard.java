package a5;

public class ChessBoard {

	private ChessPiece spaces[][];
	
	public ChessBoard() {
		spaces = new ChessPiece[8][8];	
	}
	
	public void placePieceAt(ChessPiece piece, ChessPosition position) 
	{
		spaces[position.getX()][position.getY()] = piece;
		piece.setPosition(position);
	}
	
	public ChessPiece getPieceAt(ChessPosition position) {
		return spaces[position.getX()][position.getY()];
	}
	
	public void removePieceFrom(ChessPosition position) {
		ChessPiece p = getPieceAt(position);
		if (p != null) {
			p.setPosition(null);
			spaces[position.getX()][position.getY()] = null;
		}		
	}
	
	@Override
	public String toString() {
		String result = "";
		String row_sep = " +-+-+-+-+-+-+-+-+\n";
		
		result += row_sep;
		
		for (int r=7; r>=0; r--) {
			result += r;
			for (int c=0; c<8; c++) {
				result += "|";
				ChessPiece piece = getPieceAt(new ChessPosition(c,r));
				if (piece != null) {
					result += piece.getMark();
				} else {
					result += " ";
				}
			}
			result += "|\n";
			result += row_sep;
		}
		result += "  0 1 2 3 4 5 6 7\n";

		return result;
	}
}
