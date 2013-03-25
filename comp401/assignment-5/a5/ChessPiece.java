package a5;

public abstract class ChessPiece {

	private ChessPlayer owner;
	private ChessGame game;
	private ChessPosition position;
	protected char mark;

	protected ChessPiece(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		this.owner = owner;
		this.game = game;
		this.position = null;

		game.getBoard().placePieceAt(this, init_position);
	}

	public ChessPlayer getOwner() {
		return owner;
	}

	public ChessGame getGame() {
		return game;
	}


	public ChessPosition getPosition() {
		return position;
	}

	public void setPosition(ChessPosition new_position) {
		position = new_position;
	}


	public void moveTo(ChessPosition destination) 
			throws IllegalMove
			{
		// Subclass is required to check for legality of move
		// with respect to piece type, so here we just
		// make the move generally.
		if (destination.equals(getPosition())) {
			throw new IllegalMove(this, getPosition(), destination);
		}

		ChessBoard board = game.getBoard();

		ChessPiece at_dest = board.getPieceAt(destination);

		if (at_dest != null) {
			if (at_dest.getOwner() == getOwner()) {
				throw new IllegalMove(this, getPosition(), destination);
			}
			board.removePieceFrom(destination);
		}

		board.removePieceFrom(getPosition());
		board.placePieceAt(this, destination);		
			}

	public char getMark() {
		return mark;
	}

	protected boolean checkLineOfSight(ChessPosition start, ChessPosition end) throws IllegalMove
	{
		// Checks all positions between start and end (exclusive of either
		// start or end) to make sure they are empty.

		int dx = 0;
		if (start.getX() < end.getX()) {
			dx = 1;
		} else if (start.getX() > end.getX()) {
			dx = -1;
		}

		int dy = 0;
		if (start.getY() < end.getY()) {
			dy = 1;
		} else if (start.getY() > end.getY()) {
			dy = -1;
		}

		if ((dx != 0) && (dy != 0)) {
			// If both x and y are changing, then
			// they must change by the same absolute
			// amount (i.e., move on a perfect diagonal)
			if (Math.abs(start.getX() - end.getX()) != Math.abs(start.getY() - end.getY())) {
				throw new IllegalMove(this, start, end);
			}
		}

		ChessPosition next_pos = new ChessPosition(start.getX()+dx, start.getY()+dy);
		while (!next_pos.equals(end)) {
			if (getGame().getBoard().getPieceAt(next_pos) != null) {
				throw new IllegalMove(this, start, end);
			}
			next_pos = new ChessPosition(next_pos.getX()+dx, next_pos.getY()+dy);
		}
		// If we are here, all is good.
		return true;
	}
}

class Rook extends ChessPiece {
	public Rook(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'r';
		} else {
			mark = 'R';
		}
	}	

	@Override
	public void moveTo(ChessPosition destination) throws IllegalMove
	{
		if ((getPosition().getX() != destination.getX()) &&
				(getPosition().getY() != destination.getY())) {
			throw new IllegalMove(this, getPosition(), destination);
		}
		checkLineOfSight(getPosition(), destination);
		super.moveTo(destination);
	}
}

class Bishop extends ChessPiece {
	public Bishop(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'b';
		} else {
			mark = 'B';
		}
	}

	@Override
	public void moveTo(ChessPosition destination) throws IllegalMove
	{
		if ((getPosition().getX() == destination.getX()) ||
				(getPosition().getY() == destination.getY())) {
			throw new IllegalMove(this, getPosition(), destination);
		}
		super.moveTo(destination);
	}
}

class Knight extends ChessPiece {
	public Knight(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'n';
		} else {
			mark = 'N';
		}
	}

	@Override
	public void moveTo(ChessPosition destination) throws IllegalMove
	{
		int abs_dx = Math.abs(destination.getX()-getPosition().getX());
		int abs_dy = Math.abs(destination.getY()-getPosition().getY());

		if ((abs_dx + abs_dy != 3) || (abs_dx == 0) || (abs_dy == 0)) {
			throw new IllegalMove(this, getPosition(), destination);
		}

		super.moveTo(destination);
	}
}

class Queen extends ChessPiece {
	public Queen(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'q';
		} else {
			mark = 'Q';
		}
	}	

	@Override
	public void moveTo(ChessPosition destination) throws IllegalMove
	{
		checkLineOfSight(getPosition(), destination);
		super.moveTo(destination);
	}
}

class King extends ChessPiece {
	public King(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'k';
		} else {
			mark = 'K';
		}
	}

	@Override
	public void moveTo(ChessPosition destination) throws IllegalMove
	{
		int abs_dx = Math.abs(destination.getX()-getPosition().getX());
		int abs_dy = Math.abs(destination.getY()-getPosition().getY());

		if ((abs_dx > 1) || (abs_dy > 1)) {
			throw new IllegalMove(this, getPosition(), destination);
		}
		super.moveTo(destination);
	}
}

class Pawn extends ChessPiece {
	public Pawn(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'p';
		} else {
			mark = 'P';
		}
	}

	@Override
	public void moveTo(ChessPosition destination) throws IllegalMove
	{
		int dx = destination.getX()-getPosition().getX();
		int dy = destination.getY()-getPosition().getY();

		if (Math.abs(dy) > 2) {
			throw new IllegalMove(this, getPosition(), destination);
		}

		if (getOwner() == getGame().getPlayer1()) {
			if (dy < 1) {
				throw new IllegalMove(this, getPosition(), destination);
			}

			if ((dy == 2) && (getPosition().getY() != 1)) {
				throw new IllegalMove(this, getPosition(), destination);
			} 
		} else {
			if (dy > -1) {
				throw new IllegalMove(this, getPosition(), destination);
			}

			if ((dy == -2) && (getPosition().getY() != 6)) {
				throw new IllegalMove(this, getPosition(), destination);
			}
		}

		if (dx != 0) {
			// Movement in x direction implies capture.
			// Must make sure that dx is 1 and that there
			// is a piece at the destination.
			// Don't need to worry about whose piece because
			// superclass will do that for me.
			if (Math.abs(dx) != 1) {
				throw new IllegalMove(this, getPosition(), destination);
			}

			if (getGame().getBoard().getPieceAt(destination) == null) {
				throw new IllegalMove(this, getPosition(), destination);				
			}
		} else {
			// Here if only moving "forward".
			// Can't move forward into a piece even regardless
			// of who owns it, so check to make sure
			// destination is clear.
			if (getGame().getBoard().getPieceAt(destination) != null) {
				throw new IllegalMove(this, getPosition(), destination);
			}
		}
		
		// May need to check line of sight if moving 2 so do this regardless
		// This will also prevent trying to move 2 in y direction while
		// moving 1 in x direction and capturing since move angle will be
		// illegal according to checkLineOfSight.
		checkLineOfSight(getPosition(), destination);
		super.moveTo(destination);
	}
}
