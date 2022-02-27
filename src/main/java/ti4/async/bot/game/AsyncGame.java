package ti4.async.bot.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ti4.async.bot.game.elements.Card;

public class AsyncGame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1276619000660772064L;
	private String gameName;
	private int gameNumber = -1;
	private long primaryChannelID = -1;

	// Enum stating if we're playing or waiting to play
	private AsyncGameState gameState;

	// Map of the players, sorted by discord ID
	private HashMap<Long, Player> playerMap = new HashMap<Long, Player>(); // first key is player's discord ID

	// Action card deck
	private Deque<Card> actionCardDeck = new ArrayDeque<>(250);
	private List<Card> actionCardDiscard = new ArrayList<>(250);

	public Set<Long> getPlayerSet() {
		return playerMap.keySet();
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public AsyncGameState getGameState() {
		return gameState;
	}

	public void setGameState(AsyncGameState gameState) {
		this.gameState = gameState;
	}

	public void addPlayer(Player player) {
		playerMap.put(player.getDiscordId(), player);
	}

	public void removePlayer(Player player) {
		playerMap.remove(player.getDiscordId());
	}

	public long getPrimaryChannelID() {
		return primaryChannelID;
	}

	public void setPrimaryChannelID(long primaryChannelID) {
		this.primaryChannelID = primaryChannelID;
	}

	public void saveGameToFile(File f) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(this);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static AsyncGame readGameFromFile(File f) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
			Object temp = ois.readObject();
			if (temp instanceof AsyncGame) {
				return (AsyncGame) temp;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean containsPlayerId(long playerID) {
		return playerMap.containsKey(playerID);
	}

	public void removePlayer(long playerID) {
		playerMap.remove(playerID);
	}

	public void drawActionCards(long playerID, int cardCount) {
		Player p = playerMap.get(playerID);
		if (p != null) {
			if (actionCardDeck.isEmpty()) {
				reshuffleActionCardDiscardIntoDeck();
			}
			Card c = actionCardDeck.pollFirst();
			p.giveCard(c);
		}
	}

	private void reshuffleActionCardDiscardIntoDeck() {
		Collections.shuffle(actionCardDiscard);
		actionCardDeck.addAll(actionCardDiscard);
		actionCardDiscard.clear();
	}
}
