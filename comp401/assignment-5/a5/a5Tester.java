package a5;

import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class a5Tester {
	public ChessPlayer p1, p2;
	public ChessGame g;
	public ChessBoard b;
	
	@BeforeClass
	public static void runOnceBeforeAllTests(){
	}
	
	@Before
	public void runBeforeEachTest(){
		p1 = new ChessPlayer("P1");
		p2 = new ChessPlayer("P2");
		
		g = new ChessGame(p1, p2);
		b = g.getBoard();
	}
	
	@Test
	public void testFirstRowMovesOnInitialBoard(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		/* Let's try to move all chess pieces in 1st row of player 1, everything throws an exception */
		int startY = 0, endY = 1, i;
		int failCount = 0;
		for(i = 0; i < 8; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out.println("Player 1 - Trying to move 1st row pieces (there is a block)");
					printFailedToGenException("Player 1", from, to);
					failCount++;
				} catch (Exception e) {
					assert(e instanceof IllegalMove);
				}
			}
		}
		//Change startY and endY
		startY = 7; endY = 6;
		/* Let's try to move all chess pieces in 1st row of player 2, everything throws an exception */
		for(i = 0; i < 8; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out.println("Player 2 - Trying to move 1st row pieces (there is a block)");
					printFailedToGenException("Player 2", from, to);
					failCount++;
				} catch (Exception e) {
					assert(e instanceof IllegalMove);
				}
			}
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testPawnFirstMoveWithTwoSpaces(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		
		/* Let's test if each pawn moves two spaces on first move - player 1*/
		int startY = 1, endY = 3, i, startX, endX;
		for(i = 0; i < 8; i++){
			startX = endX = i;
			ChessPosition from = new ChessPosition(startX, startY);
			ChessPosition to = new ChessPosition(endX, endY);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 1", failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 1 - Trying to move a pawn 2 spaces up for the first time");
					printInvalidExceptionGen("Player 1", from, to);
					failCount++;
				}
			}
		}
		
		/* Let's test if each pawn moves two spaces on first move - player 2*/
		startY = 6; endY = 4;
		for(i = 0; i < 8; i++){
			startX = endX = i;
			ChessPosition from = new ChessPosition(startX, startY);
			ChessPosition to = new ChessPosition(endX, endY);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 2", failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 2 - Trying to move a pawn 2 spaces up for the first time");
					printInvalidExceptionGen("Player 2", from, to);
					failCount++;
				}
			}
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testPawnTwoSpacesMoveMultipleTimes(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		
		/* Let's test if each pawn moves two spaces on first move - player 1*/
		int startY = 1, endY = 3, i, startX, endX;
		for(i = 0; i < 8; i++){
			startX = endX = i;
			ChessPosition from = new ChessPosition(startX, startY);
			ChessPosition to = new ChessPosition(endX, endY);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 1", failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 1 - Trying to move a pawn 2 spaces up for the first time");
					printInvalidExceptionGen("Player 1", from, to);
					failCount++;
				}
			}
		}
		
		/* Let's try to move those pawns again 2 spaces up, everything should throw an exception - player 1*/
		startY = 3; endY = 5;
		for(i = 0; i < 8; i++){
			startX = endX = i;
			ChessPosition from = new ChessPosition(startX, startY);
			ChessPosition to = new ChessPosition(endX, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out.println("Player 1 - Trying to move the pawn 2 spaces up multiple times");
					printFailedToGenException("Player 1", from, to);
					failCount++;
				} catch (Exception e) {
					assert(e instanceof IllegalMove);
				}
			}
		}
		
		/* Let's test if each pawn moves two spaces on first move - player 2*/
		startY = 6; endY = 4;
		for(i = 0; i < 8; i++){
			startX = endX = i;
			ChessPosition from = new ChessPosition(startX, startY);
			ChessPosition to = new ChessPosition(endX, endY);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 2", failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 2 - Trying to move a pawn 2 spaces up for the first time");
					printInvalidExceptionGen("Player 2", from, to);
					failCount++;
				}
			}
		}
		
		/* Let's try to move those pawns again 2 spaces up, everything should throw an exception - player 2*/
		startY = 4; endY = 2;
		for(i = 0; i < 8; i++){
			startX = endX = i;
			ChessPosition from = new ChessPosition(startX, startY);
			ChessPosition to = new ChessPosition(endX, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out.println("Player 2 - Trying to move the pawn 2 spaces up multiple times");
					printFailedToGenException("Player 2", from, to);
					failCount++;
				} catch (Exception e) {
					assert(e instanceof IllegalMove);
				}
			}
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testPawnFirstMoveWithOneSpace(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		
		/* Let's test if each pawn moves one space on first move - player 1*/
		int startY = 1, endY = 2, i, startX, endX;
		for(i = 0; i < 8; i++){
			startX = endX = i;
			ChessPosition from = new ChessPosition(startX, startY);
			ChessPosition to = new ChessPosition(endX, endY);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 1", failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 1 - Trying to move a pawn 1 space up for the first time");
					printInvalidExceptionGen("Player 1", from, to);
					failCount++;
				}
			}
		}
		
		/* Let's test if each pawn moves one space on first move - player 2*/
		startY = 6; endY = 5;
		for(i = 0; i < 8; i++){
			startX = endX = i;
			ChessPosition from = new ChessPosition(startX, startY);
			ChessPosition to = new ChessPosition(endX, endY);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 2", failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 2 - Trying to move a pawn 1 space up for the first time");
					printInvalidExceptionGen("Player 2", from, to);
					failCount++;
				}
			}
		}
	
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testPawnTwoSpacesMoveAfterInitialMove(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		
		/* Let's test if each pawn moves one space on first move - player 1*/
		int startY = 1, endY = 2, i, startX, endX;
		for(i = 0; i < 8; i++){
			startX = endX = i;
			ChessPosition from = new ChessPosition(startX, startY);
			ChessPosition to = new ChessPosition(endX, endY);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 1", failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 1 - Trying to move a pawn 1 space up for the first time");
					printInvalidExceptionGen("Player 1", from, to);
					failCount++;
				}
			}
		}
		
		/* Let's try to move each pawn 2 spaces up after it has moved from initial square - player 1*/
		/* everything should thrown an exception */
		startY = 2; endY = 4;
		for(i = 0; i < 8; i++){
			startX = endX = i;
			ChessPosition from = new ChessPosition(startX, startY);
			ChessPosition to = new ChessPosition(endX, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out.println("Player 1 - Trying to move the pawn 2 spaces up after initial move");
					printFailedToGenException("Player 1", from, to);
					failCount++;
				} catch (Exception e) {
					assert(e instanceof IllegalMove);
				}
			}
		}
		
		/* Let's test if each pawn moves one space on first move - player 2*/
		startY = 6; endY = 5;
		for(i = 0; i < 8; i++){
			startX = endX = i;
			ChessPosition from = new ChessPosition(startX, startY);
			ChessPosition to = new ChessPosition(endX, endY);
			ChessPiece p = b.getPieceAt(from);
			ChessPiece oldDestPiece = b.getPieceAt(to);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, p, oldDestPiece,"Pawn", "Player 2", failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 2 - Trying to move a pawn 1 space up for the first time");
					printInvalidExceptionGen("Player 2", from, to);
					failCount++;
				}
			}
		}
		
		/* Let's try to move each pawn 2 spaces up after it has moved from initial square - player 2*/
		/* everything should thrown an exception */
		startY = 5; endY = 3;
		for(i = 0; i < 8; i++){
			startX = endX = i;
			ChessPosition from = new ChessPosition(startX, startY);
			ChessPosition to = new ChessPosition(endX, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out.println("Player 2 - Trying to move the pawn 2 spaces up after initial move");
					printFailedToGenException("Player 2", from, to);
					failCount++;
				} catch (Exception e) {
					assert(e instanceof IllegalMove);
				}
			}
		}
		printFooter(funcName, failCount);
	}
	

	
	@Test
	public void testPawnNoMovesPossible(){
		/* Let's test if each pawn moves two spaces on first move - player*/
	}
	
	@Test
	public void templateFunc(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		/* Let's test if each pawn moves two spaces on first move - player*/
		printFooter(funcName, failCount);
	
	}
	
	@AfterClass
	public static void runOnceAfterAllTests(){
		
	}
	
	public void printHeader(String funcName){
		System.out.println("====================================================");
		System.out.println(funcName+" debug log");
		System.out.println("====================================================\n");
	}
	public void printFooter(String funcName, int failCount){
		if(failCount > 0){
			System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
			fail(funcName+" failed, moves failed = "+failCount);
		}else{
			System.out.println(funcName+" passed");
		}
//		System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
	}

	public int checkPieceMoved(ChessPosition from, ChessPosition to, ChessPiece movePiece, ChessPiece oldDestPiece,
							   String pieceName, String player, int failCount){
		ChessPiece p1 = b.getPieceAt(from);
		ChessPiece p2 = b.getPieceAt(to);
		int startX = from.getX();
		int startY = from.getY();
		int endX = to.getX();
		int endY = to.getY();
		
		//Checks
		//1 -> "from" is empty
		//2 -> "to" is not empty
		//3 -> ChessPiece at "to" is the piece we wanted to move
		//4 -> the ChessPiece that was previously at "to" (destination) is not on the board (or removed from board)
		if(p1 == null && p2 != null && movePiece == p2 && !findPiece(oldDestPiece)){
			//don't do anything, all checks succeeded
		}else{
			System.out.println(player+" - Move ("+startX+", "+startY+") -> ("+endX+", "+endY+")");
			System.out.println(player+" - "+pieceName+" was either not removed from ("+startX+", "+startY+") OR not placed at ("+endX+", "+endY+")\n");
			failCount++;
		}
		return failCount;
	}
	
	public boolean findPiece(ChessPiece p1){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				ChessPosition position = new ChessPosition(i, j);
				ChessPiece p2 = b.getPieceAt(position);
				if(p2 != null && p1 == p2){
					return true;
				}
			}
		}
		return false;
	}
	
	public void printFailedToGenException(String player, ChessPosition from, ChessPosition to){
		int startX = from.getX();
		int startY = from.getY();
		int endX = to.getX();
		int endY = to.getY();
		System.out.println(player+" - Move ("+startX+", "+startY+") -> ("+endX+", "+endY+") failed to generate an Illegal Move exception\n");
	}
	
	public void printInvalidExceptionGen(String player, ChessPosition from, ChessPosition to){
		int startX = from.getX();
		int startY = from.getY();
		int endX = to.getX();
		int endY = to.getY();
		System.out.println(player+" - Move ("+startX+", "+startY+") -> ("+endX+", "+endY+") should not generate an Illegal Move exception\n");
	}
}
