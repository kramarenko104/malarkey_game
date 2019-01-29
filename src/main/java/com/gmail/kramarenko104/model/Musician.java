package com.gmail.kramarenko104.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.log4j.Logger;

public class Musician implements Runnable {

    private boolean isPlaying;
    private boolean toPlay;
    private MediaPlayer player;
    private static Logger logger = Logger.getLogger(Musician.class);

    public Musician(boolean toPlay, String musicFileName) {
        this.toPlay = toPlay;
        this.isPlaying = false;
        Media audioFile = new Media(getClass().getResource(musicFileName).toString());
        player = new MediaPlayer(audioFile);
    }

    public void setPlay(boolean toPlay) {
        this.toPlay = toPlay;
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                if (isPlaying && !toPlay) {
                    player.stop();
                    isPlaying = false;
                    logger.debug("Stop music playing...");
                }
                if (!isPlaying && toPlay) {
                    player.play();
                    isPlaying = true;
                    logger.debug("Start music playing...");
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
