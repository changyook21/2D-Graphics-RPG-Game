package javaApplet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;

public class Aura {
	
	public int x = -1;
	public int y = -1;
	
	public void paint(Graphics g, int x, int y, ImageObserver o) {
		this.x = x;
		this.y = y;
		
		g.drawImage(getImage("glass.png"), x-1, y-5, Tile.IMAGE_WIDTH + 5, Tile.IMAGE_HEIGHT + 10, o);
	
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

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}
}

