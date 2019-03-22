package meletos.rpg_game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // this makes the app go fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameView = new GameView(this);
        setContentView(gameView); // this starts the game canvas
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.onPause();
    }

    protected void onResume () {
        super.onResume();
        gameView.onResume();
    }
}
