package objects;

import Entity.Entity;
import Entity.Projectile;
import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class obj_RangerBasicArrow extends Projectile {

    GamePanel gp;

    public obj_RangerBasicArrow(GamePanel gp){
        super(gp);
        this.gp = gp;

        name = "Ranger Attack";
        speed = 6;
        maxLife = 60;
        life = maxLife;
        attack = 3;
        isAlive = false;
        getImage();
    }

    public void getImage(){

        up1 = setup("/projectile/arrow");
        up2 = setup("/projectile/arrow");
        down1 = setup("/projectile/arrow");
        down2 = setup("/projectile/arrow");
        right1 = setup("/projectile/arrow");
        right2 = setup("/projectile/arrow");
        left1 = setup("/projectile/arrow");
        left2 = setup("/projectile/arrow");
    }
    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            BufferedImage imageToDraw = up1;

            switch (direction) {
                case "left":
                    imageToDraw = flipImage(up1);
                    break;
                case "down":
                    imageToDraw = rotateImage(up1, 90);
                    break;
                case "up":
                    imageToDraw = rotateImage(up1, 270);
                    break;
                case "right":
                default:
                    imageToDraw = up1;
                    break;
            }


            g2.drawImage(imageToDraw, screenX, screenY, gp.tileSize + 32, gp.tileSize + 32, null);
        }
    }

    private BufferedImage flipImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(image, width, 0, -width, height, null);
        g.dispose();
        return flippedImage;
    }

    private BufferedImage rotateImage(BufferedImage image, double angle) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage rotatedImage = new BufferedImage(width, height, image.getType());
        Graphics2D g = rotatedImage.createGraphics();
        g.rotate(Math.toRadians(angle), width / 2, height / 2);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return rotatedImage;
    }
}