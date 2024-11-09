package Main;
import Entity.NPC_Traveller;
import Monster.Goblin;
import Monster.Orc;
import Monster.Slime;
import objects.obj_Chest;
import objects.obj_Door;
import objects.obj_Key;

public class AssetSetter {
    GamePanel gp ;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObj(){
        gp.obj[0] = new obj_Key(gp);
        gp.obj[0].worldX = 13 * gp.tileSize;
        gp.obj[0].worldY = 8 * gp.tileSize;

        gp.obj[1] = new obj_Chest(gp);
        gp.obj[1].worldX = 13 * gp.tileSize;
        gp.obj[1].worldY = 10 * gp.tileSize;

        gp.obj[2] = new obj_Door(gp);
        gp.obj[2].worldX = 13 * gp.tileSize;
        gp.obj[2].worldY = 9 * gp.tileSize;
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
