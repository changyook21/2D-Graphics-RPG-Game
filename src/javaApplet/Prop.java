package javaApplet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;

public class Prop {
	private char shape;
	
	public static final char PROP_SHAPE = 'P';
	
	public Prop() {
		shape = PROP_SHAPE;
	}
	
	public Prop(char shape) {
		this.shape = shape;
	}
	
	public String toString() {
		return "" + shape;
	}
	
	public boolean isTree() {
		return false;
	}
	
	private Image[] spriteAnimate = new Image[3];
	private int indexSprite = 0;
	
	public void initGraphic() {
		for (int i = 1; i <= 3; i++) {
			spriteAnimate[i-1] = getImage("hero01_south_00" + i + ".png");
		}
	}
	
	public Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = AppletImage.class.getResource(path);
			tempImage =  Toolkit.getDefaultToolkit().getImage(imageURL);
		}
		catch (Exception e) {
			System.out.println("An error occured - " + e.getMessage());
		}
		return tempImage;
	}
	
	
	public void paint(Graphics g, int x, int y, ImageObserver o) {
		g.drawImage(spriteAnimate[indexSprite], x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
		indexSprite++;
		if (indexSprite >= spriteAnimate.length) {
			indexSprite = 0;
		}
	}
}
