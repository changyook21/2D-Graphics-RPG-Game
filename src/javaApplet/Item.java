package javaApplet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;

public class Item {
	private char shape;
	
	public static final char ITEM_SHAPE = 'i';
	
	public Item() {
		shape = ITEM_SHAPE;
		initGraphic();
	}
	
	public Item(char shape) {
		this.shape = shape;
		initGraphic();
	}
	
	public String toString() {
		return "" + shape;
	}
	
	public boolean use(Unit unit) {
		return use(unit);
	}
	
	public boolean isPotion() {
		return false;
	}
	
	public Image getInventoryImageIcon() {
		return inventoryImageIcons;
	}
	
	public Image getSlotImageIcon() {
		return slotImageIcons;
	}
	/// Graphics will not be supported in java servlet environment so comment out the code below when u move this code to java servlet
	public Image image = null;
	public Image inventoryImageIcons = null;
	public Image slotImageIcons = null;
	
	public void initGraphic() {
	}
	
	public void paint(Graphics g, int x, int y, ImageObserver o) {
		g.drawImage(image, x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
	}

	
}
