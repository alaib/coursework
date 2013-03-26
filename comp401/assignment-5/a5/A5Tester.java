package a5;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class A5Tester {
	public ChessPlayer p1, p2;
	public ChessGame g;
	public ChessBoard b;
	
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
	
	// Pawn test functions 
	@Test
	public void testPawnMovedToSamePos(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Move a pawn to the same position - player 1 
		int x = genRandom(0, 7);
		ChessPosition from = new ChessPosition(x, 1);
		ChessPosition to = new ChessPosition(x, 1);
		ChessPiece p = b.getPieceAt(from);
		if(p != null){
			try{
				p.moveTo(to);
				System.out.println("Player 1 - Trying to move the pawn to same square");
				printFailedToGenException("Player 1", from, to);
				failCount++;
			}catch(Exception e){
				assert(e instanceof IllegalMove);
			}
		}
		
		// Move a pawn to the same position - player 2 
		x = genRandom(0, 7);
		from = new ChessPosition(x, 6);
		to = new ChessPosition(x, 6);
		p = b.getPieceAt(from);
		if(p != null){
			try{
				p.moveTo(to);
				System.out.println("Player 2 - Trying to move the pawn to same square");
				printFailedToGenException("Player 2", from, to);
				failCount++;
			}catch(Exception e){
				assert(e instanceof IllegalMove);
			}
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testPawnOneSquareMove(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn can move one space on the board - player 1
		int x = genRandom(0, 7);
		ChessPosition from = new ChessPosition(x, 1);
		ChessPosition to = new ChessPosition(x, 2);
		ChessPiece p = b.getPieceAt(from);
		if(p != null){
			try{
				p.moveTo(to);
			}catch(IllegalMove e){
				System.out.println("Player 1 - Trying to move a pawn one spaces up");
				printInvalidExceptionGen("Player 1", from, to);
				failCount++;
			}
		}
		
		// Let's test if a pawn can move one space on the board - player 2
		x = genRandom(0, 7);
		from = new ChessPosition(x, 6);
		to = new ChessPosition(x, 5);
		p = b.getPieceAt(from);
		if(p != null){
			try{
				p.moveTo(to);
			}catch(IllegalMove e){
				System.out.println("Player 2 - Trying to move a pawn one spaces up");
				printInvalidExceptionGen("Player 2", from, to);
				failCount++;
			}
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testPawnTwoSquaresMove(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn can move two spaces on the board - player 1
		int x = genRandom(0, 7);
		ChessPosition from = new ChessPosition(x, 1);
		ChessPosition to = new ChessPosition(x, 3);
		ChessPiece p = b.getPieceAt(from);
		if(p != null){
			try{
				p.moveTo(to);
			}catch(IllegalMove e){
				System.out.println("Player 1 - Trying to move a pawn two spaces up");
				printInvalidExceptionGen("Player 1", from, to);
				failCount++;
			}
		}
		
		//Let's try to move it again by two spaces, this should throw an exception - player 1
		from = new ChessPosition(x, 3);
		to = new ChessPosition(x, 5);
		p = b.getPieceAt(from);
		if(p != null){
			try{
				p.moveTo(to);
				System.out.println("Player 1 - Trying to move the pawn 2 spaces up multiple times");
				printFailedToGenException("Player 1", from, to);
				failCount++;
			}catch(Exception e){
				assert(e instanceof IllegalMove);
			}
		}
		
		// Let's test if a pawn can move two spaces on the board - player 2
		x = genRandom(0, 7);
		from = new ChessPosition(x, 6);
		to = new ChessPosition(x, 4);
		p = b.getPieceAt(from);
		if(p != null){
			try{
				p.moveTo(to);
			}catch(IllegalMove e){
				System.out.println("Player 2 - Trying to move a pawn two spaces up");
				printInvalidExceptionGen("Player 2", from, to);
				failCount++;
			}
		}
		
		//Let's try to move it again by two spaces, this should throw an exception - player 2
		from = new ChessPosition(x, 4);
		to = new ChessPosition(x, 2);
		p = b.getPieceAt(from);
		if(p != null){
			try{
				p.moveTo(to);
				System.out.println("Player 2 - Trying to move the pawn 2 spaces up multiple times");
				printFailedToGenException("Player 2", from, to);
				failCount++;
			}catch(Exception e){
				assert(e instanceof IllegalMove);
			}
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testPawnTwoSquaresMoveAfterSingleSquareMove(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn can move one space on the board - player 1
		int x = genRandom(0, 7);
		ChessPosition from = new ChessPosition(x, 1);
		ChessPosition to = new ChessPosition(x, 2);
		ChessPiece p = b.getPieceAt(from);
		if(p != null){
			try{
				p.moveTo(to);
			}catch(IllegalMove e){
				System.out.println("Player 1 - Trying to move a pawn two spaces up");
				printInvalidExceptionGen("Player 1", from, to);
				failCount++;
			}
		}
		
		//Let's try to move it by two spaces now, this should throw an exception - player 1
		from = new ChessPosition(x, 2);
		to = new ChessPosition(x, 4);
		p = b.getPieceAt(from);
		if(p != null){
			try{
				p.moveTo(to);
				System.out.println("Player 1 - Trying to move the pawn 2 spaces up multiple times");
				printFailedToGenException("Player 1", from, to);
				failCount++;
			}catch(Exception e){
				assert(e instanceof IllegalMove);
			}
		}
		
		// Let's test if a pawn can move one space on the board - player 2
		x = genRandom(0, 7);
		from = new ChessPosition(x, 6);
		to = new ChessPosition(x, 5);
		p = b.getPieceAt(from);
		if(p != null){
			try{
				p.moveTo(to);
			}catch(IllegalMove e){
				System.out.println("Player 2 - Trying to move a pawn two spaces up");
				printInvalidExceptionGen("Player 2", from, to);
				failCount++;
			}
		}
		
		//Let's try to move it by two spaces, this should throw an exception - player 2
		from = new ChessPosition(x, 5);
		to = new ChessPosition(x, 3);
		p = b.getPieceAt(from);
		if(p != null){
			try{
				p.moveTo(to);
				System.out.println("Player 2 - Trying to move the pawn 2 spaces up multiple times");
				printFailedToGenException("Player 2", from, to);
				failCount++;
			}catch(Exception e){
				assert(e instanceof IllegalMove);
			}
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testPawnCapturingSamePlayerPiece(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn can capture it's own piece - player 1
		//move (0,1) -> (0,2) , move (1,1) -> (1,3) and move (0,2) -> (1,3) (exception)
		ChessPosition pos1 = new ChessPosition(0, 1);
		ChessPosition pos2 = new ChessPosition(0, 2);
		ChessPosition pos3 = new ChessPosition(1, 1);
		ChessPosition pos4 = new ChessPosition(1, 3);
		try{
			b.getPieceAt(pos1).moveTo(pos2);
			b.getPieceAt(pos3).moveTo(pos4);
		}catch(IllegalMove e){
			System.out.println("Player 1 - Trying to move the pawn one/two spaces");
			printInvalidExceptionGen("Player 1", pos1, pos2);
			printInvalidExceptionGen("Player 1", pos3, pos4);
			failCount++;
		}
		
		ChessPosition pos5 = new ChessPosition(0, 2);
		ChessPosition pos6 = new ChessPosition(1, 3);
		try{
			b.getPieceAt(pos5).moveTo(pos6);
			System.out.println("Player 1 - Trying to capture Pawn of the same player");
			printFailedToGenException("Player 1", pos5, pos6);
			failCount++;
		}catch(Exception e){
			assert(e instanceof IllegalMove);
		}
		
		printFooter(funcName, failCount);
	
	}
	
	@Test
	public void testPawnBlockedMove(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn honors the blockade 
		//move (0,1) -> (0,3) , move (0,6) -> (0,4) and move (0,3) -> (0,4) (exception)
		ChessPosition pos1 = new ChessPosition(0, 1);
		ChessPosition pos2 = new ChessPosition(0, 3);
		ChessPosition pos3 = new ChessPosition(0, 6);
		ChessPosition pos4 = new ChessPosition(0, 4);
		try{
			b.getPieceAt(pos1).moveTo(pos2);
			b.getPieceAt(pos3).moveTo(pos4);
		}catch(IllegalMove e){
			System.out.println("Player 1/2 - Trying to move the pawn two spaces");
			printInvalidExceptionGen("Player 1", pos1, pos2);
			printInvalidExceptionGen("Player 2", pos3, pos4);
			failCount++;
		}
		
		ChessPosition pos5 = new ChessPosition(0, 3);
		ChessPosition pos6 = new ChessPosition(0, 4);
		try{
			b.getPieceAt(pos5).moveTo(pos6);
			System.out.println("Player 1 - Trying to move a pawn which is blocked ahead");
			printFailedToGenException("Player 1", pos5, pos6);
			failCount++;
		}catch(Exception e){
			assert(e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testPawnCapture(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn honors the blockade 
		//move (0,1) -> (0,3) , move (1,6) -> (1,4) and move (0,3) -> (1,4) -> valid capture
		ChessPosition pos1 = new ChessPosition(0, 1);
		ChessPosition pos2 = new ChessPosition(0, 3);
		ChessPosition pos3 = new ChessPosition(1, 6);
		ChessPosition pos4 = new ChessPosition(1, 4);
		try{
			b.getPieceAt(pos1).moveTo(pos2);
			b.getPieceAt(pos3).moveTo(pos4);
		}catch(IllegalMove e){
			System.out.println("Player 1/2 - Trying to move the pawn two spaces");
			printInvalidExceptionGen("Player 1", pos1, pos2);
			printInvalidExceptionGen("Player 2", pos3, pos4);
			failCount++;
		}
		
		ChessPosition pos5 = new ChessPosition(0, 3);
		ChessPosition pos6 = new ChessPosition(1, 4);
		try{
			b.getPieceAt(pos5).moveTo(pos6);
		}catch(IllegalMove e){
			System.out.println("Player 1 - Trying to capture an opponent pawn");
			printInvalidExceptionGen("Player 1", pos5, pos6);
			failCount++;
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void testIfPawnCanMoveBackwards(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		// Let's test if a pawn honors the blockade 
		//move (0,1) -> (0,3) , move (1,6) -> (1,4) and move (0,3) -> (1,4) -> valid capture
		ChessPosition pos1 = new ChessPosition(0, 1);
		ChessPosition pos2 = new ChessPosition(0, 3);
		ChessPosition pos3 = new ChessPosition(1, 6);
		ChessPosition pos4 = new ChessPosition(1, 4);
		try{
			b.getPieceAt(pos1).moveTo(pos2);
			b.getPieceAt(pos3).moveTo(pos4);
		}catch(IllegalMove e){
			System.out.println("Player 1/2 - Trying to move the pawn two spaces");
			printInvalidExceptionGen("Player 1", pos1, pos2);
			printInvalidExceptionGen("Player 2", pos3, pos4);
			failCount++;
		}
		
		ChessPosition pos5 = new ChessPosition(0, 3);
		ChessPosition pos6 = new ChessPosition(0, 2);
		try{
			b.getPieceAt(pos5).moveTo(pos6);
			System.out.println("Player 1 - Trying to move the pawn backwards");
			printFailedToGenException("Player 1", pos5, pos6);
			failCount++;
		}catch(Exception e){
			assert(e instanceof IllegalMove);
		}
		
		ChessPosition pos7 = new ChessPosition(1, 4);
		ChessPosition pos8 = new ChessPosition(1, 5);
		try{
			b.getPieceAt(pos7).moveTo(pos8);
			System.out.println("Player 2 - Trying to move the pawn backwards");
			printFailedToGenException("Player 2", pos7, pos8);
			failCount++;
		}catch(Exception e){
			assert(e instanceof IllegalMove);
		}
		printFooter(funcName, failCount);
	}
	
	@Test
	public void templateFunc(){
		String funcName = new Object(){}.getClass().getEnclosingMethod().getName();
		printHeader(funcName);
		int failCount = 0;
		/* Let's test if each pawn moves two spaces on first move - player*/
		printFooter(funcName, failCount);
	
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
	
	public int genRandom(int min, int max){
		return min + (int)( Math.random() * ((max - min)+1));
	}
}