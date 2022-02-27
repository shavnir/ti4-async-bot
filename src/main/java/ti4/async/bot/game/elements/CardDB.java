package ti4.async.bot.game.elements;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class CardDB {

	private static CardDB instance;

	private EnumMap<CardType, List<Card>> cardDatabase;

	public CardDB(File f) {
		// for now dummy data to have something in there!
		List<Card> actionCardList = new ArrayList<Card>();
		for (int x = 0; x < 10; x++) {
			Card c = new Card(x, "Card " + x, "Generic Card Text", CardType.ACTION_CARD);
			actionCardList.add(c);
		}
		cardDatabase = new EnumMap<>(CardType.class);
		cardDatabase.put(CardType.ACTION_CARD, actionCardList);
	}

	public static void initializeSingleton(File f) {
		// load from file here
		instance = new CardDB(f);
	}

	public static CardDB getSingleton() {
		return instance;
	}

	public List<Card> getActionCardList() {
		return cardDatabase.get(CardType.ACTION_CARD);
	}
}
