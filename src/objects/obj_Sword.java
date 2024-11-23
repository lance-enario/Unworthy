package objects;
import Entity.Entity;
import Main.GamePanel;

public class obj_Sword extends Entity{
    public obj_Sword(GamePanel gp){
        super(gp);
        name = "Hero's Sword";
        down1 = setup("/objects/sword");
        attackValue = 3;
        description = "[" + name + "]\nA magical sword that\ngrows in power the\nlonger it is used.";
        knockbackPower = 5;
    }


}
