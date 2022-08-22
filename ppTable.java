package ppPackage;

import static ppPackage.ppSimParams.*;
import java.awt.Color;
import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class ppTable {
	
	//Instance Variables
	GraphicsProgram GProgram;

	//Constructor
	public ppTable(GraphicsProgram GProgram) {
		GProgram.add(makeGroundLine());
		this.GProgram = GProgram;
	}

	/**
	 * Method creates the ground plane and adds it to the Graphics Program
	 * @return groundLine 
	 */
	public GRect makeGroundLine() {
		GRect groundLine = new GRect(0, HEIGHT, WIDTH+OFFSET, 2);
		groundLine.setColor(Color.BLACK);
		groundLine.setFilled(true);
		return groundLine;
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
		
	/**
	 * Method to erase all the objects on the display (besides the JButtons) and draws a new ground line
	 */
	public void newScreen() {
		GProgram.removeAll();
		GProgram.add(makeGroundLine());
	}
 }