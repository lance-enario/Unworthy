package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;

public class Entity {
    public int worldX, worldY;
    public String direction = "up";
    public String maintain = "right";
    public boolean isAlive = true;
    public boolean isDying = false;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public int centerX,centerY;
    public BufferedImage[] idleFrames;
    public int audioCounter = 0;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public String[][] dialogues = new String[20][20];
    public Entity attacker;

    //STATE
    public boolean hpBarOn = false;
    public boolean onPath = false;
    public boolean collisionOn = false;
    public boolean isInvincible = false;
    public boolean isAttacking = false;
    public boolean knockback = false;
    public String knockBackDirection;
    public boolean isTransparent = false;
    public int dialogueSet = 0;

    //placeholder area lines for collision & dialogue check
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public Rectangle solidArea = new Rectangle(32,56, 64, 64);
    public Rectangle DialogueArea = new Rectangle(32,56, 64, 64);

    public int solidAreaDefaultX, solidAreaDefaultY;


    //COUNTER
    public int invincibleCounter = 0;
    public int actionLockCounter = 0;
    public int hpBarCounter = 0;
    public int attackCounter = 30;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    int knockbackCounter = 0;


    public String[] dialogue = new String[20];
    public int dialogueIndex = 0;

    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;

    // CHARACTER ATTRIBUTES
    public int speed;
    public int defaultSpeed;
    public int maxLife;
    public int life;
    public int level;
    public int attack;
    public Projectile projectile;
    public int strength;
    public int dexterity;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coins;
    public Entity currentWeapon;
    public Entity currentShield;

    // ITEM ATTRIBUTES
    public ArrayList<Entity> inventory = new ArrayList<>();
    public  final int maxInventorySize = 20;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int value;
    public int price;
    public boolean stackable = false;
    public int amount = 1;
    public int knockbackPower = 0;

    //TYPE
    public int type; // 0 = player, 1 = npc, 2 = monster
    public final int type_player = 0;
    public final int type_boss = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_shield = 4;
    public final int type_wand = 5;
    public final int type_bow = 6;
    public final int type_obstacle = 7;
    public final int type_consumable = 8;
    public final int type_pickUpOnly = 9;
    public final int type_sign = 10;
    //public final int type_armor = 7;

