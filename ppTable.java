package ppPackage;

import static ppPackage.ppSimParams.*;
import java.awt.Color;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class ppTable {

	//Constructor; Adds right, left, and ground lines to the GraphicsProgram using the makeLine() method
	public ppTable(GraphicsProgram GProgram) {
		GProgram.add(makeLine(0, HEIGHT, WIDTH+OFFSET, 2, Color.BLACK));	//Ground line starting at HEIGHT pixels from top to bottom
		GProgram.add(makeLine(XwallL*Xs, 0, 2, HEIGHT, Color.BLUE));	//Left wall line
		GProgram.add(makeLine(XwallR*Xs, 0, 2, HEIGHT, Color.RED));	//Right wall line
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
}
