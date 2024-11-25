package Npc;

import Entity.Entity;
import Main.GamePanel;

import java.util.Random;

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
        right2 = setup("/NPC_Traveller/wanderingTraveller6");
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
                Please, Lord Lucian, we beg you. You’re the only one who can save us. Defeat the beasts and
                free our town. Without the gates open, we can’t trade, we can’t flee—we’re at the mercy of\s
                starvation and the king’s soldiers. We shall meet again at the castle gates should you be
                able to free us.
               \s""";

        dialogues[1][0] = """
                My lord... you’ve done it. The monsters... they’re gone. For the first time
                in years, the night will pass without fear clawing at our hearts. I can’t...
                I can’t find the words to thank you. Follow me to the town!
                """;
        dialogues[1][1] = """
               "Meadowspire, hear me! This is not the end of your struggles, but it is the start of\s
               something new. Together, we will rebuild, stronger than before. Let this day mark the\s
               turning of the tide for all of Eldoria."
              \s""";

        dialogues[2][0] = """
                Even under the shadow of tyranny, Meadowspire endures. Its spirit shines brighter\s
                than the darkness that seeks to consume it.
               \s""";

        dialogues[2][1] = """
                Look at the stonework on those towers! Even after all this time, the craftsmanship
                is unmatched. This place must have been glorious in its prime."
                """;
        dialogues[2][2] = """
               And the people—they still find a way to smile, to laugh. It takes a special kind\s
               of strength to hold on to hope like that.
               \s""";
        dialogues[2][3] = """
               My lord, there is something you must know. Beyond Meadowspire, deep in the
               Whispering Mines, lies a dungeon—an ancient place shrouded in darkness and danger.
               \s""";
        dialogues[2][4] = """
               Whispers speak of a relic hidden within—one of great power, capable of tipping\s
               the balance in your favor against Sirius. But the path to the dungeon is perilous,
               and its secrets are veiled in song and riddle.
               \s""";
        dialogues[2][5] = """
               There is one who can guide you—a bard named Kael. He is no ordinary minstrel;
               his songs hold truths long forgotten. He wanders the taverns and markets of\s
               Meadowspire, singing tales of the dungeon’s lore. If anyone knows how to find\s
               it, it is Kael.
               \s""";
    }

    @Override
    public void setAction() {
        if(gp.ui.numberofdeadppl <=0){
            dialogueSet = 1;
        }
        if(dialogueSet == 1 && gp.currentMap == 0) {
            worldX = 79 * gp.tileSize;
            worldY = 42 * gp.tileSize;
        }
        if(gp.currentMap == 1){
            dialogueSet = 2;
        }
    }

    public void speak(){
       startDialogue(this, dialogueSet);


       if(dialogues[dialogueSet][0] == null){
           dialogueSet --;
       }
    }



}
