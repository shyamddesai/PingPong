package ppPackage;

import static ppPackage.ppSimParams.*;
import java.awt.Color;
import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class ppTable {

	//Constructor; Adds left and ground lines to the GraphicsProgram using the makeLine() method
	public ppTable(GraphicsProgram GProgram) {
		GProgram.add(makeLine(0, HEIGHT, WIDTH+OFFSET, 2, Color.BLACK));	//Ground line starting at HEIGHT pixels from top to bottom
		GProgram.add(makeLine(XwallL*Xs, 0, 2, HEIGHT, Color.BLUE));	//Left wall line
	}
	
	/**
	 * A method that takes input of common parameters to create the ground, and left and right walls
	 * @param X - Starting position of the line (pixels) on x-axis from left to right
	 * @param Y - Starting position of the line (pixels) on y-axis from top to bottom
	 * @param width - Thickness of the line (pixels) if vertical; Length of line (pixels) if horizontal
	 * @param height - Thickness of the line (pixels) if horizontal; Length of line (pixels) if vertical
	 * @param color - Color of the line
	 * @return Line - GRect object with the user's specified parameters; Ready to add to the GraphicsProgram
	 */
	private GRect makeLine(double X, double Y, int width, int height, Color color) {
		GRect Line = new GRect(X, Y, width, height);
		Line.setColor(color);
		Line.setFilled(true);		
		return Line;
	}
	
	/**
	 * Method to convert from world to screen coordinates.
	 * @param P - A point object in world coordinates
	 * @return p - The corresponding point object in screen coordinates
	 * @author F.P. Ferrie
	 * Credit: F2021-Assignment1 Solution
	 */
	public GPoint W2S (GPoint P) {
		double X = P.getX();
		double Y = P.getY();
		
		double x = (X-Xmin)*Xs;
		double y = ymax - (Y-Ymin)*Ys;
		
		return new GPoint(x,y);		
	}
	
	/**
	 * Method to convert from screen to world coordinates.
	 * @param P -  A point object in screen coordinates
	 * @return p - The corresponding point object in world coordinates
	 * @author K. Poulin 
	 *Credit: F2021 - Tutorial 6
	 */
	public GPoint S2W (GPoint P) {
		double ScrX = P.getX();
		double ScrY = P.getY();
		
		double Xw = ScrX/Xs + Xmin;
		double Yw = (ymax - ScrY)/Ys + Ymin;
		
		return new GPoint(Xw, Yw);
	}
}
