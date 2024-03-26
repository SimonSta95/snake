import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, KeyListener{
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    int WIDTH;
    int HEIGHT;
    int TILESIZE = 25;

    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food
    Tile food;
    Random random;

    //logic
    Timer gameLoop;
    int velX;
    int velY;
    boolean gameOver = false;

    //Game instance that will fill the window
    Game(int gameWidth, int gameHeight) {
        this.WIDTH = gameWidth;
        this.HEIGHT = gameHeight;
        setPreferredSize(new Dimension(this.WIDTH, this.HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //spawn snake head in the middle and make it move down by default
        snakeHead = new Tile(12, 12);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        spawnFood();

        velX = 0;
        velY = 1;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        //draw grid
        for (int i = 0; i < WIDTH/TILESIZE; i++) {
           g.drawLine(i * TILESIZE, 0, i * TILESIZE, HEIGHT);
           g.drawLine(0, i * TILESIZE, WIDTH, i * TILESIZE);
        }

        //draw food
        g.setColor(Color.red);
        g.fillRect(food.x * TILESIZE, food.y * TILESIZE, TILESIZE, TILESIZE);


        //draw snake head
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * TILESIZE, snakeHead.y * TILESIZE, TILESIZE, TILESIZE);


        //draw snake body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * TILESIZE, snakePart.y * TILESIZE, TILESIZE, TILESIZE);
            g.fill3DRect(snakePart.x * TILESIZE, snakePart.y * TILESIZE, TILESIZE, TILESIZE, true);
        }

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), TILESIZE - 16, TILESIZE);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), TILESIZE - 16, TILESIZE);
        }
    }

    public void spawnFood() {
        food.x = random.nextInt(WIDTH/TILESIZE);
        food.y = random.nextInt(HEIGHT/TILESIZE);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        //consume food
        if(collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            spawnFood();
        }

        //snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if(i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //snake head
        snakeHead.x += velX;
        snakeHead.y += velY;

        //game over 
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            //collision with head
            if(collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x*TILESIZE < 0 || snakeHead.x*TILESIZE > WIDTH || 
            snakeHead.y*TILESIZE < 0 || snakeHead.y*TILESIZE > HEIGHT) {
            gameOver = true;
        }
    }

    //game loop
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP  && velY != 1) {
            velX = 0;
            velY = -1;
        } 
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velY != -1) {
            velX = 0;
            velY = 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velX != 1) {
            velX = -1;
            velY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velX != -1) {
            velX = 1;
            velY = 0;
        }
    }


    //NOT NEEDED
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
