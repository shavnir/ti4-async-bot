package ti4.async.bot.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ti4.async.bot.game.elements.Card;

public class Player implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2288205124281785620L;
	private long discordId;
	private String displayName;

	private List<Card> hand = new ArrayList<>();
	
	public Player(long discordId) {
		this.discordId = discordId;
	}

	public void setDiscordId(long discordId) {
		this.discordId = discordId;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public long getDiscordId() {
		return discordId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void giveCard(Card c) {
		hand.add(c);
	}
	
	public Card takeCard(int index) { 
		Card c = hand.get(index);
		hand.remove(index);
		return c;
	}
	
}
