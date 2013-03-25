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
	public void testInitialBoard(){
		System.out.println("==========================");
		System.out.println("testInitialBoard debug log");
		System.out.println("==========================\n");
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
					System.out.println("Move ("+i+", "+startY+") -> Move ("+i+", "+endY+") failed to generate an Illegal Move exception");
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
					System.out.println("Move ("+i+", "+startY+") -> Move ("+i+", "+endY+") failed to generate an Illegal Move exception");
					failCount++;
				} catch (Exception e) {
					assert(e instanceof IllegalMove);
				}
			}
		}
		if(failCount > 0){
			fail("testInitialBoard failed, failure count = "+failCount);
		}else{
			System.out.println("testInitialBoard passed");
		}
		System.out.println("\n==========================\n");
	}
	
	@AfterClass
	public static void runOnceAfterAllTests(){
		
	}

}
