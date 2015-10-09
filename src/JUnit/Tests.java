package JUnit;

import org.junit.Test;

import game.Player;
import game.avatar.Avatar;
import game.objects.GameObject;
import rooms.Board1;
import rooms.Board2;
import rooms.Board3;
import ui.GameLauncher;
import ui.GamePlayFrame;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Donald Tang
 */
public class Tests {
	private Robot bot;
	private GameLauncher g;
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testBoard1() {
		Board1 board = new Board1();
		GameObject o = (GameObject) board.squares[11][11];
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testBoard2() {
		Board2 board = new Board2();
		GameObject o = (GameObject) board.squares[11][11];
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testBoard3() {
		Board3 board = new Board3();
		GameObject o = (GameObject) board.squares[11][11];
	}

	@Test
	public void testInstanceOfBoard1() {
		Board1 board = new Board1();
		assertThat(board, instanceOf(Board1.class));
	}

	@Test
	public void testInstanceOfBoard2() {
		Board2 board = new Board2();
		assertThat(board, instanceOf(Board2.class));
	}

	@Test
	public void testInstanceOfBoard3() {
		Board3 board = new Board3();
		assertThat(board, instanceOf(Board3.class));
	}

	@Test
	public void testAvatar1() throws IOException {
		Player p = new Player("Donald");
		p.setAvatar(new Avatar());
		ArrayList<Avatar> list = (ArrayList<Avatar>) p.getAvatar().getAllAvatars();
		p.setAvatar(list.get(0));
		assertTrue(p.getAvatar().getName().equals("Bulbasaur"));
	}

	@Test
	public void testAvatar2() throws IOException {
		Player p = new Player("Donald");
		p.setAvatar(new Avatar());
		ArrayList<Avatar> list = (ArrayList<Avatar>) p.getAvatar().getAllAvatars();
		p.setAvatar(list.get(1));
		assertTrue(p.getAvatar().getName().equals("Charmander"));
	}

	@Test
	public void testAvatar3() throws IOException {
		Player p = new Player("Donald");
		p.setAvatar(new Avatar());
		ArrayList<Avatar> list = (ArrayList<Avatar>) p.getAvatar().getAllAvatars();
		p.setAvatar(list.get(2));
		assertTrue(p.getAvatar().getName().equals("Squirtle"));
	}

	@Test
	public void testCharacterSelect() throws IOException, AWTException {
		createGame();
		bot.mouseMove(200, 200);
		bot.delay(1000);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		bot.keyPress(KeyEvent.VK_S);
		bot.keyRelease(KeyEvent.VK_S);
		bot.delay(100);
		bot.keyPress(KeyEvent.VK_S);
		bot.keyRelease(KeyEvent.VK_S);
		bot.delay(100);
		
		
	}

	public void createGame() throws AWTException {
		this.g = new GameLauncher();
		this.bot = new Robot();
		bot.delay(1000);
		bot.mouseMove(10, 40);
		bot.delay(1000);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		bot.mouseMove(10, 50);
		bot.delay(1000);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		bot.delay(1000);
		bot.mouseMove(1000, 40);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		bot.delay(1000);
		bot.mouseMove(10, 40);
		bot.delay(1000);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		bot.mouseMove(10, 80);
		bot.delay(1000);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		bot.delay(1000);
		bot.keyPress(KeyEvent.VK_H);
		bot.delay(500);
		bot.keyPress(KeyEvent.VK_H);
		bot.delay(500);
		bot.keyPress(KeyEvent.VK_H);
		bot.delay(500);
		bot.keyPress(KeyEvent.VK_H);
		bot.keyPress(KeyEvent.VK_ENTER);
		bot.delay(500);
		bot.mouseMove(500, 200);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		bot.delay(5000);
	}
}
