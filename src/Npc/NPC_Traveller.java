package Npc;

import Entity.Entity;
import Main.GamePanel;

public class NPC_Traveller extends Entity {
    GamePanel gp;
    public NPC_Traveller(GamePanel gp) {
        super(gp);
        this.gp = gp;
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
        right1 = setup("/NPC_Traveller/wanderingTraveller6");
    }

    public void setDialogue(){
        dialogues[0][0] = """
                By the stars above... it cannot be! Lord Lucian, you’ve returned! We thought you lost to the
                wilderness, yet here you stand, alive and as noble as your father before you. Praise the heavens,
                our true lord has come back to us!""";
        dialogues[0][1] = """
                We remember the days of King Alaric’s reign, my lord. The fields were green, the rivers clean,
                 and no one went to bed cold or hungry. Now... now we are forgotten.""";
        dialogues[0][2] = """
                Then hope is not lost. We’ve waited for this day, my lord. The people remember your kindness,
                your courage. You’ve come back to us, and we will stand with you. Whatever you need, the\s
                villagers and I will follow you to the end."\
               \s""";
        dialogues[0][3] = """
               Unfortunately, a band of monsters are scattered around the town gates. We of low stature,
               cannot enter the town as it is dangerous and that also the king highly forbids it. Most
               of your followers were cast out from the castle and left to live miserably. He believes
               we bring misfortune to the kingdom.\s
               \s""";
        dialogues[0][4] = """
                Please, Lord Lucian, we beg you. You’re the only one who can save us. Defeat the beastS and
                free our town. Without the gates open, we can’t trade, we can’t flee—we’re at the mercy of\s
                starvation and the king’s soldiers.
               \s""";

        dialogues[1][0] = "TYSMNIDA";
    }

    public void speak(){
       startDialogue(this, dialogueSet);
       if(gp.ui.numberofdeadppl <=0){
           dialogueSet++;
       }
       if(dialogues[dialogueSet][0] == null){
           dialogueSet --;
       }
    }



}
