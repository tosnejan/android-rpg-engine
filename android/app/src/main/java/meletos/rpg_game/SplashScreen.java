package meletos.rpg_game;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        Thread myThread = new Thread(){

            @Override
            public void run() {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    SystemClock.sleep(2000);    //Time for SplashScreen before it kills it self
                    startActivity(intent);
                    finish();
            }
        };
        myThread.start();
    }
}
