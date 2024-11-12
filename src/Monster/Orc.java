package Monster;

import Entity.Entity;
import Main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Orc extends Entity{
    GamePanel gp;

    public Orc(GamePanel gp){
        super(gp);

        this.gp = gp;

        type = type_monster; //monster type
        name = "Orc";
        speed = 2;
        maxLife = 6;
        life = maxLife;
        attack = 2;
        defense = 1;
        exp = 5;
        solidArea = new Rectangle(3, 18, 32, 75);
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
        right1 = setup("/monster/orc/walk/orc_walking_6");
        right2 = setup("/monster/orc/walk/orc_walking_7");
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
            //MONSTER HP BAR
            if(type == 2 && hpBarOn) {
                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;

                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX-1,screenY-16,gp.tileSize+2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;

                if(hpBarCounter > 600){
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
                g2.drawImage(image, screenX  + gp.tileSize + 5, screenY, -gp.tileSize - 30, gp.tileSize+30, null);
            } else {
                g2.drawImage(image, screenX - 29,  screenY, gp.tileSize+30, gp.tileSize+30, null);
            }

            changeAlpha(g2,1f);

            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

    @Override
    public void damageReaction(){
        actionLockCounter = 0;
        direction = gp.player.direction;
    }

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
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
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
            } else{
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


}
