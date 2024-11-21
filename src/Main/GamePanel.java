package Main;

import AI.Pathfinder;
import Entity.Player;
import Entity.Entity;
import Entity.Warrior;
import Entity.Mage;
import Entity.Ranger;

import Tiles.TileManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16
    final int scale = 4; // 8 original value
    private int playerScale = 6;

    public final int tileSize = originalTileSize * scale; // 48x48
    public int playerSize = originalTileSize * playerScale;
    public int maxScreenCol = 24; // 16 initial value (tried for fullscreen)
    public int maxScreenRow = 13; // 12 initial value
    public int screenWidth = tileSize * maxScreenCol; // 768 px
    public int screenHeight = tileSize * maxScreenRow; // 576 px

    // WORLD SETTINGS
    public  int  maxWorldCol;
    public  int maxWorldRow;
    public boolean fullScreenOn = false;
    public final int maxMap = 5;
    public int currentMap = 0;

    //FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    // FPS of game
    int FPS = 60;

    // SYSTEM
    public KeyHandler keyH = new KeyHandler(this);
    public TileManager tileM = new TileManager(this);
    public UI ui = new UI(this);
    Sound sound = new Sound();
    Sound SE = new Sound();
    Thread gameThread;
    public EventHandler eHandler = new EventHandler(this);
    public AssetSetter aSet = new AssetSetter(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    Config config = new Config(this);
    public Pathfinder pFinder = new Pathfinder(this);


    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Player mage = new Mage(this, keyH);
    public Player warrior = new Warrior(this, keyH);
    public Player ranger = new Ranger(this, keyH);
    public Entity[][] npc = new Entity[maxMap][20];
    public Entity[][] obj = new Entity[maxMap][20];
    public Entity[][] monster = new Entity[maxMap][20];
    public Entity[][] signs = new Entity[maxMap][20];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();


    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionState = 5;
    public final int gameOverState = 6;
    public final int transitionState = 7;
    public String text;
    public final int buyState = 8;

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
        aSet.setMonster();
        gameState = titleState;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();

        if(fullScreenOn) {
            setFullScreen();
        }
    }
    public void restart(){
        player.setDefaultValues();
        player.setItems();
        aSet.setObj();
        aSet.setNPC();
        aSet.setMonster();
    }

    public void setFullScreen(){
        //GET LOCAL SCREEN DEVICE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        //GET FULL SCREEN WIDTH AND HEIGTH
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();

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
                drawToTempScreen(); // draw everything to the buffered image
                drawToScreen(); // draw the buffered image to the screen
                delta--;
            }

        }
    }


    public void update(){
        if (gameState == playState){

            //Player
            player.update();

            //NPC
            for(int i =0; i < npc[1].length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }
            for(int i =0; i < signs[1].length; i++) {
                if (signs[currentMap][i] != null) {
                    signs[currentMap][i].update();
                }
            }

            for(int i = 0; i < monster[1].length; i++){
                if(monster[currentMap][i] != null){
                    if(monster[currentMap][i].isAlive && !monster[currentMap][i].isDying){
                        monster[currentMap][i].update();
                    }
                    if(!monster[currentMap][i].isAlive){
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                    }
                }
            }

            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    if(projectileList.get(i).isAlive){
                        projectileList.get(i).update();
                    }
                    if(!projectileList.get(i).isAlive){
                        projectileList.remove(i);
                    }
                }
            }
        }

        if (gameState == pauseState){
        }
    }
    public void drawToTempScreen(){
        //tile screen
        if(gameState == titleState){
            ui.draw(g2);
        }
        //others
        else{
            //draw map
            tileM.draw(g2);

            // ADD ENTITIES TO THE LIST
            for(int i = 0; i < npc[1].length; i++){
                if(npc[currentMap][i] != null){
                    entityList.add(npc[currentMap][i]);
                }
            }

            for(int i = 0; i < signs[1].length; i++){
                if(signs[currentMap][i] != null){
                    entityList.add(signs[currentMap][i]);
                }
            }

            for(int i = 0; i < obj[1].length; i++){
                if(obj[currentMap][i] != null){
                    entityList.add(obj[currentMap][i]);
                }
            }

            for(int i = 0; i < monster[1].length; i++){
                if(monster[currentMap][i] != null){
                    entityList.add(monster[currentMap][i]);
                }
            }

            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    entityList.add(projectileList.get(i));
                }
            }

            //SORT
            entityList.sort(new Comparator<Entity>() {

                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            //DRAW ENTITIES
            for(int i = 0; i < entityList.size(); i++){
                entityList.get(i).draw(g2);
            }
            // EMPTY THE LIST
            entityList.clear();

            // UI
            ui.draw(g2);
            player.draw(g2);

            //debug text col & row
            if(keyH.showDebugText) {
                g2.setFont(new Font("Arial",Font.PLAIN,20));
                g2.setColor(Color.white);
                int x = 100;
                int y = 400;
                int lineHeight =  20;

                g2.drawString("Col: X " + (player.worldX + player.solidArea.x) / tileSize,x,y); y+=lineHeight;
                g2.drawString("Row: Y " + (player.worldY + player.solidArea.y) / tileSize,x,y); y+=lineHeight;
            }
        }
    }
    public void drawToScreen(){
        Graphics g = getGraphics();
        g.drawImage(tempScreen,0,0,screenWidth2,screenHeight2,null);
        g.dispose();
    }
    public void playMusic(int i){
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic(){
        sound.stop();
    }

    public Clip playCutScenes(int i){
        sound.setFile(i);
        sound.play();
        return sound.clip;
    }

    public void playSE(int i){
        SE.setFile(i);
        SE.play();
    }




}