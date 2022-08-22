package ppPackage;

import static ppPackage.ppSimParams.*;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ppSim extends GraphicsProgram {
	
	//Instance variables
	ppTable myTable; //Current table instance
	ppPaddle RPaddle; //Current player paddle instance
	ppPaddleAgent LPaddle; //Current agent paddle instance
	ppBall myBall; //Current ball instance
	ppScoreboard myScore; //Current scoreboard instance
	
	JSlider TickSlider; //Slider for TICK variable
	JSlider AgentLag; //Slider for agent lag

	RandomGenerator rgen;
	JButton startButton;
	
	//Main entry (starting) point of our program
	public static void main(String[] args) {
		new ppSim().start(args);
	}
	
	/*
	 * Entry point of this program
	 */
	public void init() {
		this.setSize(ppSimParams.WIDTH+(int)((LPaddleXinit-ppPaddleW)*Xs), ppSimParams.HEIGHT+OFFSET); //Make screen symmetric
		this.setBackground(Color.pink.brighter());
		
		//Create Components
		startButton = new JButton("Start");
		myScore = new ppScoreboard("Agent", "Player");
		JButton newServeButton = new JButton("New Serve");
		traceButton = new JToggleButton("Trace");
		JButton clearButton = new JButton("Clear");
		JButton quitButton = new JButton("Quit");
		

		//Add Components to the screen
		add(myScore.getPanel(), NORTH);
		add(startButton, SOUTH);
		add(newServeButton, SOUTH);
		add(traceButton, SOUTH);
		add(clearButton, SOUTH);
		add(quitButton, SOUTH);
		
		add(new JLabel("-t"), SOUTH);
		add(myScore.getTimeSlider(), SOUTH);
		add(new JLabel("+t   "), SOUTH);
		add(new JLabel("-lag"), SOUTH);
		add(myScore.getLagSlider(), SOUTH);
		add(new JLabel("+lag"), SOUTH);
				
		addMouseListeners();
		addActionListeners();
				
		//Initialize Random Generator object
		rgen = RandomGenerator.getInstance();
		rgen.setSeed(RSEED);
		
		myTable = new ppTable(this);
		//myBall = newBall();
		//newGame();		
	}
	
	/**
	 * Method generates random parameters (Yo, Vo, Theta, Loss) and returns an instance of ppBall
	 * @return myBall - A ppBall object that's add to the screen with random parameters created by the Random Generator
	 */
	ppBall newBall() {
		Color iColor = Color.RED;
		double iYinit = rgen.nextDouble(YinitMIN,YinitMAX);
		double iLoss = rgen.nextDouble(EMIN,EMAX); 
		double iVel = rgen.nextDouble(VoMIN,VoMAX);
		double iTheta = rgen.nextDouble(ThetaMIN,ThetaMAX);
		
		//Create ball, paddle, and link to paddle
		myBall = new ppBall(Xinit, iYinit, iVel, iTheta, iLoss, iColor, myTable, this, myScore);
		
		return myBall;
	}
	
	/**
	 * Method removes all the objects (besides buttons) from the screen 
	 * and reassigns myBall to a new ppBall and adds new ground line for a new game
	 * @author F.P. Ferrie
	 * Credit: F2021-Assignment4 Handout
	 */
	public void newGame() {
		if (myBall != null) myBall.kill(); //Stop current game in play
		
		myTable.newScreen();
		myBall = newBall();
		
		RPaddle = new ppPaddle(ppPaddleXinit, ppPaddleYinit, Color.GREEN, myTable, this, myScore);
		LPaddle = new ppPaddleAgent(LPaddleXinit, LPaddleYinit, Color.BLUE, myTable, this, myScore);
		LPaddle.attachBall(myBall);
		
		//The existing paddle objects are assigned to the paddle instance of myBall
		myBall.setRightPaddle(RPaddle);
		myBall.setLeftPaddle(LPaddle);
		
		//Start simulation
		pause(STARTDELAY);
		myBall.start();
		LPaddle.start();
		RPaddle.start();
	}
	
	/**
	 * Method handles mouse event that moves the paddle on the y-axis
	 * @param e -  MouseEvent object  
	 * @author F.P. Ferrie
	 *Credit: F2021 - Assignment3 Handout
	 */
	public void mouseMoved(MouseEvent e) {
		if (myTable==null || RPaddle==null) return; //Avoid any null-pointer exceptions
		
		//Convert mouse position to a point in Screen Coordinates
		GPoint Pm = myTable.S2W(new GPoint(e.getX(),e.getY())); // Mouse position in world	coordinates
		double PaddleX = RPaddle.getP().getX(); // X-position of paddle unchanged
		double PaddleY = Pm.getY(); // Y-position of paddle is Y-position of mouse
		RPaddle.setP(new GPoint(PaddleX,PaddleY)); //Update paddle position
	}
	
	/**
	 * Method handles action event when a button is pressed, 
	 * either of the following actions are performed: new game begins, or program is terminated.
	 * @param e -  ActionEvent object  
	 */
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("Start")) {
			pause(1000);
			newGame();
		} else if(command.equals("New Serve")) newGame();
		else if(command.equals("Clear")) myScore.clearScore();
		else if(command.equals("Quit"))	System.exit(0);
	}
}