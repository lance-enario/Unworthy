package objects;

import Entity.Projectile;
import Main.GamePanel;

public class obj_MageSkill1 extends Projectile{
    public obj_MageSkill1(GamePanel gp){
        super(gp);
        solidArea.width = 64;
        solidArea.height = 64;

        name = "MageSkill1";
        speed = 4;
        maxLife = 80;
        life = maxLife;
        attack = 5;
        isAlive = false;
        knockbackPower = 2;
        getImage();
    }

    public void getImage(){
        up1 = setup("/projectile/mageskill1");
    }
}
