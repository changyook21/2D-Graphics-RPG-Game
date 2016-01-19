package javaApplet;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SpriteAnimate extends Frame implements Runnable {
	private Image offScreenImage;
	private Image backGroundImage;
	private Image[] gifImages = new Image[6];
	//offscreen graphics context
	private Graphics offScreenGraphicsCtx;
	private Thread animationThread;
	private MediaTracker mediaTracker;
	private SpriteManager spriteManager;
	//Animation display rate, 12fps
	private int animationDelay = 83;
	private Random rand = new Random(System.currentTimeMillis());
	
	public static void main(String[] args){
		new SpriteAnimate();
	}//end main
	//---------------------------------//
	SpriteAnimate() {//constructor
	    // Load and track the images
	    mediaTracker = new MediaTracker(this);
	    //Get and track the background 
	    // image
	    backGroundImage = Toolkit.getDefaultToolkit().getImage("background02.gif");
	    mediaTracker.addImage(backGroundImage, 0);
	
	    //Get and track 6 images to use 
	    // for sprites
	    gifImages[0] = Toolkit.getDefaultToolkit().getImage("redball.gif");
	    mediaTracker.addImage(gifImages[0], 0);
	    gifImages[1] = Toolkit.getDefaultToolkit().getImage("greenball.gif");
	    mediaTracker.addImage(gifImages[1], 0);
	    gifImages[2] = Toolkit.getDefaultToolkit().getImage("blueball.gif");
	    mediaTracker.addImage(gifImages[2], 0);
	    gifImages[3] = Toolkit.getDefaultToolkit().getImage("yellowball.gif");
	    mediaTracker.addImage(gifImages[3], 0);
	    gifImages[4] = Toolkit.getDefaultToolkit().getImage("purpleball.gif");
	    mediaTracker.addImage(gifImages[4], 0);
	    gifImages[5] = Toolkit.getDefaultToolkit().getImage("orangeball.gif");
	    mediaTracker.addImage(gifImages[5], 0);
	    
	    //Block and wait for all images to 
	    // be loaded
	    try {
	    	mediaTracker.waitForID(0);
	    }
	    catch (InterruptedException e) {
	    	System.out.println(e);
	    }//end catch
	    
	    //Base the Frame size on the size 
	    // of the background image.
	    //These getter methods return -1 if
	    // the size is not yet known.
	    //Insets will be used later to 
	    // limit the graphics area to the 
	    // client area of the Frame.
	    int width = backGroundImage.getWidth(this);
	    int height = backGroundImage.getHeight(this);
	
	    //While not likely, it may be 
	    // possible that the size isn't
	    // known yet.  Do the following 
	    // just in case.
	    //Wait until size is known
	    while(width == -1 || height == -1){
	    	System.out.println("Waiting for image");
	      width = backGroundImage.getWidth(this);
	      height = backGroundImage.getHeight(this);
	    }//end while loop
	    
	    //Display the frame
	    setSize(width,height);
	    setVisible(true);
	    setTitle("Copyright 2001, R.G.Baldwin");
	
	    //Create and start animation thread
	    animationThread = new Thread(this);
	    animationThread.start();
	  
	    //Anonymous inner class window 
	    // listener to terminate the 
	    // program.
	    this.addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent e) {
	    		System.exit(0);
	    	}
	    });
	}//end constructor
	//---------------------------------//

	public void run() {
		//Create and add sprites to the 
		//sprite manager
		spriteManager = new SpriteManager(new BackgroundImage(this, backGroundImage));
		//Create 15 sprites from 6 gif 
		//files.
		for (int cnt = 0; cnt < 15; cnt++){
			Point position = spriteManager.getEmptyPosition(new Dimension(gifImages[0].getWidth(this),gifImages[0].getHeight(this)));
			spriteManager.addSprite(makeSprite(position, cnt % 6));
		}//end for loop
		
		//Loop, sleep, and update sprite 
		// positions once each 83 
		// milliseconds
		long time = System.currentTimeMillis();
		while (true) {//infinite loop
			spriteManager.update();
			repaint();
			try {
				time += animationDelay;
				Thread.sleep(Math.max(0,time - 
						System.currentTimeMillis()));
			}
			catch (InterruptedException e) {
				System.out.println(e);
			}//end catch
		}//end while loop
	}//end run method
	//---------------------------------//
  
	private Sprite makeSprite(Point position, int imageIndex) {
		return new Sprite(this, gifImages[imageIndex], position, new Point(rand.nextInt() % 5,rand.nextInt() % 5));
	}//end makeSprite()
	//---------------------------------//

	//Overridden graphics update method 
	// on the Frame
	public void update(Graphics g) {
		//Create the offscreen graphics 
		// context
		if (offScreenGraphicsCtx == null) {	
			offScreenImage = createImage(getSize().width, getSize().height);
			offScreenGraphicsCtx = offScreenImage.getGraphics();
		}//end if

		// Draw the sprites offscreen
		spriteManager.drawScene(offScreenGraphicsCtx);
		// Draw the scene onto the screen
		if(offScreenImage != null){
			g.drawImage(offScreenImage, 0, 0, this);
		}//end if
	}//end overridden update method
	//---------------------------------//
	
	//Overridden paint method on the 
	// Frame
	public void paint(Graphics g) {
		//Nothing required here.  All 
		// drawing is done in the update 
		// method above.
	}//end overridden paint method
}//end class Animate01
//===================================//

