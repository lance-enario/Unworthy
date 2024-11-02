package objects;

import Entity.Entity;
import Main.GamePanel;

public class obj_Door extends Entity {

    public obj_Door(GamePanel gp) {
        super(gp);

        name = "Door";
        down1 = setup("/objects/Door");
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}

