package thecursedgame.domain;

public class Level {
	private String[] layout;
	private int x;
	private int y;
	
	public Level(String[] layout, int x, int y) {
		this.layout = layout;
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public String[] getLayout() {
		return this.layout;
	}
}

