package Entity;

import Main.GamePanel;

public class NPC_Traveller extends Entity {

    public NPC_Traveller(GamePanel gp) {
        super(gp);
        direction = "default";
        speed = 0;
        setDialogue();
        getImage();
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

    public void speak(){
        super.speak();
    }

}
