package objects;

import javax.imageio.ImageIO;
import java.io.IOException;

public class obj_Key extends superObject{

    public obj_Key(){
        name = "Key";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
        }catch (IOException e){
        }

    }
}
