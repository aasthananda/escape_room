/*
* Problem 1: Escape Room
*
* V1.0
* 10/10/2019
* Copyright(c) 2019 PLTW to present. All rights reserved
*/
import java.util.Scanner;


/**
* Create an escape room game where the player must navigate
* to the other side of the screen in the fewest steps, while
* avoiding obstacles and collecting prizes.
*/
public class EscapeRoom
{
 public static void main(String[] args)
 {     
   // welcome message
   System.out.println("Welcome to EscapeRoom!");
   System.out.println("Get to the other side of the room, avoiding walls and invisible traps,");
   System.out.println("pick up all the prizes.\n");
  
   GameGUI game = new GameGUI();
   game.createBoard();


   // size of move
   int m = 60;
   // individual player moves
   int px = 0;
   int py = 0;
  
   int score = 0;


   Scanner in = new Scanner(System.in);
   String[] validCommands = { "right", "left", "up", "down", "r", "l", "u", "d",
   "jump", "jr", "jumpleft", "jl", "jumpup", "ju", "jumpdown", "jd",
   "pickup", "p", "quit", "q", "replay", "help", "?"};
    // set up game
   boolean play = true;
   while (play)
   {
       System.out.print("\nEnter command > ");
       String cmd = UserInput.getValidInput(validCommands);


       px = 0;
       py = 0;


       if (cmd.equals("right") || cmd.equals("r")) {
           px = m;
       }
       else if (cmd.equals("left") || cmd.equals("l")) {
           px = -m;
       }
       else if (cmd.equals("up") || cmd.equals("u")) {
           py = -m;
       }
       else if (cmd.equals("down") || cmd.equals("d")) {
           py = m;
       }

      //jump
       else if (cmd.equals("jump") || cmd.equals("jr")) {
           px = m * 2;
       }
       else if (cmd.equals("jumpleft") || cmd.equals("jl")) {
           px = -m * 2;
       }
       else if (cmd.equals("jumpup") || cmd.equals("ju")) {
           py = -m * 2;
       }
       else if (cmd.equals("jumpdown") || cmd.equals("jd")) {
           py = m * 2;
       }


       // replay
       else if (cmd.equals("replay")) {
           score += game.replay();
           continue;
       }


       // help
       else if (cmd.equals("help") || cmd.equals("?")) {
           System.out.println("""
Commands:
right / r     - move right
left / l      - move left
up / u        - move up
down / d      - move down
jump/jr       - jump right
jumpleft/jl   - jump left
jumpup/ju     - jump up
jumpdown/jd   - jump down
pickup/p      - pick up prize
replay        - restart the board
quit/q        - end the game
help/?        - show commands
""");
           continue;
       }


       // --- QUIT GAME ---
       else if (cmd.equals("quit") || cmd.equals("q")) {
           play = false;
           continue;
       }


       // --- MOVE THE PLAYER ---
       int moveScore = game.movePlayer(px, py);


       // Only proceed if the move is valid (not off-grid or blocked by wall)
       if (moveScore >= 0) {
           // --- AUTOMATIC TRAP CHECK ---
           if (game.isTrap(0, 0)) { // current location
               score += game.springTrap(0, 0);
           }


           // --- AUTOMATIC PRIZE PICKUP ---
           score += game.pickupPrize();
       } else {
           // If move was invalid, deduct penalty
           score += moveScore;
       }
   }


   score += game.endGame();


   System.out.println("Final Score = " + score);
   System.out.println("Total Steps = " + game.getSteps());
 }
}
