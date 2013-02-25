package a3;

public interface DeckIterator {
	
	// hasNext() returns true if another
	// card is available through this
	// DeckIterator
	
	boolean hasNext();
	
	// next() returns the card at the
	// top of the associated deck object and removes it from
	// the deck. Specific implementations of DeckIterator
	// may rotate the deck some number of times in order to
	// get a particular card or type of card on top before 
	// removing and returning it. For example, see the
	// comments associated with the methods 
	// faceUpIterator() and faceDownIterator() in the
	// definition of Deck.
	Card next();
}
