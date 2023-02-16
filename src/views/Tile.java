package views;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.world.Champion;

public class Tile extends Rectangle {
	
	public Tile(boolean light, int x, int y) {
        setWidth(170);
        setHeight(170);
        relocate(x*170, y*170);
        setFill(light ? Color.valueOf("#feb") : Color.valueOf("#582"));
    }
}
