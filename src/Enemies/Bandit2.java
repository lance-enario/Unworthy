package Enemies;

import Entity.Entity;
import Main.GamePanel;
import objects.obj_Coin;
import objects.obj_Potion;
import objects.obj_Projectile;
import objects.obj_arrow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Bandit2 extends Entity {
    GamePanel gp;


    public Bandit2(GamePanel gp){
        super(gp);

        this.gp = gp;

        type = type_monster; //monster type
        name = "Bandit2";
        speed = 2;
        maxLife = 5;
        life = maxLife;
        attack = 1;
        defense = 0;
        exp = 2;
        projectile = new obj_arrow(gp);

        solidArea = new Rectangle(10, 40, 40, 53);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
    }



    public void getImage(){
        up1 = setup("/Enemies/Bandit2/bandit2_walk_0");
        up2 = setup("/Enemies/Bandit2/bandit2_walk_1");
        down1 = setup("/Enemies/Bandit2/bandit2_walk_2");
        down2 = setup("/Enemies/Bandit2/bandit2_walk_3");
        left1 = setup("/Enemies/Bandit2/bandit2_walk_4");
        left2 = setup("/Enemies/Bandit2/bandit2_walk_5");
        right1 = setup("/Enemies/Bandit2/bandit2_walk_6");
        right2 = setup("/Enemies/Bandit2/bandit2_walk_7");
    }

    @Override
    public void setAction() {
        if(onPath){
//            int goalCol = 78;
//            int goalRow = 22;

            int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;
            searchPath(goalCol,goalRow);

            int i = new Random().nextInt(100)+1;
            if(i > 99 && !projectile.isAlive && shotAvailableCounter == 30){
                projectile.set(worldX, worldY, direction, true, this);
                for(int j = 0; j < gp.projectile[1].length; j++){
                    if(gp.projectile[gp.currentMap][j] == null){
                        gp.projectile[gp.currentMap][j] = projectile;
                        break;
                    }
                }
                shotAvailableCounter = 0;
            }
        } else {
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

    }

    @Override
    public void update() {
        super.update();

        int xDistance = Math.abs(worldX-gp.player.worldX);
        int yDistance = Math.abs(worldY-gp.player.worldY);
        int tileDistance = (xDistance+yDistance)/gp.tileSize;

        if(onPath && tileDistance > 10){
            onPath = false;
        }

        if(!onPath && tileDistance < 5){
            int i = new Random().nextInt(100)+1;
            if(i > 50) onPath = true;
        }

        if(onPath && tileDistance > 20) onPath = false;
        setAction();
        CollisionOn = false;
    }

    public void damageReaction(){
        actionLockCounter = 0;
        //direction = gp.player.direction;
        onPath = true;
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

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (direction) {
                case "default", "up", "down", "left", "right":
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
            //Enemies HP BAR
            if(type == 2 && hpBarOn) {
                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;

                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX-1,screenY-16,gp.tileSize+2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;

                if(hpBarCounter > 600) {
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
                    (direction.equals("down") && maintain.equals("left")) ||
                    (direction.equals("default") && maintain.equals("left"));

            if (shouldFlip) {
                g2.drawImage(image, screenX + gp.tileSize, screenY, -gp.tileSize-30, gp.tileSize+30, null);
            } else {
                g2.drawImage(image, screenX, screenY, gp.tileSize+30, gp.tileSize+30, null);
            }

            changeAlpha(g2,1f);

            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }


}

