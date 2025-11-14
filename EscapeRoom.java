import java.util.Scanner;

public class EscapeRoom
{
    public static void main(String[] args)
    {
        System.out.println("Welcome to EscapeRoom!");
        System.out.println("Get to the other side of the room, avoiding walls and invisible traps,");
        System.out.println("pick up all the prizes.\n");
        System.out.println("Please type in one of these commands: right, left, up, down, r, l, u, d, jump, jr, jumpleft, jl, jumpup, ju, jumpdown, jd, pickup, p, quit, q, replay, help, ?");

        GameGUI game = new GameGUI();
        game.createBoard();

        int m = 60;
        int px = 0;
        int py = 0;
        int score = 0;

        Scanner in = new Scanner(System.in);
        String[] validCommands = {
                "right","left","up","down","r","l","u","d",
                "jump","jr","jumpleft","jl","jumpup","ju","jumpdown","jd",
                "pickup","p","quit","q","replay","help","?"
        };

        boolean play = true;

        while (play)
        {
            System.out.print("\nEnter command > ");
            String cmd = UserInput.getValidInput(validCommands);

            px = 0;
            py = 0;

            if (cmd.equals("right") || cmd.equals("r")) px = m;
            else if (cmd.equals("left") || cmd.equals("l")) px = -m;
            else if (cmd.equals("up") || cmd.equals("u")) py = -m;
            else if (cmd.equals("down") || cmd.equals("d")) py = m;
            else if (cmd.equals("jump") || cmd.equals("jr")) px = m*2;
            else if (cmd.equals("jumpleft") || cmd.equals("jl")) px = -m*2;
            else if (cmd.equals("jumpup") || cmd.equals("ju")) py = -m*2;
            else if (cmd.equals("jumpdown") || cmd.equals("jd")) py = m*2;
            else if (cmd.equals("replay")) {
                score += game.replay();
                continue;
            }
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
            else if (cmd.equals("quit") || cmd.equals("q")) {
                play = false;
                continue;
            }

            int moveScore = game.movePlayer(px, py);

            if (moveScore >= 0) {
                if (game.isTrap(0,0)) {
                    System.out.println("YOU STEPPED ON A TRAP — GAME OVER!");
                    play = false;
                    continue;
                }
                score += game.pickupPrize();
            } else {
                score += moveScore;
            }
        }

        score += game.endGame();
        System.out.println("Final Score = " + score);
        System.out.println("Total Steps = " + game.getSteps());
    }
}
