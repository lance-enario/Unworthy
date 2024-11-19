package objects;

import Main.GamePanel;
import Entity.Entity;

public class obj_Coin extends Entity{

    GamePanel gp;

    public obj_Coin(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = type_pickUpOnly;
        name = "Gold Coin";
        value = 1;
        up1 = setup("/objects/Coin");
    }

    public boolean use(Entity e){
        //NEED SE
        gp.ui.showMessage("Coin +" + value);
        gp.player.coins += value;

        return true;
    }
}
