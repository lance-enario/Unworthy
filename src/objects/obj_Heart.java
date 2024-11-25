package objects;

import Entity.Entity;
import Main.GamePanel;

public class obj_Heart extends Entity {

    public obj_Heart(GamePanel gp){
        super(gp);

        name = "Heart";
        image = setup("/objects/heart2");
        image2 = setup("/objects/heart1");
        image3 = setup("/objects/heart3");

    }
}

