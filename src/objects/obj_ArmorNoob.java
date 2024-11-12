package objects;

import Entity.Entity;
import Main.GamePanel;

public class obj_ArmorNoob extends Entity {
    public obj_ArmorNoob(GamePanel gp){
        super(gp);

        name = "Chain Guard";
        down1 = setup(""); // wala pa
        defenseValue = 2;
        description = "[" + name + "]\n A coat of mail for\nselfless martyrs in combat.";

    }

}

