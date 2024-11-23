package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import objects.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Mage extends Player{

    //graphic images for player
    BufferedImage[] walkFrames = new BufferedImage[6];
    BufferedImage[] idleFrames = new BufferedImage[6];
    BufferedImage[] bscAttackFrames = new BufferedImage[7];

    //ult frames for player
    BufferedImage[] ultWalkFrames = new BufferedImage[6];
    BufferedImage[] ultIdleFrames = new BufferedImage[6];
    BufferedImage[] ultBscAttackFrames = new BufferedImage[7];

    //temporary frames
    BufferedImage[] tempWalkFrames = new BufferedImage[6];
    BufferedImage[] tempIdleFrames = new BufferedImage[6];
    BufferedImage[] tempBscAttackFrames = new BufferedImage[7];

    //other frames
    BufferedImage[] ultTransformFrames = new BufferedImage[25];
    BufferedImage[] shieldFrames = new BufferedImage[18];

    //boolean
    boolean skill3Pressed = false;
    boolean skill2Pressed = false;
    boolean resetFrames = false;

    //mage cooldowns
    public int mageSkill1Counter = 180;
    public int mageSkill2Counter = 600;
    public int mageSkill3Counter = 1500;

    //ult duration
    private int ultCounter = 0;
    private int transformationSpriteNum = 1;
    private int transformationSpriteCounter = 0;
    private int frameCtr = 1;

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
        worldX = gp.tileSize * 14; //spawn point    Stage 1 = 19,41     Stage2 = 14,47     Stage 3 = 49,47
        worldY = gp.tileSize * 47;
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
                    ultWalkFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/ult/walk ulted/mage_ult_walk" + (i + 1) + ".png")));
                    ultIdleFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/ult/idle ulted/mage_ult_idle" + (i + 1) + ".png")));
                }
                for (int i = 0; i < 7; i++) {
                    bscAttackFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/bscAttack/" + (i + 1) + ".png")));
                    ultBscAttackFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/ult/bsc attack ulted/mage_ult_basic_attack" + (i + 1) + ".png")));
                }
                for (int i = 0; i < 16; i++) {
                    shieldFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/skill2/" + "shield" + (i + 1) + ".png")));
                }
                for (int i = 0; i < 25; i++) {
                    ultTransformFrames[i] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Mage/ult/transformation ulted/transformation" + (i + 1) + ".png")));
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
            } else if (keyH.skill3Pressed && mageSkill3Counter == 1500){
                mageSkill3Counter = 0;
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
            collisionOn = false;
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
            if (!collisionOn && !keyH.enterPressed) {
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
            } else if (keyH.skill3Pressed && mageSkill3Counter == 1500){
                mageSkill3Counter = 0;
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

        if (transformationSpriteCounter > 3) {
            if (transformationSpriteNum == frameCtr){
                transformationSpriteNum++;
                frameCtr++;
            } else {
                transformationSpriteNum = 25;
            }
            transformationSpriteCounter = 0;
        }

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

        if (skill3Pressed){ //change sprite for player while ult is occurring
            transformationSpriteCounter++;
            ultCounter++;
            if (ultCounter == 1){
                tempWalkFrames = walkFrames;
                tempIdleFrames = idleFrames;
                tempBscAttackFrames = bscAttackFrames;

                walkFrames = ultWalkFrames;
                idleFrames = ultIdleFrames;
                bscAttackFrames = ultBscAttackFrames;

            } else if (ultCounter > 600){
                skill3Pressed = false;
                gp.stopMusic();
                walkFrames = tempWalkFrames;
                idleFrames = tempIdleFrames;
                bscAttackFrames = tempBscAttackFrames;
                ultCounter = 0;
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

        if(mageSkill3Counter < 1500){
            mageSkill3Counter++;
        }

    }

    public void mageSkill1() {
        String[] directions = {"up", "down", "left", "right", "upleft", "upright", "downleft", "downright"};
        for (int i = 0; i < directions.length; i++) {
            Projectile proj = new obj_MageSkill1(gp);
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

    public void mageSkill2(){
        //isInvincible = true;
        gp.playSE(20);
        skill2Pressed = true;
        shieldCtr = 1;
        shieldSpriteNum = 1;
    }

    public void mageSkill3(){
        skill3Pressed = true;
        gp.playMusic(28);
        frameCtr = 1;
        transformationSpriteNum = 1;
    }

    @Override
    public void attacking(){

        if (gp.keyH.bscAtkPressed && shotAvailableCounter == 30) {
            Projectile newProjectile = new obj_MageAttack(gp);
            if (skill3Pressed){
                Projectile sideproj1 = new obj_MageAttack(gp);
                Projectile sideproj2 = new obj_MageAttack(gp);

                switch(direction){
                    case "up":
                        sideproj1.set(worldX, worldY, "upleftleft", true, this);
                        sideproj2.set(worldX, worldY, "uprightright", true, this);
                        break;
                    case "down":
                        sideproj1.set(worldX, worldY, "downleftleft", true, this);
                        sideproj2.set(worldX, worldY, "downrightright", true, this);
                        break;
                    case "left":
                        sideproj1.set(worldX, worldY, "leftupleft", true, this);
                        sideproj2.set(worldX, worldY, "leftdownleft", true, this);
                        break;
                    case "right":
                        sideproj1.set(worldX, worldY, "rightdownright", true, this);
                        sideproj2.set(worldX, worldY, "rightupright", true, this);
                        break;
                }

                for(int i = 0; i < gp.projectile[1].length; i++){
                    if(gp.projectile[gp.currentMap][i] == null){
                        gp.projectile[gp.currentMap][i] = sideproj1;
                        break;
                    }
                }

                for(int i = 0; i < gp.projectile[1].length; i++){
                    if(gp.projectile[gp.currentMap][i] == null){
                        gp.projectile[gp.currentMap][i] = sideproj2;
                        break;
                    }
                }
            }
            newProjectile.set(worldX, worldY, direction, true, this);
            shotAvailableCounter = 0;
            //gp.projectileList.add(newProjectile);

            for(int i = 0; i < gp.projectile[1].length; i++){
                if(gp.projectile[gp.currentMap][i] == null){
                    gp.projectile[gp.currentMap][i] = newProjectile;
                    break;
                }
            }
            gp.playSE(18);
        }
    }

    public void draw (Graphics2D g2) {
        BufferedImage image = idleFrames[0];
        BufferedImage shieldImage;
        shieldImage = shieldFrames[(shieldSpriteNum - 1) % shieldFrames.length];

        if (skill3Pressed && transformationSpriteNum < 25) {
            image = ultTransformFrames[transformationSpriteNum % ultTransformFrames.length];
        } else if (isAttacking) {
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


