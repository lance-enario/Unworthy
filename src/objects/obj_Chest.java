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
    }

    public void interact(){
        gp.gameState = gp.dialogueState;

        if(!opened){
            StringBuilder sb = new StringBuilder();
            sb.append("You open the chest and find a " + loot.name + "!");

            if(!gp.player.canObtainItem(loot)){
                sb.append("\n...Your hands are too full to pick that up!");
            } else {
                sb.append("\nYou obtained "+loot.name+"!");
                up1 = image2;
                opened = true;
            }
            gp.ui.currentDialogue = sb.toString();
        }
        else {
            gp.ui.currentDialogue = "It's empty!";
        }
    }
}
