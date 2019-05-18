package meletos.rpg_game.file_io;

import java.util.HashMap;

import meletos.rpg_game.dialog.DialogSwitcher;

public class CharacterRepresentation {
    public String charType;
    public String imagesFolder;
    public String image;
    public boolean isEnemy;
    public int yCoord;
    public int xCoord;
    public HashMap<String,Integer> stats;
    public int[][] dialogs;
    public int actualDialog;
    public boolean played;
    public DialogSwitcher[] dialogSwitchers;
/*
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
    }*/

    public CharacterRepresentation(String charType, String imagesFolder, String image,
                                   boolean isEnemy, int yCoord, int xCoord, HashMap<String, Integer> stats,
                                   int[][] dialogs, int actualDialog, boolean played, DialogSwitcher[] dialogSwitchers) {
        this.charType = charType;
        this.imagesFolder = imagesFolder;
        this.image = image;
        this.isEnemy = isEnemy;
        this.yCoord = yCoord;
        this.xCoord = xCoord;
        this.stats = stats;
        this.dialogs = dialogs;
        this.actualDialog = actualDialog;
        this.played = played;
        this.dialogSwitchers = dialogSwitchers;
    }
}
