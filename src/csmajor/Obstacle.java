package csmajor;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

public class Obstacle extends Rectangle{
	double width;
	double height;
	private int screenWidth = (int) Screen.getScreens().get(0).getBounds().getWidth();
	private int screenHeight = (int) Screen.getScreens().get(0).getBounds().getHeight();
	Obstacle(double ix, double iy, double iw, double ih){
		//super(iw,ih,Color.YELLOW);
		super(iw,ih,Color.TRANSPARENT);
		//this.setTranslateX(ix);
		//this.setTranslateY(iy);
		this.setTranslateX(ix + (screenWidth/2));
		this.setTranslateY(iy + (screenHeight/2) - 550);
	}
}
