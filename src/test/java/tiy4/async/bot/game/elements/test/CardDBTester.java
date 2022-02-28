package tiy4.async.bot.game.elements.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

import ti4.async.bot.game.elements.CardDB;

public class CardDBTester {

	@Test
	public void test() {
		File f = new File(this.getClass().getClassLoader().getResource("cards.csv").getFile());
		CardDB.initializeSingleton(f);
		assertNotNull(CardDB.getSingleton());
		assertEquals(CardDB.getSingleton().getActionCardList().size(), 2);
		assertEquals(CardDB.getSingleton().getAgendaCardList().size(), 1);
	}

}
