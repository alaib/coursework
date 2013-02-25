package a3;

public interface Card {

	// Every Card belongs to some deck. The
	// getDeck() method should return the Deck
	// object associated with the Card.
	// 
	Deck getDeck();

	// A Card is either "face up" or "face down"
	// The isFaceUp() method should return true if
	// the Card is "face up" and false otherwise.
	boolean isFaceUp();
	
	// The flip() method should make a "face up"
	// card "face down" and vice versa.
	void flip();
	
}
