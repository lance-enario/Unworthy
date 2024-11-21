package objects;

import Entity.Entity;
import Main.GamePanel;


public class obj_Key extends Entity {

    GamePanel gp;

    public obj_Key(GamePanel gp){
        super(gp);
        this.gp =gp;

        type = type_consumable;
        name = "Key";
        up1 = setup("/objects/key");
        down1 = setup("/objects/key");
    }
    public boolean use(Entity entity) {
        gp.gameState = gp.dialogueState;

        int objIndex = getDetected(entity, gp.obj, "Guard");
        if (objIndex != 999) {
            gp.ui.currentDialogue = "You give the guard the " + name + " and he lets you through.";
            gp.obj[gp.currentMap][objIndex] = null;
            return true;
        }

        objIndex = getDetected(entity, gp.obj, "Iron_Door");
        if (objIndex != 999) {
            gp.ui.currentDialogue = "You use the " + name + " and open the door.";
            System.out.println(objIndex);
            if(objIndex == 7){
                gp.obj[gp.currentMap][objIndex] = null;
                gp.obj[gp.currentMap][objIndex-1] = null;
            }else {
                gp.obj[gp.currentMap][objIndex+1] = null;
                gp.obj[gp.currentMap][objIndex] = null;
            }return true;
        }


        gp.ui.currentDialogue = "You cannot use the " + name + " here.";
        return false;
    }

}
