package objects;

import Entity.Projectile;
import Main.GamePanel;

public class obj_BossProj extends Projectile{
    public obj_BossProj(GamePanel gp){
        super(gp);
        solidArea.width = 64;
        solidArea.height = 64;

        name = "Boss Proj";
        speed = 5;
        maxLife = 120;
        life = maxLife;
        attack = 5;
        isAlive = false;
        knockbackPower = 2;
        getImage();
    }

    public void getImage(){
        up1 = setup("/projectile/bossproj");
    }
}
