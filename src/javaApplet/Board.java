package javaApplet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;

public class Board {
	private Tile[][] board;
	
	public int rowSize;
	public int colSize;
	
	public Hero hero;
	
	private Hero[] heroes;
	private int numHeroes;
	private int maxNumHeroes;
	
	private Monster[] monsters;
	private int numMons;
	private int maxNumMons;
	
	private int maxNumPotions;
	private int maxNumAuras;
	private int maxNumTrees;
	
	// Used for hero's smooth moving 
	public static final int HERO_SPEED = 2; // 10일 때 두번 scroll이 자연스러움 작아질 수록 느려
	public static final int MOVING_MAX_FRAMES = 10;
	
	// Hero user interface starting row, column
	public static final int UI_HUD_HERO_STAT_BAR_START_ROW = 10;
	public static final int UI_HUD_HERO_STAT_BAR_START_COL = 10;
	
	// RPG initializing
	public static final int MAX_NUM_MONS = 10;
	public static final int MAX_NUM_POTIONS = 10;
	public static final int MAX_NUM_AURAS = 10;
	public static final int MAX_NUM_TREES = 20;
	public static final int MAX_NUM_HEROES = 10;
	
	// Hero view port range
	public static final int VIEWPORT_RANGE_ROW = HttpChatApplet.GAME_WINDOW_ROW_SIZE / 2;
	public static final int VIEWPORT_RANGE_COL = HttpChatApplet.GAME_WINDOW_COL_SIZE / 2;
	
	public static final String UNKNOWN_TILE = "#";
	
	public static final int HERO_STAT_ROW = 10;
	public static final int HERO_STAT_COL = 50;
	public static final int STRING_ROW = 10;
	public static final int STRING_COL = 15;
	public int statAreaWidth = Tile.IMAGE_WIDTH * 16;
	public int statAreaHeight = Tile.IMAGE_HEIGHT * 3;
	
	public Board(int rowSize, int colSize) {
		initBoard(rowSize, colSize);
		initMonsters();
		initPotions();
		initTrees();
		initGraphic();
	
	}
	
	

	public String toString() {
		String s = "";
		s += "+";
		for (int j = 1; j < board[0].length+1; j++) {
			s += "-";
		}
		s += "+" + '\n';
		
		for (int i = 0; i < board.length; i++) {
			s += "|";
			for (int j = 0; j < board[0].length; j++) {
				s += board[i][j].toString();
			}
			s += "|" + '\n';
		}
		
		s += "+";
		for (int j = 1; j < board[0].length+1; j++) {
			s += "-";
		}
		s += "+" + '\n';
		
		return s;
	}
	
	public String printBoardHeroView(Hero hero) {
		String s = "";
	    int startRow = hero.getRow() - VIEWPORT_RANGE_ROW;
	    int endRow = hero.getRow() + VIEWPORT_RANGE_ROW;
	    
	    int startCol = hero.getCol() - VIEWPORT_RANGE_COL;
	    int endCol = hero.getCol() + VIEWPORT_RANGE_COL;
	    
	    for (int i = startRow; i <= endRow; i++) {
	        for (int j = startCol; j <= endCol; j++) {
	            if (validate(i, j)) {
	            	s += board[i][j].toString();
	            }
	            else {
	                s += UNKNOWN_TILE;
	            }
	        }
	        s += '\n';
	    }
	    return s;
	}
	
