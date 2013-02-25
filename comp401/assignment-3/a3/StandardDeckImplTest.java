package a3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import a3.StandardCard.Suit;

public class StandardDeckImplTest {

	@Test
	public void testGetSize() {

		Deck d = new StandardDeckImpl();

		assertEquals("Standard deck should start off with 52 cards", d.getSize(), 52);

		Card c = d.getTop();

		assertEquals("Deck shouldn't change size because of getTop()", d.getSize(), 52);

		c = d.takeTop();

		assertEquals("Deck size should go down by one if card taken with takeTop()", d.getSize(), 51);

		d.placeOnTop(c);

		assertEquals("Deck size should go up by one if card placed back in deck with placeOnTop()", d.getSize(), 52);
	}

	@Test
	public void testIsEmpty() {
		Deck d = new StandardDeckImpl();

		for (int num_cards_taken = 0; num_cards_taken < 52; num_cards_taken++) {
			assertEquals("isEmpty() should be false until 52 cards taken", d.isEmpty(), false);
			d.takeTop();
		}
		assertEquals("isEmpty() should be true after 52 cards taken", d.isEmpty(), true);
	}


	@Test
	public void testTop() {
		Deck d = new StandardDeckImpl();

		Card card_from_get_top = d.getTop();
		Card card_from_take_top = d.takeTop();

		assertEquals("takeTop() should return same object as prior call to getTop()",
				card_from_get_top, card_from_take_top);

		d.placeOnTop(card_from_take_top);

		assertEquals("getTop() should return same object after top card taken and then placed back on top",
				card_from_get_top, d.getTop());
	}

	@Test
	public void testPlaceOnTop() {
		Deck d1 = new StandardDeckImpl();
		Deck d2 = new StandardDeckImpl();

		Card card_from_d1 = d1.takeTop();
		Card card_from_d2 = d2.takeTop();

		try {
			d1.placeOnTop(card_from_d2);
			fail("Placing card from a different deck should throw a runtime exception");
		} catch (RuntimeException e) {

		}

		try {
			d2.placeOnTop(card_from_d1);
			fail("Placing card from a different deck should throw a runtime exception");
		} catch (RuntimeException e) {

		}

		try {
			StandardCard c = new StandardCardImpl(Suit.SPADES, 1, null);
			d1.placeOnTop(c);
			fail("Placing card not associated with correct deck should throw a runtime exception");
		} catch (RuntimeException e) {

		}		
	}

	@Test
	public void testAllCardsPresent() {
		Deck d = new StandardDeckImpl();

		int num_spades = 0;
		int num_hearts = 0;
		int num_diamonds = 0;
		int num_clubs = 0;

		int[] rank_counts = new int[14];

		while (!d.isEmpty()) {
			StandardCard c = (StandardCard) d.takeTop();

			rank_counts[c.getRank()] += 1;
			switch (c.getSuit()) {
			case HEARTS:
				num_hearts++; break;
			case DIAMONDS:
				num_diamonds++; break;
			case CLUBS:
				num_clubs++; break;
			case SPADES:
				num_spades++; break;
			}			
		}

		assertEquals("Should be 13 spades in deck", num_spades, 13);
		assertEquals("Should be 13 hearts in deck", num_hearts, 13);
		assertEquals("Should be 13 diamonds in deck", num_diamonds, 13);
		assertEquals("Should be 13 clubs in deck", num_clubs, 13);
		assertEquals("Should be 4 aces in deck", rank_counts[StandardCard.ACE], 4);
		assertEquals("Should be 4 twos in deck", rank_counts[2], 4);
		assertEquals("Should be 4 threes in deck", rank_counts[3], 4);
		assertEquals("Should be 4 fours in deck", rank_counts[4], 4);
		assertEquals("Should be 4 fives in deck", rank_counts[5], 4);
		assertEquals("Should be 4 sixes in deck", rank_counts[6], 4);
		assertEquals("Should be 4 sevens in deck", rank_counts[7], 4);
		assertEquals("Should be 4 eights in deck", rank_counts[8], 4);
		assertEquals("Should be 4 nines in deck", rank_counts[9], 4);
		assertEquals("Should be 4 tens in deck", rank_counts[10], 4);
		assertEquals("Should be 4 jacks in deck", rank_counts[StandardCard.JACK], 4);
		assertEquals("Should be 4 queens in deck", rank_counts[StandardCard.QUEEN], 4);
		assertEquals("Should be 4 kings in deck", rank_counts[StandardCard.KING], 4);		
	}

	@Test
	public void testReset() {
		Deck d = new StandardDeckImpl();

		StandardCard[] orig_cards = new StandardCard[52];

		for (int i=0; i<52; i++) {
			orig_cards[i] = (StandardCard) d.takeTop();
		}

		d.reset();

		for (int i=0; i<52; i++) {
			assertTrue("Card " + i + " from top not same after reset", orig_cards[i].equals((StandardCard) d.takeTop()));
		}

		d.reset();

		for (int i=0; i<52; i++) {
			d.getTop().flip();
			d.rotate();
		}

		d.reset();

		for (int i=0; i<52; i++) {
			assertTrue("Card " + i + " should be flipped face down on reset", !(d.getTop().isFaceUp()));
		}
	}

