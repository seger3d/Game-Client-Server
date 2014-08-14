package client.base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Graphic extends JFrame {
	
	private BufferedImage backBuffer;
	private int windowWidth = 400;
	private int windowHeight = 400;
	private Insets insets;
	
	public Graphic(){
		setTitle("Game Tutorial"); 
        setSize(windowWidth, windowHeight); 
        setResizable(false); 
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setVisible(true); 
        insets = getInsets(); 
        setSize(insets.left + windowWidth + insets.right, 
                        insets.top + windowHeight + insets.bottom); 
        
        backBuffer = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB); 
	}
	
	public void run(){
		draw();
	}
	
	private void draw() {
		Graphics g = getGraphics(); 
		Graphics bbg = backBuffer.getGraphics(); 
		bbg.setColor(Color.WHITE); 
		bbg.fillRect(0, 0, windowWidth, windowHeight); 
		bbg.setColor(Color.BLACK); 
        bbg.drawOval(10, 10, 20, 20); 
		g.drawImage(backBuffer, insets.left, insets.top, this); 
	}

}