	public void initBoard(int rowSize, int colSize) {
		this.rowSize = rowSize;
		this.colSize = colSize;
		board = new Tile[rowSize][colSize];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new Tile();
			}
		}
		maxNumHeroes = MAX_NUM_HEROES;
		heroes = new Hero[maxNumHeroes];
	}
	
	public Hero getHero(int index) {
		if (heroes[index] != null) {
			return heroes[index];
		}
		return null;
	}
	
	public Hero getHero(String userId) {
		for (int i = 0; i < maxNumHeroes; i++) {
			if (heroes[i] != null && heroes[i].getUserId().equals(userId)) {
				return heroes[i];
			}
		}
		return null;
	}
	
	public String printHeroes () {
		String hr = "";
		for (int i = 0; i < maxNumHeroes; i++) {
			if (heroes[i] != null) {
				hr += "[" + heroes[i].getUserId() + "]";
			}
		}
		return hr;
	}
	
	public void setHero(Hero hero) {
		int row = -1;
		int col = -1;
		while (true) {
			row = Main.r.nextInt(board.length);
			col = Main.r.nextInt(board[0].length);
			if (board[row][col].isEmpty()) {
				break;
			}
		}

		for (int i = 0; i < maxNumHeroes; i++) {
			if (heroes[i] == null) {
				heroes[i] = hero;
				while (true) {
					if (board[row][col].getUnit() == null) {
						hero.setRow(row);
						hero.setCol(col);
						hero.setBoard(this);
						board[row][col].setUnit(hero);
						break;
					}
					col++;
				}
				break;
			}
		}
	}
	
	public void initMonsters() {
		int row = 0;
		int col = 0;
		maxNumMons = MAX_NUM_MONS;
		monsters = new Monster[maxNumMons];

		for (int i = 0; i < maxNumMons; i++) {
			monsters[i] = new Monster();
			monsters[i].setBoard(this);
			numMons++;
			while (true) {
				row = Main.r.nextInt(board.length);
				col = Main.r.nextInt(board[0].length);
				if (board[row][col].isEmpty()) {
					setUnit(row, col, monsters[i]);
					break;
				}
			}
		}
	}
	
	public void initPotions() {
		maxNumPotions = MAX_NUM_POTIONS;
		for (int i = 0; i < maxNumPotions; i++) {
			while (true) {
				int row = Main.r.nextInt(board.length);
				int col = Main.r.nextInt(board[0].length);
				if (board[row][col].isEmpty()) {
					board[row][col].setItem(new HpPotion());
					break;
				}
			}
		}
		
		maxNumPotions = MAX_NUM_POTIONS;
		for (int i = 0; i < maxNumPotions; i++) {
			while (true) {
				int row = Main.r.nextInt(board.length);
				int col = Main.r.nextInt(board[0].length);
				if (board[row][col].isEmpty()) {
					board[row][col].setItem(new MpPotion());
					break;
				}
			}
		}
	}
	
	public void initTrees() {
		maxNumTrees = MAX_NUM_TREES;
		for (int i = 0; i < maxNumTrees; i++) {
			while (true) {
				int row = Main.r.nextInt(board.length);
				int col = Main.r.nextInt(board[0].length);
				if (board[row][col].isEmpty()) {
					board[row][col].setProp(new Tree());
					break;
				}
			}
		}
	}
	
	public void moveMons() {
		for (int i = 0; i < numMons; i++) {
			int randDir = Main.r.nextInt(Unit.NUM_DIR);
			if (monsters[i] != null) {
				monsters[i].move(randDir);
			}
		}
	}
	
	public boolean canUnitClimb(int row, int col) {
		return validate(row, col) &&  board[row][col].getUnit() == null && board[row][col].getProp() == null;
	}
	
	public boolean validate(int row, int col) {
		return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
	}
	
	public void setUnit(int row, int col, Unit unit) {
		board[row][col].setUnit(unit);
		if (unit != null) {
			unit.setRow(row);
			unit.setCol(col);
		}
	}
	
	public Unit getUnit(int row, int col) {
		return board[row][col].getUnit();
	}
	
	public void removeDeadUnit(int row, int col) {
		if (validate(row, col)) {
			if (board[row][col].getUnit() != null) {
				board[row][col].setUnit(null);
			}
		}
	}
	
	public Item getItem(int row, int col) {
		if (board[row][col].getItem() != null) {
			return board[row][col].getItem();
		}
		return null;
	}
	
	public void setItem(int row, int col, Item item) {
		board[row][col].setItem(item);
	}
	
	// Graphics will not be supported in java servlet environment so comment out the code below when u move this code to java servlet
	public Image unknownTileImage = null;
	public Image uiHudHeroStatBar = null;
	public Image alpha1 = null;
	
	public void initGraphic() {
		unknownTileImage = getImage("tile_0002_0001.png");	
//		uiHudHeroStatBar = getImage("slot_0001_33X33_edited.png");
		uiHudHeroStatBar = getImage("portrait_interface_0003.png");
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
	
	public void paintHUD(Graphics g, int startX, int startY, Hero hero, ImageObserver o) {
//		g.drawImage(uiHudHeroStatBar, startX+UI_HUD_HERO_STAT_BAR_START_ROW, startY+UI_HUD_HERO_STAT_BAR_START_COL, 150, 31, o); // Top left interface
//		int uiInterfaceStartX = startX;
//		int uiInterfaceStartY = (startY+HttpChatApplet.gameWindowHeight)-106;
//		g.drawImage(uiHudHeroStatBar, uiInterfaceStartX, uiInterfaceStartY, 792, 106, o); // Bottom left interface
//		g.drawImage(uiHudHeroStatBar, uiInterfaceStartX, uiInterfaceStartY, 146, 106, o);
		g.drawImage(uiHudHeroStatBar, 0, 0, 400, 106, o);
	}

	public void paintMinimap(Graphics g, int startX, int startY, Hero hero, ImageObserver o, int rowSize, int colSize) {
		// Inner rectangle for statistics printing 
		g.setColor(Color.black);
		for (int i = 0; i <= rowSize; i++) {
	        for (int j = 0; j <= colSize; j++) {
	        	// hero를 이제 부드러운 스크롤에서는 중앙에 직접 그려야 한다.
	        	// 그리고 tile에서는 그리지 않게 hero가 있으면 무시한다.
	            if (validate(i, j)) { 
	            	g.drawString(board[i][j].toString(), 50+j*4,50+ i*4);
	            }
	            
	    	}
		}
	}
	
	public void paintStat(Graphics g, int startX, int startY, Hero hero, ImageObserver o) {
		int innerRecX = startX + 10;
		int innerRecY = startY + 10;
		int innerRecWidth = statAreaWidth;
		int innerRecHeight = statAreaHeight;
		
		int outterRecX = innerRecX-2;
		int outterRecY = innerRecX-2;
		int outterRecWidth = statAreaWidth+4;
		int outterRecHeight = statAreaHeight+4;
		
		// Outter rectangle for border
		g.setColor(Color.lightGray);
		g.drawRect(outterRecX, outterRecY, outterRecWidth, outterRecHeight);
		
		// Inner rectangle for statistics printing 
		g.setColor(Color.black);
		g.drawRect(innerRecX, innerRecY, innerRecWidth, innerRecHeight);
		g.drawString(hero.printStat(), innerRecX+STRING_ROW, innerRecY+STRING_COL);
		g.drawString(hero.printEquipment(), innerRecX+STRING_ROW, innerRecY+STRING_COL*2);
		g.drawString(hero.printSlots(), innerRecX+STRING_ROW, innerRecY+STRING_COL*3);
		g.drawString(hero.printInventory(), innerRecX+STRING_ROW, innerRecY+STRING_COL*4);
		g.drawString("Current users : " + hero.getBoard().printHeroes(), innerRecX+STRING_ROW, innerRecY+STRING_COL*5);
		Unit curEnemy = hero.enemy;
		if (curEnemy != null && !curEnemy.isDead()) {
			g.drawString("Enemy : " + curEnemy.printStat(), innerRecX+STRING_ROW, innerRecY+STRING_COL*4);
		}
	}
	
	public int mMovingDir = -1;
	public boolean mMoving = false;
	public int mMovingMaxFrames = MOVING_MAX_FRAMES;
	public int mMovingCurFrame = 0;
	
	public void paintBoardHeroView(Graphics g, int startX, int startY, Hero hero, ImageObserver o) {
		//------------------------------------------------------------------------------------------------
		// Hero가 움직여야만 적을 check해 없에준, 그러므로 몬스터의 잔상이 생기는 현을 없에기 위해
		// 매번 update되는 Board paint() 에서 hero의 적을 check해 죽여주고 없에줘야 한다 
		//------------------------------------------------------------------------------------------------
		if (hero.enemy != null) {
			removeDeadUnit(hero.enemy.getRow(), hero.enemy.getCol());
			hero.incExp(hero.enemy.getExp());
			hero.incGold(hero.enemy.getGold());
			hero.enemy = null;
		}
		//------------------------------------------------------------------------------------------------		
		
		int curHeroRow = -1;
		int curHeroCol = -1;
		
		if (mMoving) {
			// 움직이는 동안에는 옛날 위치를 위주로 그린다 
			curHeroRow = hero.getPrevRow(); 
			curHeroCol = hero.getPrevCol();
		}
		else {
			// scroll이 끝난 직후부터는 새 위치를 위주로 그린다 
			curHeroRow = hero.getRow();
			curHeroCol = hero.getCol();
		}
		
		//====================================================================================
		// In order to move hero smoothly we need to move the entire board with a frame rate
		// and extra one line of tiles needed
		// acts like a buffer 
		//====================================================================================
	    int startRow = curHeroRow - VIEWPORT_RANGE_ROW - 1;
	    int endRow = curHeroRow + VIEWPORT_RANGE_ROW + 1;
	    
	    int startCol = curHeroCol - VIEWPORT_RANGE_COL - 1 ;
	    int endCol = hero.getPrevCol() + VIEWPORT_RANGE_COL + 1;
//	    int endCol = curHeroCol + VIEWPORT_RANGE_COL;

	    int curX = startX - Tile.IMAGE_WIDTH;
	    int curY = startY - Tile.IMAGE_HEIGHT;
	    
	    // 현재 에니메잍되고 있는 판때기의 시작 위치를 기록해놓아야만 매 row를 다시 그릴때 시작 x를 정확히 정할 수 있다.
	    // board를 그릴때, curX에 매번 다시 넣어주어야 한다.
	    int curStartX = curX;
	    
		//====================================================================================
	    // Hero smoothly moving
	    // If hero moved, mMoving flag turned on
	    // Board's starting x, y will be decreased matching our frame rates given
	    // When hero is moved, board starting x, y is decreased base on hero's previous position 
	    // (when smoothly moving)
	    //
	    // After hero is on the next tile, board is printed base on hero's current position
		//====================================================================================
	    //------------------------------------------------------------------------------------
	    // Hero moving north smoothly
	    //------------------------------------------------------------------------------------
	    if (mMoving && mMovingDir == Hero.DIR_N) {
	    	curY = curY + (Tile.IMAGE_HEIGHT/mMovingMaxFrames) * mMovingCurFrame;
	    	if (HttpChatApplet.count % HERO_SPEED == 0) {
	    		mMovingCurFrame++;
	    	}
	    	
	    	if (mMovingCurFrame == mMovingMaxFrames) {
	    		mMoving = false; ///?????????????????????  Hero의 move()에서 켠 것이 cancel될 수도 있으므로.. 나중에 다음 move가 set되도록 하는 변수를 이용할 수도 있다.
	    		mMovingCurFrame = 0;
	    	}
	    }
	    //------------------------------------------------------------------------------------
	    // Hero moving east smoothly
	    //------------------------------------------------------------------------------------
	    else if (mMoving && mMovingDir == Hero.DIR_E) {
	    	curX = curX - (Tile.IMAGE_WIDTH/mMovingMaxFrames) * mMovingCurFrame;
	    	curStartX = curX;
	    	if (HttpChatApplet.count % HERO_SPEED == 0) {
//	    		System.out.println("mMovingCurFrame :" + mMovingCurFrame);
	    		mMovingCurFrame++;
	    	}
	    	
	    	if (mMovingCurFrame == mMovingMaxFrames) {
	    		mMoving = false;
	    		mMovingCurFrame = 0;
	    	}
	    }
	    //------------------------------------------------------------------------------------
	    // Hero moving south smoothly
	    //------------------------------------------------------------------------------------
	    else if (mMoving && mMovingDir == Hero.DIR_S) {
	    	curY = curY - (Tile.IMAGE_HEIGHT/mMovingMaxFrames) * mMovingCurFrame;
	    	if (HttpChatApplet.count % HERO_SPEED == 0) {
	    		mMovingCurFrame++;
	    	}
	    	
	    	if (mMovingCurFrame == mMovingMaxFrames) {
	    		mMoving = false;
	    		mMovingCurFrame = 0;
	    	}
	    }
	    //------------------------------------------------------------------------------------
	    // Hero moving west smoothly
	    //------------------------------------------------------------------------------------
	    else if (mMoving && mMovingDir == Hero.DIR_W) {
	    	curX = curX + (Tile.IMAGE_WIDTH/mMovingMaxFrames) * mMovingCurFrame;
	    	curStartX = curX;
	    	if (HttpChatApplet.count % HERO_SPEED == 0) {
	    		mMovingCurFrame++;
	    	}
	    	
	    	if (mMovingCurFrame == mMovingMaxFrames) {
	    		mMoving = false;
	    		mMovingCurFrame = 0;
	    	}
	    }
	    //------------------------------------------------------------------------------------

	    for (int i = startRow; i <= endRow; i++) {
	        for (int j = startCol; j <= endCol; j++) {
	        	// hero를 이제 부드러운 스크롤에서는 중앙에 직접 그려야 한다.
	        	// 그리고 tile에서는 그리지 않게 hero가 있으면 무시한다.
	            if (validate(i, j)) { 
	            	board[i][j].paint(g, curX, curY, o);
	            }
	            else { // draw out of bound image 
	        		g.drawImage(unknownTileImage, curX, curY, Tile.IMAGE_WIDTH, Tile.IMAGE_HEIGHT, o);
	            }
	            curX += Tile.IMAGE_WIDTH;
	        }
	        // next starting point(x, y) should be set
	        curX = curStartX;
	        curY += Tile.IMAGE_HEIGHT;
	    }
	    
		//====================================================================================
	    // Hero를 부드러운 스크롤에서는 중앙에 직접 그려야 한다.
	    // For hero to move smoothly, hero should be printed after the entire board is printed
	    // and hero should not be printed on tile now
		//====================================================================================
	    hero.paint(g, startX+(VIEWPORT_RANGE_COL * Tile.IMAGE_WIDTH), startY+(VIEWPORT_RANGE_ROW * Tile.IMAGE_HEIGHT), o);
	    
//	    testing mouse listener
//		super.paint(g);		
//		
//		if ( isButtonPressed ) {
//			g.setColor( Color.black );
//		}
//		else {
//			g.setColor( Color.gray );
//		}
//		g.fillRect( mx-20, my-20, 40, 40 );		
//
//		for (int i = 1; i <= 12; i++) {
//			spriteAnimate[i-1] = getImage("animate" + i + ".png");
//		}
//		
//		g.clearRect(10, 100, 100, 100);
//		g.drawImage(spriteAnimate[j], 10, 100, 100, 100, this);
//		if (j < 11) {
//			j++;
//		}
//		else {
//			j = 0;
//		}
	}
	
	public char getFx(int row, int col) {
		return board[row][col].getFx();
	}
	
	public void setFx(int row, int col, char fx) {
		board[row][col].setFX(fx);
	}
	
	public void clearFx(int row, int col) {
		board[row][col].clearFx();
	}
}
