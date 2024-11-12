package Main;
import Entity.NPC_Traveller;
import Monster.Goblin;
import Monster.Orc;
import Monster.Slime;
import objects.obj_Chest;
import objects.obj_Door;
import objects.obj_Key;
import objects.obj_Potion;

public class AssetSetter {
    GamePanel gp ;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObj(){
        int i = 0;
        gp.obj[i] = new obj_Key(gp);
        gp.obj[i].worldX = 13 * gp.tileSize;
        gp.obj[i].worldY = 8 * gp.tileSize;
        i++;
        gp.obj[i] = new obj_Chest(gp);
        gp.obj[i].worldX = 13 * gp.tileSize;
        gp.obj[i].worldY = 10 * gp.tileSize;
        i++;
        gp.obj[i] = new obj_Door(gp);
        gp.obj[i].worldX = 13 * gp.tileSize;
        gp.obj[i].worldY = 9 * gp.tileSize;
        i++;
        gp.obj[i] = new obj_Potion(gp);
        gp.obj[i].worldX = 14 * gp.tileSize;
        gp.obj[i].worldY = 9 * gp.tileSize;
    }

    public void setNPC(){
        gp.npc[0] = new NPC_Traveller(gp);
        gp.npc[0].worldX = 13 * gp.tileSize;
        gp.npc[0].worldY = 16 * gp.tileSize;
    }

    public void setMonster(){
        int i = 0;
        gp.monster[i] = new Slime(gp);
        gp.monster[i].worldX = 15 * gp.tileSize;
        gp.monster[i].worldY = 22 * gp.tileSize;
        i++;
        gp.monster[i] = new Orc(gp);
        gp.monster[i].worldX = 12 * gp.tileSize;
        gp.monster[i].worldY = 22 * gp.tileSize;
        i++;
        gp.monster[i] = new Goblin(gp);
        gp.monster[i].worldX = 16 * gp.tileSize;
        gp.monster[i].worldY = 22 * gp.tileSize;
        i++;
    }

}
