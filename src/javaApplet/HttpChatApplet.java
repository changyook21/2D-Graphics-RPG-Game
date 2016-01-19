package javaApplet;
import java.applet.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.Format;
import java.util.*;

import javax.sound.sampled.AudioFormat;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JApplet;

import com.oreilly.servlet.HttpMessage;


import javaApplet.MoveToText.FindTextPane;

//import javafx.scene.input.KeyCode;

//import javaApplet.MyFrame.MyDispatcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
//BGM ------------------------------------------------------------------------
import java.applet.AudioClip;
import java.applet.Applet;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import java.net.URL;
import java.net.MalformedURLException;

public class HttpChatApplet extends JPanel implements Runnable, MouseListener, KeyEventDispatcher, MouseMotionListener {
	
	//----------------------------------------------------------------------
	// Temporary board initialization
	//----------------------------------------------------------------------
	public Board board = null;
	public Hero hero = new Hero("hero1");
	char c = '\0';
	
	//----------------------------------------------------------------------
	// Hero move related
	//----------------------------------------------------------------------
	public static final char NORTH_KEY = 'W';
	public static final char EAST_KEY = 'D';
	public static final char SOUTH_KEY = 'S';
	public static final char WEST_KEY = 'A';
	
	public static final int DIR_N = 0;
	public static final int DIR_E = 1;
	public static final int DIR_S = 2;
	public static final int DIR_W = 3;
	
	public static final int HERO_SLOT_INDEX_ONE = 1;
	public static final int HERO_SLOT_INDEX_TWO = 2;
	public static final int HERO_SLOT_INDEX_THREE = 3;
	
	//----------------------------------------------------------------------
	// Timer for board animation (Frame rate)
	//----------------------------------------------------------------------
	public static final int FPS = 60;								
	public static final int TIMER_DELAY = 1000 / FPS;  	// 16.6666ms , 60fps

	public Timer timer;
	public static int count;
	
	//====================================================================================================
	// Components' constants positions (x, y) and sizes (width, height)
	//====================================================================================================
	public static final int FRAME_WIDTH = 1000;
	public static final int FRAME_HEIGHT = 738; // frame height that shows on mac 15"
//	public static final int FRAME_HEIGHT = 1000;
	
	//----------------------------------------------------------------------
	// RPG screen size initialization
	//----------------------------------------------------------------------
	public static int GAME_WINDOW_ROW_SIZE = 13;	// 13
	public static int GAME_WINDOW_COL_SIZE = 33;	// 33
	
	public static int gameWindowRow = GAME_WINDOW_ROW_SIZE;
	public static int gameWindowCol = GAME_WINDOW_COL_SIZE;

	public static int gameWindowWidth = Tile.IMAGE_WIDTH * gameWindowCol;
	public static int gameWindowHeight = Tile.IMAGE_HEIGHT * gameWindowRow;
	
	//----------------------------------------------------------------------
	// BOARD_X
	//----------------------------------------------------------------------
	public static int BOARD_X = (FRAME_WIDTH - gameWindowWidth) / 2;
	//----------------------------------------------------------------------
	
	public static final int DEFAULT_SLIT = 10; // interval between components
	
	//----------------------------------------------------------------------
	// Upper layout sizes, contains, button get, post, text field id, pwd
	//----------------------------------------------------------------------
	public static int upperLayoutX = BOARD_X;
	public static int upperLayoutY = DEFAULT_SLIT;
	
	public static int upperLayoutWidth = gameWindowWidth;
	public static int upperLayoutHeight = gameWindowHeight/8; 
	//----------------------------------------------------------------------
	
	public static int btGetX = 0;
	public static int btGetY = 0;
	public static int btGetWidth = (upperLayoutWidth-DEFAULT_SLIT*3)/4;
	public static int btGetHeight = upperLayoutHeight;
	
	public static int btPostX = btGetWidth + DEFAULT_SLIT;
	public static int btPostY = 0;
	public static int btPostWidth = (upperLayoutWidth-DEFAULT_SLIT*3)/4;
	public static int btPostHeight = upperLayoutHeight;
	
	public static int tfNameX = btPostX + btPostWidth + DEFAULT_SLIT;
	public static int tfNameY = 0;
	public static int tfNameWidth = (upperLayoutWidth-DEFAULT_SLIT*3)/4;
	public static int tfNameHeight = upperLayoutHeight;
	
	public static int tfPwdX = tfNameX + tfNameWidth + DEFAULT_SLIT;
	public static int tfPwdY = 0;
	public static int tfPwdWidth = (upperLayoutWidth-DEFAULT_SLIT*3)/4;
	public static int tfPwdHeight = upperLayoutHeight;

	//----------------------------------------------------------------------
	// BOARD_Y
	//----------------------------------------------------------------------
	public static final int BOARD_Y = upperLayoutY + upperLayoutHeight + DEFAULT_SLIT;
	//----------------------------------------------------------------------
	
	//----------------------------------------------------------------------	
	// Text area panel
	//----------------------------------------------------------------------
	public static int taMessageX = BOARD_X;
	public static int taMessageY = BOARD_Y + gameWindowHeight + DEFAULT_SLIT;
	public static int taMessageWidth = gameWindowWidth;
	public static int taMessageHeight = gameWindowHeight/4;
	//----------------------------------------------------------------------
	
	//----------------------------------------------------------------------
	// Lower layout sizes, contains, button get, post, text field id, pwd
	//----------------------------------------------------------------------
	public static int lowerLayoutX = BOARD_X;
	public static int lowerLayoutY = taMessageY + taMessageHeight + DEFAULT_SLIT;
	public static int lowerLayoutWidth = upperLayoutWidth;
	public static int lowerLayoutHeight = upperLayoutHeight;
	//----------------------------------------------------------------------
	
	public static int tfMessageX = 0;
	public static int tfMessageY = 0;
	public static int tfMessageWidth = lowerLayoutWidth/5*4;
	public static int tfMessageHeight = lowerLayoutHeight;
	
	public static int btSendX = tfMessageX + tfMessageWidth + DEFAULT_SLIT;
	public static int btSendY = tfMessageY;
	public static int btSendWidth = lowerLayoutWidth - tfMessageWidth - DEFAULT_SLIT;
	public static int btSendHeight = lowerLayoutHeight;
	
	
	//====================================================================================================

	
	public static int curWindowWidth;
	public static int curWindowHeight;
	
	public static int prevWindowWidth;
	public static int prevWindowHeight;
	
public static HttpChatApplet httpChatApplet;
	
	//----------------------------------------------------------------------
	// Panels that are gonna be added to the JLayeredPanel
	//----------------------------------------------------------------------
	public static JScrollPane scroller;
	public static TextAreaPanel textAreaPanel;
	public static InventoryPanel inventoryPanel;
	public static EquipmentPanel equipmentPanel;
	public static PortraitPanel portraitPanel;
	public static MinimapPanel minimapPanel;

	public static JPanel upperLayout;
	public static JPanel lowerLayout;
	
	public static JLayeredPane mainPanel = null;
	
	public JFrame frame = null;
	
	//----------------------------------------------------------------------
	// Components to be added to the Applet client's screen
	//----------------------------------------------------------------------
	private static JButton btGet = new JButton("Get");
	private static JButton btPost = new JButton("Post");
	private static JButton btSend = new JButton("Send");
	private static JTextField tfName = new JTextField(30);
	private static JTextField tfPwd = new JTextField(30);
	private static JTextField tfMessage = new JTextField(30);
	private JTextArea taResultBoard = new JTextArea(40, 80);
	private JTextArea taResultMessage = new JTextArea(10, 80);
	
	//----------------------------------------------------------------------
	// Thread related
	//----------------------------------------------------------------------
	boolean connected = false;
	boolean threadSuspended;
	
