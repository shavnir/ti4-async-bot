package ti4.async.bot.game.elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class CardDB {

	private static CardDB instance;

	private EnumMap<CardType, List<Card>> cardDatabase;

	public CardDB(File f) {
		cardDatabase = new EnumMap<>(CardType.class);
		for (CardType ct : CardType.values()) {
			cardDatabase.put(ct, new ArrayList<Card>());
		}

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line = reader.readLine();
			while (line != null) {
				// Parse action card line
				String[] splits = line.split(",");
				int id = Integer.parseInt(splits[0]);
				String name = splits[1];
				String text = splits[2];
				CardType type = CardType.valueOf(splits[3]);
				Card c = new Card(id, name, text, type);
				cardDatabase.get(type).add(c);

				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	public List<Card> getAgendaCardList() {
		return cardDatabase.get(CardType.AGENDA);
	}
}
