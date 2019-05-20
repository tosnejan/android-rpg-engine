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
import static meletos.rpg_game.State.BATTLE;
import static meletos.rpg_game.State.ENDGAME;
import static meletos.rpg_game.State.MAIN_MENU;

/**
 * Class that takes care of all the game sound.
 */
public class Sound {
    private MediaPlayer mediaPlayer;
    private int volume; // got from UI
    private float mediaVolume; // actual volume for media player
    private Context context;
    private int menu_theme = R.raw.intro_theme;
    private int game_theme = R.raw.game_theme;
    private int endgame_theme = R.raw.endgame_theme;
    private int battle_theme = R.raw.battle_theme;
    private State prevState = State.MENU;

    public Sound(Context context) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        this.context = context;
    }

    public int getVolume(){
        return volume;
    }

    /**
     * Sets volume, volume is in range from 0 to 10. It then gets translated
     * by logarithmic scale into float 0 - 1
     * @param volume
     */
    public void setVolume(int volume){
        this.volume = volume;
        SharedPreferences settings = context.getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("volume", volume);
        editor.apply();
        if (volume == 0) {
            mediaVolume = 0;
        }
        else {
            mediaVolume = (float)Math.log10(volume); // computing real volume
        }
        mediaPlayer.setVolume(mediaVolume, mediaVolume);
    }

    /**
     * Method that updates the sound based on the current state.
     * @param state -- state of the game
     */
    public void play (State state) {
        switch (state) {
            case MAIN_MENU:
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(context, menu_theme);
                break;
            case ENDGAME:
                if (mediaPlayer.isPlaying()) killSounds();
                mediaPlayer = MediaPlayer.create(context, endgame_theme);
                break;
            case BATTLE:
                if (mediaPlayer.isPlaying())
                    killSounds();
                mediaPlayer = MediaPlayer.create(context, battle_theme);
                break;
            default:
                if (prevState != ENDGAME && prevState!= MAIN_MENU && prevState != BATTLE) {
                    return;
                }
                if (mediaPlayer.isPlaying())
                    killSounds();
                mediaPlayer = MediaPlayer.create(context, game_theme);
                break;
        }
        prevState = state;
        if (state == ENDGAME) mediaPlayer.setVolume(mediaVolume*2f, mediaVolume*2f);
        else mediaPlayer.setVolume(mediaVolume, mediaVolume);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    /**
     * Kills all the sounds.
     */
    public void killSounds () {
        mediaPlayer.stop();
        mediaPlayer.reset();
    }
}
