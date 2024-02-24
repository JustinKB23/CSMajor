package csmajor;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Map extends ImageView{
	
	double width;
	double height;
	
	Map(double ix, double iy, double iw, double ih, Image i){
		super(i);
		this.setTranslateX(ix);
		this.setTranslateY(iy);
		this.setViewOrder(ih);
		this.setFitHeight(ih);
		this.setFitWidth(iw);
		width = iw;
		height = ih;
	}
	
	double getWidth() {
		return width;
	}
	
	double getHeight() {
		return height;
	}

}