	@Test
	public void testCut() {
		for (int i=1; i<52; i++) {
			testSpecificCut(i);
		}
	}

	public void testSpecificCut(int pos) {
		Deck d = new StandardDeckImpl();

		for (int i=0; i<52-pos; i++) {
			d.rotate();
		}

		Card c = d.getTop();

		for (int i=0; i<pos; i++) {
			d.rotate();
		}

		d.cut(pos);

		assertEquals("Cutting deck at " + pos + " from bottom should be same as rotating " + (52-pos) + " times", c, d.getTop());
	}

	@Test
	public void testIterator() {
		Deck d = new StandardDeckImpl();

		DeckIterator faceless_iterator = d.iterator();

		assertTrue("Iterator should have next card to give", faceless_iterator.hasNext());getClass();

		Card card_on_top = d.getTop();
		Card card_from_iterator = faceless_iterator.next();
		assertEquals("Iterator next() should return top card", card_on_top, card_from_iterator);
		assertEquals("Using next() should remove card from deck", d.getSize(), 51);

		card_on_top = d.getTop();
		card_on_top.flip();
		card_from_iterator = faceless_iterator.next();
		assertEquals("Iterator next() should return top card even if face up", card_on_top, card_from_iterator);
		assertEquals("Using next() should remove card from deck", d.getSize(), 50);
	}


	@Test
	public void testFaceUpDownIterator() {
		Deck d = new StandardDeckImpl();

		Card c1 = d.getTop();

		// Rotate deck 4 times
		for (int i=0; i<4; i++) {d.rotate();}

		// Remember this card and flip it
		Card c5 = d.getTop();
		c5.flip();

		// Remember the card after the flipped card
		d.rotate();
		Card c6 = d.getTop();

		// Rotate deck 9 more times
		for (int i=0; i<9; i++) {d.rotate();}

		// Remember this card and flip it, and remember card after as well
		Card c15 = d.getTop();
		c15.flip();
		d.rotate();
		Card c16 = d.getTop();
		

		// Rotate deck 9 more times
		for (int i=0; i<9; i++) {d.rotate();}

		// Remember this card and flip it, and remember card after as well
		Card c25 = d.getTop();
		c25.flip();
		d.rotate();
		Card c26 = d.getTop();

		// Rotate deck 9 more times
		for (int i=0; i<9; i++) {d.rotate();}

		// Remember this card and flip it, and remember card after as well
		Card c35 = d.getTop();
		c35.flip();
		d.rotate();
		Card c36 = d.getTop();

		// Rotate deck 9 more times
		for (int i=0; i<9; i++) {d.rotate();}

		// Remember this card and flip it, and remember card after as well
		Card c45 = d.getTop();
		c45.flip();
		d.rotate();
		Card c46 = d.getTop();

		while (d.getTop() != c1) {d.rotate();}
	
		StandardDeckImpl d1 = (StandardDeckImpl)d;
		d1.printDeck();

		DeckIterator di = d.iterator();
		DeckIterator di_up = d.faceUpIterator();
		DeckIterator di_down = d.faceDownIterator();

		assertTrue("Faceless iterator should have more cards", di.hasNext());
		assertTrue("Faceup iterator should have more cards", di_up.hasNext());
		assertTrue("Facedown iterator should have more cards", di_down.hasNext());

		assertEquals("Next card from face up iterator should be " + printStandardCard((StandardCard) c5),
				c5, di_up.next());

		assertEquals("Next card from face down iterator should be " + printStandardCard((StandardCard) c6),
				c6, di_down.next());

		assertEquals("Next card from face up iterator should be " + printStandardCard((StandardCard) c15),
				c15, di_up.next());

		assertEquals("Next card from faceless iterator should be " + printStandardCard((StandardCard) c16),
				c16, di.next());

		assertEquals("Next card from face up iterator should be " + printStandardCard((StandardCard) c25),
				c25, di_up.next());

		assertEquals("Next card from face down iterator should be " + printStandardCard((StandardCard) c26),
				c26, di_down.next());

		assertEquals("Next card from face up iterator should be " + printStandardCard((StandardCard) c35),
				c35, di_up.next());

		assertEquals("Next card from faceless iterator should be " + printStandardCard((StandardCard) c36),
				c36, di.next());

		assertEquals("Next card from face up iterator should be " + printStandardCard((StandardCard) c45),
				c45, di_up.next());

		assertEquals("Next card from face down iterator should be " + printStandardCard((StandardCard) c46),
				c46, di_down.next());
		
		assertTrue("Faceless iterator should have more cards", di.hasNext());
		assertTrue("Faceup iterator should not have more cards", !di_up.hasNext());
		assertTrue("Facedown iterator should have more cards", di_down.hasNext());

	}
	
	public String printStandardCard(StandardCard c) {
		return c.getRank() + " of " + c.getSuit().toString();
	}

}