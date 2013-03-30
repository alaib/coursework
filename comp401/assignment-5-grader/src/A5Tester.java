package a5;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class A5Tester {
	public ChessPlayer p1, p2;
	public ChessGame g;
	public ChessBoard b;

	@Before
	public void runBeforeEachTest() {
		p1 = new ChessPlayer("P1");
		p2 = new ChessPlayer("P2");

		g = new ChessGame(p1, p2);
		b = g.getBoard();
	}

	@Test
	public void testFirstRowMovesOnInitialBoard() {
		String funcName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		/*
		 * Let's try to move all chess pieces in 1st row of player 1, everything
		 * throws an exception
		 */
		int startY = 0, endY = 1, i;
		int failCount = 0;
		for (i = 0; i < 8; i++) {
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out
							.println("Player 1 - Trying to move 1st row pieces (there is a block)");
					printFailedToGenException("Player 1", from, to);
					failCount++;
				} catch (Exception e) {
					assert (e instanceof IllegalMove);
				}
			}
		}
		// Change startY and endY
		startY = 7;
		endY = 6;
		/*
		 * Let's try to move all chess pieces in 1st row of player 2, everything
		 * throws an exception
		 */
		for (i = 0; i < 8; i++) {
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out
							.println("Player 2 - Trying to move 1st row pieces (there is a block)");
					printFailedToGenException("Player 2", from, to);
					failCount++;
				} catch (Exception e) {
					assert (e instanceof IllegalMove);
				}
			}
		}
		printFooter(funcName, failCount);
	}

	// Pawn test functions
	@Test
	public void testPawnMovedToSamePos() {
		String funcName = new Object() { }.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Move a pawn to the same position - player 1
		int x = genRandom(0, 7);
		ChessPosition from = new ChessPosition(x, 1);
		ChessPosition to = new ChessPosition(x, 1);
		ChessPiece p = b.getPieceAt(from);
		if (p != null) {
			try {
				p.moveTo(to);
				System.out
						.println("Player 1 - Trying to move the pawn to same square");
				printFailedToGenException("Player 1", from, to);
				failCount++;
			} catch (Exception e) {
				assert (e instanceof IllegalMove);
			}
		}

		// Move a pawn to the same position - player 2
		x = genRandom(0, 7);
		from = new ChessPosition(x, 6);
		to = new ChessPosition(x, 6);
		p = b.getPieceAt(from);
		if (p != null) {
			try {
				p.moveTo(to);
				System.out
						.println("Player 2 - Trying to move the pawn to same square");
				printFailedToGenException("Player 2", from, to);
				failCount++;
			} catch (Exception e) {
				assert (e instanceof IllegalMove);
			}
		}
		printFooter(funcName, failCount);
	}

	@Test
	public void testPawnOneSquareMove() {
		String funcName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn can move one space on the board - player 1
		int x = genRandom(0, 7);
		ChessPosition from = new ChessPosition(x, 1);
		ChessPosition to = new ChessPosition(x, 2);
		ChessPiece p = b.getPieceAt(from);
		ChessPiece oldDestPiece = b.getPieceAt(to);
		if (p != null) {
			try {
				p.moveTo(to);
				//Check if pawn has actually been moved
				failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 1", failCount);
			} catch (IllegalMove e) {
				System.out
						.println("Player 1 - Trying to move a pawn one spaces up");
				printInvalidExceptionGen("Player 1", from, to);
				failCount++;
			}
		}

		// Let's test if a pawn can move one space on the board - player 2
		x = genRandom(0, 7);
		from = new ChessPosition(x, 6);
		to = new ChessPosition(x, 5);
		p = b.getPieceAt(from);
		oldDestPiece = b.getPieceAt(to);
		if (p != null) {
			try {
				p.moveTo(to);
				//Check if pawn has actually been moved
				failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 2", failCount);
			} catch (IllegalMove e) {
				System.out
						.println("Player 2 - Trying to move a pawn one spaces up");
				printInvalidExceptionGen("Player 2", from, to);
				failCount++;
			}
		}
		printFooter(funcName, failCount);
	}

	@Test
	public void testPawnTwoSquaresMove() {
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn can move two spaces on the board - player 1
		int x = genRandom(0, 7);
		ChessPosition from = new ChessPosition(x, 1);
		ChessPosition to = new ChessPosition(x, 3);
		ChessPiece p = b.getPieceAt(from);
		ChessPiece oldDestPiece = b.getPieceAt(to);
		if (p != null) {
			try {
				p.moveTo(to);
				//Check if pawn has actually been moved
				failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 1", failCount);
			} catch (IllegalMove e) {
				System.out.println("Player 1 - Trying to move a pawn two spaces up");
				printInvalidExceptionGen("Player 1", from, to);
				failCount++;
			}
		}

		// Let's try to move it again by two spaces, this should throw an
		// exception - player 1
		from = new ChessPosition(x, 3);
		to = new ChessPosition(x, 5);
		p = b.getPieceAt(from);
		if (p != null) {
			try {
				p.moveTo(to);
				System.out
						.println("Player 1 - Trying to move the pawn 2 spaces up multiple times");
				printFailedToGenException("Player 1", from, to);
				failCount++;
			} catch (Exception e) {
				assert (e instanceof IllegalMove);
			}
		}

		// Let's test if a pawn can move two spaces on the board - player 2
		x = genRandom(0, 7);
		from = new ChessPosition(x, 6);
		to = new ChessPosition(x, 4);
		p = b.getPieceAt(from);
		oldDestPiece = b.getPieceAt(to);
		if (p != null) {
			try {
				p.moveTo(to);
				//Check if pawn has actually been moved
				failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 1", failCount);
			} catch (IllegalMove e) {
				System.out
						.println("Player 2 - Trying to move a pawn two spaces up");
				printInvalidExceptionGen("Player 2", from, to);
				failCount++;
			}
		}

		// Let's try to move it again by two spaces, this should throw an
		// exception - player 2
		from = new ChessPosition(x, 4);
		to = new ChessPosition(x, 2);
		p = b.getPieceAt(from);
		if (p != null) {
			try {
				p.moveTo(to);
				System.out
						.println("Player 2 - Trying to move the pawn 2 spaces up multiple times");
				printFailedToGenException("Player 2", from, to);
				failCount++;
			} catch (Exception e) {
				assert (e instanceof IllegalMove);
			}
		}
		printFooter(funcName, failCount);
	}

	@Test
	public void testPawnTwoSquaresMoveAfterSingleSquareMove() {
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn can move one space on the board - player 1
		int x = genRandom(0, 7);
		ChessPosition from = new ChessPosition(x, 1);
		ChessPosition to = new ChessPosition(x, 2);
		ChessPiece p = b.getPieceAt(from);
		ChessPiece oldDestPiece = b.getPieceAt(to);
		if (p != null) {
			try {
				p.moveTo(to);
				failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 1", failCount);
			} catch (IllegalMove e) {
				System.out
						.println("Player 1 - Trying to move a pawn two spaces up");
				printInvalidExceptionGen("Player 1", from, to);
				failCount++;
			}
		}

		// Let's try to move it by two spaces now, this should throw an
		// exception - player 1
		from = new ChessPosition(x, 2);
		to = new ChessPosition(x, 4);
		p = b.getPieceAt(from);
		if (p != null) {
			try {
				p.moveTo(to);
				System.out
						.println("Player 1 - Trying to move the pawn 2 spaces up multiple times");
				printFailedToGenException("Player 1", from, to);
				failCount++;
			} catch (Exception e) {
				assert (e instanceof IllegalMove);
			}
		}

		// Let's test if a pawn can move one space on the board - player 2
		x = genRandom(0, 7);
		from = new ChessPosition(x, 6);
		to = new ChessPosition(x, 5);
		p = b.getPieceAt(from);
		oldDestPiece = b.getPieceAt(to);
		if (p != null) {
			try {
				p.moveTo(to);
				failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 1", failCount);
			} catch (IllegalMove e) {
				System.out
						.println("Player 2 - Trying to move a pawn two spaces up");
				printInvalidExceptionGen("Player 2", from, to);
				failCount++;
			}
		}

		// Let's try to move it by two spaces, this should throw an exception -
		// player 2
		from = new ChessPosition(x, 5);
		to = new ChessPosition(x, 3);
		p = b.getPieceAt(from);
		if (p != null) {
			try {
				p.moveTo(to);
				System.out
						.println("Player 2 - Trying to move the pawn 2 spaces up multiple times");
				printFailedToGenException("Player 2", from, to);
				failCount++;
			} catch (Exception e) {
				assert (e instanceof IllegalMove);
			}
		}
		printFooter(funcName, failCount);
	}

	@Test
	public void testPawnCapturingSamePlayerPiece() {
		String funcName = new Object() { }.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn can capture it's own piece - player 1
		// move (0,1) -> (0,2) , move (1,1) -> (1,3) and move (0,2) -> (1,3)
		// (exception)
		ChessPosition pos1 = new ChessPosition(0, 1);
		ChessPosition pos2 = new ChessPosition(0, 2);
		ChessPosition pos3 = new ChessPosition(1, 1);
		ChessPosition pos4 = new ChessPosition(1, 3);
		try {
			b.getPieceAt(pos1).moveTo(pos2);
			b.getPieceAt(pos3).moveTo(pos4);
		} catch (IllegalMove e) {
			System.out
					.println("Player 1 - Trying to move the pawn one/two spaces");
			printInvalidExceptionGen("Player 1", pos1, pos2);
			printInvalidExceptionGen("Player 1", pos3, pos4);
			failCount++;
		}

		ChessPosition pos5 = new ChessPosition(0, 2);
		ChessPosition pos6 = new ChessPosition(1, 3);
		try {
			b.getPieceAt(pos5).moveTo(pos6);
			System.out
					.println("Player 1 - Trying to capture Pawn of the same player");
			printFailedToGenException("Player 1", pos5, pos6);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}

		printFooter(funcName, failCount);

	}

	@Test
	public void testPawnBlockedMove() {
		String funcName = new Object() { }.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn honors the blockade
		// move (0,1) -> (0,3) , move (0,6) -> (0,4) and move (0,3) -> (0,4)
		// (exception)
		ChessPosition pos1 = new ChessPosition(0, 1);
		ChessPosition pos2 = new ChessPosition(0, 3);
		ChessPosition pos3 = new ChessPosition(0, 6);
		ChessPosition pos4 = new ChessPosition(0, 4);
		try {
			b.getPieceAt(pos1).moveTo(pos2);
			b.getPieceAt(pos3).moveTo(pos4);
		} catch (IllegalMove e) {
			System.out
					.println("Player 1/2 - Trying to move the pawn two spaces");
			printInvalidExceptionGen("Player 1", pos1, pos2);
			printInvalidExceptionGen("Player 2", pos3, pos4);
			failCount++;
		}

		ChessPosition pos5 = new ChessPosition(0, 3);
		ChessPosition pos6 = new ChessPosition(0, 4);
		try {
			b.getPieceAt(pos5).moveTo(pos6);
			System.out
					.println("Player 1 - Trying to move a pawn which is blocked ahead");
			printFailedToGenException("Player 1", pos5, pos6);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}

	@Test
	public void testPawnCapture() {
		String funcName = new Object() { }.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn honors the blockade
		// move (0,1) -> (0,3) , move (1,6) -> (1,4) and move (0,3) -> (1,4) ->
		// valid capture
		ChessPosition pos1 = new ChessPosition(0, 1);
		ChessPosition pos2 = new ChessPosition(0, 3);
		ChessPosition pos3 = new ChessPosition(1, 6);
		ChessPosition pos4 = new ChessPosition(1, 4);
		try {
			b.getPieceAt(pos1).moveTo(pos2);
			b.getPieceAt(pos3).moveTo(pos4);
		} catch (IllegalMove e) {
			System.out
					.println("Player 1/2 - Trying to move the pawn two spaces");
			printInvalidExceptionGen("Player 1", pos1, pos2);
			printInvalidExceptionGen("Player 2", pos3, pos4);
			failCount++;
		}

		ChessPosition pos5 = new ChessPosition(0, 3);
		ChessPosition pos6 = new ChessPosition(1, 4);
		ChessPiece p = b.getPieceAt(pos5);
		ChessPiece oldDestPiece = b.getPieceAt(pos6);
		try {
			b.getPieceAt(pos5).moveTo(pos6);
			failCount = checkPieceMoved(pos5, pos6, p, oldDestPiece,"Pawn", "Player 1", failCount);
		} catch (IllegalMove e) {
			System.out.println("Player 1 - Trying to capture an opponent pawn");
			printInvalidExceptionGen("Player 1", pos5, pos6);
			failCount++;
		}
		printFooter(funcName, failCount);
	}

	@Test
	public void testIfPawnCanMoveBackwards() {
		String funcName = new Object() { }.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn honors the blockade
		// move (0,1) -> (0,3) , move (1,6) -> (1,4) and move (0,3) -> (1,4) ->
		// valid capture
		ChessPosition pos1 = new ChessPosition(0, 1);
		ChessPosition pos2 = new ChessPosition(0, 3);
		ChessPosition pos3 = new ChessPosition(1, 6);
		ChessPosition pos4 = new ChessPosition(1, 4);
		try {
			b.getPieceAt(pos1).moveTo(pos2);
			b.getPieceAt(pos3).moveTo(pos4);
		} catch (IllegalMove e) {
			System.out
					.println("Player 1/2 - Trying to move the pawn two spaces");
			printInvalidExceptionGen("Player 1", pos1, pos2);
			printInvalidExceptionGen("Player 2", pos3, pos4);
			failCount++;
		}

		ChessPosition pos5 = new ChessPosition(0, 3);
		ChessPosition pos6 = new ChessPosition(0, 2);
		try {
			b.getPieceAt(pos5).moveTo(pos6);
			System.out.println("Player 1 - Trying to move the pawn backwards");
			printFailedToGenException("Player 1", pos5, pos6);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}

		ChessPosition pos7 = new ChessPosition(1, 4);
		ChessPosition pos8 = new ChessPosition(1, 5);
		try {
			b.getPieceAt(pos7).moveTo(pos8);
			System.out.println("Player 2 - Trying to move the pawn backwards");
			printFailedToGenException("Player 2", pos7, pos8);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}

	//King Tests
	@Test
	public void testKingMovedToSamePos(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test moving King to same position -> Player 1 and Player 2
		ChessPosition pos1 = new ChessPosition(4, 0);
		ChessPosition pos2 = new ChessPosition(4, 7);
		ChessPiece p1 = b.getPieceAt(pos1);
		ChessPiece p2 = b.getPieceAt(pos2);
		try {
			p1.moveTo(pos1);
			System.out.println("Player 1 - Trying to move the king to same square");
			printFailedToGenException("Player 1", pos1, pos1);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		
		try {
			p2.moveTo(pos2);
			System.out.println("Player 2 - Trying to move the king to same square");
			printFailedToGenException("Player 2", pos2, pos2);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testKingCapturingSamePlayerPiece(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test King capturing same player piece -> Player 1 and Player 2
		ChessPosition pos1 = new ChessPosition(4, 0);
		ChessPosition pos2 = new ChessPosition(4, 7);
		ChessPosition pos3 = new ChessPosition(4, 1);
		ChessPosition pos4 = new ChessPosition(4, 6);
		ChessPiece p1 = b.getPieceAt(pos1);
		ChessPiece p2 = b.getPieceAt(pos2);
		try {
			p1.moveTo(pos3);
			System.out.println("Player 1 - King tries to capture Pawn of the same player");
			printFailedToGenException("Player 1", pos1, pos3);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		
		try {
			p2.moveTo(pos4);
			System.out.println("Player 2 - King tries to capture Pawn of the same player");
			printFailedToGenException("Player 2", pos2, pos4);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testKingMoveAndCapture(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's move the King and capture opponent King
		// For visualization see -> http://imgur.com/zNpSXaD
		//Get pawn out of the way
		try {
			b.getPieceAt(new ChessPosition(4,1)).moveTo(new ChessPosition(4,2));
		} catch (IllegalMove e1) {
		}
		
		int x[] = {4, 4, 3, 4, 4, 4, 4, 4};
		int y[] = {0, 1, 2, 3, 4, 5, 6, 7};
		for(int i = 1; i < x.length ; i++){
			ChessPosition from = new ChessPosition(x[i-1], y[i-1]);
			ChessPosition to = new ChessPosition(x[i], y[i]);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			try {
				p.moveTo(to);
				failCount = checkPieceMoved(from, to, p, oldDestPiece,"King", "Player 1", failCount);
			} catch (IllegalMove e) {
				System.out.println("Player 1 - Trying to capture opponent king with valid moves but encountered exception");
				printInvalidExceptionGen("Player 1", from, to);
				failCount++;
				break;
			}
		}
		printFooter(funcName, failCount);
	}
	
	//Knight Tests
	@Test
	public void testKnightMovedToSamePos(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test moving Knight to same position -> Player 1 and Player 2
		ChessPosition pos1 = new ChessPosition(6, 0);
		ChessPosition pos2 = new ChessPosition(1, 7);
		ChessPiece p1 = b.getPieceAt(pos1);
		ChessPiece p2 = b.getPieceAt(pos2);
		try {
			p1.moveTo(pos1);
			System.out.println("Player 1 - Trying to move the knight to same square");
			printFailedToGenException("Player 1", pos1, pos1);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		
		try {
			p2.moveTo(pos2);
			System.out.println("Player 2 - Trying to move the knight to same square");
			printFailedToGenException("Player 2", pos2, pos2);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testKnightCapturingSamePlayerPiece(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test knight capturing same player piece -> Player 1 and Player 2
		ChessPosition pos1 = new ChessPosition(6, 0);
		ChessPosition pos2 = new ChessPosition(1, 7);
		ChessPosition pos3 = new ChessPosition(4, 1);
		ChessPosition pos4 = new ChessPosition(3, 6);
		ChessPiece p1 = b.getPieceAt(pos1);
		ChessPiece p2 = b.getPieceAt(pos2);
		try {
			p1.moveTo(pos3);
			System.out.println("Player 1 - Knight tries to capture Pawn of the same player");
			printFailedToGenException("Player 1", pos1, pos3);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		
		try {
			p2.moveTo(pos4);
			System.out.println("Player 2 - Knight tries to capture Pawn of the same player");
			printFailedToGenException("Player 2", pos2, pos4);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testKnightMoveAndCapture(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		//Let's move the Player 1 knight in front of Player 2's pawn ahead of Player 2's King and try all board pos
		//Visualization -> http://imgur.com/8Fw857V
		int x[] = {6, 5, 3, 4};
		int y[] = {0, 2, 3, 5}; 
		for(int i = 1; i < x.length; i++){
			ChessPosition from = new ChessPosition(x[i-1], y[i-1]);
			ChessPosition to = new ChessPosition(x[i], y[i]);
			ChessPiece p = b.getPieceAt(from);
			try {
				p.moveTo(to);
			} catch (IllegalMove e) {
				System.out.println("Player 1 - Trying to move Knight with valid moves but encountered exception");
				printInvalidExceptionGen("Player 1", from, to);
				failCount++;
				printFooter(funcName, failCount);
				return;
			}
		}	
		
		//Build valid move list
		ChessPosition pos = new ChessPosition(4, 5);
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
		
		//From this point, try all board positions, only 8 should be valid, also check opponent piece capture
		ChessPiece p = b.getPieceAt(pos);
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				ChessPosition to = new ChessPosition(i, j);
				ChessPiece oldDestPiece = b.getPieceAt(to);
				if(validMoves.contains(to)){
					try {
						p.moveTo(to);
						failCount = checkPieceMoved(pos, to, p, oldDestPiece,"Knight", "Player 1", failCount);
						//Move it back to pos for trying next position
						b.getPieceAt(to).moveTo(pos); //hopefully this won't be bugged
					} catch (IllegalMove e) {
						System.out.println("Player 1 - Knight tries to move to a valid position");
						printInvalidExceptionGen("Player 1", pos, to);
						failCount++;
					}
				}else{
					try {
						p.moveTo(to);
						System.out.println("Player 1 - Knight tries to move to an invalid position");
						printFailedToGenException("Player 1", pos, to);
						failCount++;
					} catch (Exception e) {
						assert (e instanceof IllegalMove);
					}
				}
			}
		}
		printFooter(funcName, failCount);
	}
	
	//Rook Tests
	@Test
	public void testRookMovedToSamePos(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test moving Rook to same position -> Player 1 and Player 2
		ChessPosition pos1 = new ChessPosition(0, 0);
		ChessPosition pos2 = new ChessPosition(7, 7);
		ChessPiece p1 = b.getPieceAt(pos1);
		ChessPiece p2 = b.getPieceAt(pos2);
		try {
			p1.moveTo(pos1);
			System.out.println("Player 1 - Trying to move the rook to same square");
			printFailedToGenException("Player 1", pos1, pos1);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		
		try {
			p2.moveTo(pos2);
			System.out.println("Player 2 - Trying to move the rook to same square");
			printFailedToGenException("Player 2", pos2, pos2);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testRookCapturingSamePlayerPiece(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test capturing same player piece with Rook -> Player 1 and Player 2
		ChessPosition pos1 = new ChessPosition(0, 0);
		ChessPosition pos2 = new ChessPosition(7, 7);
		ChessPosition pos3 = new ChessPosition(0, 1);
		ChessPosition pos4 = new ChessPosition(7, 6);
		ChessPiece p1 = b.getPieceAt(pos1);
		ChessPiece p2 = b.getPieceAt(pos2);
		try {
			p1.moveTo(pos3);
			System.out.println("Player 1 - Rook tries to capture Pawn of the same player");
			printFailedToGenException("Player 1", pos1, pos3);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		
		try {
			p2.moveTo(pos4);
			System.out.println("Player 2 - Rook tries to capture Pawn of the same player");
			printFailedToGenException("Player 2", pos2, pos4);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testRookMoveAndCapture(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		//Let's move the Rook around the board as per visualization and see if everything works
		//Visualization -> http://imgur.com/MkDNXmx
		//below should be read in pairs move (x[1], y[1]) -> (x[2], y[2]) , index += 2
		int x[] = {0, 0, 0, 0, 0, 4, 1, 1, 0, 0, 0, 3, 3, 3, 3, 3, 3, 4, 4, 1};
		int y[] = {1, 3, 0, 2, 2, 6, 1, 3, 2, 6, 2, 2, 2, 6, 6, 7, 7, 7, 7, 7}; 
		int valid[] = {1, 1, 0, 1, 0, 1, 1, 1, 1, 0};
		int k = 0;
		for(int i = 0; i < x.length; i += 2, k++){
			ChessPosition from = new ChessPosition(x[i], y[i]);
			ChessPosition to = new ChessPosition(x[i+1], y[i+1]);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			if(valid[k] == 1){
				try {
					p.moveTo(to);
					failCount = checkPieceMoved(from, to, p, oldDestPiece,"Rook", "Player 1", failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 1 - Rook/Pawn tried to move to a valid position");
					printInvalidExceptionGen("Player 1", from, to);
					failCount++;
				}
			}else{
				try {
					p.moveTo(to);
					System.out.println("Player 1 - Rook/Pawn tried to move to an invalid position");
					printFailedToGenException("Player 1", from, to);
					failCount++;
				} catch (Exception e) {
					assert (e instanceof IllegalMove);
				}
			}
		}	
		printFooter(funcName, failCount);
	}
	
	//Bishop Tests
	@Test
	public void testBishopMovedToSamePos(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test moving Bishop to same position -> Player 1 and Player 2
		ChessPosition pos1 = new ChessPosition(5, 0);
		ChessPosition pos2 = new ChessPosition(2, 7);
		ChessPiece p1 = b.getPieceAt(pos1);
		ChessPiece p2 = b.getPieceAt(pos2);
		try {
			p1.moveTo(pos1);
			System.out.println("Player 1 - Trying to move bishop to same square");
			printFailedToGenException("Player 1", pos1, pos1);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		
		try {
			p2.moveTo(pos2);
			System.out.println("Player 2 - Trying to move bishop to same square");
			printFailedToGenException("Player 2", pos2, pos2);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testBishopCapturingSamePlayerPiece(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test capturing same player piece with Bishop -> Player 1 and Player 2
		ChessPosition pos1 = new ChessPosition(5, 0);
		ChessPosition pos2 = new ChessPosition(2, 7);
		ChessPosition pos3 = new ChessPosition(4, 1);
		ChessPosition pos4 = new ChessPosition(3, 6);
		ChessPiece p1 = b.getPieceAt(pos1);
		ChessPiece p2 = b.getPieceAt(pos2);
		try {
			p1.moveTo(pos3);
			System.out.println("Player 1 - Bishop tries to capture Pawn of the same player");
			printFailedToGenException("Player 1", pos1, pos3);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		
		try {
			p2.moveTo(pos4);
			System.out.println("Player 2 - Bishop tries to capture Pawn of the same player");
			printFailedToGenException("Player 2", pos2, pos4);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testBishopMoveAndCapture(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		//Let's move the Bishop around the board as per visualization and see if everything works
		//Visualization -> http://imgur.com/w3HjP6l
		//below should be read in pairs move (x[1], y[1]) -> (x[2], y[2]) , index += 2
		int x[] = {4, 4, 3, 3, 5, 2, 2, 5, 2, 2, 2, 6, 2, 5, 5, 2, 2, 3, 5, 4};
		int y[] = {1, 3, 1, 3, 0, 3, 0, 3, 3, 6, 3, 7, 3, 6, 3, 6, 6, 7, 6, 7}; 
		int valid[] = {1, 1, 1, 1, 0, 0, 1, 1, 1, 1};
		int k = 0;
		for(int i = 0; i < x.length; i += 2, k++){
			ChessPosition from = new ChessPosition(x[i], y[i]);
			ChessPosition to = new ChessPosition(x[i+1], y[i+1]);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			if(valid[k] == 1){
				try {
					p.moveTo(to);
					failCount = checkPieceMoved(from, to, p, oldDestPiece,"Bishop", "Player 1", failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 1 - Bishop/Pawn tried to move to a valid position");
					printInvalidExceptionGen("Player 1", from, to);
					failCount++;
				}
			}else{
				try {
					p.moveTo(to);
					System.out.println("Player 1 - Bishop/Pawn tried to move to an invalid position");
					printFailedToGenException("Player 1", from, to);
					failCount++;
				} catch (Exception e) {
					assert (e instanceof IllegalMove);
				}
			}
		}	
		printFooter(funcName, failCount);
	}
	
	//Queen Tests
	@Test
	public void testQueenMovedToSamePos(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test moving Queen to same position -> Player 1 and Player 2
		ChessPosition pos1 = new ChessPosition(3, 0);
		ChessPosition pos2 = new ChessPosition(3, 7);
		ChessPiece p1 = b.getPieceAt(pos1);
		ChessPiece p2 = b.getPieceAt(pos2);
		try {
			p1.moveTo(pos1);
			System.out.println("Player 1 - Trying to move the queen to same square");
			printFailedToGenException("Player 1", pos1, pos1);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		
		try {
			p2.moveTo(pos2);
			System.out.println("Player 2 - Trying to move the queen to same square");
			printFailedToGenException("Player 2", pos2, pos2);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testQueenCapturingSamePlayerPiece(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test capturing same player piece with Queen -> Player 1 and Player 2
		ChessPosition pos1 = new ChessPosition(3, 0);
		ChessPosition pos2 = new ChessPosition(3, 7);
		ChessPosition pos3 = new ChessPosition(3, 1);
		ChessPosition pos4 = new ChessPosition(3, 6);
		ChessPiece p1 = b.getPieceAt(pos1);
		ChessPiece p2 = b.getPieceAt(pos2);
		try {
			p1.moveTo(pos3);
			System.out.println("Player 1 - Queen tries to capture Pawn of the same player");
			printFailedToGenException("Player 1", pos1, pos3);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		
		try {
			p2.moveTo(pos4);
			System.out.println("Player 2 - Queen tries to capture Pawn of the same player");
			printFailedToGenException("Player 2", pos2, pos4);
			failCount++;
		} catch (Exception e) {
			assert (e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testQueenMoveAndCapture(){
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		//Let's move the Queen around the board as per visualization and see if everything works
		//Visualization -> http://imgur.com/wnYYepj
		//below should be read in pairs move (x[1], y[1]) -> (x[2], y[2]) , index += 2
		int x[] = {4, 4, 3, 4, 4, 4, 4, 2, 2, 6, 2, 2, 2, 3, 3, 4};
		int y[] = {1, 3, 0, 1, 1, 7, 1, 3, 3, 7, 3, 6, 6, 7, 7, 7}; 
		int valid[] = {1, 1, 0, 1, 0, 1, 1, 1};
		int k = 0;
		for(int i = 0; i < x.length; i += 2, k++){
			ChessPosition from = new ChessPosition(x[i], y[i]);
			ChessPosition to = new ChessPosition(x[i+1], y[i+1]);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			if(valid[k] == 1){
				try {
					p.moveTo(to);
					failCount = checkPieceMoved(from, to, p, oldDestPiece,"Queen", "Player 1", failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 1 - Queen/Pawn tried to move to a valid position");
					printInvalidExceptionGen("Player 1", from, to);
					failCount++;
				}
			}else{
				try {
					p.moveTo(to);
					System.out.println("Player 1 - Queen/Pawn tried to move to an invalid position");
					printFailedToGenException("Player 1", from, to);
					failCount++;
				} catch (Exception e) {
					assert (e instanceof IllegalMove);
				}
			}
		}	
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testGameOfTheCentury() {
		//Game of the Century Wiki Link -> http://en.wikipedia.org/wiki/The_Game_of_the_Century_(chess)
		//Played between Donald Byrne and 13-year-old Bobby Fischer in 1956
		//Youtube link -> http://www.youtube.com/watch?v=M624T3PTggU
		
		String funcName = new Object() {}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		Scanner scanner = new Scanner(getClass().getResourceAsStream("game_of_the_century.txt"));
		String line = new String();
		while(scanner.hasNextLine()){
			line = scanner.nextLine();
			if(line.contains("Castling") || line.contains("Sacrifice")){
				continue;
			}
			line = line.trim();
			if(line.isEmpty()){
				continue;
			}
			String []splitStr = line.split(" ");
			int []intArr = new int[splitStr.length];
			if(splitStr.length != 4){
				fail("Bad input data, please check game_of_the_century.txt file");
			}
			for(int i = 0; i < splitStr.length; i++){
				intArr[i] = Integer.parseInt(splitStr[i]);
			}
			ChessPosition from = new ChessPosition(intArr[0], intArr[1]);
			ChessPosition to = new ChessPosition(intArr[2], intArr[3]);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			try {
				p.moveTo(to);
				failCount = checkPieceMoved(from, to, p, oldDestPiece,Character.toString(p.mark), p.getOwner().toString(), failCount);
			} catch (IllegalMove e) {
				System.out.println("===========================");
				System.out.println("Bobby Fisher (Player 2)");
				System.out.println("===========================");
				System.out.print(b.toString());
				System.out.println("===========================");
				System.out.println("Donald Byrne (Player 1)");
				System.out.println("===========================\n");
				System.out.println(p.getOwner().toString()+" - "+Character.toString(p.mark)+" tried to move to a valid position");
				printInvalidExceptionGen(p.getOwner().toString(), from, to);
				failCount++;
			}
			if(failCount > 0){
				fail("Failed a legal move, please check the console for more details on the error");
			}
		}
		// Print out the final board
		System.out.println("===========================");
		System.out.println("Bobby Fisher (Player 2)");
		System.out.println("===========================");
		System.out.print(b.toString());
		System.out.println("===========================");
		System.out.println("Donald Byrne (Player 1)");
		System.out.println("===========================\n");
		printFooter(funcName, failCount);

	}
		
	public void printHeader(String funcName) {
		System.out.println("====================================================");
		System.out.println(funcName + " debug log");
		System.out.println("====================================================\n");
	}

	public void printFooter(String funcName, int failCount) {
		if (failCount > 0) {
			System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
			fail(funcName + " failed, moves failed = " + failCount);
		} else {
			System.out.println(funcName + " passed");
		}
		// System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
	}

	public int checkPieceMoved(ChessPosition from, ChessPosition to,
			ChessPiece movePiece, ChessPiece oldDestPiece, String pieceName,
			String player, int failCount) {
		ChessPiece p1 = b.getPieceAt(from);
		ChessPiece p2 = b.getPieceAt(to);
		int startX = from.getX();
		int startY = from.getY();
		int endX = to.getX();
		int endY = to.getY();

		// Checks
		// 1 -> "from" is empty
		// 2 -> "to" is not empty
		// 3 -> ChessPiece at "to" is the piece we wanted to move
		// 4 -> the ChessPiece that was previously at "to" (destination) is not
		// on the board (or removed from board)
		if (p1 == null && p2 != null && movePiece == p2
				&& !findPiece(oldDestPiece)) {
			// don't do anything, all checks succeeded
		} else {
			System.out.println(player + " - Move (" + startX + ", " + startY
					+ ") -> (" + endX + ", " + endY + ")");
			System.out.println(player + " - " + pieceName
					+ " was either not removed from (" + startX + ", " + startY
					+ ") OR not placed at (" + endX + ", " + endY + ")\n");
			failCount++;
		}
		return failCount;
	}

	public boolean findPiece(ChessPiece p1) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				ChessPosition position = new ChessPosition(i, j);
				ChessPiece p2 = b.getPieceAt(position);
				if (p2 != null && p1 == p2) {
					return true;
				}
			}
		}
		return false;
	}

	public void printFailedToGenException(String player, ChessPosition from,
			ChessPosition to) {
		int startX = from.getX();
		int startY = from.getY();
		int endX = to.getX();
		int endY = to.getY();
		System.out.println(player + " - Move (" + startX + ", " + startY
				+ ") -> (" + endX + ", " + endY
				+ ") failed to generate an Illegal Move exception\n");
	}

	public void printInvalidExceptionGen(String player, ChessPosition from,
			ChessPosition to) {
		int startX = from.getX();
		int startY = from.getY();
		int endX = to.getX();
		int endY = to.getY();
		System.out.println(player + " - Move (" + startX + ", " + startY
				+ ") -> (" + endX + ", " + endY
				+ ") should not generate an Illegal Move exception\n");
	}

	public int genRandom(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}
}
