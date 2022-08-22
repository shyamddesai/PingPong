package ppPackage;

import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import static ppPackage.ppSimParams.*;
import java.awt.Color;

public class ppBall extends Thread {

	//Instance variables
	private double Xinit; //Initial position of ball - X
	private double Yinit; //Initial position of ball - Y
	private double Vo; //Initial velocity (Magnitude)
	private double theta; //Initial direction
	private double loss; //Energy loss on collision
	private Color color; //Color of ball
	private GraphicsProgram GProgram; //Instance of ppSim class (this)
	GOval myBall; //Graphics object representing ball
	ppTable myTable; 
	ppPaddle RPaddle;

	private GPoint p; 
	private double ScrX;
	private double ScrY;
	
	/**
	* The constructor for the ppBall class copies parameters to instance variables, initializes a GPoint object used to 
	* represent the trailing points of the ball, initializes a GOval object used to represent the ping-pong ball, 
	* and adds it to the display.
	*
	* @param Xinit - Starting position of the ball X (meters)
	* @param Yinit - Starting position of the ball Y (meters)
	* @param Vo - Initial velocity (m/s)
	* @param theta - Initial angle to the horizontal (degrees)
	* @param loss - Loss on collision ([0,1])
	* @param color - Ball color
	* @param myTable - A reference to the ppTable class which creates ground and wall lines, and has the W2S and S2W methods
	* @param GProgram - A reference to the ppSim class used to manage the display
	* 
	* @author F.P. Ferrie
	 * Credit: F2021-Assignment2-Handout
	*/
	public ppBall(double Xinit, double Yinit, double Vo, double theta, double loss, Color color, ppTable myTable, GraphicsProgram GProgram) {
		this.Xinit=Xinit;
		this.Yinit=Yinit;
		this.Vo=Vo;
		this.theta=theta;
		this.loss=loss;
		this.color=color;
		this.myTable = myTable;
		this.GProgram=GProgram;
		
		p = myTable.W2S(new GPoint(Xinit,Yinit+bSize)); 
		ScrX = p.getX();	//Convert simulation to screen coordinates for x-coordinates
		ScrY = p.getY(); //Convert simulation to screen coordinates for y-coordinates
		
		this.myBall = new GOval(ScrX-bSize*Xs, ScrY, 2*bSize*Xs, 2*bSize*Ys);  //Parameters are center of ball(X-bSize, Y+bSize) respectively, and diameter of ball
		myBall.setColor(this.color);
		myBall.setFilled(true);
		GProgram.add(myBall);
	}
	
