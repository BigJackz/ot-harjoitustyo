package thecursedgame.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import thecursedgame.domain.Levels;
import thecursedgame.domain.Player;

public class Main extends Application {
	ArrayList<Node> platforms = new ArrayList<>();
	ArrayList<Node> savePoints = new ArrayList<>();
	ArrayList<Node> extraJumps = new ArrayList<>();
	ArrayList<Node> speedBoosters = new ArrayList<>();
	ArrayList<Node> jumpBoosters = new ArrayList<>();
	ArrayList<Node> trampolines = new ArrayList<>();
	ArrayList<Node> teleports = new ArrayList<>();
	ArrayList<Node> keys = new ArrayList<>();
	ArrayList<Node> doors = new ArrayList<>();
	ArrayList<Node> checkPoints = new ArrayList<>();
	ArrayList<Node> goals = new ArrayList<>();
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
	int jumpCounter = 0;
	final int originalJumpAmount = 32;
	int jumpAmount = 32;
	boolean turboJump = false;
	int timerMilliSeconds = 0;
	int timerSeconds;
	int timerMinutes = 0;
	boolean canSave = false;
	BorderPane BP;
	Text timer;
	Group endScreenGroup;
	Scene endScene;
	BorderPane endBorder;
	String name;
	BorderPane borderPane;
	int finishingTime = 0;
	public static void main(String[] args) {
		launch(Main.class);
	}

	/**
	 * Initialaizes some content
	 */
	public void init() {
		player = new Player(levels.getLevel(currentLevel).getX(), levels.getLevel(currentLevel).getY());
		gameGroup = new Group();
		gameGroup.getChildren().add(player.model());
		BP = new BorderPane();
		timer = new Text();
		BP.setRight(timer);
		gameGroup.getChildren().add(BP);
		createMap(gameGroup, levels.getLevelLayout(currentLevel));
		gameScene = new Scene(gameGroup, 800, 500);
		endBorder = new BorderPane();
		endBorder.setPrefSize(800, 500);
	}

