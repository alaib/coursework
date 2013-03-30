package a5;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {

	public static void main(String[] args) {

		ChessPlayer p1 = new ChessPlayer("P1");
		ChessPlayer p2 = new ChessPlayer("P2");
		
		ChessGame g = new ChessGame(p1, p2);
		ChessBoard b = g.getBoard();
		
		Scanner s = new Scanner(System.in);
		
		// This pattern matches valid move command coordinates
		Pattern move_pattern = Pattern.compile("\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)\\s*->\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
		
		while (true) {
			// Print out the board
			System.out.print(b.toString());

			// Print input cursor
			System.out.print('>');

			// Get next command from input
			String cmd = s.next();

			// Quit if "quit"
			if (cmd.equals("quit")) {
				break;
			}

			if (cmd.equals("move")) {
				// Process "move" command.
				// Match rest of line against expected form of move command
				// (i.e., something that looks like: (x,y)->(X,Y) where x/X and y/Y are digits)
				
				Matcher matcher = move_pattern.matcher(s.nextLine());
				if (matcher.find()) {
					// Matched correctly. Create positions from matched data
					try {
						ChessPosition from = new ChessPosition(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
						ChessPosition to = new ChessPosition(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));

						// See if there is a piece at the first coordinate...
						ChessPiece p = b.getPieceAt(from);
						if (p != null) {
							// ... and if so, then move to the second coordinate.
							p.moveTo(to);
						}
					} catch (IllegalArgumentException e) {
						// Catch exception when coordinates are out of range
						System.out.println(e.getMessage());
					} catch (IllegalMove e) {
						// Catch exception when move is illegal
						System.out.println(e.getMessage());
					}
				} else {
					// Rest of line does not match expected form for move command.
					// Report this fact.
					System.out.println("Badly formed move command");
				}
			} else {
				// Report unknown command.
				System.out.println("Unknown command");
			}
		}
		
		// Print goodbye message.
		System.out.println("Game Over");
		
	}
}
