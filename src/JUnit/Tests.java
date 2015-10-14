package JUnit;

import org.junit.Test;

import game.Player;
import game.avatar.Avatar;
import game.objects.GameObject;
import network.GameServer;
import rooms.Board1;
import rooms.Board2;
import rooms.Board3;
import ui.GameLauncher;
import ui.GameFrame;
import ui.ServerFrame;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Donald Tang
 */
public class Tests {
	private Player p;
	private ArrayList<Avatar> list;

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
	/*
	 * Tests if avatar is made correctly
	 */
	@Test
	public void testAvatar1() throws IOException {
		createPlayer();
		p.setAvatar(list.get(0));
		assertTrue(p.getAvatar().getName().equals("Bulbasaur"));
	}

	@Test
	public void testAvatar2() throws IOException {
		createPlayer();
		p.setAvatar(list.get(1));
		assertTrue(p.getAvatar().getName().equals("Charmander"));
	}

	@Test
	public void testAvatar3() throws IOException {
		createPlayer();
		p.setAvatar(list.get(2));
		assertTrue(p.getAvatar().getName().equals("Squirtle"));
	}
	/*
	 * Tests if levels are correctly allocated
	 */
	@Test
	public void testPlayerLevel1() throws IOException {
		createPlayer();
		p.setAvatar(list.get(0));
		assertTrue(p.getPlayerLevel() == 1);
	}

	@Test
	public void testPlayerLevel2() throws IOException {
		createPlayer();
		p.setAvatar(list.get(0));
		p.setPlayerLevel(2);
		assertTrue(p.getPlayerLevel() == 2);
	}

	@Test
	public void testPlayerLevel3() throws IOException {
		createPlayer();
		p.setAvatar(list.get(0));
		p.setPlayerLevel(3);
		assertTrue(p.getPlayerLevel() == 3);
	}

	@Test
	public void testPlayerLevel4() throws IOException {
		try {
			createPlayer();
			p.setAvatar(list.get(0));
			p.getAvatar().getCurrentEvolution(p.getPlayerLevel());
		} catch (IllegalArgumentException e) {
			fail("Shouldn't have thrown an error");
		}
	}

	@Test
	public void testPlayerLevel5() throws IOException {
		try {
			createPlayer();
			p.setAvatar(list.get(0));
			p.getAvatar().getCurrentEvolution(p.getPlayerLevel());
		} catch (IllegalArgumentException e) {
			fail("Shouldn't have thrown an error");
		}
	}

	@Test
	public void testPlayerLevel6() throws IOException {
		try {
			createPlayer();
			p.setAvatar(list.get(0));
			p.getAvatar().getCurrentEvolution(5);
			fail("Max level is 3");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testPlayerLevel7() throws IOException {
		try {
			createPlayer();
			p.setAvatar(list.get(0));
			p.getAvatar().getCurrentEvolution(0);
			fail("Min level is 1");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testPlayerLevel8() throws IOException {
		try {
			createPlayer();
			p.setAvatar(list.get(0));
			p.getAvatar().getCurrentEvolution(0);
			fail("Min level is 1");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testPlayerLevel9() throws IOException {
		try {
			createPlayer();
			p.setAvatar(list.get(0));
			p.getAvatar().getNextEvolution(2);
		} catch (IllegalArgumentException e) {
			fail("Max level is 3");
		}
	}

	@Test
	public void testPlayerLevel10() throws IOException {
		try {
			createPlayer();
			p.setAvatar(list.get(0));
			p.getAvatar().getNextEvolution(3);
			fail("Max level is 3");
		} catch (IllegalArgumentException e) {

		}
	}

	@Test
	public void testPlayerLevel11() throws IOException {
		try {
			createPlayer();
			p.setAvatar(list.get(0));
			p.getAvatar().getNextEvolution(4);
			fail("Max level is 3");
		} catch (IllegalArgumentException e) {
		}
	}
	/*
	 * Tests the player.equals() method
	 */
	@Test
	public void testPlayerLevel12() throws IOException {
		try {
			createPlayer();
			p.setAvatar(list.get(0));
			p.getAvatar().getNextEvolution(-1);
			fail("Min level is 1");
		} catch (IllegalArgumentException e) {
		}
	}
	@Test
	public void testPlayerEquals1() throws IOException {
		try {
			createPlayer();
			Player p = new Player(-1, "Donald", new Avatar());
			this.p.setAvatar(list.get(0));
			p.setAvatar(list.get(0));
			assertTrue(p.equals(this.p));
		} catch (IllegalArgumentException e) {
		}
	}
	@Test
	public void testPlayerEquals2() throws IOException {
		try {
			createPlayer();
			Player p = new Player(-1, "Donald", new Avatar());
			this.p.setAvatar(list.get(1));
			p.setAvatar(list.get(1));
			assertTrue(p.equals(this.p));
		} catch (IllegalArgumentException e) {
		}
	}
	@Test
	public void testPlayerEquals3() throws IOException {
		try {
			createPlayer();
			Player p = new Player(-1, "Donald", new Avatar());
			this.p.setAvatar(list.get(2));
			p.setAvatar(list.get(2));
			assertTrue(p.equals(this.p));
		} catch (IllegalArgumentException e) {
		}
	}
	@Test
	public void testPlayerEquals4() throws IOException {
		try {
			createPlayer();
			Player p = new Player(-1, "Prashant", new Avatar());
			assertFalse(p.equals(this.p));
		} catch (IllegalArgumentException e) {
		}
	}
	@Test
	public void testPlayerEquals5() throws IOException {
		try {
			createPlayer();
			Player p = new Player(-2, "Donald", new Avatar());
			assertFalse(p.equals(this.p));
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testPlayerEquals6() throws IOException {
		try {
			createPlayer();
			Player p = new Player(-1, null, new Avatar());
			this.p.setAvatar(list.get(2));
			p.setAvatar(list.get(0));
			assertFalse(p.equals(this.p));
		} catch (IllegalArgumentException e) {
		}
	}
	/*
	 * Method to create a player.	
	 */
	public void createPlayer() throws FileNotFoundException {
		this.p = new Player(-1, "Donald", new Avatar());
		this.list = (ArrayList<Avatar>) this.p.getAvatar().getAllAvatars();
	}

}
