import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class loader extends JPanel implements  MouseListener, MouseMotionListener,ActionListener  {
	boolean selected = true;
	int level = 1;

	@Override
public void paint(Graphics g){
	super.paint(g);

	         Graphics2D g2d = (Graphics2D) g;
	         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	         g.setFont(new Font("Arial", Font.BOLD, 25));				
	         g2d.drawString("Choose level ",70,70);
	         g2d.drawString("Easy :- " ,70,100);
	         g2d.drawString("Medium :-" ,70,125);
	         g2d.drawString("Hard :- " ,70,150);
	         g2d.fillRect(190,80, 100,20);
	         g2d.fillRect(190,105, 100,20);
	         g2d.fillRect(190,135, 100,20);
	         g2d.fillRect(140, 165, 100, 20);
    	     g.setFont(new Font("Arial", Font.PLAIN, 15));				
	         g2d.setColor(Color.WHITE);       
	         g2d.drawString("Click here", 195, 95);
	         g2d.drawString("Click here", 195, 120);
	         g2d.drawString("Click here", 195, 150);
	         g2d.drawString("Procedd", 145, 180);
	         g2d.setColor(Color.GREEN);       
	         
	         if(selected){
	        	 if(level == 1){ 
	        		 g2d.fillOval(300, 85, 12, 12);
	        		 g2d.fillRect(190,80, 100,20);
	        		 
	        	 }

	        	 if(level == 2){

	        		 g2d.fillOval(300,110 , 12, 12);
	        		 g2d.fillRect(190,105, 100,20);

	    	         
	    	         

	        		 
	        		 
	        	 }

	        	 if(level == 3){
	        		 g2d.fillOval(300, 140, 12, 12);
	        		 g2d.fillRect(190,135, 100,20);
	        		 

	        	 }
	        	 
	        	 
	         }
	
}
public static void main(String[] args)throws InterruptedException{
	 JFrame frame = new JFrame("Sample Frame");
	 loader load = new loader();
	 load.addMouseMotionListener(load);
	 load.addMouseListener(load);
	 frame.add(load);
	 frame.setSize(500,500);
	 frame.setVisible(true);
	 frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

	 
	
	
}
@Override
public void actionPerformed(ActionEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void mouseDragged(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void mouseMoved(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void mouseClicked(MouseEvent e) {
 
	
	
	if(e.getY() < 100 && e.getY() > 80) {		
		level = 1;
		repaint();
	}
   if(e.getY() < 125 && e.getY() > 105) {		
	   level = 2;
		repaint();
	}
 if(e.getY() < 155 && e.getY() > 135) {		
	 level = 3;
		repaint();
}if(e.getY() < 185 && e.getY() > 165) {		

	Game game = new Game(level);
	
      
}

	
}
@Override
public void mouseEntered(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void mouseExited(MouseEvent arg0) {
	// TODO Auto-generated method stub
	
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
