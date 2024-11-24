package Entity;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class npc_walk1 extends Entity {
    public npc_walk1(GamePanel gp) {
        super(gp);
        direction = "default";  // Initial direction
        speed = 2;  // Set speed to 1 for slower movement
        setDialogue();
        getImage();
    }

    public void getImage() {
        right1 = setup("/NPC/walk1/right_1");
        right2 = setup("/NPC/walk1/right_2");
        left1 = setup("/NPC/walk1/right_1");
        left2 = setup("/NPC/walk1/right_2");
        up1 = right1;
        up2 = right2;
        down1 = left1;
        down2 = left2;

    }

    public void setDialogue() {
        dialogues[0][0] = "Oops! Sorry";
        dialogues[0][1] = "Out of the way mate!";
        dialogues[0][2] = "Pardon!";
        dialogues[0][3] = "Im on a hurry!!";
    }

    @Override

    public void speak(){
        startDialogue(this, dialogueSet);
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
                g2.drawImage(image, (screenX + gp.tileSize), screenY, -gp.tileSize-30, gp.tileSize+30, null);
            } else {
                g2.drawImage(image, screenX, screenY, gp.tileSize+30, gp.tileSize+30, null);
            }


            changeAlpha(g2,1.0f);

            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

}
