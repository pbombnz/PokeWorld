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
		Player p = new Player(-1, "Donald", new Avatar());
		ArrayList<Avatar> list = (ArrayList<Avatar>) p.getAvatar().getAllAvatars();
		p.setAvatar(list.get(0));
		assertTrue(p.getAvatar().getName().equals("Bulbasaur"));
	}

	@Test
	public void testAvatar2() throws IOException {
		Player p = new Player(-1, "Donald", new Avatar());
		ArrayList<Avatar> list = (ArrayList<Avatar>) p.getAvatar().getAllAvatars();
		p.setAvatar(list.get(1));
		assertTrue(p.getAvatar().getName().equals("Charmander"));
	}

	@Test
	public void testAvatar3() throws IOException {
		Player p = new Player(-1, "Donald", new Avatar());
		ArrayList<Avatar> list = (ArrayList<Avatar>) p.getAvatar().getAllAvatars();
		p.setAvatar(list.get(2));
		assertTrue(p.getAvatar().getName().equals("Squirtle"));
	}

	@Test
	public void testPlayerLevel1() throws IOException {
		Player p = new Player(-1, "Donald", new Avatar());
		ArrayList<Avatar> list = (ArrayList<Avatar>) p.getAvatar().getAllAvatars();
		p.setAvatar(list.get(2));
		assertTrue(p.getPlayerLevel() == 1);
	}
	@Test
	public void testPlayerLevel2() throws IOException {
		Player p = new Player(-1, "Donald", new Avatar());
		ArrayList<Avatar> list = (ArrayList<Avatar>) p.getAvatar().getAllAvatars();
		p.setAvatar(list.get(2));
		p.setPlayerLevel(2);
		assertTrue(p.getPlayerLevel() == 2);
	}
	@Test
	public void testPlayerLevel3() throws IOException {
		Player p = new Player(-1, "Donald", new Avatar());
		ArrayList<Avatar> list = (ArrayList<Avatar>) p.getAvatar().getAllAvatars();
		p.setAvatar(list.get(2));
		p.setPlayerLevel(3);
		assertTrue(p.getPlayerLevel() == 3);
	}
	@Test
	public void testPlayerLevel4() throws IOException {
		Player p = new Player(-1, "Donald", new Avatar());
		ArrayList<Avatar> list = (ArrayList<Avatar>) p.getAvatar().getAllAvatars();
		p.getAvatar().getCurrentEvolution(p.getPlayerLevel());
		fail("");
	}
}
