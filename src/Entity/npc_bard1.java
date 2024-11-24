package Entity;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class
npc_bard1 extends Entity {

    public npc_bard1(GamePanel gp) {
        super(gp);
        direction = "default";
        speed = 0;
        setDialogue();
        getImage();
    }

    public void getImage() {
        up1 = setup("/NPC/bard1/bard_0");
        up2 = setup("/NPC/bard1/bard_1");
        down1 = setup("/NPC/bard1/bard_0");
        down2 = setup("/NPC/bard1/bard_1");
        left1 = setup("/NPC/bard1/bard_0");
        left2 = setup("/NPC/bard1/bard_1");
        right1 = setup("/NPC/bard1/bard_0");
        right2 = setup("/NPC/bard1/bard_0");
    }

    public void setDialogue(){
        dialogues[0][0] = "Fine day innit!";
        dialogues[0][1] = "Puff, the magic dragon lived by the sea\n" +
                      "And frolicked in the autumn mist in a land called Honah Lee\n" +
                      "Little Jackie Paper loved that rascal Puff\n" +
                      "And brought him strings, and sealing wax, and other fancy stuff";
        dialogues[0][2] = " Oh, Puff, the magic dragon lived by the sea\n" +
                "And frolicked in the autumn mist, in a land called Honah Lee\n" +
                "Puff, the magic dragon, lived by the sea\n" +
                "And frolicked in the autumn mist, in a land called Honah Lee";
        dialogues[0][3] = "Together they would travel on a boat with billowed sail\n" +
                "Jackie kept a lookout perched on Puff's gigantic tail\n" +
                "Noble kings and princes would bow whenever they came\n" +
                "Pirate ships would lower their flags when Puff roared out his name";

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
