
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Random;

@SuppressWarnings("serial")
public class Game extends JPanel implements  MouseListener, MouseMotionListener  {
	int sigma  =1;
	int ballx = 400;
    int bally = 0;
    int dia = 10;
    int omega = 0;
    int ballvx = 0;
    int ballvy = 0;
    int lengthbat = 50;
    int widthbat  =10;
    static int dimenx = 500;
    static int dimeny = 500;
    int topscore = 3;
    // these variable are to store the position of bat to predict the velocity of cursor
    int p1 []  = new int [4];
    int p2 []  = new int [4];
    int p3 []  = new int [4];
    int p4 []  = new int [4];
    int vbat1 = 0;
    int vbat2 = 0;
    int vbat3 = 0;
    int vbat4 = 0;
    int chance1 =0;
    int chance2 = 0;
    int chance4 = 0;
    double factor = 0.9;
    
    //nomenclature anti-clockwise
    //starting point x1 and y1
	boolean upwards,toLeft, isstarted, isgameruning , ishit;
    static int x1 = 10;
    static int y1 = 70;
    int x2 = x1;
    int y2 = y1+dimeny;
    int x3 = x2 + dimenx;
    int y3 = y2 ;
    int x4 = dimenx + x1;;
    int y4 = y1;
    int ballxmin = x1;
    int ballymin = y1;
    int ballxmax = x3-dia;
    int ballymax = y3-dia;
    int score1 , score2,score3,score4 = 0;
    //these will the dynamic variable of the game 
    int baty1 = y1;
    int batx2 = x2;
    int baty3 = y3-lengthbat;
    int batx4 = x4-lengthbat; 
    int batspeed1 = 0;
    int batspeed2 = 0;
    int batsspeed3 = 0;
    int batspeed4 = 0;
    int batymin = y1;
    int batxmin = x1;
    int batymax = y3-lengthbat;
    int batxmax = x4-lengthbat;
    // these two variable are like velocity because it is distance change afte revery frame
    // if the frame chnage is assumed to be in constant time 
    // we will update these two fro speed change
    int ballincrx = 2;
    int ballincry = 2;
    
    // public void init(){  
    	//addMouseMotionListener(this);
		//addMouseListener(this);
    //}
    
    // this function  is error creator based on input
    // that is the level if 1 - easy , if 2- medium , if 3 - hard
    // hard means computer will do least errors due to close
    public int error(){
    	Random rand = new Random(); 	 
    	
    	int  x = (int )(rand.nextGaussian()*sigma);
//    	if(x>lengthbat/2 || x < lengthbat*(-1)/2 ){ x = lengthbat/2+dia+1;}		
		
    	return x;
    }
    // the below function set the accuracy level for the computer player
    // if the level is hard than bat will fall in the range of lengthbat/2 
    public void levelsetter(int i){
    	// hard for player and good for computer
    	if(i == 1){ sigma =  lengthbat/2;}
    	// medium 
    	if(i == 2){ sigma  = lengthbat*3/4;}
    	// easy
    	if(i == 3){ sigma  = lengthbat*3/4; }
    	
    }
    private void moveBall() {
        ballx = ballxpos(ballx);
        bally = ballypos(bally);
    }
    
    private void  speedxchanger(int velbat){
    	// tthe below K is just a factor to decide the effect of speed
    	double k = .005;
    	int ko = 1;
    	System.out.println("omega before collsion is    "+omega);
    	//this omega could be negative
    	//System.out.println("omega before collsion with velbat ==   "+ velbat);
    	omega =  omega + (velbat/dia)*ko ;
    	//omega =  omega - ((omega*dia-velbat)/dia)*ko ;
    	//System.out.println("omega after collision is    "+omega);
    	//System.out.println("ball in crement before collsion is    "+ ballincrx);
    	
    	ballincrx = ballincrx + (int)(2*ballincry*omega*k); 
    //	ballincrx =  (int)(2*ballincry*omega*k); 
    	
    	if (ballincrx <0){ toLeft = !toLeft; 
    	ballincrx = ballincrx*(-1);
    	}
    	//System.out.println("ball in crement after collsion is    "+ ballincrx);
    	
    
    }
    private void  speedychanger(int velbat){
    	// tthe below K is just a factor to decide the effect of speed
    	double k = .05;
    	int ko = 1;
    	//System.out.println("omega before collsion in y   "+omega);
    	//omega =  omega - ((omega*dia-velbat)/dia)*ko ;
    	//System.out.println("omega after collision is    "+omega);
    	
    	//this omega could be negative
    	omega =  omega + (velbat/dia)*ko ;
    	//System.out.println("omega after collision in  y    "+omega);
    	//System.out.println("ball in crement in y is before collsion is    "+ ballincry);
    	
    	ballincry = ballincry + (int)(2*ballincrx*omega*k); 
    	//System.out.println("ball in crement in y is after collsion is    "+ ballincry);
    	
    	if (ballincry <0){ upwards = !upwards; 
    	ballincry = ballincry*(-1);
    	}
    
    
    }
    
