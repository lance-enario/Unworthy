package objects;

import Entity.Projectile;
import Main.GamePanel;

public class obj_MageAttack extends Projectile{

    GamePanel gp;

    public obj_MageAttack(GamePanel gp){
        super(gp);
        this.gp = gp;

        name = "MageAttack";
        speed = 8;
        maxLife = 60;
        life = maxLife;
        attack = 3;
        isAlive = false;
        getImage();
    }

    public void getImage(){
        up1 = setup("/projectile/mageproj1");
    }
}
