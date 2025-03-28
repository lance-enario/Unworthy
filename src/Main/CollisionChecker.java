package Main;

import Entity.Entity;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        //use temp direction when knockbacked
        String direction = entity.direction;
        if(entity.knockback){
            direction = entity.knockBackDirection;
        }

        switch (direction){
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }


    public int checkOBJ(Entity entity, boolean player){
        int index = 999;

        int i;
        for(i = 0; i<gp.obj[1].length; i++){
            if(gp.obj[gp.currentMap][i] != null){
                //get entity position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                //get obj position
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y;

                //use temp direction when knockbacked
                String direction = entity.direction;
                if(entity.knockback){
                    direction = entity.knockBackDirection;
                }

                switch (direction){
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if(entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)){
                            if(gp.obj[gp.currentMap][i].collision){
                                entity.collisionOn = true;
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if(entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)){
                            if(gp.obj[gp.currentMap][i].collision){
                                entity.collisionOn = true;
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if(entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)){
                            if(gp.obj[gp.currentMap][i].collision){
                                entity.collisionOn = true;
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)){
                            if(gp.obj[gp.currentMap][i].collision){
                                entity.collisionOn = true;
                            }
                            if(player){
                                index = i;
                            }
                        }
                        break;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;
                gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY;
            }
        }

        return index;
    }

    public int checkEntity(Entity entity, Entity[][] target) {
        int index = 999;

        for (int i = 0; i < target[1].length; i++) {
            if (target[gp.currentMap][i] != null){ //&& target[i].solidArea != null) {

                // Set entity solidArea position based on world coordinates
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Set target solidArea position based on world coordinates
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;

                //use temp direction when knockbacked
                String direction = entity.direction;
                if(entity.knockback){
                    direction = entity.knockBackDirection;
                }

                switch (direction){
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                    case "default":
                        // No movement, so directly check current position for collision
                        break;
                }

                // Check for collision without considering direction
                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                    if(target[gp.currentMap][i] != entity){
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }

    // this function checks if the player can interact with an npc
    public int checkDialogue(Entity player, Entity[] npc){
        int index = 999;

        for (int i = 0; i < npc.length; i++) {
            if (npc[i] != null && npc[i].solidArea != null) {

                int DialogueAreaDefaultX = player.DialogueArea.x;
                int DialogueAreaDefaultY = player.DialogueArea.y;

                // Set entity solidArea position based on world coordinates
                player.DialogueArea.x = player.worldX + player.DialogueArea.x;
                player.DialogueArea.y = player.worldY + player.DialogueArea.y;
                // Set target solidArea position based on world coordinates
                npc[i].solidArea.x = npc[i].worldX + npc[i].solidArea.x;
                npc[i].solidArea.y = npc[i].worldY + npc[i].solidArea.y;

                switch (player.direction) {
                    case "up": player.DialogueArea.y -= player.speed; break;
                    case "down": player.DialogueArea.y += player.speed; break;
                    case "left": player.DialogueArea.x -= player.speed; break;
                    case "right": player.DialogueArea.x += player.speed; break;
                }

                if (player.DialogueArea.intersects(npc[i].solidArea)){
                    System.out.println("Can interact!");
                    index = i;
                }

                player.DialogueArea.x = DialogueAreaDefaultX;
                player.DialogueArea.y = DialogueAreaDefaultY;
                npc[i].solidArea.x = npc[i].solidAreaDefaultX;
                npc[i].solidArea.y = npc[i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public boolean checkPlayer(Entity entity){

        boolean contactPlayer = false;

        //get entity position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        //get obj position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up": entity.solidArea.y -= entity.speed; break;
            case "down": entity.solidArea.y += entity.speed; break;
            case "left": entity.solidArea.x -= entity.speed; break;
            case "right": entity.solidArea.x += entity.speed; break;
        }

        // Check for collision without considering direction
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }

}