class BackgroundImage{
	private Image image;
	private Component component;
	private Dimension size;
	
	public BackgroundImage(Component component, Image image) {
		this.component = component;
		size = component.getSize();
		this.image = image;
	}//end construtor
  
	public Dimension getSize(){
		return size;
	}//end getSize()

	public Image getImage(){
		return image;
	}//end getImage()

	public void setImage(Image image){
		this.image = image;
	}//end setImage()

	public void drawBackgroundImage(Graphics g) {
		g.drawImage(image, 0, 0, component);
	}//end drawBackgroundImage()	
}//end class BackgroundImage
//===========================

class SpriteManager extends Vector {
	private BackgroundImage backgroundImage;
  
	public SpriteManager(BackgroundImage backgroundImage) {
		this.backgroundImage = backgroundImage;
	}//end constructor
	//---------------------------------//
  
	public Point getEmptyPosition(Dimension spriteSize){
		Rectangle trialSpaceOccupied = new Rectangle(0, 0, spriteSize.width, spriteSize.height);
		Random rand = new Random(System.currentTimeMillis());
		boolean empty = false;
		int numTries = 0;

		// Search for an empty position
		while (!empty && numTries++ < 100){
			// Get a trial position
			trialSpaceOccupied.x = Math.abs(rand.nextInt() %backgroundImage.getSize().width);
			trialSpaceOccupied.y = Math.abs(rand.nextInt() %backgroundImage.getSize().height);
			// Iterate through existing 
			// sprites, checking if position 
			// is empty
			boolean collision = false;
			for(int cnt = 0;cnt < size();cnt++){
				Rectangle testSpaceOccupied = ((Sprite)elementAt(cnt)).getSpaceOccupied();
				if (trialSpaceOccupied.intersects(testSpaceOccupied)){
					collision = true;
				}//end if
			}//end for loop
			empty = !collision;
		}//end while loop
		return new Point(trialSpaceOccupied.x, trialSpaceOccupied.y);
  }//end getEmptyPosition()
  //---------------------------------//
  
	public void update() {
		Sprite sprite;
    
		//Iterate through sprite list
		for (int cnt = 0;cnt < size();cnt++){
			sprite = (Sprite)elementAt(cnt);
			//Update a sprite's position
			sprite.updatePosition();

			//Test for collision. Positive 
			// result indicates a collision
			int hitIndex = testForCollision(sprite);
			if (hitIndex >= 0){
				//a collision has occurred
				bounceOffSprite(cnt,hitIndex);
			}//end if
		}//end for loop
	}//end update
	//---------------------------------//
  
	private int testForCollision(Sprite testSprite) {
		//Check for collision with other 
		// sprites
		Sprite  sprite;
		for (int cnt = 0;cnt < size();cnt++){
			sprite = (Sprite)elementAt(cnt);
			if (sprite == testSprite)
				//don't check self
				continue;
				//Invoke testCollision method 
				// of Sprite class to perform
				// the actual test.
			if (testSprite.testCollision(sprite))
				//Return index of colliding 
				// sprite
				return cnt;
		}//end for loop
		return -1;//No collision detected
	}//end testForCollision()
	//---------------------------------//
  
	private void bounceOffSprite(int oneHitIndex,int otherHitIndex){
		//Swap motion vectors for 
		// bounce algorithm
		Sprite oneSprite = (Sprite)elementAt(oneHitIndex);
		Sprite otherSprite = (Sprite)elementAt(otherHitIndex);
		Point swap = oneSprite.getMotionVector();
		oneSprite.setMotionVector(otherSprite.getMotionVector());
		otherSprite.setMotionVector(swap);
	}//end bounceOffSprite()
	//---------------------------------//
  
	public void drawScene(Graphics g){
		//Draw the background and erase 
		// sprites from graphics area
		//Disable the following statement 
		// for an interesting effect.
		backgroundImage.drawBackgroundImage(g);
		//Iterate through sprites, drawing
		// each sprite
		for (int cnt = 0;cnt < size();cnt++)
		((Sprite)elementAt(cnt)).drawSpriteImage(g);
	}//end drawScene()
	//---------------------------------//
  
	public void addSprite(Sprite sprite){
		add(sprite);
	}//end addSprite()
}//end class SpriteManager
//===================================//

