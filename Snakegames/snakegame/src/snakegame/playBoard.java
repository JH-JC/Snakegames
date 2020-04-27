package snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class playBoard extends JPanel implements ActionListener {
	 private final int BORDER_W = 300;		//Declaring default border size  in both height and width
	    private final int BORDER_H = 300;
	    private final int SIZE_OFDOT = 10;		
	    private final int ALL_DOTS = 900; //All possible dots that can be present in the window (900=(300*300)/(10*10))
	    private final int RANDOM_POSITION = 29; //This will be used to calculate where the random location the objective will be on the window
	    private final int DELAY = 140;	//Sets delay for game
	    private final int x[] = new int [ALL_DOTS];
	    private final int y[] = new int [ALL_DOTS];	//These 2 arrays contain the x and y coordinates for all dots present on the snake 
	    private int numDots;
	    private int apple_x;	//Will be used to set x and y cords of the objective
	    private int apple_y;

	    private boolean directionLeft = false;		//These will be used for the keys that will control the snake 
	    private boolean directionRight = true;
	    private boolean directionUp = false;
	    private boolean directionDown = false;
	    private boolean insideGame = true;

	    private Timer timer; //Sets a timer for the game
	    
	    private Image dot;			//These are image variables that will be used to create the graphical representation of the snake and objective
	    private Image objective;
	    private Image snakeHead;

	    public playBoard() {	//This is set to public so the snake class and call it and access it 
	        
	        initBoard();

}
	    private void initBoard() {

	        addKeyListener(new TAdapter());
	        setBackground(Color.black);
	        setFocusable(true);

	        setPreferredSize(new Dimension(BORDER_W, BORDER_H));
	        loadImages();
	        initGame();
	    }

	    private void loadImages() {		//This method will load the images when the game starts 
	    	
	        ImageIcon dots = new ImageIcon("src/resources/dot.png");		//These access the resources files for the snake and set it to the original image variable 
	        dot = dots.getImage();

	        ImageIcon apple = new ImageIcon("src/resources/apple.png");	//In the quotations, they show where to access the specific files  
	        objective = apple.getImage();

	        ImageIcon head = new ImageIcon("src/resources/head.png");
	        snakeHead = head.getImage();
	    }

	    private void initGame() {

	        numDots = 3;

	        for (int m = 0; m < numDots; m++) {
	            x[m] = 50 - m * 10;
	            y[m] = 50;
	        }
	        
	        locateApple();

	        timer = new Timer(DELAY, this);	//Set a new timer delay
	        timer.start();
	    }

	    @Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);

	        doDrawing(g);
	    }
	    
	    private void doDrawing(Graphics g) {		//Make the actual images appear during the game 
	        
	        if (insideGame) {

	            g.drawImage(objective, apple_x, apple_y, this);

	            for (int z = 0; z < numDots; z++) {
	                if (z == 0) {
	                    g.drawImage(snakeHead, x[z], y[z], this);
	                } else {
	                    g.drawImage(dot, x[z], y[z], this);
	                }
	            }

	            Toolkit.getDefaultToolkit().sync();

	        } else {

	            gameOver(g);	//Set this variable as the game over screen
	        }        
	    }

	    private void gameOver(Graphics g) {		//This code will set the very simple graphics for mostly the game over screen, in fact, this entire damn thing is the game over screen 
	        
	        String msg = "Game Over";
	        Font small = new Font("Helvetica", Font.BOLD, 14);	
	        FontMetrics metr = getFontMetrics(small);

	        g.setColor(Color.white);
	        g.setFont(small);
	        g.drawString(msg, (BORDER_W - metr.stringWidth(msg)) / 2, BORDER_H / 2);
	    }

	    private void checkApple() {

	        if ((x[0] == apple_x) && (y[0] == apple_y)) {	//When the snake hits the objective, the number of dots will grow by one every single time they hit this objective

	            numDots++;
	            locateApple();
	        }
	    }

	    private void move() {	//This method will provide the code to show the player that the dots are moving fluently throughout the game

	        for (int m = numDots; m > 0; m--) {		//All this code will move the dots up the chain depending on what specified direction the player goes for
	            x[m] = x[(m - 1)];					
	            y[m] = y[(m - 1)];
	        }

	        if (directionLeft) {
	            x[0] -= SIZE_OFDOT;
	        }

	        if (directionRight) {
	            x[0] += SIZE_OFDOT;
	        }

	        if (directionUp) {
	            y[0] -= SIZE_OFDOT;
	        }

	        if (directionDown) {
	            y[0] += SIZE_OFDOT;
	        }
	    }

	    private void checkCollision() {	//Make sure this method is called

	        for (int m = numDots; m > 0; m--) {

	            if ((m > 4) && (x[0] == x[m]) && (y[0] == y[m])) {		//If the snake hits itself, game over 
	                insideGame = false;	//When insideGame is false, this will always mean the game is over
	            }
	        }

	        if (y[0] >= BORDER_H) {		//The following code will check if the snake has hit the border windows, if so, game is over:
	        	insideGame = false;		//This one will check if the snake hit bottom window
	        }

	        if (y[0] < 0) {
	            insideGame = false;
	        }

	        if (x[0] >= BORDER_W) {		//This will check if it hit either the left or right side of the window:
	            insideGame = false;
	        }

	        if (x[0] < 0) {				
	            insideGame = false;
	        }
	        
	        if (!insideGame) {
	            timer.stop();
	        }
	    }

	    private void locateApple() {		//This method is being called by checkApple so it does not need to be called in the actions performed section

	        int r = (int) (Math.random() * RANDOM_POSITION);	//This will move the apple into random positions after being "eaten"
	        apple_x = ((r * SIZE_OFDOT));						//In this case its the x coordinates

	        r = (int) (Math.random() * RANDOM_POSITION);		//And this one is for y cords
	        apple_y = ((r * SIZE_OFDOT));
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {	//This method will take in the ActionEvent functionality and all the key methods needed for the game to function

	        if (insideGame) {	//If the game is running...

	            checkApple();		//All 3 of these methods will continue to be called/checked, when the snake head hits the apple, that is an action that must be checked, when the snake moves and goes to a different direction, this is an action that must be checked, when the snake "eats" the apple, it must obtain a new dot, this is an action that must be checked
	            checkCollision();
	            move();
	        }

	        repaint();	//Refreshes screen
	    }

	    private class TAdapter extends KeyAdapter {		// TAdapter class that extends KeyAdapter method for key settings 

	        @Override								//Set to @Override since each key will override one another when pressed 
	        public void keyPressed(KeyEvent e) {

	            int key = e.getKeyCode();	//Initialize a new key variable

	            if ((key == KeyEvent.VK_LEFT) && (!directionRight)) {	//All this remaining code will take in the key commands set, such as VK_LEFT which would the the left arrow on your keyboard, same with VK_RIGHT, etc, this code is the usual directional keys for arrows 
	                directionLeft = true;
	                directionUp = false;
	                directionDown = false;
	            }

	            if ((key == KeyEvent.VK_RIGHT) && (!directionLeft)) {	//If the specified right key is pressed and not the left key,(the opposite key), all the following directions will be false besides the right arrow key
	            	directionRight = true;
	            	directionUp = false;
	            	directionDown = false;
	            }

	            if ((key == KeyEvent.VK_UP) && (!directionDown)) {
	            	directionUp = true;
	            	directionRight = false;
	            	directionLeft = false;
	            }

	            if ((key == KeyEvent.VK_DOWN) && (!directionUp)) {
	            	directionDown = true;
	                directionRight = false;
	                directionLeft = false;
	            }
	        }
	    }

}
