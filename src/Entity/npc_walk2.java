package Entity;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class npc_walk2 extends Entity {
    public npc_walk2(GamePanel gp) {
        super(gp);
        direction = "default";  // Initial direction
        speed = 5;  // Set speed to 1 for slower movement
        setDialogue();
        getImage();

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 30;
        solidArea.height = 30;
    }

    public void getImage() {
        left1 = setup("/NPC/farmer1_walk/right1");
        left2 = setup("/NPC/farmer1_walk/right2");
        right1 = setup("/NPC/farmer1_walk/right1");
        right2 = setup("/NPC/farmer1_walk/right2");
        up1 = left1;
        up2 = left2;
        down1 = right1;
        down2 = right2;

    }

    public void setDialogue() {
        dialogues[0][0] = "Hi! Bye!";
        dialogues[0][1] = "I cannot be bothered right now!";
        dialogues[0][2] = "i got work to do!";
        dialogues[0][3] = "Tsk";
    }

    @Override

    public void speak(){
        startDialogue(this, dialogueSet);
        onPath = true;
    }

    @Override
    public void setAction() {

        if(onPath){
//            int goalCol = 78;
//            int goalRow = 22;

            int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;
            searchPath(goalCol,goalRow);
        } else {
            actionLockCounter++;

            if(actionLockCounter == 40){
                Random rand = new Random();
                int i = rand.nextInt(100) + 1;
                if(i <= 25){
                    direction = "up";
                }
                if ( i > 25 && i <= 50){
                    direction = "down";
                }
                if(i > 50 && i <=75 ){
                    direction = "left"; // left
                }
                if(i > 75 && i <= 100 ){
                    direction = "right"; // right
                }
                actionLockCounter = 0;
            }
        }


    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (direction) {
                case "default", "up", "down", "left", "right", "upleft", "upright", "downleft", "downright":
                    if(spriteNum == 1) image = up1;
                    if(spriteNum == 2) image = up2;
                    if(spriteNum == 3) image = down1;
                    if(spriteNum == 4) image = down2;
                    if(spriteNum == 5) image = left1;
                    if(spriteNum == 6) image = left2;
                    if(spriteNum == 7) image = right1;
                    if(spriteNum == 8) image = right2;
                    break;
            }

            boolean shouldFlip = direction.equals("left") ||
                    (direction.equals("up") && maintain.equals("left")) ||
                    (direction.equals("down") && maintain.equals("left"));

                if (shouldFlip) {
                    g2.drawImage(image, (screenX + gp.tileSize), screenY, -gp.tileSize, gp.tileSize, null);
                } else {
                    g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }


            changeAlpha(g2,1.0f);

            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

}
