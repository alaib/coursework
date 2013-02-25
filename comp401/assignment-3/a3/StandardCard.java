package a3;

public interface StandardCard extends Card {
	
	public enum Suit {SPADES, HEARTS, DIAMONDS, CLUBS};

	public static final int ACE = 1;
	public static final int JACK = 11;
	public static final int QUEEN = 12;
	public static final int KING = 13;

	Suit getSuit();
	int getRank();
	boolean equals(StandardCard c);
}
