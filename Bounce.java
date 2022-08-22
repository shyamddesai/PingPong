package Assignment1;

import java.awt.Color;
import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class Bounce extends GraphicsProgram {
	
	//Screen Dimensions
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 600;
	public static final int OFFSET = 200;
	
	//Ping pong table and walls
	private static final double ppTableXlen = 2.74; // Length
	private static final double ppTableHgt = 1.52; // Ceiling
	private static final double XwallL = 0.05; // Position of left wall
	private static final double XwallR = 2.69; // Position of right wall
	
	//Minimum/Maximum values for world and screen parameters
	public static final double Xmin = 0.0;
	public static final double Xmax = ppTableXlen;
	public static final double Ymin = 0.0;
	public static final double Ymax = ppTableHgt;
	public static final int xmin = 0;
	public static final int xmax =  WIDTH;
	public static final int ymin = 0;
	public static final int ymax = HEIGHT;
	public static final double Xs = (xmax-xmin)/(Xmax-Xmin);
	public static final double Ys = (ymax-ymin)/(Ymax-Ymin);
	public static final double PD = 1;	//trace point diameter
	
	//Simulation parameters
	public static final double g = 9.8;
	public static final double k = 0.1316;	//air friction
	public static final double Pi = 3.1416;
	public static final double bSize = 0.02;
	public static final double bMass = 0.0027;
	public static final double Xinit = XwallL;
	public static final double Yinit = Ymax/2;
	public static final double Vdef = 3.0;	//default velocity (m/s)
	public static final double Tdef = 30.0;	//default angle (degrees)
	
	//Miscellaneous parameters
	private static final double ETHR = 0.001; // Minimum ball energy
	public static final int SLEEP = 10;	//delay time (ms)
	public static final double TICK = SLEEP/1000.0;	//clock increment at each iteration
	public static final boolean DEBUG = false;	//Enable debug messages
	public static final boolean TEST = true;	//Print motion parameters
	
	public void run() {	//Entry point for GraphicsProgram is run()
		this.resize(WIDTH, HEIGHT+OFFSET);	//Resize/initialize graphics window
		
		//Create the ground, and walls on the GUI
		GRect groundLine = new GRect(0, HEIGHT, WIDTH+OFFSET, 2); //Parameters are x, y, width, thickness/length
		groundLine.setColor(Color.BLACK);
		groundLine.setFilled(true);
		add(groundLine);
		
		GRect rightWall = new GRect(XwallR*Xs, 0, 2, HEIGHT);
		rightWall.setColor(Color.RED);
		rightWall.setFilled(true);
		add(rightWall);
		
		GRect leftWall = new GRect(XwallL*Xs, 0, 2, HEIGHT);
		leftWall.setColor(Color.BLUE);
		leftWall.setFilled(true);
		add(leftWall);
			
		//Create trailing points for the ball on the GUI
		GPoint p = W2S(new GPoint(Xinit, Yinit));
		double ScrX = p.getX();	//Convert simulation to screen coordinates
		double ScrY = p.getY();
		
		//Create the ball on the GUI
		GOval myBall = new GOval(ScrX, ScrY, 2*bSize*Xs, 2*bSize*Ys);  //Parameters are x, y, width, length/height
		myBall.setColor(Color.RED);
		myBall.setFilled(true);
		add(myBall);
		pause(1000);	//To see the initial position of the ball

		//Take user input
		double Vo = readDouble("Enter initial velocity [0,100]: ");
		double theta = readDouble("Enter launch angle [-90, 90]: ");
		double loss = readDouble("Enter energy loss parameter [0,1]: ");
//		double Vo = 3.0;
//		double theta = 10;
//		double loss = 0.12;
		
		//Initialize program variables
		double time = 0;
		double Xo = Xinit+bSize;
		double Yo = Yinit;
		double Vt = bMass*g/(4*Pi*bSize*bSize*k);
		double Vox = Vo*Math.cos(theta*Pi/180);
		double Voy = Vo*Math.sin(theta*Pi/180);
		double KEx = ETHR, KEy = ETHR, PE = ETHR;
		double X, Y, Vx, Vy;
		double totalTime = 0;	//My time variable resets to 0 at every collisions and this didn't seem useful for the output 

		boolean running = true;
		System.out.printf("\t\t\t\t Balll Position and Velocity\n");
			
		//Enter simulation loop where we consider wall and ground collisions, and kinetic and potential energies
		while(running) {
			
			//Update relative position and velocities
			X = Vox*Vt/g*(1-Math.exp(-g*time/Vt));
			Y = Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time;
			Vx = Vox*Math.exp(-g*time/Vt);
			Vy = (Voy+Vt)*Math.exp(-g*time/Vt)-Vt;

			//Ground collisions; happens when ball hits the ground 
 			if (Vy<0 && (Yo+Y)<=bSize) {
  				
				//Recalculate energies; only KE exists when the ball hits the ground
				KEx=0.5*bMass*Vx*Vx*(1-loss);
 				KEy=0.5*bMass*Vy*Vy*(1-loss);
 				PE=0;
				
				//Recalculate new Vox and Voy as ball bounces upward
 				Vox=Math.sqrt(2*KEx/bMass);
 				Voy=Math.sqrt(2*KEy/bMass);
				if (Vx<0) Vox=-Vox;		//Allows the ball to continue traveling in the initial direction of motion
				
				//Reinitialize time and motion parameters to reflect a collision
				time=0;		// time is reset at every collision
				Xo+=X;		// need to accumulate distance between collisions
				Yo=bSize;		// the absolute position of the ball on the ground
				X=0;	Y=0;
				
				if((KEx+KEy+PE)<ETHR) running=false;	//Running condition i.e. when ball has no energy left
			}
				
			//Right wall collisions
			if (Vx>0 && (Xo+X)>=XwallR) {
				
				KEx=0.5*bMass*Vx*Vx*(1-loss);
 				KEy=0.5*bMass*Vy*Vy*(1-loss);
 				PE=bMass*g*(Y+Yo);
				
 				Vox=Math.sqrt(2*KEx/bMass);
				Voy=Math.sqrt(2*KEy/bMass);
				Vox = -Vox; //After collision with the right wall, Vox is negative
				if (Vy<0) Voy =-Voy; //The sign of Voy depends on Vy
 				
				time=0;	
				Xo=XwallR-bSize;
				Yo+=Y;
				X=0;	Y=0;
			}
				
			//Collision with left wall
			if (Vx<0 && (Xo+X)<=XwallL+bSize) {
				
				KEx=0.5*bMass*Vx*Vx*(1-loss);
 				KEy=0.5*bMass*Vy*Vy*(1-loss);		
				PE=bMass*g*(Y+Yo);
	
				//Vox is positive
				Vox=Math.sqrt(2*KEx/bMass);
				Voy=Math.sqrt(2*KEy/bMass);
				if (Vy<0) Voy =-Voy; //The sign of Voy might change depending on Vy
				
				time=0;	
				Xo=XwallL+bSize;
				Yo+=Y;
				X=0;	Y=0;
 			}
			
			if (TEST) {
				System.out.printf("t: %.2f\t\tX: %.2f\t\tY: %.2f\t\tVx: %.2f\t\tVy: %.2f\n", totalTime,Xo+X,Yo+Y,Vx,Vy);	//Display current values
			}
			pause(SLEEP); //Pause program
			//pause(SLEEP*5);  
						
			//Trace points for the ball
			p = W2S(new GPoint(Xo+X, Yo+Y)); //Get current screen coordinates
			ScrX = p.getX();
			ScrY = p.getY();
			myBall.setLocation(ScrX, ScrY);
			trace(ScrX+bSize*Xs, ScrY+bSize*Ys);
			
			time += TICK;
			totalTime += TICK;
		}
	}

	/* 
	 	Methods
	 					*/
	
	//W2S method to convert world coordinates into screen coordinates
	GPoint W2S (GPoint P) {
		double X = P.getX();
		double Y = P.getY();
		
		double x = (X-Xmin-bSize)*Xs;
		double y = ymax - (Y-Ymin+bSize)*Ys;
		
		return new GPoint(x,y);		
	}
	
	//Trace points of the ball's path; takes parameters ScrX and ScrY
	private void trace(double ScrX, double ScrY) {
		GOval point = new GOval(ScrX, ScrY, PD, PD); //x, y, width, length
		point.setColor(Color.BLACK);
		point.setFilled(true);
		add(point);		
	}
}
