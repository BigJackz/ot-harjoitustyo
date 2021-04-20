package thecursedgame.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import thecursedgame.domain.Levels;

public class LevelsTest {
	Levels levels;

	@Before
	public void setUp() {
		levels = new Levels();
	}

	@Test
	public void levelSizeIsRight() {
		assertEquals(3, levels.getSize());
	}
}
