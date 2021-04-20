package thecursedgame.ui;

import java.sql.Savepoint;
import java.util.ArrayList;
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
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import thecursedgame.domain.Levels;
import thecursedgame.domain.Player;

public class Main extends Application {
	ArrayList<Node> platforms = new ArrayList<>();
	ArrayList<Node> savePoints = new ArrayList<>();
	Levels levels = new Levels();
	private int currentLevel = 0;
	Group gp;
	Player player;
	ArrayList<Node> traps = new ArrayList<>();
	Scene scene;

	HashMap<KeyCode, Boolean> pressedButtons = new HashMap<>();

	public static void main(String[] args) {
		launch(Main.class);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("TheCursedGame");
		player = new Player(20, 400);

		gp = new Group();
		gp.getChildren().add(player.model());

		createMap(gp, levels.getLevel(0));

		scene = new Scene(gp, 800, 500);
		primaryStage.setScene(scene);
		primaryStage.show();

		
		

		AnimationTimer timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				update(player.model(), player, primaryStage);
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

	public void update(Rectangle r, Player p, Stage stage) {
		System.out.println("y: " + r.getTranslateY() + " x: " + r.getTranslateX() + " on air? : " + p.getOnAir());
		if (pressedButtons.getOrDefault(KeyCode.A, false)) {
			r.setTranslateX(r.getTranslateX() - 2);
		}
		if (pressedButtons.getOrDefault(KeyCode.D, false)) {
			r.setTranslateX(r.getTranslateX() + 2);
		}
		if (pressedButtons.getOrDefault(KeyCode.SPACE, false)) {
			// if (p.getOnAir() == false) r.setY(r.getY()-57);
			p.jump();
			p.setOnAir(true);
		}

		Double past = r.getTranslateY();
		System.out.println(past);
		for (Node platform : platforms) {
			if (r.getBoundsInParent().intersects(platform.getBoundsInParent())) {
				p.setOnAir(false);
				if (!past.equals(r.getTranslateY())) {

					r.setTranslateY(platform.getTranslateY() - 15.6);

					return;

				} else if (pressedButtons.getOrDefault(KeyCode.A, false)
						|| pressedButtons.getOrDefault(KeyCode.D, false)
								&& r.getBoundsInParent().intersects(platform.getBoundsInParent())) {
					r.setTranslateX(r.getTranslateX());
					return;
				} else {
					return;
				}
			}

		}
		for (Node s : savePoints) {
			if (r.getBoundsInParent().intersects(s.getBoundsInParent())) {
				System.out.println("olet viineri");
				currentLevel++;
				/*GridPane gridPane = new GridPane();
				Group group = new Group();
				TextArea text = new TextArea("Olet viineri!");
				gridPane.add(text, 400, 250);
				group.getChildren().add(gridPane);
				Scene winner = new Scene(group, 800, 500);
				if (currentLevel >= levels.getSize()) {
					primaryStage.setScene(winner);
				} else {*/
				Group nextGp = new Group();
				nextGp.getChildren().add(player.model());
				createMap(nextGp, levels.getLevel(currentLevel));
				player.updatePosition(20, 50);
				scene = new Scene(nextGp, 800, 500);
				stage.setScene(scene);
				
				//}

			}
		}
		for (Node t : traps) {
			if (r.getBoundsInParent().intersects(t.getBoundsInParent())) {
				player.die();
			}
		}
		if (r.getTranslateY() > 500) {
			p.die();
		}

		r.setTranslateY(r.getTranslateY() + 1);
	}

	// creates the map layout
	public void createMap(Group g, String[] level) {
		platforms.clear();
		savePoints.clear();
		traps.clear();
		for (int i = 0; i < level.length; i++) {
			String s = level[i];
			for (int ii = 0; ii < s.length(); ii++) {
				switch (s.charAt(ii)) {
				case '0':
					break;
				case '1':
					Node platform = createPlatform(ii * 20, i * 20, 20, 20, Color.RED);
					g.getChildren().add(platform);
					break;
				case '2':
					Node savePoint = createEntity2(ii * 20, i * 20, 10, 10, Color.AQUA);
					g.getChildren().add(savePoint);
					savePoints.add(savePoint);
					break;
				case '3' :
					Node trap = createEntity2(ii*20, ii*20, 15, 15, Color.BLACK);
					g.getChildren().add(trap);
					traps.add(trap);
					break;
				}
			}
		}
	}

	// creates platform entitys
	public Node createPlatform(int x, int y, int w, int h, Color color) {
		Rectangle entity = new Rectangle(w, h);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(color);
		platforms.add(entity);
		return entity;
	}

	// create other entitys these 2 creates will be merged at some point I hope
	public Node createEntity2(int x, int y, int w, int h, Color color) {
		Rectangle entity = new Rectangle(w, h);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(color);
		return entity;

	}

}
