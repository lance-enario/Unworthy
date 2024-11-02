package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;

public class Entity {
    public int worldX, worldY;
    public String direction;
    public String maintain;
    public String prevDirection;
    public boolean isAttacking;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage[] idleFrames;
    public int audioCounter = 0;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(32,56, 64, 64);
    public Rectangle DialogueArea = new Rectangle(32,56, 80, 80);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean CollisionOn = false;
    public int actionLockCounter = 0;
    String[] dialogue = new String[20];
    int dialogueIndex = 0;

    // ATTRIBUTES
    public String name;
    public int speed;
    public int maxLife;
    public int life;
    public int level;
    public int attack;
    public Projectile projectile;
    public boolean alive;

    GamePanel gp;


    public Entity(GamePanel gp) {
        this.gp = gp;
    }


    public void setAction(){}


    public void speak(){
        if(dialogue[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogue[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction){
            case "up":  direction = "down"; break;
            case "left": direction = "right"; break;
            case "down": direction = "up"; break;
            case "right": direction = "left"; break;
            case "default": direction = "default"; break;
        }
    }


    public void update() {
        setAction();
        CollisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkPlayer(this);
        spriteCounter++;

        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 3;
            } else if (spriteNum == 3) {
                spriteNum = 4;
            } else if (spriteNum == 4) {
                spriteNum = 5;
            } else if (spriteNum == 5) {
                spriteNum = 6;
            } else {
                spriteNum = 1;
            }
            spriteCounter = 0;

        }
    }


    public void draw2(Graphics g2) {
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (direction) {
                case "default", "up", "down", "left", "right":
                   if(spriteNum == 1) {
                        image = up1;
                    }
                    if(spriteNum == 2) {
                        image = up2;
                    }
                    if(spriteNum == 3) {
                        image = down1;
                    }
                    if(spriteNum == 4) {
                        image = down2;
                    }
                    if(spriteNum == 5) {
                        image = left1;
                    }
                    if(spriteNum == 6) {
                        image = left2;
                    }
                    if(spriteNum == 7) {
                        image = right1;
                    }
                    break;

            }
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            g2.setColor(Color.green);
            g2.drawRect(screenX + DialogueArea.x, screenY +  DialogueArea.y, DialogueArea.width, DialogueArea.height);
            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

    public BufferedImage setup(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return  image;
    }

}


