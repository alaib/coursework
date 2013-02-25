package a3;

public class StandardCardImpl implements StandardCard {

	private boolean face_up;
	private Suit suit;
	private int rank;
	private StandardDeckImpl deck;
	
	public StandardCardImpl(Suit suit, int rank, StandardDeckImpl deck) {
		face_up = false;
		
		if (rank < 1 || rank > KING) {
			throw new RuntimeException("Rank is out of range for standard card");
		}

		this.suit = suit;
		this.rank = rank;
		this.deck = deck;
	}

	public Suit getSuit() {
		return suit;
	}
	
	public int getRank() {
		return rank;
	}
	
	public Deck getDeck() {
		return deck;
	}
	
	public boolean isFaceUp() {
		return face_up;
	}
	
	public void flip() {
		face_up = !face_up;
	}

	@Override
	public boolean equals(StandardCard c) {
		return ((c.getSuit() == suit) && (c.getRank() == rank));
	}
}
