package objects;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class obj_Chest extends  superObject{

    public obj_Chest(){
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/chest.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
