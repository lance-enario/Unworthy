package Main;

import Entity.Entity;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        if (entity.solidArea == null) {
            System.out.println("Error: Entity solidArea is null.");
            return;  // Exit the method if solidArea is null
        }

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.CollisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.CollisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.CollisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.CollisionOn = true;
                }
                break;
        }
    }


    public int checkOBJ(Entity entity, boolean player){
        int index = 999;

        int i;
        for(i = 0; i<gp.obj.length; i++){
            if(gp.obj[i] != null){
                //get entity posi
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                //get obj posi
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch (entity.direction){
                    case "up":
                       entity.solidArea.y -= entity.speed;
                       if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                           if(gp.obj[i].collision){
                               entity.CollisionOn = true;
                           }
                           if(player){
                               index = i;
                           }
                        }
                       break;
                   case "down":
                       entity.solidArea.y += entity.speed;
                       if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                           if(gp.obj[i].collision){
                               entity.CollisionOn = true;
                           }
                           if(player){
                               index = i;
                           }
                   }
                       break;
                   case "left":
                       entity.solidArea.x -= entity.speed;
                       if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                           if(gp.obj[i].collision){
                               entity.CollisionOn = true;
                           }
                           if(player){
                               index = i;
                           }
                   }
                       break;
                   case "right":
                       entity.solidArea.x += entity.speed;
                       if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                           if(gp.obj[i].collision){
                               entity.CollisionOn = true;
                           }
                           if(player){
                               index = i;
                           }
                   }
                       break;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }
        
        return index;
    }

    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;

        for (int i = 0; i < target.length; i++) {
            if (target[i] != null && target[i].solidArea != null) {
                // Store the original positions of the entity and target solid areas
                int originalEntitySolidAreaX = entity.solidArea.x;
                int originalEntitySolidAreaY = entity.solidArea.y;
                int originalTargetSolidAreaX = target[i].solidArea.x;
                int originalTargetSolidAreaY = target[i].solidArea.y;

                // Set entity solidArea position based on world coordinates
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                // Set target solidArea position based on world coordinates
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                // Check collision based on direction
                switch (entity.direction) {
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

                // Check for collision
                if (entity.solidArea.intersects(target[i].solidArea)) {
                    entity.CollisionOn = true;
                    System.out.println("Collision detected!");
                    index = i;
                }

                // Reset solid areas to their original positions
                entity.solidArea.x = originalEntitySolidAreaX;
                entity.solidArea.y = originalEntitySolidAreaY;
                target[i].solidArea.x = originalTargetSolidAreaX;
                target[i].solidArea.y = originalTargetSolidAreaY;
            }
        }
        return index;
    }

    public void checkPlayer(Entity entity){
        //get entity posi
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        //get obj posi
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.CollisionOn = true;
                    System.out.println("Collision above!");
                }

                break;
            case "down":
                entity.solidArea.y += entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.CollisionOn = true;
                    System.out.println("Collision below!");

                }

                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.CollisionOn = true;
                    System.out.println("Collision left!");

                }

                break;
            case "right":
                entity.solidArea.x += entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.CollisionOn = true;
                    System.out.println("Collision right!");

                }
                break;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
    }

}
