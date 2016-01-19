package javaApplet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;

public class Tile {
	public static final int IMAGE_WIDTH = 24;
	public static final int IMAGE_HEIGHT = 32;
	
	public static final char FX_NONE = '\0';
	
	public int indexSprite = 0;
	
	public static boolean drawn = false;
	
	private char fx;
	
	private Unit unit;
	private Item item;
	private Prop prop;
	
	
	public static final char EMPTY_CHAR = '.';
	
	public Tile() {
		fx = FX_NONE;
		unit = null;
		item = null;
		prop = null;
		initGraphic();
	}
	
	public String toString() {
		if (unit != null) {
			return "" + unit.toString();
		}
		if (item != null) {
			return "" + item.toString();
		}
		if (prop != null) {
			return "" + prop.toString();
		}
		else {
			return "" + EMPTY_CHAR;
		}
	}

	public char getFx() {
		return fx;
	}
	
	public void setFX(char fx) {
		this.fx = fx;
	}
	
	public void clearFx() {
		fx = FX_NONE;
	}
	
	public Unit getUnit() {
		return unit;
	}
	
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	
	public Prop getProp() {
		return prop;
	}
	
	public void setProp(Prop prop) {
		this.prop = prop;
	}
	
	public boolean isEmpty() {
		return unit == null && item == null && prop == null; 
	}
	
	// Graphics will not be supported in java servlet environment so comment out the code below when u move this code to java servlet
	public Image image = null;
	private Image[][] spriteAttack = new Image[4][3];
	
	public void initGraphic() {
		String tileImage = "";
		tileImage = "tile_0001_0001.png";

		image = getImage(tileImage);
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) { 
//				System.out.println("animation_000" + (i+1) + "_000" + (j+1) + ".png");
				spriteAttack[i][j] = getImage("animation_000" + (i+1) + "_000" + (j+1) + ".png");
			}
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
		g.drawImage(image, x, y, IMAGE_WIDTH, IMAGE_HEIGHT, o);

		if (item != null) {
//			if (item.isPotion()) {
				item.paint(g, x, y, o);
//			}
		}
		if (unit != null) {
			if (unit.isHero()) {
//				unit.paint(g, x, y, o);
			}
			else if (unit.isMonster()) {
				unit.paint(g, x, y, o);
			}
		}
		if (prop != null) {
			if (prop.isTree()) {
				prop.paint(g, x, y, o);
			}
		}
		if (fx != '\0') {
			int fxInt = Character.getNumericValue(fx);
			g.drawImage(spriteAttack[fxInt][indexSprite], x, y, IMAGE_WIDTH, IMAGE_HEIGHT, o);
			if (HttpChatApplet.count % (HttpChatApplet.FPS / 20) == 0) {
				indexSprite++;
			}

			if (indexSprite >= spriteAttack[0].length) {
				indexSprite = 0;
				fx = '\0';
			}
		}
	}
}
