import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Game extends JPanel implements MouseListener, MouseMotionListener,
		ActionListener, Runnable {
	// all the variables
	static Game gameobject;
	static Game gameobjectse;
	int crntplyr = 2;
	int level = 1;
	int index = 0;
	int sigma = 1; //
	int ballx = 0; // x,y co-ordinate of ball tobe used in paint
	int bally = 200;
	int dia = 20; // dia of ball
	int omega = 0; // rotational velocity of ball
	int ballvx = 0; // ball vel. in x,y direction
	int ballvy = 0;
	int lengthbat = 70; // length and width of bat for player
	int widthbat = 10;
	static int dimenx = 500; // dimension of the board
	static int dimeny = 500;
	int topscore = 3; // maximum allowed score to eliminate the player
	// these variable are to store the position of bat to predict the velocity
	// of cursor
	int vlimit = 5; // upper limit of ball velocity in one direction
	int vlimitl = 2; // lower limit of ball vel
	int velcsigma = 20;// this is define the velocity of the bat of computer
	// this will be 0 if that index of client is not there and 1 if there
	int openspot[] = new int[5];

	// these variable store the movement of curosr to CAL. THE VELocity of bat
	int p1[] = new int[5];

	int p2[] = new int[5];
	int p3[] = new int[5];
	int p4[] = new int[6];
	// these all will be the velocity of \each bat i= player number
	int vbat[] = new int[5];
	// chance variable are to moving bat after 8 frame so that variation on the
	// screen could be minimize
	int chance1 = 0;
	int chance2 = 0;
	int chance4 = 0;
	// how much of vel to be used
	double factor = 0.9;
	int time; // how much time to delay the frame so that fame per second can be
				// controlled and also used in judging level
	// nomenclature anti-clockwise
	// starting point x1 and y1
	// for juding the direction of ball
	boolean upwards, toLeft, isstarted, isgameruning, ishit;

	// cordinate of edges of board
	static int x1 = 10;
	static int y1 = 70;
	int x2 = x1;
	int y2 = y1 + dimeny;
	int x3 = x2 + dimenx;
	int y3 = y2;
	int x4 = dimenx + x1;;
	int y4 = y1;
	int ballxmin = x1; // minmum ball can be drawed
	int ballymin = y1;
	int ballxmax = x3 - dia; // max ball can be draw
	int ballymax = y3 - dia;
	int score[] = new int[5];
	int score1, score2, score3, score4 = 0; // score of each player

	// these will the dynamic variable of the game

	// cordinate of each bat for every player only dynamic one for each player
	// as bat is constrined in 1-d
	// this array will store the dynamic co-ordinate of every bat

	int bat[] = new int[5];
	int baty1 = y1;
	// int bat[2] = x2;
	// int bat[3] = y3-lengthbat;
	// int bat[4] = x4-lengthbat;
	// constrain on each bat
	int batymin = y1;
	int batxmin = x1;
	int batymax = y3 - lengthbat;
	int batxmax = x4 - lengthbat;
	// these two variable are like velocity because it is distance change afte
	// revery frame
	// if the frame chnage is assumed to be in constant time
	// we will update these two fro speed change
	// how much ball cordinate increase after every frame
	int ballincrx = 2;
	int ballincry = 0;

	public void init() {
		bat[1] = y1;
		bat[2] = x2;
		bat[3] = y3 - lengthbat;
		bat[4] = x4 - lengthbat;
	}

	// public void init(){
	// addMouseMotionListener(this);
	// addMouseListener(this);
	// }
	// baty[1] = y1;
	// this function is error creator based on input
	// that is the level if 1 - easy , if 2- medium , if 3 - hard
	// hard means computer will do least errors due to close

	public Game(int level) {
		System.out.println("From The game in constructor 116 th line");

		new Thread(this).start();
		System.out.println("From The game after constructor 120 th line");

		gameobject = this;
	}

	public int veldecider() {
		Random r = new Random();
		double i = r.nextGaussian() * velcsigma;
		return (int) i;
	}

	public void levelsetter(int i) {
		// hard for player and good for computer
		if (i == 1) {
			sigma = lengthbat * 3 / 4;
			time = 10;
			vlimit = 10;
			velcsigma = 40;
		}
		// medium
		if (i == 2) {
			sigma = lengthbat;
			time = 20;
			vlimit = 8;
			velcsigma = 30;
		}
		// easy
		if (i == 3) {
			sigma = 2 * lengthbat;
			time = 30;
			vlimit = 5;
			velcsigma = 20;
		}

	}

	public int error() {
		Random rand = new Random();

		int x = (int) (rand.nextGaussian() * sigma);
		// if(x>lengthbat/2 || x < lengthbat*(-1)/2 ){ x = lengthbat/2+dia+1;}

		return x;
	}

	// the below function set the accuracy level for the computer player
	// if the level is hard than bat will fall in the range of lengthbat/2

	private void moveBall() {
		ballx = ballxpos(ballx);
		bally = ballypos(bally);
	}

	// this function is to randomly decide the velocity of the computerbat

	public void todo() {
		// <-------------------------------------------->

		// calculate the computer response if any is active
		if (crntplyr == 3)
			computerplayer();
		// processdata(); // set all the variable..
		// set the value of bring data;
		// <--------------------------------------------------->
		// repeller();// repel the coresponding ball
		// the below function only at central server and this data is than send
		if (crntplyr == 3) {
			repeller();
		}// repel the ball and set all the omega and vel data
		// senddata();
	}

	// call this function from client to send data
	public String clientsend() {
		// usernumber,vbat,bat
		String response = "";
		response = response + crntplyr + "," + vbat[crntplyr] + ","
				+ bat[crntplyr];
		return response;
	}

	// send data of server
	public String serversend() {
		// toleft,upwards,ballx,bally,bat[1],bat[2],bat[3],bat[4],
		// socre[1],socre[2],socre[3],socre[4],omega
		String response = "";
		if (toLeft) {
			response += "1,";
		} else {
			response += "0,";
		}
		if (upwards) {
			response += "1,";
		} else {
			response += "0,";
		}
		response = response + ballx + "," + bally + "," + bat[1] + "," + bat[2]
				+ "," + bat[3] + "," + bat[4] + "," + score[1] + "," + score[2]
				+ "," + score[3] + "," + score[4] + "," + omega;

		return response;
	}

	// this function would be called from the server class only
	public void serverprocessor(String s) {
		// format would be
		// usernumber,vbat,bat
		String ary[] = s.split(",");
		int number = Integer.valueOf(ary[0]).intValue();
		vbat[number] = Integer.valueOf(ary[1]).intValue();
		bat[number] = Integer.valueOf(ary[2]).intValue();
	}

	// call this when a new client is connected to server
	public void connectclient(String s) {
		// playernumber
		int number = Integer.valueOf(s).intValue();
		openspot[number] = 1;
		// means now computer will not be operating this

	}

	public void clientprocessor(String s) {
		// toleft (0,1)0 == false
		// upwards(0,1)
		// ballx
		// bally
		// vbat[]
		// omega
		// bat[]
		// score[]
		// toleft,upwards,ballx,bally,bat[1],bat[2],bat[3],bat[4],
		// socre[1],socre[2],socre[3],socre[4],omega
		String ary[] = s.split(",");
		int number = Integer.valueOf(ary[0]).intValue();
		if (number == 0) {
			toLeft = false;
		} else {
			toLeft = true;
		}

		number = Integer.valueOf(ary[1]).intValue();
		if (number == 0) {
			toLeft = false;
		} else {
			toLeft = true;
		}

		ballx = Integer.valueOf(ary[2]).intValue();
		bally = Integer.valueOf(ary[3]).intValue();
		omega = Integer.valueOf(ary[12]).intValue();
		int i = 1;
		while (i < 5) {
			if (i != crntplyr) {
				// all particular player related data
				// bat[i] = from data
				// vbat[i] =
				bat[i] = Integer.valueOf(ary[i + 3]).intValue();
				score[i] = Integer.valueOf(ary[i + 7]).intValue();
			}
			i++;
		}
		/* toleft,upwards,ballx,bally,omega */
	}

	private void computerplayer() {
		// this chance1 is delaying the frame change to 8 frame per change so
		// that sharp fluctuation could be neglected
		// for playyer 1

		if (ballx < (x1 + x3) / 2 && openspot[1] == 0) {

			if (upwards && chance1 == 0) {
				int x = error();
				// System.out.println(" value of error for player 1 upward before checking is    "+
				// x);
				if (x > lengthbat / 2) {
					x = lengthbat / 2 + dia + 1;
				}

				if (x < lengthbat * (-1) / 2) {
					x = -1 * lengthbat / 2 + dia + 1;
				}

				// System.out.println(" value of error for player 1 after checking is   "+
				// x);
				if (bally > batymin + lengthbat)
					bat[1] = bally - lengthbat / 2 + x;
				else
					bat[1] = batymin;
				chance1++;
				// System.out.println(score3);
			} else if (!upwards && chance1 == 0) {
				int x = error();
				// System.out.println(" value of error for player 1 down before checking is    "+
				// x);
				if (x > lengthbat / 2) {
					x = lengthbat / 2 + dia + 1;
				}
				if (x < lengthbat * (-1) / 2) {
					x = -1 * lengthbat / 2 + dia + 1;
				}

				// System.out.println(" value of error for player 1 after checking is   "+
				// x);

				if (bally < batymax - lengthbat)
					bat[1] = bally + (int) dia / 2 - lengthbat / 2 - x;// (int)(Math.random()*5);
				else
					bat[1] = batymax;
				chance1++;
			} else {
				chance1++;
				if (chance1 > 8) {
					chance1 = 0;
				}
			}

			// taking the VELOCITY OF BATA AFTER THE BALL CROOS A LINE
			if (ballx < (x1 + x3) / 4) {
				vbat[1] = (int) veldecider();
			}
		}
		// for player 2
		if (bally > (y1 + y3) / 2 && openspot[2] == 0) {
			if (toLeft && chance2 == 0) {
				int x = error();
				if (x > lengthbat / 2) {
					x = lengthbat / 2 + dia + 1;
				}
				if (x < lengthbat * (-1) / 2) {
					x = -1 * lengthbat / 2 + dia + 1;
				}

				// System.out.println(" value of error before checking is   "+
				// x);
				if (ballx > batxmin + lengthbat)
					bat[2] = ballx + (int) dia / 2 - lengthbat / 2 + x;
				else
					bat[2] = batxmin;
				chance2++;
			} else if (!toLeft && chance2 == 0) {
				int x = error();
				// if(x>lengthbat/2 || x < lengthbat*(-1)/2 ){ x =
				// lengthbat/2+dia+1;}
				if (x > lengthbat / 2) {
					x = lengthbat / 2 + dia + 1;
				}
				if (x < lengthbat * (-1) / 2) {
					x = -1 * lengthbat / 2 + dia + 1;
				}

				if (ballx < batxmax - lengthbat)
					bat[2] = ballx + (int) dia / 2 - lengthbat / 2 - x;// (int)(Math.random()*5);
				else
					bat[2] = batxmax;
				chance2++;

			} else {
				chance2++;
				if (chance2 > 8) {
					chance2 = 0;
				}
			}

			if (bally < 3 * (y1 + y3) / 4) {
				/*
				 * p2[0] = bat[2]; // velocity is postion change in 3 frames
				 * times vbat2 = p2[0]-p2[3]; p2[3] = p2[2]; p2[2] = p2[1];
				 * p2[1] = p2[0];
				 */
				vbat[2] = veldecider();
				// System.out.println("i was here in vbat 2  and vbat is set to   "
				// + vbat2);

			}

		}
		// for player 4
		if (bally < (y1 + y3) / 2 && openspot[4] == 0) {

			if (toLeft && chance4 == 0) {
				int x = error();
				if (x > lengthbat / 2) {
					x = lengthbat / 2 + dia + 1;
				}
				if (x < lengthbat * (-1) / 2) {
					x = -1 * lengthbat / 2 + dia + 1;
				}

				// if(x>lengthbat/2 || x < lengthbat*(-1)/2 ){ x =
				// lengthbat/2+dia+1;}
				// System.out.println(" value of error before checking is   "+
				// x);
				if (ballx > batxmin + lengthbat)
					bat[4] = ballx + (int) dia / 2 - lengthbat / 2 - x;
				else
					bat[4] = batxmin;
				chance4++;

			} else if (!toLeft && chance4 == 0) {
				int x = error();
				if (x > lengthbat / 2) {
					x = lengthbat / 2 + dia + 1;
				}
				if (x < lengthbat * (-1) / 2) {
					x = -1 * lengthbat / 2 + dia + 1;
				}
				// if(x>lengthbat/2 || x < lengthbat*(-1)/2 ){ x =
				// lengthbat/2+dia+1;}
				if (ballx < batxmax - lengthbat)
					bat[4] = ballx + (int) dia / 2 - lengthbat / 2 + x;// (int)(Math.random()*5);
				else
					bat[4] = batxmax;
				chance4++;
			} else {
				chance4++;
				if (chance4 > 8) {
					chance4 = 0;
				}
			}
			if (bally < (y1 + y3) / 4) {

				// p4[0] = bat[4];
				// velocity is postion change in 3 frames times
				/*
				 * vbat4 = p4[0]-p4[3]; p4[3] = p4[2]; p4[2] = p4[1]; p4[1] =
				 * p4[0];
				 */
				vbat[4] = veldecider();
				// System.out.println("i was here in vbat and vbat is set to   "
				// + vbat4);
			}
		}
		// player 3 would be person
	}

	private void repeller() { // this function only run on central server

		// only done if computer is player 3
		// player 3 manual
		if (ballx + dia > (x3 - widthbat) && ballx < x3) {
			// if the ball hits the player's bat change the direction.
			if (((bally + dia) > bat[crntplyr])
					&& ((bat[3] + lengthbat) > bally)) {

				speedychanger(vbat[crntplyr]);
				// System.out.println("I was here in true and score not count and vbAT IS "
				// + vbat3);
				toLeft = true;
				// showStatus("Player hits");
				// hitSound.play();
			}
		}

		// player 1 computer
		if ( /* ballx > 16 && */ballx < x1 + widthbat) {
			// if the ball hits the computer's bat change the direction.
			if (((bally + dia) > bat[1]) && ((bat[1] + lengthbat) > bally)) {
				toLeft = false;
				speedychanger(vbat[1]);
				// //System.out.println("from to do");
				// showStatus("Computer hits");
				// hitSound.play();
			}
		}
		// player 2 computer
		if (bally + dia > (y3 - widthbat) && bally < y3) {
			// if the ball hits the player's bat change the direction.
			if (((ballx + dia) > bat[2]) && ((bat[2] + lengthbat) > ballx)) {
				upwards = true;

				speedxchanger(vbat[2]);
				// System.out.println("from to do i wanted");

				// showStatus("Player hits");
				// hitSound.play();
			}
		}
		// player 4
		if (bally > 16 && bally < y1 + widthbat) {
			// if the ball hits the computer's bat change the direction.
			if (((ballx + dia) > bat[4]) && ((bat[4] + lengthbat) > ballx)) {
				upwards = false;

				speedxchanger(vbat[4]);

				// System.out.println("from to do");
				// showStatus("Computer hits");
				// hitSound.play();
			}
		}

		// end game if top score is attained
		if (score[1] == topscore || score[3] == topscore)
			endGame();

		if (isstarted) {
			ballx = ballxpos(ballx);
			bally = ballypos(bally);
		}

	}

	// calculate the speed of the ball after collision only
	private void speedxchanger(int velbat) {
		// tthe below K is just a factor to decide the effect of speed
		double k = .1;
		double ko = .09;
		// System.out.println("omega before collsion is    "+omega);
		// this omega could be negative
		// System.out.println("omega before collsion is  ==   "+ omega +
		// " with velbat == " + velbat + "and dia == "+ dia);
		omega = omega + (int) ((velbat) * ko);
		if (omega > vlimit) {
			omega = vlimit;
		}
		if (omega < -vlimit) {
			omega = -vlimit;
		}
		// omega = omega - ((omega*dia-velbat)/dia)*ko ;
		// System.out.println("omega after collision is    "+omega);
		// System.out.println("ball in crement before collsion is    "+
		// ballincrx + " and ballincr y is " + ballincry);

		ballincrx = ballincrx + (int) (2 * ballincry * omega * k);
		if (ballincrx > vlimit) {
			ballincrx = vlimit;
		}
		if (ballincrx < -vlimit) {
			ballincrx = -vlimit;
		}
		// ballincrx = (int)(2*ballincry*omega*k);
		if (ballincrx < vlimitl && ballincrx >= 0) {
			ballincrx = vlimitl;
		}
		if (ballincrx > -vlimitl && ballincrx < 0) {
			ballincrx = -vlimitl;
		}

		// System.out.println("ball in crement after collsion and before sign change    "+
		// ballincrx);

		if (ballincrx < 0) {
			toLeft = !toLeft;
			ballincrx = ballincrx * (-1);
		}
		// System.out.println("ball in crement after collsion is  and after sign change   "+
		// ballincrx);
	}

	private void speedychanger(int velbat) {
		// tthe below K is just a factor to decide the effect of speed
		double k = .1;
		double ko = .09;
		// System.out.println("omega before collsion in y   "+omega);
		// omega = omega - ((omega*dia-velbat)/dia)*ko ;

		// System.out.println("omega after collision is    "+omega);

		// this omega could be negative

		// System.out.println("omega before collsion in y is  ==   "+ omega +
		// "with velbat == " + velbat + "and dia == "+ dia);
		omega = omega + (int) ((velbat / dia) * ko);
		if (omega > vlimit) {
			omega = vlimit;
		}
		if (omega < -vlimit) {
			omega = -vlimit;
		}

		// System.out.println("omega after collision is  in y  "+omega);
		// System.out.println("ball in crement before collsion is  in y   "+
		// ballincrx + " and ballincr y is " + ballincry);

		ballincry = ballincry + (int) (2 * ballincrx * omega * k);
		// set the limit in velocity of ball

		if (ballincry > vlimit) {
			ballincry = vlimit;
		}
		if (ballincry < -vlimit) {
			ballincry = -vlimit;
		}

		if (ballincry < vlimitl && ballincry >= 0) {
			ballincry = vlimitl;
		}
		if (ballincry > -vlimitl && ballincry < 0) {
			ballincry = -vlimitl;
		}
		if (ballincry < 0) {
			upwards = !upwards;
			ballincry = ballincry * (-1);
			// System.out.println("ball in crement after collsion is  and after sign change   "+
			// ballincrx);

		}
	}

	// increase the score if ball is missed
	protected int ballxpos(int x) {
		if (x > ballxmax) {
			// if the player miss then it change the direction and count score
			// exactly when it past the mark of maximum limit
			// System.out.println("I was here in score3 and to left is  " +
			// toLeft );

			if (isstarted && !toLeft) {
				score[3] += 1;
				// showStatus("Player missed");
				// hitSound.play();
				// System.out.println("I was here in score3  " + score3 );
			}
			// System.out.println("I was here in score3 after if  " + score3 );
			toLeft = true;
			return ballxmax;
		}
		// same as above for computer player
		if (x < ballxmin) {
			if (isstarted && toLeft) {
				score[1] += 1;
				// System.out.println("the score of 1 is  " + score1 +
				// "direction up is " + upwards);

				// showStatus("Computer missed");
				// hitSound.play();
			}

			// System.out.println("in ball xpos");
			toLeft = false;
			return ballxmin;
		}

		if (toLeft)
			return ballx - ballincrx;
		else
			return ballx + ballincrx;
	}

	// Move ball in Y-direction
	protected int ballypos(int y) {
		// --------------//
		// score counter for player 2
		if (y > ballymax) {
			if (isstarted && !upwards) {
				score[2] += 1;

				// System.out.println("score 2 is " + score2 +
				// "status of is left is " + toLeft);
				// showStatus("Player missed");
				// hitSound.play();
			}
			upwards = true;
			return ballymax;
		}
		// score counter for player 4
		if (y < ballymin) {
			if (isstarted && upwards) {
				score[4] += 1;
				// System.out.println("score4 is " + score4);
				// showStatus("Computer missed");
				// hitSound.play();
			}

			// System.out.println("in ball xpos");
			upwards = false;
			return ballymin;
		}

		// -------------//
		if (upwards)
			return bally - ballincry;
		else
			return bally + ballincry;
	}

	@Override
	public void paint(Graphics g2) {

		super.paint(g2);
		// g = g2;
		// System.out.println("is started in paint is " + isstarted);
		Graphics2D g2d = (Graphics2D) g2;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// format -- > (outer point of x co-ordinate,outer point of
		// y-co-ordinate,radius x, radius y)

		// dia is atgually dia here so dia/2 = diaactual
		if (!isstarted) {
			g2d.drawString("Choose level ", 70, 70);
			g2d.drawString("Easy :- ", 70, 100);
			g2d.drawString("Medium :-", 70, 125);
			g2d.drawString("Hard :- ", 70, 150);
			g2d.fillRect(190, 80, 100, 20);
			g2d.fillRect(190, 105, 100, 20);
			g2d.fillRect(190, 135, 100, 20);
			g2d.fillRect(140, 165, 100, 20);
			g2.setFont(new Font("Arial", Font.PLAIN, 15));
			g2d.setColor(Color.WHITE);
			g2d.drawString("Click here", 195, 95);
			g2d.drawString("Click here", 195, 120);
			g2d.drawString("Click here", 195, 150);
			g2d.drawString("Procedd", 145, 180);
			g2d.setColor(Color.GREEN);

			if (level == 1) {
				g2d.fillOval(300, 85, 12, 12);
				g2d.fillRect(190, 80, 100, 20);

			}

			if (level == 2) {

				g2d.fillOval(300, 110, 12, 12);
				g2d.fillRect(190, 105, 100, 20);

			}

			if (level == 3) {
				g2d.fillOval(300, 140, 12, 12);
				g2d.fillRect(190, 135, 100, 20);

			}

		}

		else {
			g2d.fillOval(ballx, bally, dia, dia);
			int i1 = ballx + dia / 2;
			int j1 = bally + dia / 2;
			double k = .05;
			double i2 = i1 + dia / 2 * Math.cos(omega * k * index) + 5;
			double j2 = j1 + dia / 2 * Math.sin(omega * k * index) + 5;
			g2d.setColor(Color.GREEN);

			g2d.drawLine(i1, j1, (int) i2, (int) j2);
			g2d.setColor(Color.BLUE);
			g2d.drawString("Score card :-", 15, 15);
			// (Starting x , starting y , dimension x , dimension y)
			g2d.fillRect(x1, bat[1], widthbat, lengthbat); // paint the
															// player1's bat
			g2d.drawString("PLAYER1 :- " + score[1], 30, 30);
			g2d.setColor(Color.RED);
			g2d.fillRect(bat[2], y2 - widthbat, lengthbat, widthbat); // paint
																		// the
																		// player2's
																		// bat
			g2d.drawString("PLAYER2 :- " + score[2], 120, 30);
			g2d.setColor(Color.GREEN);
			g2d.drawString("PLAYER3 :- " + score[3], 210, 30);
			g2d.fillRect(x3 - widthbat, bat[3], widthbat, lengthbat); // paint
																		// the
																		// player3's
																		// bat
			g2d.setColor(Color.GRAY);
			g2d.drawString("PLAYER4 :- " + score[4], 290, 30);
			g2d.fillRect(bat[4], y4, lengthbat, widthbat); // paint the
															// player4's bat
			g2d.setColor(Color.BLACK);
			int vel = (int) Math.pow(
					Math.pow(ballincrx, 2) + Math.pow(ballincry, 2), .5);
			g2d.drawString("Ball velocity :- " + vel + "pixel/frame", 100, 60);
			g2d.drawString("omega :- " + omega, 30, 60);
			g2d.drawRect(x1, y1, dimenx, dimeny);
			int dia2 = (x1 + x3);

			g2d.drawOval((x1 + x3) / 2 - dia2 / 4, (y1 + y2) / 2 - dia2 / 4,
					dia2 / 2, dia2 / 2);
			g2d.drawLine((x1 + x3) / 2, y1, (x1 + x3) / 2, y2);
			g2d.drawLine(x1, (y1 + y2) / 2, x3, (y1 + y2) / 2);
		}
	}

	public void endGame() {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		// TODO Auto-generated method stub
		// System.out.println();

		if (crntplyr == 1 || crntplyr == 3) {
			int y = e.getY();
			p4[0] = y;
			// velocity is postion change in 3 frames times
			vbat[crntplyr] = p4[0] - p4[3];
			p4[5] = p4[4];
			p4[4] = p4[3];
			p4[3] = p4[2];
			p4[2] = p4[1];
			p4[1] = p4[0];
			// System.out.println(vbat4);
			if ((y - bat[3]) > 0)
				MoveDown(p4[0]);
			else
				MoveUp(p4[0]);
		}
		if (crntplyr == 2 || crntplyr == 4) {
			int x = e.getX();
			p4[0] = x;
			// velocity is postion change in 3 frames times
			vbat[crntplyr] = p4[0] - p4[5];
			p4[5] = p4[4];
			p4[4] = p4[3];
			p4[3] = p4[2];
			p4[2] = p4[1];
			p4[1] = p4[0];
			// System.out.println(vbat4);
			if ((x - bat[3]) > 0)
				Moveleft(p4[0]);
			else
				Moveright(p4[0]);
		}
	}

	protected void Moveleft(int x) {
		if (x > ballxmin)
			bat[crntplyr] = x;
		else
			bat[crntplyr] = ballxmin;

	}

	protected void Moveright(int x) {

		if (x > ballxmin)
			bat[crntplyr] = x;
		else
			bat[crntplyr] = ballymax - lengthbat;

	}

	protected void MoveUp(int y) {
		if (y > ballymin)
			bat[crntplyr] = y;
		else
			bat[crntplyr] = ballymin;
	}

	// move player's bat down
	protected void MoveDown(int y) {

		if (y < ballymax)
			bat[crntplyr] = y;
		else
			bat[crntplyr] = ballymax - lengthbat;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// System.out.println(10);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		// System.out.println(10);
		if (!isstarted) {
			if (e.getY() < 100 && e.getY() > 80) {
				level = 1;
				repaint();
			}
			if (e.getY() < 125 && e.getY() > 105) {
				level = 2;
				repaint();
			}
			if (e.getY() < 155 && e.getY() > 135) {
				level = 3;
				repaint();
			}
			if (e.getY() < 185 && e.getY() > 165) {
				isstarted = true;
				startgame();

			}
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// System.out.println(10);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// System.out.println(10);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void startgame() {

	}

	public static void main(String[] args) {
		Game game = new Game(2);
		game.endGame();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Ping pong Game");
		// Game game = new Game();
		addMouseMotionListener(this);
		addMouseListener(this);
		levelsetter(level);// 3-- being the easy
		frame.add(this);
		frame.setSize(dimenx + x1 + 50, dimeny + y1 + 50);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(dimenx + x1 + 50, dimeny + y1 + 50);
		while (true) {
			isstarted = true;
			if (crntplyr == 3) {
				todo();
			}
			repaint();

			// time being dependent on the level the speed of the ball varies

			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}