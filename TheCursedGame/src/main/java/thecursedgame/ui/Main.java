package thecursedgame.ui;

import java.util.HashMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import thecursedgame.domain.Player;

public class Main extends Application {

	HashMap<KeyCode, Boolean> pressedButtons = new HashMap<>();
	public static void main(String[] args) {
		launch(Main.class);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("TheCursedGame");
		Player player = new Player();
		
		Group gp = new Group();
		gp.getChildren().add(player.model());
		
		Scene scene = new Scene(gp, 800, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		AnimationTimer timer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				update(player.model(), player);
				addKeyHandler(scene, player.model());
			}
		};
		timer.start();
	}

	
	public void addKeyHandler(Scene scene, Rectangle r) {
		scene.setOnKeyPressed(event -> {
			pressedButtons.put(event.getCode(), Boolean.TRUE);
		});
		scene.setOnKeyReleased(event -> {
			pressedButtons.put(event.getCode(), Boolean.FALSE);
		});
	}
	
	
	public void update(Rectangle r, Player p) {
		Double y = r.getY();
		if (r.getY()<485) {
			r.setY(y+1);
		}
		if (r.getY() == 485) {
			p.setOnAir(false);
		}
		if (pressedButtons.getOrDefault(KeyCode.A, false)) {
			r.setX(r.getX() -2);
		}
		if (pressedButtons.getOrDefault(KeyCode.D, false)) {
			r.setX(r.getX()+2);
		}
		if (pressedButtons.getOrDefault(KeyCode.SPACE, false)) {
			//if (p.getOnAir() == false) r.setY(r.getY()-57);
			p.jump();
			p.setOnAir(true);
		}
		
	}

}






