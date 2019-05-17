package meletos.rpg_game.battle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.IOException;
import java.util.HashMap;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.State;
import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.inventory.Inventory;

public class Battle {
    private GameHandler gameHandler;
    private boolean playersRound = true;
    private int playerShield;
    private boolean animationDone = true;
    private Animations animation;
    private Bitmap frame;
    private Bitmap heroBar;
    private Bitmap enemyBar;
    private Bitmap heal;
    private Bitmap shield;
    private Bitmap attacked;
    private HashMap<String,Integer> enemyStats;
    private HashMap<String,Integer> heroStats;
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
            image = BitmapFactory.decodeStream(am.open("battle/frame.png"));
            frame = Bitmap.createScaledBitmap(image, screenWidth/3, screenHeight/20, true);
            image = BitmapFactory.decodeStream(am.open("battle/hero bar.png"));
            heroBar = Bitmap.createScaledBitmap(image, screenWidth/3, screenHeight/20, true);
            image = BitmapFactory.decodeStream(am.open("battle/enemy bar.png"));
            enemyBar = Bitmap.createScaledBitmap(image, screenWidth/3, screenHeight/20, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initNewBattle(FatherCharacter character){
        enemyStats = character.getStats();
        heroStats = gameHandler.getInventory().getStats(gameHandler);
        enemyBar = Bitmap.createScaledBitmap(enemyBar, gameHandler.getScreenWidth()/3, gameHandler.getScreenHeight()/20, true);
        heroBar = Bitmap.createScaledBitmap(heroBar, gameHandler.getScreenWidth()/3, gameHandler.getScreenHeight()/20, true);
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
            if (enemyStats.get("HP") <= 0){
                gameHandler.removeCharacter(gameHandler.getFighting());
                gameHandler.setGameViewState(State.MAP);
            } else if (heroStats.get("HP") <= 0){
                //TODO game over
                gameHandler.setGameViewState(State.MAP);
            }
            if (playerShield != 0){
                playerShield--;
            }
            toDraw = null;
            animationDone = true;
            playersRound = !playersRound;
        } else if (!playersRound){
            animationDone = false;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(frame, 2*gameHandler.getScreenWidth()/5f, gameHandler.getScreenHeight()-frame.getHeight(), null);
        canvas.drawBitmap(frame, 2*gameHandler.getScreenWidth()/5f, 0, null);
        canvas.drawBitmap(heroBar, 2*gameHandler.getScreenWidth()/5f, gameHandler.getScreenHeight()-frame.getHeight(), null);
        canvas.drawBitmap(enemyBar, 2*gameHandler.getScreenWidth()/5f, 0, null);
        if (!animationDone && toDraw != null){
            canvas.drawBitmap(toDraw, x, y, null);
        }
    }

    public void setShield() {
        if (playersRound && animationDone) {
            playerShield = 4;
            animation = Animations.SHIELD;
            animationDone = false;
            playersRound = false;
        }
    }

    public void attack() {
        if (playersRound && animationDone) {
            int hp = enemyStats.get("HP");
            int mr = enemyStats.get("MR");
            int arm = enemyStats.get("ARM");
            int damage = (int)(heroStats.get("DMG") * gameHandler.getHeroProperties().getDamageMultiplayer());
            int inteligence = (int)(heroStats.get("INT") * gameHandler.getHeroProperties().getInteligenceMultiplayer());
            hp += arm < damage ? arm - damage : 0;
            hp += mr < inteligence ? mr - inteligence : 0;
            enemyBar = Bitmap.createScaledBitmap(enemyBar, (gameHandler.getScreenWidth()/3)* (hp >= 2 ? hp : 2) / 1000, gameHandler.getScreenHeight()/20, true);
            enemyStats.put("HP", hp);
            animation = Animations.ATTACK;
            animationDone = false;
            playersRound = false;
        }
    }

    public void healChar(Inventory inventory) {

        HashMap<String,Integer> stats = gameHandler.getHeroStats();
        int hp = stats.get("HP");
        if (hp <= 900 && inventory.deleteItem(21)){
            stats.put("HP", hp + 100);
            heroBar = Bitmap.createScaledBitmap(heroBar, (gameHandler.getScreenWidth()/3)* (stats.get("HP") >= 2 ? stats.get("HP") : 2) / 1000, gameHandler.getScreenHeight()/20, true);
            animation = Animations.HEAL;
            animationDone = false;
            playersRound = false;
        } else if (hp <= 900) alert(gameHandler.getText().getText(18));
        else if (inventory.hasItem(21)) alert(gameHandler.getText().getText(17));
    }

    private void alert(String message){
        AlertDialog alertDialog = new AlertDialog.Builder(gameHandler.context).create();
        alertDialog.setTitle(gameHandler.getText().getText(19));
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
