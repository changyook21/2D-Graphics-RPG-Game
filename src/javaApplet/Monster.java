package javaApplet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;

public class Monster extends Unit {
	public static final char MONSTER_SHAPE = 'm';
	public static final int MONSTER_HP = 20;
	public static final int MONSTER_MP = 0;
	public static final int MONSTER_EXP = 10;
	public static final int MONSTER_GOLD = 10;
	public static final int MONSTER_ATK = 5;
	public static final int c = 5;
	
	public Monster() {
		super(MONSTER_SHAPE, MONSTER_HP, MONSTER_MP, MONSTER_EXP, MONSTER_GOLD, MONSTER_ATK, MONSTER_ATK);
		initGraphic();
	}
	
	public Monster(char shape, int hp, int mp, int exp, int gold, int atk, int def) {
		super(shape, hp, mp, exp, gold, atk, def);
		initGraphic();
	}
	
	public void move(int dir) {
		if (frozen) {
			return;
		}
		
		int prevRow = row;
		int prevCol = col;
		int rowDir = ((dir == DIR_N)? -1: (dir == DIR_E)? 0: (dir == DIR_S)? 1: 0); 
		int colDir = ((dir == DIR_E)? 1: (dir == DIR_S)? 0: (dir == DIR_W)? -1: 0); 
		
		int heroRow = row + rowDir;
		int heroCol = col + colDir;
		
//		System.out.print("heroRow : " + heroRow + '\n');
//		System.out.print("heroCol : " + heroCol + '\n');
		
		if (board.canUnitClimb(heroRow, heroCol)) {
			board.setUnit(prevRow, prevCol, null);
			board.setUnit(heroRow, heroCol, this);
		}
	}
	
	public boolean isMonster() {
		return true;
	}
	
	public void interact(Unit unit) {
		if (unit != null) {
			decHp(unit.getAtk());
			unit.decHp(getAtk());
		}
	}
	
	// Graphics will not be supported in java servlet environment so comment out the code below when u move this code to java servlet
	private Image[] spriteAnimate = new Image[5];
	private int indexSprite = 0;
	
	public void initGraphic() {
		for (int i = 1; i <= 5; i++) {
			spriteAnimate[i-1] = getImage("monster01_000" + i + ".png");
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
		if (HttpChatApplet.count % (HttpChatApplet.FPS / 5) == 0) {
			indexSprite++;	
		}
		
		if (indexSprite >= spriteAnimate.length) {
			indexSprite = 0;
		}
	}
}