	Thread t = null;		// use to constantly refresh page
	HttpMessage msg = null; // use to pass values to ChatServlet
	
	//----------------------------------------------------------------------
	// Mouse related
	//----------------------------------------------------------------------
	int width, height;
	int mx, my;  			// the mouse coordinates
	boolean isButtonPressed = false;
	boolean isInventoryPanelPressed = false;
	boolean isEquipmentPanelPressed = false;
	
//	Rectangle inventoryRectangle = null;
	
	Point p = new Point(0, 0);
	
	public int clickedOffsetXForInventoryPanel;
	public int clickedOffsetYForInventoryPanel;
	
	public int clickedOffsetXForEquipmentPanel;
	public int clickedOffsetYForEquipmentPanel;
	
	//----------------------------------------------------------------------
	// Image, Background music related
	//----------------------------------------------------------------------
	int i = 0;
	int j = 0;
	
	URL url = null;
	Image img = null;
	AudioClip bgm = null;

	//======================================================================	
	// Variables used in Timer run() 
	//====================================================================== 
	
	//----------------------------------------------------------------------
	// Boolean flag needed so that we know if user is holding down a moving key 
	//----------------------------------------------------------------------
	public boolean northKeyHeldDown = false;
	public boolean eastKeyHeldDown = false;
	public boolean southKeyHeldDown = false;
	public boolean westKeyHeldDown = false;
	
	//----------------------------------------------------------------------
	// Counter needed so that after hero moving key is pressed for certain time, keep moving hero
	//----------------------------------------------------------------------
	public int northKeyHeldDownCounter = 0;
	public int eastKeyHeldDownCounter = 0;
	public int southKeyHeldDownCounter = 0;
	public int westKeyHeldDownCounter = 0;
	
	public static final int DIRECTION_KEY_SPEED = 20; // hero is moved every 1/3 seconds (60 % 20 == 0)
	
	//----------------------------------------------------------------------
	// Boolean flag use to draw hero's statistics and inventory panel
	//----------------------------------------------------------------------
	public static boolean statBarOn = false;
	public static boolean invenBarOn = false;
	public static boolean equipBarOn = false;
	public static boolean minimapBarOn = false;

	//----------------------------------------------------------------------
	// When message is sent, the message less than 17 characters will be drawn on top of speaking hero
	//----------------------------------------------------------------------
	public static String textMessage = "";
	public static boolean messageSent = false;
	public int messageSentCounter = 0;
	//====================================================================== 
	
	// Handle mouse clicked on item icons
	public static boolean inventoryItemClicked = false;
	public static boolean slotItemClicked = false;
	public static boolean equipmentItemClicked = false;
	public Image curImageMoving;
	
	public class RepaintTimer extends java.util.TimerTask
	{
		public boolean isRunning;

		public RepaintTimer() {
			count = 0;
			isRunning = true;
		}
		
		public void run() //this becomes the loop
	    {
//			System.out.println("count="+(count/60)+"\n");
			count++;
			
			Rectangle r = frame.getBounds();
			curWindowWidth = r.width;
			curWindowHeight = r.height;
			
			if ((curWindowWidth != prevWindowWidth || curWindowHeight != prevWindowHeight )&& count % 100 == 0) { // || curWindowHeight != prevWindowHeight

				gameWindowRow = gameWindowRow - ((prevWindowHeight - curWindowHeight) / Tile.IMAGE_HEIGHT);
				gameWindowCol = gameWindowCol - ((prevWindowWidth - curWindowWidth) / Tile.IMAGE_WIDTH);
				gameWindowWidth = Tile.IMAGE_WIDTH * gameWindowCol;
				gameWindowHeight = Tile.IMAGE_HEIGHT * gameWindowRow;
				
				BOARD_X = (curWindowWidth - gameWindowWidth)/2;
				
				prevWindowWidth = curWindowWidth;
				prevWindowHeight = curWindowHeight;
				
				upperLayoutX = BOARD_X;
				upperLayoutY = DEFAULT_SLIT;
				upperLayoutWidth = gameWindowWidth;
				upperLayoutHeight = gameWindowHeight/8;
//				System.out.println("gameWindowHeight : " + gameWindowHeight);
//				System.out.println("upperLayoutHeight : " + upperLayoutHeight);

//				System.out.println("upperLayoutHeight : " + upperLayoutHeight);
				
				btGetX = 0;
				btGetY = 0;
				btGetWidth = upperLayoutWidth/4;
				btGetHeight = upperLayoutHeight;
				
				btPostX = btGetWidth;
				btPostY = 0;
				btPostWidth = upperLayoutWidth/4;
				btPostHeight = upperLayoutHeight;
				
				tfNameX = btPostX + btPostWidth;
				tfNameY = 0;
				tfNameWidth = upperLayoutWidth/4;
				tfNameHeight = upperLayoutHeight;
				
				tfPwdX = tfNameX + tfNameWidth;
				tfPwdY = 0;
				tfPwdWidth = upperLayoutWidth/4;
				tfPwdHeight = upperLayoutHeight;

				taMessageX = BOARD_X;
				taMessageY = BOARD_Y + gameWindowHeight + DEFAULT_SLIT;
				taMessageWidth = gameWindowWidth;
				taMessageHeight = gameWindowHeight/4;
				
				lowerLayoutX = BOARD_X;
				lowerLayoutY = taMessageY + taMessageHeight + DEFAULT_SLIT;
				lowerLayoutWidth = gameWindowWidth;
				lowerLayoutHeight = upperLayoutHeight;
						
				tfMessageX = 0;
				tfMessageY = 0;
				tfMessageWidth = lowerLayoutWidth/5 * 4;
				tfMessageHeight = lowerLayoutHeight;
				
				btSendX = tfMessageX + tfMessageWidth;
				btSendY = tfMessageY;
				btSendWidth = lowerLayoutWidth - tfMessageWidth;
				btSendHeight = lowerLayoutHeight;
				
				btGet.setBounds(0, btGetY, btGetWidth, btGetHeight);
				btPost.setBounds(btPostX, btPostY, btPostWidth, btPostHeight);
				tfName.setBounds(tfNameX, tfNameY, tfNameWidth, tfNameHeight);
				tfPwd.setBounds(tfPwdX, tfPwdY, tfPwdWidth, tfPwdHeight);
				tfMessage.setBounds(0, 0, tfMessageWidth, tfMessageHeight);
				btSend.setBounds(btSendX, 0, btSendWidth, btSendHeight);
				
				upperLayout.setBounds(upperLayoutX, upperLayoutY, upperLayoutWidth, upperLayoutHeight);
				
				scroller.setBounds(0, 0, taMessageWidth, taMessageHeight);
				textAreaPanel.setBounds(taMessageX, taMessageY, taMessageWidth, taMessageHeight);
				
				lowerLayout.setBounds(lowerLayoutX, lowerLayoutY, lowerLayoutWidth, lowerLayoutHeight);
//				System.out.println("taMessageY : " + taMessageY);
//				System.out.println("lowerLayoutY : " + lowerLayoutY);
				
				httpChatApplet.setBounds(BOARD_X, BOARD_Y, gameWindowWidth, gameWindowHeight);
				
				inventoryPanel.setBounds(BOARD_X + 4, BOARD_Y + 110, 260, 124);
				equipmentPanel.setBounds(BOARD_X + 400, BOARD_Y + 110, 322, 254);
				
				portraitPanel.setBounds(BOARD_X, BOARD_Y, 400, 106);
//				if (inventoryRectangle == null) {
//					inventoryRectangle = new Rectangle(0, 0, 260, 124);
//				}
			}
			
			//----------------------------------------------------------------------
			// Keep moves hero on the same direction when user is holding down a moving key for certain time
			// handling 4 directions
			//----------------------------------------------------------------------
			if (northKeyHeldDown) {
				if (northKeyHeldDownCounter % DIRECTION_KEY_SPEED == 0) {
					board.getHero("hero1").move(DIR_N);
				}
				northKeyHeldDownCounter++;
			}
			if (eastKeyHeldDown) {
				if (eastKeyHeldDownCounter % DIRECTION_KEY_SPEED == 0) {
					board.getHero("hero1").move(DIR_E);
				}
				eastKeyHeldDownCounter++;
			}
			if (southKeyHeldDown) {
				if (southKeyHeldDownCounter % DIRECTION_KEY_SPEED == 0) {
					board.getHero("hero1").move(DIR_S);
				}
				southKeyHeldDownCounter++;
			}
			if (westKeyHeldDown) {
				if (westKeyHeldDownCounter % DIRECTION_KEY_SPEED == 0) {
					board.getHero("hero1").move(DIR_W);
				}
				westKeyHeldDownCounter++;
			}
			
			//----------------------------------------------------------------------
			// when 'H' key is pressed draw statistics of hero on the top left of the game screen
			//----------------------------------------------------------------------
			if (messageSent) {
				messageSentCounter++;
				if (messageSentCounter == 150) {
					messageSentCounter = 0;
					messageSent = false;
				}
			}
			
	        //doGameUpdates();
			if (count % (FPS) == 0) {
				board.moveMons();	
			}
			
	        //render();
			repaint();
			
	        if (!isRunning) {
	            timer.cancel();
	        }
	    }
	}

