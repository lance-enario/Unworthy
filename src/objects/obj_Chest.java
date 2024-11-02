package objects;
import Entity.Entity;
import Main.GamePanel;


public class obj_Chest extends Entity {

    public obj_Chest(GamePanel gp){
        super(gp);

        name = "Chest";
        down1 = setup("/objects/chest");
    }
}
