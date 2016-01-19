package javaApplet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;

public class Unit {
	public Board board;
	
	public Unit enemy;	
	
	protected int row;
	protected int col;	
	
	protected int prevRow;
	protected int prevCol;	
	
	private char shape;
	private String userId;
	protected int hp;
	private int maxHp;
	private int mp;
	private int maxMp;
	private int exp;
	private int gold;
	private int atk;
	private int def;
	
	public static final boolean unitMoved = false;
	
	public static final int NUM_DIR = 4;
	public static final int DIR_N = 0;
	public static final int DIR_E = 1;
	public static final int DIR_S = 2;
	public static final int DIR_W = 3;
	
	private boolean dead;
	protected boolean frozen;
	
	public static final char UNIT_SHAPE = 'u';
	public static final int UNIT_HP = 0;
	public static final int UNIT_MAX_HP = 100;
	public static final int UNIT_MP = 0;
	public static final int UNIT_MAX_MP = 100;
	public static final int UNIT_EXP = 0;
	public static final int UNIT_GOLD = 0;
	public static final int UNIT_ATK = 0;
	public static final int UNIT_DEF = 0;
	
	public Unit() {
		shape = UNIT_SHAPE;
		row = -1;
		col = -1;
		prevRow = -1;
		prevCol = -1;
		hp = UNIT_HP;
		maxHp = UNIT_MAX_HP;
		mp = UNIT_MP;
		maxMp = UNIT_MAX_MP;
		exp = UNIT_EXP;
		gold = UNIT_GOLD;
		atk = UNIT_ATK;
		def = UNIT_DEF;
		
		dead = false;
		frozen = false;
		initGraphic();
	}
	
	public Unit(char shape) {
		this.shape = shape;
		row = -1;
		col = -1;
		prevRow = -1;
		prevCol = -1;
		hp = UNIT_HP;
		maxHp = UNIT_MAX_HP;
		mp = UNIT_MP;
		maxMp = UNIT_MAX_MP;
		exp = UNIT_EXP;
		gold = UNIT_GOLD;
		atk = UNIT_ATK;
		def = UNIT_DEF;
		
		dead = false;
		frozen = false;
//		initGraphic();
	}
	
	public Unit(char shape, String userId, int hp, int mp, int exp, int gold, int atk, int def) {
		this.shape = shape;
		row = -1;
		col = -1;
		prevRow = -1;
		prevCol = -1;
		this.userId = userId;
		this.hp = hp;
		maxHp = UNIT_MAX_HP;
		this.mp = mp;
		maxMp = UNIT_MAX_MP;
		this.exp = exp;
		this.gold = gold;
		this.atk = atk;
		this.def = def;
		
		dead = false;
		frozen = false;
//		initGraphic();
	}
	
	public Unit(char shape, int hp, int mp, int exp, int gold, int atk, int def) {
		this.shape = shape;
		row = -1;
		col = -1;
		prevRow = -1;
		prevCol = -1;
		this.hp = hp;
		maxHp = UNIT_MAX_HP;
		this.mp = mp;
		maxMp = UNIT_MAX_MP;
		this.exp = exp;
		this.gold = gold;
		this.atk = atk;
		this.def = def;
		
		dead = false;
		frozen = false;
//		initGraphic();
	}
	
	public String toString() {
		return "" + shape;
	}
	
	public String printStat() {
		String s = "";
		if (userId == null) {
			s = "(" + shape + "): " + "hp: " + hp + " mp: " + mp + 
				       " exp: " + exp + " gold: " + gold + " atk: " + atk + 
					   " def: " + def;	
		}
		else {
			s = "[" + userId + "]: " + "hp: " + hp + " mp: " + mp + 
				       " exp: " + exp + " gold: " + gold + " atk: " + atk + 
					   " def: " + def;
		}
		return s;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId= userId;
	}
	
	//------------------------------------- HP
	public int getHp() {
		return hp;
	}
	public void decHp(int hpToDec) {
		if (hp - hpToDec <= 0) {
			hp = 0;
			dead = true;
		}
		else {
			hp -= hpToDec;
		}
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void incHp(int hpToInc) {
		if (hp + hpToInc >= maxHp) {
			hp = maxHp;
		}
		else {
			hp += hpToInc;
		}
	}
	
	public int getMaxHp() {
		return maxHp;
	}
	
	//------------------------------------- MP
	public int getMp() {
		return mp;
	}
	public void decMp(int mpToDec) {
		if (mp - mpToDec <= 0) {
			mp = 0;
		}
		else {
			mp -= mpToDec;
		}
	}
	public void incMp(int mpToInc) {
		if (mp + mpToInc >= maxMp) {
			mp = maxMp;
		}
		else {
			mp += mpToInc;
		}
	}
	
	//------------------------------------- EXP
	public int getExp() {
		return exp;
	}
	public void decExp(int expToDec) {
		if (exp - expToDec <= 0) {
			exp = 0;
		}
		else {
			exp -= expToDec;
		}
	}
	public void incExp(int expToInc) {
		exp += expToInc;
	}
	
	//------------------------------------- GOLD
	public int getGold() {
		return gold;
	}
	public void decGold(int goldToDec) {
		if (gold + goldToDec <= 0) {
			gold = 0;
		}
		else {
			gold -= goldToDec;
		}
	}
	
	public void incGold(int goldToInc) {
		gold += goldToInc;
	}
	//------------------------------------- ATK
	public int getAtk() {
		return atk;
	}
	public void decAtk(int atkToDec) {
		if (atk + atkToDec <= 0) {
			atk = 0;
		}
		else {
			atk -= atkToDec;
		}
	}
	public void incAtk(int atkToInc) {
		atk += atkToInc;
	}
	
	//------------------------------------- DEF
	public int getDef() {
		return def;
	}
	public void decDef(int defToDec) {
		if (def + defToDec <= 0) {
			def = 0;
		}
		else {
			def -= defToDec;
		}
	}
	public void incDef(int defToInc) {
		def += defToInc;
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	public void setCol(int col) {
		this.col = col;
	}
	
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	
	public int getPrevRow () {
		return prevRow;
	}
	public int getPrevCol() {
		return prevCol;
	}
	
	public void move(int dir) {
		int prevRow = row;
		int prevCol = col;
		int rowDir = ((dir == DIR_N)? -1: (dir == DIR_E)? 0: (dir == DIR_S)? 1: 0); 
		int colDir = ((dir == DIR_E)? 1: (dir == DIR_S)? 0: (dir == DIR_W)? -1: 0); 
		
		int heroRow = row + rowDir;
		int heroCol = col + colDir;
		
		if (board.canUnitClimb(heroRow, heroCol)) {
			board.setUnit(prevRow, prevCol, null);
			board.setUnit(heroRow, heroCol, this);
		}
	}
	
	public void interact(Unit unit) {
		interact(unit);
	}

	public boolean isMonster() {
		return isMonster();
	}

	public void freeze() {
		frozen = true;
	}
	
	public void unFreeze() {
		frozen = false;
	}
	
	public boolean isHero() {
		return false;
	}
	
	// Graphics will not be supported in java servlet environment so comment out the code below when u move this code to java servlet
	
	public void initGraphic() {
	}
	
	public Image getImage(String path) {
		return null;
	}
		
	public void paint(Graphics g, int x, int y, ImageObserver o) {
	}
}
