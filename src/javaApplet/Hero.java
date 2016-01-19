package javaApplet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.*;

public class Hero extends Unit {
	public static final int HERO_MOVING_ANIMATION_SPRITE_FRAME = 2;
	Aura aura = new Aura();
	Buff buff = new Buff();
	public Vector<Item> inventory;
	public Vector<Item> equipment;

	int numItemsInInventory = 0;
	int numItemsInEquipment = 0;

	
	public Vector<Item> slots;
	int numItemsInSlots = 0;
	
	public int prevMovement = -1;
	
	public boolean north = false;
	public boolean east = false;
	public boolean south = false;
	public boolean west = false;
	public boolean idle = false;
	
	public static final int CAPACITY_SLOTS = 5;
	public static final int CAPACITY_INVENTORY = 12;
	public static final int CAPACITY_EQUIPMENT = 12;

	public static final char HERO_SHAPE = 'H';
	public static final int HERO_HP = 100;
	public static final int HERO_MP = 100;
	public static final int HERO_EXP = 0;
	public static final int HERO_GOLD = 10;
	public static final int HERO_ATK = 10;
	public static final int HERO_DEF = 10;
	
	
	public Hero() {
		super(HERO_SHAPE, HERO_HP, HERO_MP, HERO_EXP, HERO_GOLD, HERO_ATK, HERO_DEF);
		initInventory(CAPACITY_INVENTORY);
		initEquipment(CAPACITY_EQUIPMENT);
		initSlots(CAPACITY_SLOTS);
		numItemsInSlots = 0;
		initGraphic();
		
	}
	
	public Hero(String userId) {
		super(HERO_SHAPE, userId, HERO_HP, HERO_MP, HERO_EXP, HERO_GOLD, HERO_ATK, HERO_DEF);
		initInventory(CAPACITY_INVENTORY);
		initEquipment(CAPACITY_EQUIPMENT);
		initSlots(CAPACITY_SLOTS);
		numItemsInSlots = 0;
		initGraphic();
	}
	
	public Hero(char shape, int hp, int mp, int exp, int gold, int atk, int def) {
		super(shape, hp, mp, exp, gold, atk, def);
		initInventory(CAPACITY_INVENTORY);
		initEquipment(CAPACITY_EQUIPMENT);
		initSlots(CAPACITY_SLOTS);
		numItemsInSlots = 0;
		initGraphic();
	}
	
	public void setRow(int row) {
		super.setRow(row);
	}	
	public void setCol(int col) {
		super.setCol(col);
	}
	
	public void initInventory(int maxNumItems) {
		inventory = new Vector<Item>(maxNumItems);
		for (int i = 0; i < maxNumItems; i++) {
			inventory.add(null);
		}
		inventory.setElementAt(new HpPotion(), 0);
	}
	public void initEquipment(int maxNumItems) {
		equipment = new Vector<Item>(maxNumItems);
		for (int i = 0; i < maxNumItems; i++) {
			equipment.add(null);
		}
		equipment.setElementAt(new HpPotion(), 0);
	}
	
	public void initSlots(int maxNumItems) {
		slots = new Vector<Item>(maxNumItems);
		for (int i = 0; i < maxNumItems; i++) {
			slots.add(null);
		}
		slots.setElementAt(new HpPotion(), 0);
	}
	
	public String printInventory() {
		String s = getUserId() + "'s inventory : [";
		for (int i = 0; i < inventory.capacity(); i++) {
			s += "(";
			if (inventory.elementAt(i) == null) {
				s += " ";
			}
			else {
				s += inventory.elementAt(i);
			}
			s += ")";
		}
		s += "]";
		return s;
	}
	public String printEquipment() {
		String s = getUserId() + "'s equipment : [";
		for (int i = 0; i < equipment.capacity(); i++) {
			s += "(";
			if (equipment.elementAt(i) == null) {
				s += " ";
			}
			else {
				s += equipment.elementAt(i);
			}
			s += ")";
		}
		s += "]";
		return s;
	}
	
