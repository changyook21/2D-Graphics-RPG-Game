package javaApplet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;

public class PortraitPanel extends JPanel {
	public Hero hero;
    
    public static int iconStartX = 113;
    public static int iconStartY = 52;
    
    public static int ICON_IMAGE_INTERVAL = 36;
    
    public Image[] slotIconImageArray;
    public Rectangle[] slotRectangleImagesArray;
    
    public int rectangleMovingIndex;
    public int rectangleReleaseIndex;
    public Item releasedItem = null;
    
    public int imageX;
    public int imageY;
    public boolean movingImage = false;
    
    public Image curMovingImage;
    
    public static final int ICON_WIDTH = 28; 
    public static final int ICON_HEIGHT = 28;
    
	public PortraitPanel() {
	}
	
	public void paintOnOther(Graphics g, int offsetX, int offsetY) {
		if (movingImage) {
			if (curMovingImage != null) {
				g.drawImage(curMovingImage, imageX + offsetX, imageY + offsetY, this);
			}
		}
	}
	public void paintComponent(Graphics g) {
	    int width = getWidth();
	    int height = getHeight();
	    
	    BufferedImage image = null;
	    
		try {
    		URL url = (new java.io.File("src/javaApplet/portrait_interface_0003.png")).toURI().toURL();
			image = ImageIO.read(url);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (image != null) {
			g.drawImage(image, 0, 0, this);
		}
		
		// Hero item을 check 해 그려줘야 하기 때문에, 현hero의 slot을 빼준
		Vector<Item> heroSlotItems = this.hero.slots;
		
		// Inventory item icon칸마다 그려질 이미지들을 array형태로 만든다 
		slotIconImageArray = new Image[heroSlotItems.capacity()];
		// 똑같은 방식으로 이미지마다 나와바리를 check하기위한 rectangle 들의 array도 만들어 준
		slotRectangleImagesArray = new Rectangle[heroSlotItems.capacity()];
		
		int maxNumSlotItems = heroSlotItems.capacity();
		
		//================================================================================
		// Image array에 hero의 slot에 있는 item마다 각자 자기의 icon image를 주면
		// 여기의 array에 저장을 해준다 
		//================================================================================
		for (int i = 0; i < maxNumSlotItems; i++) {
			if (heroSlotItems.get(i) != null) {
				slotIconImageArray[i] = heroSlotItems.get(i).getSlotImageIcon(); // 실제 item image와 inventory에 그려질 image의 사이즈가 달라 까로 받아온다 
			}
		}
		
		
		//================================================================================
		// 저장해 놓은 image들을 위치와 거리에 맞춰 그려준다 
		//================================================================================
		iconStartX = 112;
		for (int i = 0; i < maxNumSlotItems; i++) {
			slotRectangleImagesArray[i] = new Rectangle(iconStartX + this.getX(), iconStartY + this.getY(), ICON_WIDTH, ICON_HEIGHT);
			if (slotIconImageArray[i] != null) {
				g.drawImage(slotIconImageArray[i], iconStartX, iconStartY, this);
			}
			iconStartX += ICON_IMAGE_INTERVAL;
		}
		//================================================================================

		if (movingImage) {
			if (curMovingImage != null) {
				g.drawImage(curMovingImage,imageX,imageY,this);
			}
		}
		
		if (HttpChatApplet.inventoryItemClicked) {
//			HttpChatApplet.inventoryPanel.paintOnOther(g, this.getX() - HttpChatApplet.BOARD_X, this.getY() - HttpChatApplet.BOARD_Y);
			HttpChatApplet.inventoryPanel.paintOnOther(g, HttpChatApplet.inventoryPanel.getX() - this.getX(), HttpChatApplet.inventoryPanel.getY() - this.getY());
		}
	}
	
	public void moveImage(Image curImage, int rectangleMovingIndex) {
		movingImage = true;
		this.rectangleMovingIndex = rectangleMovingIndex;
		curMovingImage = curImage;
		
		if (rectangleMovingIndex != -1) {
//			System.out.println("releasedItem!");
			//================================================================================
			// put the item clicked to any slot
			//================================================================================
			if (hero.slots.get(rectangleMovingIndex) != null) {
				releasedItem = hero.slots.get(rectangleMovingIndex);
				hero.slots.setElementAt(null, rectangleMovingIndex);
			}
		}
	}
	
	public void releaseImage(Image curImage, int rectangleReleaseIndex) {
		movingImage = false;
		this.rectangleReleaseIndex = rectangleReleaseIndex;
		if (rectangleReleaseIndex != -1) {
//			System.out.println("rectangleReleaseIndex : " + rectangleReleaseIndex);
			if (hero.slots.get(rectangleReleaseIndex) == null) {
				hero.slots.setElementAt(releasedItem, rectangleReleaseIndex);
				releasedItem = null;
			}
		}
	}
	
	public void setHero(Hero hero) {
		this.hero = hero;
	}
}
