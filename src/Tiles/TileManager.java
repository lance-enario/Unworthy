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
    int mapTileNum [] [];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile =  new Tiles[20];
        mapTileNum = new int [gp.maxScreenCol] [gp.maxScreenRow];
        getTileImage();
        loadMap();
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

    public void loadMap(){
        try{
            InputStream is =  getClass().getResourceAsStream("/Maps/Stage1.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col =0;
            int row = 0;
            while(col < gp.maxScreenCol && row < gp.maxScreenRow){
                String line = br.readLine();
                while(col < gp.maxScreenCol){
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxScreenCol){
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
            int col =0, row = 0, x = 0, y = 0;

        while(col < gp.maxScreenCol && row < gp.maxScreenRow){
            int tileNum = mapTileNum[col][row];
            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null );
            col++;
            x += gp.tileSize;

                if(col == gp.maxScreenCol){
                    col = 0;
                    x = 0;
                    row++;
                    y += gp.tileSize;
                }
        }
    }
}