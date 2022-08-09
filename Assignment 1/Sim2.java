import java.awt.Color;

import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

/**
 * A simple Java program to simulate equations of motion for a ping-pong ball
 * executing a parabolic trajectory from launch with velocity Vo and angle theta.
 * This version animates the ball on the graphics display
 * 
 * @author ferrie
 *
 */
public class Sim2 extends GraphicsProgram {
	
	// Program Parameters
	//
	// All constants used in the program should be defined as parameters.
	// This eliminates error and makes the program more maintainable.
	
	// Screen Dimensions
	
	private static final int WIDTH = 1280;							// n.b. screen coordinates
    private static final int HEIGHT = 600;
    private static final int OFFSET = 200;	
	
	// World and Screen Parameters
	
    private static final double Xmin = 0.0;							// Minimum value of X (pp table)
    private static final double Xmax = 2.74;						// Maximum value of X
    private static final double Ymin = 0.0;							// Minimum value of Y
    private static final double Ymax = 1.52;						// Maximum value of Y (height above table)
    private static final int xmin = 0;								// Minimum value of x
    private static final int xmax = WIDTH;							// Maximum value of x
    private static final int ymin = 0;								// Minimum value of y
    private static final int ymax = HEIGHT;							// Maximum value of y
    private static final double Xs = (xmax-xmin)/(Xmax-Xmin);		// Scale factor X
    private static final double Ys = (ymax-ymin)/(Ymax-Ymin);		// Scale factor Y	    
    private static final double PD = 1;								// Trace point diameter

	// Simulation Parameters
    
	public static final double g = 9.8;	          // gravitational constant
	public static final double k = 0.1316;        // air friction
	public static final double Pi = 3.1416;       // Pi to 4 places
	public static final double bSize = 0.02;      // Radius of ball (m)
	public static final double bMass = 0.0027;    // Mass of ball (kg)
	public static final double Xinit = 0.0;	      // Initial X position of ball
	public static final double Yinit = Ymax/2;    // Initial Y position of ball
	
	public static final double Vdef = 3.0;		  // Default velocity (m/s)
	public static final double Tdef = 30.0;		  // Default angle (degrees)
	
	public static final int SLEEP = 10;		  	  // Delay time in milliseconds
	public static final double TICK=SLEEP/1000.0; // Clock increment at each iteration.
	

	/**
	 * The entry point for a class that extends GraphicsProgram is run().
	 */
	
	public void run() {
		
		// Our program consists of a single loop that increments time until the
		// ball hits the ground.  At each iteration new values for position and
		// velocity are calculated for the new value of time and the position of the
		// ball updated on the screen.
		
		// Graphics Initialization: set window size, draw floor, create ball and place
		// at initial location.
		
		this.resize(WIDTH,HEIGHT+OFFSET);    // initialize window size
		
		// Create the ground plane
		
    	GRect gPlane = new GRect(0,HEIGHT,WIDTH+OFFSET,3);	// A thick line HEIGHT pixels down from the top
    	gPlane.setColor(Color.BLACK);
    	gPlane.setFilled(true);
    	add(gPlane);
    	
    	// Create the ball
    	
    	GPoint p = W2S(new GPoint(Xinit,Yinit));		
    	double ScrX = p.getX();						// Convert simulation to screen coordinates
    	double ScrY = p.getY();			
    	GOval myBall = new GOval(ScrX,ScrY,2*bSize*Xs,2*bSize*Ys);
    	myBall.setColor(Color.RED);
    	myBall.setFilled(true);
    	add(myBall);
    	pause(1000);								// So we can see the starting point of the ball
	
		// Get inputs from user
		
		double Vo = readDouble("Enter initial velocity: ");			
		double theta = readDouble("Enter launch angle: ");
	
		// Initialize program variables
		
		double Xo = Xinit;							// Set initial X position
		double Yo = Yinit;							// Set initial Y position
		double time = 0;							// Time starts at 0 and counts up
		double Vt = bMass*g / (4*Pi*bSize*bSize*k); // Terminal velocity
		double Vox=Vo*Math.cos(theta*Pi/180);		// X component of velocity
		double Voy=Vo*Math.sin(theta*Pi/180);		// Y component of velocity
		
		// Simulation loop.  Calculate position and velocity, print, increment
		// time.  Do this until ball hits the ground.
		
		boolean falling = true;						// Initial state = falling.
		
		// Important - X and Y are ***relative*** to the initial starting position Xo,Yo.
		// So the absolute position is Xabs = X+Xo and Yabs = Y+Yo.
		// Also - print out a header line for the displayed values.
		
		System.out.printf("\t\t\t Ball Position and Velocity\n");
		
		while (falling) {
			double X = Vox*Vt/g*(1-Math.exp(-g*time/Vt));				// Update relative position
			double Y = Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time;
    		double Vx = Vox*Math.exp(-g*time/Vt);						// Update velocity
    		double Vy = (Voy+Vt)*Math.exp(-g*time/Vt)-Vt;
    		
    	// Display current values (1 time/second)
    		
    		System.out.printf("t: %.2f\t\t X: %.2f\t Y: %.2f\t Vx: %.2f\t Vy: %.2f\n",
					time,X+Xo,Y+Yo,Vx,Vy);
    		
    		pause(SLEEP);										// Pause program for SLEEP mS
    		
    	// Check to see if we hit the ground yet.  When the ball hits the ground, the height of the center
    	// is the radius of the ball.
    		
    		if (Y+Yo < bSize) falling = false;
    		
    	// Update the position of the ball.  Plot a tick mark at current location.
    		
    		p = W2S(new GPoint(Xo+X-bSize,Yo+Y+bSize));		// Get current position in screen coordinates
    		ScrX = p.getX();
    		ScrY = p.getY();
    		myBall.setLocation(ScrX,ScrY);
    		trace(ScrX,ScrY);

    		
    		time += TICK;
    		
		}
	}
	

	/***
     * Method to convert from world to screen coordinates.
     * @param P a point object in world coordinates
     * @return p the corresponding point object in screen coordinates
     */
    
    GPoint W2S (GPoint P) {
    	return new GPoint((P.getX()-Xmin)*Xs,ymax-(P.getY()-Ymin)*Ys);
    }
    
    /***
     * A simple method to plot a dot at the current location in screen coordinates
     * @param scrX
     * @param scrY
     */
    
    private void trace(double ScrX, double ScrY) {
		GOval pt = new GOval(ScrX,ScrY,PD,PD);
		pt.setColor(Color.BLACK);
		pt.setFilled(true);
		add(pt);	
	}


}