	public String printSlots() {
		String s = getUserId() + "'s slots : [";
		for (int i = 0; i < slots.capacity(); i++) {
			s += "(";
			if (slots.elementAt(i) == null) {
				s += " ";
			}
			else {
				s += slots.elementAt(i);
			}
			s += ")";
		}
		s += "]";
		return s;
	}
	
	public void move(int dir) {
		boolean moved = false;
		
		prevRow = row;
		prevCol = col;
		int rowDir = ((dir == DIR_N)? -1: (dir == DIR_E)? 0: (dir == DIR_S)? 1: 0); 
		int colDir = ((dir == DIR_E)? 1: (dir == DIR_S)? 0: (dir == DIR_W)? -1: 0); 
		
		if (dir == DIR_N) {
			north = true;
			east = false;
			south = false;
			west = false;
			idle = false;
			prevMovement = dir;
		}
		else if (dir == DIR_E) {
			north = false;
			east = true;
			south = false;
			west = false;
			idle = false;
			prevMovement = dir;
		}
		else if(dir == DIR_S) {
			north = false;
			east = false;
			south = true;
			west = false;
			idle = false;
			prevMovement = dir;
		}
		else if (dir == DIR_W){ 
			north = false;
			east = false;
			south = false;
			west = true;
			idle = false;
			prevMovement = dir;
		}
		else {
			north = false;
			east = false;
			south = false;
			west = false;
			idle = false;
			prevMovement = -1;
		}
		
		int heroRow = row + rowDir;
		int heroCol = col + colDir;
		
		if (board.canUnitClimb(heroRow, heroCol)) {
			board.setUnit(prevRow, prevCol, null);
			board.setUnit(heroRow, heroCol, this);
//			pickUpItemsInventory(heroRow, heroCol, board);
			if (numItemsInSlots < slots.capacity()) {
//				System.out.println("numItemsInSlots : " + numItemsInSlots);
//				System.out.println("slots.capacity() : " + slots.capacity());
				pickUpItemsSlot(heroRow, heroCol, board);
			}
			else if (numItemsInSlots >= slots.capacity()) {

				pickUpItemsInventory(heroRow, heroCol, board);
			}
			else {
				System.out.println("Hero::move(): All slot is full to pick up item!");
			}
			moved = true;
			
			// board가 smooth하게 scroll되게하기 위해서 필요.
			if (board.mMoving) {
				board.mMovingCurFrame = 0;
			}
			board.mMoving = true;
			board.mMovingDir = dir;
		}
		
		
//        // freeze monsters around the hero when the hero moved.
//        for (int i = row-1; i <= row+1; i++) {
//            for (int j = col-1; j <= col+1; j++) {
//                if (!(i == row && j == col)) { // except hero.
//                    Unit *curUnit = board->getUnit(i, j);
//                    if (curUnit != NULL && curUnit->isMonster()) {
//                        curUnit->freeze();
//                    }
//                }
//            }
//        }
//    }
//    
//    // battle when an enmy has been engaged.
//    if (enemy != NULL) {
//        if (enemy != NULL && enemy->isMonster()) {
//            // engaged!!!
//            if (unitEngaged != NULL && unitEngaged != enemy) { // new enemy
//                unitEngaged->setUnitEngaged(NULL);
//            }
//            unitEngaged = enemy;
//            
//            // battle (one time)
//            unitEngaged->decHp(atk);
//            if (!unitEngaged->isDead()) {
//                decHp(unitEngaged->getAtk());
//            }
//            else { // monster has been died!
//                incExp(unitEngaged->getExp());
//                incGold(unitEngaged->getGold());
//            }
//        }
//    }
		if (moved) {
			for (int i = prevRow-1; i <= prevRow+1; i++) {
				for (int j = prevCol-1; j <= prevCol+1; j++) {
					if (board.validate(i, j) && !(i == row && j == col)) {
						Unit curUnit = board.getUnit(i, j);
						if (curUnit != null && curUnit.isMonster()) {
							curUnit.unFreeze();
						}
					}
				}
			}
			
			for (int i = row-1; i <= row+1; i++) {
				for (int j = col-1; j <= col+1; j++) {
					if (board.validate(i, j) && !(i == row && j == col)) {
						Unit curUnit = board.getUnit(i, j);
						if (curUnit != null && curUnit.isMonster()) {
							curUnit.freeze();
						}
					}
				}
			}
		}

		if (!moved) {
			if (board.validate(heroRow, heroCol) && !board.canUnitClimb(heroRow, heroCol) && board.getUnit(heroRow, heroCol) != null && ((Monster)board.getUnit(heroRow, heroCol)).isMonster()) {
				enemy = board.getUnit(heroRow, heroCol);
			}
		}
		
		if (enemy != null) {
			if (!enemy.isDead()) {
				enemy.interact(this);
			}
		}
	}
	public void pickUpItemsInventory(int row, int col, Board board) {
		if (numItemsInInventory >= CAPACITY_INVENTORY) {
			return;
		}
		if (board.getItem(row, col) != null) {
			for (int i = 0; i < inventory.capacity(); i++) {
				if (inventory.elementAt(i) == null) {
					inventory.setElementAt(board.getItem(row, col), i);
					board.setItem(row, col, null);
					numItemsInInventory++;
					break;
				}
			}
		}
	}
	
