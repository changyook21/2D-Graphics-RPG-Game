package javaApplet;

public class Potion extends Item{
	private int hp;
	private int mp;
	private int cost;
	
	public static final int DEFAULT_POTION_HP = 0;
	public static final int DEFAULT_POTION_MP = 0;
	public static final int DEFAULT_POTION_COST = 0;
	
	
	public static final char POTION_SHAPE = 'b';
	
	public Potion() {
		super(POTION_SHAPE);
		hp = DEFAULT_POTION_HP;
		hp = DEFAULT_POTION_MP;
		cost = DEFAULT_POTION_COST;
	}
	
	public Potion(char shape) {
		super(shape);
	}
	
	public Potion(char shape, int hp, int mp) {
		super(shape);
		this.hp = hp;
		this.mp = mp;
	}
	
	public int getHp() {
		return hp;
	}
	
	public int getMp() {
		return mp;
	}
	
	public int getCost() {
		return cost;
	}
	
	public boolean use(Unit unit) {
		return false;
	}
	
	public boolean isPotion() {
		return true;
	}
}
