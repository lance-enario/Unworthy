package objects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class obj_Door extends superObject{

    public obj_Door() {
        name = "Door";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        collision = true;
    }
}