	//==================================================================================================== main()
    public static void main(String[] args) {
	    JFrame frame = new JFrame();
	    frame.setTitle("Sketch");
	    frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		prevWindowWidth = frame.getWidth();
		prevWindowHeight = frame.getHeight();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    
		
		
		
		//================================================================================
	    // Minimap panel that will be printed when 'M' key is pressed
	    //================================================================================
		minimapPanel = new MinimapPanel();
		minimapPanel.setBounds(gameWindowWidth-BOARD_X-DEFAULT_SLIT, BOARD_Y+DEFAULT_SLIT, 200, 200);
		
		minimapPanel.setLayout(null);
		minimapPanel.setOpaque(false);
		minimapPanel.setBackground(new Color(0,0,0,0));

	    httpChatApplet = new HttpChatApplet();
	    httpChatApplet.frame = frame;
	    httpChatApplet.setBounds(BOARD_X, BOARD_Y, gameWindowWidth, gameWindowHeight);

	    //================================================================================
		// Making a panel for text area that prints chat messages 
		// Because in order to scroll text areas, 
		// we need to add a new panel to the entire frame
		// then add scrollable text area to the panel
	    //================================================================================
		textAreaPanel = new TextAreaPanel();
		textAreaPanel.setBounds(taMessageX, taMessageY, taMessageWidth, taMessageHeight);
		textAreaPanel.setLayout(null);
	    
	    //================================================================================
	    // Inventory panel & Equipment panel that will be printed when 'H' key is pressed
	    //================================================================================
		inventoryPanel = new InventoryPanel();
		inventoryPanel.setLayout(null);
		inventoryPanel.setBounds(BOARD_X + 4, BOARD_Y + 110, 260, 124);
		
		equipmentPanel = new EquipmentPanel();
		equipmentPanel.setLayout(null);
		equipmentPanel.setBounds(BOARD_X + 400, BOARD_Y + 110, 322, 254);
		
		//================================================================================
	    // Portrait panel including health bar, mana bar, portrait, slots are going to be painted on this panel 
	    //================================================================================
		portraitPanel = new PortraitPanel();
		portraitPanel.setLayout(null);
		portraitPanel.setOpaque(false);
		portraitPanel.setBounds(BOARD_X, BOARD_Y, 400, 106);
		
	    //================================================================================
		// Arranging painting orders, using JLayeredPane
	    //================================================================================
		mainPanel = new JLayeredPane();
		mainPanel.setLayout(null);
		mainPanel.add(httpChatApplet, new Integer(1));
		mainPanel.add(textAreaPanel, new Integer(1));
		mainPanel.add(inventoryPanel, new Integer(2));
		mainPanel.add(equipmentPanel, new Integer(2));
		mainPanel.add(portraitPanel, new Integer(2));
		mainPanel.add(minimapPanel, new Integer(3));
		
		
		//================================================================================
		// we have to call init() manually
		// initializing components that will be added to the screen
	    //================================================================================
	    httpChatApplet.init();
	    
      	httpChatApplet.frame.setContentPane(mainPanel);

	    frame.setVisible(true);
	}


