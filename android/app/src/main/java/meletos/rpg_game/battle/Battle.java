package meletos.rpg_game.battle;

import java.util.HashMap;

import meletos.rpg_game.GameHandler;

public class Battle {
    private GameHandler gameHandler;
    private boolean playersRound = true;
    private int playerShield = 0;
    private boolean animationDone = true;
    private HashMap<String,Integer> enemyStats;

    public Battle(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        //enemyStats = gameHandler.getFighting().getStats();

    }

    private void load(){

    }

    public void update(){
        if (!animationDone){

        } else if (!playersRound){

        }
    }

    public void draw(){
        if (!animationDone){

        }
    }

    public void setShield() {
        if (playersRound && animationDone) {
            playerShield = 2;
            playersRound = false;
        }
    }

    public void attack() {
        if (playersRound && animationDone) {
            playersRound = false;
        }
    }
}
