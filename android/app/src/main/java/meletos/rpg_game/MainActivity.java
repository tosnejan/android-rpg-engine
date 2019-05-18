package meletos.rpg_game;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

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
        //SharedPreferences settings = getSharedPreferences("settings", 0);
        text = new Text(this);// change this for default language
        gameView = new GameView(this, text);
        setContentView(gameView); // this starts the game canvas
        gameView.onCreate();
        checkPermissions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.onPause();
    }

    @Override
    protected void onResume () {
        super.onResume();
        gameView.onResume();
    }

    @Override
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
            finish();
        } else if (gameView.getState() == State.MENU){
            gameView.setState(State.MAP);
        } else if (gameView.getState() == State.INVENTORY){
            gameView.setState(State.MAP);
        } else if (gameView.getState() == State.BATTLE){
        } else {
            gameView.setState(State.MENU);
        }
    }

    /**
     * Checks if the app is permitted to use the devices storage.
     * Upon result is triggered the onRequestPermissionsResult
     */
    public void checkPermissions () {
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100
            );
        }
    }

    /**
     * Stops the game if permissions are not given
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[]
                                                   grantResults) {
        switch (requestCode) {
            case 100: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    finish(); // finishes the game if permissions are not granted
                }
                return;
            }
        }
    }

}
