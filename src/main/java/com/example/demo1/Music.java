package com.example.demo1;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Music {
    private long musicTimePosition;
    private File musicPath;
    private Media media;
    private MediaPlayer player;
    private boolean pause;

    /*Constructor*/
    public Music(String musicLocation) {
        try {
            musicPath = new File(musicLocation);
            if (musicPath.exists()) {
                this.musicTimePosition = 0;
                pause = true;
            }
            media = new Media(musicPath.toURI().toString());
            player = new MediaPlayer(media);
        } catch (Exception e) {
            System.out.println("Music path doesn't exist.");
        }
    }

    /*Thực hiện trả về giá trị thời gian audio theo giây (long).*/
    public long getMusicTimePosition() {
        return (long) player.getCurrentTime().toSeconds();
    }

    /*Trả về giá trị pause.*/
    public boolean isPause() {
        return pause;
    }

    /*Phát audio.*/
    public void play(){
        player.play();
        pause = false;
    };

    /*Pause audio.*/
    public void pause(){
        player.pause();
        pause = true;
    };

    /*Stop audio.*/
    public void stop(){
        player.stop();
        pause = true;
    };

    /*Trả về tổng thời gian bài hát theo giây (long).*/
    public long getMusicTimeLength(){
        return (long) player.getTotalDuration().toSeconds();
    };

    public double getMusicTimePercent(){
        return (double) player.getCurrentTime().toSeconds() / player.getTotalDuration().toSeconds();
    }

    /*Điều chỉnh thời gian audio theo giá trị timePercent.
    * timePercent nhận giá trị trong khoảng (0,1)*/
    public void setMusicTimePercent(double timePercent){
        player.seek(player.getTotalDuration().multiply(timePercent));

    };

    /*Trả về giá trị volume hiện tại.*/
    public float getVolume(){
        return (float) player.getVolume();
    };

    /*Điều chỉnh giá trị volume theo giá trị truyền vào.*/
    public void setVolume(float volume){
        player.setVolume(volume);
    };
}