	public void pickUpItemsSlot(int row, int col, Board board) {
		if (numItemsInSlots >= CAPACITY_SLOTS) {
			return;
		}
		if (board.getItem(row, col) != null) {
			for (int i = 0; i < slots.capacity(); i++) {
				if (slots.elementAt(i) == null) {
					slots.setElementAt(board.getItem(row, col), i);
					board.setItem(row, col, null);
					numItemsInSlots++;
					break;
				}
			}
		}
	}

	public void useItemInventory(int index) {
		if (index >= 0 && index < inventory.capacity()) {
			Item item = inventory.get(index);
			if (item != null) {
				if (item.use(this)) {
					inventory.setElementAt(null, index);
					numItemsInInventory--;
				}	
			}
		}
	}
	
	public void useItemSlot(int index) {
		if (index >= 0 && index < slots.capacity()) {
			Item item = slots.get(index);
			if (item != null) {
				if (item.use(this)) {
					slots.setElementAt(null, index);
					numItemsInSlots--;
				}	
			}
		}
	}
	
	public boolean isHero() {
		return true;
	}
	
	// Graphics will not be supported in java servlet environment so comment out the code below when u move this code to java servlet
	private Image[] spriteAnimateNorth = new Image[3];
	private Image[] spriteAnimateEast = new Image[3];
	private Image[] spriteAnimateSouth = new Image[3];
	private Image[] spriteAnimateWest = new Image[3];
	
	private int indexSprite = 2;
	
