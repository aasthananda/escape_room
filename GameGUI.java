import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Random;
import java.util.ArrayList;

public class GameGUI extends JPanel implements ActionListener
{
    JFrame frame;
    Timer timer;
    int delay = 60;

    int w, h, s;
    int x, y;
    int steps;
    int goal;
    int scoreVal = 5;
    int wallVal = -1;
    int trapVal = -20;

    ArrayList<Rectangle> walls;
    ArrayList<Rectangle> traps;
    ArrayList<Ellipse2D.Double> prizes;

    Image player;
    Image rock;
    Image flag;

    boolean repaintNeeded;

    public GameGUI()
    {
        w = 10;
        h = 10;
        s = 60;
        x = s/2;
        y = s/2;
        steps = 0;
        goal = w*s + s/2;

        walls = new ArrayList<>();
        traps = new ArrayList<>();
        prizes = new ArrayList<>();

        player = new ImageIcon("small-duke.png").getImage();
        rock = new ImageIcon("rock.png").getImage();
        flag = new ImageIcon("flag.png").getImage();

        timer = new Timer(delay, this);
    }

    public void createBoard()
    {
        walls.clear();
        traps.clear();
        prizes.clear();

        Random rand = new Random();

        for (int i=1; i<=5; i++)
        {
            int wx = rand.nextInt(w)*s;
            int wy = rand.nextInt(h)*s;
            Rectangle r;
            if (rand.nextInt(2)==0)
                r = new Rectangle(wx+s-5, wy, 8, s);
            else
                r = new Rectangle(wx, wy+s-5, s, 8);
            walls.add(r);
        }

        for (int i=0; i<5; i++)
        {
            int tx = rand.nextInt(w)*s + 10;
            int ty = rand.nextInt(h)*s + 10;
            traps.add(new Rectangle(tx, ty, 40, 40));
        }

        for (int i=0; i<3; i++)
        {
            int px = rand.nextInt(w)*s + 20;
            int py = rand.nextInt(h)*s + 20;
            prizes.add(new Ellipse2D.Double(px, py, 20, 20));
        }

        x = s/2;
        y = s/2;
        steps = 0;

        frame = new JFrame("Escape Room");
        frame.setSize(w*s+100, h*s+100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);

        timer.start();
    }

    public int movePlayer(int dx, int dy)
    {
        int nx = x + dx;
        int ny = y + dy;

        for (Rectangle r : walls)
            if (r.contains(nx, ny))
                return wallVal;

        x = nx;
        y = ny;
        steps++;
        repaint();
        return 0;
    }

    public boolean isTrap(int dx, int dy)
    {
        double px = x + dx;
        double py = y + dy;

        for (Rectangle r : traps)
            if (r.getWidth()>0 && r.contains(px, py))
                return true;

        return false;
    }

    public int springTrap(int dx, int dy)
    {
        double px = x + dx;
        double py = y + dy;

        for (Rectangle r : traps)
        {
            if (r.getWidth()>0 && r.contains(px, py))
            {
                r.setSize(0,0);
                return trapVal;
            }
        }
        return -trapVal;
    }

    public int pickupPrize()
    {
        for (Ellipse2D.Double e : prizes)
        {
            if (e.width>0 && e.contains(x, y))
            {
                e.width = 0;
                e.height = 0;
                return scoreVal;
            }
        }
        return 0;
    }

    public int endGame()
    {
        if (x >= goal)
            return 100;
        return 0;
    }

    public int getSteps()
    {
        return steps;
    }

    public int replay()
    {
        int oldSteps = steps;
        createBoard();
        return -oldSteps;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (Rectangle r : walls)
            g2.drawImage(rock, r.x, r.y, r.width, r.height, null);

        for (Rectangle t : traps)
            if (t.width>0)
                g2.setColor(Color.WHITE);

        g2.setColor(Color.WHITE);
        for (Rectangle t : traps)
            if (t.width>0)
                g2.fill(t);

        g2.drawImage(player, x-20, y-20, 40, 40, null);
        g2.drawImage(flag, goal-40, s/2-20, 40, 40, null);

        g2.setColor(Color.YELLOW);
        for (Ellipse2D.Double e : prizes)
            if (e.width>0)
                g2.fill(e);
    }

    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }
}
