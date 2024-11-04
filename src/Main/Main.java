package Main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setTitle("Unworthy");

            GamePanel gamePanel = new GamePanel();
            window.add(gamePanel);

            window.pack();

            window.setLocationRelativeTo(null);
            window.setVisible(true);

            gamePanel.setupGame();
            gamePanel.startGameThread();
    }
}