package Main;

import javax.swing.JFrame;

public class Main {
        public static JFrame window;
        public static void main(String[] args) {
                window = new JFrame();
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setTitle("Unworthy");

                GamePanel gamePanel = new GamePanel();
                window.add(gamePanel);

                gamePanel.config.loadConfig();
                if (gamePanel.fullScreenOn) {
                        window.setUndecorated(true);
                }

                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);

                gamePanel.initBufferStrategy(); // Initialize the BufferStrategy

                gamePanel.setupGame();
                gamePanel.startGameThread();
        }

}