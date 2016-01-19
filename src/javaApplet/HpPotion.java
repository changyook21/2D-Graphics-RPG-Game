package javaApplet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;

public class HpPotion extends Potion{
	public static final int HP_POTION_HP = 10;
	public static final int HP_POTION_COST = 10;
	public static final char HP_POTION_SHAPE = 'b';
	
	public HpPotion() {
		super(HP_POTION_SHAPE, HP_POTION_HP, 0);
	}
	
	public boolean use(Unit unit) {
		if (unit != null) {
			if (unit.getHp() == unit.getMaxHp()) {
				return false;
			}
			else {
				unit.incHp(getHp());
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
		image = getImage("potion01_hp_0001.png");	
		inventoryImageIcons = getImage("inventory_icon_33X33_potion_hp.png");
		slotImageIcons = getImage("slot_icon_28X28_potion_hp.png");
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
