package Enemies;

import Entity.Entity;
import Entity.Projectile;
import Main.GamePanel;
import Main.UtilityTool;
import objects.obj_BossProj;
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
    BufferedImage[] ultWalkFrames = new BufferedImage[8];
    BufferedImage[] ultIdleFrames = new BufferedImage[8];
    BufferedImage[] ultBscAttackFrames = new BufferedImage[8];

    // ATTACK INTERVALS
    int attackInterval = 120;
    int skill1Interval = 180;
    int skill2Interval = 0;
    int skill3Interval = 0;


    int transformCounter = 0;
    // BOOLEAN
    boolean isTransformed = false;
    boolean isDead = false;

    public static final String king = "King Sirius";

    public Boss_King(GamePanel gp){
        super(gp);

        this.gp = gp;

        defaultSpeed = 1;
        speed = defaultSpeed;
        type = type_boss; //monster type
        name = king;
        maxLife = 100;
        life = maxLife;
        attack = 5;
        defense = 0;
        exp = 200;
        solidArea = new Rectangle(32, 64, 64, 96);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }


    public void skill2() {
        String[] directions = {"up", "down", "left", "right", "upleft", "upright", "downleft", "downright"};
        for (int i = 0; i < directions.length; i++) {
            Projectile proj = new obj_BossProj(gp);
            proj.set(worldX, worldY, directions[i], true, this);
            for(int j = 0; j < gp.projectile[1].length; j++){
                if(gp.projectile[gp.currentMap][j] == null){
                    gp.projectile[gp.currentMap][j] = proj;
                    break;
                }
            }
        }
        gp.playSE(19);
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

        if(onPath) {
            //check if it stops chasing
            checkStopChasingOrNot(gp.player,15,100);

            //search direction to go
            searchPath(getGoalCol(gp.player),getGoalRow(gp.player));
        } else {
            //check if it starts chasing
            checkStartChasingOrNot(gp.player,5,100);
            //get a random direction
            getRandomDirection();
        }

        if(!isAttacking){
            checkAttackOrNot(30,gp.tileSize*4,gp.tileSize);
        }
    }

    @Override
    public void update() {

        if (life < 50){
            isTransformed = true;
            transformCounter++;
        }

        if (life <= 0){
            isDead = true;
        }

        if (isTransformed && attackInterval == 120){
            skill2();
            attackInterval = 0;
        }

        if (knockback){
            checkCollision();

            if (collisionOn){
                knockbackCounter = 0;
                knockback = false;
                speed = defaultSpeed;
            } else {
                switch(knockBackDirection){
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
                }

                knockbackCounter++;
                if (knockbackCounter == 10){
                    knockbackCounter = 0;
                    knockback = false;
                    speed = defaultSpeed;
                }
            }
        } else if(isAttacking){
            attacking();
            checkStopChasingOrNot(gp.player,1,100);
        } else {
            setAction();
            checkCollision();
        }

        if (isInvincible){
            invincibleCounter++;
            if (invincibleCounter > 30){
                isInvincible = false;
                invincibleCounter = 0;
            }
        }

        if (!collisionOn) {
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
            }
        }

        if (attackInterval < 120){
            attackInterval++;
        }

        spriteCounter++;

        if (spriteCounter > 6) {
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
            }  else if (spriteNum == 7) {
                spriteNum = 8;
            }  else {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

    }


    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage image = idleFrames[0];

//        if (isTransformed && transformCounter == 8) {
//            image = ultTransformFrames[transformationSpriteNum % ultTransformFrames.length];
//        }

        if (!isTransformed) {
            if (isAttacking) {
                image = bscAttackFrames[spriteNum % bscAttackFrames.length]; // Use modulo to prevent index out of bounds
            } else if (direction.equals("left") || direction.equals("right") || direction.equals("down") || direction.equals("up")) {
                image = switch (direction) {
                    case "left", "right", "up", "down" -> walkFrames[spriteNum % walkFrames.length]; // Walk animation frame
                    default -> idleFrames[0]; // Fallback to first idle frame if direction is unrecognized
                };
            } else {
                image = idleFrames[spriteNum % idleFrames.length];// Idle animation frame
            }
        } else if (isTransformed && transformCounter < 60) {
            image = transformFrames[spriteNum % transformFrames.length];
        } else {
            if (isAttacking) {
                image = ultBscAttackFrames[spriteNum % bscAttackFrames.length]; // Use modulo to prevent index out of bounds
            } else if (direction.equals("left") || direction.equals("right") || direction.equals("down") || direction.equals("up")) {
                image = switch (direction) {
                    case "left", "right", "up", "down" -> ultWalkFrames[spriteNum % walkFrames.length]; // Walk animation frame
                    default -> idleFrames[0]; // Fallback to first idle frame if direction is unrecognized
                };
            } else {
                image = ultIdleFrames[spriteNum % idleFrames.length];// Idle animation frame
            }
        }
        // visual confirmation of invincible state
        if (isTransparent){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
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
                g2.drawImage(image, screenX + gp.tileSize*2, screenY, -gp.tileSize*2, gp.tileSize*2, null);
            } else {
                g2.drawImage(image, screenX, screenY, gp.tileSize*2, gp.tileSize*2, null);
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
        return centerX = worldX + bscAttackFrames[0].getWidth() / 2;
    }

    public int getCenterY() {
        return centerY =  worldY + bscAttackFrames[0].getHeight() / 2;
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
                    isAttacking = true;
                    spriteNum = 1;
                    spriteCounter = 0;
                    shotAvailableCounter = 0;
                }
            } else {
                // If the player is more than 2 tiles away, stop attacking
                isAttacking = false;
                //System.out.println("Player is too far away to attack.");
            }
        } else {
            // If the player is not in range, stop attacking
            isAttacking = false;

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





