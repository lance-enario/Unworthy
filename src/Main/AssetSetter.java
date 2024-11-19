package Main;
import Entity.*;
import Monster.Orc;
import Monster.Slime;
import objects.*;

public class AssetSetter {
    GamePanel gp ;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObj(){
        int i = 0;
        int mapNum = 0;
        gp.obj[mapNum][i] = new obj_Key(gp);
        gp.obj[mapNum][i].worldX = 39 * gp.tileSize;
        gp.obj[mapNum][i].worldY =42 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new obj_Chest(gp, new obj_Key(gp));
        gp.obj[mapNum][i].worldX = 41 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 42 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new obj_Door(gp);
        gp.obj[mapNum][i].worldX = 43 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 42 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new obj_Potion(gp);
        gp.obj[mapNum][i].worldX = 45 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 42 * gp.tileSize;
        i++;

        // Map 1 Village
        mapNum = 1;
        gp.obj[mapNum][i] = new obj_guard(gp);
        gp.obj[mapNum][i].worldX = 49 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 22 * gp.tileSize;
        i++;


        // Map 3 Dungeon
        mapNum = 3;
        gp.obj[mapNum][i] = new obj_IronDoor(gp);
        gp.obj[mapNum][i].worldX = 78 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 60 * gp.tileSize;
        i++;

        gp.obj[mapNum][i] = new obj_Chest(gp, new obj_Potion(gp));
        gp.obj[mapNum][i].worldX = 50 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 17 * gp.tileSize;
        i++;

        gp.obj[mapNum][i] = new obj_Chest(gp, new obj_Key(gp));
        gp.obj[mapNum][i].worldX = 38 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 18 * gp.tileSize;
        i++;

        gp.obj[mapNum][i] = new obj_Chest(gp, new obj_Door(gp));
        gp.obj[mapNum][i].worldX = 56 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 32 * gp.tileSize;
        i++;


    }

    public void setNPC(){
        int mapNum = 0;
        int i = 0;
        gp.npc[mapNum][i] = new NPC_Traveller(gp);
        gp.npc[mapNum][i].worldX = 22 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 40 * gp.tileSize;
        i++;

        mapNum = 1;
//        gp.npc[mapNum][i] = new npc_guardr(gp);
//        gp.npc[mapNum][i].worldX = 50 * gp.tileSize;
//        gp.npc[mapNum][i].worldY = 22 * gp.tileSize;
//        i++;
//        gp.npc[mapNum][i] = new npc_guardl(gp);
//        gp.npc[mapNum][i].worldX = 49 * gp.tileSize;
//        gp.npc[mapNum][i].worldY = 22 * gp.tileSize;
//        i++;
        gp.npc[mapNum][i] = new npc_bard1(gp);
        gp.npc[mapNum][i].worldX = 47 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 50 * gp.tileSize;
        i++;
        gp.npc[mapNum][i] = new npc_dancer(gp);
        gp.npc[mapNum][i].worldX = 46 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 51 * gp.tileSize;
        i++;
        gp.npc[mapNum][i] = new npc_toss(gp);
        gp.npc[mapNum][i].worldX = 37 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 45 * gp.tileSize;
        i++;
        gp.npc[mapNum][i] = new npc_old_baby(gp);
        gp.npc[mapNum][i].worldX = 63 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 47 * gp.tileSize;
        i++;
        gp.npc[mapNum][i] = new npc_cat(gp);
        gp.npc[mapNum][i].worldX = 44 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 42 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_bard2(gp);
        gp.npc[mapNum][i].worldX = 48 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 39 * gp.tileSize;
        i++;
        gp.npc[mapNum][i] = new npc_dancer2(gp);
        gp.npc[mapNum][i].worldX = 49 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 41 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_sweep(gp);
        gp.npc[mapNum][i].worldX = 31 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 47 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_walk1(gp);
        gp.npc[mapNum][i].worldX = 47 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 56 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_young_baby(gp);
        gp.npc[mapNum][i].worldX = 51 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 53 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_blacksmith(gp);
        gp.npc[mapNum][i].worldX = (58 * gp.tileSize) - (gp.tileSize/3);
        gp.npc[mapNum][i].worldY = 45 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_child1(gp);
        gp.npc[mapNum][i].worldX = 54 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 64 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_child2(gp);
        gp.npc[mapNum][i].worldX = 52 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 64 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_farmer2(gp);
        gp.npc[mapNum][i].worldX = 64 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 58 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_farmer1(gp);
        gp.npc[mapNum][i].worldX = 62 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 56 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_walk2(gp);
        gp.npc[mapNum][i].worldX = 43 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 70 * gp.tileSize;
        i++;

    }

    public void setMonster(){
        int i = 0;
        int mapNum = 0;
        gp.monster[mapNum][i] = new Slime(gp);
        gp.monster[mapNum][i].worldX = 36 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 55 * gp.tileSize;
        i++;

        gp.monster[mapNum][i] = new Slime(gp);
        gp.monster[mapNum][i].worldX = 43 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 35 * gp.tileSize;
        i++;

        gp.monster[mapNum][i] = new Slime(gp);
        gp.monster[mapNum][i].worldX = 78 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 29 * gp.tileSize;
        i++;

        gp.monster[mapNum][i] = new Slime(gp);
        gp.monster[mapNum][i].worldX = 72 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 36 * gp.tileSize;
        i++;

        gp.monster[mapNum][i] = new Slime(gp);
        gp.monster[mapNum][i].worldX = 77 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 57 * gp.tileSize;
        i++;


        // if ganahan mo place  in map 1
        //mapNum = 1;
        //same code
        mapNum = 3;
        gp.monster[mapNum][i] = new Slime(gp);
        gp.monster[mapNum][i].worldX = 38 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 20 * gp.tileSize;
        i++;

        gp.monster[mapNum][i] = new Slime(gp);
        gp.monster[mapNum][i].worldX = 50 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 20 * gp.tileSize;
        i++;
    }

}
