package csmajor;

import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

import java.io.File;
import javafx.util.Duration;

import javafx.animation.Animation;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Sprite extends Rectangle{
	//	 1
	//4	 p	2
	//	 3

	//private int screenWidth = (int) Screen.getScreens().get(0).getBounds().getWidth();
	
									//up,right,down,left,sprint
	private boolean motion[] = {false,false,false,false,false};
	private int screenHeight = (int) Screen.getScreens().get(0).getBounds().getHeight();
	int lastDir = 3;
	final String type;
	boolean moving = false;
	Image Img = new Image(new File("").toURI().toString());
	ImageView skin = new ImageView(Img);
	//Spritesheet dimentions and values
	final int COLUMNS  = 4;
    final int COUNT    = 4;
    final int OFFSET_X = 0;
    final int OFFSET_Y = 184;
    final int WIDTH    = 72;
    final int HEIGHT   = 92;
    int height;
    int width;
    double health;
    Weapon weapon;
	
    Animation animation; 
	
	int speed;
	/*
	 * I really didn't comment in this class that much because most of it is fairly self
	 * explanatory in my opinion due to the names I used.
	 */
	
	Sprite(int x, int y, int w, int h,int s, String type, Weapon wp, double hp){
		super(w-20,h/2-10,Color.TRANSPARENT); //USE THIS FOR ACTUAL GAME
		//super(w-20,h/2-10,Color.RED); //USE THIS TO SHOW COLLISION RECTANGLE
		this.type = type;
		this.speed = s;
		this.height = h;
		this.width = w;
		this.health = hp;
		setViewOrder(screenHeight);
		Img = new Image(new File("art/" + type + "spritesheet.png").toURI().toString());
		skin.setImage(Img);
		setTranslateX(x);
		skin.setTranslateX(x-10);
		setTranslateY(y);
		skin.setTranslateY(y-(h/2)-5);
		skin.setFitHeight(h);
		skin.setFitWidth(w);
		skin.setViewOrder(screenHeight-getTranslateY());
		animation = new SpriteAnimation(
	            skin,
	            Duration.millis(600),
	            COUNT, COLUMNS,
	            OFFSET_X, OFFSET_Y,
	            WIDTH, HEIGHT
	    );
		animation.setCycleCount(Animation.INDEFINITE);
		skin.setViewport(new Rectangle2D(0, 184, 72, 92));
		weapon = wp;
	}
	String getType() {
		return type;
	}
	double getHealth() {
		return health;
	}
	
	void damage(double d) {
		health = health - d;
	}
	
	Weapon getWeapon() {
		return weapon;
	}
	
	void setMotion(int i, boolean s) {
		motion[i] = s;
	}
	
	boolean getMotion(int i) {
		return motion[i];
	}
	
	int getLastDir() {
		return lastDir;
	}
	
	void setMoving(boolean t) {
		moving = t;
	}
	
	boolean isMoving() {
		return moving;
	}
	
	ImageView getSkin() {
		return skin;
	}
	
	void pauseSpriteAnimation() {
		animation.pause();
	}
	double getSpeed() {
		return speed;
	}
	
	void resumeSpriteAnimation() {
		animation.playFromStart();
	}
	void moveLeft(boolean clear) {
		lastDir = 4;
		((SpriteAnimation) animation).setY(276);
			if(this.type != "player") {
				if(clear) {
					if(motion[0] || motion[2]) {
						setTranslateX(getTranslateX()-((speed/3) * 2));
					}else {
						setTranslateX(getTranslateX()-speed);
					}
				}
				skin.setTranslateX(getTranslateX()-10);
			}
	}
	void moveRight(boolean clear) {
		lastDir = 2;
		((SpriteAnimation) animation).setY(92);
			if(this.type != "player") {
				if(clear) {
					if(motion[0] || motion[2]) {
						setTranslateX(getTranslateX()+((speed/3) * 2));
					}else {
						setTranslateX(getTranslateX()+speed);
					}
				}
				skin.setTranslateX(getTranslateX()-10);
			}
	}
	void moveUp(boolean clear) {
		lastDir = 1;
		((SpriteAnimation) animation).setY(0);
			if(this.type != "player") {
				if(clear) {
					if(motion[3] || motion[1]) {
						setTranslateY(getTranslateY()-((speed/3) * 2));
					}else {
						setTranslateY(getTranslateY()-speed);
					}
				}
				skin.setTranslateY(getTranslateY()-(height/2)-5);
				skin.setViewOrder(screenHeight-getTranslateY());
			}
	}
	void moveDown(boolean clear) {
		lastDir = 3;
		((SpriteAnimation) animation).setY(184);
			if(this.type != "player") {
				if(clear) {
					if(motion[3] || motion[1]) {
						setTranslateY(getTranslateY()+((speed/3) * 2));
					}else {
						setTranslateY(getTranslateY()+speed);
					}
				}
				skin.setTranslateY(getTranslateY()-(height/2)-5);
				skin.setViewOrder(screenHeight-getTranslateY());
			}
	}
	//checks if direction is clear and if the player can move there
	boolean checkDirClear(int dir, Node s, Map map){
		//	 1
		//4	 this	2
		//	 3
		if(s == this) {
			return true;
		}
		if(this.type == "ghost") {
			//return true;
		}
		if(this.type == "player" && (s instanceof Enemy)) {
			return true;
		}
		if(this.type == "ghost" && !(s instanceof Enemy)) {
			return true;
		}
		
		Bounds sbounds = s.getBoundsInParent();
		if(dir == 1) {
			if(sbounds.intersects(this.getTranslateX(), this.getTranslateY() - speed, this.getWidth(), this.getHeight())) {
				return false;
			}
			if(this.getTranslateY() - this.speed <= map.getTranslateY()) {
				return false;
			}
		}else if(dir == 2) {
			if(sbounds.intersects(this.getTranslateX() + speed, this.getTranslateY(), this.getWidth(), this.getHeight())) {
				return false;
			}
			if(this.getTranslateX() + this.speed >= map.getTranslateX() + map.getWidth()) {
				return false;
			}
		}else if(dir == 3) {
			if(sbounds.intersects(this.getTranslateX(), this.getTranslateY() + speed, this.getWidth(), this.getHeight())) {
				return false;
			}
			if(this.getTranslateY() + this.speed >= map.getTranslateY() + map.getHeight()) {
				return false;
			}
		}else if (dir == 4) {
			if(sbounds.intersects(this.getTranslateX() - speed, this.getTranslateY(), this.getWidth(), this.getHeight())) {
				return false;
			}
			if(this.getTranslateX() - this.speed <= map.getTranslateX()) {
				return false;
			}
		}
		return true;
	}
	
}
