package meletos.rpg_game.file_io;

import java.util.HashMap;

public class CharacterRepresentation {
    public String charType;
    public String imagesFolder;
    public String image;
    public boolean isEnemy;
    public int yCoord;
    public int xCoord;
    public HashMap<String,Integer> stats;

    public CharacterRepresentation(
            String charType, String imagesFolder, String image, boolean isEnemy,
            int yCoord, int xCoord, HashMap<String, Integer> stats
    ) {
        this.charType = charType;
        this.imagesFolder = imagesFolder;
        this.image = image;
        this.isEnemy = isEnemy;
        this.yCoord = yCoord;
        this.xCoord = xCoord;
        this.stats = stats;
    }
}
