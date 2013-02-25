package a3;
import java.util.Iterator;

public interface Deck {
//	public void printDeck();
	// getSize() returns the number of cards still in the deck
	int getSize();

	// isEmpty() returns true if the Deck is currently empty
	// (i.e., if getSize() returns 0)
	boolean isEmpty();
	
	// getTop() returns the Card at top of deck but does not remove
	// it from the deck.
	// Throws a runtime exception if the deck is empty
	Card getTop();
	
	// takeTop() returns the Card at top of deck and also
	// removes it from the deck.
	// Throws a runtime exception if the deck is empty
	Card takeTop();
	
	// placeOnTop(Card c) places the Card object passed in as the
	// parameter c on top of the deck. The size of the deck should
	// grow by one.
	// Throws a runtime exception if the card does not "belong" to
	// the deck (i.e., if the object reference returned by c.getDeck()
	// is not a reference to this deck).
	void placeOnTop(Card c);
	
	// cut(int position) cuts the deck "position" cards from the bottom.
	// Should be equivalent to executing rotate "getSize() - position" times.
	// If position is less than 1 or equal to or greater than the number
	// of cards in the deck, the method should do nothing to alter the
	// deck.
	void cut(int position);
	
	// Rotates the deck by taking the card from the top and placing it
	// on the bottom of the deck.
	void rotate();
	
	// The iterator() method generates an object that acts as an
	// iterator for the cards in the deck. See the definition of 
	// DeckIterator for details.
	DeckIterator iterator();
	
	// The faceUpIterator() method generates an object that acts as an
	// iterator for the cards in the deck ignoring cards that are face
	// down. In other words, hasNext() will return true only if the deck still
	// has at least one card that is face up. Similarly, next() will rotate the
	// deck until the next face up card is on the top, and then remove and return
	// that face up card.
	DeckIterator faceUpIterator();
	
	// The faceDownIterator() method generates an object that acts as an
	// iterator for the cards in the deck ignoring cards that are face
	// up.
	DeckIterator faceDownIterator();
	
	// Resets the deck with all of its original card objects in their original
	// order and all face down.
	void reset();
}
