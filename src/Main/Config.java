package Main;

import java.io.*;

public class Config {

    GamePanel gp;

    public Config(GamePanel gp){
        this.gp = gp;
    }

    public void saveConfig(){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

            // Full Screen Setting
            if(gp.fullScreenOn){
                bw.write("On");
            }
            if(!gp.fullScreenOn){
                bw.write("Off");
            }
            bw.newLine();

            // Music Volume
            bw.write(String.valueOf(gp.sound.volumeScale));
            bw.newLine();

            // Sound Effect Volume
            bw.write(String.valueOf(gp.SE.volumeScale));
            bw.newLine();

            bw.close();

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void loadConfig(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));

            String s = br.readLine();

            // Full Screen
            if(s.equals("On")){
                gp.fullScreenOn = true;
            }
            if(s.equals("Off")){
                gp.fullScreenOn = false;
            }

            // Music volume
            s = br.readLine();
            gp.sound.volumeScale = Integer.parseInt(s);

            // Sound Effect Volume
            s = br.readLine();
            gp.SE.volumeScale = Integer.parseInt(s);

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
