package thecursedgame.ui;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import thecursedgame.domain.Levels;
import thecursedgame.domain.Player;

public class Main extends Application {
	ArrayList<Node> platforms = new ArrayList<>();
	ArrayList<Node> savePoints = new ArrayList<>();
	ArrayList<Node> extraJumps = new ArrayList<>();
	ArrayList<Node> speedBoosters = new ArrayList<>();
	Levels levels = new Levels();
	private int currentLevel = 2;
	Group gameGroup;
	Player player;
	ArrayList<Node> traps = new ArrayList<>();
	Scene gameScene;
	Scene menuScene;
	HashMap<KeyCode, Boolean> pressedButtons = new HashMap<>();
	int counter = 0;
	int speed = 1;
	int speedCounter = 0;
	
	public static void main(String[] args) {
		launch(Main.class);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Main games setup
		primaryStage.setResizable(false);
		primaryStage.setTitle("TheCursedGame");
		player = new Player(20, 400); // change to 20,400
		gameGroup = new Group();
		gameGroup.getChildren().add(player.model());
		createMap(gameGroup, levels.getLevel(currentLevel));
		// menu setup
		Group menuGroup = new Group();
		BorderPane borderPane = new BorderPane();
		borderPane.setPrefSize(800, 500);

		//TextField text = new TextField("hehexd");
		Button newGame = new Button("New Game");
		
		borderPane.setCenter(newGame);
		//borderPane.setTop(text);
		
		menuScene = new Scene(borderPane);
		newGame.setOnAction(e -> {
			primaryStage.setScene(gameScene);
		});
		
		gameScene = new Scene(gameGroup, 800, 500);
		primaryStage.setScene(menuScene);
		primaryStage.show();
		
		

		AnimationTimer timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				update(player.model(), player, primaryStage);
				addKeyHandler(gameScene, player.model());
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
	
	boolean megajump = false;
	int count = 0;
	public void update(Rectangle r, Player p, Stage stage) {
		
		if (pressedButtons.getOrDefault(KeyCode.A, false)) {
			r.setTranslateX(r.getTranslateX() - speed);
		}
		if (pressedButtons.getOrDefault(KeyCode.D, false)) {
			r.setTranslateX(r.getTranslateX() + speed);
		}
		if (pressedButtons.getOrDefault(KeyCode.SPACE, false)) {
			if (p.getOnAir() == false) {
				megajump = true;
			}
			p.setOnAir(true);
		}
		// cheatmode flying for testing purposes
		if (pressedButtons.getOrDefault(KeyCode.F, false)) {
			r.setTranslateY(r.getTranslateY()-4);
		}

		// jumping done here
		if (megajump) {
			r.setTranslateY(r.getTranslateY() -2);
			count++;
			if (count > 35) {
				megajump = false;
				count = 0;
			}
		} else {
			r.setTranslateY(r.getTranslateY() + 1);
		}
		// doesn't allow player to go out of bounds
		if (r.getTranslateX() < 0) {
			r.setTranslateX(0);
		}
		if (r.getTranslateX()>785) {
			r.setTranslateX(785);
		}
		for (Node j : extraJumps) {
			if (r.getBoundsInParent().intersects(j.getBoundsInParent())) {
				//megajump = true; this makes a trampoline : D might use it later
				if (j.isVisible()) {
					p.setOnAir(false);
					j.setVisible(false);
				}
			}
		}
		for (Node s : speedBoosters) {
			if (r.getBoundsInParent().intersects(s.getBoundsInParent())) {
				if (s.isVisible()) {
					speed = speed+1;
					s.setVisible(false);
					speedCounter = 0;
				}
			}
		}
		// checks if player is hit by a trap
		for (Node t : traps) {
			if (r.getBoundsInParent().intersects(t.getBoundsInParent())) {
				player.die(extraJumps);
			}
		}
				// checks the hitboxes
		for (Node platform : platforms) {
			if (r.getBoundsInParent().intersects(platform.getBoundsInParent())) {
				
				// right side hitbox of the platforms
				if((pressedButtons.getOrDefault(KeyCode.A, false))
								&& r.getBoundsInParent().intersects(platform.getBoundsInParent().getMinX()+1,platform.getBoundsInParent().getMinY()+2, 19,15)) {
					r.setTranslateX(platform.getTranslateX()+21);
					p.setOnAir(true);
					return;

				// left side hitbox of the platforms
				} else if ((pressedButtons.getOrDefault(KeyCode.D, false))
						&& r.getBoundsInParent().intersects(platform.getBoundsInParent().getMinX()-1,platform.getBoundsInParent().getMinY()+2, 18,15)) {
					r.setTranslateX(platform.getTranslateX()-16);
					p.setOnAir(true);
					return;
					
				}
				//lower hitbox of the platform
				else if (r.getBoundsInParent().intersects(platform.getBoundsInParent().getMinX()-1,platform.getBoundsInParent().getMaxY()+1, 21,1)) {
					r.setTranslateY(platform.getTranslateY() + 20);
					megajump = false;
					count = 0;
					return;
				}
				// upper hitbox for platforms there is a bug that allows player to get through the ground if pressing A or D in a corner...
				else if (r.getBoundsInParent().intersects(platform.getBoundsInParent().getMinX()-1,platform.getBoundsInParent().getMinY()+1, 21,2)) {
					p.setOnAir(false);
					r.setTranslateY(platform.getTranslateY() - 15);
					return;
				}
					return;
			}
			
		}
		// checks if player gets hit by a checkpoint and goes to next level
		for (Node s : savePoints) {
			if (r.getBoundsInParent().intersects(s.getBoundsInParent())) {
				currentLevel++;
				Group nextGp = new Group();
				nextGp.getChildren().add(player.model());
				createMap(nextGp, levels.getLevel(currentLevel));
				player.updatePosition(20, (int) s.getBoundsInParent().getMinY());
				gameScene = new Scene(nextGp, 800, 500);
				stage.setScene(gameScene);

			}
		}
		// if player falls out of the map they die
		if (r.getTranslateY() > 500) {
			p.die(extraJumps);
		}
		counter++;
		speedCounter++;
		if (speedCounter >500) {
			speed = 1;
			speedCounter = 0;
		}
		if (counter > 500) {
			for (Node j : extraJumps) {
				j.setVisible(true);
				
			}
			for (Node s : speedBoosters) {
				s.setVisible(true);
			}
			counter = 0;
		}
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
					Node platform = createEntity(ii * 20, i * 20, 20, 20, Color.GREEN);
					g.getChildren().add(platform);
					platforms.add(platform);
					break;
				case '2':
					Node savePoint = createEntity(ii * 20, i * 20, 10, 10, Color.BLUE);
					g.getChildren().add(savePoint);
					savePoints.add(savePoint);
					break;
				case '3' :
					Node trap = createEntity(ii*20, i*20, 20, 20, Color.BLACK);
					g.getChildren().add(trap);
					traps.add(trap);
					break;
				case '5' :
					Node trap2 = createEntity(ii*20, i*21, 10, 20, Color.BLACK);
					g.getChildren().add(trap2);
					traps.add(trap2);
					break;
				case '4':
					Node extraJump = createEntity(ii * 20, i * 20, 5, 5, Color.DEEPPINK);
					g.getChildren().add(extraJump);
					extraJumps.add(extraJump);
					break;
				case '6':
					Node speedBoost = createEntity(ii * 20, i * 20, 5, 5, Color.DEEPSKYBLUE);
					g.getChildren().add(speedBoost);
					speedBoosters.add(speedBoost);
					break;
				}
			}
		}
	}
	// create any kind of entity for the map
	public Node createEntity(int x, int y, int w, int h, Color color) {
		Rectangle entity = new Rectangle(w, h);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(color);
		return entity;

	}

}
