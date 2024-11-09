package objects;
import Entity.Entity;
import Main.GamePanel;

public class obj_Wand extends Entity{
    public obj_Wand(GamePanel gp){
        super(gp);
        name = "Wand";
        down1 = setup("/objects/wand");
        attack = 2;
        description = "[" + name + "]\nAn aghanim Scepter.";
    }


}
