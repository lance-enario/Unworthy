package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import Main.Sound;
import objects.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Player extends Entity {

    KeyHandler keyH;
    Sound sound;
    public final int screenX;
    public final int screenY;
    public BufferedImage[] walkFrames = new BufferedImage[6];
    public BufferedImage[] idleFrames = new BufferedImage[6];
    BufferedImage[] bscAttackFrames = new BufferedImage[7];
    int hasKey =0 ;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp); // setter for gp
        this.keyH = keyH;   //setter for keyH

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // (30, 75, 45, 40)
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
        coins = 0;
        currentWeapon = new obj_Wand(gp);
        currentShield = new obj_Book(gp);
        attack = getAttack();   // total attack value is decided by strength and weapon.
        defense = getDefense(); // total defense value is decided by dexterity and shield.

        direction = "left";
        maintain = "right";
        isAttacking = false;
        projectile = new obj_MageAttack(gp);
    }

    public void setItems(){
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new obj_Key(gp));
        inventory.add(new obj_Key(gp));
        inventory.add(new obj_Potion(gp));
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //@Override
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
        } else if (keyH.skill1Pressed || keyH.skill2Pressed || keyH.skill3Pressed){
            if (keyH.skill1Pressed && mageSkill1Counter == 180) {
                mageSkill1();
                mageSkill1Counter = 0;
            } else if (keyH.skill2Pressed){
                mageSkill2();
            } else {
                mageSkill3();
            }

        } else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            //SKILLS
            if (keyH.skill1Pressed && mageSkill1Counter == 180) {
                mageSkill1();
                mageSkill1Counter = 0;
            } else if (keyH.skill2Pressed){
                mageSkill2();
            } else if (keyH.skill3Pressed){
                mageSkill3();
            }

            //MOVEMENT
            if (keyH.upPressed) {
                direction = "up";
                System.out.println("up");
            } else if (keyH.downPressed) {
                direction = "down";
                System.out.println("down");
            } else if (keyH.leftPressed) {
                direction = "left";
                maintain = direction;
                System.out.println("left");
            } else if (keyH.rightPressed) {
                direction = "right";
                maintain = direction;
                System.out.println("right");
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

        } else {

            int npcDialogue = gp.cChecker.checkDialogue(this, gp.npc[gp.currentMap]);
            interactNPC(npcDialogue);

            gp.keyH.enterPressed = false;
        }
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

        // this code snippet handles invincibility
        if (isInvincible){
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

    }

//    public void mageSkill1(){
//        Projectile projUp = new obj_MageAttack(gp);
//        Projectile projDown = new obj_MageAttack(gp);
//        Projectile projLeft = new obj_MageAttack(gp);
//        Projectile projRight = new obj_MageAttack(gp);
//
//        Projectile projUpLeft = new obj_MageAttack(gp);
//        Projectile projUpRight = new obj_MageAttack(gp);
//        Projectile projDownLeft = new obj_MageAttack(gp);
//        Projectile projDownRight = new obj_MageAttack(gp);
//
//        projUp.set(worldX, worldY, "up", true, this);
//        projDown.set(worldX, worldY, "down", true, this);
//        projLeft.set(worldX, worldY, "left", true, this);
//        projRight.set(worldX, worldY, "right", true, this);
//
//        projUpLeft.set(worldX, worldY, "upleft", true, this);
//        projUpRight.set(worldX, worldY, "upright", true, this);
//        projDownLeft.set(worldX, worldY, "downleft", true, this);
//        projDownRight.set(worldX, worldY, "downright", true, this);
//
//        gp.projectileList.add(projUp);
//        gp.projectileList.add(projDown);
//        gp.projectileList.add(projLeft);
//        gp.projectileList.add(projRight);
//
//        gp.projectileList.add(projUpLeft);
//        gp.projectileList.add(projUpRight);
//        gp.projectileList.add(projDownLeft);
//        gp.projectileList.add(projDownRight);
//        gp.playSE(3);
//    }

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

        if (gp.keyH.bscAtkPressed && shotAvailableCounter == 30){
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

    public void pickUpOBJ(int i) {
        if(i != 999){
            String text;
            if(inventory.size() != maxInventorySize){
                inventory.add(gp.obj[gp.currentMap][i]);
                // Need sounds
                text = "You have picked up a " + gp.obj[gp.currentMap][i].name + "!";
            } else{
                text = "You cannot carry anymore stuff!";
            }
            gp.ui.showMessage(text);
            gp.obj[gp.currentMap][i] = null;
        }
    }

        public void interactNPC(int i) {
             if(i!=999){
                 if(gp.keyH.enterPressed){
                     gp.gameState = gp.dialogueState;
                     gp.npc[gp.currentMap][i].speak();
                 }
             }
             gp.keyH.enterPressed = false;
        }

        public void contactMonster(int i){
            if(i!=999){
                if (!isInvincible){

                    int damage = gp.monster[gp.currentMap][i].attack - defense;
                    if(damage < 0){
                        damage = 0;
                    }

                    life -= damage;
                    isInvincible = true;
                }
            }
        }

    public void damageMonster(int i, int attack){
        if (i != 999){
            if(!gp.monster[gp.currentMap][i].isInvincible){

                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if(damage < 0){
                    damage = 0;
                }
                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.showMessage(damage + "damage!");
                gp.monster[gp.currentMap][i].isInvincible = true;
                gp.monster[gp.currentMap][i].damageReaction();

                if(gp.monster[gp.currentMap][i].life <= 0){
                    gp.monster[gp.currentMap][i].isDying = true;
                    gp.playSE(2);
                    gp.ui.showMessage("Killed the " + gp.monster[gp.currentMap][i].name + "!");
                    gp.ui.showMessage("Exp + " + gp.monster[gp.currentMap][i].exp);
                    exp += gp.monster[gp.currentMap][i].exp;
                    checkLevelUp();
                }
            }
        }
    }

    public void checkLevelUp(){
        if(exp >= nextLevelExp){
            level++;
            nextLevelExp = nextLevelExp*2; // Up to us to decide whats the exp for nextlevelup
            maxLife += 2;
            strength++;
            dexterity++;
            attack++;
            attack = getAttack();
            defense = getDefense();

            //need sound effect;
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You leveled up to " + level + "!\n" + "You are now stronger than you are before";
        }
    }
    public void selectItem(){
        int itemIndex = gp.ui.getItemIndexOnSlot();

        if(itemIndex < inventory.size()){
            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_sword || selectedItem.type == type_shield){
                currentWeapon = selectedItem;
                attack = getAttack();
            }
            if(selectedItem.type == type_shield){
                currentShield = selectedItem;
                defense = getDefense();
            }
            if(selectedItem.type == type_consumable){
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }
    }
    public void draw (Graphics2D g2) {
        BufferedImage image = idleFrames[0];

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

        if (shouldFlip) {
            g2.drawImage(image, (screenX + gp.playerSize+30), screenY, -(gp.playerSize+30), gp.playerSize+30, null);
        } else {
            g2.drawImage(image, screenX, screenY, gp.playerSize+30, gp.playerSize+30, null);
        }

        // reset composite
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        //visible collision checker, just cross out if not needed
        g2.setColor(Color.green);
        g2.drawRect(screenX + DialogueArea.x,screenY + DialogueArea.y, DialogueArea.width, DialogueArea.height);
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }

}


