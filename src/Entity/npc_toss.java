package Entity;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class npc_toss extends Entity {

    public npc_toss(GamePanel gp) {
        super(gp);
        direction = "default";
        speed = 0;
        setDialogue();
        getImage();
    }

    public void getImage() {
        up1 = setup("/NPC/toss/toss_");
        up2 = setup("/NPC/toss/toss_");
        down1 = setup("/NPC/toss/toss_1");
        down2 = setup("/NPC/toss/toss_1");
        left1 = setup("/NPC/toss/toss_2");
        left2 = setup("/NPC/toss/toss_2");
        right1 = setup("/NPC/toss/toss_");
        right2 = setup("/NPC/toss/toss_");
    }

    public void setDialogue(){
        dialogue[0] = "G'dday!";
        dialogue[1] = "Ah, a discerning eye! You’ve chosen well. I’ll give you a fair price, as long as you don’t tell the others!";
        dialogue[2] = "Step closer, my friend! No need to wander. I’ve got wares for all, and I’m sure we can come to a fair deal.";
        dialogue[3] = "You won’t find a better deal in the market, I assure you. My goods are the finest quality, and my prices, well, they’re as fair as the sun rises!";

    }

    public void speak(){
        super.speak();
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
