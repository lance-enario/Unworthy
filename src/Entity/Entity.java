package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;

public class Entity {
    public int worldX, worldY;
    public String direction = "down";
    public String maintain = "right";
    public boolean isAttacking;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage[] idleFrames;
    public int audioCounter = 0;
    public int spriteCounter = 0;
    public int spriteNum = 1;

    //placeholder area lines for collision & dialogue check
    public Rectangle solidArea = new Rectangle(32,56, 64, 64);
    public Rectangle DialogueArea = new Rectangle(32,56, 64, 64);

    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean CollisionOn = false;

    public int actionLockCounter = 0;

    //entity invincibility
    public boolean isInvincible = false;
    public int invincibleCounter = 0;

    String[] dialogue = new String[20];
    int dialogueIndex = 0;
    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    public int type = 0;

    // ATTRIBUTES
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
        gp.cChecker.checkOBJ(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.type == 2 && contactPlayer){
            if (!gp.player.isInvincible){
                gp.player.life -= 1;
                gp.player.isInvincible = true;
            }
        }

        if (!CollisionOn) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
                case "default":
                    break;
            }
        }

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

    public void draw(Graphics g2) {
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
//                switch(direction){
//                    case "up": image = (spriteNum == 1) ? up1 : up2; break;
//                    case "down": image = (spriteNum == 1) ? down1 : down2; break;
//                    case "left": image = (spriteNum == 1) ? left1 : left2; break;
//                    case "right": image = (spriteNum == 1) ? right1 : right2; break;
//
//                }

//          g2.setColor(Color.green);
//          g2.drawRect(screenX + DialogueArea.x, screenY +  DialogueArea.y, DialogueArea.width, DialogueArea.height);

            boolean shouldFlip = false;

            if (direction.equals("left") ||
                    (direction.equals("up") && maintain.equals("left")) ||
                    (direction.equals("down") && maintain.equals("left")) ||
                    (direction.equals("default") && maintain.equals("left")))
            {
                shouldFlip = true;
            }

            if (shouldFlip) {
                g2.drawImage(image, (screenX + gp.tileSize), screenY, -gp.tileSize, gp.tileSize, null);
            } else {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

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
        return image;
    }

}


