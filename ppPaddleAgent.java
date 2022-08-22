package ppPackage;

import static ppPackage.ppSimParams.*;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import java.awt.Color;

@SuppressWarnings("unused")
public class ppPaddleAgent extends ppPaddle {
	
	//Instance variables
	ppBall myBall;
	
	public ppPaddleAgent(double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram, ppScoreboard myScore) {
		super(X, Y, myColor, myTable, GProgram, myScore); //Access the superclass constructor
	}
	
	/**
	 * The entry point to this program.
	 * 
	 * When we enter the while loop, it first read current Y-position of the ball, 
	 * then changes the current paddle position (using setP() method from the ppPaddle class).
	 * We retain the current X-position of the paddle (using the getter method from ppPaddle),
	 * but get a new Y-position of the paddle from the Y-position of the ball.
	 * 
	 * The agent's paddle continuously tracks and update paddle position relative to the position of the ball
	 */
	public void run() {
		int ballSkip = 0;
		while(true) {		
			if (ballSkip++ >= myScore.getLag()) { //Update paddle position
				double Y = myBall.getP().getY(); //Get ball Y-position
				this.setP(new GPoint(this.getP().getX(), Y)); //Set paddle position		
				ballSkip=0;
			}
			GProgram.pause(myScore.getTimeDelay()); //Time (ms)
		}
	}
	
/**
 * Method sets the myBall parameter to the myBall instance variable
 * @param myBall - A ppBall object
 */
	public void attachBall(ppBall myBall) {
		this.myBall = myBall;
	}
}