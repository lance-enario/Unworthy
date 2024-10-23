package Entity;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class NPC_Traveller extends Entity {

    BufferedImage[] idleFrames = new BufferedImage[6];

    public NPC_Traveller(GamePanel gp) {
        super(gp);
        direction = "default";
        speed = 1;
//        try {
//            for (int i = 0; i < 6; i++) {
//                idleFrames[i] = ImageIO.read(getClass().getResourceAsStream("/NPC_Traveller/wanderingTraveller_" + i + ".png"));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        getImage();
    }

    public void getImage() {
        up1 = setup("/NPC_Traveller/wanderingTraveller_0");
        up2 = setup("/NPC_Traveller/wanderingTraveller_1");
        down1 = setup("/NPC_Traveller/wanderingTraveller_2");
        down2 = setup("/NPC_Traveller/wanderingTraveller_3");
        left1 = setup("/NPC_Traveller/wanderingTraveller_4");
        left2 = setup("/NPC_Traveller/wanderingTraveller_5");
        right1 = setup("/NPC_Traveller/wanderingTraveller_6");
     //   right2 = setup("/NPC_Traveller/wanderingTraveller_7");
    }

    @Override
    public void setAction(){
        actionLockCounter++;

        if(actionLockCounter  == 120){
            Random rand = new Random();
            int i = rand.nextInt(100) + 1;

            // naka default tanan since mag idle raman sad ang traveller
            if(i<=25) {
                direction = "up"; //up
            }
            if(i > 25 && i <=50){
                direction = "down"; //down
            }
            if(i > 50 && i <= 75){
                direction = "left"; // left
            }
            if(i > 75 && i <= 100){
                direction = "right"; // right
            }

            actionLockCounter = 0;
        }



    }

}

