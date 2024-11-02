package objects;

import Entity.Entity;
import Main.GamePanel;


public class obj_Key extends Entity {

    public obj_Key(GamePanel gp){
        super(gp);
        name = "Key";
        down1 = setup("/objects/key");
    }
}
