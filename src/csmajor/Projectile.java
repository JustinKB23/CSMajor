package csmajor;



import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Projectile extends Circle{
	private double ySpeed;
	private double xSpeed;
	private double range;
	private double distanceMoved;
	private boolean remove;
	Projectile(double d, double e, double xspeed, double yspeed, double r){
		super(2,2,2,Color.BLACK);
		setTranslateX(d);
		setTranslateY(e);
		xSpeed = xspeed;
		ySpeed = yspeed;
		range = r;
		remove = false;
	}
	double getRange() {
		return range;
	}
	double getDistanceMoved() {
		return distanceMoved;
	}
	boolean getRemove() {
		return remove;
	}
	void markRemove() {
		remove = true;
	}
	
	int move(Node s, double dmg){
		//return 1 = hit enemy and remove
		//return 2 = remove
		//return 0 = nothing
			this.setTranslateX(getTranslateX() + xSpeed);
			this.setTranslateY(getTranslateY() + ySpeed);
			distanceMoved += 1;
			Bounds tbounds = this.getBoundsInParent();
			Bounds sbounds = s.getBoundsInParent();
			if(tbounds.intersects(sbounds)) {
				if(s instanceof Sprite){
					if(((Sprite) s).getType() == "ghost") {
						((Enemy)s).damage(dmg);
						return 1;
					}else if(((Sprite) s).getType() == "player")
						return 0;
					
				}
				//return true;
				return 2;
			}
			return 0;
	}
}
