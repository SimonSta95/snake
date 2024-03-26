import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int WIDTH = 600;
        int HEIGHT = WIDTH;
        
        //Create Window and define properties
        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create game instance and put it into the window
        Game snakeGame = new Game(WIDTH, HEIGHT);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}
