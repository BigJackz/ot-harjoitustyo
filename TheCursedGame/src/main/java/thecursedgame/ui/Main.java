package thecursedgame.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
	ArrayList<Node> trampolines = new ArrayList<>();
	Levels levels = new Levels();
	private int currentLevel = 0;
	Group gameGroup;
	Player player;
	ArrayList<Node> traps = new ArrayList<>();
	Scene gameScene;
	Scene menuScene;
	HashMap<KeyCode, Boolean> pressedButtons = new HashMap<>();
	int counter = 0;
	final int originalSpeed = 2;
	int speed = 2;
	int speedCounter = 0;
	
	public static void main(String[] args) {
		launch(Main.class);
	}
	
	public void doit() {
		player = new Player(levels.getLevelInfo(currentLevel).getX(), levels.getLevelInfo(currentLevel).getY());
		gameGroup = new Group();
		gameGroup.getChildren().add(player.model());
		createMap(gameGroup, levels.getLevel(currentLevel));
		gameScene = new Scene(gameGroup, 800, 500);
	}
	@Override
	public void start(Stage primaryStage) {
		// Main games setup
		primaryStage.setResizable(false);
		primaryStage.setTitle("TheCursedGame");

		// menu setup
		BorderPane borderPane = new BorderPane();
		//borderPane.setPrefSize(800, 500);
		VBox box = new VBox();
		//box.setPrefSize(800, 500);
		borderPane.setCenter(box);
		
		
		//TextField text = new TextField("hehexd");
		Button newGame = new Button("New Game ");
		Button loadGame = new Button("Load Game");
		box.setLayoutX(400);
		box.setLayoutY(250);
		box.setPadding(new Insets(250));
		box.setSpacing(20);
		box.getChildren().add(newGame);
		box.getChildren().add(loadGame);
		
		menuScene = new Scene(borderPane);
		newGame.setOnAction(e -> {
			
			primaryStage.setScene(gameScene);
		});
		
		loadGame.setOnAction(e -> {
			try {
				currentLevel = loadGame();
				doit();
				player.setDeaths(savedDeaths());
			}catch (IOException e1) {
				e1.printStackTrace();
			}
			primaryStage.setScene(gameScene);
		});

		primaryStage.setScene(menuScene);
		primaryStage.show();
		doit();
		

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
			r.setTranslateY(r.getTranslateY()-6);
		}

		// jumping done here
		if (megajump) {
			r.setTranslateY(r.getTranslateY() -2);
			count++;
			if (count > 32) {
				megajump = false;
				count = 0;
			}
		} else {
			r.setTranslateY(r.getTranslateY() + 2);
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
				die(player);
			}
		}
				// checks the hitboxes
		for (Node platform : platforms) {
			if (r.getBoundsInParent().intersects(platform.getBoundsInParent())) {
				
				// right side hitbox of the platforms
				if((pressedButtons.getOrDefault(KeyCode.A, false))
								&& r.getBoundsInParent().intersects(platform.getBoundsInParent().getMinX()+21,platform.getBoundsInParent().getMinY()+3, 18,14)) {
					r.setTranslateX(platform.getTranslateX()+21);
					p.setOnAir(true);
					return;

				// left side hitbox of the platforms
				} else if ((pressedButtons.getOrDefault(KeyCode.D, false))
						&& r.getBoundsInParent().intersects(platform.getBoundsInParent().getMinX()-1,platform.getBoundsInParent().getMinY()+3, 18,14)) {
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
				saveGame(player);
				Group nextGp = new Group();
				nextGp.getChildren().add(player.model());
				createMap(nextGp, levels.getLevel(currentLevel));
				player.updatePosition(levels.getLevelInfo(currentLevel).getX(),levels.getLevelInfo(currentLevel).getY()); //(20, (int) s.getBoundsInParent().getMinY());
				gameScene = new Scene(nextGp, 800, 500);
				stage.setScene(gameScene);

			}
		}
		for (Node t : trampolines) {
			if (r.getBoundsInParent().intersects(t.getBoundsInParent())) {
				megajump = true;
			}
		}
		// if player falls out of the map they die
		if (r.getTranslateY() > 500) {
			die(player);
		}
		counter++;
		speedCounter++;
		if (speedCounter >400) {
			speed = originalSpeed;
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
				case '7':
					Node trampoline = createEntity(ii*20, i*20, 13, 4, Color.DARKORCHID);
					g.getChildren().add(trampoline);
					trampolines.add(trampoline);
					break;
				}
			}
		}
	}
	// create rectangle entity for the map
	public Node createEntity(int x, int y, int w, int h, Color color) {
		Rectangle entity = new Rectangle(w, h);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(color);
		return entity;
	}
	
	
	// Saving the game to 2 files with character in the file
	String separator = File.separator;
	File saves = new File("src" + separator + "main" + separator + "java" + separator + "files" + separator + "save.txt");
	File deaths = new File("src" + separator + "main" + separator + "java" + separator + "files" + separator + "deaths.txt");
	public void saveGame(Player p) {
		FileWriter fr;
		FileWriter frDeaths;
		try {
			fr = new FileWriter(saves);
			frDeaths = new FileWriter(deaths);
			frDeaths.write(String.valueOf(p.getDeaths()));
			frDeaths.close();
			fr.write(String.valueOf(currentLevel));
			fr.close();
		} catch (IOException e) {
			System.out.println("Somethings wrong I can feel it: " + e.getMessage());
			e.printStackTrace();
		}

	}
	// Loading the level where you left if you load a game
	public int loadGame() throws IOException {
		FileReader fr = new FileReader(saves);
		BufferedReader br = new BufferedReader(fr);
		String map = br.readLine();
		int levelNumber = Integer.valueOf(map);
		br.close();
		return levelNumber;
	}
	// Loading the deaths you had if you load a game
	/**
	 * Reads the amount of deaths from a text file containing 1 integer
	 * @return The amount of deaths the person had last time if loading a game as integer
	 * @throws IOException
	 */
	public int savedDeaths() throws IOException {
		FileReader frDeaths = new FileReader(deaths);
		BufferedReader brDeaths = new BufferedReader(frDeaths);
		String deaths = brDeaths.readLine();
		int deathss = Integer.valueOf(deaths);
		brDeaths.close();
		return deathss;
	}
	// makes the player dead, since I might add new stuff such as the speed boost I want to be able to reset them here
    /**
     * Makes the player die and removes any buffs that the player might have
     * @param Player Instance of Player
     * @see thecursedgame.domain.Player.java
     */
	public void die(Player p) {
		p.die();
		speed = originalSpeed;
		for (Node s : speedBoosters) {
			s.setVisible(true);
		}
		for (Node j : extraJumps) {
			j.setVisible(true);
		}
		counter = 0;
	}
	
	

}
