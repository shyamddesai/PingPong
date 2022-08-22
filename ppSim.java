package ppPackage;

import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import static ppPackage.ppSimParams.*;
import java.awt.Color;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class ppSim extends GraphicsProgram {
	
	//Instance variables
	ppTable myTable;
	ppPaddle myPaddle;
	ppBall myBall;
	
	
	//Main entry (starting) point of our program
	public static void main(String[] args) {
		new ppSim().start(args);
	}
	
	//Entry point for the GraphicsProgram class; No explicit constructor
	public void run() { 
		
		//Setting a fixed screen resolution. The (int)(Xwall*Xs) addition to the first parameters makes the window look symmetric
		this.setSize(ppSimParams.WIDTH+(int)(XwallL*Xs), ppSimParams.HEIGHT+OFFSET);

		//Generate table
		this.myTable = new ppTable(this);
		
		//Generate paddle
		this.myPaddle = new ppPaddle(ppPaddleXinit, ppPaddleYinit, myTable, this);
		addMouseListeners();
		
		//Generate parameters for Yo, Vo, Theta, and Loss with the Random Generator
		RandomGenerator rgen = RandomGenerator.getInstance();
		rgen.setSeed(RSEED);
		
		Color iColor = Color.RED;
		double iYinit = rgen.nextDouble(YinitMIN,YinitMAX);
		double iLoss = rgen.nextDouble(EMIN,EMAX); 
		double iVel = rgen.nextDouble(VoMIN,VoMAX);
		double iTheta = rgen.nextDouble(ThetaMIN,ThetaMAX);
		
		//Create instances of a ball
		myBall = new ppBall(Xinit, iYinit, iVel, iTheta, iLoss, iColor, myTable, this); //Launch ball from the left wall
		myBall.setRightPaddle(myPaddle); //The existing paddle object is assigned to the paddle instance of myBall

		//Start simulation
		pause(STARTDELAY);
		myBall.start(); 
		myPaddle.start();
	}
	
	/**
	 * Method to move the paddle on the y-axis w.r.t. to mouse movement
	 * @param e -  MouseEvent object  
	 * @author F.P. Ferrie
	 *Credit: F2021 - Assignment 3 Handout
	 */
	public void mouseMoved(MouseEvent e) {
		//Convert mouse position to a point in Screen Coordinates
		GPoint Pm = myTable.S2W(new GPoint(e.getX(),e.getY())); 
		double PaddleX = myPaddle.getP().getX();
		double PaddleY = Pm.getY(); 
		myPaddle.setP(new GPoint(PaddleX,PaddleY));
	}
}
