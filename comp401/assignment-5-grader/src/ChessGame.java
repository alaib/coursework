package a5;

public class ChessGame {

	private ChessBoard board;
	private ChessPlayer player1;
	private ChessPlayer player2;
	
	public ChessGame(ChessPlayer player1, ChessPlayer player2) {
		this.player1 = player1;
		this.player2 = player2;
		board = new ChessBoard();
		
		new Rook(player1, this, new ChessPosition(0,0));
		new Knight(player1, this, new ChessPosition(1,0));
		new Bishop(player1, this, new ChessPosition(2,0));
		new Queen(player1, this, new ChessPosition(3,0));
		new King(player1, this, new ChessPosition(4,0));
		new Bishop(player1, this, new ChessPosition(5,0));
		new Knight(player1, this, new ChessPosition(6,0));
		new Rook(player1, this, new ChessPosition(7,0));

		for (int i=0; i<8; i++) {
			new Pawn(player1, this, new ChessPosition(i,1));
		}

		new Rook(player2, this, new ChessPosition(0,7));
		new Knight(player2, this, new ChessPosition(1,7));
		new Bishop(player2, this, new ChessPosition(2,7));
		new Queen(player2, this, new ChessPosition(3,7));
		new King(player2, this, new ChessPosition(4,7));
		new Bishop(player2, this, new ChessPosition(5,7));
		new Knight(player2, this, new ChessPosition(6,7));
		new Rook(player2, this, new ChessPosition(7,7));

		for (int i=0; i<8; i++) {
			new Pawn(player2, this, new ChessPosition(i,6));
		}		
	}
	
	public ChessPlayer getPlayer1() {
		return player1;
	}
	
	public ChessPlayer getPlayer2() {
		return player2;
	}

	public ChessBoard getBoard() {
		return board;
	}
	
}
