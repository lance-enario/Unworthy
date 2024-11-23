package Enemies;

import Entity.Entity;
import Main.GamePanel;
import objects.obj_Coin;
import objects.obj_Potion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Orc extends Entity{
    GamePanel gp;

    public Orc(GamePanel gp){
        super(gp);

        this.gp = gp;

        defaultSpeed = 2;
        speed = defaultSpeed;
        type = type_monster; //monster type
        name = "Orc";
        maxLife = 6;
        life = maxLife;
        attack = 2;
        defense = 1;
        exp = 5;
        solidArea = new Rectangle(10, 20, 50, 60);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage(){
        up1 = setup("/monster/orc/walk/orc_walking_0");
        up2 = setup("/monster/orc/walk/orc_walking_1");
        down1 = setup("/monster/orc/walk/orc_walking_2");
        down2 = setup("/monster/orc/walk/orc_walking_3");
        left1 = setup("/monster/orc/walk/orc_walking_4");
        left2 = setup("/monster/orc/walk/orc_walking_5");
        right1 = setup("/monster/orc/walk/orc_walking_2");
        right2 = setup("/monster/orc/walk/orc_walking_3");
    }
    public void getAttackImage(){
//        attackUp1 = setup("/monster/orc/walk/orc_attack_3");
//        attackUp1 = setup("/monster/orc/walk/orc_attack_3");
//        attackDown1 = setup("/monster/orc/walk/orc_attack_3");
//        attackDown2 = setup("/monster/orc/walk/orc_attack_3");
//        attackLeft1 = setup("/monster/orc/walk/orc_attack_3");
//        attackLeft2 = setup("/monster/orc/walk/orc_attack_3");
//        attackRight1 = setup("/monster/orc/walk/orc_attack_3");
//        attackRight2 = setup("/monster/orc/walk/orc_attack_3");
    }

    @Override
    public void setAction() {
        if(onPath){
            int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;
            searchPath(goalCol,goalRow);
        } else {
            if(onPath){
//            int goalCol = 78;
//            int goalRow = 22;

                int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
                int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;
                searchPath(goalCol,goalRow);
            } else {
                actionLockCounter++;

                if(actionLockCounter == 40){
                    Random rand = new Random();
                    int i = rand.nextInt(100) + 1;
                    if(i <= 25){
                        direction = "up";
                    }
                    if ( i > 25 && i <= 50){
                        direction = "down";
                    }
                    if(i > 50 && i <=75 ){
                        direction = "left"; // left
                    }
                    if(i > 75 && i <= 100 ){
                        direction = "right"; // right
                    }
                    actionLockCounter = 0;
                }
            }
        }

//        if(isAttacking){
//            checkAttackOrNot(30,gp.tileSize*4,gp.tileSize);
//        }

    }

    @Override
    public void damageReaction(){
        actionLockCounter = 0;
        //direction = gp.player.direction;
        onPath = true;
    }

    @Override
    public void update() {
        super.update();

        int xDistance = Math.abs(worldX-gp.player.worldX);
        int yDistance = Math.abs(worldY-gp.player.worldY);
        int tileDistance = (xDistance+yDistance)/gp.tileSize;

        if(!onPath && tileDistance < 5){
            int i = new Random().nextInt(100)+1;
            if(i > 50) onPath = true;
        }

        if(onPath && tileDistance > 20) onPath = false;
        setAction();
        collisionOn = false;
    }
    public void checkDrop(){
        int i = new Random().nextInt(100)+1;
        if(i < 50){
            dropItem(new obj_Potion(gp));
        }
        if(i > 50){
            dropItem(new obj_Coin(gp));
        }
    }


    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (direction) {
                case "default", "up", "down", "left", "right", "upleft", "upright", "downleft", "downright":
                    if(spriteNum == 1) image = up1;
                    if(spriteNum == 2) image = up2;
                    if(spriteNum == 3) image = down1;
                    if(spriteNum == 4) image = down2;
                    if(spriteNum == 5) image = left1;
                    if(spriteNum == 6) image = left2;
                    if(spriteNum == 7) image = right1;
                    if(spriteNum == 8) image = right2;
                    break;
            }

            //MONSTER HP BAR
            if(type == 2 && hpBarOn) {
                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;

                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX-1,screenY-16,gp.tileSize+2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;

                if(hpBarCounter > 300) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if(isInvincible){
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2,0.4f);
            }

            if(isDying){
                dyingAnimation(g2);
            }

            boolean shouldFlip = direction.equals("left") ||
                    (direction.equals("up") && maintain.equals("left")) ||
                    (direction.equals("down") && maintain.equals("left"));


            if (shouldFlip) g2.drawImage(image, (screenX + gp.tileSize), screenY, -gp.tileSize-40, gp.tileSize+40, null);
            else g2.drawImage(image, screenX, screenY, gp.tileSize+40, gp.tileSize+40, null);

            changeAlpha(g2,1.0f);

            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

}
