package a6;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ChessGame extends java.util.Observable implements
		java.util.Observer {
	private ChessBoard board;
	private ChessPlayer player1;
	private ChessPlayer player2;
	private List<ChessMove> log;

	public ChessGame(ChessPlayer player1, ChessPlayer player2) {
		this.player1 = player1;
		this.player2 = player2;
		log = new ArrayList<ChessMove>();
		board = new ChessBoard();

		new Rook(player1, this, new ChessPosition(0, 0));
		new Knight(player1, this, new ChessPosition(1, 0));
		new Bishop(player1, this, new ChessPosition(2, 0));
		new Queen(player1, this, new ChessPosition(3, 0));
		new King(player1, this, new ChessPosition(4, 0));
		new Bishop(player1, this, new ChessPosition(5, 0));
		new Knight(player1, this, new ChessPosition(6, 0));
		new Rook(player1, this, new ChessPosition(7, 0));

		for (int i = 0; i < 8; i++) {
			new Pawn(player1, this, new ChessPosition(i, 1));
		}

		new Rook(player2, this, new ChessPosition(0, 7));
		new Knight(player2, this, new ChessPosition(1, 7));
		new Bishop(player2, this, new ChessPosition(2, 7));
		new Queen(player2, this, new ChessPosition(3, 7));
		new King(player2, this, new ChessPosition(4, 7));
		new Bishop(player2, this, new ChessPosition(5, 7));
		new Knight(player2, this, new ChessPosition(6, 7));
		new Rook(player2, this, new ChessPosition(7, 7));

		for (int i = 0; i < 8; i++) {
			new Pawn(player2, this, new ChessPosition(i, 6));
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

	@Override
	public void update(Observable o, Object arg) {
		// Type cast arg to ChessMove and add to log
		ChessMove m = (ChessMove) arg;
		log.add(m);

		// Call setChanged
		setChanged();
		// Notify observers
		notifyObservers(arg);
	}

	public int getLogSize() {
		return log.size();
	}

	public ChessMove[] getMoves(int n) {
		int lSize = this.getLogSize();
		ChessMove[] moves;

		if (lSize == 0) {
			moves = new ChessMove[0];
			return moves;
		}

		if (n == 0 || Math.abs(n) > lSize) {
			moves = new ChessMove[lSize];
			moves = log.toArray(moves);
		} else {
			int absN = Math.abs(n);
			moves = new ChessMove[absN];
			int dir = 1;
			int index = 0;
			int k = 0;
			if (n < 0) {
				index = lSize - 1;
				dir = -1;
			}
			while (absN > 0) {
				moves[k++] = log.get(index);
				index = index + dir;
				absN--;
			}
		}
		return moves;
	}

	public void undo() {
		int lSize = getLogSize();
		if (lSize == 0) {
			return;
		}
		ChessMove m = log.remove(lSize - 1);
		ChessPiece p = m.getPiece();
		ChessPiece captured = m.getCaptured();
		ChessPosition from = m.getFrom();
		ChessPosition to = m.getTo();

		// Move the piece -> "from"
		board.removePieceFrom(to);
		board.placePieceAt(p, from);

		// Restore captured piece at "to"
		if (captured != null) {
			board.placePieceAt(captured, to);
		}
		// Call setChanged
		setChanged();
		// Notify observers
		notifyObservers(null);

	}
}
