package thecursedgame.domain;

import javafx.scene.shape.Rectangle;

public class Player {
	private boolean alive;
	private Rectangle model;
	private boolean onAir;
	private int x;
	private int y;
	private int deaths;
	
	public Player(int x, int y) {
		this.alive = true;
		this.onAir = true;
		this.x = x;
		this.y = y;
		this.model = new Rectangle(15,15);
		this.model.setTranslateX(x);
		this.model.setTranslateY(y);
	}
	/**
	 * Makes the model teleport to coordinates x and y
	 * Also adds 1 to the death counter every time it's called
	 */
	public void die() {
		this.model.setTranslateX(this.x);
		this.model.setTranslateY(this.y);
		deaths++;
	}
	public Rectangle model() {
		return this.model;
	}
	public boolean getOnAir() {
		return this.onAir;
	}
	public void setOnAir(boolean b) {
		this.onAir = b;
	}
	public boolean getAlive() {
		return this.alive;
	}
	/**
	 * Updates the characters position on x-y coordinates
	 * @param x integer value of x coordinate where the position of the character is updated
	 * @param y integer value of y coordinate where the position of the character is updated
	 */
	public void updatePosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.model.setTranslateX(x);
		this.model.setTranslateY(y);
	}
	/**
	 * Checks the players deaths
	 * @return the amount of deaths as Integer
	 */
	public int getDeaths() {
		return this.deaths;
	}
	/**
	 * Sets the players deaths
	 * @param newDeaths The amount of deaths you want to set
	 */
	public void setDeaths(int newDeaths) {
		this.deaths = newDeaths;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
}
