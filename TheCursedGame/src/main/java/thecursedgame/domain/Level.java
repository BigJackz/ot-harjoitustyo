package thecursedgame.domain;

import java.util.ArrayDeque;

public class Level {
	private String[] layout;
	private int x;
	private int y;
	private TeleportLocation tp;
	private int keyAmount;
	private ArrayDeque<Integer> keys;
	public Level(String[] layout, int x, int y, int tpx, int tpy, int keyAmount) {
		this.layout = layout;
		this.x = x;
		this.y = y;
		this.keyAmount = keyAmount;
		this.tp = new TeleportLocation(tpx,tpy);
		keys = new ArrayDeque<Integer>();
		for (int i = 0; i<this.keyAmount+2; i++) {
			this.keys.add(i);
	}
	}
	public Level(String[] layout, int x, int y, int keyAmount) {
		this.layout = layout;
		this.x = x;
		this.y = y;
		this.keyAmount = keyAmount;
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * Returns this levels layout
	 * @return Level layout as String[]
	 */
	public String[] getLayout() {
		return this.layout;
	}
	public int getTpy() {
		return tp.getY();
	}
	public int getTpx() {
		return tp.getX();
	}
/**
 * Removes 1 key
 */
	public void removeKey() {
		this.keys.poll();
	}
	/**
	 * Checks if all the keys have been collected
	 * @return True if no keys left on the map else returns False
	 */
	public boolean noRealKeysLeft() {
		if (this.keys.size() == 2) {
			return true;
		}
		return false;
	}
}

