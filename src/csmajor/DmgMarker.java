package csmajor;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;


public class DmgMarker extends Label{
	int life;

	DmgMarker(double x, double y, double val, int dur, boolean crit){
		super(Double.toString(val));
		setTranslateX(x);
		setTranslateY(y);
		life = dur;
		//this.setStyle("-fx-font-weight: bold" + "-fx-stroke: black" +
	    //"-fx-stroke-width: 2px");
		this.setScaleX(2);
        this.setScaleY(2);
		if(!crit)
			this.setTextFill(Color.YELLOW);
		else
			this.setTextFill(Color.RED);
	}
	
	boolean update() {
		//decrememnts life counter and returns true when the main game should delete the object
		life--;
		if(life < 10) {
			this.setScaleX(getScaleX()-.1);
			this.setScaleY(getScaleY()-.1);
		}
		return (life <= 0);
	}
}
