package meletos.rpg_game.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

public class Sound {
    private MediaPlayer mediaPlayer;
    private int maxVolume = 10;
    private int volume;
    private Context context;

    private boolean muted;

    public Sound(Context context) {
        this.context = context;
    }

    public int getVolume(){
        return volume;
    }

    public void setVolume(int volume){
        this.volume = volume;
        SharedPreferences settings = context.getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("volume", volume);
        editor.apply();
        if (volume == 0) muted = true;
        else muted = false;
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
