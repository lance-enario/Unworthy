package Main;
import Entity.NPC_Traveller;
import Monster.Goblin;
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
        int mapNUm = 0;
        gp.obj[mapNUm][i] = new obj_Key(gp);
        gp.obj[mapNUm][i].worldX = 39 * gp.tileSize;
        gp.obj[mapNUm][i].worldY =42 * gp.tileSize;
        i++;
        gp.obj[mapNUm][i] = new obj_Chest(gp);
        gp.obj[mapNUm][i].worldX = 41 * gp.tileSize;
        gp.obj[mapNUm][i].worldY = 42 * gp.tileSize;
        i++;
        gp.obj[mapNUm][i] = new obj_Door(gp);
        gp.obj[mapNUm][i].worldX = 43 * gp.tileSize;
        gp.obj[mapNUm][i].worldY = 42 * gp.tileSize;
        i++;
        gp.obj[mapNUm][i] = new obj_Potion(gp);
        gp.obj[mapNUm][i].worldX = 45 * gp.tileSize;
        gp.obj[mapNUm][i].worldY = 42 * gp.tileSize;
    }

    public void setNPC(){
        int mapNUm = 0;
        gp.npc[mapNUm][0] = new NPC_Traveller(gp);
        gp.npc[mapNUm][0].worldX = 22 * gp.tileSize;
        gp.npc[mapNUm][0].worldY = 40 * gp.tileSize;

        mapNUm = 1;
        gp.npc[mapNUm][0] = new NPC_Traveller(gp);
        gp.npc[mapNUm][0].worldX = 24 * gp.tileSize;
        gp.npc[mapNUm][0].worldY = 47 * gp.tileSize;
    }

    public void setMonster(){
        int i = 0;
        int mapNUm = 0;
        gp.monster[mapNUm][i] = new Slime(gp);
        gp.monster[mapNUm][i].worldX = 36 * gp.tileSize;
        gp.monster[mapNUm][i].worldY = 55 * gp.tileSize;
        i++;

        gp.monster[mapNUm][i] = new Slime(gp);
        gp.monster[mapNUm][i].worldX = 43 * gp.tileSize;
        gp.monster[mapNUm][i].worldY = 35 * gp.tileSize;
        i++;

        gp.monster[mapNUm][i] = new Slime(gp);
        gp.monster[mapNUm][i].worldX = 78 * gp.tileSize;
        gp.monster[mapNUm][i].worldY = 29 * gp.tileSize;
        i++;

        gp.monster[mapNUm][i] = new Slime(gp);
        gp.monster[mapNUm][i].worldX = 72 * gp.tileSize;
        gp.monster[mapNUm][i].worldY = 36 * gp.tileSize;
        i++;

        gp.monster[mapNUm][i] = new Slime(gp);
        gp.monster[mapNUm][i].worldX = 77 * gp.tileSize;
        gp.monster[mapNUm][i].worldY = 57 * gp.tileSize;
        i++;


        // if ganahan mo place  in map 1
        //mapNUm = 1;
        //same code
        mapNUm = 1;
        gp.monster[mapNUm][i] = new Goblin(gp);
        gp.monster[mapNUm][i].worldX = 25 * gp.tileSize;
        gp.monster[mapNUm][i].worldY = 47 * gp.tileSize;
    }

}
