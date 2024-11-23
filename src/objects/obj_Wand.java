package objects;
import Entity.Entity;
import Main.GamePanel;

public class obj_Wand extends Entity{
    public obj_Wand(GamePanel gp){
        super(gp);
        name = "Dagon";
        down1 = setup("/objects/wand");
        attackValue = 2;
        description = "[" + name + "]\nA lesser wand that\ngrows in power the\nlonger it is used.";

    }


}
