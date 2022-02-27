package ti4.async.bot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class BotEntryPoint {

	public static void main(String[] args) {
		try {
			JDA jda = JDABuilder.createDefault(args[0]).build();
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}

}
