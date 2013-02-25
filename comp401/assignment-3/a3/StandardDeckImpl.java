package a3;

import java.util.ArrayList;

public class StandardDeckImpl implements Deck {

	StandardCard[] cards;
	ArrayList<Card> cards_in_deck;

	StandardDeckImpl() {
		cards = new StandardCard[52];

		int cidx = 0;
		for (StandardCardImpl.Suit s : StandardCardImpl.Suit.values()) {
			for (int r=1; r<=13; r++) {
				cards[cidx++] = new StandardCardImpl(s, r, this);
			}
		}		
		reset();
	}
		
	public void printDeck(){
		System.out.println("=========== Start Print Deck ==============");
		for(int i = 0; i < this.getSize(); i++){
			int j = i + 1;
			StandardCard c = (StandardCard)this.getTop();
			StandardCard.Suit s = c.getSuit();
			String suitString = "♠";
			String cardStatus = "U";
			if(s == StandardCard.Suit.CLUBS){
				suitString = "♣";
			}else if(s == StandardCard.Suit.HEARTS){
				suitString = "♥";
			}else if(s == StandardCard.Suit.DIAMONDS){
				suitString = "♦";
			}
			if(c.isFaceUp() == false){
				cardStatus = "D";
			}
			int rank = c.getRank();
			String rankString = Integer.toString(rank);
			if(rank == 1 || rank > 10){
				switch(rank){
				case 1:
					rankString = "A";
					break;
				case 11:
					rankString = "J";
					break;
				case 12:
					rankString = "Q";
					break;
				case 13:
					rankString = "K";
				}
			}
			System.out.println(j + " " + rankString+" "+suitString+" "+cardStatus);				
			this.rotate();
		}
		System.out.println("=========== End Print Deck ==============");
	}
	
	public int getSize() {
		return cards_in_deck.size();
	}
	
	public boolean isEmpty() {
		return getSize() == 0;
	}

	public Card takeTop() {
		if (getSize() == 0) throw new RuntimeException("Deck is empty");

		return cards_in_deck.remove(getSize()-1);
	}

	public Card getTop() {
		if (getSize() == 0) throw new RuntimeException("Deck is empty");
		
		return cards_in_deck.get(getSize()-1);
	}
	
	public void placeOnTop(Card c) {
		if (c.getDeck() != this) {
			throw new RuntimeException("Card not from this deck");
		}
		cards_in_deck.add(c);
	}
	
	public void cut(int position) {
		if ((position <= 0) || (position >= getSize())) {
			return;
		}
		
		for (int i=0; i<getSize()-position; i++) {
			rotate();
		}
	}

	public void rotate() {
		if (getSize() > 0) {
			Card c = takeTop();
			cards_in_deck.add(0, c);
		}
	}

	public DeckIterator iterator() {
		return new DeckIteratorImpl(this);
	}

	public DeckIterator faceDownIterator() {
		return new DeckIteratorImpl(this, false);
	}

	public DeckIterator faceUpIterator() {
		return new DeckIteratorImpl(this, true);
	}
	
	public void reset() {
		cards_in_deck = new ArrayList<Card>();
		for (Card c : cards) {
			if (c.isFaceUp()) {
				c.flip();
			}
			cards_in_deck.add(c);
		}
	}

}