    private void computerplayer(){
    //this chance1 is delaying the frame change to 8 frame per change so that sharp fluctuation could be neglected
   	// for playyer 1
    	 if (ballx<(x1+x3)/2 ){
    		
			if (upwards && chance1 == 0){
				int x = error();
				System.out.println(" value of error for player 1 upward before checking is    "+ x);
				if(x>lengthbat/2 || x < lengthbat*(-1)/2 ){ x = lengthbat/2+dia+1;}		

				System.out.println(" value of error for player 1 after checking is   "+ x);
				if ( bally > batymin+lengthbat) baty1 = bally + (int)dia/2 - lengthbat/2 + x  ;
				else baty1 = batymin;
				chance1 ++;
				//System.out.println(score3);
			}
			else if (!upwards && chance1 == 0) {
				int x = error();
				System.out.println(" value of error for player 1 down before checking is    "+ x);
				if(x>lengthbat/2 || x < lengthbat*(-1)/2 ){ x = lengthbat/2+dia+2;}		

				System.out.println(" value of error for player 1 after checking is   "+ x);

				if ( bally < batymax-lengthbat) baty1 = bally + (int )dia/2 - lengthbat/2 - x;// (int)(Math.random()*5);
				else baty1 = batymax;
				chance1++;
			}
			else{
				chance1++;
				if (chance1>8){ chance1 = 0;}
				}
			
			//taking the VELOCITY OF BATA AFTER THE BALL CROOS A LINE 
			if (ballx<(x1+x3)/4){
				p4[0] = baty1;
				// velocity is postion change in 3 frames times
				 vbat1 = p1[0]-p1[3];
				 p1[3] = p1[2];
				 p1[2] = p1[1];
				p1[1] = p1[0];
			
			}
   	}
   	//for player 2
   	if ( bally > (y1+y3)/2){
			if (toLeft && chance2 == 0){
				int x = error();
				if(x>lengthbat/2 || x < lengthbat*(-1)/2 ){ x = lengthbat/2+dia+1;}		
				//System.out.println(" value of error before checking is   "+ x);
			    if ( ballx > batxmin+lengthbat) batx2 = ballx + (int)dia/2 - lengthbat/2-x;
				else batx2 = batxmin;
				chance2++;
			}
			else if (!toLeft && chance2 == 0) {
				int x = error();
				//if(x>lengthbat/2 || x < lengthbat*(-1)/2 ){ x = lengthbat/2+dia+1;}		

				if ( ballx < batxmax-lengthbat) batx2 = ballx + (int)dia/2-lengthbat/2+x;// (int)(Math.random()*5);
				else batx2 = batxmax;
				chance2++;
				
			}
			else {
				chance2++;
				if (chance2>8){ chance2 = 0;}
			}
			
			
			if (bally<3*(y1+y3)/4){
				p2[0] = batx2;
				// velocity is postion change in 3 frames times
				 vbat2 = p2[0]-p2[3];
				 p2[3] = p2[2];
				 p2[2] = p2[1];
				 p2[1] = p2[0];
			}
   	
		}
   	//for player 4
   	if (bally<(y1+y3)/2){

		if (toLeft && chance4 == 0){
			int x = error();
			if(x>lengthbat/2 || x < lengthbat*(-1)/2 ){ x = lengthbat/2+dia+1;}		
			//System.out.println(" value of error before checking is   "+ x);
		    if ( ballx > batxmin+lengthbat) batx4 = ballx + (int)dia/2 - lengthbat/2-x;
			else batx4 = batxmin;
			chance4++;
		}
		else if (!toLeft && chance4 == 0) {
			int x = error();
			if(x>lengthbat/2 || x < lengthbat*(-1)/2 ){ x = lengthbat/2+dia+1;}		
			if ( ballx < batxmax-lengthbat) batx4 = ballx + (int)dia/2-lengthbat/2+x;// (int)(Math.random()*5);
			else batx4 = batxmax;
			chance4++;
		}
		else {
			chance4++;
			if (chance4>8){ chance4 = 0;}
		}
		

   		
   		
			if (bally< (y1+y3)/4){
				p4[0] = batx4;
				// velocity is postion change in 3 frames times
				 vbat1 = p4[0]-p4[3];
				 p4[3] = p4[2];
				 p4[2] = p4[1];
				p4[1] = p4[0];
			}
   		
			
		}
   	// player 3 would be person
   	}
    private void repeller(){
    	//BOunce back ball if in contact with bat and change the speed as well
    	// by changing the ballxincr and y
		//player 3 manual 
    	if( ballx + dia > (x3-widthbat) && ballx < x3 ) {
			// if the ball hits the player's bat change the direction.
			if( ((bally + dia) > baty3) && ((baty3 + lengthbat) > bally) ) {
	
				speedychanger(vbat3);
				System.out.println("I was here in true and score not count and vbAT IS "  + vbat3);
				toLeft = true;
				//showStatus("Player hits");
				//hitSound.play(); 
			}
		}				
    	// player 1 computer
		if( /*ballx > 16 &&*/ ballx < x1 + widthbat ) {
			// if the ball hits the computer's bat change the direction.
			if( ((bally + dia) > baty1) && ((baty1 + lengthbat) > bally) ) {
				toLeft = false;
				speedychanger(vbat1);
				////System.out.println("from to do");
				//showStatus("Computer hits");							
				//hitSound.play(); 
			}
		}
		// player 2 computer
		if( bally+dia  > (y3-widthbat) && bally < y3 ) {
			// if the ball hits the player's bat change the direction.
			if( ((ballx + dia) > batx2) && ((batx2 + lengthbat) > ballx) ) {
				upwards = true;

				speedxchanger(vbat2);
				//System.out.println("from to do i wanted");
				
				//showStatus("Player hits");
				//hitSound.play(); 
			}
		}					
		// player 4
		if( bally > 16 && bally < y1 + widthbat ) {
			// if the ball hits the computer's bat change the direction.
			if( ((ballx + dia) > batx4) && ((batx4 + lengthbat) > ballx) ) {
				upwards = false;

				speedxchanger(vbat4);

				//System.out.println("from to do");
				//showStatus("Computer hits");							
				//hitSound.play(); 
			}
		}
		
		//end game if top score is attained
		if (score1==topscore || score3==topscore) endGame();
		
		if (isstarted){
			ballx = ballxpos(ballx);
			bally = ballypos(bally);
		}

    }
    
    
    public void todo(){
       	//<-------------------------------------------->
    	computerplayer();
    	     	
    	//<--------------------------------------------------->
    	repeller();
    }

	
	protected int ballxpos(int x) {
		if(x > ballxmax) {	
			//if the player miss then it change the direction and count score 
			// exactly when it past the mark of maximum limit
			System.out.println("I was here in score3 and to left is  " + toLeft );
			 
			if(isstarted && !toLeft) {
				score3 += 1;
				//showStatus("Player missed");
				//hitSound.play(); 
			System.out.println("I was here in score3  " + score3 );
			}

			System.out.println("I was here in score3 after if  " + score3 );
			toLeft = true;
			return ballxmax;
		}
		//same as above for computer player
		if(x < ballxmin) {				
			if(isstarted && toLeft){
				score1 += 1;
				System.out.println("the score of 1 is  " + score1 + "direction up is " + upwards);
					
				//showStatus("Computer missed");
				//hitSound.play(); 
			}

		//	System.out.println("in ball xpos");
			toLeft = false;
			return ballxmin;
		}
		
		if(toLeft) return ballx - ballincrx;
		else return ballx + ballincrx;
	}
	
