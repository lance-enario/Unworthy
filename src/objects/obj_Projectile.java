package objects;

import Entity.Projectile;
import Main.GamePanel;

public class obj_Projectile extends Projectile {

    GamePanel gp;

    public obj_Projectile(GamePanel gp){
        super(gp);
        this.gp = gp;

        name = "MonsterAttack";
        speed = 4;
        maxLife = 60;
        life = maxLife;
        attack = 3;
        isAlive = false;
        getImage();
    }

    public void getImage(){
        up1 = setup("/projectile/mageproj1");
        up2 = setup("/projectile/mageproj1");
        down1 = setup("/projectile/mageproj1");
        down2 = setup("/projectile/mageproj1");
        left1 = setup("/projectile/mageproj1");
        left2 = setup("/projectile/mageproj1");
        right1 = setup("/projectile/mageproj1");
        right2 = setup("/projectile/mageproj1");
    }
}