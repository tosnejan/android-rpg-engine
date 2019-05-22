package meletos.rpg_game.battle;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.State;
import meletos.rpg_game.characters.FatherCharacter;

/**
 * Class representing the battle.
 */
public class Battle {
    private final GameHandler gameHandler;
    private boolean playersRound = true;
    private int heroShield;
    private int enemyShield;
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
    private HashMap<String,Integer> itemsStats;
    private int x = 0, y = 0;
    private Bitmap toDraw;

    public Battle(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        load();
    }

    /**
     * Loads images.
     */
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

    /**
     * Prepare new battle.
     * @param character enemy to fight with
     */
    public void initNewBattle(FatherCharacter character){
        enemyStats = character.getStats();
        heroStats = gameHandler.getHeroStats();
        itemsStats = gameHandler.getInventory().getItemsStats();
        enemyBar = Bitmap.createScaledBitmap(enemyBar, (gameHandler.getScreenWidth()/3)* (enemyStats.get("HP") >= 2 ? enemyStats.get("HP") : 2) / 1000, gameHandler.getScreenHeight()/20, true);
        heroBar = Bitmap.createScaledBitmap(enemyBar, (gameHandler.getScreenWidth()/3)* (heroStats.get("HP") >= 2 ? heroStats.get("HP") : 2) / 1000, gameHandler.getScreenHeight()/20, true);
        heroShield = 0;
        playersRound = true;
    }

    /**
     * Do animation or play as enemy.
     */
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
                gameHandler.getGameView().getEndgame().setMessage(20, 21);
                gameHandler.setGameViewState(State.ENDGAME);
            }
            if (heroShield != 0){
                heroShield--;
            }
            if (enemyShield != 0){
                enemyShield--;
            }
            toDraw = null;
            animationDone = true;
            playersRound = !playersRound;
        } else if (!playersRound){
            if (new Random().nextInt(10) < 3){
                enemyShield = 6;
                animation = Animations.ENEMY_SHIELD;
            } else {
                attacked();
                animation = Animations.ATTACKED;
            }
            animationDone = false;
        }
    }

    /**
     * Draw health bars and animation if needed.
     * @param canvas canvas to draw
     */
    public void draw(Canvas canvas){
        canvas.drawBitmap(frame, 2*gameHandler.getScreenWidth()/5f, gameHandler.getScreenHeight()-frame.getHeight(), null);
        canvas.drawBitmap(frame, 2*gameHandler.getScreenWidth()/5f, 0, null);
        canvas.drawBitmap(heroBar, 2*gameHandler.getScreenWidth()/5f, gameHandler.getScreenHeight()-frame.getHeight(), null);
        canvas.drawBitmap(enemyBar, 2*gameHandler.getScreenWidth()/5f, 0, null);
        if (!animationDone && toDraw != null){
            canvas.drawBitmap(toDraw, x, y, null);
        }
    }

    /**
     * Set shield for player for 6 turns.
     */
    public void setShield() {
        if (playersRound && animationDone) {
            heroShield = 6;
            animation = Animations.SHIELD;
            animationDone = false;
        }
    }

    /**
     * Attack as player. Calculate damage and does it.
     */
    void attack() {
        if (playersRound && animationDone) {
            int hp = enemyStats.get("HP");
            int mr = enemyStats.get("MR");
            int arm = enemyStats.get("ARM");
            int damage = heroStats.get("DMG") + (itemsStats.containsKey("DMG") ? itemsStats.get("DMG") : 0);
            int inteligence = heroStats.get("INT") + (itemsStats.containsKey("INT") ? itemsStats.get("INT") : 0);
            damage = (int)(damage * gameHandler.getHeroProperties().getDamageMultiplayer());
            inteligence = (int)(inteligence * gameHandler.getHeroProperties().getInteligenceMultiplayer());
            if (enemyShield != 0){
                mr *= 2;
                arm *= 2;
            }
            hp += arm < damage ? arm - damage : 0;
            hp += mr < inteligence ? mr - inteligence : 0;
            enemyBar = Bitmap.createScaledBitmap(enemyBar, (gameHandler.getScreenWidth()/3)* (hp >= 2 ? hp : 2) / 1000, gameHandler.getScreenHeight()/20, true);
            enemyStats.put("HP", hp);
            animation = Animations.ATTACK;
            animationDone = false;
        }
    }

    /**
     * Attack as enemy. Calculate damage and does it.
     */
    private void attacked() {
        int hp = heroStats.get("HP");
        int mr = heroStats.get("MR") + (itemsStats.containsKey("MR") ? itemsStats.get("MR") : 0);
        int arm = heroStats.get("ARM") + (itemsStats.containsKey("ARM") ? itemsStats.get("ARM") : 0);
        int damage = enemyStats.get("DMG");
        int inteligence = enemyStats.get("INT");
        if (heroShield != 0){
            mr *= 2;
            arm *= 2;
        }
        hp += arm < damage ? arm - damage : 0;
        hp += mr < inteligence ? mr - inteligence : 0;
        heroBar = Bitmap.createScaledBitmap(enemyBar, (gameHandler.getScreenWidth()/3)* (hp >= 2 ? hp : 2) / 1000, gameHandler.getScreenHeight()/20, true);
        heroStats.put("HP", hp);
        animation = Animations.ATTACKED;
        animationDone = false;
    }

    /**
     * Heal player by 100.
     */
    void healChar() {
        HashMap<String,Integer> stats = gameHandler.getHeroStats();
        int hp = stats.get("HP");
        stats.put("HP", hp + 100);
        heroBar = Bitmap.createScaledBitmap(heroBar, (gameHandler.getScreenWidth()/3)* (stats.get("HP") >= 2 ? stats.get("HP") : 2) / 1000, gameHandler.getScreenHeight()/20, true);
        animation = Animations.HEAL;
        animationDone = false;
    }
}
