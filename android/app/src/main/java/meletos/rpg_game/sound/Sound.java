package meletos.rpg_game.sound;

import android.media.MediaPlayer;

public class Sound {
    private MediaPlayer mediaPlayer;
    private int maxVolume = 10;
    private int volume;

    private boolean muted;

    public Sound() {
        load();
    }

    private void load() {
        //TODO load sounds
    }

    public int getVolume(){
        return volume;
    }

    public void setVolume(int volume){
        this.volume = volume;

    }

    public boolean isMuted() {
        return muted;
    }

    public void mute() {
        muted = true;
    }

    public void unMute() {
        muted = false;
    }
}
