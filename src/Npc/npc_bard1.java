package Npc;

import Entity.Entity;
import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class
npc_bard1 extends Entity {

    GamePanel gp;

    public npc_bard1(GamePanel gp) {
        super(gp);
        this.gp = gp;
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
        dialogues[0][0] = "Ah, a lord graces my stage! Tell me, traveler, do you seek\n" +
                          "a tale or a tune? Or perhaps... both?";
        dialogues[0][1] = "The Tomb, you say? A dangerous tune, that one. Many have sought\n" +
                          " its secrets, and most never sang again. But I’ll share what I know...\nin song, of course.";
        dialogues[0][2] = """
               In the southeasts where the winds bite deep,
               A sovereign lies in eternal sleep.
               Beneath the ice, in the frozen gloom,
               A tomb awaits—a ruler’s doom.
               \s""";
        dialogues[0][3] = """
               The Soulbrand rests where the frost is cold,
               A blade of legend, both dark and bold.
               The dead rise here, their whispers loud,
               To guard the relic beneath its shroud."
               \s""";
        dialogues[0][4] = """
                But beware, brave knight, of the blade you seek,
                Its power tempts both strong and weak.
                The Soulbrand’s call, a chilling sound,
                Can raise a king—or drag him down."*
               \s""";
        dialogues[0][5] = """
                A brave soul, indeed! May your steps be swift, your blade be true,
                And when you return, perhaps a song or two? Best of luck, Lord Lucian."
               \s""";
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
