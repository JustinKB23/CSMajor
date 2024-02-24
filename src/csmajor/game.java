package csmajor;

import java.io.File;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class game extends Application {
	//Database reqis
	static java.sql.Connection connection = null;
	/*static String databaseName = "mydb";
	static String url = "jdbc:mysql://localhost:3306/" + databaseName + "?autoReconnect=true&useSSL=false";
	
	static String username = "root";
	static String password = "nairb1200";*/
	
	static String databaseName = "sql9337101";
	static String url = "jdbc:mysql://52.5.226.201:3306/" + databaseName;//+ "?autoReconnect=true&useSSL=false";
	
	static String username = "sql9337101";
	static String password = "rJrLIBUTGU";
	
	private static int Score = 0;
	
	Timer gameTimer = new Timer();
	Thread gameTime = new Thread(gameTimer);
	
	boolean dead = false;
	boolean started = false;
	
	Label pscore = new Label("");
	
	Media DeathmusicFile = new Media(new File("sounds/Death.wav").toURI().toString());
	MediaPlayer DeathMusic = new MediaPlayer(DeathmusicFile);
	
	
	//store the actual computer screen size
	private int screenWidth = 1366;//(int) Screen.getScreens().get(0).getBounds().getWidth();
	private int screenHeight = 768;//(int) Screen.getScreens().get(0).getBounds().getHeight();
	private int screenWidthreal = (int) Screen.getScreens().get(0).getBounds().getWidth();
	private int screenHeightreal = (int) Screen.getScreens().get(0).getBounds().getHeight();
	
	boolean spawnNewEnemies = false;
	
	MediaPlayer GameMusic;
	
	private Pane root = new Pane();
	private Pane menu = new Pane();
	private Pane ded = new Pane();
	Scene deathScene = new Scene(createDeadScreen());
	Label cords = new Label("X = 0, y = 0");
	Rectangle OuterHealthBar = new Rectangle(50, 10, Color.RED);
	Rectangle InnerHealthBar = new Rectangle(50, 10, Color.GREEN);

	Map map;
	//
	//ImageView dguniv;
	//
	private Sprite player = new Sprite(screenWidth/2,screenHeight/2,60,84,4,"player",new rangedWeapon(30,15,400,1),100);
	//private Sprite player = new Sprite(screenWidth/2,screenHeight/2,60,84,4,"player",new Melee(30,3),100);
	ArrayList<Sprite> enemies = new ArrayList<Sprite>(); 
	ArrayList<Projectile> sceneProjectiles = new ArrayList<Projectile>();
	ArrayList<Node> uiComponents = new ArrayList<Node>();
	ArrayList<Obstacle> mapBounds = new ArrayList<Obstacle>();
	ArrayList<Object> tbremoved = new ArrayList<Object>();
	ArrayList<Enemy> newEnemies = new ArrayList<Enemy>();


	@Override
	public void start(Stage stage) throws Exception {
		// main controls listener for all player commands
		Scene scene = new Scene(createContent(stage));
		Scene menuScene = new Scene(createMenu(stage,scene));
		
		scene.setOnMouseClicked(mouseHandler);
		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case A:
				//player.moveLeft();
				player.setMotion(3,true);
				break;
			case D:
				//player.moveRight();
				player.setMotion(1,true);
				break;
			case W:
				//player.moveUp();
				player.setMotion(0,true);
				break;
			case S:
				//player.moveDown();
				player.setMotion(2,true);
				break;
			case SHIFT:
				player.setMotion(4,true);
				break;
			default:
				break;
			}
		});
		
		scene.setOnKeyReleased(e -> {
			switch (e.getCode()) {
			case A:
				player.setMotion(3,false);
				break;
			case D:
				player.setMotion(1,false);
				break;
			case W:
				player.setMotion(0,false);
				break;
			case S:
				player.setMotion(2,false);
				break;
			case SHIFT:
				player.setMotion(4,false);
				break;
				
			default:
				break;
			}
		});
		
		stage.setScene(menuScene);
		stage.show();
		
	}
	private Parent createDeadScreen() {
		//display the main menu of the game
		ded.setPrefSize(screenWidth,screenHeight);
	    
		Image back = new Image(new File("art/ded.gif").toURI().toString());
		Image dead = new Image(new File("art/diedBanner.png").toURI().toString());
		ImageView background = new ImageView(back);
		ImageView banner = new ImageView(dead);
		Rectangle drop = new Rectangle(300,150,Color.BLACK);
		drop.setTranslateX((screenWidth/2)-170);
		drop.setTranslateY(((screenHeight/4)*3)-60);
		Label enter = new Label("Thanks for Playing!\nEnter name to submit score");
		enter.setTextFill(Color.WHITE);
		enter.setTranslateX((screenWidth/2)-130);
		enter.setTranslateY(((screenHeight/4)*3)-50);
		
		
		pscore.setTextFill(Color.WHITE);
		pscore.setTranslateX((screenWidth/2)-130);
		pscore.setTranslateY(((screenHeight/4)*3)+50);
		banner.setFitWidth(screenWidth);
		banner.setTranslateY((screenHeight/2)-50);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		TextField name = new TextField("Player");
		name.setPrefWidth(130);
		name.setTranslateX((screenWidth/2)-150);
		name.setTranslateY((screenHeight/4)*3);
		Button submit = new Button("Submit");
		submit.setTranslateX((screenWidth/2) + 40);
		submit.setTranslateY((screenHeight/4)*3);
		submit.setOnAction(e -> {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				connection = DriverManager.getConnection(url,username,password);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    System.out.println("Database connected");
		    java.sql.Statement statement = null;
			try {
				statement = connection.createStatement();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				statement = connection.createStatement();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    String line = "INSERT INTO sql9337101.csmajor (name,score,survivalTime) VALUES ('" + name.getText() + "','" + Integer.toString(Score) + "','" + Integer.toString(gameTimer.getMin()) + ":" + Integer.toString(gameTimer.getSeconds()) + "')";
		    try {
				statement.executeUpdate(line);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    
		    
			System.exit(0);
			}
		);
		ded.getChildren().addAll(background,drop,enter,submit,banner,name,pscore);
		
		return ded;
	}
	
	private Parent createMenu(Stage stage, Scene ms) throws SQLException, ClassNotFoundException {
		//display the main menu of the game
		menu.setPrefSize(screenWidth,screenHeight);
		System.out.println("coords: " + screenWidthreal + ", " + screenHeightreal);
		
		TextArea scores = new TextArea();
		//ListView<String> scores = new ListView<String>();
		//scores.getItems().add("Leaderboard:");
		//highscore list
		
		
		
		Class.forName("com.mysql.jdbc.Driver");
		// Load the JDBC driver
	    System.out.println("Driver loaded");
	    
		connection = DriverManager.getConnection(url,username,password);
		
		

	    System.out.println("Database connected");

	    // Create a statement
	    java.sql.Statement statement = connection.createStatement();

	    // Execute a statement
	    ResultSet resultSet = statement.executeQuery
	      ("SELECT name, score, survivalTime FROM sql9337101.csmajor ORDER BY score DESC;");
	    //ResultSet resultSet = statement.exequteQuery("INSERT INTO csmajor.highscores (name,score,survivalTime) VALUES ('bmano1200','21342354','30:40')");
	    
	    int i = 1;
	    String scoreline = "Leaderboard";
	    // Iterate through the result
	    while (resultSet.next()) {
	    	scoreline += String.format("\n %1s) %s \n     Score: %s \n    Time: %s", i, resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
	      //System.out.println(scoreline);
	      i++;
	    }
	    scores.setText(scoreline);

	    // Close the connection
	    connection.close();
	    
	    
	    
		scores.setTranslateX(screenWidth - 200);
		scores.setTranslateY(50);
		scores.setMinSize(150, screenHeight - 100);
		scores.setMaxSize(150, screenHeight - 100);
		Image back = new Image(new File("art/menu_background.png").toURI().toString());
		ImageView background = new ImageView(back);
		Media musicFile = new Media(new File("sounds/main.wav").toURI().toString());
		MediaPlayer GameMusic = new MediaPlayer(musicFile);
		//MediaView main = new MediaView(GameMusic);
		
		GameMusic.setVolume(0.2);
		GameMusic.play();
		//GameMusic.setCycleCount(MediaPlayer.INDEFINITE);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		Button playButt = new Button("Play Game");
		playButt.setTranslateX(screenWidth/4);
		playButt.setTranslateY(screenHeight/3);
		playButt.setOnAction(e -> {
			stage.setScene(ms);
			started = true;
			GameMusic.stop();
			gameTime.start();
			gameTimer.enable();
			}
		);
		MediaView mediaView = new MediaView(GameMusic);
        
		menu.getChildren().addAll(background,playButt,scores,mediaView);
		
		return menu;
	}
	
	private void spawnEnemies(int c, Pane r) {
		//responsible for spawning enemies
		for(int i = 0; i < c; i++) {
			enemies.add(new Enemy(getRand(300, 2000),getRand(2350, 2700),60,84,3,"ghost",30,100));
			//enemies.add(new Enemy(795,2350,60,84,1,"ghost",30,100));
		}
		for(Sprite e : enemies) {
			root.getChildren().addAll(e,e.getSkin());
		}
	}
	
	public static int getRand(int min, int max) {
		//Just a random number generator
		Random r = new Random();
		return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();
		
	}
	
	private void generateMapBounds() {
		//Yeah dont even try to understand this, im tired and only tired me will get whats happening
		//but it fixes the fact that people have different resolution monitors
		int offx = (screenWidth - screenWidthreal)/2;
		int offy = (screenHeight - screenHeightreal)/2;
		mapBounds.add(new Obstacle(-180 + offx, 330 + offy, 420, 180));//height 180
		mapBounds.add(new Obstacle(-480 + offx, 750 + offy, 880, 250));
		mapBounds.add(new Obstacle(240 + offx,330 + offy,160,310));
		mapBounds.add(new Obstacle(-180 + offx,510 + offy,75,240));
		mapBounds.add(new Obstacle(-100 + offx, 510 + offy, 50, 95));
		mapBounds.add(new Obstacle(100 + offx, 510 + offy, 140, 40));
		mapBounds.add(new Obstacle(400 + offx,350 + offy,390,160));
		mapBounds.add(new Obstacle(940 + offx,2030 + offy,680,320));
		mapBounds.add(new Obstacle(540 + offx,2250 + offy,160,105));
		mapBounds.add(new Obstacle(-170 + offx,2350 + offy,1190,50));
		mapBounds.add(new Obstacle(-480 + offx,1000 + offy,80,360));
		mapBounds.add(new Obstacle(-480 + offx, 1350 + offy, 1250,160));
		mapBounds.add(new Obstacle(540 + offx, 1510 + offy, 160, 190));
		mapBounds.add(new Obstacle(-170 + offx, 1470 + offy,80, 886));
		mapBounds.add(new Obstacle(-90 + offx, 1860 + offy, 634,150));
		mapBounds.add(new Obstacle(610 + offx,-150 + offy,135,510));
		mapBounds.add(new Obstacle(610 + offx,-150 + offy,325,220));
		mapBounds.add(new Obstacle(935 + offx,-150 + offy,85,500));
		mapBounds.add(new Obstacle(880 + offx,350 + offy,435,158));
		mapBounds.add(new Obstacle(1240 + offx,350 + offy,80,1000));
		mapBounds.add(new Obstacle(870 + offx,1356 + offy,450,154));
		mapBounds.add(new Obstacle(940 + offx,1510 + offy,690,278));
		mapBounds.add(new Obstacle(550 + offx,1810 + offy,150,330));
		mapBounds.add(new Obstacle(940 + offx,1780 + offy,160,130));
		mapBounds.add(new Obstacle(1540 + offx,1780 + offy,80,250));
		mapBounds.add(new Obstacle(-84 + offx,1513 + offy,570,71));
		mapBounds.add(new Obstacle(-89 + offx,2012 + offy,85,42));
		mapBounds.add(new Obstacle(205 + offx,2012 + offy,150,42));
		mapBounds.add(new Obstacle(1102 + offx, 1790 + offy,190,50));
		mapBounds.add(new Obstacle(1500 + offx,1790 + offy,30,30));
		mapBounds.add(new Obstacle(703 + offx,1510 + offy,30,60));
		mapBounds.add(new Obstacle(895 + offx, 1513 + offy, 30, 60));
		mapBounds.add(new Obstacle(-125 + offx,1000 + offy,170,45));
		mapBounds.add(new Obstacle(-305 + offx,1150 + offy,435,84));
		mapBounds.add(new Obstacle(235 + offx,1000 + offy,156,40));
		mapBounds.add(new Obstacle(235 + offx,1150 + offy,156,210));
		mapBounds.add(new Obstacle(595 + offx,695 + offy,95,200));
		mapBounds.add(new Obstacle(688 + offx,720 + offy,51,130));
		mapBounds.add(new Obstacle(940 + offx, 695 + offy, 95, 200));
		mapBounds.add(new Obstacle(895 + offx,725 + offy,51,130));
		mapBounds.add(new Obstacle(940 + offx,510 + offy,120,50));
		mapBounds.add(new Obstacle(595 + offx,1033 + offy,95,200));
		mapBounds.add(new Obstacle(690 + offx,1065 + offy,51,130));
		mapBounds.add(new Obstacle(940 + offx,1040 + offy,95,200));
		mapBounds.add(new Obstacle(895 + offx,1065 + offy,51,130));
		
	}

	private Parent createContent(Stage stage) {
		// TODO Auto-generated method stub
		//create player
		//Image dgun = new Image(new File("art/dgun.png").toURI().toString());
		//dguniv = new ImageView(dgun);
		//dguniv.setViewOrder(screenHeight);
		//dguniv.setFitHeight(30);
		//dguniv.setFitWidth(50);
		//root.getChildren().add(dguniv);
		uiComponents.add(player);
		uiComponents.add(player.getSkin());
		uiComponents.add(cords);
		Image mapart = new Image(new File("art/main_level.png").toURI().toString());
		map = new Map(0,-550,3000, 3000, mapart);
		root.setPrefSize(screenWidth,screenHeight);
		root.getChildren().addAll(player,player.getSkin()); //add playerObject to scene
		root.getChildren().add(map);
		generateMapBounds(); //create walls and obstacles
		root.getChildren().addAll(mapBounds); //add walls and obstacles to the screen
		root.getChildren().addAll(cords);
		//setup player health
		cords.setTranslateX(10);
		cords.setTranslateY(10);
		cords.setTextFill(Color.RED);
		//cords.setMinSize(50, 50);
		OuterHealthBar.setStroke(Color.BLACK);
		OuterHealthBar.setTranslateX(675);
		OuterHealthBar.setTranslateY(340);
		InnerHealthBar.setTranslateX(675);
		InnerHealthBar.setTranslateY(340);
		uiComponents.add(OuterHealthBar);
		uiComponents.add(InnerHealthBar);
		spawnEnemies(20,root);
		//create main update loop to make the game possible
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				exe(stage);
			}
		};
		
		timer.start();
		
		root.getChildren().addAll(OuterHealthBar,InnerHealthBar);
		
		return root;
	}
	
	@SuppressWarnings("deprecation")
	private void gameEnd(Stage stage) throws SQLException, ClassNotFoundException {
		Score += gameTimer.getFullSeconds();
		dead = true;
		stage.setScene(deathScene);
		gameTimer.disable();
		gameTime.stop();
		String pscores = "Score: " + Integer.toString(Score) + "        Time Survived: " + gameTimer.getMin() + ":" + gameTimer.getSeconds();
		pscore.setText(pscores);
		DeathMusic.play();
		DeathMusic.setCycleCount(MediaPlayer.INDEFINITE);
	}
	
	private void exe(Stage stage) {
		if(!dead && started) {
			update(stage);
		}
	}
	
	private void update(Stage stage) {
		
		DmgMarker phit = null;
		//Itterate through every node, update movements for sprite objects and trigger ai movements
		//Also check for collisions between everything
		//dguniv.setTranslateX(player.getTranslateX() + 30);
		//dguniv.setTranslateY(player.getTranslateY());
		//cords.setText("X = " + (map.getTranslateX()) + ", Y = " + (map.getTranslateY()));
		if(spawnNewEnemies) {
			for(int i = 0; i < getRand(2,3); i++) {
				newEnemies.add(new Enemy(getRand(300, 2000),getRand(2350, 2700),60,84,5,"ghost",30,100));
				//enemies.add(new Enemy(795,2350,60,84,1,"ghost",30,100));
			}
			for(int i = 0; i < newEnemies.size(); i++) {
				root.getChildren().addAll(newEnemies.get(i),newEnemies.get(i).getSkin());
				enemies.add(newEnemies.get(i));
				newEnemies.remove(i);
			}
			spawnNewEnemies = false;
		}
		
		//Rectangle OuterHealthBar = new Rectangle(250, 250, 100, 30);
		//OuterHealthBar.setStroke(Color.BLACK);
		//OuterHealthBar.setFill(Color.RED);
		//Rectangle InnerHealthBar = new Rectangle(250.0, 250.0, player.getHealth(), 30);
		//InnerHealthBar.setFill(Color.GREEN);
		InnerHealthBar.setWidth(player.getHealth() / 2);
		//InnerHealthBar.setTranslateX(10);
		//InnerHealthBar.setTranslateY(10);
		//OuterHealthBar.setTranslateX(10);
		//OuterHealthBar.setTranslateY(10);
		//root.getChildren().add(InnerHealthBar);
		
		cords.setText( "Score: " + Score + "       Survived Time: " + Integer.toString(gameTimer.getMin()) + ":" + Integer.toString(gameTimer.getSeconds()));
		if(enemies.size() < getRand(0,16)) {
			spawnNewEnemies = true;
		}
		if(player.getWeapon().getType() == "ranged") {
			((rangedWeapon)player.getWeapon()).update();
		}
		if(player.getHealth() <= 0) {
			try {
				gameEnd(stage);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(Node n : root.getChildren()) {
			if(n != null) {
				if(n instanceof DmgMarker) {
					if(((DmgMarker)n).update()) {
						tbremoved.add(n);
					}
				}
				if(n instanceof Projectile) {
					Projectile s = (Projectile) n;
					for(Node p : root.getChildren()) {
						if(p != null) {
							if(p instanceof Rectangle) {
								for(int i = 0; i < ((rangedWeapon)player.getWeapon()).getSpeed(); i++) {
									int hitCode = s.move(p,player.getWeapon().getDamage());
									if(hitCode == 1) {
										root.getChildren().add(new DmgMarker(s.getTranslateX(), s.getTranslateY(), player.getWeapon().getDamage(), 20,(player.getWeapon().getDamage() >= (player.getWeapon().getBaseDamage() + 5))));
										Score+=(player.getWeapon().getDamage() / 2);
									}
									if(hitCode == 1 || hitCode == 2) {
										tbremoved.add(s);
									break;
									}
								}
							}
						}
					}
					if(s.getRange() <= s.getDistanceMoved() || s.getRemove()) {
						tbremoved.add(s);
					}
				}
				if(n instanceof Sprite) {
					boolean clear = true;
					Sprite s = ((Sprite)n);
					if(s.getHealth() <= 0) {
						if(s instanceof Enemy) {
							tbremoved.add(s);
							Score += (30);
						}
						
						break;
					}
					if(s instanceof Enemy)
						//((Enemy) s).updateWander();
						phit = ((Enemy) s).smoothHunt(screenWidth/2, screenHeight/2, player);
						if(phit != null)
							root.getChildren().add(phit);
					//check if any motion in sprite and update acordingly
					if(s.getMotion(0) || s.getMotion(1) || s.getMotion(2) || s.getMotion(3)) {
						if(!s.isMoving())
						s.resumeSpriteAnimation();
						s.moving = true;
					}else {
						if(s.isMoving())
						s.pauseSpriteAnimation();
						s.moving = false;
					}
					
					//All this handles sprite motion, collisions and speed
					if(s.getMotion(0)) {
						for(Node p : root.getChildren()) {
							if(p instanceof Rectangle) {
							clear = s.checkDirClear(1,p,map);
							if(!clear)
								break;
							}
						}
						s.moveUp(clear);
						if(s == player) {
							for(Node x : root.getChildren()) {
								if(x != null) {
									if(!uiComponents.contains(x)) {
											if(clear) {
												if(player.getMotion(3) || player.getMotion(1)) {
													x.setTranslateY(x.getTranslateY()+((player.getSpeed()/3) * 2));
												}else {
													x.setTranslateY(x.getTranslateY()+player.getSpeed());
												}
											}
											x.setViewOrder(screenHeight-x.getTranslateY());
									}
								}
							}
						}
					}
					if(s.getMotion(1)) {
						for(Node p : root.getChildren()) {
							if(p != null) {
								if(p instanceof Rectangle) {
								clear = s.checkDirClear(2,p,map);
								if(!clear)
									break;
								}
							}
						}
						s.moveRight(clear);
						if(s == player) {
							for(Node x : root.getChildren()) {
								if(x != null) {
									if(!uiComponents.contains(x)) {
											if(clear) {
												if(player.getMotion(0) || player.getMotion(2)) {
													x.setTranslateX(x.getTranslateX()-((player.getSpeed()/3) * 2));
												}else {
													x.setTranslateX(x.getTranslateX()-player.getSpeed());
												}
											}
									}
								}
							}
						}
					}
					if(s.getMotion(2)) {
						for(Node p : root.getChildren()) {
							if(p != null) {
								if(p instanceof Rectangle) {
								clear = s.checkDirClear(3,p,map);
								if(!clear)
									break;
								}
							}
						}
						s.moveDown(clear);
						if(s == player) {
							for(Node x : root.getChildren()) {
								if(x != null) {
									if(!uiComponents.contains(x)) {
										if(clear) {
											if(player.getMotion(3) || player.getMotion(1)) {
												x.setTranslateY(x.getTranslateY()-((player.getSpeed()/3) * 2));
											}else {
												x.setTranslateY(x.getTranslateY()-player.getSpeed());
											}
										}
											x.setViewOrder(screenHeight-x.getTranslateY());
									}
								}
							}
						}
					}
					if(s.getMotion(3)) {
						for(Node p : root.getChildren()) {
							if(p != null) {
								if(p instanceof Rectangle) {
								clear = s.checkDirClear(4,p,map);
								if(!clear)
									break;
								}
							}
						}
						s.moveLeft(clear);
						if(s == player) {
							for(Node x : root.getChildren()) {
								if(x != null) {
									if(!uiComponents.contains(x)) {
										if(clear) {
											if(player.getMotion(0) || player.getMotion(2)) {
												x.setTranslateX(x.getTranslateX()+((player.getSpeed()/3) * 2));
											}else {
												x.setTranslateX(x.getTranslateX()+player.getSpeed());
											}
										}
									}
								}
							}
						}
					}
					//prevent player from having "super speed" when moving diagonally
					if(s.getMotion(4)) {
						if(s.speed != 6)
						s.speed = 6;
					}else {
						if(s.speed != 4)
							s.speed = 4;
					}
				}
			}
		}
		for(Object o : tbremoved) {
			if(o != null) {
				if(o instanceof DmgMarker) {
					sceneProjectiles.remove(o);
					root.getChildren().remove(o);
					//Runtime.getRuntime().gc();
				}else {
					if(o instanceof Enemy) {
						enemies.remove(o);
						root.getChildren().remove((((Enemy) o).getSkin()));
						root.getChildren().remove(o);
						//Runtime.getRuntime().gc();
					}
				}
				
			}
			
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
		 
        @Override
        public void handle(MouseEvent mouseEvent) {
             
        	if(player.getWeapon().getType() == "ranged") {
        		//calculate a new trajectory for the bullet based on mouse position and set the bullet on its way
        		if(((rangedWeapon)player.getWeapon()).canFire()) {
	        		double angle = Math.atan((mouseEvent.getSceneY()-player.getTranslateY()) / (player.getTranslateX()-mouseEvent.getSceneX()));
	        		angle = Math.abs(angle);
	        		int xdir = -1;
	        		int ydir = 1;
	        		if(player.getTranslateX() - mouseEvent.getSceneX() < 0) {
	        			xdir = 1;
	        		}
	        		if(mouseEvent.getSceneY() - player.getTranslateY() < 0) {
	        			ydir = -1;
	        		}
	        		((rangedWeapon)player.getWeapon()).fired();
	        		//Projectile temp = new Projectile(player.getTranslateX(), player.getTranslateY(),((((rangedWeapon) player.getWeapon()).getSpeed()) * Math.cos(angle)) * xdir, ((((rangedWeapon) player.getWeapon()).getSpeed()) * Math.sin(angle)) * ydir,((rangedWeapon)player.getWeapon()).getRange() );
	        		Projectile temp = new Projectile(player.getTranslateX(), player.getTranslateY(),(1.0 * Math.cos(angle)) * xdir, (1.0 * Math.sin(angle)) * ydir,((rangedWeapon)player.getWeapon()).getRange() );
	        		root.getChildren().add(temp);
	        		sceneProjectiles.add(temp);
        		}
        	}
        	if(player.getWeapon().getType() == "melee") {
        		if(player.getLastDir() == 1) {
        			
        		}
				if(player.getLastDir() == 2) {
				        			
				}
				if(player.getLastDir() == 3) {
					
				}
				if(player.getLastDir() == 4) {
					
				}
        	}

        }
     
    };

}
