package objects;

import Entity.Entity;
import Main.GamePanel;

public class obj_arrows extends Entity {
    public obj_arrows(GamePanel gp){
        super(gp);

        name = "Arrow";
        down1 = setup("/objects/arrow");
        defenseValue = 1;
        description = "[" + name + "]\nthe bullet or ammo \nfor the bowskie";
    }

}
