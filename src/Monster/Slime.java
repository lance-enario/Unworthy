
package Monster;

import Entity.Entity;
import Main.GamePanel;

import java.awt.*;
import java.util.Random;

public class Slime extends Entity {
    GamePanel gp;


    public Slime(GamePanel gp){
        super(gp);

        this.gp = gp;

        type = type_monster; //monster type
        name = "Green Slime";
        speed = 1;
        maxLife = 5;
        life = maxLife;
        attack = 1;
        defense = 0;
        exp = 2;
        solidArea = new Rectangle(4, 15, 50, 47);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
    }

    public void getImage(){
        up1 = setup("/monster/slime/walk/slime_walk_0");
        up2 = setup("/monster/slime/walk/slime_walk_1");
        down1 = setup("/monster/slime/walk/slime_walk_2");
        down2 = setup("/monster/slime/walk/slime_walk_3");
        left1 = setup("/monster/slime/walk/slime_walk_4");
        left2 = setup("/monster/slime/walk/slime_walk_5");
        right1 = setup("/monster/slime/walk/slime_walk_6");
        right2 = setup("/monster/slime/walk/slime_walk_7");
    }

    @Override
    public void setAction() {
        actionLockCounter++;

        if(actionLockCounter == 40){
            Random rand = new Random();
            int i = rand.nextInt(100) + 1;

            if(i<=25) {
                direction = "up"; //up
            }
            if(i > 25 && i <=50){
                direction = "down"; //down
            }
            if(i > 50 && i <= 75){
                direction = "left"; // left
            }
            if(i > 75){
                direction = "right"; // right
            }
            actionLockCounter = 0;
        }

    }

    @Override
    public void update() {
        setAction();
        CollisionOn = false;

        gp.cChecker.checkTile(this);
        gp.cChecker.checkOBJ(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.type == 2 && contactPlayer){
            if (!gp.player.isInvincible){
                gp.player.life -= 1;
                gp.player.isInvincible = true;
            }
        }

        if (!CollisionOn) {
            switch (direction) {
                case "up":
                    if(spriteNum == 4 || spriteNum == 5 || spriteNum == 6) {
                        //     direction = maintain;
                        worldY += speed + 1;
                    }
                    break;
                case "down":
                    if(spriteNum == 4 || spriteNum == 5 || spriteNum == 6) {
                        //   direction = maintain;
                        worldY -= speed+ 1;
                    }

                case "left":
                    if(spriteNum == 4 || spriteNum == 5 || spriteNum == 6) {
                        //   direction = maintain;
                        worldX -= speed+ 1;
                    }

                    break;
                case "right":
                    if(spriteNum == 4 || spriteNum == 5 || spriteNum == 6) {
                        //   direction = maintain;
                        worldX += speed+ 1;
                    }

                    break;
                case "default":
                    break;
            }
        }
        spriteCounter++;

        if (spriteCounter > 8) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 3;
            } else if (spriteNum == 3) {
                spriteNum = 4;
            } else if (spriteNum == 4) {
                spriteNum = 5;
            } else if (spriteNum == 5) {
                spriteNum = 6;
            } else if (spriteNum == 6) {
                spriteNum = 7;
            } else if (spriteNum == 7) {
                spriteNum = 8;
            } else {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if (isInvincible){
            invincibleCounter++;
            if (invincibleCounter > 30){
                isInvincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void damageReaction(){
        actionLockCounter = 0;
        direction = gp.player.direction;
    }

}
