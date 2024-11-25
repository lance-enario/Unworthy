package objects;

import Entity.Entity;
import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class obj_guardl extends Entity {
    GamePanel gp;
    public static final String objName = "Guard";

    public obj_guardl(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_obstacle;
        name = "Guard";
        up1 = setup("/NPC/npc_guardr/knight_left");
        collision = true;


        solidArea.x = 32;
        solidArea.y = 10;
        solidArea.width = 50;
        solidArea.height = gp.tileSize*3;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDialogue();
    }

    public void interact(){
        if (gp.ui.numberofdeadppl <=0) {
            gp.obj[gp.currentMap][1] = null;
        }else{
            startDialogue(this, 0);
        }


    }

    @Override
    public void setAction(){
        if (gp.ui.numberofdeadppl <=0) {
            gp.obj[0][1] = null;
        }
    }

    public void setDialogue(){
        dialogues[0][0] =  "You cannot enter here!";
        dialogues[1][0] =  "You can now traverse to the village.";
    }

    public BufferedImage setup(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, gp.tileSize, (gp.tileSize * 2));
        } catch (IOException e) {
            e.printStackTrace();

        }
        return image;
    }

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
                case "right":
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

            boolean shouldFlip = direction.equals("right");


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
