package ti4.async.bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ti4.async.bot.game.AsyncGame;
import ti4.async.bot.game.AsyncGameState;
import ti4.async.bot.game.Player;

public class BotListener extends ListenerAdapter {

	private HashMap<String, AsyncGame> gameMap = new HashMap<String, AsyncGame>();

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		long channelID = event.getChannel().getIdLong();
		long senderID = event.getAuthor().getIdLong();

		String messageText = event.getMessage().getContentStripped();

		// All commands will start with !
		if (!messageText.startsWith("!")) {
			return;
		}

		// Check to see if it is in a game's public channel
		Optional<Entry<String, AsyncGame>> opt = gameMap.entrySet().stream()
				.filter(x -> channelID == x.getValue().getPrimaryChannelID()).findFirst();
		if (opt.isPresent()) {
			AsyncGame relevantGame = opt.get().getValue();
			processMessageInGameChannel(relevantGame, senderID, messageText, event);
			// we're done here
			return;
		}

		// handle PMs here
		if (event.isFromType(ChannelType.PRIVATE)) {
			processPM(senderID, messageText, event);
			return;
		}

		// set game creation ability here
		processMessageOutsideOfGameChannel(channelID, senderID, messageText, event);
	}

	private void processMessageOutsideOfGameChannel(long channelID, long senderID, String messageText,
			MessageReceivedEvent event) {
		messageText = messageText.trim();
		String[] split = messageText.split(" ");
		if(messageText.startsWith("!createGame")) {
			if(split.length < 3)
			{
				event.getChannel().sendMessage("Wrong number of arugments for !createGame.  Format is !createGame # Name");
				return;
			}
			
			int gameNum = Integer.parseInt(split[1]);
			String gameName = messageText.substring(messageText.indexOf(' '));
			gameName = gameName.substring(gameName.indexOf(' '));
			
			AsyncGame game = new AsyncGame();
			game.setGameName(gameName);
			game.setGameNumber(gameNum);
			gameMap.put(gameName, game);
			
			return;
		}

	}

	private void processPM(long senderID, String messageText, MessageReceivedEvent event) {
		messageText = messageText.trim().toLowerCase();
		if (messageText.startsWith("!listmygames")) {
			gameMap.entrySet().stream().filter(x -> x.getValue().getPlayerSet().contains(senderID))
					.map(x -> "Game Number : " + x.getValue().getGameNumber() + " Name : " + x.getValue().getGameName())
					.forEach(s -> event.getPrivateChannel().sendMessage(s));
			return;
		}
		
	}

	/**
	 * This method will process a specific command sent to the bot in a specific
	 * game's primary channel
	 * 
	 * @param relevantGame the game, looked by by syncing to a discord channel's id
	 * @param senderID     the sender of the message
	 * @param messageText  the text of the message, stripped of markdown et al
	 * @param event
	 */
	private void processMessageInGameChannel(AsyncGame relevantGame, long senderID, String messageText,
			MessageReceivedEvent event) {
		messageText = messageText.trim().toLowerCase();
		if (relevantGame.getGameState() == AsyncGameState.REGISTERING_PLAYERS) {
			switch (messageText) {
			case "!addme":
				if (!relevantGame.containsPlayerId(senderID)) {
					Player p = new Player(senderID);
					relevantGame.addPlayer(p);
				}
				break;
			case "!removeme":
				if (relevantGame.containsPlayerId(senderID)) {
					relevantGame.removePlayer(senderID);
				}
				break;

			case "!startgame":
				event.getChannel().sendMessage("Starting game!");
				relevantGame.setGameState(AsyncGameState.IN_GAME);
				break;
			}
		} else if (relevantGame.getGameState() == AsyncGameState.IN_GAME) {
			if (messageText.startsWith("!drawac")) {
				String[] splitMessage = messageText.split(" ");
				if (splitMessage.length > 1) {
					try {
						int cardCount = Integer.parseInt(splitMessage[1]);
						relevantGame.drawActionCards(senderID, cardCount);
					} catch (NumberFormatException nfe) {
						event.getChannel()
								.sendMessage("Unknown command.  Format for !drawac is either !drawac or !drawac ##");
					}
				} else {
					relevantGame.drawActionCards(senderID, 1);
				}
			}
		}
	}

}
