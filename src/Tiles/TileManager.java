package Tiles;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    Tiles[] tile;
    int mapTileNum [][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile =  new Tiles[20];
        mapTileNum = new int [gp.maxWorldCol] [gp.maxWorldRow];
        getTileImage();
        loadMap("/Maps/Stage1.txt");
    }
    public void getTileImage() {
        try{
            tile[0] =  new Tiles();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/grass/grassfull_0.png"));

            tile[1] =  new Tiles();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/grass/grassfull_1.png"));

            tile[2] =  new Tiles();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/sand/sandTexture1.png"));

            tile[3] =  new Tiles();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/sand/cornerUpperLeft.png"));

            tile[4] =  new Tiles();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/sand/cornerUpperRight.png"));

            tile[5] =  new Tiles();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/sand/cornerLowerLeft.png"));

            tile[6] =  new Tiles();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/sand/cornerLowerRight.png"));

            tile[7] =  new Tiles();
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/sand/cornerUpperLeft.png"));

            tile[8] =  new Tiles();
            tile[8].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/sand/up.png"));

            tile[9] =  new Tiles();
            tile[9].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/sand/down.png"));

            tile[10] =  new Tiles();
            tile[10].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/sand/grassCornerUpperLeft.png"));

            tile[11] =  new Tiles();
            tile[11].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/sand/grassCornerUpperRight.png"));

            tile[12] =  new Tiles();
            tile[12].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/sand/grassCornerLowerLeft.png"));

            tile[13] =  new Tiles();
            tile[13].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/sand/grassCornerlowerRight.png"));

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath){
        try{
            InputStream is =  getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col =0;
            int row = 0;
            while(col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = br.readLine();
                while(col < gp.maxWorldCol){
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public  void  draw(Graphics2D g2){
            int worldCol =0, worldRow = 0;


        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldCol){

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null );
            worldCol++;

                if(worldCol == gp.maxWorldCol){
                    worldCol = 0;
                    worldRow++;
                }
        }
    }
}