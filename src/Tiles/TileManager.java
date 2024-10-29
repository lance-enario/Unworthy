package Tiles;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tiles[] tile;
    public int mapTileNum [][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile =  new Tiles[20];
        mapTileNum = new int [gp.maxWorldCol] [gp.maxWorldRow];
        getTileImage();
        loadMap("/Maps/Stage1.txt");
    }
    public void getTileImage() {
            setup(0, "grass/grassfull_0", false);
            setup(1, "grass/grassfull_1", false);
            setup(2, "sand/sandTexture1", false);
            setup(3, "sand/cornerUpperLeft", false);
            setup(4, "sand/cornerUpperRight", false);
            setup(5, "sand/cornerLowerLeft", false);
            setup(6, "sand/cornerLowerRight", false);
            setup(8, "sand/up", false);
            setup(9, "sand/down", false);
            setup(10, "sand/grassCornerUpperLeft", false);
            setup(11, "sand/grassCornerUpperRight", false);
            setup(12, "sand/grassCornerLowerLeft", false);
            setup(13, "sand/grassCornerLowerRight", false);
            setup(14, "Empty/blank", true);
            setup(15, "sand/left", false);
            setup(16, "sand/right", false);
    }

    public void setup(int index, String imageName, boolean collision){
        UtilityTool uTool = new UtilityTool();
        try{
            tile[index] = new Tiles();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
               if (line != null) {
                    String[] numbers = line.split(" ");

                    while (col < gp.maxWorldCol) {
                        int num = Integer.parseInt(numbers[col]);
                        mapTileNum[col][row] = num;
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

            int tileNum = mapTileNum[worldCol][worldRow];

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
    }
}