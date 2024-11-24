package objects;
import Entity.Entity;
import Main.GamePanel;


public class obj_Chest extends Entity {

    GamePanel gp;
    Entity loot;
    boolean opened = false;

    public obj_Chest(GamePanel gp, Entity loot){
        super(gp);
        this.gp = gp;
        this.loot = loot;
        type = type_obstacle;
        name = "Chest";
        image = setup("/objects/chest");
        image2 = setup("/objects/chest_open");
        up1 = image;
        collision = true;

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDialogue();
        setLoot(loot);
    }

    public void setLoot(Entity loot){
        this.loot = loot;
        setDialogue();
    }


    public void setDialogue(){
        dialogues[0][0] = "You open the chest and find a " + loot.name + "!" + "\n...Your hands are too full to pick that up!";
        dialogues[1][0] =  "You open the chest and find a" + loot.name + "!" + "You obtained " + loot.name +"!";
        dialogues[2][0] =  "It's empty!";;

    }
    public void interact(){
        gp.gameState = gp.dialogueState;

        if(!opened){
            //SFX
            if(!gp.player.canObtainItem(loot)){
               startDialogue(this, 0);
            } else {
               startDialogue(this, 1);
                up1 = image2;
                opened = true;
            }
        }
        else {
            startDialogue(this, 2);
        }
    }
}
