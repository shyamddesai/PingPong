package ppPackage;

import static ppPackage.ppSimParams.*;
import acm.program.GraphicsProgram;

import java.awt.Color;

@SuppressWarnings("serial")
public class ppSim extends GraphicsProgram {
	
	//Main entry (starting) point of our program
	public static void main(String[] args) {
		new ppSim().start(args);
	}
	
	//Entry point for the GraphicsProgram class; No explicit constructor
	public void run() { 
		
		//Setting a fixed screen resolution; The (int)(Xwall*Xs) addition to the first parameters makes the window look symmetric
		this.resize(ppSimParams.WIDTH+(int)(XwallL*Xs), ppSimParams.HEIGHT+OFFSET);

		//Create ground line and walls
		@SuppressWarnings("unused")
		ppTable myTable = new ppTable(this);

		//Prompt user for input (Vo, theta, loss)
		double Vo = readDouble("Enter initial velocity [0,100]: "); //double Vo = 9;
		double theta = readDouble("Enter launch angle [-90, 90]: "); //double theta = 15;
		double loss = readDouble("Enter energy loss parameter [0,1]: "); //double loss = 0.2;
		
		//Create instances of a ball
		ppBall myBall = new ppBall(Xinit, Yinit, Vo, theta, loss, Color.RED, this); //Launch ball from the left wall
		ppBall myBall2 = new ppBall(XwallR, Yinit, Vo, theta, loss, Color.BLUE, this); //Launch ball from the right wall
		
/*		
		RandomGenerator rgen = new RandomGenerator();
		System.out.println(rgen.nextDouble());
		for(int i=0; i<100; i++) {
			new ppBall(Xinit, rgen.nextDouble(100, HEIGHT/2), rgen.nextDouble(0, 15), rgen.nextDouble(-30,30), 
					rgen.nextDouble(0, 1), rgen.nextColor(), this).start();
		}
*/		
		//Start simulation
		myBall.start();
		myBall2.start();		
	}
}
