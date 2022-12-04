package audioback;

import javazoom.jl.player.Player;


public class Mp3Music extends Music {
    private Player player;
    private long musicTimeLength;

    public Mp3Music(String musicLocation) {
        super(musicLocation);
    }

    @Override
    public void play() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public long getMusicTimeLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getMusicTimePercent() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMusicTimePercent(double timePercent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public float getVolume() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setVolume(float volume) {
        // TODO Auto-generated method stub
        
    }

}