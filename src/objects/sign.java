package objects;

import Entity.Entity;
import Main.GamePanel;

public class sign extends  Entity {



        public sign(GamePanel gp) {
            super(gp);
            direction = "default";
            speed = 0;
            //setDialogue();
            getImage();
        }

    public void getImage() {
            up1 = setup("/objects/sign");
            up2 = setup("/objects/sign");
            down1 = setup("/objects/sign");
            down2 = setup("/objects/sign");
            left1 = setup("/NPC_Traveller/wanderingTraveller4");
            left2 = setup("/NPC_Traveller/wanderingTraveller5");
            right1 = setup("/NPC_Traveller/wanderingTraveller6");
        }

//        public void setDialogue(){
//            dialogue[0] = "Hi <3";
//            dialogue[1] = "Katugnaw";
//            dialogue[2] = " haha";
//            dialogue[3] = "Palambing";
//
//        }

        public void speak(){
            super.speak();
        }

    }




