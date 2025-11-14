import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Random;
//initialization stuff
public class GameGUI extends JComponent
{
    static final long serialVersionUID = 141L;

    private static final int WIDTH = 510;
    private static final int HEIGHT = 360;
    private static final int SPACE_SIZE = 60;
    private static final int GRID_W = 8;
    private static final int GRID_H = 5;
    private static final int START_LOC_X = 15;
    private static final int START_LOC_Y = 15;

    int x = START_LOC_X;
    int y = START_LOC_Y;

    private Image bgImage;
    private Image player;
    private Image prizeImage;
    private Image rockImage;
    private Image dukeImage;

    private Point playerLoc;
    private int playerSteps;

    private int totalWalls;
    private Rectangle[] walls;
    private int totalPrizes;
    private Rectangle[] prizes;
    private int totalTraps;
    private Rectangle[] traps;

    private int prizeVal = 10;
    private int trapVal = 5;
    private int endVal = 10;
    private int offGridVal = 5;
    private int hitWallVal = 5;

    private JFrame frame;

    private int dukeX;
    private int dukeY;
    private int dukeWidth = 60;
    private int dukeHeight = 60;

    public GameGUI()
    {
        try { bgImage = ImageIO.read(new File("grid.png")); } 
        catch (Exception e) { System.err.println("Could not open file grid.png"); }

        try { prizeImage = ImageIO.read(new File("coin.png")); } 
        catch (Exception e) { System.err.println("Could not open file coin.png"); }

        try { player = ImageIO.read(new File("player.png")); } 
        catch (Exception e) { System.err.println("Could not open file player.png"); }

        try { rockImage = ImageIO.read(new File("rock.png")); } 
        catch (Exception e) { System.err.println("Could not open file rock.png"); }

        try { dukeImage = ImageIO.read(new File("dukeLarge.png")); } 
        catch (Exception e) { System.err.println("Could not open file dukeLarge.png"); }

        playerLoc = new Point(x, y);
//sets up file 
        frame = new JFrame();
        frame.setTitle("EscapeRoom");
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);
        frame.setResizable(false);
//sets the numerical limits that are called later on to prevent a map filled with one thing or the other
        totalWalls = 20;
        totalPrizes = 3;
        totalTraps = 5;
//generates duke placements 
        Random rand = new Random();
        dukeX = rand.nextInt(GRID_W) * SPACE_SIZE + 15;
        dukeY = rand.nextInt(GRID_H) * SPACE_SIZE + 15;
    }
//generates board
    public void createBoard()
    {
        traps = new Rectangle[totalTraps];
        createTraps();
        prizes = new Rectangle[totalPrizes];
        createPrizes();
        walls = new Rectangle[totalWalls];
        createWalls();
    }
//made to move the player, and warn the players if they do not make smart moves... .. 
    public int movePlayer(int incrx, int incry)
    {
        int newX = x + incrx;
        int newY = y + incry;

        playerSteps++;

        if ((newX < 0 || newX > WIDTH-SPACE_SIZE) || (newY < 0 || newY > HEIGHT-SPACE_SIZE))
        {
            System.out.println("OFF THE GRID!");
            return -offGridVal;
        }

        for (Rectangle r: walls)
        {
            int startX = (int) r.getX();
            int endX = startX + (int) r.getWidth();
            int startY = (int) r.getY();
            int endY = startY + (int) r.getHeight();

            if ((incrx > 0 && x <= startX && startX <= newX && y >= startY && y <= endY) ||
                (incrx < 0 && x >= startX && startX >= newX && y >= startY && y <= endY) ||
                (incry > 0 && y <= startY && startY <= newY && x >= startX && x <= endX) ||
                (incry < 0 && y >= startY && startY >= newY && x >= startX && x <= endX))
            {
                System.out.println("A WALL IS IN THE WAY");
                return -hitWallVal;
            }
        }
//increments score
        x += incrx;
        y += incry;
        repaint();
        return 0;
    }