	public void initGraphic() {
		for (int i = 1; i <= 3; i++) {
			spriteAnimateNorth[i-1] = getImage("hero01_north_00" + i + ".png");
		}
		for (int i = 1; i <= 3; i++) {
			spriteAnimateEast[i-1] = getImage("hero01_east_00" + i + ".png");
		}
		for (int i = 1; i <= 3; i++) {
			spriteAnimateSouth[i-1] = getImage("hero01_south_00" + i + ".png");
		}
		for (int i = 1; i <= 3; i++) {
			spriteAnimateWest[i-1] = getImage("hero01_west_00" + i + ".png");
		}
		indexSprite = 2;
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
	
	
	public int x = -1;
	public int y = -1;
	
	public void paint(Graphics g, int x, int y, ImageObserver o) {
		//aura and buff effect 
		aura.paint(g, x, y, o);
		buff.paint(g, x, y, o);
		
		// Need to constantly check hero's empty item spot 
		// so that there won't be any blank item spot that can't be added
		// when user uses mouse to drag item 
		int numCurSlotItems = 0;
		int numCurInventoryItems = 0;
		int numCurEquipmentItems = 0;

		for (int i = 0; i < slots.capacity(); i++) {
			if (slots.get(i) != null) {
				numCurSlotItems++;
				numItemsInSlots = numCurSlotItems;
			}
		}
		
		for (int i = 0; i < inventory.capacity(); i++) {
			if (inventory.get(i) != null) {
				numCurInventoryItems++;
				numItemsInInventory = numCurInventoryItems;
			}
		}
		for (int i = 0; i < equipment.capacity(); i++) {
			if (equipment.get(i) != null) {
				numCurInventoryItems++;
				numItemsInEquipment = numCurEquipmentItems;
			}
		}
		
		this.x = x;
		this.y = y;
		if (north && !idle) {
			g.drawImage(spriteAnimateNorth[indexSprite], x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
		}
		else if (east && !idle) {
			g.drawImage(spriteAnimateEast[indexSprite], x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
		}
		else if (south && !idle) {
			g.drawImage(spriteAnimateSouth[indexSprite], x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
		}
		else if (west && !idle) {
			g.drawImage(spriteAnimateWest[indexSprite], x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
		}
		else if (idle) {
			if (north) {
				g.drawImage(spriteAnimateNorth[1], x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
			}
			else if (east) {
				g.drawImage(spriteAnimateEast[1], x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
			}
			else if (south) {
				g.drawImage(spriteAnimateSouth[1], x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
			}
			else if (west) {
				g.drawImage(spriteAnimateWest[1], x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
			}
			else {
				g.drawImage(spriteAnimateSouth[1], x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
			}
		}
		else {
			// Pose when the game starts
			g.drawImage(spriteAnimateSouth[1], x, y, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
		}
		
		// FPS에 맞춰서 Sprite의 갯수와 Animation Duration을 조절할 것.
		// 여기서는 3개의 동작인데, 총 1초짜리이므로, 3을 나눈다.
		if (!idle && HttpChatApplet.count % (HttpChatApplet.FPS / HERO_MOVING_ANIMATION_SPRITE_FRAME) == 0) {
			indexSprite--;
		}
		
		if (indexSprite < 0) {
			idle = true;
			indexSprite = 2;
		}
		
		// draw hp bar on top of hero
		g.setColor(Color.gray);
		g.fillRect(x-2, y-12, Tile.IMAGE_WIDTH+4, 5+4);
		
		g.setColor(Color.lightGray);
		g.fillRect(x, y-10, Tile.IMAGE_WIDTH, 5);
		
		g.setColor(Color.red);
		double hpBarPercentage = ((double)hp/100.0);
		int hpLength = (int) (Tile.IMAGE_WIDTH * hpBarPercentage);
		g.fillRect(x, y-10, hpLength, 5);
	}
	
	//========================================================================================
	// Prints sent messages on top of hero
	//========================================================================================
	public void printMessage(Graphics g, int x, int y, Hero hero, ImageObserver o, String outputToTextArea) {
		x = this.x;
		y = this.y;
		int messageAreaX = x-Tile.IMAGE_WIDTH;
		int messageAreaY = y-Tile.IMAGE_HEIGHT;
		int messageAreaWidth = Tile.IMAGE_WIDTH*3;
		int messageAreaHeight = Tile.IMAGE_HEIGHT/2;
		g.setColor(Color.black);
		g.fillRect(messageAreaX-2, messageAreaY-2, messageAreaWidth+4, messageAreaHeight+4); // border
		g.setColor(Color.lightGray);
		g.fillRect(messageAreaX, messageAreaY, messageAreaWidth, messageAreaHeight);
		g.setColor(Color.black);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
		g.drawString("\"" + outputToTextArea + "\"", messageAreaX, messageAreaY+10);
	}
	
	public static final int DEFAULT_SPRITE_ATTACK_DMG = 10;
	public void attack(int dir) {
		int rowDir = ((dir == DIR_N)? -1: (dir == DIR_E)? 0: (dir == DIR_S)? 1: 0); 
		int colDir = ((dir == DIR_E)? 1: (dir == DIR_S)? 0: (dir == DIR_W)? -1: 0);

		char animateDir = ((dir == DIR_N)? '0': (dir == DIR_E)? '1': (dir == DIR_S)? '2': (dir == DIR_W)? '3' : '\0');
		int nextRow = row+rowDir;
		int nextCol = col+colDir;
		board.setFx(nextRow, nextCol, animateDir);
		Unit curUnit = board.getUnit(nextRow, nextCol);
		if (curUnit != null) {
			curUnit.decHp(DEFAULT_SPRITE_ATTACK_DMG);
		}
//		System.out.print("animateDir : " + animateDir );
	}
}