	@Override
	public void start(Stage primaryStage) {
		// Main games setup
		primaryStage.setResizable(false);
		primaryStage.setTitle("TheCursedGame");
		borderPane = new BorderPane();
		VBox box = new VBox();
		borderPane.setCenter(box);

		Button newGame = new Button("New Game ");
		Button loadGame = new Button("Load Game");
		Button hiscores = new Button("Hiscores");
		box.setLayoutX(400);
		box.setLayoutY(250);
		box.setPadding(new Insets(250));
		box.setSpacing(20);
		box.getChildren().add(newGame);
		box.getChildren().add(loadGame);
		box.getChildren().add(hiscores);
		menuScene = new Scene(borderPane);
		newGame.setOnAction(e -> {
			timerMilliSeconds = 0;
			canSave = true;
			init();
			primaryStage.setScene(gameScene);
		});
		hiscores.setOnAction(e-> {
			hiscores(primaryStage);
		});
		loadGame.setOnAction(e -> {
			try {
				currentLevel = loadGame();
				timerMilliSeconds = loadTime();
				init();
				canSave = true;
				player.setDeaths(savedDeaths());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			primaryStage.setScene(gameScene);
		});

		primaryStage.setScene(menuScene);
		primaryStage.show();
		init();

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

	double x = 0;
	double y = 0;
	boolean megajump = false;
	int count = 0;
/**
 * Updates the game in the following
 * @param r Rectangle usually player character that we use all hitbox calculations
 * @param p Player
 * @param stage Stage where the game currently is
 */
	public void update(Rectangle r, Player p, Stage stage) {
		updateTimer();
		//checks the registered buttons and acts accordingly
		pressedButtons(p,r);
		//autosaves the game
		autoSave();
		// jumping and gravity
		jumpAndGravity(r);
		// doesn't allow player to go out of bounds
		outOfBounds(r);
		//checks if the player gets hit by extra jump
		hitBoxCheckExtraJump(p,r);
		//checks if the player gets hit by speed booster
		hitBoxCheckSpeedBoost(r);
		// checks if player gets hit by a trap and kills them
		shouldPlayerDie(r);
		//checks if the player gets hit by extra jump
		hitBoxCheck(p, r);
		//checks if the player gets hit by teleport
		hitBoxCheckTeleports(r);
		// checks if player gets hit by a checkpoint and goes to next level
		save(stage,r);
		//checks if the player gets hit by trampolines
		hitBoxCheckTrampolines(r);
		//checks if the player gets hit by jump booster
		hitBoxCheckJumpBoost(r);
		//checks if the player gets hit by keys and collects them
		hitBoxCheckKeys(r);
		//checks if the player gets hit by checkpoint
		hitBoxCheckCheckPoint(p,r);
		// if player falls out of the map they die
		fallsOutOfMap(r);
		//checks if the player gets hit by end goal
		hitBoxCheckGoal(r, stage);
		//resets the boosters
		boosterTimers();
	}
	/**
	 * Creates a basic platform that you can jump and walk on
	 * @param g Group where it is sent
	 * @param ii Width of the level
	 * @param i Height of the level
	 * @param C Colour of the platform
	 */
	public void createPlatform(Group g, int ii, int i, Color C) {
		Node platform = createEntity(ii * 20, i * 20, 20, 20, C);
		g.getChildren().add(platform);
		platforms.add(platform);
	}

	/**
	 * Creates the map layout
	 * @param g Group where the layout will be sent
	 * @param level a String[] where the layout is based on
	 */
	public void createMap(Group g, String[] level) {
		clearPreviousLevel();
		for (int i = 0; i < level.length; i++) {
			String s = level[i];
			for (int ii = 0; ii < s.length(); ii++) {
				switch (s.charAt(ii)) {
				case '0':
					break;
				case '1':
					createPlatform(g,ii,i, Color.GREEN);
					break;
				case '2':
					Node savePoint = createEntity(ii * 20, i * 20, 10, 10, Color.BLUE);
					g.getChildren().add(savePoint);
					savePoints.add(savePoint);
					break;
				case '3':
					Node trap = createEntity(ii * 20, i * 20, 20, 20, Color.BLACK);
					g.getChildren().add(trap);
					traps.add(trap);
					break;
				case '5':
					Node trap2 = createEntity(ii * 20, i * 21, 10, 20, Color.BLACK);
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
					Node trampoline = createEntity(ii * 20, i * 20, 13, 4, Color.DARKORCHID);
					g.getChildren().add(trampoline);
					trampolines.add(trampoline);
					break;
				case '8':
					Node teleport = createEntity(ii * 20, i * 20, 15, 15, Color.PURPLE);
					g.getChildren().add(teleport);
					teleports.add(teleport);
					break;
				case '9':
					Node key = createEntity(ii * 20, i * 20, 10, 10, Color.GOLD);
					g.getChildren().add(key);
					keys.add(key);
					break;
				case 'm':
					Node goal = createEntity(ii * 20, i * 20, 20, 20, Color.FUCHSIA);
					g.getChildren().add(goal);
					goals.add(goal);
					break;
				case '+':
					Node door = createEntity(ii * 20, i * 20, 10, 40, Color.BROWN);
					g.getChildren().add(door);
					doors.add(door);
					traps.add(door);
					break;
				case 'j':
					Node jumpBooster = createEntity(ii * 20, i * 20, 5, 5, Color.DARKRED);
					g.getChildren().add(jumpBooster);
					jumpBoosters.add(jumpBooster);
					break;
				case 'w':
					Node waterTrap = createEntity(ii * 20, i * 20, 20, 20, Color.AQUA);
					g.getChildren().add(waterTrap);
					traps.add(waterTrap);
					break;
				case 'c':
					Node checkPoint = createEntity(ii * 20, i * 20, 20, 10, Color.CORAL);
					g.getChildren().add(checkPoint);
					checkPoints.add(checkPoint);
					break;
				case 'g':
					createPlatform(g,ii,i, Color.DARKGREEN);

				}
				
			}
		}
	}

	/**
	 * Creates a new entity for the map
	 * @param x X coordinate of the new entity
	 * @param y Y coordinate of the new entity
	 * @param w Width of the new entity
	 * @param h Height of the new entity
	 * @param color of the new entity
	 * @return The new entity
	 */
	public Node createEntity(int x, int y, int w, int h, Color color) {
		Rectangle entity = new Rectangle(w, h);
		entity.setTranslateX(x);
		entity.setTranslateY(y);
		entity.setFill(color);
		return entity;
	}

	// Saving the game to 3 files with  few characters in the file
	String separator = File.separator;
	File saves = new File(
			"src" + separator + "main" + separator + "java" + separator + "files" + separator + "save.txt");
	File deaths = new File(
			"src" + separator + "main" + separator + "java" + separator + "files" + separator + "deaths.txt");
	File time = new File("src" + separator + "main" + separator + "java" + separator + "files" + separator + "time.txt");
/**
 * Saves the game to the 3 game files
 * @param p Player whos deaths will be saved
 */
	public void saveGame(Player p) {
		FileWriter fr;
		FileWriter frDeaths;
		FileWriter frTime;
		try {
			fr = new FileWriter(saves);
			frDeaths = new FileWriter(deaths);
			frTime = new FileWriter(time);
			frDeaths.write(String.valueOf(p.getDeaths()));
			frDeaths.close();
			fr.write(String.valueOf(currentLevel));
			fr.close();
			frTime.write(String.valueOf(timerMilliSeconds));
			frTime.close();
		} catch (IOException e) {
			System.out.println("Somethings wrong I can feel it: " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Loads the level number from last session
	 * @return The levels number you last advanced to
	 * @throws IOException
	 */
	public int loadGame() throws IOException {
		FileReader fr = new FileReader(saves);
		BufferedReader br = new BufferedReader(fr);
		String map = br.readLine();
		int levelNumber = Integer.valueOf(map);
		br.close();
		return levelNumber;
	}
	/**
	 * Loads the time from the file
	 * @return the time from last session in milliseconds
	 * @throws IOException
	 */
	public int loadTime() throws IOException {
			FileReader fr = new FileReader(time);
			BufferedReader br = new BufferedReader(fr);
			String timeInMillis = br.readLine();
			int timeMillis = Integer.valueOf(timeInMillis);
			br.close();
			return timeMillis;

	}

	// Loading the deaths you had if you load a game
	/**
	 * Reads the amount of deaths from a text file containing 1 integer
	 * 
	 * @return The amount of deaths the person had last time if loading a game as
	 *         integer
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

	// makes the player dead, since I might add new stuff such as the speed boost I
	// want to be able to reset them here
	/**
	 * Makes the player die and removes any buffs that the player might have
	 * 
	 * @param Player Instance of Player
	 * @see thecursedgame.domain.Player.java
	 */
	public void die(Player p) {
		p.die();
		speed = originalSpeed;
		jumpAmount = originalJumpAmount;
		for (Node s : speedBoosters) {
			s.setVisible(true);
		}
		for (Node j : extraJumps) {
			j.setVisible(true);
		}
		for (Node j : jumpBoosters) {
			j.setVisible(true);
		}
		counter = 0;
	}
	/**
	 * Clears all the things from last last level
	 */
	public void clearPreviousLevel() {
		platforms.clear();
		savePoints.clear();
		traps.clear();
		speedBoosters.clear();
		trampolines.clear();
		teleports.clear();
		keys.clear();
		doors.clear();
		jumpBoosters.clear();
		checkPoints.clear();
	}
	/**
	 * Checks if rectangle has hit a trap and kills them
	 * @param r the rectangle which hitbox we calculate
	 */
	public void shouldPlayerDie(Rectangle r) {
		for (Node t : traps) {
			if (r.getBoundsInParent().intersects(t.getBoundsInParent())) {
				die(player);
			}
		}
	}
	/**
	 * Updates the time
	 */
	public void updateTimer() {
		timerMilliSeconds = timerMilliSeconds +1;
		timerSeconds = timerMilliSeconds/60;
		int secondsLeft = timerSeconds%60;
		timerMinutes = timerSeconds/60;
		timer.setText(String.valueOf(timerMinutes + ":" + secondsLeft));
	}
	/**
	 * Automatically saves the game on an interval
	 */
	public void autoSave() {
		if (timerMilliSeconds%17 == 0 && canSave) {
			saveGame(player);
		}
	}
	/**
	 * Checks if rectangle hits savepoint and saves the game and goes to next stage
	 * @param stage The stage where next level will be shown
	 * @param r the rectangle which hitbox we calculate
	 */
	public void save(Stage stage , Rectangle r) {
		for (Node s : savePoints) {
			if (r.getBoundsInParent().intersects(s.getBoundsInParent())) {
				s.setTranslateY(1000);
				currentLevel++;
				saveGame(player);
				Group nextGp = new Group();
				nextGp.getChildren().add(player.model());
				nextGp.getChildren().add(BP);
				createMap(nextGp, levels.getLevelLayout(currentLevel));
				player.updatePosition(levels.getLevel(currentLevel).getX(), levels.getLevel(currentLevel).getY());
				gameScene = new Scene(nextGp, 800, 500);
				stage.setScene(gameScene);
			}
		}
	}
	/**
	 * Checks if rectangle has hit any platform and keeps them from going inside one and resets the players jump
	 * @param p Player whos jump will be reset
	 * @param r the rectangle which hitbox we calculate
	 */
	public void hitBoxCheck(Player p, Rectangle r) {
		for (Node platform : platforms) {
			if (r.getBoundsInParent().intersects(platform.getBoundsInParent())) {

				// right side hitbox of the platforms
				if ((pressedButtons.getOrDefault(KeyCode.A, false))
						&& r.getBoundsInParent().intersects(platform.getBoundsInParent().getMinX() + 21,
								platform.getBoundsInParent().getMinY() + 3, 18, 14)) {
					r.setTranslateX(platform.getTranslateX() + 21);
					p.setOnAir(true);
					return;

					// left side hitbox of the platforms
				} else if ((pressedButtons.getOrDefault(KeyCode.D, false))
						&& r.getBoundsInParent().intersects(platform.getBoundsInParent().getMinX() - 1,
								platform.getBoundsInParent().getMinY() + 3, 18, 14)) {
					r.setTranslateX(platform.getTranslateX() - 16);
					p.setOnAir(true);
					return;

				}
				// lower hitbox of the platform
				else if (r.getBoundsInParent().intersects(platform.getBoundsInParent().getMinX() - 1,
						platform.getBoundsInParent().getMaxY() + 1, 21, 1)) {
					r.setTranslateY(platform.getTranslateY() + 20);
					megajump = false;
					count = 0;
					return;
				}
				// upper hitbox for platforms
				else if (r.getBoundsInParent().intersects(platform.getBoundsInParent().getMinX() - 1,
						platform.getBoundsInParent().getMinY() + 1, 21, 2)) {
					p.setOnAir(false);
					r.setTranslateY(platform.getTranslateY() - 15);
					return;
				}
				return;
			}

		}
	}
	/**
	 * Checks if rectangle has hit speed boosters and adds 1 to the speed
	 * @param r the rectangle which hitbox we calculate
	 */
	public void hitBoxCheckSpeedBoost(Rectangle r) {
		for (Node s : speedBoosters) {
			if (r.getBoundsInParent().intersects(s.getBoundsInParent())) {
				if (s.isVisible()) {
					speed = speed + 1;
					s.setVisible(false);
					speedCounter = 0;
				}
			}
		}
	}
	/**
	 * Checks if rectangle has hit extra jump and resets the jump so it allows it to happen again
	 * @param p Player that gets the jump reset
	 * @param r the rectangle which hitbox we calculate
	 */
	public void hitBoxCheckExtraJump(Player p, Rectangle r) {
		for (Node j : extraJumps) {
			if (r.getBoundsInParent().intersects(j.getBoundsInParent())) {
				if (j.isVisible()) {
					p.setOnAir(false);
					j.setVisible(false);
				}
			}
		}
	}
	/**
	 * Checks if rectangle has hit a teleport and teleports them to the location given by current level
	 * @param r the rectangle which hitbox we calculate
	 */
	public void hitBoxCheckTeleports(Rectangle r) {
		for (Node t : teleports) {
			if (r.getBoundsInParent().intersects(t.getBoundsInParent())) {
				r.setTranslateX(levels.getTpX(currentLevel));
				r.setTranslateY(levels.getTpY(currentLevel));
			}
		}
	}
	/**
	 * Checks if rectangle has hit a trampoline and makes it jump
	 * @param r the rectangle which hitbox we calculate
	 */
	public void hitBoxCheckTrampolines(Rectangle r) {
		for (Node t : trampolines) {
			if (r.getBoundsInParent().intersects(t.getBoundsInParent())) {
				megajump = true;
			}
		}
	}
	/**
	 * Checks if rectangle has hit keys to collect them
	 * @param r the rectangle which hitbox we calculate
	 */
	public void hitBoxCheckKeys(Rectangle r) {
		for (Node k : keys) {
			if (r.getBoundsInParent().intersects(k.getBoundsInParent())) {
				k.setVisible(false);
				k.setTranslateY(1000);
				levels.getLevel(currentLevel).removeKey();
				if (levels.getLevel(currentLevel).noRealKeysLeft()) {
					for (Node d : doors) {
						d.setVisible(false);
						d.setTranslateX(10000);
					}
				}
			}
		}
	}
	/**
	 * Checks if rectangle has hit a jump booster
	 * @param r the rectangle which hitbox we calculate
	 */
	public void hitBoxCheckJumpBoost(Rectangle r) {
		for (Node j : jumpBoosters) {
			if (r.getBoundsInParent().intersects(j.getBoundsInParent())) {
				if (j.isVisible()) {
					j.setVisible(false);
					jumpAmount = jumpAmount + 18;
					jumpCounter = 0;
				}
			}
		}
	}
	/**
	 * Produces constant gravity and allows jumping
	 * @param r the rectangle which hitbox we calculate
	 */
	public void jumpAndGravity(Rectangle r) {
		if (megajump) {
			r.setTranslateY(r.getTranslateY() - 2);
			count++;
			if (count > jumpAmount) {
				megajump = false;
				count = 0;
			}
		} else {
			r.setTranslateY(r.getTranslateY() + 2);
		}
	}
	/**
	 * Checks if rectangle r is out of bounds
	 * @param r the rectangle which hitbox we calculate
	 */
	public void outOfBounds(Rectangle r) {
		if (r.getTranslateX() < 0) {
			r.setTranslateX(0);
		}
		if (r.getTranslateX() > 785) {
			r.setTranslateX(785);
		}
	}
	/**
	 * Checks the pressed buttons and acts accordingly
	 * @param p Player p that these action affect
	 * @param r Rectangle r that these actions affect
	 */
	public void pressedButtons(Player p,Rectangle r) {
		if (pressedButtons.getOrDefault(KeyCode.R, false)) {
			p.die();
		}
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
	}
	/**
	 * checks the booster timers and resets them on an interval
	 */
	public void boosterTimers() {
		counter++;
		speedCounter++;
		jumpCounter++;
		if (speedCounter > 400) {
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
		if (jumpCounter > 300) {
			for (Node j : jumpBoosters) {
				j.setVisible(true);
			}
			jumpAmount = originalJumpAmount;
			jumpCounter = 0;
		}
	}
	/**
	 * Checks if rectangle r has hit a checkpoint
	 * @param p Player p to change their position
	 * @param r the rectangle which hitbox we calculate
	 */
	public void hitBoxCheckCheckPoint(Player p,Rectangle r) {
		for (Node c : checkPoints) {
			if (r.getBoundsInParent().intersects(c.getBoundsInParent())) {
				levels.getLevel(currentLevel).setX((int)(c.getTranslateX()));
				levels.getLevel(currentLevel).setY((int)c.getTranslateY());
				c.setVisible(false);
				p.setX((int)c.getTranslateX());
				p.setY((int)c.getTranslateY());
			}
		}
	}
	/**
	 * Checks if rectangle r has hit a goal
	 * @param r rectangle usually player
	 * @param stage the stage where this happens
	 */
	public void hitBoxCheckGoal(Rectangle r, Stage stage) {
		for (Node g : goals) {
			if (r.getBoundsInParent().intersects(g.getBoundsInParent())) {
				gameEnds(stage);
				finishingTime = timerMilliSeconds;
				timerMilliSeconds = 0;
				currentLevel = 0;
				saveGame(player);
				clearPreviousLevel();
				g.setTranslateY(1000);
				stage.setScene(endScene);
			}
		}
	}
	/**
	 * Checks if the rectangle r has fallen out of map and kills them
	 * @param r the rectangle usually player model
	 */
	public void fallsOutOfMap(Rectangle r) {
		if (r.getTranslateY() > 500) {
			die(player);
		}
	}
	/**
	 * Saves the results to the database for hiscores
	 */
	public void saveToDatabase() {
		try {
			Connection db = DriverManager.getConnection("jdbc:sqlite:src" + separator + "main" + separator + "java" + separator + "files" + separator + "hiscores.db");
			PreparedStatement p = db.prepareStatement("INSERT INTO hiscores (name, time, deaths) VALUES (" + "'" + name + "'" + "," + finishingTime + "," + player.getDeaths() + ");");
			p.execute();
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	/**
	 * Setups the hiscores
	 * @param stage Stage where the hiscores menu will be sent
	 */
	public void hiscores(Stage stage) {
		BorderPane hiscoreBorder = new BorderPane();
		hiscoreBorder.setPrefSize(800, 500);
		VBox scoreBox = new VBox();
		scoreBox.setLayoutX(400);
		scoreBox.setLayoutY(250);
		scoreBox.setPadding(new Insets(150));
		scoreBox.setSpacing(20);
		Text info = new Text("Hiscores!");
		Text nameInfo = new Text("Name:");
		Text timeAndDeathsInfo = new Text("Time");
		Text timeAndDeathsInfo2 = new Text("min:sec     deaths:");
		scoreBox.getChildren().add(info);
		scoreBox.getChildren().add(nameInfo);
		VBox scoreBox2 = new VBox();
		scoreBox2.setLayoutX(400);
		scoreBox2.setLayoutY(250);
		scoreBox2.setPadding(new Insets(150));
		scoreBox2.setSpacing(20);
		scoreBox2.getChildren().add(timeAndDeathsInfo);
		scoreBox2.getChildren().add(timeAndDeathsInfo2);
		HBox spacing = new HBox();
		spacing.setLayoutX(400);
		spacing.setLayoutY(250);
		spacing.setPadding(new Insets(50));
		spacing.setSpacing(5);
		try {
			Connection db = DriverManager.getConnection("jdbc:sqlite:src" + separator + "main" + separator + "java" + separator + "files" + separator + "hiscores.db");
			PreparedStatement p = db.prepareStatement("SELECT name, time, deaths FROM hiscores ORDER BY time ASC, deaths DESC LIMIT 10;");
			ResultSet r = p.executeQuery();
			int indeks = 1;
			while (r.next()) {
				int nameLength = r.getString("name").length();
				String space = "";
				int length = 50 - nameLength;
				for (int i = 0; i<length; i++) {
					space = space + " ";
				}
				String score1 = r.getString("name");
				int finalMinutes = r.getInt("time")/(60*60);
				int finalSeconds = r.getInt("time")%60;
				String score2 = (String.valueOf(finalMinutes + ":" + finalSeconds));
				String score3 = String.valueOf(r.getInt("deaths"));
				Text score = new Text(indeks + ": " + score1);
				Text score22 = new Text(score2 + "      " + score3);
				scoreBox.getChildren().add(score);
				scoreBox2.getChildren().add(score22);
				indeks++;
			}
		} catch (SQLException e1) {
			System.out.println("Error: " + e1.getMessage());
			e1.printStackTrace();
		}
		Button returnButton = new Button("Return to menu");
		scoreBox.getChildren().add(returnButton);
		returnButton.setOnAction(e -> {
			stage.setScene(menuScene);
		});
		spacing.getChildren().add(scoreBox);
		spacing.getChildren().add(scoreBox2);
		hiscoreBorder.setCenter(spacing);
		Scene hiscoreScene = new Scene(hiscoreBorder);
		stage.setScene(hiscoreScene);
	}
	/**
	 * Ends the game and creates the end game menu
	 * @param stage this is the stage where the menu will be sent
	 */
	public void gameEnds(Stage stage) {
		endBorder = new BorderPane();
		endBorder.setPrefSize(800, 500);
		Text gratz = new Text("Congratulations! You beat the game!");
		Text gratz2 = new Text("Type your name below for hiscore entry.");
		Button submit = new Button("Submit");

		TextField nameField = new TextField("CursedGamer");
		VBox endBox = new VBox();
		endBox.setLayoutX(400);
		endBox.setLayoutY(250);
		endBox.setPadding(new Insets(250));
		endBox.setSpacing(20);
		endBox.getChildren().add(gratz);
		endBox.getChildren().add(gratz2);
		endBox.getChildren().add(nameField);
		endBox.getChildren().add(submit);
		submit.setOnAction(e -> {
			name = nameField.getText();
			saveToDatabase();
			stage.setScene(menuScene);
			stage.show();
		});
		endBorder.setCenter(endBox);
		endScene = new Scene(endBorder);
	}
}
