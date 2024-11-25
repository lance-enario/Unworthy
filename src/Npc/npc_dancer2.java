package Npc;

import Entity.Entity;
import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class npc_dancer2 extends Entity {
    GamePanel gp;
    public npc_dancer2 (GamePanel gp) {
        super(gp);
        this.gp = gp;
        direction = "default";
        speed = 0;
        setDialogue();
        getImage();
    }

    public void getImage() {
        up1 = setup("/NPC/dancer2/dance2_1");
        up2 = setup("/NPC/dancer2/dance2_1");
        down1 = setup("/NPC/dancer2/dance2_2");
        down2 = setup("/NPC/dancer2/dance2_2");
        left1 = setup("/NPC/dancer2/dance2_3");
        left2 = setup("/NPC/dancer2/dance2_3");
        right1 = setup("/NPC/dancer2/dance2_1");
        right2 = setup("/NPC/dancer2/dance2_1");
    }

    public void setDialogue(){
        dialogues[0][0] = "Every movement is a story, and every step carries the weight of a thousand tales.";
        dialogues[0][1] = "The secret is not in the steps but in the soul you pour into them. Without that, you’re merely moving.";
        dialogues[0][2] = "Do not ask me to perform for your coin. If I dance, it is for a cause greater than gold.";
        dialogues[0][3] = "The dragons of the south move in the air as I do on the ground—perhaps we share the same teacher.";

    }


    public void speak(){
        startDialogue(this, dialogueSet);
    }
    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        int scaledTileSize = (gp.tileSize * 2) - (gp.tileSize/2);  // Scale the tile size by the scaling factor

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            switch (direction) {
                case "default", "up", "down", "left", "right", "upleft", "upright", "downleft", "downright":
                    if (spriteNum == 1) image = up1;
                    if (spriteNum == 2) image = up2;
                    if (spriteNum == 3) image = down1;
                    if (spriteNum == 4) image = down2;
                    if (spriteNum == 5) image = left1;
                    if (spriteNum == 6) image = left2;
                    if (spriteNum == 7) image = right1;
                    if (spriteNum == 8) image = right2;
                    break;
            }

            boolean shouldFlip = direction.equals("default");
            g2.drawImage(image, (screenX + scaledTileSize), screenY, -scaledTileSize, scaledTileSize, null);


            solidArea.width = gp.tileSize ;
            solidArea.height = (gp.tileSize*2)-50;
            solidArea.x = solidArea.width/3; // center
            solidArea.y = (64 - solidArea.height) / 2; // center

            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

            changeAlpha(g2, 1.0f);
        }
    }



}