	//==================================================================================================== init()
	public void init() {
		httpChatApplet = this;
		//setLayout(null);
		
		//----------------------------------------------------------------------
	    // BGM
	    //----------------------------------------------------------------------
        Thread t1 = new Thread() {
            public void run() {
            	try {
            		//URL url = new URL("file:///Users/Shared/study/wy/server/workspace/rpgClient/src/javaApplet/Peppy--The-Firing-Squad_YMXB-160.mp3");
            		//URL url = (new java.io.File("src/javaApplet/Peppy--The-Firing-Squad_YMXB-160.mp3")).toURI().toURL();
            		URL url = (new java.io.File("src/javaApplet/8-bit-music-loop.mp3")).toURI().toURL();
            		
            		AudioClip bgmClip = Applet.newAudioClip(url);
            		bgmClip.loop();
	            } catch(MalformedURLException murle) {
	                murle.printStackTrace();
	            }
            }
        };
        t1.start();
        t1.run();
        SwingUtilities.invokeLater(t);
	    //----------------------------------------------------------------------
		addMouseListener( this );
		addMouseMotionListener( this );
		
		// Total screen size
		width = getSize().width;
		height = getSize().height;
		
		// Mouse x, y
		mx = width/2;
		my = height/2;
       
		// Get button
		btGet.addActionListener(new GetButtonListener());
		btGet.setBounds(0, btGetY, btGetWidth, btGetHeight);
       
		// Post button
		btPost.addActionListener(new PostButtonListener());
		btPost.setBounds(btPostX, btPostY, btPostWidth, btPostHeight);
		
		// Name text field
		tfName.setBounds(tfNameX, tfNameY, tfNameWidth, tfNameHeight);

		// Password text field
		tfPwd.setBounds(tfPwdX, tfPwdY, tfPwdWidth, tfPwdHeight);
		
		// Board printing text area
//		taResultBoard.setFont(new Font("monospaced", Font.PLAIN, 12));
//		taResultBoard.setBounds(TA_BOARD_X, TA_BOARD_Y, TA_BOARD_WIDTH, TA_BOARD_HEIGHT);
		
		// Message printing text area
		taResultMessage.setFont(new Font("monospaced", Font.PLAIN, 12));
//		taResultMessage.setBounds(taMessageX, taMessageY - BOARD_Y, taMessageWidth, taMessageHeight);
//		taResultMessage.setBounds(taMessageX, 660, taMessageWidth, taMessageHeight);

		taResultMessage.setFocusable(true);
	    taResultMessage.requestFocusInWindow();
		taResultMessage.setVisible(true);
		taResultMessage.setWrapStyleWord(true);
		taResultMessage.setEditable(true);
		
		scroller = new JScrollPane(taResultMessage);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setViewportView(taResultMessage);
        scroller.setBounds(0, 0, taMessageWidth, taMessageHeight);
        textAreaPanel.add(scroller);
        
		// Message sending text field
		tfMessage.setFont(new Font("monospaced", Font.PLAIN, 12));
		tfMessage.addActionListener(new MessageListener());
		tfMessage.setBounds(0, 0, tfMessageWidth, tfMessageHeight);

		// Message sending button
		btSend.addActionListener(new SendButtonListener());
		btSend.setBounds(btSendX, 0, btSendWidth, btSendHeight);

		upperLayout = new JPanel();
		upperLayout.setBounds(upperLayoutX, upperLayoutY, upperLayoutWidth, upperLayoutHeight);
		upperLayout.setLayout(null);
		upperLayout.add(btGet);
		upperLayout.add(btPost);
		upperLayout.add(tfName);
		upperLayout.add(tfPwd);
		
		lowerLayout = new JPanel();
		lowerLayout.setBounds(lowerLayoutX, lowerLayoutY, lowerLayoutWidth, lowerLayoutHeight);
		lowerLayout.setLayout(null);
//		lowerLayout.setVisible(true);
		lowerLayout.add(tfMessage);
		lowerLayout.add(btSend);
		
		mainPanel.add(upperLayout, new Integer(1));
		mainPanel.add(lowerLayout, new Integer(1));
		
	    //----------------------------------------------------------------------
		// Temporary RPG initialization
	    //----------------------------------------------------------------------
		board = new Board(30, 30);

		board.setHero(hero);

		inventoryPanel.setHero(hero);
		equipmentPanel.setHero(hero);

		portraitPanel.setHero(hero);
		minimapPanel.setBoard(board);

	    //----------------------------------------------------------------------
		// Key adapter
	    //----------------------------------------------------------------------
		add(new JTextField());
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    manager.addKeyEventDispatcher(new MyDispatcher());
	        
		//==================================================================================================== 
		// Because of keyListener doesn't work in jFrame, so we use Input, Action map to get key inputs
		//====================================================================================================
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "attack");
		getActionMap().put("attack", new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	board.getHero("hero1").attack(hero.prevMovement);
	            //repaint();
	        }
	    });
		
		//---------------------------------------------------------------------
		// Timer created for gaming frame rate
		//---------------------------------------------------------------------
	    timer = new Timer();
	    timer.schedule(new RepaintTimer(), 0, TIMER_DELAY); //new timer at 60 fps, the timing mechanism
	}   

    public class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
            	if ((char)e.getKeyCode() == NORTH_KEY) {
//            		System.out.println((char)e.getKeyCode() + " pressed!");
            		northKeyHeldDown = true;
            	}
            	if ((char)e.getKeyCode() == EAST_KEY) {
            		eastKeyHeldDown = true;
            	}            
            	if ((char)e.getKeyCode() == SOUTH_KEY) {
            		southKeyHeldDown = true;
            	}
            	if ((char)e.getKeyCode() == WEST_KEY) {
            		westKeyHeldDown = true;
            	}
            	
            	if ((char)e.getKeyCode() == 'H') {
            		statBarOn = !statBarOn;
            		invenBarOn = !invenBarOn;
            		
            	}
            	if ((char)e.getKeyCode() == 'I') {
            		equipBarOn = !equipBarOn;
            	}
            	
            	if ((char)e.getKeyCode() == 'M') {
            		minimapBarOn = !minimapBarOn;
            	}
            	
            	for (int i = 0; i < hero.slots.capacity(); i++) {
            		char c = (char)(i + '0');
            		if ((char)e.getKeyCode() == c) {
            			System.out.println("i :" + i);
            			System.out.println("c :" + c);
            			board.getHero("hero1").useItemSlot(i-1);
            		}
            	}
            	
            	if ((char)e.getKeyCode() == '/') {
            	    tfMessage.setFocusable(true);
            	    tfMessage.requestFocusInWindow();
            	}
            	
            	if ((char)e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            	    httpChatApplet.setFocusable(true);
            	    httpChatApplet.requestFocusInWindow();
            	}
            } 
            if (e.getID() == KeyEvent.KEY_RELEASED) {
            	if ((char)e.getKeyCode() == NORTH_KEY) {
            		northKeyHeldDown = false;
            		northKeyHeldDownCounter = 0; // must reset
//            		System.out.println((char)e.getKeyCode() + " released!");	
            	}
            	if ((char)e.getKeyCode() == EAST_KEY) {
            		eastKeyHeldDown = false;	
            		eastKeyHeldDownCounter = 0; // must reset
            	}
            	if ((char)e.getKeyCode() == SOUTH_KEY) {
            		southKeyHeldDown = false;
            		southKeyHeldDownCounter = 0; // must reset
//            		System.out.println((char)e.getKeyCode() + " released!");
            	}
            	if ((char)e.getKeyCode() == WEST_KEY) {
            		westKeyHeldDown = false;
            		westKeyHeldDownCounter = 0; // must reset
            	}
            } 
            return false;
        }
    }

	//==================================================================================================== getIamge()
	// This is the function we made to get Image by URL
	// We could use "Toolkit.getDefaultToolkit().getImage(imageURL)" directly
	//==================================================================================================== 
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
	
	//==================================================================================================== paint()
	// We used thread to constantly repaint (call paint() method) to print board  
	// In order to print board as graphics , we passes Graphic g as parameter
	// Then tile calls each objects paint() method
	//
	// repaint() controls the update(), paint() cycle
	// We used JFrame instead of JApplet so that the screen won't flank
	//==================================================================================================== 
	@Override
	public void paint(Graphics g)
	{    
		super.paint(g);
		
		if (invenBarOn) {
			inventoryPanel.setVisible(true);
		}
		else {
			inventoryPanel.setVisible(false);	
		}
		if (equipBarOn) {
			equipmentPanel.setVisible(true);
		}
		else {
			equipmentPanel.setVisible(false);	
		}
		board.paintBoardHeroView(g, 0, 0, hero, this);	// print board as hero view port
		board.paintHUD(g, 0, 0, hero, this); 			// upper HUD position
		if (statBarOn) {
			board.paintStat(g, 400, 0, hero, this);
		}

		if (minimapBarOn) {
			minimapPanel.setVisible(true);
			//board.paintMinimap(g, 0, 0, hero, this, 30, 30);
			//minimapPanel.repaint();
		}
		else {
			minimapPanel.setVisible(false);	
		}

		if (messageSent && textMessage.length() <= 17) {
			hero.printMessage(g, 0, 0, hero, this, textMessage);
		}
		
		if (invenBarOn && inventoryItemClicked) {
			inventoryPanel.paintOnOther(g, inventoryPanel.getX() - getX(), inventoryPanel.getY() - getY());
		}
		
		if (equipBarOn && equipmentItemClicked) {
			equipmentPanel.paintOnOther(g, equipmentPanel.getX() - getX(), equipmentPanel.getY() - getY());
		}
		
		if (slotItemClicked) {
//			inventoryPanel.paintOnGameWindow(g);
			portraitPanel.paintOnOther(g, portraitPanel.getX() - getX(), portraitPanel.getY() - getY());
		}
	}
	
	//==================================================================================================== start()
	// This is called when the thread starts
	//==================================================================================================== 
	public void start() {
		if (!connected) {
			return;
		}
		System.out.println("start(): begin");
			if ( t == null ) {
			System.out.println("start(): creating thread");
			t = new Thread( this );
			System.out.println("start(): starting thread");
			threadSuspended = false;
			t.start();
		}
		else {
			if ( threadSuspended ) {
				threadSuspended = false;
				System.out.println("start(): notifying thread");
				synchronized( this ) { notify();}
			}
		}
		System.out.println("start(): end");
	}
	
	// Executed whenever the browser leaves the page containing the applet.
	//==================================================================================================== stop()
	public void stop() {
		System.out.println("stop(): begin");
		threadSuspended = true;
	}

	//==================================================================================================== run()
	// This is where repaint() method is called
	// Which thread is calling constantly
	// Acts like a while loop in C++ RPG project
	//==================================================================================================== 
	public void run() {
		System.out.println("run(): begin");
		try {
			while (true) {
			System.out.println("run(): awake");
			//Here's where the thread does some work
			++i;  // this is shorthand for "i = i+1;"
			if ( i == 10 ) {
				i = 0;
			}
			
			//showStatus( "i is " + i );
			//Now the thread checks to see if it should suspend itself
			if ( threadSuspended ) {
				synchronized( this ) {
					while ( threadSuspended ) {
						System.out.println("run(): waiting");
						wait();
					}
				}
			}
			System.out.println("run(): requesting repaint");
		         
			//repaint(); // paint(g) will be called

			try{
				//do what you want to do before sleeping
				//Thread.currentThread().sleep(300);//sleep for 500 ms
				Thread.currentThread().sleep(1);
				System.out.println("Thread.currentThread().sleep()");

				//do what you want to do after sleeptig
			}
			catch(InterruptedException ie){
				//If this thread was intrrupted by nother thread 
			}
			
			//-------------------------- Moving monsters on board
			//board.moveMons();

		    try {
				//====================================================================================================
				// We used to print board from server by calling GameContants and read the board into the taResultRPG
				// but since we are trying graphics in applet using temp board, we are temporally not using the board from server
				// which means that web and applet users cannot play on the same board!
				//====================================================================================================
//		    	URL url = new URL(getCodeBase(), "http://127.0.0.1:8080/rpg/GameContents");
//				HttpMessage msg = new HttpMessage(url);
//			    Properties props = new Properties();
//			    //pros.put("message", "ID : " + tfName.getText() + "password : " + tfPwd.getText());
//				props.put("ID", "qwe");
//				props.put("password", "qwe");
//				props.put("command", "z");
//				props.put("refresh", "refresh");
//				props.put("fromApplet", "true");
//				props.put("joinRoomName", "123");
//				InputStream inRPG = msg.sendPostMessage(props);
//				
			
//				DataInputStream dataRPG = new DataInputStream(new BufferedInputStream(inRPG));
//				String outputToTextAreaRPG = "";
//				String nextMessageRPG = "";
//				while ((nextMessageRPG = dataRPG.readLine()) != null) {
//					System.out.print("Next message from run : [" + nextMessageRPG + "]");
//					outputToTextAreaRPG += nextMessageRPG + '\n';
//				}
//				taResultBoard.setText(outputToTextAreaRPG);
				//====================================================================================================
		    	
				//====================================================================================================
		    	// We can still chat with web users 
		    	// But the problem is that applet cannot use session, it will automatically invalidate session
		    	// so we used application in order to chat with web users
				//====================================================================================================
		    	URL url = new URL(null, "http://127.0.0.1:8080/rpg/ChatContents");
		    	HttpMessage msg = new HttpMessage(url);
			    Properties props = new Properties();
			    //pros.put("message", "ID : " + tfName.getText() + "password : " + tfPwd.getText());
				props.put("ID", "qwe");
				props.put("password", "qwe");
				props.put("command", "z");
				props.put("refresh", "refresh");
				props.put("fromApplet", "true");
				props.put("joinRoomName", "123");
				InputStream inMessage = msg.sendPostMessage(props);
		    	
				DataInputStream dataMessage = new DataInputStream(new BufferedInputStream(inMessage));
				String outputToTextAreaMessage = "";
				String nextMessage2 = "";
				while ((nextMessage2 = dataMessage.readLine()) != null) {
//					System.out.print("\nNext message from run : [" + nextMessage2 + "]");
					outputToTextAreaMessage += nextMessage2 + '\n';
				}
				taResultMessage.setText(outputToTextAreaMessage);

				taResultMessage.setCaretPosition(taResultMessage.getText().length());
		    }
		    //========================== error 처리 부분 
			catch (SocketException err) {
				//Can't connect to host, report it and wait before trying again
				System.out.println("Can't connect to host: " + err.getMessage());
				try { Thread.sleep(5000); } catch (InterruptedException ignored) { }
			}
		    catch (FileNotFoundException err) {
		    	//Servlet doesn't exist, report it and wait before trying again
		    	System.out.println("Resource not found: " + err.getMessage());
				try { Thread.sleep(5000); } catch (InterruptedException ignored) { }
				}
		    catch (Exception err) {
		    	//Some other problem, report it and wait before trying again
				System.out.println("General1 exception: " +
				err.getClass().getName() + ": " + err.getMessage());
				try { Thread.sleep(10); } catch (InterruptedException ignored) { }
				}
		        System.out.println("\nrun(): sleeping");
		        t.sleep( 10 );  // interval given in milliseconds
			}
		}
		catch (InterruptedException e) { }
		System.out.println("run(): end");
	}
		
	//==================================================================================================== class MessageListener
	// The message will be sent if you press enter in the tfMessage
	// For convenient, don't need to press "Send" button in order to send message to the server 
	//====================================================================================================
	class MessageListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				url = new URL(null, "http://127.0.0.1:8080/rpg/ChatContents");
				msg = new HttpMessage(url);      
				Properties props = new Properties();
				props.put("ID", "qwe");
				props.put("message", tfMessage.getText());
				props.put("fromApplet", "true");
				InputStream in = msg.sendPostMessage(props);
				System.out.println("post");
				//===============위 세가는 web socket을 연다 
				DataInputStream data = new DataInputStream(new BufferedInputStream(in));
				String outputToTextArea = "";
				String nextMessage = "";
				while ((nextMessage = data.readLine()) != null) {		        
//					System.out.print("Next message from post button : [" + nextMessage + "]");
					outputToTextArea += nextMessage + "\n";
				}
				taResultMessage.setText(outputToTextArea);
				if (tfMessage.getText() != "") {
					messageSent = true;
					httpChatApplet.setFocusable(true);
					httpChatApplet.requestFocus();
					HttpChatApplet.textMessage = tfMessage.getText();
				}
				tfMessage.setText("");
			}
			//========================== error 처리 부분 
			catch (SocketException err) {
			//Can't connect to host, report it and wait before trying again
				System.out.println("Can't connect to host: " + err.getMessage());
				try { Thread.sleep(5000); } catch (InterruptedException ignored) { }
			}
			catch (FileNotFoundException err) {
			//Servlet doesn't exist, report it and wait before trying again
				System.out.println("Resource not found: " + err.getMessage());
				try { Thread.sleep(5000); } catch (InterruptedException ignored) { }
			}
			catch (Exception err) {
			//Some other problem, report it and wait before trying again
				System.out.println("General3 exception: " +
				e.getClass().getName() + ": " + err.getMessage());
				try { Thread.sleep(1000); } catch (InterruptedException ignored) { }
			}
		}
	}
	
	//==================================================================================================== class GetButtonListener
	// Calls ChatServlet using get method whenever the "Get" button is pressed
	//====================================================================================================
	class GetButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String nextMessage = null;	   
			while (nextMessage == null) {
				try {
					URL url = new URL(null, "http://127.0.0.1:8080/rpg/ChatServlet");

					HttpMessage msg = new HttpMessage(url);
					InputStream in = msg.sendGetMessage();
					//===============위 세가는 web socket을 연다 
					DataInputStream data = new DataInputStream(new BufferedInputStream(in));
				    nextMessage = data.readLine();
//				    System.out.print("Next message from get button : [" + nextMessage + "]");
				    taResultBoard.setText(nextMessage);
				}
			    //========================== error 처리 부분 
				catch (SocketException err) {
					//Can't connect to host, report it and wait before trying again
					System.out.println("Can't connect to host: " + err.getMessage());
					try { Thread.sleep(5000); } catch (InterruptedException ignored) { }
				}
			    catch (FileNotFoundException err) {
			    	//Servlet doesn't exist, report it and wait before trying again
			    	System.out.println("Resource not found: " + err.getMessage());
			    	try { Thread.sleep(5000); } catch (InterruptedException ignored) { }
			    }
			    catch (Exception err) {
			    	//Some other problem, report it and wait before trying again
			    	System.out.println("General2 exception: " + err.getClass().getName() + ": " + err.getMessage());
			    	try { Thread.sleep(1000); } catch (InterruptedException ignored) { }
			    }
			}
		}
	}

	//==================================================================================================== class PostButtonListener
	// Calls ChatServlet using get method whenever the "Post" button is pressed
	//====================================================================================================
	class PostButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				url = new URL(null, "http://127.0.0.1:8080/rpg/ChatServlet");