	//Move ball in Y-direction
	protected int ballypos(int y) {
		//--------------//
		// score counter for player 2
		if(y > ballymax) {				
			if(isstarted && !upwards) {
				score2 += 1;

				System.out.println("score 2 is " + score2 + "status of is left is " + toLeft);
				//showStatus("Player missed");
				//hitSound.play(); 
			}
			upwards = true;
			return ballymax;
		}
		//score counter for player 4
		if(y < ballymin ) {				
			if(isstarted && upwards){
				score4 += 1;		
				System.out.println("score4 is " + score4);

				//showStatus("Computer missed");
				//hitSound.play(); 
			}

		//	System.out.println("in ball xpos");
			upwards = false;
			return ballymin;
		}

		//-------------//
		if(upwards) return bally -ballincry;
		else return bally + ballincry;
	}

    @Override
    public void paint(Graphics g) {
 
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        // format -- > (outer point of x co-ordinate,outer point of y-co-ordinate,radius x, radius y)
        //dia is atgually dia here so dia/2 = diaactual
        g2d.fillOval(ballx, bally, dia, dia);
        g2d.setColor(Color.BLUE);
        g2d.drawString("Score card :-" ,15, 15);
        //(Starting x , starting y , dimension x , dimension y)
        g2d.fillRect(x1,baty1,widthbat,lengthbat); // paint the player1's bat
        g2d.drawString("PLAYER1 :- "+ score1 ,30, 30);
        g2d.setColor(Color.RED);
        g2d.fillRect(batx2,y2-widthbat,lengthbat,widthbat); // paint the player2's bat
        g2d.drawString("PLAYER2 :- "+ score2 ,120, 30);
        g2d.setColor(Color.GREEN);
        g2d.drawString("PLAYER3 :- "+ score3 ,210, 30);
        g2d.fillRect(x3-widthbat,baty3,widthbat,lengthbat); // paint the player3's bat
        g2d.setColor(Color.GRAY);
        g2d.drawString("PLAYER4 :- "+ score4 ,290, 30);
        g2d.fillRect(batx4,y4,lengthbat,widthbat); // paint the player4's bat
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x1, y1, dimenx, dimeny);
        int dia2 = (x1+x3);
        g2d.drawOval((x1+x3)/2 - dia2/4,(y1+y2)/2 - dia2/4 , dia2/2,dia2/2);
        g2d.drawLine((x1+x3)/2, y1,(x1+x3)/2, y2);
        g2d.drawLine(x1, (y1+y2)/2,x3,(y1+y2)/2 );
    }

      public void endGame(){
    	
    	
    	
    }
    
	
	@Override
	public void mouseMoved(MouseEvent e) {
	
		// TODO Auto-generated method stub
		//System.out.println();
	
		
		int y  = e.getY();
		p4[0] = y;
		// velocity is postion change in 3 frames times
		 vbat3 = p4[0]-p4[3];
		 p4[3] = p4[2];
		 p4[2] = p4[1];
		p4[1] = p4[0];
//		System.out.println(vbat4);
		if ( (y-baty3) > 0 ) MoveDown(p4[0]);
		else MoveUp(p4[0]);
	}
	protected void MoveUp(int y){		
		if ( y > ballymin) baty3 = y;
		else baty3 = ballymin;
	} 
	//move player's bat down	
	protected void MoveDown(int y){			
		
		if ( y > ballymin) baty3 = y;
		else baty3 = ballymax-lengthbat;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
	//	System.out.println(10);		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println(10);
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	//	System.out.println(10);
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	//	System.out.println(10);
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	 public static void main(String[] args) throws InterruptedException {
	        JFrame frame = new JFrame("Ping pong Game");
	        Game game = new Game();
	    	game.addMouseMotionListener(game);
			game.addMouseListener(game);
			game.levelsetter(3);// 3-- being the easy
	        frame.add(game);
	        frame.setSize(dimenx+x1+50,dimeny+y1+50);
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        while (true) {
	        	game.isstarted= true;
	        	game.todo();
	            game.moveBall();
	            game.repaint();
	            Thread.sleep(30);
	        }
	    }
	    	
}