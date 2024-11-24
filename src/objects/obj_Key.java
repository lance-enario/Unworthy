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
        price = 100;
        stackable = true;
        setDialogue();
    }
    public boolean use(Entity entity) {
        gp.gameState = gp.dialogueState;

        int objIndex = getDetected(entity, gp.obj, "Guard");
        if (objIndex != 999) {
           startDialogue(this, 0);
            gp.obj[gp.currentMap][objIndex] = null;
            return true;
        }

        objIndex = getDetected(entity, gp.obj, "Iron_Door");
        if (objIndex != 999) {
            startDialogue(this, 1);
            System.out.println(objIndex);

                gp.obj[gp.currentMap][objIndex] = null;
                gp.obj[gp.currentMap][objIndex-1] = null;
                return true;
        }
        objIndex = getDetected(entity, gp.obj, "Iron_Door2");
        if (objIndex != 999) {
            startDialogue(this, 1);
            System.out.println(objIndex);

            gp.obj[gp.currentMap][objIndex] = null;
            gp.obj[gp.currentMap][objIndex+1] = null;
            return true;
        }


        startDialogue(this, 2);
        return false;
    }

    public void interact(){
        startDialogue(this, dialogueIndex);
    }

    public void setDialogue(){
        dialogues[0][0] = "You give the guard the " + name + " and he lets you through.";
        dialogues[0][1] = "You use the " + name + " and open the door.";
        dialogues[0][2] = "You cannot use the " + name + " here.";

    }

}
