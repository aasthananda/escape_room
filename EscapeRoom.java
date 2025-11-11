import java.util.Scanner;

public class EscapeRoom
{
    public static void main(String[] args) 
    {      
        System.out.println("Welcome to the Jungle Temple Escape Room!");
        System.out.println("Navigate to the far right of the temple, avoid rocks,");
        System.out.println("springs traps, and collect all treasures along the way.\n");

        GameGUI game = new GameGUI();
        game.createBoard();

        int m = 60; 
        int score = 0;

        Scanner in = new Scanner(System.in);
        String[] validCommands = { "right", "left", "up", "down", "r", "l", "u", "d",
                                   "jump", "jr", "jumpleft", "jl", "jumpup", "ju", "jumpdown", "jd",
                                   "pickup", "p", "quit", "q", "replay", "help", "?"};

        boolean play = true;

        while (play)
        {
            System.out.print("Enter command: ");
            String cmd = UserInput.getValidInput(validCommands);

            switch(cmd)
            {
                case "right": case "r":
                    score += game.movePlayer(m, 0);
                    break;
                case "left": case "l":
                    score += game.movePlayer(-m, 0);
                    break;
                case "up": case "u":
                    score += game.movePlayer(0, -m);
                    break;
                case "down": case "d":
                    score += game.movePlayer(0, m);
                    break;
                case "jump": case "jr":
                    score += game.movePlayer(m * 2, 0);
                    break;
                case "jumpleft": case "jl":
                    score += game.movePlayer(-m * 2, 0);
                    break;
                case "jumpup": case "ju":
                    score += game.movePlayer(0, -m * 2);
                    break;
                case "jumpdown": case "jd":
                    score += game.movePlayer(0, m * 2);
                    break;
                case "pickup": case "p":
                    score += game.pickupPrize();
                    break;
                case "help": case "?":
                    System.out.println("Commands:");
                    System.out.println("right / r - move right");
                    System.out.println("left / l - move left");
                    System.out.println("up / u - move up");
                    System.out.println("down / d - move down");
                    System.out.println("jump / jr - jump 2 spaces right");
                    System.out.println("jumpleft / jl - jump 2 spaces left");
                    System.out.println("jumpup / ju - jump 2 spaces up");
                    System.out.println("jumpdown / jd - jump 2 spaces down");
                    System.out.println("pickup / p - pick up treasure");
                    System.out.println("replay - restart the board");
                    System.out.println("quit / q - end the game");
                    break;
                case "replay":
                    score += game.replay();
                    break;
                case "quit": case "q":
                    play = false;
                    break;
            }

            if (game.isTrap(m, 0) || game.isTrap(-m, 0) || game.isTrap(0, -m) || game.isTrap(0, m))
            {
                score += game.springTrap(m, 0);
                score += game.springTrap(-m, 0);
                score += game.springTrap(0, -m);
                score += game.springTrap(0, m);
            }
        }

        score += game.endGame();

        System.out.println("Final Score: " + score);
        System.out.println("Total Steps: " + game.getSteps());
    }
}