//				url = new URL("", "http://127.0.0.1:8080/rpg/ChatServlet", s);

				msg = new HttpMessage(url);      
				Properties props = new Properties();
				props.put("ID", "qwe");
				props.put("password", "qwe");
				
				props.put("chatRoom", "CreateChatRoom");
				props.put("chatRoomName", "qwe");
				
//				props.put("chatRoom", "JoinChatRoom");
//				props.put("joinRoomName", "123");
				
				props.put("fromApplet", "true");
				InputStream in = msg.sendPostMessage(props);
//				System.out.println("post");
				//===============위 세가는 web socket을 연다 
				DataInputStream data = new DataInputStream(new BufferedInputStream(in));
				String outputToTextArea = "";
				String nextMessage = "";
				while ((nextMessage = data.readLine()) != null) {		        
//					System.out.print("Next message from post button : [" + nextMessage + "]");
					outputToTextArea += nextMessage + "\n";
				}
				taResultBoard.setText(outputToTextArea);
			    connected = true;
			    start();
			}
			//========================== error 처리 부분 
			catch (SocketException err) {
			//Can't connect to host, report it and wait before trying again
				System.out.println("Can't connect to host: " + err.getMessage());
				try { Thread.sleep(5000); } catch (InterruptedException ignored) { }
			}
			catch (FileNotFoundException err) {
			//Servlet doesn't exist, report it and wait before trying again
				System.out.println("Resource not found: " + err.getMessage());
				try { Thread.sleep(5000); } catch (InterruptedException ignored) { }
			}
			catch (Exception err) {
			//Some other problem, report it and wait before trying again
				System.out.println("General3 exception: " +
				e.getClass().getName() + ": " + err.getMessage());
				try { Thread.sleep(1000); } catch (InterruptedException ignored) { }
			}
		}
	}
	
	//==================================================================================================== class SendButtonListener
	// This class is added to the sending button so whenever press "Send" button, 
	// This class will send the message input from tfMessage to the ChatContents
	//====================================================================================================
	class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				url = new URL(null, "http://127.0.0.1:8080/rpg/ChatContents");
				msg = new HttpMessage(url);      
				Properties props = new Properties();
				props.put("ID", "qwe");
				props.put("message", tfMessage.getText());
				props.put("fromApplet", "true");
				InputStream in = msg.sendPostMessage(props);
