package objects;

import Entity.Projectile;
import Main.GamePanel;

import java.awt.*;

public class obj_LichSkill extends Projectile{
    public obj_LichSkill(GamePanel gp){
        super(gp);
        solidArea.width = 64;
        solidArea.height = 64;

        name = "Lich Attack";
        speed = 4;
        maxLife = 100;
        life = maxLife;
        attack = 5;
        isAlive = false;
        knockbackPower = 2;
        getImage();
    }

    public void getImage(){
        up1 = setup("/General/Lich/basic_attack/projectile/lich_basic_projectile_0");
        up2 = setup("/General/Lich/basic_attack/projectile/lich_basic_projectile_1");
        down1 = setup("/General/Lich/basic_attack/projectile/lich_basic_projectile_2");
        down2 = setup("/General/Lich/basic_attack/projectile/lich_basic_projectile_3");
        left1 = setup("/General/Lich/basic_attack/projectile/lich_basic_projectile_4");
        left2 = setup("/General/Lich/basic_attack/projectile/lich_basic_projectile_5");
        right1 = setup("/General/Lich/basic_attack/projectile/lich_basic_projectile_5");
        right2 = setup("/General/Lich/basic_attack/projectile/lich_basic_projectile_5");
    }

    public Color getParticleColor(){
        Color color = new Color(235,9,21);
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
