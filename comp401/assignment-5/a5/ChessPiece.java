package a5;

import java.util.ArrayList;
import java.util.List;

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
	
	public void moveTo(ChessPosition destination) throws IllegalMove{
		//If destination is same as position, throw exception
		if(destination.equals(position)){
			throw new IllegalMove(this, position, destination);
		}
	
		//Valid move for the piece, check if there is collision or not
		ChessBoard board = this.getGame().getBoard();
		ChessPiece prevPiece = board.getPieceAt(destination);
		
		if(prevPiece != null){
			//position is occupied by a piece
			if(prevPiece.getOwner() == this.getOwner()){
				//the position is occupied by a piece of the same player
				throw new IllegalMove(this, position, destination);
			}else{
				//remove opponent piece from board
				board.removePieceFrom(destination);
				
				//remove current piece from old pos and put it in new position
				ChessPiece p = board.getPieceAt(this.position);
				board.removePieceFrom(this.position);
				board.placePieceAt(p, destination);
			}
		}else{
			//position is free
			//remove current piece from old pos and put it in new position
			ChessPiece p = board.getPieceAt(this.position);
			board.removePieceFrom(this.position);
			board.placePieceAt(p, destination);
		}
	}
	
	public char getMark() {
		return mark;
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
	
	public void moveTo(ChessPosition destination) throws IllegalMove{
		ChessPosition pos = this.getPosition();
		
		int posX = pos.getX();
		int posY = pos.getY();
		int destX = destination.getX();
		int destY = destination.getY();
		
		//for rook, the destination X or Y position has to be same as current Pos, else throw exception
		if(destX != posX && destY != posY){
			throw new IllegalMove(this, pos, destination);
		}
	
		//Call moveHorizontal
		boolean status = moveHorizontal(destination);
		
		//everything failed, throw Illegal move
		if(!status){
			throw new IllegalMove(this, pos, destination);
		}
	}
	
	public boolean moveHorizontal(ChessPosition destination) throws IllegalMove{
		ChessPosition pos = this.getPosition();
		ChessGame game = this.getGame();
		ChessBoard board = game.getBoard();
		
		int posX = pos.getX();
		int posY = pos.getY();
		int currX, currY;
		int dir = -1, maxY = 0, minY = 7, maxX = 7, minX = 0;
		if(this.getOwner() == this.getGame().getPlayer1()){
			dir = 1;
			maxY = 7;
			minY = 0;
		}
		
		//Shoot rays in four directions (up, down, left, right) until you find an occupied square or destination
		//Up
		currX = posX; currY = posY + dir;
		while(true){
			/* flip bounds checking based on player 1 or 2 */
			if(dir == -1){
				if(currY < maxY){
					break;
				}
			}else{
				if(currY > maxY){
					break;
				}
			}
			
			ChessPosition p = new ChessPosition(currX, currY);
			//if p is destination, call parent moveTo
			if(p.equals(destination)){
				super.moveTo(destination);
				return true;
			}
			//there is a piece blocking the path
			if(board.getPieceAt(p) != null){
				break;
			}
			currY += dir;
		}
		
		//Down
		currX = posX; currY = posY - dir;
		while(true){
			/* flip bounds checking based on player 1 or 2 */
			if(dir == -1){
				if(currY > minY){
					break;
				}
			}else{
				if(currY < minY){
					break;
				}
			}
			ChessPosition p = new ChessPosition(currX, currY);
			//if p is destination, call parent moveTo
			if(p.equals(destination)){
				super.moveTo(destination);
				return true;
			}
			//there is a piece blocking the path
			if(board.getPieceAt(p) != null){
				break;
			}
			currY -= dir;
		}
		
		//Left
		currX = posX - 1; currY = posY;
		while(currX >= minX){
			ChessPosition p = new ChessPosition(currX, currY);
			//if p is destination, call parent moveTo
			if(p.equals(destination)){
				super.moveTo(destination);
				return true;
			}
			//there is a piece blocking the path
			if(board.getPieceAt(p) != null){
				break;
			}
			currY -= 1;
		}
		
		//Right
		currX = posX + 1; currY = posY;
		while(currX <= maxX){
			ChessPosition p = new ChessPosition(currX, currY);
			//if p is destination, call parent moveTo
			if(p.equals(destination)){
				super.moveTo(destination);
				return true;
			}
			//there is a piece blocking the path
			if(board.getPieceAt(p) != null){
				break;
			}
			currY -= 1;
		}
		//if control reaches here, then unable to reach destination due to blockade
		return false;
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
	
	public void moveTo(ChessPosition destination) throws IllegalMove{
		ChessPosition pos = this.getPosition();
		//Generate 8 possible moves and check if destination happens to be one such move
		int []dx = {1, 2, 2, 1,-1,-2,-2,-1};
		int []dy = {2, 1,-1,-2, 2, 1,-1,-2};
		List<ChessPosition> validMoves = new ArrayList<ChessPosition>();
		for(int i = 0; i < dx.length; i++){
			try{
				ChessPosition p = new ChessPosition(pos.getX()+dx[i], pos.getY()+dy[i]);
				validMoves.add(p);
			}catch(IllegalArgumentException e){
				//Don't do anything, out of bounds
			}
		}
		if(validMoves.contains(destination)){
			super.moveTo(destination);
		}else{
			throw new IllegalMove(this, pos, destination);
		}
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
	
	public void moveTo(ChessPosition destination) throws IllegalMove{
		//The absolute difference in position and destination has to be <= 1
		ChessPosition pos = this.getPosition();
		int posX = pos.getX();
		int posY = pos.getY();
		int destX = destination.getX();
		int destY = destination.getY();
		
		if(Math.abs(posX - destX) <= 1 && Math.abs(posY - destY) <= 1){
			super.moveTo(destination);
		}else{
			throw new IllegalMove(this, pos, destination);
		}
	}
}

class Pawn extends ChessPiece {
	boolean firstMove;
	
	public Pawn(ChessPlayer owner, ChessGame game, ChessPosition init_position) {
		super(owner, game, init_position);
		if (owner == game.getPlayer1()) {
			mark = 'p';
		} else {
			mark = 'P';
		}
		firstMove = true;
	}
	
	public void moveTo(ChessPosition destination) throws IllegalMove{
		ChessPosition pos = this.getPosition();
		ChessGame game = this.getGame();
		ChessBoard board = game.getBoard();
		ChessPlayer owner = this.getOwner();
		
		int dx = 1;
		int dy = -1;
		if(owner == game.getPlayer1()){
			dy = 1;
		}
		
		//Get a list of valid positions and check if destination is one of them, else throw IllegalMove
		//2 spaces ahead and free slot
		if(firstMove){
			ChessPosition p1 = new ChessPosition(pos.getX(), pos.getY() + 2 * dy);
			if(destination.equals(p1) && board.getPieceAt(p1) == null){
				super.moveTo(destination);
				firstMove = false;
				return;
			}
		}
		
		List<ChessPosition> validMoves = new ArrayList<ChessPosition>();
		//1 space ahead and free slot
		try{
			ChessPosition p2 = new ChessPosition(pos.getX(), pos.getY() + dy);
			if(board.getPieceAt(p2) == null){
				validMoves.add(p2);
			}
		}catch(IllegalArgumentException e){
			//Don't do anything, out of board coordinates
		}
		
		//right diagonal capture
		try{
			ChessPosition p3 = new ChessPosition(pos.getX() + dx, pos.getY() + dy);
			ChessPiece p3Piece = board.getPieceAt(p3);
			if(p3Piece != null && p3Piece.getOwner() != owner){
				validMoves.add(p3);
			}
		}catch(IllegalArgumentException e){
			//Don't do anything, out of board coordinates
		}
		
		//left diagonal capture
		try{
			ChessPosition p4 = new ChessPosition(pos.getX() - dx, pos.getY() + dy);
			ChessPiece p4Piece = board.getPieceAt(p4);
			if(p4Piece != null && p4Piece.getOwner() != owner){
				validMoves.add(p4);
			}
		}catch(IllegalArgumentException e){
			//Don't do anything, out of board coordinates
		}
		
		if(validMoves.contains(destination)){
			super.moveTo(destination);
		}else{
			throw new IllegalMove(this, pos, destination);
		}
	}
}