	/**
	 * A start message is sent to each instance of the ppBall class
	 * 
	 * The simulation amounts to computing the position and velocity of the ball according
	 * to the above equations for increasing values of time. These expressions
	 * are relative to the starting point or the last collision, so it is necessary to keep
	 * track of the offsets to absolute positions, Xo and Yo.
	 *
	 * Every time a collision occurs, the simulation is reset (time = 0), new starting
	 * conditions determined, and the simulation run forward.
	 *
	 *@author F.P. Ferrie
	 *Credit: F2021-Assignment1 Solution
	 */
	public void run() {
		
		//Initialize simulation parameters
		double time = 0; //time (reset at each interval)
		double Xo = Xinit; //Initial X offset (ball touching wall)
		double Yo = Yinit; //Initial Y offset
		double Vt = bMass*g/(4*Pi*bSize*bSize*k); //Terminal velocity
		double Vox = Vo*Math.cos(theta*Pi/180); //Initial velocity components in X
		double Voy = Vo*Math.sin(theta*Pi/180); //Initial velocity components in Y
		double KEx = ETHR, KEy = ETHR, PE = ETHR; //Kinetic energy in X and Y directions; Potential energy
		double X, Y, Vx, Vy; //X and Y position and velocities
		double totalTime = 0; //Total time passed since the execution of the program till ball loses energy 

		boolean running = true;
		if (MESG) System.out.printf("\t\t\t\t Balll Position and Velocity\n");
		
		//Main simulation loop
		while(running) {
			
			//Update relative position and velocities
			X = Vox*Vt/g*(1-Math.exp(-g*time/Vt));
			Y = Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time;
			Vx = Vox*Math.exp(-g*time/Vt);
			Vy = (Voy+Vt)*Math.exp(-g*time/Vt)-Vt;

			//Ground collisions; When ball hits the ground Vy<0	or Yo+Y<=bSize; Program halts if ball runs out of energy
 			if (Vy<0 && (Yo+Y)<=bSize) {
  				
				//Recalculate energies; only KE exists when the ball hits the ground
				KEx=0.5*bMass*Vx*Vx*(1-loss);
 				KEy=0.5*bMass*Vy*Vy*(1-loss);
 				PE=0;
				
				//Recalculate new Vox and Voy as ball bounces upward
 				Vox=Math.sqrt(2*KEx/bMass);
 				Voy=Math.sqrt(2*KEy/bMass);
				if (Vx<0) Vox=-Vox;	//Ball continues traveling in the initial direction of motion
				
				//Reinitialize time and motion parameters to reflect a collision
				time=0;	 //Time is reset at every collision
				Xo+=X;	 //Accumulate distance between collisions
				Yo=bSize;	//Absolute position of the ball on the ground
				X=0;	Y=0; //Reset X and Y values for next loop
				
				if((KEx+KEy+PE)<ETHR) running=false;	//Running condition i.e. when ball has no energy left
			}
 			
 			//Paddle collisions; Program halts if ball goes past the paddle
 			if (Vx>0 && (Xo+X)>=RPaddle.getP().getX() - ppPaddleW/2 - bSize) {
 				if(RPaddle.contact(X+Xo, Y+Yo)) {
 					KEx=0.5*bMass*Vx*Vx*(1-loss);
 	 				KEy=0.5*bMass*Vy*Vy*(1-loss);
 	 				PE=bMass*g*(Y+Yo);
 					
 	 				Vox=-Math.sqrt(2*KEx/bMass); //After collision with the paddle, Vox is negative so ball goes in leftward direction
 					Voy=Math.sqrt(2*KEy/bMass);

 					Vox=Vox*ppPaddleXgain; //Scale X component of velocity 
 					Voy=Voy*ppPaddleYgain*RPaddle.getSgnVy(); //Scale Y + same direction as paddle
 					
 					//A3-5 Modification
 					//Voy=Voy*ppPaddleYgain*RPaddle.getV().getY(); // Scale Y + same dir. as paddle
 					
 					time=0;	
 					Xo=RPaddle.getP().getX() - ppPaddleW/2 - bSize;
 					Yo+=Y;
 					X=0;	Y=0;
 				} else running = false;
 			}
				
			//Left wall collisions
			if (Vx<0 && (Xo+X)<=XwallL+bSize) {
				
				KEx=0.5*bMass*Vx*Vx*(1-loss);
 				KEy=0.5*bMass*Vy*Vy*(1-loss);		
				PE=bMass*g*(Y+Yo);
	
				Vox=Math.sqrt(2*KEx/bMass); //Vox is positive
				Voy=Math.sqrt(2*KEy/bMass);
				if (Vy<0) Voy =-Voy; 
				
				time=0;	
				Xo=XwallL+bSize;
				Yo+=Y;
				X=0;	Y=0;
 			}
	
			//Get current screen coordinates
			p = myTable.W2S(new GPoint(Xo+X-bSize,Yo+Y+bSize)); 
			ScrX = p.getX();
			ScrY = p.getY();
			this.myBall.setLocation(ScrX, ScrY); //Move ball to new position
			trace(ScrX+bSize*Xs, ScrY+bSize*Ys); //Create trailing dots to show motion of the ball on the screen

			//Debugging tool; Displays program variables at every iteration of the loop
			if (MESG) {
				System.out.printf("t: %.2f\t\tX: %.2f\t\tY: %.2f\t\tVx: %.2f\t\tVy: %.2f\n", totalTime,Xo+X,Yo+Y,Vx,Vy);	//Display current values
			}
			
			//Update time variables
			time += TICK;
			totalTime += TICK;
			
			//Pause display
			GProgram.pause(TICK*TSCALE);
		}
	}
		
	/**
	 * Method to plot a tracing points on the screen
	 * @param X - X location of point (world coordinates)
	 * @param Y - Y location of point (world coordinates)
	 * @author F.P. Ferrie
	 * Credit: F2021-Assignment1 Solution
	 */
	private void trace(double ScrX, double ScrY) {
		GOval point = new GOval(ScrX, ScrY, PD, PD); //x, y, width, length
		point.setColor(Color.BLACK);
		point.setFilled(true);
		GProgram.add(point);		
	}
	
	/**
	 * Method sets the parameter RPaddle to the instance variable RPaddle
	 * @param myPaddle - Paddle object
	 * @author F.P. Ferrie
	 * Credit: F2021-Assignment 3 Handout
	 */
	public void setRightPaddle(ppPaddle myPaddle) {
		this.RPaddle = myPaddle;
	}
}
