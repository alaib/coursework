package a5;

import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out.println("Player 1 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") failed to generate an Illegal Move exception");
					failCount++;
				} catch (Exception e) {
					assert(e instanceof IllegalMove);
				}
			}
		}
		//Change startY and endY
		startY = 7; endY = 6;
		/* Let's try to move all chess pieces in 1st row of player 2, everything throws an exception */
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out.println("Player 2 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") failed to generate an Illegal Move exception");
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
		int startY = 1, endY = 3, i;
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, "Pawn", "Player 1", i, startY, endY, failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 1 - Trying to move a pawn 2 spaces up for the first time");
					System.out.println("Player 1 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") should not generate Illegal Move exception\n");
					failCount++;
				}
			}
		}
		
		/* Let's test if each pawn moves two spaces on first move - player 2*/
		startY = 6; endY = 4;
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, "Pawn", "Player 2", i, startY, endY, failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 2 - Trying to move a pawn 2 spaces up for the first time");
					System.out.println("Player 2 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") should not generate Illegal Move exception\n");
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
		int startY = 1, endY = 3, i;
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, "Pawn", "Player 1", i, startY, endY, failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 1 - Trying to move a pawn 2 spaces up for the first time");
					System.out.println("Player 1 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") should not generate Illegal Move exception\n");
					failCount++;
				}
			}
		}
		
		/* Let's try to move those pawns again 2 spaces up, everything should throw an exception - player 1*/
		startY = 3; endY = 5;
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out.println("Player 1 - Trying to move the pawn 2 spaces up multiple times");
					System.out.println("Player 1 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") failed to generate an Illegal Move exception\n");
					failCount++;
				} catch (Exception e) {
					assert(e instanceof IllegalMove);
				}
			}
		}
		
		/* Let's test if each pawn moves two spaces on first move - player 2*/
		startY = 6; endY = 4;
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, "Pawn", "Player 2", i, startY, endY, failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 2 - Trying to move a pawn 2 spaces up for the first time");
					System.out.println("Player 2 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") should not generate Illegal Move exception\n");
					failCount++;
				}
			}
		}
		
		/* Let's try to move those pawns again 2 spaces up, everything should throw an exception - player 2*/
		startY = 4; endY = 2;
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out.println("Player 2 - Trying to move the pawn 2 spaces up multiple times");
					System.out.println("Player 2 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") failed to generate an Illegal Move exception\n");
					failCount++;
				} catch (Exception e) {
					assert(e instanceof IllegalMove);
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
		int startY = 1, endY = 2, i;
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, "Pawn", "Player 1", i, startY, endY, failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 1 - Trying to move a pawn 1 space up for the first time");
					System.out.println("Player 1 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") should not generate Illegal Move exception\n");
					failCount++;
				}
			}
		}
		
		/* Let's try to move each pawn 2 spaces up after it has moved from initial square - player 1*/
		/* everything should thrown an exception */
		startY = 2; endY = 4;
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out.println("Player 1 - Trying to move the pawn 2 spaces up after initial move");
					System.out.println("Player 1 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") failed to generate an Illegal Move exception\n");
					failCount++;
				} catch (Exception e) {
					assert(e instanceof IllegalMove);
				}
			}
		}
		
		/* Let's test if each pawn moves one space on first move - player 2*/
		startY = 6; endY = 5;
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					//Check if pawn has actually been moved
					failCount = checkPieceMoved(from, to, "Pawn", "Player 2", i, startY, endY, failCount);
				} catch (IllegalMove e) {
					System.out.println("Player 2 - Trying to move a pawn 1 space up for the first time");
					System.out.println("Player 2 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") should not generate Illegal Move exception\n");
					failCount++;
				}
			}
		}
		
		/* Let's try to move each pawn 2 spaces up after it has moved from initial square - player 2*/
		/* everything should thrown an exception */
		startY = 5; endY = 3;
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
					System.out.println("Player 2 - Trying to move the pawn 2 spaces up after initial move");
					System.out.println("Player 2 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") failed to generate an Illegal Move exception\n");
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
		int startY = 1, endY = 2, i;
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
				} catch (IllegalMove e) {
					System.out.println("Player 1 - Trying to move a pawn 1 space up for the first time");
					System.out.println("Player 1 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") should not generate Illegal Move exception\n");
					failCount++;
				}
			}
		}
		
		/* Let's test if each pawn moves two spaces on first move - player 2*/
		startY = 6; endY = 5;
		for(i = 0; i < 7; i++){
			ChessPosition from = new ChessPosition(i, startY);
			ChessPosition to = new ChessPosition(i, endY);
			ChessPiece p = b.getPieceAt(from);
			if (p != null) {
				try {
					p.moveTo(to);
				} catch (IllegalMove e) {
					System.out.println("Player 2 - Trying to move a pawn 1 space up for the first time");
					System.out.println("Player 2 - Move ("+i+", "+startY+") -> ("+i+", "+endY+") should not generate Illegal Move exception\n");
					failCount++;
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

	public int checkPieceMoved(ChessPosition from, ChessPosition to, String piece, String player, int i, int startY, int endY, int failCount){
		ChessPiece p1 = b.getPieceAt(from);
		ChessPiece p2 = b.getPieceAt(to);
		if(p1 == null && p2 != null && p2.equals(p1)){
			//don't do anything, all checks succeeded
		}else{
			System.out.println(player+" - Move ("+i+", "+startY+") -> ("+i+", "+endY+")");
			System.out.println(player+" - "+piece+" was either not removed from ("+i+", "+startY+") OR not placed at ("+i+", "+endY+")\n");
			failCount++;
		}
		return failCount;
	}
}
