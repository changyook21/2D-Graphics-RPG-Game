package javaApplet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JApplet;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import javax.imageio.ImageIO;

public class FirstApplet extends JApplet {
	
	public void start() {
	}

	Image aang;

    
    
    final int radius = 25;

	public void paint ( Graphics gr ) { 		  
		gr.setColor( Color.white );
		gr.fillRect( 0, 0, 150, 150 );
		gr.setColor( Color.black );
		
		gr.drawOval( (150/2 - radius), (150/2 - radius), radius*2, radius*2 );
		//	    System.out.print("tombStone");
		    
	   
	}
	  
//	public void init() {
//		setBackground(Color.pink);
//	}
//
//	public void paint(Graphics g) {
//		g.drawString("Graphic", 100, 100);
//		System.out.print("tombStone");
//	}
}
