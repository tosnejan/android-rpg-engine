package meletos.rpg_game.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import meletos.rpg_game.GameView;
import meletos.rpg_game.R;
import meletos.rpg_game.State;
import meletos.rpg_game.menu.MainMenuStates;
import meletos.rpg_game.menu.MenuStates;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class Sound {
    private MediaPlayer mediaPlayer;
    private int maxVolume = 10;
    private int volume;
    private float mediaVolume;
    private Context context;
    private boolean muted;
    private int menu_theme = R.raw.intro_theme;
    private int game_theme = R.raw.game_theme;

    public Sound(Context context) {
        mediaPlayer = new MediaPlayer();
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
        mediaVolume = (float)(volume/10.0); // computing real volume
        mediaPlayer.setVolume(mediaVolume, mediaVolume);
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

    public void play (State state) {
        switch (state) {
            case MAIN_MENU:
                System.out.println("In main menu.");
                mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(context, menu_theme);
                break;
            default:
                System.out.println("In game.");
                mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(context, game_theme);
                break;
        }
        mediaPlayer.setVolume(mediaVolume, mediaVolume);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void killSounds () {
        mediaPlayer.stop();
        mediaPlayer.reset();
    }
}
