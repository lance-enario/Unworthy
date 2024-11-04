package Object;

import Main.GamePanel;
import Entity.Projectile;

public class OBJ_MageAttack extends Projectile {
    GamePanel gp;

    public OBJ_MageAttack (GamePanel gp){
        super(gp);
        this.gp = gp;

        name = "Fireball";
        speed = 5;
        maxLife = 80;
        life = maxLife;
        attack = 2;
        isAlive = false;
//        getImage();
    }

//    public void getImage(){
//        ball = setup("projectile/mageproj1.png", gp.tileSize, gp.tileSize);
//    }

}
