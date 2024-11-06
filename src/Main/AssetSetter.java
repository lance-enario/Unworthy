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
        gp.monster[0] = new Slime(gp);
        gp.monster[0].worldX = 8 * gp.tileSize;
        gp.monster[0].worldY = 16 * gp.tileSize;

        gp.monster[1] = new Orc(gp);
        gp.monster[1].worldX = 8 * gp.tileSize;
        gp.monster[1].worldY = 17 * gp.tileSize;

        gp.monster[2] = new Goblin(gp);
        gp.monster[2].worldX = 9 * gp.tileSize;
        gp.monster[2].worldY = 18 * gp.tileSize;
    }

}
