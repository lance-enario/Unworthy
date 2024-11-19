package Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[30];

    public Sound (){
        soundURL[0] = getClass().getResource("/sound/maintheme.wav");
        soundURL[1] = getClass().getResource("/sound/dirtstep.wav");
        soundURL[2] = getClass().getResource("/sound/Slime Death.wav");

        //cutscene sounds
        for(int i = 0;i < 12;i++){
            soundURL[3+i] = getClass().getResource("/sound/cutscenes/" + (i+1) + ".wav");
        }

        soundURL[17] = getClass().getResource("/sound/cutscenes/bg.WAV");
        soundURL[18] = getClass().getResource("/sound/mageattack.wav");
        soundURL[19] = getClass().getResource("/sound/mageskill1.wav");
    }

    public void setFile(int i){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }
}
