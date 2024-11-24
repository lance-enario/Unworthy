package Entity;

import Main.GamePanel;
import Main.KeyHandler;
import objects.*;

import java.awt.*;
import java.util.Random;

public class Player extends Entity {

    KeyHandler keyH;
    public final int screenX;
    public final int screenY;

    Random rand = new Random();
    int max=4,min=1;

    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);          // setter for gp
        this.keyH = keyH;   //setter for keyH

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // (30, 75, 45, 40)
        //DialogueArea = new Rectangle(13, 40, 100, 100);
        //solidArea = new Rectangle(40,80, 33, 32);
        //solidAreaDefaultX = solidArea.x;
        //solidAreaDefaultY = solidArea.y;
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 19; //spawn point    Stage 1 = 19,41     Stage2 = 14,47     Stage 3 = 49,47      Dungeon = 73,14
        worldY = gp.tileSize * 41;
        defaultSpeed = 6;
        speed = defaultSpeed;

        // PLAYER STATUS
        maxLife = 10;
        life = maxLife;
        level = 1;
        strength = 1;   // the more strength he has, the more damage he gives
        dexterity = 1;  // the more dexterity he has, the less damage he receives.
        exp = 0;
        nextLevelExp = 10;
        coins = 0;
        attack = getAttack();   // total attack value is decided by strength and weapon.
        defense = getDefense(); // total defense value is decided by dexterity and shield.

        direction = "right";
        maintain = "right";
        isAttacking = false;
        projectile = new obj_MageAttack(gp);
    }

    public void setItems(){
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
    }

    public int getAttack(){
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage() {}

    //@Override
    public void update() {

        if (knockback) {

            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkOBJ(this, true);
            gp.cChecker.checkEntity(this, gp.npc);
            gp.cChecker.checkEntity(this, gp.signs);
            gp.cChecker.checkEntity(this, gp.monster);


            if (collisionOn) {
                knockbackCounter = 0;
                knockback = false;
                speed = defaultSpeed;
            } else {
                switch (gp.player.direction) {
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


                knockbackCounter++;
                if (knockbackCounter == 10) {
                    knockbackCounter = 0;
                    knockback = false;
                    speed = defaultSpeed;
                }
            //System.out.println("X: " + worldX/gp.tileSize + " " + "Y: " + worldY/gp.tileSize);
        }else if (isAttacking || keyH.bscAtkPressed) {
            isAttacking = true;
            attacking();

            attackCounter++;
            if (attackCounter > 30) {
                keyH.bscAtkPressed = false;
                isAttacking = false;
                attackCounter = 0;
            }
        }  else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

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

            int signDialogue = gp.cChecker.checkEntity(this, gp.signs);
            interactSign(signDialogue);

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

            if(audioCounter < 60){
                audioCounter++;
            } else {
                int num = rand.nextInt((max - min + 1) + min);

                switch(num){
                    case 1:
                        gp.playSE(24);
                        break;
                    case 2:
                        gp.playSE(25);
                        break;
                    case 3:
                        gp.playSE(26);
                        break;
                    case 4:
                        gp.playSE(27);
                        break;
                }
                audioCounter = 0;
            }

            gp.keyH.enterPressed = false;

        } else {

            int npcDialogue = gp.cChecker.checkDialogue(this, gp.npc[gp.currentMap]);
            interactNPC(npcDialogue);

            int signDialogue = gp.cChecker.checkDialogue(this, gp.signs[gp.currentMap]);
            interactSign(signDialogue);

            gp.keyH.enterPressed = false;
        }

        if (isInvincible){
            invincibleCounter++;
            if (invincibleCounter > 60) {
                isInvincible = false;
                isTransparent= false;
                invincibleCounter = 0;
            }
        }
        if (life > maxLife){
            life = maxLife;
        }
        if(life <= 0){
            gp.playSE(29);
            gp.gameState = gp.gameOverState;
        }
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

        solidArea.width = attackArea.width;
        solidArea.height = attackArea.height;

        //check monster collision on hit
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        damageMonster(monsterIndex, this, attack, currentWeapon.knockbackPower);

        int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
        damageProjectile(projectileIndex);

        worldX = currentWorldX;
        worldY = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;
    }

    public void pickUpOBJ(int i) {
        if(i != 999){
            if(gp.obj[gp.currentMap][i].type == type_pickUpOnly){
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
            }
            if (gp.obj[gp.currentMap][i] != null) {
                // para OBSTACLE
                if (gp.obj[gp.currentMap][i].type == type_obstacle) {
                    if (keyH.enterPressed) {

                        gp.obj[gp.currentMap][i].interact();
                    }
                } else { // para INVENTORY
                    String text;
                    if (canObtainItem(gp.obj[gp.currentMap][i])) {
                        // Need sounds
                        text = "You have picked up a " + gp.obj[gp.currentMap][i].name + "!";
                    } else {
                        text = "You cannot carry anymore stuff!";
                    }
                    gp.ui.showMessage(text);
                    gp.obj[gp.currentMap][i] = null;
                }
            }
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

    public void interactSign(int i) {
        if(i!=999){
            if(gp.keyH.enterPressed){
                gp.gameState = gp.dialogueState;
                gp.signs[gp.currentMap][i].speak();
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
                    isTransparent = false;
                }
            }
        }

    public void damageMonster(int i, Entity attacker, int attack, int knockbackPower){
        if (i != 999){
            if(!gp.monster[gp.currentMap][i].isInvincible){
                gp.playSE(22);

                if (knockbackPower > 0) {
                    setknockback(gp.monster[gp.currentMap][i], attacker, knockbackPower);
                }

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



    public void damageProjectile(int i){
        if(i != 999){
            Entity projectile = gp.projectile[gp.currentMap][i];
            projectile.isAlive = false;
            generateParticle(projectile, projectile);
        }
    }

    public void checkLevelUp(){
        if(exp >= nextLevelExp){
            level++;
            nextLevelExp = nextLevelExp*2; // Up to us to decide what's the exp for nextlevelup
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
        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

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
                if(selectedItem.use(this)) {
                    if(selectedItem.amount > 1){
                        selectedItem.amount--;
                    }
                    else {
                        inventory.remove(itemIndex);
                    }
                }
            }
        }
    }

    public int searchItemInInventory(String itemName){
        int itemIndex = 999;

        for(int i = 0 ; i < inventory.size(); i++){
            if(inventory.get(i).name.equals(itemName)){
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    public boolean canObtainItem(Entity item){

        boolean isObtain = false;

        //CHECK IF STACKABLE
        if(item.stackable){
            int index = searchItemInInventory(item.name);

            if(index != 999){
                inventory.get(index).amount++;
                isObtain = true;
            } else { // New item so need to check vacancy
                if(inventory.size() != maxInventorySize){
                    inventory.add(item);
                    isObtain = true;
                }
            }
        }
        else{ // NOT STACKABLE so check vacancy
            if(inventory.size() != maxInventorySize){
                inventory.add(item);
                isObtain = true;
            }
        }
        return isObtain;
    }



    public void draw (Graphics2D g2) {

    }
}


