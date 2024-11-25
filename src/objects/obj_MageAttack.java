package objects;

import Entity.Projectile;
import Main.GamePanel;

import java.awt.*;

public class obj_MageAttack extends Projectile{
    public obj_MageAttack(GamePanel gp){
        super(gp);
        type = 11;
        solidArea.width = 32;
        solidArea.height = 32;

        name = "MageAttack";
        speed = 8;
        maxLife = 60;
        life = maxLife;
        attack = 3;
        isAlive = false;
        knockbackPower = 0;
        getImage();
    }

    public void getImage(){
        up1 = setup("/projectile/mageproj1");
        up2 = setup("/projectile/mageproj1");
        down1 = setup("/projectile/mageproj1");
        down2 = setup("/projectile/mageproj1");
        right1 = setup("/projectile/mageproj1");
        right2 = setup("/projectile/mageproj1");
        left1 = setup("/projectile/mageproj1");
        left2 = setup("/projectile/mageproj1");
    }

    public Color getParticleColor(){
        Color color = new Color(240,50,0);
        return color;
    }

    public int getParticleSize(){
        int size = 10;
        return size;
    }

    public int getParticleSpeed(){
        int speed = 1;
        return speed;
    }

    public int getParticleMaxLife(){
        int maxLife = 20;
        return maxLife;
    }
}
