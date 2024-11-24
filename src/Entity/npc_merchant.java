package Entity;
import Main.GamePanel;
import objects.obj_Key;
import objects.obj_Potion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public class npc_merchant extends Entity{
        public npc_merchant(GamePanel gp){
            super(gp);

            direction = "down";
            speed = 0;

            solidArea = new Rectangle();
            solidArea.x = 8;
            solidArea.y = 16;
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
            solidArea.width = 32;
            solidArea.height = 32;

            getImage();
            setDialogue();
            setItems();
        }

        public void getImage(){
            up1 = setup("/NPC/farmer2/farmer2_1");
            up2 = setup("/NPC/farmer2/farmer2_2");
            down1 = setup("/NPC/farmer2/farmer2_3");
            down2 = setup("/NPC/farmer2/farmer2_1");
            left1 = setup("/NPC/farmer2/farmer2_2");
            left2 = setup("/NPC/farmer2/farmer2_3");
            right1 = setup("/NPC/farmer2/farmer2_3");
            right2 = setup("/NPC/farmer2/farmer2_3");

        }

        public void setDialogue(){
            dialogue[0] = "Hey there!\nI have some good stuff.\nDo you want to buy?";

        }

        public void setItems(){
            inventory.add(new obj_Potion(gp));
            inventory.add(new obj_Key(gp));
            //add more for inventory
        }

        public void speak(){
            super.speak();
            gp.gameState = gp.buyState;
            gp.ui.npc = this;

        }
}
