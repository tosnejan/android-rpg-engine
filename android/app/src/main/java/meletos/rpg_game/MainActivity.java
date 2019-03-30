package meletos.rpg_game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import meletos.rpg_game.text.Language;
import meletos.rpg_game.text.Text;

public class MainActivity extends Activity {

    private GameView gameView;
    private Text text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // this makes the app go fullscreen
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        text = new Text(Language.CZE,this);// change this for default language
        gameView = new GameView(this, text);
        setContentView(gameView); // this starts the game canvas
        gameView.onCreate();
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

    protected void onDestroy () {
        super.onDestroy();
        gameView.onDestroy();
    }

    /**
     * Displays dialog upon user trying to kill the app.
     */
    @Override
    public void onBackPressed() {
        if (gameView.getState() == State.MAIN_MENU) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(text.getText(2))
                    .setMessage(text.getText(3))
                    .setPositiveButton(text.getText(1), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(text.getText(0), null)
                    .show();
        } else {
            gameView.setState(State.MENU);
        }
    }
}
