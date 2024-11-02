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
        speed = 0;
        setDialogue();
        getImage();
        DialogueArea = new Rectangle(1, 1, 70, 70);
    }

    public void getImage() {
        up1 = setup("/NPC_Traveller/wanderingTraveller0");
        up2 = setup("/NPC_Traveller/wanderingTraveller1");
        down1 = setup("/NPC_Traveller/wanderingTraveller2");
        down2 = setup("/NPC_Traveller/wanderingTraveller3");
        left1 = setup("/NPC_Traveller/wanderingTraveller4");
        left2 = setup("/NPC_Traveller/wanderingTraveller5");
        right1 = setup("/NPC_Traveller/wanderingTraveller6");
    }

    public void setDialogue(){
        dialogue[0] = "Hi <3";
        dialogue[1] = "Katugnaw";
        dialogue[2] = " haha";
        dialogue[3] = "Palambing";

    }

    @Override
    public void setAction(){
        actionLockCounter++;

        if(actionLockCounter  == 120){
            Random rand = new Random();
            int i = rand.nextInt(100) + 1;

            // naka default tanan since mag idle raman sad ang traveller
//            if(i<=25) {
//                direction = "up"; //up
//            }
//            if(i > 25 && i <=50){
//                direction = "down"; //down
//            }
//            if(i > 50 && i <= 75){
//                direction = "left"; // left
//            }
//            if(i > 75 && i <= 100){
//                direction = "right"; // right
//            }
            direction = "default";

            actionLockCounter = 0;
        }
    }

    public void speak(){
        super.speak();
    }

}
