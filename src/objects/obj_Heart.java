package objects;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.io.IOException;

public class obj_Heart extends superObject {
    GamePanel gp;

    public obj_Heart(GamePanel gp){
        this.gp = gp;

        name = "Heart";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/heart2.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/objects/heart1.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/objects/heart3.png"));
            image = uTool.scaleImage(image,gp.tileSize,gp.tileSize);
            image2 = uTool.scaleImage(image2,gp.tileSize,gp.tileSize);
            image3 = uTool.scaleImage(image3,gp.tileSize,gp.tileSize);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

