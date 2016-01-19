package javaApplet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;

public class Tree extends Prop {
	public static final char TREE_SHAPE = 'Y';
	public static final int TREE_MAX_NUM_GRAPHIC_HIGH = 6;
	public static final int TREE_MAX_NUM_GRAPHIC_LOW = 4;
	
	public Tree() {
		super(TREE_SHAPE);
		initGraphic();
	}
	
	public boolean isTree() {
		return true;
	}
	
	private Image image = null;
	
	public void initGraphic() {		
//		int randTreeNum = Main.r.nextInt(TREE_MAX_NUM_GRAPHIC_HIGH -TREE_MAX_NUM_GRAPHIC_LOW) + TREE_MAX_NUM_GRAPHIC_LOW;
//		if (randTreeNum > 0 && randTreeNum <= 5) {
//			image = getImage("tree0" + randTreeNum + "_0001.png");
//		}
		
		int randNum = Main.r.nextInt(100);
		String treeImage = "";
		if (randNum > 50) {
			treeImage = "tree01_0001.png";
		}
		else if (randNum > 20 && randNum <= 50) {
			treeImage = "tree02_0001.png";
		}
		else {
			treeImage = "tree03_0001.png";
		}
			
		image = getImage(treeImage);
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
		g.drawImage(image, x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
	}
	
}
