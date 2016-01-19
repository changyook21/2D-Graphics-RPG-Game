package javaApplet;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
/** Bounce circles around on the screen, using
 *  double buffering for speed and to avoid problems
 *  with overlapping circles. Overrides update to
 *  to avoid flicker problems. */

public class DoubleBuffering3 extends Applet
    implements Runnable, ActionListener {
  private Vector circles;
  private int width, height;
  private Image offScreenImage;
  private Graphics offScreenGraphics;
  private Button startButton, stopButton;
  private Thread animationThread = null;

  public void init() {
    setBackground(Color.white);
    width = getSize().width;
    height = getSize().height;
    offScreenImage = createImage(width, height);
    offScreenGraphics = offScreenImage.getGraphics();
    // Automatic in some systems, not in others
    offScreenGraphics.setColor(Color.black);
    circles = new Vector();
    startButton = new Button("Start a circle");
    startButton.addActionListener(this);
    add(startButton);
    stopButton = new Button("Stop all circles");
    stopButton.addActionListener(this);
    add(stopButton);
  }

  //----------------------------------------------------
  /** When the "start" button is pressed, start the
   *  animation thread if it is not already started.
   *  Either way, add a circle to the Vector of
   *  circles that are being bounced.
   * 
   *  When the "stop" button is pressed, stop
   *  the thread and clear the Vector of circles.
   */
  public void actionPerformed(ActionEvent event) {
    if (event.getSource() == startButton) {
      if (circles.size() == 0) {
        animationThread = new Thread(this);
        animationThread.start();
      }
      int radius = 25;
      int x = radius + randomInt(width - 2 * radius);
      int y = radius + randomInt(height - 2 * radius);
      int deltaX = 1 + randomInt(10);
      int deltaY = 1 + randomInt(10);
//      circles.addElement(new MovingCircle(x, y, radius,
//                                          deltaX,
//                                          deltaY));
      repaint();
    } else if (event.getSource() == stopButton) {
      if (animationThread != null) {
        animationThread = null;
        circles.removeAllElements();
      }
    }
  }

  //----------------------------------------------------
  /** Each time around the loop, move each circle
   *  based on its current position and deltaX/deltaY
   *  values. These values reverse when the circles
   *  reach the edge of the window.
   */
  public void run() {
//    MovingCircle circle;
    Thread myThread = Thread.currentThread();
    // Really while thread not stopped
    while(animationThread==myThread) { 
//      for(int j=0; j<circles.size(); j++) {
//        circle = (MovingCircle)circles.elementAt(j);
//        circle.move(width, height);
//      }
      repaint();
      pause(100);
    }
  }

  //----------------------------------------------------
  /** Skip the usual screen-clearing step of update
   *  so that there is no "flicker" between each
   *  drawing step.
   */
  public void update(Graphics g) {
    paint(g);
  }


  //----------------------------------------------------
  /** Clear the offscreen pixmap, draw each circle
   *  onto it, then draw that pixmap onto the
   *  applet window.
   */
  public void paint(Graphics g) {
    offScreenGraphics.clearRect(0, 0, width, height);
//    MovingCircle circle;
//    for(int i=0; i<circles.size(); i++) {
//      circle = (MovingCircle)circles.elementAt(i);
//      circle.draw(offScreenGraphics);
//    }
    g.drawImage(offScreenImage, 0, 0, this);
  }

  //----------------------------------------------------
  // Returns an int from 0 to max (inclusive),
  // yielding max + 1 possible values.

  private int randomInt(int max) {
    double x =
      Math.floor((double)(max + 1) * Math.random());
    return((int)(Math.round(x)));
  }

  //----------------------------------------------------
  // Sleep for the specified amount of time.

  private void pause(int milliseconds) {
    try {
      Thread.sleep((long)milliseconds);
    } catch(InterruptedException ie) {}
  }
}