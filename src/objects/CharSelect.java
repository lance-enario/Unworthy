package objects;

import Entity.Entity;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.util.Objects;

public class CharSelect extends Entity {

    public CharSelect(GamePanel gp) {
        super(gp);
        name = "CharSelect";
        try{
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Ranger/1.png")));
            image2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Warrior/3.png")));
            image3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/2.png")));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
