package ppPackage;

import static ppPackage.ppSimParams.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import acm.gui.IntField;
import acm.gui.TableLayout;
import java.awt.Color;

public class ppScoreboard {

	//Instance Variables
	private JPanel myPanel;
	private JLabel PlayerName;
	private JLabel AgentName;
	private IntField PlayerScore;
	private IntField AgentScore;
	private JSlider TimeSlider;
	private JSlider AgentLag;
	
	//Constructor
	public ppScoreboard(String playerName, String agentName) {
				
		//Initialize JComponents for the scoreboard
		myPanel = new JPanel();
		myPanel.setBackground(Color.ORANGE);
		
		PlayerName = new JLabel("   " + agentName + ": ");
		AgentName = new JLabel(playerName + ": ");
		
		PlayerScore = new IntField(0);
		AgentScore = new IntField(0);
		
		TimeSlider = new JSlider(1000, 8000, 1000);
		TimeSlider.setValue(3000); //Set default starting screen pause timer
		AgentLag = new JSlider(1, 7, 2);
		AgentLag.setValue(3); //Set default starting agent lag
		
		//Add JComponents to the GraphicsProgram
		myPanel.setLayout(new TableLayout(1, 4));
		myPanel.add(AgentName);
		myPanel.add(AgentScore);
		myPanel.add(PlayerName);
		myPanel.add(PlayerScore);
	}
	
	/**
	 * Method gets the reference for the JPanel
	 * @return myPanel
	 */
	public JPanel getPanel() {
		return myPanel;
	}
	
	/**
	 * Method gets the screen pause delay JSlider reference
	 * @return TimeSlider
	 */
	public JSlider getTimeSlider() {
		return TimeSlider;
	}
	
	/**
	 * Method gets the agent lag JSlider reference
	 * @return AgentLag
	 */
	public JSlider getLagSlider() {
		return AgentLag;
	}
	
	/**
	 * Method to increase the agent's score by one point
	 */
	public void IncrementAgentScore() {
		int currentScore = AgentScore.getValue();
		AgentScore.setValue(currentScore+1);
	}
	
	/**
	 * Method to increase the player's score by one point
	 */
	public void IncrementPlayerScore() {
		int currentScore = PlayerScore.getValue();
		PlayerScore.setValue(currentScore+1);
	}
	
	/**
	 * Method to set the score of both the player and agent to 0
	 */
	public void clearScore() {
		AgentScore.setValue(0);
		PlayerScore.setValue(0);
	}
	
	/**
	 * Method to get the screen pause time delay
	 * @return timeDelay - Similar to TICK*TSCALE to pause the screen but TSCALE modified by user's slider input
	 */
	public double getTimeDelay() {
		return TimeSlider.getValue()*TICK;
	}
	
	/**
	 * Method to get the agent lag value
	 * @return agentLag
	 */
	public double getLag() {
		return AgentLag.getValue();
	}
}