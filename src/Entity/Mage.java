package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import objects.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Mage extends Player{

    //graphic images for player
    BufferedImage[] walkFrames = new BufferedImage[6];
    BufferedImage[] idleFrames = new BufferedImage[6];
    BufferedImage[] bscAttackFrames = new BufferedImage[7];
    BufferedImage[] shieldFrames = new BufferedImage[18];

    //player skill counters
    public int mageSkill1Counter = 180;
    public int mageSkill2Counter = 600;
    boolean skill2Pressed = false;

    //shield sprite variables
    public int shieldSpriteNum = 1;
    public int shieldSpriteCounter = 0;
    public int shieldCtr = 1;

    public Mage(GamePanel gp, KeyHandler keyH) {

        super(gp, keyH);          // setter for gp

        DialogueArea = new Rectangle(13, 40, 100, 100);
        solidArea = new Rectangle(45,95, 33, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackArea.width = 50;
        attackArea.height = 24;

        setDefaultValues();
        getPlayerImage();
        setItems();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 19; //spawn point    Stage 1 = 19,41     Stage2 = 14,47     Stage 3 = 49,47
        worldY = gp.tileSize * 41;
        speed = 15; // 3 default but increased just for testing

        // PLAYER STATUS
        maxLife = 10;
        life = maxLife;
        level = 1;
        strength = 1;   // the more strength he has, the more damage he gives
        dexterity = 1;  // the more dexterity he has, the less damage he receives.
        exp = 0;
        nextLevelExp = 30;
        coins = 100;
        currentWeapon = new obj_Wand(gp);
        currentShield = new obj_Book(gp);
        attack = getAttack();   // total attack value is decided by strength and weapon.
        defense = getDefense(); // total defense value is decided by dexterity and shield.

        direction = "left";
        maintain = "right";
        isAttacking = false;
        projectile = new obj_MageAttack(gp);
    }

    public int getAttack(){
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage() {
        try {
            for (int i = 0; i < 6; i++) {
                walkFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/walk/" + (i + 1) + ".png")));
                idleFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/idle/Idle" + (i + 1) + ".png")));
            }
            for (int i = 0; i < 7; i++) {
                bscAttackFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/bscAttack/" + (i + 1) + ".png")));
            }
            for (int i = 0; i < 16; i++) {
                shieldFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/skill2/" + "shield"+ (i + 1) + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update() {
        if (isAttacking || keyH.bscAtkPressed) {
            isAttacking = true;
            attacking();

            attackCounter++;
            if (attackCounter > 30) {
                keyH.bscAtkPressed = false;
                isAttacking = false;
                attackCounter = 0;
            }
        }  else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            //SKILLS
            if (keyH.skill1Pressed && mageSkill1Counter == 180) {       //skill1Counter is the cooldown. If 3 seconds have passed, skill becomes available
                mageSkill1();
                mageSkill1Counter = 0;
            } else if (keyH.skill2Pressed && mageSkill2Counter == 600){ //If 10 seconds have passed, skill becomes available
                mageSkill2();
                mageSkill2Counter = 0;
            } else if (keyH.skill3Pressed){
                mageSkill3();
            }

            //MOVEMENT
            if (keyH.upPressed) {
                direction = "up";
                //System.out.println("up");
            } else if (keyH.downPressed) {
                direction = "down";
                //System.out.println("down");
            } else if (keyH.leftPressed) {
                direction = "left";
                maintain = direction;
                //System.out.println("left");
            } else if (keyH.rightPressed) {
                direction = "right";
                maintain = direction;
                //System.out.println("right");
            }

            //collision checker
            CollisionOn = false;
            gp.cChecker.checkTile(this);

            //obj checker
            int objIDX = gp.cChecker.checkOBJ(this, true);
            pickUpOBJ(objIDX);

            //check npc collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            //check event
            gp.eHandler.checkEvent();

            //if collision != true, player can move
            if (!CollisionOn && !keyH.enterPressed) {
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

            if(audioCounter < 20){
                audioCounter++;
            } else {
                gp.playSE(1);
                audioCounter = 0;
            }

            gp.keyH.enterPressed = false;

        } else if (keyH.skill1Pressed || keyH.skill2Pressed || keyH.skill3Pressed){
            if (keyH.skill1Pressed && mageSkill1Counter == 180) {
                mageSkill1();
                mageSkill1Counter = 0;
            } else if (keyH.skill2Pressed && mageSkill2Counter == 600){
                mageSkill2();
                mageSkill2Counter = 0;
            } else {
                mageSkill3();
            }

        } else {

            int npcDialogue = gp.cChecker.checkDialogue(this, gp.npc[gp.currentMap]);
            interactNPC(npcDialogue);

            gp.keyH.enterPressed = false;
        }

        //HANDLES SPRITE FRAME BY FRAME CHANGES FOR PLAYER
        spriteCounter++;

        if (isAttacking){
            spriteNum = 5;
        }

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
            } else {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        System.out.println("shieldSpriteNum = " + shieldSpriteNum + " " + "shieldCtr = " + shieldCtr);
        if (shieldSpriteCounter > 1) {
            if (shieldSpriteNum == shieldCtr && shieldSpriteNum < 16){
                shieldSpriteNum++;
                shieldCtr++;
            } else {
                shieldSpriteNum = 16;
            }
            shieldSpriteCounter = 0;
        }

        //HANDLES SHIELD FRAMES FOR PLAYER
        if (skill2Pressed) {
            invincibleCounter++;
            if(invincibleCounter == 30){
                isInvincible = true;
            }
            shieldSpriteCounter++;
        }

        // this code snippet handles invincibility
        if (isInvincible && skill2Pressed) {
            invincibleCounter++;
            if (invincibleCounter > 300) {
                isInvincible = false;
                skill2Pressed = false;
                invincibleCounter = 0;
            }
        } else if (isInvincible){
            invincibleCounter++;
            if (invincibleCounter > 60) {
                isInvincible = false;
                invincibleCounter = 0;
            }
        }

        if(shotAvailableCounter < 30){
            shotAvailableCounter++;
        }

        if(mageSkill1Counter < 180){
            mageSkill1Counter++;
        }

        if(mageSkill2Counter < 600){
            mageSkill2Counter++;
        }

    }

    public void mageSkill1() {
        String[] directions = {"up", "down", "left", "right", "upleft", "upright", "downleft", "downright"};
        for (int i = 0; i < directions.length; i++) {
            Projectile proj = new obj_MageSkill1(gp);
            proj.set(worldX, worldY, directions[i], true, this);
            gp.projectileList.add(proj);
        }
        gp.playSE(19);
    }

    public void mageSkill2(){
        //isInvincible = true;
        gp.playSE(20);
        skill2Pressed = true;
        shieldCtr = 1;
        shieldSpriteNum = 1;
    }

    public void mageSkill3(){

    }

    public void attacking(){
        int currentWorldX = worldX;
        int currentWorldY = worldY;
        int solidAreaWidth = solidArea.width;
        int solidAreaHeight = solidArea.height;

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

        if (gp.keyH.bscAtkPressed && shotAvailableCounter == 30) {
            Projectile newProjectile = new obj_MageAttack(gp);
            newProjectile.set(worldX, worldY, direction, true, this);
            shotAvailableCounter = 0;
            gp.projectileList.add(newProjectile);
            gp.playSE(18);
        }

        solidArea.width = attackArea.width;
        solidArea.height = attackArea.height;

        //check monster collision on hit
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        damageMonster(monsterIndex, attack);

        worldX = currentWorldX;
        worldY = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;
    }

    public void draw (Graphics2D g2) {
        BufferedImage image = idleFrames[0];
        BufferedImage shieldImage;
        shieldImage = shieldFrames[(shieldSpriteNum - 1) % shieldFrames.length];

        if (isAttacking) {
            image = bscAttackFrames[spriteNum % bscAttackFrames.length]; // Use modulo to prevent index out of bounds
        } else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){
            image = switch (direction) {
                case "left", "right", "up", "down" ->
                        walkFrames[(spriteNum - 1) % walkFrames.length]; // Walk animation frame
                default -> idleFrames[0]; // Fallback to first idle frame if direction is unrecognized
            };
        } else {
            image = idleFrames[(spriteNum - 1) % idleFrames.length];// Idle animation frame
        }

        if (skill2Pressed){
            g2.drawImage(shieldImage, screenX + 3, screenY + 25, gp.playerSize+30, gp.playerSize+30, null);
        }

        // visual confirmation of invincible state
        if (isInvincible){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        }

        boolean shouldFlip = false;

        if (direction.equals("left") ||
                (direction.equals("up") && maintain.equals("left")) ||
                (direction.equals("down") && maintain.equals("left")) ||
                (direction.equals("default") && maintain.equals("left")))
        {
            shouldFlip = true;
        }

        if (shouldFlip)
            g2.drawImage(image, (screenX + gp.playerSize+30), screenY, -(gp.playerSize+30), gp.playerSize+30, null);
        else
            g2.drawImage(image, screenX, screenY, gp.playerSize+30, gp.playerSize+30, null);

        // reset composite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        //visible collision checker, just cross out if not needed
        g2.setColor(Color.green);
        g2.drawRect(screenX + DialogueArea.x,screenY + DialogueArea.y, DialogueArea.width, DialogueArea.height);
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }
}


