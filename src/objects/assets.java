package objects;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class assets {
    public BufferedImage image, image2, image3, image4, image5, image6, image7, image8, image9, banner,outskirts, village, castle, dungeon;
    public BufferedImage[] cue = new BufferedImage[15];

    public assets (GamePanel gp) {

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/title.png")));
            image2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/Charselect.png")));
            image3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/buttons/back.png")));
            image4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/buttons/warrior.png")));
            image5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/buttons/mage.png")));
            image6 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/buttons/ranger.png")));
            image7= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Ranger/1.png")));
            image8 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Warrior/3.png")));
            image9 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/2.png")));

            for(int i =0;i < 12;i++){
                cue[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/cutScenes/" + (i+1) + ".png")));
            }

            banner = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/banner/banner2.png")));
            outskirts = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/banner/yes.png")));
            village = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/banner/village.png")));
            castle = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/banner/castle.png")));
            dungeon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/banner/dungeon.png")));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
