package objects;

import Entity.Entity;
import Main.GamePanel;


public class obj_Potion extends Entity {
    GamePanel gp;
    int value = 5;

    public obj_Potion(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Health Potion";
        down1 = setup("/objects/potion");
        up1 = setup("/objects/potion");
        description = "[" + name + "]\nA magical salve that\ncan mend even the\ndeepest of wounds.";
    }
    public void use(Entity e){
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drink the " + name + "!\n" + "Your wounds have been healed by " + value + ".";
        e.life += value;
        if(gp.player.life > gp.player.maxLife){
            gp.player.life = gp.player.maxLife;
        }
        //needs sounds
    }
}
