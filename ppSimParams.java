package ppPackage;

public class ppSimParams {

	// 1. Parameters defined in screen coordinates (pixels, acm coordinates)
	public static final int WIDTH = 1280; // n.b. screen coordinates
	public static final int HEIGHT = 600;
	public static final int OFFSET = 200;
	
	public static final double ppTableXlen = 2.74; // Length
	public static final double ppTableHgt = 1.52; // Ceiling
	public static final double XwallL = 0.05; // Position of left wall
	public static final double XwallR = 2.69; // Position of right wall

	//A2-3 adjustments
	//public static final double XwallL = 0.25;
	//public static final double XwallR = 2.4;
	
	// 3. Parameters defined in simulation coordinates (MKS, X-->range, Y-->height)
	public static final double g = 9.8; // MKS
	public static final double k = 0.1316; // Vt constant
	public static final double Pi = 3.1416;
	public static final double bSize = 0.02; // pp ball radius
	public static final double bMass = 0.0027; // pp ball mass
	public static final double SLEEP = 10; //Delay time (ms)
	public static final double TICK =  SLEEP/1000.0; // Clock tick duration (sec)
	public static final double ETHR = 0.001; // Minimum ball energy
	public static final double Xmin = 0.0; // Minimum value of X (pp table)
	public static final double Xmax = ppTableXlen; // Maximum value of X
	public static final double Ymin = 0.0; // Minimum value of Y
	public static final double Ymax = ppTableHgt; // Maximum value of Y
	public static final int xmin = 0; // Minimum value of x
	public static final int xmax = WIDTH; // Maximum value of x
	public static final int ymin = 0; // Minimum value of y
	public static final int ymax = HEIGHT; // Maximum value of y
	public static final double Xs = (xmax-xmin)/(Xmax-Xmin); // Scale factor X
	public static final double Ys = (ymax-ymin)/(Ymax-Ymin); // Scale factor Y
	public static final double Xinit = XwallL; // Initial ball location (X)
	public static final double Yinit = Ymax/2; // Initial ball location (Y)
	public static final double PD = 1; // Trace point diameter
	
	// 4. Miscellaneous
	public static final boolean TEST = false; // Print position & velocity data if true.
	
}
