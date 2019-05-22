package meletos.rpg_game.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import meletos.rpg_game.R;
import meletos.rpg_game.State;

import static meletos.rpg_game.State.BATTLE;
import static meletos.rpg_game.State.ENDGAME;
import static meletos.rpg_game.State.LOST;
import static meletos.rpg_game.State.MAIN_MENU;

/**
 * Class that takes care of all the game sound.
 */
public class Sound {
    private MediaPlayer mediaPlayer;
    private int volume; // got from UI
    private float mediaVolume; // actual volume for media player
    private final Context context;
    private final int menu_theme = R.raw.intro_theme;
    private final int game_theme = R.raw.game_theme;
    private final int endgame_theme = R.raw.endgame_theme;
    private final int battle_theme = R.raw.battle_theme;
    private final int lost_theme = R.raw.lost_theme;
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
     * @param volume in range 0 - 10
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
        if (prevState == LOST && state != MAIN_MENU) return;
        switch (state) {
            case MAIN_MENU:
                swapSongs(menu_theme);
                break;
            case ENDGAME:
                swapSongs(endgame_theme);
                break;
            case LOST:
                swapSongs(lost_theme);
                break;
            case BATTLE:
                swapSongs(battle_theme);
                break;
            default:
                if (prevState != ENDGAME && prevState!= MAIN_MENU && prevState != BATTLE) {
                    return;
                }
                swapSongs(game_theme);
                break;
        }
        prevState = state;
        if (state == ENDGAME) mediaPlayer.setVolume(mediaVolume*2f, mediaVolume*2f);
        else mediaPlayer.setVolume(mediaVolume, mediaVolume);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    /**
     * Swaps two songs when moving between game states
     * @param songID of song to be played
     */
    private void swapSongs(int songID) {
        if (mediaPlayer.isPlaying())
            killSounds();
        mediaPlayer = MediaPlayer.create(context, songID);
    }

    /**
     * Kills all the sounds.
     */
    public void killSounds () {
        mediaPlayer.stop();
        mediaPlayer.reset();
    }
}
