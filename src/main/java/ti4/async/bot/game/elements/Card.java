package ti4.async.bot.game.elements;

import java.io.Serializable;

public class Card implements Serializable {

	private long id;
	private String name;
	private String text;
	private CardType type;
	
	public Card(long id, String name, String text, CardType type) {
		this.id = id;
		this.name = name;
		this.text = text;
		this.type = type;
	}

	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getText() {
		return text;
	}
	public CardType getType() {
		return type;
	}
}
