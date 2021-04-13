package thecursedgame.domain;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class Player {
	private boolean alive;
	private Rectangle model;
	private boolean onAir;
	private Point2D movement;
	
	public Player() {
		this.alive = true;
		this.onAir = true;
		this.model = new Rectangle(15,15);
		this.movement = new Point2D(0, 0);
	}
	public boolean die() {
		return this.alive = false;
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
		if (this.onAir == false) this.model.setY(this.model.getY()-57);
	}
}
