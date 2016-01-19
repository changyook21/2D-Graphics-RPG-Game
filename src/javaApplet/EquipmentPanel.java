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

public class EquipmentPanel extends JPanel {
	public Hero hero;
	
    public static final int BORDER_WIDTH = 5;
    public static final int BORDER_HEIGHT = 5;
    
    public static int iconStartX = 4 + BORDER_WIDTH;
    public static int iconStartY = 35 + BORDER_HEIGHT;
    
    public static int ICON_IMAGE_INTERVAL = 272;
    
    public Image[] equipmentIconImageArray;
    public Rectangle[] equipmentRectangleImagesArray;
    
    public int rectangleMovingIndex;
    public int rectangleReleaseIndex;
    
    public Item releasedItem = null;
    
    public int imageX;
    public int imageY;
    public boolean movingImage = false;
    
    public Image curMovingImage;
    
    public static final int ICON_WIDTH = 33;
    public static final int ICON_HEIGHT = 33;
    
	public EquipmentPanel() {
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
	    
	    // Border
	    g.setColor(Color.gray);
	    g.fillRect(0, 0, width+10, height+10);
	    BufferedImage image = null;
	    
		try {
    		URL url = (new java.io.File("src/javaApplet/Equipment.png")).toURI().toURL();
			image = ImageIO.read(url);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (image != null) {
			g.drawImage(image, BORDER_WIDTH , BORDER_HEIGHT, this);
		}
		
		// Hero item을 check 해 그려줘야 하기 때문에, 현hero의 slot을 빼준
		Vector<Item> heroEquipmentItems = this.hero.equipment;
		
		// Inventory item icon칸마다 그려질 이미지들을 array형태로 만든다 
		equipmentIconImageArray = new Image[heroEquipmentItems.capacity()];
		// 똑같은 방식으로 이미지마다 나와바리를 check하기위한 rectangle 들의 array도 만들어 준
		equipmentRectangleImagesArray = new Rectangle[heroEquipmentItems.capacity()];
		
		int maxNumEquipmentItems = heroEquipmentItems.capacity();
		int maxNumEquipmentItemsRow = 6;
		int maxNumEquipmentItemsPerRow = maxNumEquipmentItems/maxNumEquipmentItemsRow;
		
		//================================================================================
		// Image array에 hero의 slot에 있는 item마다 각자 자기의 icon image를 주면
		// 여기의 array에 저장을 해준다 
		//================================================================================
		for (int i = 0; i < maxNumEquipmentItems; i++) {
			if (heroEquipmentItems.get(i) != null) {
				equipmentIconImageArray[i] = heroEquipmentItems.get(i).getInventoryImageIcon(); // 실제 item image와 inventory에 그려질 image의 사이즈가 달라 까로 받아온다 
			}
		}
		
		//================================================================================
		// 저장해 놓은 image들을 위치와 거리에 맞춰 그려준다 
		//================================================================================
		int curIndex = 0;
		for (int i = 0; i < maxNumEquipmentItemsRow; i++) {
			for (int j = 0; j < maxNumEquipmentItemsPerRow; j++) { 
				// drag할 경우, 이미지가 없더라도, 이미지가 있어야 하는 자리를 클릭했을 때 drag를 못하게 해야 한
				// 이 곳에서 만들어 줘야, 처음 만들 때부터 이미지 아이콘의 위치를 정검할 수 있다
				//
				// paint는 지속적으로 call되기 때문에 그에 따라 위치도 다시 갱신 된다
				equipmentRectangleImagesArray[curIndex] = new Rectangle(iconStartX + this.getX(), iconStartY + this.getY(), ICON_WIDTH, ICON_HEIGHT);
				if (equipmentIconImageArray[curIndex] != null) {
					g.drawImage(equipmentIconImageArray[curIndex], iconStartX, iconStartY, this);
					// 이미지마다 똑같은 자리에, 자신의 나와바리를 check를 도와줄 rectangle을 만들어 준다 
//					rectangleImagesArray[curIndex] = new Rectangle(iconStartX + this.getX(), iconStartY + this.getY(), heroItems.get(i).getImageIcon().getWidth(this), heroItems.get(i).getImageIcon().getHeight(this));
				}
				iconStartX += ICON_IMAGE_INTERVAL;
				curIndex++;
			}
			iconStartX = 4 + BORDER_WIDTH; // reset startX
			iconStartY += 35;
		}
		iconStartY = 35 + BORDER_HEIGHT; // reset startY
		//================================================================================

		if (movingImage) {
			if (curMovingImage != null) {
				g.drawImage(curMovingImage,imageX,imageY,this);
			}
		}
		
		if (HttpChatApplet.slotItemClicked) {
			HttpChatApplet.portraitPanel.paintOnOther(g, HttpChatApplet.portraitPanel.getX() - getX(), HttpChatApplet.portraitPanel.getY() - getY()); 
		}
	}
	
	public void moveImage(Image curImage, int rectangleMovingIndex) {
//		System.out.println("moveImage!");
		movingImage = true;
		curMovingImage = curImage;
		this.rectangleMovingIndex = rectangleMovingIndex;

		if (rectangleMovingIndex != -1) {
			//================================================================================
			// put the item clicked to any slot
			//================================================================================
			if (hero.equipment.get(rectangleMovingIndex) != null) {
//				System.out.println("releasedItem!");
				releasedItem = hero.equipment.get(rectangleMovingIndex);
				hero.equipment.setElementAt(null, rectangleMovingIndex);
			}
		}
	}
	
	public void releaseImage(Image curImage, int rectangleReleaseIndex) {
		movingImage = false;
		this.rectangleReleaseIndex = rectangleReleaseIndex;
		
		if (rectangleReleaseIndex != -1) {
			if (hero.equipment.get(rectangleReleaseIndex) == null) {
				if (releasedItem != null) {
//					System.out.println("rectangleReleaseIndex  : " + rectangleReleaseIndex);
					hero.equipment.setElementAt(releasedItem, rectangleReleaseIndex);
					releasedItem = null;
				}
			}
		}
	}
	
	public void setHero(Hero hero) {
		this.hero = hero;
	}
}
