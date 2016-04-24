
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Game extends JPanel implements  MouseListener, MouseMotionListener  {
    int ballx = 100;
    int bally = 0;
    int radii = 10;
    int lengthbat = 50;
    int widthbat  =20;
    static int dimenx = 500;
    static int dimeny = 500;
    
    //nomenclature anti-clockwise
    //starting point x1 and y1
	boolean upwards,toLeft, isstarted, isgameruning;
    static int x1 = 20;
    static int y1 = 20;
    int x2 = x1;
    int y2 = y1+dimeny;
    int x3 = x2 + dimenx;
    int y3 = y2 ;
    int x4 = dimenx + x1;;
    int y4 = y1;
    int ballxmin = x1;
    int ballymin = y1;
    int ballxmax = x3-radii;
    int ballymax = y3-radii;
    int score1 , score2,score3,score4 = 0;
    //these will the dynamic variable of the game 
    int baty1 = y1;
    int batx2 = x2;
    int baty3 = y3-lengthbat;
    int batx4 = x4-lengthbat; 
    int ballincr = 2;
   
    // public void init(){  
    	//addMouseMotionListener(this);
		//addMouseListener(this);
    //}
    private void moveBall() {
        ballx = ballxpos(ballx);
        bally = ballypos(bally);
    }
    private void movebat(){
    	
    	
    	
    	
    	
    }
	protected int ballxpos(int x) {
		if(ballx > ballxmax) {				
			if(isstarted && !toLeft) {
				score3 += 1;
				//showStatus("Player missed");
				//hitSound.play(); 
			}
			toLeft = true;
			return ballxmax;
		}
		if(x < ballxmin ) {				
			if(isstarted && toLeft){
				score1 += 1;		
				//showStatus("Computer missed");
				//hitSound.play(); 
			}
			toLeft = false;
			return ballxmin;
		}
		
		if(toLeft) return ballx - ballincr;
		else return ballx + ballincr;
	}

	//Move ball in Y-direction
	protected int ballypos(int y) {
		if( y > ballymax) {
			upwards = true;
			//hitSound.play(); 
			return ballymax;
		}
		if(y < ballymin) {
			upwards = false;
		//	hitSound.play();
			return ballymin;
		}
		if(upwards) return bally - ballincr;
		else return bally + ballincr;
	}

    
    
    @Override
    public void paint(Graphics g) {
 
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillOval(ballx, bally, radii, radii);
        g2d.setColor(Color.BLUE);
        //(Starting x , starting y , dimension x , dimension y)
        g2d.fillRect(x1,baty1,widthbat,lengthbat); // paint the player1's bat
        g2d.setColor(Color.RED);
        g2d.fillRect(batx2,y2-widthbat,lengthbat,widthbat); // paint the player2's bat
        g2d.setColor(Color.GREEN);
        g2d.fillRect(x3-widthbat,baty3,widthbat,lengthbat); // paint the player3's bat
        g2d.setColor(Color.GRAY);
        g2d.fillRect(batx4,y4,lengthbat,widthbat); // paint the player4's bat
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x1, y1, dimenx, dimeny);
        int dia = (x1+x3);
        g2d.drawOval((x1+x3)/2 - dia/4,(y1+y2)/2 - dia/4 , dia/2,dia/2);
        g2d.drawLine((x1+x3)/2, y1,(x1+x3)/2, y2);
        g2d.drawLine(x1, (y1+y2)/2,x3,(y1+y2)/2 );
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Sample Frame");
        Game game = new Game();
    	game.addMouseMotionListener(game);
		game.addMouseListener(game);
    
    	
        frame.add(game);
        frame.setSize(dimenx+x1+50,dimeny+y1+50);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        while (true) {
           game.moveBall();
            game.repaint();
            Thread.sleep(10);
        }
    }
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(10);		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println();
		
		int y = e.getY();
		System.out.println(y);
		if ( (y - baty3) > 0 ) MoveDown(y);
		else MoveUp(y);
	}
	protected void MoveUp(int y){		
		if ( y > ballymin) baty3 = y;
		else baty3 = ballymin;
	}
	//move player's bat down	
	protected void MoveDown(int y){			
		if ( y < (ballymax-lengthbat)) baty3 = y;
		else baty3 = ballymax-lengthbat;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(10);
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(10);
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(10);
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}