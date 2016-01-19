package javaApplet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;

public class MpPotion extends Potion {
	
	public static final int MP_POTION_MP = 10;
	public static final int MP_POTION_COST = 10;
	public static final char MP_POTION_SHAPE = 'b';
	
	public MpPotion() {
		super(MP_POTION_SHAPE, 0, MP_POTION_MP);
	}
	
	public boolean use(Unit unit) {
		if (unit != null) {
			if (unit.getHp() == unit.getMaxHp()) {
				return false;
			}
			else {
				unit.incHp(getMp());
				return true;
			}
		}
		return false;
	}

	public Image getInventoryImageIcon() {
		return inventoryImageIcons;
	}
	
	public Image getSlotImageIcon() {
		return slotImageIcons;
	}
	
	
	public void initGraphic() {
		image = getImage("potion01_mp_0001.png");	
		inventoryImageIcons = getImage("inventory_icon_33X33_potion_mp.png");
		slotImageIcons = getImage("slot_icon_28X28_potion_mp.png");
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
