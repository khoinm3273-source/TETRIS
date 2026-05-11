package src;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private static SoundManager instance;
    private Clip bgmClip;
    private SoundManager() {
    }
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playBGM(String filePath) {
        stopBGM(); //Tắt nhạc

        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioStream);

            //Cài đặt lặp liên tục cho nhạc nền
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Background music cannot be played: " + e.getMessage());
        }
    }

    // Dừng nhạc nền khi game over hoặc tắt game
    public void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
        }
    }

    // Phát hiệu ứng âm thanh
    public void playSFX(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            Clip sfxClip = AudioSystem.getClip();
            sfxClip.open(audioStream);
            sfxClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("The effect cannot be played: " + e.getMessage());
        }
    }
}