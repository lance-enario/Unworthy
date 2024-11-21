package Main;

import Entity.Entity;
import Tiles.TileManager;

import java.awt.*;

public class EventHandler {

    GamePanel gp;
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;

    int previousEventY;
    int previousEventX;
    boolean canTouchEvent = true;


    int tempMap,tempCol,tempRow;

    public EventHandler(GamePanel gp){
        this.gp = gp;

        eventRect = new EventRect();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 25;
        eventRect.height = 25;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent(){
        //Check if the player is more than 1 tile away from the last event
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > gp.tileSize){
            canTouchEvent = true;
        }

        if(canTouchEvent){
//            if(hit(0,25, 42, "any") ) {
//                damagePit(gp.dialogueState);
//            }
            // 0 to 1
            if (canTouchEvent) {
                //dungeon hole
                if (hit(3, 72, 25, "any") ||
                        hit(3, 73, 25, "any") ||
                        hit(3, 74, 25, "any") ||
                        hit(3, 75, 25, "any") ||
                        hit(3, 76, 25, "any") ||
                        hit(3, 77, 25, "any")) {
                    damagePit(gp.dialogueState);
                }
                if (hit(3, 72, 26, "any") ||
                        hit(3, 73, 26, "any") ||
                        hit(3, 74, 26, "any") ||
                        hit(3, 75, 26, "any") ||
                        hit(3, 76, 26, "any") ||
                        hit(3, 77, 26, "any")) {
                    damagePit(gp.dialogueState);
                }
                //dungeon hole2
                if (hit(3, 65, 17, "any") ||
                        hit(3, 66, 17, "any") ||
                        hit(3, 67, 17, "any") ||
                        hit(3, 68, 17, "any") ||
                        hit(3, 69, 17, "any")) {
                    damagePit(gp.dialogueState);
                }
                if (hit(3, 65, 16, "any") ||
                        hit(3, 66, 16, "any") ||
                        hit(3, 67, 16, "any") ||
                        hit(3, 68, 16, "any") ||
                        hit(3, 69, 16, "any")) {
                    damagePit(gp.dialogueState);
                }
                // 0 to 1
                if (hit(0, 83, 41, "any") ||
                        hit(0, 83, 42, "any") ||
                        hit(0, 83, 43, "any")) {
                    teleport(1, 14, 47, gp.dialogueState, "Travelling to the Village");

                }
                // 1 to 0
                else if (hit(1, 12, 48, "any") ||
                        hit(1, 12, 49, "any") ||
                        hit(1, 13, 47, "any") ||
                        hit(1, 12, 47, "any")) {
                    teleport(0, 81, 40, gp.dialogueState, "Leaving the village");

                }

                // 1 to 2
                else if (hit(1, 49, 10, "any") ||
                        hit(1, 50, 10, "any")) {
                    teleport(2, 49, 91, gp.dialogueState, "Entering the castle");


                }

                // 2 to 1
                else if (hit(2, 49, 93, "any") ||
                        hit(2, 50, 93, "any")) {
                    teleport(1, 49, 11, gp.dialogueState, "Exiting the castle");


                }
                // dungeon to 1
                else if (hit(3, 74, 15, "any")) {
                    teleport(1, 79, 88, gp.dialogueState, "Exiting the dungeon");


                }
                //1 to dungeon
                else if (hit(1, 80, 90, "any") ||
                        hit(1, 79, 90, "any")) {
                    teleport(3, 73, 14, gp.dialogueState, "Entering the dungeon");


                }

                if (hit(0, 5, 5, "any")) {
                    damagePit(gp.dialogueState);
                }

                else if(hit(1,12,9, "up") == true) {speak(gp.npc[1][0]);}
            }
        }
    }

    public boolean hit(int map,int col ,int row, String reqDirection){
        boolean hit = false;
        if (gp.currentMap == map){
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect.x = col * gp.tileSize + eventRect.x;
            eventRect.y = row * gp.tileSize + eventRect.y;

            if(gp.player.solidArea.intersects(eventRect)){
                if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")){
                    hit = true;

                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect.x = eventRectDefaultX;
            eventRect.y = eventRectDefaultY;
        }

        return hit;
    }

    public void teleport(int map, int col, int row, int gameState, String text){
        gp.ui.currentDialogue = text;
        gp.gameState = gp.transitionState;

        tempMap = map;
        tempCol = col;
        tempRow = row;
        canTouchEvent = false;

    }

    public void damagePit(int gameState){
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You fell in a hole and took damage! \nYou climb up the hole."; // depending on the dialogue
        gp.player.life -= 1;
        canTouchEvent = false;
    }

    public void speak(Entity entity){
        if(gp.keyH.enterPressed == true){
            gp.gameState = gp.dialogueState;
            entity.speak();
        }
    }
}