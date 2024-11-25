package Enemies;

import Entity.Entity;
import Entity.Projectile;
import Main.GamePanel;
import Main.UtilityTool;
import objects.obj_Coin;
import objects.obj_LichSkill;
import objects.obj_Potion;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Boss_King extends Entity{
    GamePanel gp;

    //base frames for Sirius
    BufferedImage[] walkFrames = new BufferedImage[8];
    BufferedImage[] idleFrames = new BufferedImage[8];
    BufferedImage[] bscAttackFrames = new BufferedImage[8];
    BufferedImage[] transformFrames = new BufferedImage[8];

    //ult frames for Sirius
    BufferedImage[] ultWalkFrames = new BufferedImage[6];
    BufferedImage[] ultIdleFrames = new BufferedImage[6];
    BufferedImage[] ultBscAttackFrames = new BufferedImage[7];

    // ATTACK INTERVALS
    int attackInterval = 120;
    int skill1Interval = 180;
    int skill2Interval = 0;
    int skill3Interval = 0;

    // BOOLEAN
    boolean isCasting = false;

    public static final String king = "King Sirius";

    public Boss_King(GamePanel gp){
        super(gp);

        this.gp = gp;

        defaultSpeed = 3;
        speed = defaultSpeed;
        type = type_boss; //monster type
        name = king;
        maxLife = 200;
        life = maxLife;
        attack = 5;
        defense = 0;
        exp = 200;
        solidArea = new Rectangle(32, 92, 150, 200);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void skill1(){

    }

    public void getImage(){
        for (int i = 0; i < 8; i++) {
            bscAttackFrames[i] = setup("/Boss/pretrans_attack/pretrans_attack_" + i);
            walkFrames[i] = setup("/Boss/pretrans_walk/pretrans_walk_" + i);
            idleFrames[i] = setup("/Boss/pretrans_idle/pretrans_idle_" + i);
            ultWalkFrames[i] = setup("/Boss/trans_walk/trans_walk_" + i);
            ultIdleFrames[i] = setup("/Boss/trans_idle/trans_idle_" + i);
            ultBscAttackFrames[i] = setup("/Boss/trans_attack/trans_attack_" + i);
            transformFrames[i] = setup("/Boss/pretrans_transform/transformation_" + i);
        }
    }

    @Override
    public void setAction() {
        // If the Lich King is within casting range, start casting
        if (getTileDistance(gp.player) < 2) {
            spriteNum = 1; // Reset sprite animation for casting
            spriteCounter = 0;
        }
    }

    @Override
    public void update() {

        if (attackInterval < 120){
            attackInterval++;
        }

        super.update();
    }


    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (worldX + gp.tileSize * 6 > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize * 6 > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            // Normal movement sprites
            switch (direction) {
                case "up" -> image = spriteNum == 1 ? up1 : up2;
                case "down" -> image = spriteNum == 1 ? down1 : down2;
                case "left" -> image = spriteNum == 1 ? left1 : left2;
                case "right" -> image = spriteNum == 1 ? right1 : right2;
            }
        }

            //MONSTER HP BAR
            if(type == type_monster || type == type_boss && hpBarOn) {
                double oneScale = (double) gp.tileSize/maxLife;
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
                g2.drawImage(image, screenX + gp.tileSize * 3, screenY, -gp.tileSize*3, gp.tileSize*6, null);
            } else {
                g2.drawImage(image, screenX, screenY, gp.tileSize*3, gp.tileSize*6, null);
            }

            changeAlpha(g2,1f);

            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }


    @Override
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

    public int getDetected(Entity user, Entity target[][], String targetName) {
        int index = 999;

        int col = user.getCol();
        int row = user.getRow();

        // Checking all four directions (up, down, left, right) for target
        for (int i = 0; i < 4; i++) {
            int checkCol = col;
            int checkRow = row;

            if (i == 0) checkRow -= 1; // Up
            if (i == 1) checkRow += 1; // Down
            if (i == 2) checkCol -= 1; // Left
            if (i == 3) checkCol += 1; // Right

            // Loop through all objects in the current map
            for (int j = 0; j < target[1].length; j++) {
                if (target[gp.currentMap][j] != null &&
                        target[gp.currentMap][j].getCol() == checkCol &&
                        target[gp.currentMap][j].getRow() == checkRow &&
                        target[gp.currentMap][j].name.equals(targetName)) {
                    index = j;
                    return index;
                }
            }
        }
        return index;
    }
    public int getCenterX() {
        return centerX = worldX + left1.getWidth() / 2;
    }

    public int getCenterY() {
        return centerY =  worldY + up1.getHeight() / 2;
    }

    @Override
    public int getXDistance(Entity target){
        int xDistance = Math.abs(getCenterY() - target.worldX);
        return xDistance;
    }

    @Override
    public int getYDistance(Entity target) {
        int yDistance = Math.abs(getCenterX() - target.worldY);
        return yDistance;
    }

    public int getTileDistance(Entity target){
        return (getXDistance(target) + getYDistance(target))/gp.tileSize;
    }
    public int getGoalCol(Entity target){
        return (target.worldX + target.solidArea.x)/gp.tileSize;
    }
    public int getGoalRow(Entity target){
        return (target.worldY + target.solidArea.y)/gp.tileSize;
    }

    @Override
    public void checkStopChasingOrNot(Entity target, int distance, int rate){
        if(getTileDistance(target) > distance)
        {
            isAttacking = false;
            onPath = false;
        }
    }

    @Override
    public void checkStartChasingOrNot(Entity target, int distance, int rate){
        if(getTileDistance(target) < distance)
        {
            onPath = true;
        }
    }

    @Override
    public void checkShootOrNot(int rate, int shotInterval){
        int i = new Random().nextInt(rate);
        if(i == 0 && !projectile.isAlive && shotAvailableCounter == shotInterval){
            projectile.set(worldX, worldY, direction, true, this);

            //check vacancy
            for(int ii = 0; i < gp.projectile[1].length; ii++){
                if(gp.projectile[gp.currentMap][ii] == null){
                    gp.projectile[gp.currentMap][ii] = projectile;
                    break;
                }
            }

        }

    }

    @Override
    public void getRandomDirection(){
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

    public void checkCastOrNot(int rate, int straight, int horizontal){
        boolean targetInRange = false;
        int xDis = getXDistance(gp.player);
        int yDis = getYDistance(gp.player);

        switch(direction){
            case "up":
                if(gp.player.worldY< getCenterY() && yDis < straight && xDis < horizontal)
                    targetInRange = true;
                break;
            case "down":
                if(gp.player.worldY > getCenterY() && yDis < straight && xDis < horizontal)
                    targetInRange = true;
                break;
            case "left":
                if(gp.player.worldX < getCenterY() && xDis < straight && yDis < horizontal)
                    targetInRange = true;
                break;
            case "right":
                if(gp.player.worldX > getCenterY() && xDis < straight && yDis < horizontal)
                    targetInRange = true;
                break;
        }

        if(targetInRange){
            // Calculate the distance to the player
            int distanceToPlayer = getTileDistance(gp.player);

            // If the player is within 2 tiles, allow the attack
            if (distanceToPlayer <= 2) {
                int i = new Random().nextInt(rate);
                if(i == 0){
                    isCasting = true;
                    spriteNum = 1;
                    spriteCounter = 0;
                    shotAvailableCounter = 0;
                }
            } else {
                // If the player is more than 2 tiles away, stop attacking
                isCasting = false;
                //System.out.println("Player is too far away to attack.");
            }
        } else {
            // If the player is not in range, stop attacking
            isCasting = false;

        }
    }

    @Override
    public void attacking(){
        int currentWorldX = worldX;
        int currentWorldY = worldY;
        int solidAreaWidth = solidArea.width;
        int solidAreaHeight = solidArea.height;
        if(gp.cChecker.checkPlayer(this)){
            damagePlayer(attack);
        }

        switch(direction){
            case "up":
                worldY -= attackArea.height;
                break;
            case "down":
                worldY += attackArea.height;
                break;
            case "left":
                worldX -= attackArea.width;
                break;
            case "right":
                worldX += attackArea.width;
                break;
        }

        if(type == type_monster){
            if(gp.cChecker.checkPlayer(this)){
                damagePlayer(attack);
            }
        } else {
            //check monster collision on hit
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            gp.player.damageMonster(monsterIndex, this, attack, currentWeapon.knockbackPower);

            int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
            gp.player.damageProjectile(projectileIndex);
        }

        worldX = currentWorldX;
        worldY = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;
    }

    @Override
    public void setknockback(Entity target, Entity attacker, int knockbackPower){

        this.attacker = attacker;
        target.knockBackDirection = attacker.direction;
        target.speed += knockbackPower;
        target.knockback = true;
    }

    public void moveTowardPlayer(int interval){
        actionLockCounter++;

        if (actionLockCounter > interval){

            if (getXDistance(gp.player) > getYDistance(gp.player)){
                if (gp.player.worldX < getCenterX()){
                    direction = "left";
                } else {
                    direction = "right";
                }
            } else if (getXDistance(gp.player) < getYDistance(gp.player)){
                if (gp.player.worldY < getCenterY()){
                    direction = "up";
                } else {
                    direction = "down";
                }
            }
            actionLockCounter = 0;
        }

    }


    @Override
    public BufferedImage setup(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize*4);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return image;
    }




















}





