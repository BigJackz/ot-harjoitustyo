package thecursedgame.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import thecursedgame.domain.Player;

public class PlayerTest {
	Player p;
	
	@Before
	public void setUp() {
		p = new Player();
	}
	@Test
	public void playerCanDie() {
		p.die();
		assertEquals(false, p.getAlive());
	}
}
