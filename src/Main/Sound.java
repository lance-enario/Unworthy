package Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[50];
    FloatControl fc;
    int volumeScale = 3;
    float volume;

    public Sound (){
        soundURL[0] = getClass().getResource("/sound/maintheme.wav");
        soundURL[1] = getClass().getResource("/sound/dirtstep.wav");
        soundURL[2] = getClass().getResource("/sound/Slime Death.wav");

        //cutscene sounds
        for(int i = 0;i < 12;i++){
            soundURL[3+i] = getClass().getResource("/sound/cutscenes/" + (i+1) + ".wav");
        }

        soundURL[17] = getClass().getResource("/sound/cutscenes/bg.WAV");
        soundURL[18] = getClass().getResource("/sound/mageattack.wav");     //mage attack
        soundURL[19] = getClass().getResource("/sound/mageskill1.wav");     //mage skill1
        soundURL[20] = getClass().getResource("/sound/shield.wav");         //mage shield
        soundURL[21] = getClass().getResource("/sound/slash.wav");          //warrior slash
        soundURL[22] = getClass().getResource("/sound/npc_hit_1.wav");      //slime hit
        soundURL[23] = getClass().getResource("/sound/ranger_shoot.wav");      //slime hit
        soundURL[24] = getClass().getResource("/sound/dirtstep1.wav");
        soundURL[25] = getClass().getResource("/sound/dirtstep2.wav");
        soundURL[26] = getClass().getResource("/sound/dirtstep3.wav");
        soundURL[27] = getClass().getResource("/sound/dirtstep4.wav");
        soundURL[28] = getClass().getResource("/sound/transformation.wav");
        soundURL[29] = getClass().getResource("/sound/DeathSound.wav");
        soundURL[30] = getClass().getResource("/sound/OptionMenuSound.wav");

    }

    public void setFile(int i){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();
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

    public void checkVolume(){
        switch(volumeScale){
            case 0: volume = -80f; break;
            case 1: volume = -20f; break;
            case 2: volume = -12f; break;
            case 3: volume = -5f; break;
            case 4: volume = 1f; break;
            case 5: volume = 6f; break;
        }
        fc.setValue(volume);
    }
}