class Sprite {
	private Component component;
	private Image image;
	private Rectangle spaceOccupied;
	private Point motionVector;
	private Rectangle bounds;
	private Random rand; 

	public Sprite(Component component,Image image,Point position,Point motionVector){
		//Seed a random number generator 
		// for this sprite with the sprite
		// position.
		rand = new Random(position.x);
		this.component = component;
		this.image = image;
		setSpaceOccupied(new Rectangle(position.x,position.y,image.getWidth(component),image.getHeight(component)));
		this.motionVector = motionVector;
		//Compute edges of usable graphics
		// area in the Frame.
		int topBanner = ((Container)component).getInsets().top;
		int bottomBorder = ((Container)component).getInsets().bottom;
		int leftBorder = ((Container)component).getInsets().left;
		int rightBorder = ((Container)component).getInsets().right;
		bounds = new Rectangle(0 + leftBorder,0 + topBanner,component.getSize().width - (leftBorder + rightBorder),component.getSize().height -(topBanner + bottomBorder));
	}//end constructor
	//---------------------------------//

	public Rectangle getSpaceOccupied(){
		return spaceOccupied;
	}//end getSpaceOccupied()
	//---------------------------------//
  
	void setSpaceOccupied(Rectangle spaceOccupied){
		this.spaceOccupied = spaceOccupied;
	}//setSpaceOccupied()
	//---------------------------------//
  
	public void setSpaceOccupied(Point position){
		spaceOccupied.setLocation(position.x, position.y);
	}//setSpaceOccupied()
	//---------------------------------//
  
	public Point getMotionVector(){
		return motionVector;
	}//end getMotionVector()
	//---------------------------------//
  
	public void setMotionVector(Point motionVector){
		this.motionVector = motionVector;
	}//end setMotionVector()
	//---------------------------------//
  
	public void setBounds(Rectangle bounds){
		this.bounds = bounds;
	}//end setBounds()
	//---------------------------------//
  
	public void updatePosition() {
		Point position = new Point(spaceOccupied.x, spaceOccupied.y);
	    
	    //Insert random behavior.  During 
	    // each update, a sprite has about
	    // one chance in 10 of making a 
	    // random change to its 
	    // motionVector.  When a change 
	    // occurs, the motionVector
	    // coordinate values are forced to
	    // fall between -7 and 7.  This 
	    // puts a cap on the maximum speed
	    // for a sprite.
    if(rand.nextInt() % 10 == 0){
    	Point randomOffset = new Point(rand.nextInt() % 3,rand.nextInt() % 3);
    	motionVector.x += randomOffset.x;
    	if(motionVector.x >= 7) motionVector.x -= 7;
    	if(motionVector.x <= -7) motionVector.x += 7;
    	motionVector.y += randomOffset.y;
    	if(motionVector.y >= 7) motionVector.y -= 7;
    	if(motionVector.y <= -7) motionVector.y += 7;
    }//end if
    
    //Move the sprite on the screen
    position.translate(motionVector.x, motionVector.y);

    //Bounce off the walls
    boolean bounceRequired = false;
    Point tempMotionVector = new Point(motionVector.x,motionVector.y);
    
    //Handle walls in x-dimension
    if (position.x < bounds.x) {
    	bounceRequired = true;
    	position.x = bounds.x;
    	//reverse direction in x
    	tempMotionVector.x = -tempMotionVector.x;
    }
    else if ((position.x + spaceOccupied.width)> (bounds.x + bounds.width)) {
    	bounceRequired = true;
    	position.x = bounds.x + bounds.width - spaceOccupied.width;
    	//reverse direction in x
    	tempMotionVector.x = -tempMotionVector.x;
    }//end else if
    
    //Handle walls in y-dimension
    if (position.y < bounds.y){
      bounceRequired = true;
      position.y = bounds.y;
      tempMotionVector.y = -tempMotionVector.y;
    }
    else if ((position.y + spaceOccupied.height)> (bounds.y + bounds.height)){
    	bounceRequired = true;
    	position.y = bounds.y + bounds.height - spaceOccupied.height;
    	tempMotionVector.y = -tempMotionVector.y;
    }//end else if
    
    if(bounceRequired)
      //save new motionVector
    	setMotionVector(tempMotionVector);
    	//update spaceOccupied
    	setSpaceOccupied(position);
	}//end updatePosition()
	//---------------------------------//
  
	public void drawSpriteImage(Graphics g){
		g.drawImage(image,spaceOccupied.x,spaceOccupied.y,component);
	}//end drawSpriteImage()
	//---------------------------------//
  
	public boolean testCollision(Sprite testSprite){
    //Check for collision with 
    // another sprite
	    if (testSprite != this){
	      return spaceOccupied.intersects(testSprite.getSpaceOccupied());
	    }//end if
    return false;

	}//end testCollision
}//end Sprite class
//===================================//