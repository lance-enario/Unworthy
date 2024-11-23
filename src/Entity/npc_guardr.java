package Entity;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class npc_guardr extends Entity {

    public npc_guardr(GamePanel gp) {
        super(gp);
        direction = "default";
        speed = 0;
        setDialogue();
        getImage();
    }

    public void getImage() {
        up1 = setup("/NPC/npc_guardr/knight1_0");
        up2 = setup("/NPC/npc_guardr/knight1_1");
        down1 = setup("/NPC/npc_guardr/knight1_2");
        down2 = setup("/NPC/npc_guardr/knight1_0");
        left1 = setup("/NPC/npc_guardr/knight1_1");
        left2 = setup("/NPC/npc_guardr/knight1_2");
        right1 = setup("/NPC/npc_guardr/knight1_0");
        right2 = setup("/NPC/npc_guardr/knight1_0");
    }

    public void setDialogue() {
        dialogue[0] = "You cannot enter!";
        dialogue[1] = "Katugnaw";
        dialogue[2] = " haha";
        dialogue[3] = "Palambing";
    }

    public void speak() {
        super.speak();
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Scale the tile size by the scaling factor (optional)
        int scaledTileSize = gp.tileSize * 2;

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

            boolean shouldFlip = direction.equals("left");

            solidArea.width = gp.tileSize;
            solidArea.height = gp.tileSize * 2;
            solidArea.x = solidArea.width / 3;  // Centered
            solidArea.y = (64 - solidArea.height) / 2;  // Centered
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;

            if (shouldFlip) {
                g2.drawImage(image, screenX + gp.tileSize, screenY, -scaledTileSize, scaledTileSize, null);
            } else {
                g2.drawImage(image, screenX, screenY, scaledTileSize, scaledTileSize, null);
            }

            g2.setColor(Color.red);
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

            changeAlpha(g2, 1.0f);
        }
    }
}
