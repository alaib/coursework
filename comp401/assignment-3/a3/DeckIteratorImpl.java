package a3;

public class DeckIteratorImpl implements DeckIterator {
	
	private boolean check_face;
	private boolean face_status;
	private Deck deck;
	private boolean done;
	
	DeckIteratorImpl(Deck deck) {
		check_face = false;
		this.deck = deck;
		done = false;
	}
	
	DeckIteratorImpl(Deck deck, boolean face_status) {
		this(deck);
		check_face = true;
		this.face_status = face_status;
	}
	

	@Override
	public boolean hasNext() {
		if (done) {
			return false;
		}

		if (deck.getSize() == 0) {
			done = true;
		} else if (check_face) {			
			boolean found_eligible_card = false;
			for (int i=0; i<deck.getSize(); i++) {
				if (deck.getTop().isFaceUp() == face_status) {
					found_eligible_card = true;
				}
				deck.rotate();
			}
			if (!found_eligible_card)
				done = true;
		}
		return !done;
	}

	@Override
	public Card next() {
		if (!hasNext()) {
			throw new RuntimeException("No next card");
		}

		if (check_face) {
			while (deck.getTop().isFaceUp() != face_status) {
				deck.rotate();
			}
		}
		return deck.takeTop();
	}
}