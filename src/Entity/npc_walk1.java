package Entity;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class npc_walk1 extends Entity {
    public npc_walk1(GamePanel gp) {
        super(gp);
        direction = "default";  // Initial direction
        speed = 1;  // Set speed to 1 for slower movement
        setDialogue();
        getImage();
    }

    public void getImage() {

        left1 = setup("/NPC/walk1/left_1");
        left2 = setup("/NPC/walk1/left_2");
        right1 = setup("/NPC/walk1/right_1");
        right2 = setup("/NPC/walk1/right_2");
    }

    public void setDialogue() {
        dialogue[0] = "Hi <3";
        dialogue[1] = "Katugnaw";
        dialogue[2] = " haha";
        dialogue[3] = "Palambing";
    }

    @Override
    public void speak() {
        super.speak();
    }

    @Override
    public void update() {
        setAction();
        CollisionOn = false;

        gp.cChecker.checkTile(this);
        gp.cChecker.checkOBJ(this, false);
        int i = gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);



        if (!CollisionOn) {
            switch (direction) {

                case "left":
                    if(spriteNum == 4 || spriteNum == 5 || spriteNum == 6) {
                        //   direction = maintain;
                        worldX -= speed + 2;
                    }

                    break;
                case "right":
                    if(spriteNum == 4 || spriteNum == 5 || spriteNum == 6) {
                        //   direction = maintain;
                        worldX += speed + 2;
                    }

                    break;
                case "default":
                    break;
            }
        }
        spriteCounter++;

            if (spriteCounter > 4) {
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
                } else if (spriteNum == 6) {
                    spriteNum = 7;
                } else if (spriteNum == 7) {
                    spriteNum = 8;
                } else {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }





    }

    @Override
    public void setAction() {
        actionLockCounter++;

        if(actionLockCounter == 40){
            Random rand = new Random();
            int i = rand.nextInt(100) + 1;


            if(i <= 50 ){
                direction = "left"; // left
            }
            if(i > 50 && i <= 99 ){
                direction = "right"; // right
            }
            actionLockCounter = 0;
        }

    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (direction) {
                case "default", "up", "down", "left", "right":
                    if(spriteNum == 1) image = left1;
                    if(spriteNum == 2) image = left2;
                    if(spriteNum == 3) image = left1;
                    if(spriteNum == 4) image = left1;
                    if(spriteNum == 5) image = right1;
                    if(spriteNum == 6) image = right1;
                    if(spriteNum == 7) image = right1;
                    if(spriteNum == 8) image = right1;
                    break;
            }

                g2.drawImage(image, screenX, screenY, gp.tileSize+30, gp.tileSize+30, null);

            changeAlpha(g2,1f);

            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

}
