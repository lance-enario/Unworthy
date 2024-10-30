package Main;

import Entity.Player;
import Entity.Entity;
import Tiles.TileManager;
import objects.superObject;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16
    final int scale = 4; //giilisan nako to 4 from 8 try lang
    final int playerScale = 6;

    public final int tileSize = originalTileSize * scale; // 48x48
    public final int playerSize = originalTileSize * playerScale;
    public int maxScreenCol = 16;
    public int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol; // 768 px
    public int screenHeight = tileSize * maxScreenRow; // 576 px

    // WORLD SETTINGS
    public  final  int  maxWorldCol = 50;
    public  final  int maxWorldRow = 50;

    // FPS of game
    int FPS = 60;

    // SYSTEM
    public KeyHandler keyH = new KeyHandler(this);
    public TileManager tileM = new TileManager(this);
    public UI ui = new UI(this);
    Sound sound = new Sound();
    Thread gameThread;
    public EventHandler eHandler = new EventHandler(this);
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public CollisionChecker cChecker = new CollisionChecker(this);

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity[] ent = new Entity[10];
    public Entity[] npc = new Entity[10];

    public AssetSetter aSet = new AssetSetter(this);
    public superObject obj [] = new superObject[10];

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }


    public void setupGame(){
        aSet.setObj();
        aSet.setNPC();
       playMusic(0);
        gameState = titleState;
    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {

        double drawInterval = 1000000000.0/FPS; // 0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if (delta >= 1){
                update(); // keeps track of key presses during gameplay and updates values
                repaint(); // draws changes based on updated variable on screen
                delta--;
            }

        }
    }


    public void update(){

        if (gameState == playState){
            //Player
            player.update();
            //NPC
            for(int i =0; i < npc.length; i++){
                if(npc[i] != null){
                    npc[i].update();
                }
            }
        }
        if (gameState == pauseState){
        }
    }


    public void paintComponent(Graphics g){
        //Toolkit.getDefaultToolkit().sync();
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //tile screen
        if(gameState == titleState){
            ui.draw(g2);
        }
        //others
        else{
            //draw map
            tileM.draw(g2);

            //draw object
            for (int i = 0; i < obj.length; i++) {
                if(obj[i] != null){
                    obj[i].draw(g2, this);
                }
            }

            //draw NPC
            for (int i = 0; i < npc.length; i++) {
                if(npc[i] != null){
                    npc[i].draw2(g2);
                }
             }
        }

        //draw others
        player.draw(g2);
        ui.draw(g2);
        g2.dispose();
    }


    public void playMusic(int i){
        sound.setFile(i);
        sound.play();
        sound.loop();
    }


    public void stopMusic(){
         sound.stop();
    }


    public void playSE(int i){
         sound.setFile(i);
         sound.play();
    }

}