//				System.out.println("post");
				//===============위 세가는 web socket을 연다 
				DataInputStream data = new DataInputStream(new BufferedInputStream(in));
				String outputToTextArea = "";
				String nextMessage = "";
				while ((nextMessage = data.readLine()) != null) {		        
//					System.out.print("Next message from post button : [" + nextMessage + "]");
					outputToTextArea += nextMessage + "\n";
				}
				taResultMessage.setText(outputToTextArea);
				if (tfMessage.getText() != "") {
					messageSent = true;
					HttpChatApplet.textMessage = tfMessage.getText();
				}
			}
			//========================== error 처리 부분 
			catch (SocketException err) {
			//Can't connect to host, report it and wait before trying again
				System.out.println("Can't connect to host: " + err.getMessage());
				try { Thread.sleep(5000); } catch (InterruptedException ignored) { }
			}
			catch (FileNotFoundException err) {
			//Servlet doesn't exist, report it and wait before trying again
				System.out.println("Resource not found: " + err.getMessage());
				try { Thread.sleep(5000); } catch (InterruptedException ignored) { }
			}
			catch (Exception err) {
			//Some other problem, report it and wait before trying again
				System.out.println("General3 exception: " +
				e.getClass().getName() + ": " + err.getMessage());
				try { Thread.sleep(1000); } catch (InterruptedException ignored) { }
			}
		}
	}
	
	//==================================================================================================== Mouse listener methods
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//================================================================================
		// Mouse가 press 됐, flag가 켜져, handleDrag method를 call해,
		// drag가 될 때마다, 마우스를 pass해줘, drag하는 동안 마우스의 위치에, 
		// 같이 pass한 inventory panel을 set location해준다  
		//================================================================================
		if (isInventoryPanelPressed) {
			handleDrag(e, inventoryPanel, clickedOffsetXForInventoryPanel, clickedOffsetYForInventoryPanel);
		}
		if (isEquipmentPanelPressed) {
			handleDrag(e, equipmentPanel, clickedOffsetXForEquipmentPanel, clickedOffsetYForEquipmentPanel);
		}
	}

	// Move panel while dragging mouse
    public void handleDrag(MouseEvent me, JPanel panel, int clickedOffsetX, int clickedOffsetY) {
    	//================================================================================
		// Press 된 순간 기록해 놓은 coordinate을, drag할 때의 마우스 coordinate에서 빼주면
    	// 원래 panel 시작점에서, 내가 움직인 마큼, 움직이게 된다, 그러면 더 자연스러운 움직임을 더해준다  
		//================================================================================
        
      	me.translatePoint(me.getComponent().getLocation().x, me.getComponent().getLocation().y);
//    	panel.setLocation(me.getComponent().getLocation().x, me.getY() - clickedOffsetY);

//    	System.out.println("panel.getX()  : " + panel.getX() );
//      	System.out.println("panel.getY()  : " + panel.getY());
//        System.out.println("panel.getWidth()  : " + panel.getWidth() );
//      	System.out.println("panel.getHeight()  : " + panel.getHeight());
//        System.out.println("me.getComponent().getLocation().x  : " + me.getComponent().getLocation().x );
//      	System.out.println("me.getComponent().getLocation().y  : " + me.getComponent().getLocation().y);
//      	System.out.println("me.getComponent().getWidth() : " + me.getComponent().getWidth());
//      	System.out.println("me.getComponent().getHeight() : " + me.getComponent().getHeight());
        
    	panel.setLocation(me.getX() - clickedOffsetX, me.getY() - clickedOffsetY);

    	int panelX = panel.getX();
    	int panelY = panel.getY();
    	int panelWidth = panel.getWidth();
    	int panelHeight = panel.getHeight();
    	
    	int gameWindowStartX = me.getComponent().getLocation().x;
    	int gameWindowStartY = me.getComponent().getLocation().y;
    	
    	boolean outsideX = false;
    	boolean outsideY = false;
    	
    	if (panelX < gameWindowStartX || panelX >= gameWindowStartX + gameWindowWidth - panelWidth) {
    		outsideX = true;
    	}
    	if (panelY < gameWindowStartY || panelY >= gameWindowStartY + gameWindowHeight - panelHeight) {
    		outsideY = true;
    	}
    	
    	//========================================================================================
    	// Handle non-corner boundaries
    	//========================================================================================
    	if (panelX < gameWindowStartX && !outsideY) {
    		panel.setLocation(gameWindowStartX, me.getY() - clickedOffsetY);
    	}
    	else if (panelX >= gameWindowStartX + gameWindowWidth - panelWidth && !outsideY) {
    		panel.setLocation(gameWindowStartX + gameWindowWidth - panelWidth, me.getY() - clickedOffsetY);
    	}
    	else if (panelY < gameWindowStartY && !outsideX) {
    		panel.setLocation(me.getX() - clickedOffsetX, gameWindowStartY);
    	}
    	else if (panelY >= gameWindowStartY + gameWindowHeight - panelHeight && !outsideX) {
    		panel.setLocation(me.getX() - clickedOffsetX, gameWindowStartY + gameWindowHeight - panelHeight);
    	}
    	
    	//========================================================================================
    	// Handle four corners
    	//========================================================================================
    	if (panelX < gameWindowStartX && panelY < gameWindowStartY) {		 // left up
    		panel.setLocation(gameWindowStartX, gameWindowStartY);
    	}
    	else if (panelX >= gameWindowStartX + gameWindowWidth - panelWidth && panelY < gameWindowStartY) {	 // right up
    		panel.setLocation(gameWindowStartX + gameWindowWidth - panelWidth, gameWindowStartY);
    	}
    	else if (panelX >= gameWindowStartX + gameWindowWidth - panelWidth && panelY >= gameWindowStartY + gameWindowHeight - panelHeight) {	 // right down
    		panel.setLocation(gameWindowStartX + gameWindowWidth - panelWidth, gameWindowStartY + gameWindowHeight - panelHeight);
    	}
    	else if (panelX < gameWindowStartX && panelY >= gameWindowStartY + gameWindowHeight - panelHeight) {	 // left down
    		panel.setLocation(gameWindowStartX, gameWindowStartY + gameWindowHeight - panelHeight);
    	}
    }
    
	@Override
	public void mouseMoved(MouseEvent e) {
        e.translatePoint(e.getComponent().getLocation().x, e.getComponent().getLocation().y);

		// TODO Auto-generated method stub
		mx = e.getX();
		my = e.getY();
//		System.out.println( "Mouse at (" + mx + "," + my + ")" );
		
		if (inventoryItemClicked && inventoryPanel.releasedItem != null) {
			// item을 클릭했을 때 마우스에 붙어 같이 움직이게 한다 
			inventoryPanel.imageX = e.getX() - inventoryPanel.getX();
			inventoryPanel.imageY = e.getY() - inventoryPanel.getY();
			inventoryPanel.moveImage(curImageMoving, -1);
		}
		if (equipmentItemClicked && equipmentPanel.releasedItem != null) {
			// item을 클릭했을 때 마우스에 붙어 같이 움직이게 한다 
			equipmentPanel.imageX = e.getX() - equipmentPanel.getX();
			equipmentPanel.imageY = e.getY() - equipmentPanel.getY();
			equipmentPanel.moveImage(curImageMoving, -1);
		}
		
		if (slotItemClicked && portraitPanel.releasedItem != null) {
			portraitPanel.imageX = e.getX() - portraitPanel.getX();
			portraitPanel.imageY = e.getY() - portraitPanel.getY();
			portraitPanel.moveImage(curImageMoving, -1);
		}
		e.consume();
	}

	public Item curMovingItem;
	
	@Override
	public void mouseClicked(MouseEvent e) {
//		System.out.println("Clicked!");
//
//		System.out.println("inventoryPanel.rectangleImagesArray[i].x=" + inventoryPanel.rectangleImagesArray[i].x);
//		System.out.println("inventoryPanel.rectangleImagesArray[i].y=" + inventoryPanel.rectangleImagesArray[i].y);

		// TODO Auto-generated method stub
        e.translatePoint(e.getComponent().getLocation().x, e.getComponent().getLocation().y);

        if (invenBarOn) {
	        // 클릭한 순간, inventory panel에 있는 item icon images로 만들어진 똑같은 사이즈의 rectangle들이 이루어진 rectangelImagesArray를 access해 
			for (int i = 0; i < inventoryPanel.inventoryRectangleImagesArray.length; i++) {
		        // 똑같은 사이즈를 가진 item icon image들로 이루어진 slotIconImageArray를 check해, 그자리에 item이 있다면 
				if (inventoryPanel.inventoryIconImageArray[i] != null) {
					// Inventory panel이 켜져있는지 확인후 , mouse click된 지점을 점검한
					if (invenBarOn && inventoryPanel.inventoryRectangleImagesArray[i].contains(e.getPoint())) {
						// 만약에 하나의 item이 있으면서, user가 그 자리를 click했으면 
						if (portraitPanel.releasedItem == null) {
							inventoryItemClicked = true;		// toggle을 해줘, 또 클릭하면 cancel이 된다 
							// curImageMoving이라 member variable에 클릭한 index의 자리에 있는 image를 받아온다  
							curImageMoving = inventoryPanel.inventoryIconImageArray[i]; 
	//						curMovingItem = inventoryPanel.releasedItem;
							// click한 위치를 set해줘 inventory에서 access할 수 있게 한다 
							inventoryPanel.imageX = e.getX() - inventoryPanel.getX();
							inventoryPanel.imageY = e.getY() - inventoryPanel.getY();
							// inventory panel의 moveImage method를 call해 움직이고 있는 이미지를 준다, 그러면 invenPanel에서 flag가 켜져 그려준다 
							inventoryPanel.moveImage(curImageMoving, i);
						}
					}	
				}
				else if (inventoryPanel.inventoryIconImageArray[i] == null) {
					if (inventoryPanel.inventoryRectangleImagesArray[i].contains(e.getPoint())) {
						inventoryItemClicked = false;  
						if (!inventoryItemClicked && inventoryPanel.releasedItem != null) {
							inventoryPanel.releaseImage(curImageMoving, i);
						}
						else if (portraitPanel.releasedItem != null && inventoryPanel.releasedItem == null) {
							hero.inventory.setElementAt(portraitPanel.releasedItem, i);
							portraitPanel.releasedItem = null;
							portraitPanel.movingImage = false;
							curImageMoving = null;
						}
					}
				}
			}
        }

		for (int i = 0; i < portraitPanel.slotRectangleImagesArray.length; i++) { 
			if (portraitPanel.slotIconImageArray[i] != null) {
				if (portraitPanel.slotRectangleImagesArray[i].contains(e.getPoint())) { 
					if (inventoryPanel.releasedItem == null) {
						slotItemClicked = true;  
						curImageMoving = portraitPanel.slotIconImageArray[i];
						curMovingItem = portraitPanel.releasedItem;
						portraitPanel.imageX = e.getX() - portraitPanel.getX();
						portraitPanel.imageY = e.getY() - portraitPanel.getY(); 
						portraitPanel.moveImage(curImageMoving, i);
					}
				}	
			}
			else if (portraitPanel.slotIconImageArray[i] == null) {
				if (portraitPanel.slotRectangleImagesArray[i].contains(e.getPoint())) {
					slotItemClicked = false;  
					if (!slotItemClicked && portraitPanel.releasedItem != null) {						
						portraitPanel.releaseImage(portraitPanel.slotIconImageArray[i], i);	
					}
					else if (inventoryPanel.releasedItem != null && portraitPanel.releasedItem == null) {
						hero.slots.setElementAt(inventoryPanel.releasedItem, i);
						inventoryPanel.releasedItem = null;
						inventoryPanel.movingImage = false;
						curImageMoving = null;
					}
				}
			}
		}
		if (equipBarOn) {
	        // 클릭한 순간, inventory panel에 있는 item icon images로 만들어진 똑같은 사이즈의 rectangle들이 이루어진 rectangelImagesArray를 access해 
			for (int i = 0; i < equipmentPanel.equipmentRectangleImagesArray.length; i++) {
		        // 똑같은 사이즈를 가진 item icon image들로 이루어진 slotIconImageArray를 check해, 그자리에 item이 있다면 
				if (equipmentPanel.equipmentIconImageArray[i] != null) {
					// Inventory panel이 켜져있는지 확인후 , mouse click된 지점을 점검한
					if (equipBarOn && equipmentPanel.equipmentRectangleImagesArray[i].contains(e.getPoint())) {
						// 만약에 하나의 item이 있으면서, user가 그 자리를 click했으면 
						if (portraitPanel.releasedItem == null) {
							equipmentItemClicked = true;		// toggle을 해줘, 또 클릭하면 cancel이 된다 
							// curImageMoving이라 member variable에 클릭한 index의 자리에 있는 image를 받아온다  
							curImageMoving = equipmentPanel.equipmentIconImageArray[i]; 
	//						curMovingItem = inventoryPanel.releasedItem;
							// click한 위치를 set해줘 inventory에서 access할 수 있게 한다 
							equipmentPanel.imageX = e.getX() - equipmentPanel.getX();
							equipmentPanel.imageY = e.getY() - equipmentPanel.getY();
							// inventory panel의 moveImage method를 call해 움직이고 있는 이미지를 준다, 그러면 invenPanel에서 flag가 켜져 그려준다 
							equipmentPanel.moveImage(curImageMoving, i);
						}
					}	
				}
				else if (equipmentPanel.equipmentIconImageArray[i] == null) {
					if (equipmentPanel.equipmentRectangleImagesArray[i].contains(e.getPoint())) {
						equipmentItemClicked = false;  
						if (!equipmentItemClicked && equipmentPanel.releasedItem != null) {
							equipmentPanel.releaseImage(curImageMoving, i);
						}
						else if (portraitPanel.releasedItem != null && equipmentPanel.releasedItem == null) {
							hero.equipment.setElementAt(portraitPanel.releasedItem, i);
							portraitPanel.releasedItem = null;
							portraitPanel.movingImage = false;
							curImageMoving = null;
						}
					}
				}
			}
        }

		for (int i = 0; i < portraitPanel.slotRectangleImagesArray.length; i++) { 
			if (portraitPanel.slotIconImageArray[i] != null) {
				if (portraitPanel.slotRectangleImagesArray[i].contains(e.getPoint())) { 
					if (equipmentPanel.releasedItem == null) {
						slotItemClicked = true;  
						curImageMoving = portraitPanel.slotIconImageArray[i];
						curMovingItem = portraitPanel.releasedItem;
						portraitPanel.imageX = e.getX() - portraitPanel.getX();
						portraitPanel.imageY = e.getY() - portraitPanel.getY(); 
						portraitPanel.moveImage(curImageMoving, i);
					}
				}	
			}
			else if (portraitPanel.slotIconImageArray[i] == null) {
				if (portraitPanel.slotRectangleImagesArray[i].contains(e.getPoint())) {
					slotItemClicked = false;  
					if (!slotItemClicked && portraitPanel.releasedItem != null) {						
						portraitPanel.releaseImage(portraitPanel.slotIconImageArray[i], i);	
					}
					else if (equipmentPanel.releasedItem != null && portraitPanel.releasedItem == null) {
						hero.slots.setElementAt(equipmentPanel.releasedItem, i);
						equipmentPanel.releasedItem = null;
						equipmentPanel.movingImage = false;
						curImageMoving = null;
					}
				}
			}
		}
	}

	
	boolean isInventoryItemIconPressed = false;
	boolean isEquipmentItemIconPressed = false;
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		isButtonPressed = true;
		
