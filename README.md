# Ping Pong Simulation

This is an interactive Java-based ping pong game where you can play against a computer-controlled opponent. The game simulates realistic ball physics, paddle control, and scoring in a 2D environment.

## Overview
The game operates by:
1. Simulating a 2D ping pong game with realistic ball bouncing and collisions.
2. Allowing the player to control the left paddle while the right paddle is controlled by a computer agent.
3. Displaying the score in real-time, and allowing users to customize parameters such as game speed and paddle size.

This project includes the following source files:
- `ppSim.java`: The main class that runs the simulation, handling the game loop and user interactions.
- `ppPaddle.java`: Manages the player's paddle movement and interaction with the ball.
- `ppPaddleAgent.java`: Controls the computer paddle that competes against the player.
- `ppBall.java`: Simulates the ball's movement and interaction with the paddles and table.

![Bounce](https://user-images.githubusercontent.com/21160813/186038854-734e3125-2862-4863-89e2-c56bfff3e8fb.jpg)  
*(Screenshot of the Ping Pong simulation in action)*

---

## How to Compile and Run

1. **Compile the Program**:
   ```bash
   javac *.java
   ```

2. **Run the Simulation**:
   ```bash
   java ppSim
   ```

3. **Play the Game**:
   - Control the left paddle using your keyboard or mouse.
   - The computer will control the right paddle.

4. **View the Simulation**:
   - Watch the ball bounce between the paddles.
   - Adjust the lag and other simulation parameters using the on-screen controls.