    GamePanel gp;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }
    public int getLeftX(){
        return worldX + solidArea.x;
    }
    public int getRightX(){
        return worldX + solidArea.width;
    }
    public int getTopY(){
        return worldY + solidArea.y;
    }
    public int getBottomY(){
        return worldY + solidArea.y + solidArea.height;
    }
    public int getCol(){
        return (worldX + solidArea.x)/gp.tileSize;
    }
    public int getRow(){
        return (worldY + solidArea.y)/gp.tileSize;
    }

    public void setAction(){}

    public void damageReaction(){}

    public void interact(){}

    public void speak(){
        if(dialogue[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogue[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction){
            case "up":  direction = "down"; break;
            case "left": direction = "right"; break;
            case "down": direction = "up"; break;
            case "right": direction = "left"; break;
        }
    }

    public void startDialogue(Entity entity, int setNum){
        gp.gameState = gp.dialogueState;
        gp.ui.npc = entity;
        dialogueSet = setNum;

        switch (gp.player.direction){
            case "up":  direction = "down"; break;
            case "left": direction = "right"; break;
            case "down": direction = "up"; break;
            case "right": direction = "left"; break;
        }
    }
    public boolean use(Entity entity){
        return false;
    }
    public void checkDrop(){}

    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < gp.obj[1].length; i++) {
            if (gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX;
                gp.obj[gp.currentMap][i].worldY = worldY;
                break;
            }
        }
    }

    public void checkCollision(){
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkOBJ(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this, gp.signs);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.type == type_monster && contactPlayer){
            if (!gp.player.isInvincible){

                int damage  = attack - gp.player.defense;
                if(damage < 0){
                    damage = 0;
                }

                gp.player.life -= damage;
                gp.player.isInvincible = true;
            }
        }
    }

    public Color getParticleColor(){
        Color color = null;
        return color;
    }

    public int getParticleSize(){
        int size = 0;
        return size;
    }

    public int getParticleSpeed(){
        int speed = 0;
        return speed;
    }

    public int getParticleMaxLife(){
        int maxLife = 0;
        return maxLife;
    }

    public void generateParticle(Entity generator, Entity target){
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(gp, generator,color,size,speed,maxLife,-2,-1);
        Particle p2 = new Particle(gp, generator,color,size,speed,maxLife,2,-1);
        Particle p3 = new Particle(gp, generator,color,size,speed,maxLife,-2,1);
        Particle p4 = new Particle(gp, generator,color,size,speed,maxLife,2,1);
        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);
    }

    public void update() {
        //System.out.println(gp.ui.numberofdeadppl);
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
            } else if (spriteNum == 6){
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

    public void damagePlayer(int attack) {
        if (!gp.player.isInvincible) {

            int damage = attack - gp.player.defense;
            if (damage < 0) {
                damage = 0;
            }

            if(damage != 0){
                gp.player.isTransparent = true;
                setknockback(gp.player, this, knockbackPower);
            }

            gp.player.life -= damage;
            gp.player.isInvincible = true;

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
                case "default", "up", "down", "left", "right",
                     "upleft", "upright", "downleft", "downright",
                     "upleftleft", "downleftleft", "uprightright", "downrightright",
                     "leftupleft", "leftdownleft", "rightupright", "rightdownright":
                    if (spriteNum == 1) {
                        if (isAttacking) {
                            image = attackUp1;
                        }
                        if (!isAttacking) {
                            image = up1;
                        }
                    }
                    if (spriteNum == 2) {
                        if (isAttacking) {
                            image = attackUp2;
                        } if(!isAttacking) {
                            image = up2;
                        }

                    }
                    if (spriteNum == 3) {
                        if (isAttacking) {
                            image = attackDown1;
                        } if(!isAttacking) {
                            image = down1;
                        }

                    }
                    if(spriteNum == 4) {
                        if (isAttacking) {
                            image = attackDown2;
                        } if(!isAttacking) {
                            image = down2;
                        }

                    }
                    if(spriteNum == 5) {
                        if (isAttacking) {
                            image = attackLeft1;
                        } if(!isAttacking) {
                            image = left1;
                        }
                    }
                    if(spriteNum == 6) {
                        if (isAttacking) {
                            image = attackLeft2;
                        } if(!isAttacking) {
                            image = left2;
                        }
                    }
                    if(spriteNum == 7) {
                        if (isAttacking) {
                            image = attackRight1;
                        } if(!isAttacking) {
                            image = right1;
                        }
                    }
                    if (spriteNum == 8) {
                        if (isAttacking) {
                            image = attackRight2;
                        } if(!isAttacking) {
                            image = right2;
                        }
                    }
                    break;
            }

            //MONSTER HP BAR
            if ((type == type_monster || type == type_boss) && hpBarOn) {
                double oneScale = (double) gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

                hpBarCounter++;

                if (hpBarCounter > 300) {
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

            if (type == type_boss){
                if (shouldFlip) {
                    g2.drawImage(image, (screenX + gp.tileSize * 5
                    ), screenY, -(gp.tileSize*5), gp.tileSize*5, null);
                } else {
                    g2.drawImage(image, screenX, screenY, gp.tileSize*5, gp.tileSize*5, null);
                }
            } else if (type == 11) { //projectile size
                if (shouldFlip) {
                    g2.drawImage(image, (screenX + 32), screenY, -(gp.tileSize-32), gp.tileSize-32, null);
                } else {
                    g2.drawImage(image, screenX, screenY, gp.tileSize-32, gp.tileSize-32, null);
                }
            } else if(type == 10){
                g2.drawImage(image, screenX, screenY - 22, gp.tileSize, gp.tileSize + 20, null);
            } else {
                if (shouldFlip) {
                    g2.drawImage(image, (screenX + gp.tileSize), screenY, -gp.tileSize, gp.tileSize, null);
                } else {
                    g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }

            changeAlpha(g2,1.0f);

            g2.setColor(Color.red);
            g2.drawRect(screenX+ solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

    public void dyingAnimation(Graphics2D g2){
        dyingCounter++;
        int i = 5;
        if(dyingCounter <= i){changeAlpha(g2,0f);}
        if(dyingCounter > i && dyingCounter <= i*2){changeAlpha(g2,1f);}
        if(dyingCounter > i*2 && dyingCounter <= i*3){changeAlpha(g2,0f);}
        if(dyingCounter > i*3 && dyingCounter <= i*4){changeAlpha(g2,1f);}
        if(dyingCounter > i*4 && dyingCounter <= i*5){changeAlpha(g2,0f);}
        if(dyingCounter > i*5 && dyingCounter <= i*6){changeAlpha(g2,1f);}
        if(dyingCounter > i*6 && dyingCounter <= i*7){changeAlpha(g2,0f);}
        if(dyingCounter > i*7 && dyingCounter <= i*8){changeAlpha(g2,1f);}
        if(dyingCounter > i*8){
            isDying = false;
            isAlive = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public BufferedImage setup(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return image;
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

    public void searchPath(int goalCol,int goalRow){
        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pFinder.setNodes(startCol,startRow,goalCol,goalRow);

        if(gp.pFinder.search() == true){
            //next worldX && worldY

            int nextX = gp.pFinder.pathlist.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathlist.get(0).row * gp.tileSize;

            //entity's solid area position
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY =  worldY + solidArea.y + solidArea.height;

            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize){
                direction = "up";
            }
            else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize){
                direction = "down";
            }
            else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize){
                //can go left or right
                if(enLeftX > nextX){
                    direction ="left";
                }
                if(enLeftX < nextX){
                    direction = "right";
                }
            }
            else if (enTopY > nextY && enLeftX > nextX){
                //up or left
                direction = "up";

                checkCollision();
                if(collision){
                    direction = "left";
                }
            }
            else if (enTopY > nextY && enLeftX < nextX){
                direction = "up";
                checkCollision();
                if(collision){
                    direction = "right";
                }
            }
            else if (enTopY < nextY && enLeftX > nextX){
                direction = "down";
                checkCollision();
                if(collision){
                    direction = "left";
                }
            }
            else if (enTopY < nextY && enLeftX < nextX){
                direction = "down";
                checkCollision();
                if(collision){
                    direction = "right";
                }
            }

//            int nextCol = gp.pFinder.pathlist.get(0).col;
//            int nextRow = gp.pFinder.pathlist.get(0).row;
//            if(nextCol == goalCol && nextRow == goalRow){
//                onPath = false;
//            }
        }
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
    public void checkStopChasingOrNot(Entity target, int distance, int rate){
        if(getTileDistance(target) > distance)
        {
            isAttacking = false;
            onPath = false;
        }

    }
    public void checkStartChasingOrNot(Entity target, int distance, int rate){
        if(getTileDistance(target) < distance)
        {
            onPath = true;
        }
    }
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

    public void checkAttackOrNot(int rate, int straight, int horizontal){
        boolean targetInRange = false;
        int xDis = getXDistance(gp.player);
        int yDis = getYDistance(gp.player);

        switch(direction){
            case "up":
                if(gp.player.worldY < worldY && yDis < straight && xDis < horizontal)
                    targetInRange = true;
                break;
            case "down":
                if(gp.player.worldY > worldY && yDis < straight && xDis < horizontal)
                    targetInRange = true;
                break;
            case "left":
                if(gp.player.worldX < worldX && xDis < straight && yDis < horizontal)
                    targetInRange = true;
                break;
            case "right":
                if(gp.player.worldX  > worldX && xDis < straight && yDis < horizontal)
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
            }
        } else {
            // If the player is not in range, stop attacking
            isAttacking = false;

        }
    }

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

    public void setknockback(Entity target, Entity attacker, int knockbackPower){

        this.attacker = attacker;
        target.knockBackDirection = attacker.direction;
        target.speed += knockbackPower;
        target.knockback = true;
    }

    public int getYDistance(Entity target) {
        int yDistance = Math.abs(worldY - target.worldY);
        return yDistance;
    }
    public int getXDistance(Entity target){
        int xDistance = Math.abs(worldX - target.worldX);
        return xDistance;
    }
}