package Tiles;

import Main.GamePanel;
import Main.UtilityTool;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class TileManager {
    GamePanel gp;
    public Tiles[] tile;
    public int[][][] mapTileNum;
    boolean drawPath = true;

    ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> collisionStatus = new ArrayList<>();

    public TileManager(GamePanel gp) {
        this.gp = gp;

        //read tile data from file
        InputStream is = getClass().getResourceAsStream("/Maps/tileData0.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        // getting tile names and collision info
        String line;

        try {
            while ((line = br.readLine()) != null){
                fileNames.add(line);
                collisionStatus.add(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tile =  new Tiles[fileNames.size()];
        getTileImage();

        //maxworldcol ug row
        is = getClass().getResourceAsStream("/Maps/Stage1.txt");
        br = new BufferedReader(new InputStreamReader(is));

        try {
            String line2 = br.readLine();
            String maxTile[] = line2.split(" ");

            gp.maxWorldCol = maxTile.length;
            gp.maxWorldRow = maxTile.length;
            mapTileNum = new int [gp.maxMap][gp.maxWorldCol] [gp.maxWorldRow];

        } catch (IOException e){
            System.out.println("Exception!");
        }
        loadMap("/Maps/Stage1.txt",0);
        loadMap("/Maps/Stage2.txt",1);
        loadMap("/Maps/Stage3.txt",2);
        loadMap("/Maps/Dungeon.txt",3);
//        loadMap("/Maps/Stage1.txt");
    }
    public void getTileImage() {
        for(int i = 0; i < fileNames.size(); i++){
            String fileName;
            boolean collision;

            //get a file name
            fileName = fileNames.get(i);

            //get a collision status
            if(collisionStatus.get(i).equals("true")) {
                collision = true;
            } else {
                collision = false;
            }
            setup(i, fileName,collision);
        }
//            setup(0, "grass/grassfull_0", false);
//            setup(1, "grass/grassfull_1", false);
//            setup(2, "sand/sandTexture1", false);
//            setup(3, "sand/cornerUpperLeft", false);
//            setup(4, "sand/cornerUpperRight", false);
//            setup(5, "sand/cornerLowerLeft", false);
//            setup(6, "sand/cornerLowerRight", false);
//            setup(8, "sand/up", false);
//            setup(9, "sand/down", false);
//            setup(10, "sand/grassCornerUpperLeft", false);
//            setup(11, "sand/grassCornerUpperRight", false);
//            setup(12, "sand/grassCornerLowerLeft", false);
//            setup(13, "sand/grassCornerLowerRight", false);
//            setup(14, "Empty/blank", true);
//            setup(15, "sand/left", false);
//            setup(16, "sand/right", false);
    }

    public void setup(int index, String imageName, boolean collision){
        UtilityTool uTool = new UtilityTool();
        try{
            tile[index] = new Tiles();
            System.out.println(imageName);
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/Stage/" + imageName));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath,int map) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                if (line != null) {
                    String[] numbers = line.split(" ");

                    while (col < gp.maxWorldCol) {
                        int num = Integer.parseInt(numbers[col]);
                        mapTileNum[map][col][row] = num;
                        col++;
                    }

                    if (col == gp.maxWorldCol) {
                        col = 0;
                        row++;
                    }
                } else {
                    // System.out.println("Warning: Reached end of file earlier than expected.");
                    break;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void draw(Graphics2D g2){

        int worldCol =0, worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldCol){

            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if(     worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

                g2.drawImage(tile[tileNum].image, screenX, screenY,null );
            }

            worldCol++;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }
        if(drawPath){
            g2.setColor(new Color(255,0,0,70));
            for(int i = 0; i < gp.pFinder.pathlist.size(); i++){
                int worldX = gp.pFinder.pathlist.get(i).col* gp.tileSize;
                int worldY = gp.pFinder.pathlist.get(i).row* gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                g2.fillRect(screenX, screenY, gp.tileSize,gp.tileSize);
            }
        }
    }
}