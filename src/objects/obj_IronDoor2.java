package objects;

import Entity.Entity;
import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class obj_IronDoor2 extends Entity{
    GamePanel gp;
    public static final String objName = "Iron_Door";

    public obj_IronDoor2(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_obstacle;
        name = "Iron_Door";
        up1 = setup("/objects/iron_door1");
        collision = true;

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    public void interact(){
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "It wont budge!";

    }

    public void setDialogue(){

    }
    public BufferedImage setup(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image,gp.tileSize,(gp.tileSize*2));
        } catch (IOException e) {
            e.printStackTrace();

        }
        return image;
    }
}
