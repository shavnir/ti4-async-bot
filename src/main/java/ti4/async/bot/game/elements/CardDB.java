package ti4.async.bot.game.elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CardDB {
	
	private File dbFile;
	
	public CardDB(File f) {
		dbFile = f;
	}
	
	public List<Card> getActionCardList(){
		// for now dummy data
		List<Card> rtnVal = new ArrayList<Card>();
		for(int x=0;x<10;x++) {
			Card c = new Card(x,"Card " + x,"Generic Card Text", CardType.ACTION_CARD);
			rtnVal.add(c);
		}
		return rtnVal;
	}
}
