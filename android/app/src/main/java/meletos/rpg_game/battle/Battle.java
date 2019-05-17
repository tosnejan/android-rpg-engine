package meletos.rpg_game.battle;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.IOException;
import java.util.HashMap;

import meletos.rpg_game.GameHandler;

public class Battle {
    private GameHandler gameHandler;
    private boolean playersRound = true;
    private int playerShield;
    private boolean animationDone = true;
    private Animations animation;
    private Bitmap heal;
    private Bitmap shield;
    private Bitmap attacked;
    private HashMap<String,Integer> enemyStats;
    private int x = 0, y = 0;
    private Bitmap toDraw;

    public Battle(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        load();
    }

    private void load(){
        AssetManager am = gameHandler.context.getAssets();
        int screenHeight = gameHandler.getScreenHeight();
        int screenWidth = gameHandler.getScreenWidth();
        try {
            Bitmap image = BitmapFactory.decodeStream(am.open("battle/animations/heal.png"));
            heal = Bitmap.createScaledBitmap(image, screenWidth, screenHeight, true);
            image = BitmapFactory.decodeStream(am.open("battle/animations/attacked.png"));
            attacked = Bitmap.createScaledBitmap(image, screenWidth, screenHeight, true);
            image = BitmapFactory.decodeStream(am.open("battle/animations/shield.png"));
            shield = Bitmap.createScaledBitmap(image, screenWidth, screenHeight, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initNewBattle(){
        enemyStats = gameHandler.getFighting().getStats();
        playerShield = 0;
        playersRound = true;
    }

    public void update(){
        if (!animationDone){
            try {
                switch (animation) {
                    case HEAL:
                        x = 0;
                        y = 0;
                        toDraw = heal;
                        Thread.sleep(500);
                        break;
                    case ATTACK:
                        break;
                    case ATTACKED:
                        x = 0;
                        y = 0;
                        toDraw = attacked;
                        Thread.sleep(500);
                        break;
                    case SHIELD:
                        x = 0;
                        y = 0;
                        toDraw = shield;
                        Thread.sleep(500);
                        break;
                    case ENEMY_SHIELD:
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (playerShield != 0){
                playerShield--;
            }
            animationDone = true;
            playersRound = !playersRound;
        } else if (!playersRound){
            animationDone = false;
        }
    }

    public void draw(Canvas canvas){
        if (!animationDone){
            canvas.drawBitmap(toDraw, x, y, null);
        }
    }

    public void setShield() {
        if (playersRound && animationDone) {
            System.out.println("shield");
            playerShield = 4;
            animation = Animations.SHIELD;
            animationDone = false;
            playersRound = false;
        }
    }

    public void attack() {
        if (playersRound && animationDone) {
            System.out.println("attack");
            animation = Animations.ATTACK;
            animationDone = false;
            playersRound = false;
        }
    }

    public void healChar() {
        HashMap<String,Integer> stats = gameHandler.getHeroStats();
        int hp = stats.get("HP");  stats.put("HP", hp + 100 > 100 ? 1000 : hp + 100);
        animation = Animations.HEAL;
        animationDone = false;
        playersRound = false;
    }
}
