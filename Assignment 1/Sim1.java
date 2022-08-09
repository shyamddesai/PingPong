/**
 * A simple Java program to simulate equations of motion for a ping-pong ball
 * executing a parabolic trajectory from launch with velocity Vo and angle theta.
 * 
 * @author ferrie
 *
 */
public class Sim1 {
	
	// Program Parameters
	//
	// All constants used in the program should be defined as parameters.
	// This eliminates error and makes the program more maintainable.
	
	public static final double HEIGHT = 100.0;    // max height (m)
	public static final double WIDTH = 180.0;     // max width (m)
	
	public static final double g = 9.8;	          // gravitational constant
	public static final double k = 0.1316;        // air friction
	public static final double Pi = 3.1416;       // Pi to 4 places
	public static final double bSize = 0.02;      // Radius of ball (m)
	public static final double bMass = 0.0027;    // Mass of ball (kg)
	public static final double Xinit = 0.0;	      // Initial X position of ball
	public static final double Yinit = HEIGHT/2;  // Initial Y position of ball
	
	public static final double Vdef = 3.0;		  // Default velocity (m/s)
	public static final double Tdef = 30.0;		  // Default angle (degrees)
	
	public static final int SLEEP = 100;		  // Delay time in milliseconds
	public static final double TICK=SLEEP/1000.0; // Clock increment at each iteration.
	
	/**
	 * The simplest Java program consists of a single class containing a single
	 * Java method named main().  The latter is the entry point for this program.
	 * @param args - You can ignore the method parameter args[] for now.
	 * @throws InterruptedException 
	 */
	
	public static void main (String args[]) throws InterruptedException {
		
		// Our program consists of a single loop that increments time until the
		// ball hits the ground.  At each iteration new values for position and
		// velocity are calculated for the new value of time.
		
		// Set inputs (normally read from user; use parameters here).
		
		double Vo = Vdef;				// Set initial velocity				
		double theta = Tdef;			// Set initial launch angle
		double Xo = Xinit;				// Set initial X position
		double Yo = Yinit;				// Set initial Y position
	
		// Initialize program variables
		
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
    		
    		Thread.sleep(SLEEP);										// Pause program for SLEEP mS
    		
    	// Check to see if we hit the ground yet.  When the ball hits the ground, the height of the center
    	// is the radius of the ball.
    		
    		if (Y+Yo < bSize) falling = false;
    		
    		time += TICK;
    		
		}
	}
}
