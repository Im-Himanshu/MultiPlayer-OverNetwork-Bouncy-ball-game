

/** 
 *  * PingPong
 *
 * @version	1.0
 * @author	Tanwani Anyangwe
 * @email	tanyan1@towson.edu
 * @url		http://tiger.towson.edu/users/tanyan1
 */
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.applet.AudioClip;
import java.applet.*;
import java.awt.*;
import java.net.*;
import java.util.*;

public class PingPongSound extends Applet 
	implements Runnable, MouseListener, MouseMotionListener {

        Thread runner;
        int ballX, ballY, batYP,batYC;        // Current coords of the dot
		int batMin, batMax;
		int ballXMin, ballXMax, ballYMin, ballYMax, ballIncr;
		boolean upwards,toLeft, started, gameover;
		int topscore = 20;
		int startPlayer;
		int scoreP, scoreC;
		String whowon;
		Color teal;
		
		// Sound clips.
		AudioClip hitSound;
		AudioClip endSound;
  
        public void init(){  
			Graphics bg = getGraphics();
			
			addMouseMotionListener(this);
			addMouseListener(this);
			
			
			ballIncr = 4;				//ball speed in increments
			ballXMin = 10 + ballIncr;	//minimum ball's x-position
			ballXMax = 300 - ballIncr;	//maximum ball's x-position
			ballYMin = 10 + ballIncr;	//minimum ball's y-position
			ballYMax = 200 - ballIncr;	//maximum ball's y-position
			
			batMin = 10;				//minimum bat's x-position
			batMax = 180;				//maximum bat's y-position
			batYP = batYC = 20;
			
			startPlayer = 1;
				
			started = false;
			gameover = false;

			teal = new Color(0,160,0);
			setBackground(teal);
			
			//drawBG(bg);
			loadSounds();

			this.requestFocus();
        }
		
		protected void newGame() {				
			//init game variables
			started = true;
			gameover = false;
			//Player and computer's scores
			scoreP = 0; 
			scoreC = 0;	
			
			if (startPlayer==0) {
				ballX = 300;
				//Get Serving position for palyer
				ballY = batYP;
				
				//Ball's direction towards computer
				toLeft = true; 
				startPlayer = 1;				
			}
			else {
				ballX = 20;
				//Get Serving position for computer
				ballY = (int)(Math.random()*200)+10;
				toLeft = false; 
				startPlayer = 0;
			}			
			
			batYP = ballY;
			
			//If service from above middle of court, then serve upwards 
			if (ballY > 160){
				upwards = true;
			}
			else {
				upwards = false;
			}
			batYC = ballY;
			//Computer's bat position
			
			showStatus(new Integer(ballY).toString());			
		}
		
		protected void endGame() {	
			if (scoreC>scoreP) {
				whowon = "      Computer Wins!";
			}
			else {
				whowon = "Congratulations, You Win!";
			}
			
			//endSound.play();
			started = false;
			gameover = true;
		}
		
		
        public void start(){
            runner = new Thread(this);
            runner.start();
        }

        public void stop() {
            runner.stop();
        }

        public void run() {

            while(true) {
					
					//Move computer's bat - allow margin for errors
					if (ballX<160){
						if (upwards){
							if ( ballY > batMin) batYC = ballY ;
							else batYC = batMin;
						}
						else {
							if ( ballY < batMax) batYC = ballY - 5;// (int)(Math.random()*5);
							else batYC = batMax;
						}
					}
					
					//Strike ball if in contact with bat
					if(ballX > 289 && ballX < 294 ) {
						// if the ball hits the player's bat change the direction.
						if( ((ballY + 5) > batYP) && ((batYP + 31) > ballY) ) {
							toLeft = true;
							showStatus("Player hits");
							hitSound.play(); 
						}
					}					
					if( ballX > 16 && ballX < 21 ) {
						// if the ball hits the computer's bat change the direction.
						if( ((ballY + 5) > batYC) && ((batYC + 31) > ballY) ) {
							toLeft = false;
							showStatus("Computer hits");							
							hitSound.play(); 
						}
					}
					
					//end game if top score is attained
					if (scoreC==topscore || scoreP==topscore)endGame();
					
					if (started){
						ballX = getXPos(ballX);
						ballY = getYPos(ballY);
					}
					repaint();		
					
					// force a repaint now
                    try {
                            Thread.sleep(30); // pause .03 secs
                    }
                    catch (InterruptedException e)
                    {
                    }
            }
        }
		
	
		///*
		public void update(Graphics g) 
		{
			//This is just your standard double buffering update method
			Graphics offgraph;
			Image offscreen = null;
			Dimension d = getSize();
			offscreen = createImage(d.width, d.height);
			offgraph = offscreen.getGraphics();
			offgraph.setColor(getBackground());
			offgraph.fillRect(0, 0, d.width, d.height);
			offgraph.setColor(getForeground());
			paint(offgraph);
			g.drawImage(offscreen, 0, 0, this);
			offscreen.flush();
		} 
		//*/
		
        public void paint(Graphics g) {				
			
			drawBG(g);		//draw the background
			if(started){
				g.setColor(Color.yellow);
				g.fillOval(ballX,ballY,10,10); //paint the dot
				g.setColor(Color.red);
				g.fillRect(300,batYP,5,30); // paint the player's bat
				g.setColor(Color.blue);
				g.fillRect(15,batYC,5,30); // paint the player's bat
			}
			
			g.setColor(Color.red);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			
			if(!started) g.drawString(" Click Here to Start! ",70,140);
			
			g.setColor(Color.green);
			if(gameover) g.drawString("Game Over! ",110,80);
			g.drawString("Computer",40,240);
			g.drawString("Player",200,240);

			g.setColor(Color.yellow);
			if(gameover) g.drawString(whowon,50,110);
			g.drawString(new Integer(scoreC).toString(),80,270);
			g.drawString(new Integer(scoreP).toString(),220,270);
							
		}
		
		protected void drawBG(Graphics bg){			
			bg.setColor(Color.white);
			bg.drawRect(9,9,301,201);
			bg.drawLine(160,10,160,210);
			bg.drawOval(110,60,100,100);
			
			bg.fillRect(0,220,320,60);
			bg.setColor(Color.white);
			bg.setColor(Color.black);
			bg.fillRoundRect(0,220,320,60,20,20);
		}

		public void loadSounds() {

		  // Load all sound clips by playing and immediately stopping them.

		  try {
		    hitSound = getAudioClip(new URL(getDocumentBase(), "hit.au"));
		    endSound = getAudioClip(new URL(getDocumentBase(), "gameOver.au"));
		  }
		  catch (MalformedURLException e) {}
			hitSound.play();    hitSound.stop();
			endSound.play();	endSound.stop();
		}

		//Move ball in x-direction
		protected int getXPos(int x) {
			if(x > ballXMax) {				
				if(started && !toLeft) {
					scoreC += 1;
					showStatus("Player missed");
					hitSound.play(); 
				}
				toLeft = true;
				return ballXMax;
			}
			
			if(x < ballXMin ) {				
				if(started && toLeft){
					scoreP += 1;		
					showStatus("Computer missed");
					hitSound.play(); 
				}
				toLeft = false;
				return ballXMin;
			}
			
			if(toLeft) return ballX - ballIncr;
			else return ballX + ballIncr;
		}

		//Move ball in Y-direction
		protected int getYPos(int y) {
			if( y > ballYMax) {
				upwards = true;
				hitSound.play(); 
				return ballYMax;
			}
			if(y < ballYMin) {
				upwards = false;
				hitSound.play();
				return ballYMin;
			}
			if(upwards) return ballY - ballIncr;
			else return ballY + ballIncr;
		}
		 
	public void mouseDragged(MouseEvent e) {
		
	}
	//move player's bat
	public void mouseMoved(MouseEvent e) {
		int y = e.getY();
		System.out.println(y);
		if ( (y - batYP) > 0 ) MoveDown(y);
		else MoveUp(y);
	}	
	public void mouseEntered(MouseEvent event) {
    }
    public void mouseExited(MouseEvent event) {
    }
    public void mousePressed(MouseEvent event) {
    }
    public void mouseReleased(MouseEvent event) {
    }

	//start game
    public void mouseClicked(MouseEvent e) {
		if(!started && e.getY() < 160 && e.getY() > 120) {		
			newGame();
		}
		else {			
		}
	}
	
	//move player's bat up
	protected void MoveUp(int y){		
		if ( y > batMin) batYP = y;
		else batYP = batMin;
	}
	//move player's bat down	
	protected void MoveDown(int y){			
		if ( y < batMax) batYP = y;
		else batYP = batMax;
	}
}