package ppPackage;

import static ppPackage.ppSimParams.*;
import acm.graphics.GPoint;
import acm.graphics.GRect;
import java.awt.Color;
import acm.program.GraphicsProgram;

public class ppPaddle extends Thread {
	
	//Instance variables
	double X, Y, Vx, Vy; //Paddle X and Y positions, and X and Y velocities respectively
	GRect myPaddle; //GRect implements paddle
	Color myColor; //Color of ball
	ppTable myTable; //Link to ppTable instance
	GraphicsProgram GProgram; //Link to display instance
	ppScoreboard myScore;
	
	/**
	 * The constructor for the ppPaddle class copies parameters to instance variables. 
	 * Initializes variables used to store the upperLeft coordinates of the paddle instance and assigns it to the point object (in simulation coordinates).
	 * Initializes a GRect object used to represent the paddle object and adds it to the display.
	 * @param X - X-position of the center of the paddle (world coordinates)
	 * @param Y - Y-position of the center of the paddle (world coordinates)
	 * @param myTable- A reference to the ppTable class which creates ground and wall lines, and has the W2S and S2W methods
	 * @param GProgram - A reference to the ppSim class used to manage the display
	 */
	public ppPaddle (double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram, ppScoreboard myScore) {
		this.X = X;
		this.Y = Y;
		this.myColor = myColor;
		this.myTable = myTable;
		this.GProgram = GProgram;
		this.myScore = myScore;
		
		this.Vx = 0;
		this.Vy = 0;
		
		// Create screen display for paddle; upperLeftX and upperLeftY are in world coordinates
		double upperLeftX = X - ppPaddleW/2;
		double upperLeftY = Y + ppPaddleH/2;
		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));  //p is in screen coordinates
		
		double ScrX = p.getX(); 
		double ScrY = p.getY();
		myPaddle = new GRect(ScrX, ScrY, ppPaddleW*Xs, ppPaddleH*Ys);
		myPaddle.setFilled(true);
		myPaddle.setColor(myColor);
		GProgram.add(myPaddle);
	}
	
	/**
	 * The ppPaddle run() continuously updates the paddle velocity based on the 
	 * user's mouse movements updating the paddle position and velocity.
	 */
	public void run() {
		double lastX = X;
		double lastY = Y;
		
		while (true) {
			Vx=(X-lastX)/TICK; 
			Vy=(Y-lastY)/TICK; 
			lastX=X;
			lastY=Y;
			GProgram.pause(myScore.getTimeDelay()); //Time (ms)
		}
	}
	
	/**
	 * Method that checks if true if ppBall touches ppPaddle 
	 * @param Sx - X-coordinate of contacting surface
	 * @param Sy - Y-coordinate of contacting surface
	 * @return true (if ppBall object touches ppPaddle object) 
	 */
	public boolean contact (double Sx, double Sy) {
		return((Sy>=Y - ppPaddleH/2) && (Sy<=Y+ppPaddleH/2));
	}
	
	/**
	 * Method returns paddle location (X,Y)
	 * @return new GPoint object containing the paddle location 
	 */
	public GPoint getP() {
		return new GPoint(X, Y);
	}
	
	/**
	 * Method sets and moves the paddle to (X,Y)
	 * The X and Y instance variables are updated (in world coordinates) based on the values in the GPoint parameter
	 * The coordinates for the upper-left corner of the paddle is computed and updated in the GPoint p object
	 * The screen coordinates are extracted from the GPoint p object and used to set the new location of the paddle object
	 * @param P - GPoint object that contains X and Y position values of the paddle object
	 */
	public void setP(GPoint P) {
		this.X = P.getX();
		this.Y = P.getY();
		
		double upperLeftX = X - ppPaddleW/2;
		double upperLeftY = Y + ppPaddleH/2;
		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));
				
		double ScrX = p.getX();
		double ScrY = p.getY();
		myPaddle.setLocation(ScrX, ScrY);
	}

	/**
	 * Method returns paddle velocity (Vx,Vy)
	 * @return GPoint object containing the Vx and Vy
	 */
	public GPoint getV() {
		return new GPoint(Vx, Vy);
	}
	
	/**
	 * Method returns the sign of the Y-velocity of the paddle
	 * @return -1 (if Vy is downward)
	 */
	public double getSgnVy() {
		if(Vy<0) return -1.0;
		else return 1.0; //if Vy>0
	}
}