//		System.out.println( "Mouse at (" + mx + "," + my + ")" );
		
//        System.out.println("e.getX() : " + e.getX());
//        System.out.println("e.getY() : " + e.getY());
		p.setLocation(e.getPoint().x + BOARD_X, e.getPoint().y + BOARD_Y);
		
//		System.out.println("p.getX() : " + p.getX());
//		System.out.println("p.getY() : " + p.getY());
	
		//================================================================================
		// 움직였을 떼 inventory panel을 get bound을 이용하 Rectangle을 만들, inventory panel이 움직일 때 같은 사이즈의 rectangle이 같이 움직여 
		// 마우스로 panel를 click했을 때 rectangle을 이용해서 point가 panel에 있는 지 확인한 다음, flag을 켜줘 
		// Drag했을 때 handleDrag method을 써서 inventory panel에 그려준다 
		//================================================================================
		if (invenBarOn && inventoryPanel.getBounds().contains(p)) { 
//			System.out.println("inventory!");
			
			// 만약에 inventoryPanel을 클릭했지만 그안에 있는 아이템의 아이콘을 클릭했을 경우 drag를 할 수 없게 한다
			// 이를 위해서는 panel에서 아이템이 있을 때만 rectangle을 만들지 않고, null check하기 전에 만들야 한다 
			for (int i = 0; i < inventoryPanel.inventoryRectangleImagesArray.length; i++) {
				if (inventoryPanel.inventoryRectangleImagesArray[i] != null) {
					if (inventoryPanel.inventoryRectangleImagesArray[i].contains(p)) {
						isInventoryItemIconPressed = true;
					}
				}
			}
			if (isInventoryItemIconPressed) {
				isInventoryPanelPressed = false;
			}
			else {
				isInventoryPanelPressed = true;
			}
			
			//-------------------------------------------------------------------------------
			// 클릭한 위치를 시작점으로 다시 그리는 대신, offset coordinate을 계산해 기록해둔 다음에, 
			// drag됐을 때마다, 움직인 거리만큼 inventory panel의 시작점도 같이 똑같은 거리를 움직이게 되면, 클릭한 지점부터 움직일 수 있게 된다  
			//-------------------------------------------------------------------------------
			clickedOffsetXForInventoryPanel = (int) p.getX() - inventoryPanel.getLocation().x;
			clickedOffsetYForInventoryPanel = (int) p.getY() - inventoryPanel.getLocation().y;
		}
		else {
			isInventoryPanelPressed = false;
		}
		
		if (equipBarOn && equipmentPanel.getBounds().contains(p)) { 
//			System.out.println("inventory!");
			
			// 만약에 inventoryPanel을 클릭했지만 그안에 있는 아이템의 아이콘을 클릭했을 경우 drag를 할 수 없게 한다
			// 이를 위해서는 panel에서 아이템이 있을 때만 rectangle을 만들지 않고, null check하기 전에 만들야 한다 
			for (int i = 0; i < equipmentPanel.equipmentRectangleImagesArray.length; i++) {
				if (equipmentPanel.equipmentRectangleImagesArray[i] != null) {
					if (equipmentPanel.equipmentRectangleImagesArray[i].contains(p)) {
						isEquipmentItemIconPressed = true;
					}
				}
			}
			if (isEquipmentItemIconPressed) {
				isEquipmentPanelPressed = false;
			}
			else {
				isEquipmentPanelPressed = true;
			}
			
			//-------------------------------------------------------------------------------
			// 클릭한 위치를 시작점으로 다시 그리는 대신, offset coordinate을 계산해 기록해둔 다음에, 
			// drag됐을 때마다, 움직인 거리만큼 inventory panel의 시작점도 같이 똑같은 거리를 움직이게 되면, 클릭한 지점부터 움직일 수 있게 된다  
			//-------------------------------------------------------------------------------
			
			clickedOffsetXForEquipmentPanel = (int) p.getX() - equipmentPanel.getLocation().x;
			clickedOffsetYForEquipmentPanel = (int) p.getY() - equipmentPanel.getLocation().y;
		}
		else {
			isEquipmentPanelPressed = false;
		}

		//================================================================================
		// "Consume" the event so it won't be processed in the
		// default manner by the source which generated it.
		e.consume();
        //e.translatePoint(e.getComponent().getLocation().x, e.getComponent().getLocation().y);
        //inventoryPanel.setBounds(e.getPoint().x + BOARD_X, e.getPoint().y + BOARD_Y, 100, 100);
		//System.out.println("" + e.getPoint().x + ", " + e.getPoint().y);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		isButtonPressed = false;
		isInventoryPanelPressed = false;
		isInventoryItemIconPressed = false;
		isEquipmentPanelPressed = false;
		isEquipmentItemIconPressed = false;
//		setBackground( Color.black );
		//repaint();
		e.consume();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	//==================================================================================================== 
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		// TODO Auto-generated method stub
		
		return false;
	}

//	public void keyReleased(KeyEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
}
