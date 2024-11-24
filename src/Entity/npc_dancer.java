package Entity;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class npc_dancer extends Entity {

    public npc_dancer (GamePanel gp) {
        super(gp);
        direction = "default";
        speed = 0;
        setDialogue();
        getImage();
    }

    public void getImage() {
        up1 = setup("/NPC/dancer1/dance_1");
        up2 = setup("/NPC/dancer1/dance_1");
        down1 = setup("/NPC/dancer1/dance_2");
        down2 = setup("/NPC/dancer1/dance_2");
        left1 = setup("/NPC/dancer1/dance_3");
        left2 = setup("/NPC/dancer1/dance_3");
        right1 = setup("/NPC/dancer1/dance_1");
        right2 = setup("/NPC/dancer1/dance_1");
    }

    public void setDialogue(){
        dialogues[0][0] = "One day, I’ll dance for the king and queen, and they’ll toss\ngold coins at my feet!";
        dialogues[0][1] = "Do you think the minstrel’s lute can play a tune fast enough for my twirls?";
        dialogues[0][2] = "Let’s pretend the marketplace is a grand hall, and I’m dancing for all the nobles!";
        dialogues[0][3] = "I’ll travel to the next kingdom and learn dances from faraway lands.\nImagine the stories in their steps!";

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
