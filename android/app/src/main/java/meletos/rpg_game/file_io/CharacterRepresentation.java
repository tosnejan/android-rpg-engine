package meletos.rpg_game.file_io;

import java.util.HashMap;

public class CharacterRepresentation {
    private String charType;
    private String imagesFolder;
    private String image;
    private boolean isEnemy;
    private int yCoord;
    private int xCoord;
    private HashMap<String,Integer> stats;

    public String getCharType() {
        return charType;
    }
}
