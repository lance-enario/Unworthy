package objects;

import Entity.Entity;
import Main.GamePanel;

public class CharSelect extends Entity {

    public CharSelect(GamePanel gp) {
        super(gp);
        name = "CharSelect";
        image = setup("/player/Ranger/Idle/ranger_idle_1");
        image2 = setup("/player/Warrior/Idle/warrior_idle_2");
        image3 = setup("/player/idle/Idle3");
    }
}
