package thecursedgame.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.Node;
import thecursedgame.domain.Player;

public class PlayerTest {
	Player p;
	ArrayList<Node> list;
	
	@Before
	public void setUp() {
		p = new Player(15,15);
		list = new ArrayList<Node>();	
	}
	@Test
	public void playerCanDie() {
		p.die();
		assertEquals(1, p.getDeaths());
	}
	@Test
	public void updatePositonWorks() {
		p.updatePosition(100, 200);
		
	}
	@Test
	public void getAliveWorks() {
		assertEquals(true, p.getAlive());
	}
	@Test
	public void getOnAirWorks() {
		assertEquals(true, p.getOnAir());
	}
	@Test
	public void setOnAirWorks() {
		p.setOnAir(false);
		assertEquals(false, p.getOnAir());
	}
	@Test
	public void playerCanJump() {
		p.setOnAir(false);
		p.jump();
		assertEquals(true, p.getOnAir());
	}
	
}
