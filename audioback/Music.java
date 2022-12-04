package audioback;

import java.io.File;

public abstract class Music {
    protected long musicTimePosition;
    protected File musicPath;
    protected String musicType;
    protected boolean pause;

    public Music(String musicLocation){
        try {
            musicPath = new File(musicLocation);
            if(musicPath.exists()){
                this.musicTimePosition = 0;
                musicType = musicLocation.substring(musicLocation.length()-3, musicLocation.length());
                pause = true;
            }
        } catch (Exception e) {
            System.out.println("Music path doesn't exist.");
        }
    }

    public long getMusicTimePosition(){
        return musicTimePosition;
    }
    
    public boolean isPause(){
        return pause;
    }

    public abstract void play();

    public abstract void pause();

    public abstract void stop();

    public abstract long getMusicTimeLength();

    public abstract double getMusicTimePercent();

    public abstract void setMusicTimePercent(double timePercent);

    public abstract float getVolume();

    public abstract void setVolume(float volume);
}
