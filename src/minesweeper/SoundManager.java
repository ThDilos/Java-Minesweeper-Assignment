package minesweeper;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private static Clip backgroundMusicClip;  // Dedicated Clip for background music

    // Play or stop background music
    public static void playBackgroundMusic(String filepath, boolean loop, float volume) {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();  // Stop current music if it's playing
            backgroundMusicClip.close(); // Close the clip to release resources
        }

        try {
            File soundFile = new File(filepath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioIn);

            // Adjust volume
            FloatControl gainControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);

            backgroundMusicClip.start();
            if (loop) {
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Stop the current background music
    public static void stopBackgroundMusic() {
        if (backgroundMusicClip != null) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close();
        }
    }

    // Play sound effects without affecting background music
    public static void playSoundEffect(String filepath, float volume) {
        try {
            File soundFile = new File(filepath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Adjust volume
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);

            clip.start();

            // Ensure the clip is closed once the sound is finished playing
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
