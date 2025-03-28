package objects;

import Entity.Entity;
import Main.GamePanel;


public class obj_Potion extends Entity {
    GamePanel gp;

    public obj_Potion(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = type_consumable;
        price = 10;
        name = "Health Potion";
        value = 5;
        down1 = setup("/objects/potion");
        up1 = setup("/objects/potion");
        description = "[" + name + "]\nA magical salve that\ncan mend even the\ndeepest of wounds.";
        stackable = true;
        setDialogue();
    }

    public void setDialogue(){
        dialogues[0][0] = "You drink the " + name + "!\n" + "Your wounds have been healed by " + value + ".";
    }
    public boolean use(Entity e){
        startDialogue(this, 0);
        e.life += value;
        if(gp.player.life > gp.player.maxLife){
            gp.player.life = gp.player.maxLife;
        }

        //needs sounds
        return true;
    }
}
