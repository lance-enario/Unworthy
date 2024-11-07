package objects;

import Entity.Entity;
import Main.GamePanel;

public class obj_Book extends Entity {
    public obj_Book(GamePanel gp){
        super(gp);

        name = "Book";
        down1 = setup("/objects/book");
        defenseValue = 1;
    }

}
