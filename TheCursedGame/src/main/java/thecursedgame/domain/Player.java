package thecursedgame.domain;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.Node;
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
	public void jump() {
		//this.model.setTranslateY(this.model.getTranslateY() + this.movement.getY());
		if (this.onAir == false) this.model.setTranslateY(this.model.getTranslateY()-60);
		this.onAir = true;
	}
	public void jump2() {
		this.model.setTranslateY(this.model.getTranslateY()-57);
	}
	public void updatePosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.model.setTranslateX(x);
		this.model.setTranslateY(y);
	}
	public int getDeaths() {
		return this.deaths;
	}
	public void setDeaths(int newDeaths) {
		this.deaths = newDeaths;
	}
}
