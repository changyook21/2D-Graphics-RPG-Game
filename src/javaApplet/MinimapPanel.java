package javaApplet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
public class MinimapPanel extends JPanel implements ActionListener{	
	private Board board = null;
	private Hero hero = new Hero("hero1");

	public MinimapPanel() {
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
	public void paintComponent(Graphics g ) {
		
        super.paintComponent(g);

	
	    int width = getWidth();
	    int height = getHeight();
	    
	    // Border
	    //g.setColor(Color.white);
	    //g.fillRect(0, 0, width, height);
	    if (board != null) {
	    	board.paintMinimap(g, 0, 0, hero, this, 30, 30);
	    }
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