//a boolean to check if the given rectangle is a trap, provides result to the user
    public boolean isTrap(int newx, int newy)
    {
        double px = playerLoc.getX() + newx;
        double py = playerLoc.getY() + newy;

        for (Rectangle r: traps)
        {
            if (r.getWidth() > 0 && r.contains(px, py))
            {
                System.out.println("A TRAP IS AHEAD");
                return true;
            }
        }

        return false;
    }

    public int springTrap(int newx, int newy)
    {
        double px = playerLoc.getX() + newx;
        double py = playerLoc.getY() + newy;
//if the player encounters a trap 
        for (Rectangle r: traps)
        {
            if (r.contains(px, py) && r.getWidth() > 0)
            {
                r.setSize(0,0);
                System.out.println("TRAP IS SPRUNG!");
                return trapVal;
            }
        }
//if the player uses the command without the trap 
        System.out.println("THERE IS NO TRAP HERE TO SPRING");
        return -trapVal;
    }

    public int pickupPrize()
    {
        double px = playerLoc.getX();
        double py = playerLoc.getY();
//if the player succesfully reaches a prize
        for (Rectangle p: prizes)
        {
            if (p.getWidth() > 0 && p.contains(px, py))
            {
                System.out.println("YOU PICKED UP A PRIZE!");
                p.setSize(0,0);
                repaint();
                return prizeVal;
            }
        }
//if the player fails to reach a prize 
        System.out.println("OOPS, NO PRIZE HERE");
        return -prizeVal;
    }
//sets quantity limits on each portion, and sets steps back to the start
    public int getSteps() { return playerSteps; }

    public void setPrizes(int p) { totalPrizes = p; }

    public void setTraps(int t) { totalTraps = t; }

    public void setWalls(int w) { totalWalls = w; }
//provides an option to replay the game 
    public int replay()
    {
        int win = playerAtEnd();

        for (Rectangle p: prizes) p.setSize(SPACE_SIZE/3, SPACE_SIZE/3);
        for (Rectangle t: traps) t.setSize(SPACE_SIZE/3, SPACE_SIZE/3);

        x = START_LOC_X;
        y = START_LOC_Y;
        playerSteps = 0;
        repaint();
        return win;
    }
//provides a pathway to call for the player quitting or winning 
    public int endGame()
    {
        int win = playerAtEnd();
        setVisible(false);
        frame.dispose();
        return win;
    }
//adds in background components 
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g.drawImage(bgImage, 0, 0, null);

        for (Rectangle t: traps)
        {
            g2.setPaint(Color.WHITE);
            g2.fill(t);
        }
//draws out the prize
        for (Rectangle p: prizes)
        {
            if (p.getWidth() > 0)
            {
                int px = (int)p.getX();
                int py = (int)p.getY();
                g.drawImage(prizeImage, px, py, null);
            }
        }
//draws out walls
        for (Rectangle r: walls)
        {
            if (rockImage != null)
            {
                g.drawImage(rockImage, (int)r.getX(), (int)r.getY(), r.width, r.height, null);
            }
            else
            {
                g2.setPaint(Color.BLACK);
                g2.fill(r);
            }
        }
//sets location and adds the Duke, how villanous 
        if (dukeImage != null)
        {
            g.drawImage(dukeImage, dukeX, dukeY, dukeWidth, dukeHeight, null);
        }

        g.drawImage(player, x, y, 40, 40, null);
        playerLoc.setLocation(x, y);
    }
//draws random prizes on the map, but limits their size
    private void createPrizes()
    {
        int s = SPACE_SIZE;
        Random rand = new Random();
        for (int i = 0; i < totalPrizes; i++)
        {
            int h = rand.nextInt(GRID_H);
            int w = rand.nextInt(GRID_W);
            Rectangle r = new Rectangle((w*s + 15), (h*s + 15), 15, 15);
            prizes[i] = r;
        }
    }
//draws random traps in the map, but limits their size
    private void createTraps()
    {
        int s = SPACE_SIZE;
        Random rand = new Random();
        for (int i = 0; i < totalTraps; i++)
        {
            int h = rand.nextInt(GRID_H);
            int w = rand.nextInt(GRID_W);
            Rectangle r = new Rectangle((w*s + 15), (h*s + 15), 15, 15);
            traps[i] = r;
        }
    }
//draws random walls in the map 
    private void createWalls()
    {
        int s = SPACE_SIZE;
        Random rand = new Random();
        for (int i = 0; i < totalWalls; i++)
        {
            int h = rand.nextInt(GRID_H);
            int w = rand.nextInt(GRID_W);
            Rectangle r;
            if (rand.nextInt(2) == 0)
            {
                r = new Rectangle((w*s + s - 5), h*s, 8, s);
            }
            else
            {
                r = new Rectangle(w*s, (h*s + s - 5), s, 8);
            }
            walls[i] = r;
        }
    }
//provides result depending on player quitting or maintaining their progress to the end 
    private int playerAtEnd()
    {
        double px = playerLoc.getX();
        if (px > (WIDTH - 2*SPACE_SIZE))
        {
            System.out.println("YOU MADE IT!");
            return endVal;
        }
        else
        {
            System.out.println("OOPS, YOU QUIT TOO SOON!");
            return -endVal;
        }
    }
}
