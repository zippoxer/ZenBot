/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 *
 * @author Radek
 */
public class Alarmer {

    public static final int PLAYER_ONSCREEN = 0;
    public static final int TRAPPED = 1;
    public static final int MAIN_CHAT = 2;
    public static final int PRIV_CHAT = 3;
    public static final int PLAYER_ATTACKING = 4;
    
    private Clip alarm;

    public Alarmer() {
        try {
            alarm = AudioSystem.getClip();
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("sounds/navy_alert.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(resource));
            alarm.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Set<Integer> playing = new HashSet<>();

    public void play(int type) {
        playing.add(type);
        if(playing.size() > 0 && !alarm.isRunning()) {
            alarm.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop(int type) {
        playing.remove(type);
        if(playing.isEmpty() && alarm.isRunning()) {
            alarm.stop();
            alarm.flush();
        }
    }

    public void stopAll() {
        playing.clear();
        if(alarm.isRunning()) {
            alarm.stop();
            alarm.flush();
        }
    }
    
    public void setVolume(int n) {
        FloatControl volume = (FloatControl) alarm.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue(volume.getMinimum() + n * (volume.getMaximum() - volume.getMinimum()) / 100.0F);
    }
}
