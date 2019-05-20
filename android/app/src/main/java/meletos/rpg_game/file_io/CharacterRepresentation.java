package meletos.rpg_game.file_io;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import meletos.rpg_game.dialog.DialogSwitcher;

/**
 * Class that represents character in JSON
 */
public class CharacterRepresentation {
    String charType;
    public String imagesFolder;
    public String image;
    boolean isEnemy;
    int yCoord;
    int xCoord;
    public HashMap<String,Integer> stats;
    int[][] dialogs;
    int actualDialog;
    boolean played;
    DialogSwitcher[] dialogSwitchers;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharacterRepresentation that = (CharacterRepresentation) o;
        return isEnemy == that.isEnemy &&
                yCoord == that.yCoord &&
                xCoord == that.xCoord &&
                actualDialog == that.actualDialog &&
                played == that.played &&
                Objects.equals(charType, that.charType) &&
                Objects.equals(imagesFolder, that.imagesFolder) &&
                Objects.equals(image, that.image) &&
                Objects.equals(stats, that.stats) &&
                Arrays.equals(dialogs, that.dialogs) &&
                Arrays.equals(dialogSwitchers, that.dialogSwitchers);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(charType, imagesFolder, image, isEnemy, yCoord, xCoord, stats, actualDialog, played);
        result = 31 * result + Arrays.hashCode(dialogs);
        result = 31 * result + Arrays.hashCode(dialogSwitchers);
        return result;
    }
}
