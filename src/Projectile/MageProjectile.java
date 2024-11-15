package Projectile;

import Main.GamePanel;
import Main.KeyHandler;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MageProjectile extends Projectile {
    GamePanel gp;
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;

    public MageProjectile (GamePanel gp, KeyHandler keyH){
        this.gp = gp;       // setter for gp
        this.keyH = keyH;   //setter for keyH

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        setDefaultValues();
        getProjectileImage();
    }

    public void setDefaultValues(){

    }

    public void getProjectileImage(){

    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
            g2.drawImage(image, screenX, screenY, gp.playerSize, gp.playerSize, null);
    }
}
