package objects;

import Entity.Entity;
import Main.GamePanel;

public class obj_Coin extends Entity {
    GamePanel gp;

    public obj_Coin(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = type_pickupOnly;
        name = "Golden Coin";
        value = 1;
        up1 = setup("/objects/Coin");
    }

    public void use(Entity e){
        //needs message
        gp.ui.showMessage("Coin +" + value);
        gp.player.coins += value;
    }
}
