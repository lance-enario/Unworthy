package objects;
import Entity.Entity;
import Main.GamePanel;

public class obj_Bow extends Entity{
    public obj_Bow(GamePanel gp){
        super(gp);
        name = "Grove Bow";
        down1 = setup("/objects/bow");
        attackValue = 3;
        description = "[" + name + "]\nA gift of the moon goddess to one of her\nprized disciplines many memories ago.";
        knockbackPower = 5;
    }


}
