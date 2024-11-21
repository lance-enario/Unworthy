package Entity;

import Main.GamePanel;

public class npc_cat extends Entity {

    public npc_cat(GamePanel gp) {
        super(gp);
        direction = "default";
        speed = 0;
        setDialogue();
        getImage();
    }

    public void getImage() {
        up1 = setup("/NPC/cat/cat_1");
        up2 = setup("/NPC/cat/cat_1");
        down1 = setup("/NPC/cat/cat_2");
        down2 = setup("/NPC/cat/cat_2");
        left1 = setup("/NPC/cat/cat_3");
        left2 = setup("/NPC/cat/cat_3");
        right1 = setup("/NPC/cat/cat_1");
        right2 = setup("/NPC/cat/cat_1");
    }
    public void setDialogue(){
        dialogue[0] = "*meow*";
        dialogue[1] = "*meeooww*";
        dialogue[2] = " *meow?*";
        dialogue[3] = "What you lookin at? I said meow";

    }

    public void speak(){
        super.speak();
    }




}
